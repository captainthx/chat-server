package com.yutsuki.chatserver.repository;

import com.yutsuki.chatserver.entity.File;
import com.yutsuki.chatserver.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FileRepository extends JpaRepository<File, Long> {

    Optional<File> findByName(String name);

    Optional<File> findByUrlAndUser(String url, User user);

    Optional<File> findByUrl(String url);

    Optional<File> findByIdAndUser(Long id, User user);

    List<File> findByUser(User user);
}