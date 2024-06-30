INSERT INTO admin VALUES(default, 'admin', 'admin', 'admin');

INSERT INTO utilisateur VALUES
(DEFAULT, '123', 'proprio'),
(DEFAULT, '234', 'proprio'),
(DEFAULT, 'client1@gmail.com', 'user'),
(DEFAULT, 'client2@gmail.com', 'user');

INSERT INTO type_biens VALUES
(DEFAULT, 'maison', 'ok'),
(DEFAULT, 'appartement', 'ok'),
(DEFAULT, 'villa', 'ok'),
(DEFAULT, 'immeuble', 'ok');

INSERT INTO biens VALUES
(DEFAULT, 'Biens1', 'description', 'region','500000','USE00001','occupe','TPB00001'),
(DEFAULT, 'Biens2', 'description', 'region','700000','USE00001','occupe','TPB00001'),
(DEFAULT, 'Biens3', 'description', 'region','750000','USE00002','occupe','TPB00001');

INSERT INTO commission VALUES
(default, 'TPB00001', 10),
(default, 'TPB00002', 12),
(default, 'TPB00003', 15),
(default, 'TPB00004', 15);

-- INSERT INTO location(id,idBiens,idClient,duree,date_debut) VALUES
-- (DEFAULT, 'BIE00001', 'USE00003', 2, '2024-08-01');

INSERT INTO location(id,idBiens,idClient,duree,date_debut) VALUES
(DEFAULT, 'BIE00001', 'USE00003', 5, '2024-03-01'),
(DEFAULT, 'BIE00002', 'USE00004', 9, '2024-02-09'),
(DEFAULT, 'BIE00003', 'USE00004', 7, '2024-04-25');

-- -- Location 1: LOC00001
-- INSERT INTO paiement (id, idLocation, valeur, date, date_debut, duree) VALUES
-- (DEFAULT, 'LOC00001', 500000, '2024-03-01', '2024-03-01', 1),
-- (DEFAULT, 'LOC00001', 500000, '2024-04-01', '2024-04-01', 1),
-- (DEFAULT, 'LOC00001', 500000, '2024-05-01', '2024-05-01', 1),
-- (DEFAULT, 'LOC00001', 500000, '2024-06-01', '2024-06-01', 1),
-- (DEFAULT, 'LOC00001', 500000, '2024-07-01', '2024-07-01', 1);

-- -- Location 2: LOC00002
-- INSERT INTO paiement (id, idLocation, valeur, date, date_debut, duree) VALUES
-- (DEFAULT, 'LOC00002', 700000, '2024-02-09', '2024-02-09', 1),
-- (DEFAULT, 'LOC00002', 700000, '2024-03-09', '2024-03-09', 1),
-- (DEFAULT, 'LOC00002', 700000, '2024-04-09', '2024-04-09', 1),
-- (DEFAULT, 'LOC00002', 700000, '2024-05-09', '2024-05-09', 1),
-- (DEFAULT, 'LOC00002', 700000, '2024-06-09', '2024-06-09', 1),
-- (DEFAULT, 'LOC00002', 700000, '2024-07-09', '2024-07-09', 1),
-- (DEFAULT, 'LOC00002', 700000, '2024-08-09', '2024-08-09', 1),
-- (DEFAULT, 'LOC00002', 700000, '2024-09-09', '2024-09-09', 1),
-- (DEFAULT, 'LOC00002', 700000, '2024-10-09', '2024-10-09', 1);

-- -- Location 3: LOC00003
-- INSERT INTO paiement (id, idLocation, valeur, date, date_debut, duree) VALUES
-- (DEFAULT, 'LOC00003', 750000, '2024-04-25', '2024-04-25', 1),
-- (DEFAULT, 'LOC00003', 750000, '2024-05-25', '2024-05-25', 1),
-- (DEFAULT, 'LOC00003', 750000, '2024-06-25', '2024-06-25', 1),
-- (DEFAULT, 'LOC00003', 750000, '2024-07-25', '2024-07-25', 1),
-- (DEFAULT, 'LOC00003', 750000, '2024-08-25', '2024-08-25', 1),
-- (DEFAULT, 'LOC00003', 750000, '2024-09-25', '2024-09-25', 1),
-- (DEFAULT, 'LOC00003', 750000, '2024-10-25', '2024-10-25', 1);

-- Assurez-vous que les tables et les séquences sont créées

-- Insertion de données fictives dans la table paiement
INSERT INTO paiement (id, idLocation, valeur, date, date_debut, duree) VALUES
(DEFAULT, 'LOC00001', 500000, '2024-03-01', '2024-03-01', 1),
(DEFAULT, 'LOC00002', 700000, '2024-02-09', '2024-02-09', 9),
(DEFAULT, 'LOC00003', 750000, '2024-04-25', '2024-04-25', 7);

-- Pour le bien BIE00001
INSERT INTO loyer_mensuel (idBiens, montant, date_changement) VALUES
('BIE00001', 500000, '2024-01-01'),
('BIE00002', 700000, '2024-01-01'),
('BIE00003', 750000, '2024-01-01');

-- Pour le bien BIE00002
INSERT INTO loyer_mensuel (idBiens, montant, date_changement) VALUES
('BIE00001', 550000, '2024-06-01'),
('BIE00002', 750000, '2024-07-01');















