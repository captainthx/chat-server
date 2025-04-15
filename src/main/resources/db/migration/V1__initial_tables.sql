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
    `title`      VARCHAR(255), -- for group
    `type`      VARCHAR(255), -- DIRECT , GROUP
    `creator_id` BIGINT NOT NULL,
    `last_message_id` BIGINT,
    `image_id`  BIGINT,
    `version`   INT          NOT NULL DEFAULT 0,
    `cdt`       TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `udt`       TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT `chat_rooms_creator_id_fk` FOREIGN KEY (`creator_id`) REFERENCES `users` (`id`) ON DELETE CASCADE,
    CONSTRAINT `chat_rooms_image_id_fk` FOREIGN KEY (`image_id`) REFERENCES `files` (`id`) ON DELETE SET NULL,
    INDEX idx_last_message_id (last_message_id)
) engine = InnoDB;


CREATE TABLE IF NOT EXISTS `room_members` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `room_id` BIGINT NOT NULL,
    `user_id` BIGINT NOT NULL,
    `role` VARCHAR(20) NOT NULL, -- 'ADMIN', 'MEMBER', 'FRIEND'
    `version` INT NOT NULL DEFAULT 0,
    `cdt` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `udt` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT `room_members_room_id_fk` FOREIGN KEY (`room_id`) REFERENCES `chat_rooms` (`id`) ON DELETE CASCADE,
    CONSTRAINT `room_members_user_id_fk` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE,
    CONSTRAINT `room_members_unique` UNIQUE (`room_id`, `user_id`)
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `invitations`
(
    `id`        BIGINT AUTO_INCREMENT PRIMARY KEY,
    `sender_id` BIGINT,
    `recipient_id`   BIGINT,
    `room_id`   BIGINT,
    `status`    VARCHAR(255) NOT NULL DEFAULT 'PENDING',-- PENDING, ACCEPT, REJECT
    `version`   INT          NOT NULL DEFAULT 0,
    `cdt`       TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `udt`       TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT `invitations_sender_id_fk` FOREIGN KEY (`sender_id`) REFERENCES `users` (`id`) ON DELETE CASCADE,
    CONSTRAINT `invitations_recipient_id_fk` FOREIGN KEY (`recipient_id`) REFERENCES `users` (`id`) ON DELETE CASCADE,
    CONSTRAINT `invitations_room_id_fk` FOREIGN key (`room_id`) REFERENCES `chat_rooms` (`id`) ON DELETE CASCADE
) engine = InnoDB;

CREATE TABLE IF NOT EXISTS `send_message`
(
    `id`        BIGINT AUTO_INCREMENT PRIMARY KEY,
    `room_id`   BIGINT NOT NULL,
    `sender_id` BIGINT NOT NULL,
    `reply_to_id` BIGINT ,
    `message`   TEXT NOT NULL,
    `status`    VARCHAR(255) NOT NULL, -- READ,UNREAD
    `type`    VARCHAR(255) NOT NULL, -- TEXT , FILE
    `attachment_id` BIGINT, -- for type file
    `version`   INT          NOT NULL DEFAULT 0,
    `cdt`       TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `udt`       TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT `send_message_room_id_fk` FOREIGN KEY (`room_id`) REFERENCES `chat_rooms` (`id`) ON DELETE CASCADE,
    CONSTRAINT `send_message_attachment_id_fk` FOREIGN KEY (`attachment_id`) REFERENCES `files` (`id`) ON DELETE CASCADE,
    CONSTRAINT `send_message_sender_id_fk` FOREIGN key (`sender_id`) REFERENCES `users` (`id`) ON DELETE CASCADE,
    CONSTRAINT `send_message_reply_to_id` FOREIGN key (`reply_to_id`) REFERENCES `send_message` (`id`) ON DELETE CASCADE,
    INDEX idx_room_id (room_id),
    INDEX idx_sender_id (sender_id),
    INDEX idx_reply_to_id (reply_to_id)
) engine = InnoDB;

ALTER  TABLE `chat_rooms`
 ADD CONSTRAINT chat_rooms_last_msg_fk FOREIGN KEY (`last_message_id`) REFERENCES send_message(`id`) ON DELETE SET NULL;
