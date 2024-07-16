CREATE TABLE `order` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `user_id` BIGINT NOT NULL,
    `created_date` DATETIME NOT NULL,
    `updated_date` DATETIME NOT NULL,
    `order_status` VARCHAR(50) NOT NULL,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`user_id`) REFERENCES `user`(`id`)
);
