INSERT INTO members (email, profile_image_url, display_name, social_type, created_at, updated_at)
VALUES ('admin@email.com',
        'https://thumbs.dreamstime.com/b/admin-icon-trendy-design-style-isolated-white-background-vector-simple-modern-flat-symbol-web-site-mobile-logo-app-135742404.jpg',
        '어드민', 'GOOGLE', NOW(), NOW()),
       ('jhy979@gmail.com',
        'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSPVpbRmWveF_TpvoZWK8sjdBuX5DU2A2lP29_iWzkMTg&s',
        '귀요미 나인', 'GOOGLE', NOW(), NOW()),
       ('508yeah@gmail.com',
        'https://file.namu.moe/file/8bc9e381797334eb33da66e3ba501be13bacd109d2d9b5f117ad0bb781574b9cb612e8226803fc6831128bc2945e5f7d2231ed68ff43ae17d3aeb6bb6947b668',
        '티거', 'GOOGLE', NOW(), NOW()),
       ('dev.hyeonic@gmail.com',
        'https://file.namu.moe/file/8bc9e381797334eb33da66e3ba501be13bacd109d2d9b5f117ad0bb781574b9cb612e8226803fc6831128bc2945e5f7d2231ed68ff43ae17d3aeb6bb6947b668',
        '매트', 'GOOGLE', NOW(), NOW());

INSERT INTO categories (name, members_id, created_at, updated_at, personal)
VALUES ('공통 일정', 1, NOW(), NOW(), false),
       ('BE 일정', 1, NOW(), NOW(), false),
       ('FE 일정', 1, NOW(), NOW(), false),
       ('안드로이드 일정', 1, NOW(), NOW(), false),
       ('DevOps 일정', 1, NOW(), NOW(), false),
       ('내 일정', 2, NOW(), NOW(), true),
       ('내 일정', 3, NOW(), NOW(), true),
       ('운동', 2, NOW(), NOW(), true);

INSERT INTO subscriptions (members_id, categories_id, color, checked, created_at, updated_at)
VALUES (2, 1, 'COLOR_1', true, NOW(), NOW()),
       (2, 3, 'COLOR_2', true, NOW(), NOW()),
       (3, 1, 'COLOR_3', true, NOW(), NOW()),
       (3, 3, 'COLOR_4', true, NOW(), NOW()),
       (4, 1, 'COLOR_5', true, NOW(), NOW()),
       (4, 2, 'COLOR_6', true, NOW(), NOW());
