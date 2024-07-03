-- Double 1er mois Admin --
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
        c.valeur AS commission_pourcentage,
        ROW_NUMBER() OVER (PARTITION BY l.id ORDER BY mc.mois) AS mois_numero
    FROM mois_complets mc
    JOIN location l ON mc.mois >= DATE_TRUNC('month', l.date_debut)
        AND mc.mois < DATE_TRUNC('month', l.date_debut + (l.duree || ' months')::INTERVAL)
    JOIN biens b ON l.idBiens = b.id
    JOIN type_biens tb ON b.idType = tb.id
    JOIN commission c ON tb.id = c.idType
)
SELECT 
    TO_CHAR(dates.mois, 'YYYY-MM-DD') AS mois,
    COALESCE(SUM(
        CASE 
            WHEN lm.mois_numero = 1 THEN lm.montant_mensuel * 2 
            ELSE lm.montant_mensuel 
        END
    ), 0) AS chiffre_affaires,
    COALESCE(SUM(
        CASE 
            WHEN lm.mois_numero = 1 THEN lm.montant_mensuel 
            ELSE lm.montant_mensuel * (lm.commission_pourcentage::float / 100) 
        END
    ), 0) AS commissions,
    COALESCE(SUM(
        CASE 
            WHEN lm.mois_numero = 1 THEN lm.montant_mensuel 
            ELSE lm.montant_mensuel * (1 - lm.commission_pourcentage::float / 100) 
        END
    ), 0) AS gains
FROM (
    SELECT GENERATE_SERIES('2022-01-01'::date, '2027-12-14'::date, '1 month'::interval) AS mois
) dates
LEFT JOIN locations_mensuelles lm ON dates.mois = lm.mois
GROUP BY dates.mois
ORDER BY dates.mois;
------------------------------------------------------------

--Proprio Double 1er Mois--
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
        b.id AS bien_id,
        b.nom AS biens,
        b.region AS bien_adresse,
        tb.nom AS bien_type,
        u.id AS utilisateur_id,
        u.username AS utilisateur_nom,
        COALESCE(
            (SELECT la.montant 
             FROM loyer_mensuel la
             WHERE la.idBiens = b.id
               AND la.date_changement <= l.date_debut
             ORDER BY la.date_changement DESC
             LIMIT 1),
            b.loyer
        ) AS montant_mensuel,
        c.valeur AS commission_pourcentage,
        ROW_NUMBER() OVER (PARTITION BY l.id ORDER BY mc.mois) AS row_num
    FROM mois_complets mc
    CROSS JOIN location l
    JOIN biens b ON l.idBiens = b.id
    JOIN type_biens tb ON b.idType = tb.id
    JOIN commission c ON tb.id = c.idType
    JOIN utilisateur u ON b.idProprio = u.id
    WHERE mc.mois >= DATE_TRUNC('month', l.date_debut)
      AND mc.mois < DATE_TRUNC('month', l.date_debut + (l.duree || ' months')::INTERVAL)
      AND u.profil = 'proprio'
)
SELECT 
    lm.utilisateur_id as idProprio,
    lm.utilisateur_nom as proprio,
    lm.bien_id as idBiens,
    lm.biens as biens,
    lm.bien_adresse as region,
    lm.bien_type as type,
    lm.location_id as idLocation,
    TO_CHAR(lm.mois, 'YYYY-MM-DD') AS date,
    lm.montant_mensuel AS ca,
    CASE 
        WHEN lm.row_num = 1 THEN 0
        ELSE lm.montant_mensuel * (lm.commission_pourcentage::float / 100)
    END AS co,
    CASE 
        WHEN lm.row_num = 1 THEN lm.montant_mensuel
        ELSE lm.montant_mensuel * (1 - lm.commission_pourcentage::float / 100)
    END AS gains
FROM locations_mensuelles lm
WHERE lm.utilisateur_id = 'USE00042' 
  AND lm.mois BETWEEN '2023-01-15' AND '2024-02-15'
