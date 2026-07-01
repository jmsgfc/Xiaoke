SET NAMES utf8mb4;
USE `xiaoke_daily`;

DELETE FROM `photo`
WHERE `sort_order` BETWEEN 1900 AND 2010
  AND `created_at` < '2026-07-01 03:50:52';

DELETE FROM `memory_entry`
WHERE `sort_order` BETWEEN 490 AND 530
  AND `created_at` < '2026-07-01 03:50:52';

DELETE FROM `memory_entry`
WHERE `id` IN (83, 84, 85, 86, 87);
