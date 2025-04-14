package com.yutsuki.chatserver.service;


import com.yutsuki.chatserver.entity.File;
import com.yutsuki.chatserver.entity.User;
import com.yutsuki.chatserver.exception.BaseException;
import com.yutsuki.chatserver.exception.FileException;
import com.yutsuki.chatserver.model.Result;
import com.yutsuki.chatserver.model.common.FileLoadAsResource;
import com.yutsuki.chatserver.model.request.FileUploadRequest;
import com.yutsuki.chatserver.model.response.FileUploadResponse;
import com.yutsuki.chatserver.repository.FileRepository;
import com.yutsuki.chatserver.utils.FileUtils;
import com.yutsuki.chatserver.utils.ResultUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Log4j2
@RequiredArgsConstructor
public class FileService {

    @Value("${api.files.directory}")
    private String fileDirectory;

    @Value("${api.files.supported-types}")
    private List<String> supportedFileTypes;

    @Value("${api.files.base-url}")
    private String fileBaseUrl;

    private final FileRepository fileRepository;

    public File saveFileToDisk(InputStream inputStream, String originalName, String contentType, long originalSize, User user) throws IOException {
        try {
            final String fileType = FileUtils.getType(contentType);
            final String fileExtension = FileUtils.getExtension(contentType);
            final String fileName = FileUtils.generateName(fileExtension);

            final Path uploadDirectory = Paths.get(fileDirectory, fileType, fileExtension);

            if (!Files.exists(uploadDirectory)) {
                log.debug("Upload-[next]:(create directory). uploadDirectory:{}", uploadDirectory);
                Files.createDirectories(uploadDirectory);
            }

            Path destinationFile = uploadDirectory.resolve(Paths.get(fileName)).normalize().toAbsolutePath();

            if (contentType.startsWith("image/")) {

                // กำหนดขนาดเป้าหมายไม่เกิน 500KB (512,017 bytes)
                long targetFileSize = 512017;

                if (originalSize > targetFileSize) {
                    double scaleFactor = Math.sqrt((double) targetFileSize / originalSize);

                    // ใช้ Thumbnails.of() จาก tempFile (หลีกเลี่ยงการใช้ BufferedImage)
                    Thumbnails.of(inputStream)
                            .scale(scaleFactor)
                            .outputFormat(fileExtension)
                            .toFile(destinationFile.toFile());
                } else {
                    // ถ้าขนาดไม่เกิน ไม่ต้องย่อ ให้คัดลอกไฟล์เดิมไปที่ปลายทาง
                    Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
                }
            } else {
                // กรณีที่ไฟล์ไม่ใช่รูปภาพ บันทึกไฟล์ตามปกติ
                Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
            }

            File file = new File();
            file.setUser(user);
            file.setOriginalName(Objects.nonNull(originalName) ? originalName : "Untitled");
            file.setName(fileName);
            file.setPath(destinationFile.toString());
            file.setUrl(fileBaseUrl + fileName);
            file.setContentType(contentType);
            file.setType(fileType);
            file.setExtension(fileExtension);
            file.setSize(Files.size(destinationFile));
            fileRepository.save(file);

            return file;
        } catch (IOException e) {
            log.error("SaveDisk-[unknown].", e);
            throw new IOException(e);
        }
    }

    public Result<FileUploadResponse> upload(User user, FileUploadRequest request) throws BaseException {
        MultipartFile file = request.getFile();

        try {
            if (file.isEmpty()) {
                log.warn("Upload-[block]:(file is empty). userId:{}, request:{}", user.getId(), request);
                throw FileException.uploadFailed();
            }

            if (!StringUtils.hasText(file.getContentType())) {
                log.warn("Upload-[block]:(file content type is empty). userId:{}, request:{}", user.getId(), request);
                throw FileException.uploadUnsupported();
            }

            if (!supportedFileTypes.contains(file.getContentType())) {
                log.warn("Upload-[block]:(file content type unsupported). userId:{}, request:{}", user.getId(), request);
                throw FileException.uploadUnsupported();
            }

            File savedFile = saveFileToDisk(file.getInputStream(), file.getOriginalFilename(), file.getContentType(), file.getSize(), user);

            FileUploadResponse response = new FileUploadResponse();
            response.setUrl(savedFile.getUrl());
            response.setType(savedFile.getType());
            response.setExtension(savedFile.getExtension());
            response.setSize(savedFile.getSize());

            return ResultUtils.success(response);
        } catch (IOException e) {
            log.error("Upload-[unknown]. userId:{}, request:{}", user.getId(), request, e);
            throw FileException.uploadFailed();
        }
    }


    public FileLoadAsResource loadAsResource(String fileName) throws BaseException {

        if (!StringUtils.hasText(fileName)) {
            log.warn("LoadAsResource-[block]:(file name is empty). fileName:{}", fileName);
            throw FileException.notFound();
        }

        Optional<File> fileOptional = fileRepository.findByName(fileName);
        if (fileOptional.isEmpty()) {
            log.warn("LoadAsResource-[block]:(file not found in table). fileName:{}", fileName);
            throw FileException.notFound();
        }

        File file = fileOptional.get();

        try {
            Path path = Paths.get(file.getPath());
            UrlResource resource = new UrlResource(path.toUri());
            if (resource.exists() || resource.isReadable()) {
                FileLoadAsResource response = new FileLoadAsResource();
                response.setFile(file);
                response.setResource(resource);
                return response;
            } else {
                throw FileException.notFound();
            }
        } catch (MalformedURLException e) {
            log.warn("LoadAsResource-[unknown].", e);
            throw FileException.notFound();
        }
    }

    public File save(File file) {
        file.setType(FileUtils.getType(file.getContentType()));
        file.setExtension(FileUtils.getExtension(file.getContentType()));
        file.setName(FileUtils.generateName(file.getExtension()));
        file.setUrl(fileBaseUrl + file.getName());
        return fileRepository.save(file);
    }

    public void delete(String filePath) {
        try {
            Path path = Paths.get(filePath);
            UrlResource resource = new UrlResource(path.toUri());
            if (resource.exists() || resource.isReadable()) {
                Files.delete(path);
            }
        } catch (Exception ignored) {
        }
    }

    public void delete(File file) {
        delete(file.getPath());
        fileRepository.delete(file);
    }

    public void deleteByIdAndUser(Long id, User user) {
        fileRepository.findByIdAndUser(id, user).ifPresent(this::delete);
    }

    public void deleteById(Long id) {
        fileRepository.findById(id).ifPresent(this::delete);
    }

    public File getByUrlAndUser(String url, User user) throws BaseException {
        Optional<File> fileOptional = fileRepository.findByUrlAndUser(url, user);
        if (fileOptional.isEmpty()) {
            log.warn("GetByUrlAndUser-[block]:(file not found) url:{}, userId:{}", url, user.getId());
            throw FileException.notFound();
        }
        return fileOptional.get();
    }
}