ORDER BY lm.utilisateur_id, lm.bien_id, lm.location_id, lm.mois;
-----------------------------------------------------------------------------------------------------------
--Client Double 1er Mois--
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
        b.id AS bien_id,
        b.nom AS biens,
        b.region AS bien_adresse,
        tb.nom AS bien_type,
        u_proprio.id AS utilisateur_id,
        u_proprio.username AS utilisateur_nom,
        u_client.id AS client_id,
        u_client.username AS client_nom,
        COALESCE(
            (SELECT la.montant 
             FROM loyer_mensuel la
             WHERE la.idBiens = b.id
               AND la.date_changement <= l.date_debut
             ORDER BY la.date_changement DESC
             LIMIT 1),
            b.loyer
        ) AS montant_mensuel,
        c.valeur AS commission_pourcentage,
        ROW_NUMBER() OVER (PARTITION BY l.id ORDER BY mc.mois) AS row_num
    FROM mois_complets mc
    JOIN location l ON mc.mois >= DATE_TRUNC('month', l.date_debut)
        AND mc.mois < DATE_TRUNC('month', l.date_debut + (l.duree || ' months')::INTERVAL)
    JOIN biens b ON l.idBiens = b.id
    JOIN type_biens tb ON b.idType = tb.id
    JOIN commission c ON tb.id = c.idType
    JOIN utilisateur u_proprio ON b.idProprio = u_proprio.id
    JOIN utilisateur u_client ON l.idClient = u_client.id
    WHERE u_client.profil = 'user'
)
SELECT 
    lm.client_id AS idClient,
    lm.client_nom AS client,
    lm.utilisateur_id AS idProprio,
    lm.utilisateur_nom AS proprio,
    lm.bien_id AS idBiens,
    lm.biens AS biens,
    lm.bien_adresse AS region,
    lm.bien_type AS type,
    lm.location_id AS idLocation,
    TO_CHAR(lm.mois, 'YYYY-MM-DD') AS date,
    CASE
        WHEN lm.row_num = 1 THEN lm.montant_mensuel * 2
        ELSE lm.montant_mensuel
    END AS montant,
    CASE
        WHEN lm.mois <= DATE_TRUNC('month', CURRENT_DATE) THEN 'Payé'
        ELSE 'À payer'
    END AS statut_paiement
FROM locations_mensuelles lm
WHERE lm.client_id = 'USE00031'
  AND lm.mois BETWEEN DATE_TRUNC('month', '2020-03-15'::DATE) AND DATE_TRUNC('month', '2025-08-30'::DATE)
ORDER BY lm.utilisateur_id, lm.bien_id, lm.location_id, lm.mois;
---------------------------------------------------------------------------------------------------------------

--Disponibilite--
-----------Date exacte----------------
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
        b.id AS bien_id,
        l.date_debut,
        l.date_debut + (l.duree || ' months')::INTERVAL AS date_fin
    FROM mois_complets mc
    CROSS JOIN location l
    JOIN biens b ON l.idBiens = b.id
    JOIN utilisateur u ON b.idProprio = u.id
    WHERE mc.mois >= DATE_TRUNC('month', l.date_debut)
      AND mc.mois < DATE_TRUNC('month', l.date_debut + (l.duree || ' months')::INTERVAL)
      AND u.profil = 'proprio'
), derniere_location AS (
    SELECT 
        bien_id,
        MAX(date_fin) AS derniere_date_fin
    FROM locations_mensuelles
    GROUP BY bien_id
), dernier_loyer AS (
    SELECT DISTINCT ON (idBiens)
        idBiens,
        montant AS dernier_montant_loyer,
        date_changement AS date_dernier_changement_loyer
    FROM loyer_mensuel
    ORDER BY idBiens, date_changement DESC
)
SELECT DISTINCT
    b.id as idBiens,
    b.nom,
    b.description,
    b.region,
    b.loyer,
    b.idProprio,
    b.etat,
    b.idType,
    u.username as proprio,
    tb.nom as type_bien,
    dl.dernier_montant_loyer,
    dl.date_dernier_changement_loyer,
    TO_CHAR(DATE_TRUNC('month', COALESCE(dloc.derniere_date_fin + INTERVAL '1 day', CURRENT_DATE)), 'YYYY-MM-DD') AS date_disponibilite
