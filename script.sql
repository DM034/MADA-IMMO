CREATE DATABASE immobilier;
DROP DATABASE immobilier;

\c immobilier

SET CLIENT_ENCODING TO 'UTF8';

CREATE TABLE admin (
    id serial primary key,
    username VARCHAR,
    password VARCHAR,
    profil VARCHAR
);

CREATE SEQUENCE seq_utilisateur START WITH 1;
CREATE TABLE utilisateur (
    id VARCHAR DEFAULT (LEFT('USE',3)||LPAD(nextval('seq_utilisateur')::text, 5,'0')),
    username VARCHAR,
    profil VARCHAR,
    primary key(id) 
);
ALTER TABLE utilisateur ADD CONSTRAINT unique_username UNIQUE (username);


CREATE SEQUENCE seq_type_biens START WITH 1;
CREATE TABLE type_biens (
    id VARCHAR DEFAULT (LEFT('TPB',3)||LPAD(nextval('seq_type_biens')::text, 5,'0')),
    nom VARCHAR,
    etat VARCHAR,
    primary key(id) 
);

CREATE SEQUENCE seq_biens START WITH 1;
CREATE TABLE biens (
    id VARCHAR DEFAULT (LEFT('BIE',3)||LPAD(nextval('seq_biens')::text, 5,'0')),
    nom VARCHAR,
    description VARCHAR,
    region VARCHAR,
    loyer FLOAT8,
    idProprio VARCHAR,
    etat VARCHAR,
    idType VARCHAR,
    FOREIGN KEY (idProprio) REFERENCES utilisateur(id),
    FOREIGN KEY (idType) REFERENCES type_biens(id),
    primary key(id) 
);

CREATE SEQUENCE seq_commission START WITH 1;
CREATE TABLE commission (
    id VARCHAR DEFAULT (LEFT('COM',3)||LPAD(nextval('seq_commission')::text, 5,'0')),
    idType VARCHAR,
    valeur VARCHAR,
    FOREIGN KEY (idType) REFERENCES type_biens(id),
    primary key(id) 
);

CREATE SEQUENCE seq_location START WITH 1;
CREATE TABLE location (
    id VARCHAR DEFAULT (LEFT('LOC',3)|| LPAD(nextval('seq_location')::text, 5, '0')),
    idBiens VARCHAR,
    idClient VARCHAR,
    duree FLOAT8,
    date_debut DATE,
    date_fin DATE GENERATED ALWAYS AS (date_debut + (duree * INTERVAL '1 month')) STORED,
    FOREIGN KEY (idBiens) REFERENCES biens(id),
    FOREIGN KEY (idClient) REFERENCES utilisateur(id),
    PRIMARY KEY (id)
);

CREATE SEQUENCE seq_photo START WITH 1;
CREATE TABLE photo (
    id VARCHAR DEFAULT (LEFT('PHT',3)||LPAD(nextval('seq_photo')::text, 5,'0')),
    idBiens VARCHAR,
    nom VARCHAR,
    FOREIGN KEY (idBiens) REFERENCES biens(id),
    primary key(id) 
);

CREATE SEQUENCE seq_paiement START WITH 1;
CREATE TABLE paiement(
    id VARCHAR DEFAULT (LEFT('PAI',3)||LPAD(nextval('seq_paiement')::text, 5,'0')),
    idLocation VARCHAR,
    valeur FLOAT8,
    date DATE,
    date_debut DATE,
    duree FLOAT8,
    FOREIGN KEY (idLocation) REFERENCES location(id),
    primary key(id) 
);

CREATE SEQUENCE seq_loyer_mensuel START WITH 1;
CREATE TABLE loyer_mensuel (
    id VARCHAR DEFAULT (LEFT('LYR',3)||LPAD(nextval('seq_loyer_mensuel')::text, 5,'0')),
    idBiens VARCHAR,
    montant FLOAT8,
    date_changement DATE,
    FOREIGN KEY (idBiens) REFERENCES biens(id),
    PRIMARY KEY (id)
);

WITH RECURSIVE mois_complets AS (
    SELECT DATE_TRUNC('month', MIN(l.date_debut)) AS mois
    FROM location l
    UNION ALL
    SELECT mois + INTERVAL '1 month'
    FROM mois_complets
    WHERE mois < DATE_TRUNC('month', (SELECT MAX(l.date_debut + (l.duree || ' months')::INTERVAL) FROM location l))
), locations_mensuelles AS (
    SELECT 
        mc.mois,
        l.id AS location_id,
        COALESCE(
            (SELECT la.montant 
             FROM loyer_mensuel la
             WHERE la.idBiens = b.id
               AND la.date_changement <= l.date_debut
             ORDER BY la.date_changement DESC
             LIMIT 1),
            b.loyer
        ) AS montant_mensuel,
        c.valeur AS commission_pourcentage
    FROM mois_complets mc
    CROSS JOIN location l
    JOIN biens b ON l.idBiens = b.id
    JOIN type_biens tb ON b.idType = tb.id
    JOIN commission c ON tb.id = c.idType
    WHERE mc.mois >= DATE_TRUNC('month', l.date_debut)
      AND mc.mois < DATE_TRUNC('month', l.date_debut + (l.duree || ' months')::INTERVAL)
)
SELECT 
    TO_CHAR(dates.mois, 'YYYY-MM-DD') AS mois,
    COALESCE(SUM(lm.montant_mensuel), 0) AS chiffre_affaires,
    COALESCE(SUM(lm.montant_mensuel * (lm.commission_pourcentage::float / 100)), 0) AS commissions,
    COALESCE(SUM(lm.montant_mensuel * (1 - lm.commission_pourcentage::float / 100)), 0) AS gains
FROM (
    SELECT GENERATE_SERIES('2024-01-01'::date, '2024-12-31'::date, '1 month'::interval) AS mois
) dates
LEFT JOIN locations_mensuelles lm ON dates.mois = lm.mois
GROUP BY dates.mois
ORDER BY dates.mois;




