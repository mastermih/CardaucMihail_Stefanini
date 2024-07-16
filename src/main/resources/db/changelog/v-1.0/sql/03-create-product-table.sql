-- Create table 'product'
CREATE TABLE `product` (
    `id` BIGINT NOT NULL,
    `category_id` BIGINT NOT NULL,
    `product_brand` VARCHAR(45) NULL,
    `product_name` VARCHAR(45) NULL,
    `electricity_consumption` DOUBLE NULL,
    `product_description` MEDIUMTEXT NULL,
    `product_width` DOUBLE NULL,
    `product_height` DOUBLE NULL,
    `product_depth` DOUBLE NULL,
    `price` INT NULL,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`category_id`) REFERENCES `category`(`id`)
);
