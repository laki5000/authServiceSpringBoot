INSERT INTO `users` (`id`, `created_at`, `created_by`, `updated_at`, `updated_by`, `password`, `username`)
SELECT 1, '2024-10-10 17:05:00.840505', 'unknown', '2024-10-10 17:05:00.840505', NULL, '$2a$10$jUzPPxPF6LjItqN5ka9QbOYW5yKtB7MazMubzVb0sJ8x6zRn8yzwy', 'root'
WHERE NOT EXISTS (SELECT 1 FROM `users` WHERE `username` = 'root');