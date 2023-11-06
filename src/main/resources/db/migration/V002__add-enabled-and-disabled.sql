ALTER TABLE `login`.`user`
    ADD COLUMN `locked` BOOLEAN NOT NULL AFTER `birth_date`,
ADD COLUMN `enabled` BOOLEAN NOT NULL AFTER `locked`;
