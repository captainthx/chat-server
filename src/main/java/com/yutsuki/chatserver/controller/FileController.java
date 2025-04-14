package com.yutsuki.chatserver.controller;

import com.yutsuki.chatserver.common.BearerAuth;
import com.yutsuki.chatserver.exception.BaseException;
import com.yutsuki.chatserver.model.Result;
import com.yutsuki.chatserver.model.request.FileUploadRequest;
import com.yutsuki.chatserver.model.response.FileUploadResponse;
import com.yutsuki.chatserver.service.AuthService;
import com.yutsuki.chatserver.service.FileService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/files")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;
    private final AuthService authService;

    @Operation(
            summary = "Upload file",
            description = """
                    <h3>errors</h3><br>
                    <li><b>file.upload.exceeded</b>: The uploaded file size exceeds the limit (10MB)</li>
                    <li><b>file.upload.unsupported</b>: The uploaded file type is not supported (image/jpeg, image/png, audio/mpeg)</li>
                    <li><b>file.upload.failed</b>: File upload failed</li>
                    """
    )
    @BearerAuth
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Result<FileUploadResponse> upload(@ModelAttribute FileUploadRequest request) throws BaseException {
        return fileService.upload(authService.getUser(), request);
    }

    @Operation(summary = "Get file")
    @GetMapping(value = "/{fileName:.+}", produces = MediaType.ALL_VALUE)
    public ResponseEntity<UrlResource> loadAsResource(@PathVariable String fileName) {
        try {
            var response = fileService.loadAsResource(fileName);
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(response.getFile().getContentType()))
                    .body(response.getResource());
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Download file")
    @GetMapping(value = "/{fileName:.+}/download", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<UrlResource> download(@PathVariable String fileName) {
        try {
            var response = fileService.loadAsResource(fileName);
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + response.getFile().getName() + "\"")
                    .body(response.getResource());
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}