CREATE SCHEMA IF NOT EXISTS `chat` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE `chat`;

CREATE TABLE IF NOT EXISTS `files`
(
    `id`            BIGINT AUTO_INCREMENT PRIMARY KEY,
    `user_id`       BIGINT,
    `original_name` VARCHAR(255),
    `name`          VARCHAR(50)  NOT NULL,
    `path`          VARCHAR(255) NOT NULL,
    `url`           VARCHAR(255) NOT NULL,
    `content_type`  VARCHAR(21)  NOT NULL,
    `type`          VARCHAR(10)  NOT NULL,
    `extension`     VARCHAR(10)  NOT NULL,
    `size`          BIGINT       NOT NULL,
    `version`       INT          NOT NULL DEFAULT 0,
    `cdt`           TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `udt`           TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT `file_name_uq` UNIQUE (`name`)
) engine = InnoDB;

CREATE TABLE IF NOT EXISTS `users`
(
    `id`        BIGINT AUTO_INCREMENT PRIMARY KEY,
    `username`  VARCHAR(30),
    `email`     VARCHAR(255),
    `password`  VARCHAR(255),
    `phone`     VARCHAR(10),
    `status`    VARCHAR(255),
    `verified`  TINYINT               DEFAULT 0,
    `avatar_id` BIGINT,
    `version`   INT          NOT NULL DEFAULT 0,
    `cdt`       TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `udt`       TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT `user_username_uq` UNIQUE (`username`),
    CONSTRAINT `user_email_uq` UNIQUE (`email`),
    CONSTRAINT `user_avatar_id_fk` FOREIGN KEY (`avatar_id`) REFERENCES `files` (`id`) ON DELETE SET NULL
) engine = InnoDB;

ALTER TABLE `files`
    ADD CONSTRAINT `files_user_id_fk` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE;

CREATE TABLE IF NOT EXISTS `chat_rooms`
(
    `id`        BIGINT AUTO_INCREMENT PRIMARY KEY,
    `name`      VARCHAR(255),
    `type`      VARCHAR(255),
    `image_id`  BIGINT,
    `version`   INT          NOT NULL DEFAULT 0,
    `cdt`       TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `udt`       TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT `chat_rooms_image_id` FOREIGN KEY (`image_id`) REFERENCES `files` (`id`) ON DELETE SET NULL
) engine = InnoDB;

CREATE TABLE IF NOT EXISTS `conversation`
(
    `id`        BIGINT AUTO_INCREMENT PRIMARY KEY,
    `user_id`   BIGINT,
    `room_id`   BIGINT,
    `role`      VARCHAR(255),
    `version`   INT          NOT NULL DEFAULT 0,
    `cdt`       TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `udt`       TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT `conversation_user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE,
    CONSTRAINT `conversation_room_id` FOREIGN KEY (`room_id`) REFERENCES `chat_rooms` (`id`) ON DELETE CASCADE
) engine = InnoDB;

CREATE TABLE IF NOT EXISTS `conversation_request`
(
    `id`        BIGINT AUTO_INCREMENT PRIMARY KEY,
    `form_user` BIGINT,
    `to_user`   BIGINT,
    `room_id`   BIGINT,
    `status`    VARCHAR(255),
    `version`   INT          NOT NULL DEFAULT 0,
    `cdt`       TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `udt`       TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT `conversation_request_form_user` FOREIGN KEY (`form_user`) REFERENCES `users` (`id`) ON DELETE CASCADE,
    CONSTRAINT `conversation_request_to_user` FOREIGN KEY (`to_user`) REFERENCES `users` (`id`) ON DELETE CASCADE,
    CONSTRAINT `conversation_request_room_id` FOREIGN key (`room_id`) REFERENCES `chat_rooms` (`id`) ON DELETE CASCADE
) engine = InnoDB;

CREATE TABLE IF NOT EXISTS `send_message`
(
    `id`        BIGINT AUTO_INCREMENT PRIMARY KEY,
    `room_id`   BIGINT,
    `form_user` BIGINT,
    `message`   VARCHAR(255),
    `status`    VARCHAR(255),
    `type`    VARCHAR(255),
    `version`   INT          NOT NULL DEFAULT 0,
    `cdt`       TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `udt`       TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT `send_message_room_id` FOREIGN KEY (`room_id`) REFERENCES `chat_rooms` (`id`) ON DELETE CASCADE,
    CONSTRAINT `send_message_form_user` FOREIGN key (`form_user`) REFERENCES `users` (`id`) ON DELETE CASCADE
) engine = InnoDB;
