-- Import existing users from MySQL springdb.users
-- Passwords are BCrypt hashes, preserved so existing logins keep working.
INSERT INTO users (id, email, name, password, role, email_verified, pending_email, created_at) VALUES
(1, 'hayley@mail.com', 'Hayley', '$2a$10$OUTYyiY/baBQlT5zysT2N.6uiD/enGAU.Q1EZ7pva.1G04bAV4ASq', 'ROLE_USER', 0, NULL, '2026-08-07 02:53:47.000'),
(2, 'hannah@gmail.com', 'Hannah', '$2a$10$M5A5BOnrg6gEFbT8tkcsZu1NWs/Etdcmok/n.AdRiDCWFXQ8R4iCe', 'ROLE_USER', 0, NULL, '2026-08-07 02:53:47.000'),
(3, 'khairij@gmail.com', 'Khairi Juri', '$2a$10$0ZJvqFG3zzQCseuXRYYmhOa8uuUxa/Nk1KW/ENCgorCjguPDVCrui', 'ROLE_ADMIN', 1, NULL, '2026-08-07 02:53:47.000'),
(4, 'khairij@naver.com', 'khairi', '$2a$10$ZXjnyza2.upVgJAgWpBw4.DtB.vmhBSdeepanKK1/jJ3Yd9KAsUxK', 'ROLE_USER', 0, NULL, '2026-08-07 02:53:47.000'),
(6, 'khairij@outlook.com', 'kairi', '$2a$10$0ZJvqFG3zzQCseuXRYYmhOa8uuUxa/Nk1KW/ENCgorCjguPDVCrui', 'ROLE_USER', 0, NULL, '2026-08-07 02:53:47.000'),
(12, 'kylie@gmail.com', 'Kylie', '$2a$10$xg.D8FMaTk4JcA9UuhShYO9GU67s82OS/mmDRUjsiuGuT1TUDdMRC', 'ROLE_USER', 0, NULL, '2026-08-07 02:53:47.000'),
(171, 'khairij@protonmail.com', 'Administrator', '$2a$10$MNIOgO77WH2XFFDKrEn0HuCjS.sD7bp84vZHrkhf3mIQUKIDDsNb2', 'ROLE_ADMIN', 0, NULL, '2026-08-12 02:46:58.000');