INSERT INTO gift_certificate
VALUES (1, 'GiftCertificate1', 'description', 12, 123, '2023-01-04 22:06:14',
        '2023-01-04 22:06:14');
INSERT INTO gift_certificate
VALUES (2, 'GiftCertificate2', 'description', 12, 123, '2022-01-04 22:06:14',
        '2022-01-04 22:06:14');

INSERT INTO tag
VALUES (1, 'SomeName1');

INSERT INTO tag
VALUES (2, 'SomeName2');

INSERT INTO gift_certificate_tag
VALUES (1, 1, 1);

INSERT INTO gift_certificate_tag
VALUES (2, 1, 2);
