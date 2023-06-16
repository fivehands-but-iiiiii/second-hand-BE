insert into member (member_id, profile_img_url, oauth)
VALUES ('new-pow', 'https://avatars.githubusercontent.com/u/103120173?v=4', 'GITHUB'),
       ('Lily', 'https://avatars.githubusercontent.com/u/88878874?v=4', 'GITHUB'),
       ('NANII', 'https://avatars.githubusercontent.com/u/107349637?v=4', 'GITHUB'),
       ('irin', 'https://avatars.githubusercontent.com/u/103120173?v=4', 'GITHUB'),
       ('jaea-kim', 'https://ca.slack-edge.com/T74H5245A-U04FXMJ80BY-6ec5d36e2615-512', 'GITHUB'),
       ('HG-SONG', 'https://ca.slack-edge.com/T74H5245A-U04F3BARLB1-a572638186fe-512', 'GITHUB'),
       ('Haena', 'https://avatars.githubusercontent.com/u/97685264?v=4', 'GITHUB');

insert into region(id, city, county, district)
VALUES (1, '서울시', '성북구', '장위1동'),
       (2, '서울시', '중구', '동화동'),
       (3, '서울시', '노원구', '상계2동'),
       (4, '서울시', '강남구', '역삼동');

insert into based_region (member_id, region_id, represented)
VALUES (1, 1, true),
       (2, 2, true),
       (1, 2, false),
       (3, 1, true),
       (4, 2, true),
       (5, 4, true),
       (6, 4, true);