FROM biens b
JOIN type_biens tb ON b.idType = tb.id
JOIN utilisateur u ON b.idProprio = u.id
LEFT JOIN derniere_location dloc ON b.id = dloc.bien_id
LEFT JOIN dernier_loyer dl ON b.id = dl.idBiens
WHERE u.profil = 'proprio'
ORDER BY b.idProprio, b.id;

--------Date du jour------------
CREATE OR REPLACE VIEW v_detail_biens_disponibilite AS
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
        b.id AS bien_id,
        l.date_debut,
        l.date_debut + (l.duree || ' months')::INTERVAL AS date_fin
    FROM mois_complets mc
    CROSS JOIN location l
    JOIN biens b ON l.idBiens = b.id
    JOIN utilisateur u ON b.idProprio = u.id
    WHERE mc.mois >= DATE_TRUNC('month', l.date_debut)
      AND mc.mois < DATE_TRUNC('month', l.date_debut + (l.duree || ' months')::INTERVAL)
      AND u.profil = 'proprio'
), derniere_location AS (
    SELECT 
        bien_id,
        MAX(date_fin) AS derniere_date_fin
    FROM locations_mensuelles
    GROUP BY bien_id
), dernier_loyer AS (
    SELECT DISTINCT ON (idBiens)
        idBiens,
        montant AS dernier_montant_loyer,
        date_changement AS date_dernier_changement_loyer
    FROM loyer_mensuel
    ORDER BY idBiens, date_changement DESC
)
SELECT DISTINCT
    b.id as idBiens,
    b.nom,
    b.description,
    b.region,
    b.loyer,
    b.idProprio,
    b.etat,
    b.idType,
    u.username as proprio,
    tb.nom as type_bien,
    dl.dernier_montant_loyer,
    dl.date_dernier_changement_loyer,
    TO_CHAR(
        GREATEST(
            DATE_TRUNC('month', COALESCE(dloc.derniere_date_fin + INTERVAL '1 day', CURRENT_DATE)),
            DATE_TRUNC('month', CURRENT_DATE)
        ),
        'YYYY-MM-DD'
    ) AS date_disponibilite
FROM biens b
JOIN type_biens tb ON b.idType = tb.id
JOIN utilisateur u ON b.idProprio = u.id
LEFT JOIN derniere_location dloc ON b.id = dloc.bien_id
LEFT JOIN dernier_loyer dl ON b.id = dl.idBiens
WHERE u.profil = 'proprio' AND idproprio = 'USE00028'
ORDER BY b.idProprio, b.id;
-----------------------------------------------------------------------

--Date exacte avec null----------------
CREATE OR REPLACE VIEW v_controlle_dispo AS
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
        b.id AS bien_id,
        l.date_debut,
        l.date_debut + (l.duree || ' months')::INTERVAL AS date_fin
    FROM mois_complets mc
    CROSS JOIN location l
    JOIN biens b ON l.idBiens = b.id
    JOIN utilisateur u ON b.idProprio = u.id
    WHERE mc.mois >= DATE_TRUNC('month', l.date_debut)
      AND mc.mois < DATE_TRUNC('month', l.date_debut + (l.duree || ' months')::INTERVAL)
      AND u.profil = 'proprio'
), derniere_location AS (
    SELECT 
        bien_id,
        MAX(date_fin) AS derniere_date_fin
    FROM locations_mensuelles
    GROUP BY bien_id
)
SELECT DISTINCT
    u.id as idProprio,
    u.username as proprio,
    b.id as idBiens,
    b.nom as biens,
    b.region as bien_adresse,
    tb.nom as type,
    CASE 
        WHEN dl.derniere_date_fin IS NOT NULL THEN 
            TO_CHAR(dl.derniere_date_fin + INTERVAL '1 day', 'YYYY-MM-DD')
        ELSE NULL
    END AS date_disponibilite
