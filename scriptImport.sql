CREATE TABLE csv_location(
    id serial PRIMARY KEY,
    reference VARCHAR,
    date_debut VARCHAR,
    duree_mois FLOAT8,
    client VARCHAR
);

create table csv_biens(
    id serial PRIMARY KEY,
    reference VARCHAR,
    nom VARCHAR,
    description VARCHAR,
    type VARCHAR,
    region VARCHAR,
    loyer_mensuel FLOAT8,
    proprietaire VARCHAR
);

create table csv_commission(
    id serial PRIMARY KEY,
    type VARCHAR,
    commission VARCHAR
);

-- Proprio
select proprietaire from csv_biens group by proprietaire;
insert into utilisateur (username) select proprietaire from csv_biens group by proprietaire;
update utilisateur set profil = 'proprio'

-- User
select client from csv_location group by client;
insert into utilisateur (username) select client from csv_location group by client;
update utilisateur set profil = 'user' WHERE profil is null;

-- Type biens
select type from csv_biens group by type;
insert into type_biens (nom) select type from csv_biens group by type;

--Biens
select csv_biens.nom, csv_biens.description, csv_biens.region, csv_biens.loyer_mensuel, u.id as idProprio, t.id as idType, csv_biens.reference from csv_biens
    join utilisateur u on u.username = csv_biens.proprietaire
    join type_biens t on t.nom = csv_biens.type;
insert into biens (nom, description, region, loyer, idProprio, idType, reference) 
    select csv_biens.nom, csv_biens.description, csv_biens.region, csv_biens.loyer_mensuel, u.id as idProprio, t.id as idType, csv_biens.reference from csv_biens
    join utilisateur u on u.username = csv_biens.proprietaire
    join type_biens t on t.nom = csv_biens.type;

-- Commission
select type_biens.id, commission from csv_commission
    join type_biens on type_biens.nom = csv_commission.type;
insert into commission 
    select type_biens.id, commission from csv_commission
    join type_biens on type_biens.nom = csv_commission.type;

-- Location
select biens.id, u.id, csv.duree_mois, csv.date_debut from csv_location csv
    join biens on biens.reference = csv.reference
    join utilisateur u on u.username = csv.client;
insert into location (idBiens, idClient, duree, date_debut)
    select biens.id, u.id, csv.duree_mois, TO_DATE(csv.date_debut, 'YYYY-MM-DD') AS date_debut from csv_location csv
    join biens on biens.reference = csv.reference
    join utilisateur u on u.username = csv.client;

-- Loyer mensuel
select biens.id, csv_biens.loyer_mensuel, (select MIN(TO_DATE(date_debut, 'YYYY-MM-DD')) from csv_location) as date_changement from csv_biens
    join biens on biens.reference = csv_biens.reference;
insert into loyer_mensuel (idBiens, montant, date_changement)
    select biens.id, csv_biens.loyer_mensuel, (select MIN(TO_DATE(date_debut, 'YYYY-MM-DD')) from csv_location) as date_changement from csv_biens
        join biens on biens.reference = csv_biens.reference;

