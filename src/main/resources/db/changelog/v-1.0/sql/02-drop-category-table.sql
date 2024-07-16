-- Drop foreign key constraints
ALTER TABLE `category` DROP FOREIGN KEY `fk_category_parent`;

-- Drop table 'category'
DROP TABLE IF EXISTS `category`;