FROM biens b
JOIN type_biens tb ON b.idType = tb.id
JOIN utilisateur u ON b.idProprio = u.id
LEFT JOIN derniere_location dl ON b.id = dl.bien_id
WHERE u.profil = 'proprio'
ORDER BY u.id, b.id;
-------------------------------------------------------------------------------
--Propr loyer miova--
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
        b.id AS bien_id,
        b.nom AS biens,
        b.region AS bien_adresse,
        tb.nom AS bien_type,
        u.id AS utilisateur_id,
        u.username AS utilisateur_nom,
        l.date_debut,
        l.date_debut + (l.duree || ' months')::INTERVAL AS date_fin,
        COALESCE(
            (SELECT la.montant 
             FROM loyer_mensuel la
             WHERE la.idBiens = b.id
               AND DATE_TRUNC('month', la.date_changement) < mc.mois
             ORDER BY la.date_changement DESC
             LIMIT 1),
            b.loyer
        ) AS montant_mensuel,
        c.valeur AS commission_pourcentage,
        ROW_NUMBER() OVER (PARTITION BY l.id ORDER BY mc.mois) AS row_num
    FROM mois_complets mc
    CROSS JOIN location l
    JOIN biens b ON l.idBiens = b.id
    JOIN type_biens tb ON b.idType = tb.id
    JOIN commission c ON tb.id = c.idType
    JOIN utilisateur u ON b.idProprio = u.id
    WHERE mc.mois >= DATE_TRUNC('month', l.date_debut)
      AND mc.mois < DATE_TRUNC('month', l.date_debut + (l.duree || ' months')::INTERVAL)
      AND u.profil = 'proprio'
)
SELECT 
    lm.utilisateur_id as idProprio,
    lm.utilisateur_nom as proprio,
    lm.bien_id as idBiens,
    lm.biens as biens,
    lm.bien_adresse as region,
    lm.bien_type as type,
    lm.location_id as idLocation,
    TO_CHAR(lm.mois, 'YYYY-MM-DD') AS date,
    lm.montant_mensuel AS ca,
    CASE 
        WHEN lm.row_num = 1 THEN 0
        ELSE lm.montant_mensuel * (lm.commission_pourcentage::float / 100)
    END AS co,
    CASE 
        WHEN lm.row_num = 1 THEN lm.montant_mensuel
        ELSE lm.montant_mensuel * (1 - lm.commission_pourcentage::float / 100)
    END AS gains,
    lm.row_num
FROM locations_mensuelles lm
WHERE lm.utilisateur_id = 'USE00071' 
  AND lm.mois BETWEEN DATE_TRUNC('month', '2020-03-15'::DATE) AND DATE_TRUNC('month', '2026-08-30'::DATE)
ORDER BY lm.utilisateur_id, lm.bien_id, lm.location_id, lm.mois;
-------------------------------------------------------------------------------
--Admin loyer miova--
CREATE VIEW test as 
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
               AND DATE_TRUNC('month', la.date_changement) < mc.mois
             ORDER BY la.date_changement DESC
             LIMIT 1),
            b.loyer
        ) AS montant_mensuel,
        c.valeur AS commission_pourcentage,
        ROW_NUMBER() OVER (PARTITION BY l.id ORDER BY mc.mois) AS mois_numero
    FROM mois_complets mc
    JOIN location l ON mc.mois >= DATE_TRUNC('month', l.date_debut)
        AND mc.mois < DATE_TRUNC('month', l.date_debut + (l.duree || ' months')::INTERVAL)
    JOIN biens b ON l.idBiens = b.id
    JOIN type_biens tb ON b.idType = tb.id
    JOIN commission c ON tb.id = c.idType
)
SELECT 
    TO_CHAR(dates.mois, 'YYYY-MM-DD') AS mois,
    COALESCE(SUM(
        CASE 
            WHEN lm.mois_numero = 1 THEN lm.montant_mensuel * 2 
            ELSE lm.montant_mensuel 
        END
    ), 0) AS chiffre_affaires,
    COALESCE(SUM(
        CASE 
            WHEN lm.mois_numero = 1 THEN lm.montant_mensuel 
            ELSE lm.montant_mensuel * (lm.commission_pourcentage::float / 100) 
        END
    ), 0) AS commissions,
    COALESCE(SUM(
        CASE 
            WHEN lm.mois_numero = 1 THEN lm.montant_mensuel 
            ELSE lm.montant_mensuel * (1 - lm.commission_pourcentage::float / 100) 
        END
    ), 0) AS gains
FROM (
    SELECT GENERATE_SERIES('2024-01-13'::date, '2026-12-12'::date, '1 month'::interval) AS mois
) dates
LEFT JOIN locations_mensuelles lm ON dates.mois = lm.mois
GROUP BY dates.mois
ORDER BY dates.mois;

