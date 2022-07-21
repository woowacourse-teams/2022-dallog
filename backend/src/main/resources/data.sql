INSERT INTO members (email, profile_image_url, display_name, social_type, created_at, updated_at)
VALUES ('admin@email.com',
        'https://thumbs.dreamstime.com/b/admin-icon-trendy-design-style-isolated-white-background-vector-simple-modern-flat-symbol-web-site-mobile-logo-app-135742404.jpg',
        '어드민', 'GOOGLE', NOW(), NOW()),
       ('jhy979@gmail.com',
        'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSPVpbRmWveF_TpvoZWK8sjdBuX5DU2A2lP29_iWzkMTg&s',
        '귀요미 나인', 'GOOGLE', NOW(), NOW()),
       ('508yeah@gmail.com',
        'https://file.namu.moe/file/8bc9e381797334eb33da66e3ba501be13bacd109d2d9b5f117ad0bb781574b9cb612e8226803fc6831128bc2945e5f7d2231ed68ff43ae17d3aeb6bb6947b668',
        '티거', 'GOOGLE', NOW(), NOW());

INSERT INTO categories (name, members_id, created_at, updated_at)
VALUES ('공통 일정', 1, NOW(), NOW()),
       ('BE 일정', 1, NOW(), NOW()),
       ('FE 일정', 1, NOW(), NOW()),
       ('안드로이드 일정', 1, NOW(), NOW()),
       ('DevOps 일정', 1, NOW(), NOW()),
       ('내 일정', 2, NOW(), NOW()),
       ('내 일정', 3, NOW(), NOW()),
       ('운동', 2, NOW(), NOW());

INSERT INTO subscriptions (members_id, categories_id, color, created_at, updated_at)
VALUES (2, 1, '#868e96', NOW(), NOW()),
       (2, 3, '#ffdeeb', NOW(), NOW()),
       (3, 1, '#d0bfff', NOW(), NOW()),
       (3, 3, '#4263eb', NOW(), NOW());
