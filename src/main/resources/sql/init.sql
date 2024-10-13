INSERT INTO `users` (`id`, `created_at`, `created_by`, `updated_at`, `updated_by`, `password`, `username`)
SELECT 1, NOW(), 'system', NOW(), NULL, '$2a$10$jUzPPxPF6LjItqN5ka9QbOYW5yKtB7MazMubzVb0sJ8x6zRn8yzwy', 'root'
WHERE NOT EXISTS (SELECT 1 FROM `users` WHERE `username` = 'root');

INSERT INTO `roles` (`id`, `created_at`, `created_by`, `updated_at`, `updated_by`, `name`)
SELECT 1, NOW(), 'system', NOW(), NULL, 'USER'
WHERE NOT EXISTS (SELECT 1 FROM `roles` WHERE `name` = 'USER');

INSERT INTO `roles` (`id`, `created_at`, `created_by`, `updated_at`, `updated_by`, `name`)
SELECT 2, NOW(), 'system', NOW(), NULL, 'ADMIN'
WHERE NOT EXISTS (SELECT 1 FROM `roles` WHERE `name` = 'ADMIN');

INSERT INTO `users_roles` (`user_id`, `role_id`)
SELECT 1, 2
WHERE NOT EXISTS (SELECT 1 FROM `users_roles` WHERE `user_id` = 1 AND `role_id` = 2);

INSERT INTO `users_roles` (`user_id`, `role_id`)
SELECT 1, 1
WHERE NOT EXISTS (SELECT 1 FROM `users_roles` WHERE `user_id` = 1 AND `role_id` = 1);