String sql = "WITH RECURSIVE mois_complets AS ( "
            +"     SELECT DATE_TRUNC('month', MIN(l.date_debut)) AS mois "
            +"     FROM location l "
            +"     UNION ALL "
            +"     SELECT mois + INTERVAL '1 month' "
            +"     FROM mois_complets "
            +"     WHERE mois < DATE_TRUNC('month', (SELECT MAX(l.date_debut + (l.duree || ' months')::INTERVAL) FROM location l)) "
            +" ), locations_mensuelles AS ( "
            +"     SELECT  "
            +"         mc.mois, "
            +"         l.id AS location_id, "
            +"         COALESCE( "
            +"             (SELECT la.montant  "
            +"              FROM loyer_mensuel la "
            +"              WHERE la.idBiens = b.id "
            +"                AND DATE_TRUNC('month', la.date_changement) < mc.mois "
            +"              ORDER BY la.date_changement DESC "
            +"              LIMIT 1), "
            +"             b.loyer "
            +"         ) AS montant_mensuel, "
            +"         c.valeur AS commission_pourcentage, "
            +"         ROW_NUMBER() OVER (PARTITION BY l.id ORDER BY mc.mois) AS mois_numero "
            +"     FROM mois_complets mc "
            +"     JOIN location l ON mc.mois >= DATE_TRUNC('month', l.date_debut) "
            +"         AND mc.mois < DATE_TRUNC('month', l.date_debut + (l.duree || ' months')::INTERVAL) "
            +"     JOIN biens b ON l.idBiens = b.id "
            +"     JOIN type_biens tb ON b.idType = tb.id "
            +"     JOIN commission c ON tb.id = c.idType "
            +" ) "
            +" SELECT  "
            +"     TO_CHAR(dates.mois, 'YYYY-MM-DD') AS mois, "
            +"     COALESCE(SUM( "
            +"         CASE  "
            +"             WHEN lm.mois_numero = 1 THEN lm.montant_mensuel * 2  "
            +"             ELSE lm.montant_mensuel  "
            +"         END "
            +"     ), 0) AS chiffre_affaires, "
            +"     COALESCE(SUM( "
            +"         CASE  "
            +"             WHEN lm.mois_numero = 1 THEN lm.montant_mensuel  "
            +"             ELSE lm.montant_mensuel * (lm.commission_pourcentage::float / 100)  "
            +"         END "
            +"     ), 0) AS commissions, "
            +"     COALESCE(SUM( "
            +"         CASE  "
            +"             WHEN lm.mois_numero = 1 THEN lm.montant_mensuel  "
            +"             ELSE lm.montant_mensuel * (1 - lm.commission_pourcentage::float / 100)  "
            +"         END "
            +"     ), 0) AS gains "
            +" FROM ( "
            +"     SELECT GENERATE_SERIES('2023-01-01'::date, '2024-12-31'::date, '1 month'::interval) AS mois "
            +" ) dates "
            +" LEFT JOIN locations_mensuelles lm ON dates.mois = lm.mois "
            +" GROUP BY dates.mois "
            +" ORDER BY dates.mois";
---------------------------------------------------------------------------------------

