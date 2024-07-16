-- Create table 'category'
CREATE TABLE `category` (
    `id` BIGINT NOT NULL,
    `name` VARCHAR(50) NULL,
    `parent_id` BIGINT NULL,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`parent_id`) REFERENCES `category`(`id`)
);
