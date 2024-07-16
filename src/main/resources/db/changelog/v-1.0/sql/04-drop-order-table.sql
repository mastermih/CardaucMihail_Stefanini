-- Drop foreign key constraints
ALTER TABLE `order` DROP FOREIGN KEY `fk_order_user`;

-- Drop table 'order'
DROP TABLE IF EXISTS `order`;
