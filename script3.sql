select loc.id as idlocation, biens.id as idbiens, biens.nom, u.id as idclient, u.username, loc.duree, loc.date_debut, loc.date_fin from location loc
    join biens on biens.id = loc.idbiens
    join utilisateur u on u.id = loc.idclient;

CREATE OR REPLACE VIEW v_liste_locations AS
    select loc.id as idlocation, biens.id as idbiens, biens.nom, u.id as idclient, u.username, loc.duree, loc.date_debut, loc.date_fin from location loc
    join biens on biens.id = loc.idbiens
    join utilisateur u on u.id = loc.idclient;



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
    lm.row_num as num,
    CASE
        WHEN lm.row_num = 1 THEN lm.montant_mensuel * 2
        ELSE lm.montant_mensuel
    END AS montant,
    CASE
        WHEN lm.mois <= DATE_TRUNC('month', CURRENT_DATE) THEN 'Payé'
        ELSE 'À payer'
    END AS statut_paiement
FROM locations_mensuelles lm
WHERE lm.location_id = 'LOC00038' 
ORDER BY lm.utilisateur_id, lm.bien_id, lm.location_id, lm.mois;





CREATE OR REPLACE VIEW v_detail_location AS
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
    lm.commission_pourcentage,
    CASE 
        WHEN lm.row_num = 1 THEN lm.montant_mensuel
        ELSE lm.montant_mensuel * (1 - lm.commission_pourcentage::float / 100)
    END AS gains,
    lm.row_num as num,
    CASE
        WHEN lm.row_num = 1 THEN lm.montant_mensuel * 2
        ELSE lm.montant_mensuel
    END AS montant,
    CASE
        WHEN lm.mois <= DATE_TRUNC('month', CURRENT_DATE) THEN 'P'
        ELSE 'NP'
    END AS statut_paiement
FROM locations_mensuelles lm
ORDER BY lm.utilisateur_id, lm.bien_id, lm.location_id, lm.mois;
