-- Drop foreign key constraints
ALTER TABLE `product` DROP FOREIGN KEY `fk_product_category`;

-- Drop table 'product'
DROP TABLE IF EXISTS `product`;