--Client loyer miova--
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
        b.id AS bien_id,
        b.nom AS biens,
        b.region AS bien_adresse,
        tb.nom AS bien_type,
        u_proprio.id AS utilisateur_id,
        u_proprio.username AS utilisateur_nom,
        u_client.id AS client_id,
        u_client.username AS client_nom,
        COALESCE(
            (SELECT la.montant 
             FROM loyer_mensuel la
             WHERE la.idBiens = b.id
               AND DATE_TRUNC('month', la.date_changement) < mc.mois
             ORDER BY la.date_changement DESC
             LIMIT 1),
            b.loyer
        ) AS montant_mensuel,
        c.valeur AS commission_pourcentage,
        ROW_NUMBER() OVER (PARTITION BY l.id ORDER BY mc.mois) AS row_num
    FROM mois_complets mc
    JOIN location l ON mc.mois >= DATE_TRUNC('month', l.date_debut)
        AND mc.mois < DATE_TRUNC('month', l.date_debut + (l.duree || ' months')::INTERVAL)
    JOIN biens b ON l.idBiens = b.id
    JOIN type_biens tb ON b.idType = tb.id
    JOIN commission c ON tb.id = c.idType
    JOIN utilisateur u_proprio ON b.idProprio = u_proprio.id
    JOIN utilisateur u_client ON l.idClient = u_client.id
    WHERE u_client.profil = 'user'
)
SELECT 
    lm.client_id AS idClient,
    lm.client_nom AS client,
    lm.utilisateur_id AS idProprio,
    lm.utilisateur_nom AS proprio,
    lm.bien_id AS idBiens,
    lm.biens AS biens,
    lm.bien_adresse AS region,
    lm.bien_type AS type,
    lm.location_id AS idLocation,
    TO_CHAR(lm.mois, 'YYYY-MM-DD') AS date,
    CASE
        WHEN lm.row_num = 1 THEN lm.montant_mensuel * 2
        ELSE lm.montant_mensuel
    END AS montant,
    CASE
        WHEN lm.mois <= DATE_TRUNC('month', CURRENT_DATE) THEN 'Payé'
        ELSE 'À payer'
    END AS statut_paiement,
    lm.row_num
FROM locations_mensuelles lm
WHERE lm.client_id = 'USE00075'
  AND lm.mois BETWEEN DATE_TRUNC('month', '2020-03-15'::DATE) AND DATE_TRUNC('month', '2026-08-30'::DATE)
ORDER BY lm.utilisateur_id, lm.bien_id, lm.location_id, lm.mois;










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
        b.id AS bien_id,
        b.nom AS biens,
        b.region AS bien_adresse,
        tb.nom AS bien_type,
        u.id AS utilisateur_id,
        u.username AS utilisateur_nom,
        l.date_debut,
        DATE_TRUNC('month', l.date_debut) AS debut_mois,
        l.date_debut + (l.duree || ' months')::INTERVAL AS date_fin,
        COALESCE(
            (SELECT la.montant 
             FROM loyer_mensuel la
             WHERE la.idBiens = b.id
               AND DATE_TRUNC('month', la.date_changement) < mc.mois
             ORDER BY la.date_changement DESC
             LIMIT 1),
            b.loyer
        ) AS montant_mensuel,
        c.valeur AS commission_pourcentage,
        ROW_NUMBER() OVER (PARTITION BY l.id ORDER BY mc.mois) AS row_num
    FROM mois_complets mc
    CROSS JOIN location l
    JOIN biens b ON l.idBiens = b.id
    JOIN type_biens tb ON b.idType = tb.id
    JOIN commission c ON tb.id = c.idType
    JOIN utilisateur u ON b.idProprio = u.id
    WHERE mc.mois >= DATE_TRUNC('month', l.date_debut)
      AND mc.mois < DATE_TRUNC('month', l.date_debut + (l.duree || ' months')::INTERVAL)
      AND u.profil = 'proprio'
)
SELECT 
    lm.utilisateur_id as idProprio,
    lm.utilisateur_nom as proprio,
    lm.bien_id as idBiens,
    lm.biens as biens,
    lm.bien_adresse as region,
    lm.bien_type as type,
    lm.location_id as idLocation,
    TO_CHAR(lm.mois, 'YYYY-MM-DD') AS date,
    TO_CHAR(lm.debut_mois, 'YYYY-MM-DD') AS date_debut,
    lm.montant_mensuel AS ca,
    CASE 
        WHEN lm.row_num = 1 THEN 0
        ELSE lm.montant_mensuel * (lm.commission_pourcentage::float / 100)
    END AS co,
    CASE 
        WHEN lm.row_num = 1 THEN lm.montant_mensuel
        ELSE lm.montant_mensuel * (1 - lm.commission_pourcentage::float / 100)
    END AS gains,
    lm.row_num as num
FROM locations_mensuelles lm
WHERE lm.utilisateur_id = 'USE00071' 
  AND lm.mois BETWEEN DATE_TRUNC('month', '2023-07-15'::DATE) AND DATE_TRUNC('month', '2024-07-30'::DATE)
ORDER BY lm.utilisateur_id, lm.bien_id, lm.location_id, lm.mois;