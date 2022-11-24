DROP DATABASE IF EXISTS bilabonnement;

CREATE DATABASE bilabonnement;

USE bilabonnement;

CREATE TABLE kunder(
                       kunde_id int auto_increment,
                       kunde_fornavn varchar(50),
                       kunde_efternavn varchar(50),
                       kunde_kontaktnummer int,
                       kunde_email varchar(25),
                       PRIMARY KEY(kunde_id));

CREATE TABLE lejebiler(
                          vognnummer int,
                          stelnummer int,
                          fabrikant varchar(25),
                          model varchar(50),
                          udstyrspakke varchar(25),
                          lejepris double,
                          købspris double,
                          stålpris double,
                          co2_niveau double,
                          reg_afgift double,
                          status varchar(25),
                          PRIMARY KEY(vognnummer));

CREATE TABLE lejeaftaler(
                            kontrakt_id int auto_increment,
                            kunde_id int,
                            vognnummer int,
                            aftaletype varbinary(25),
                            kilometerpakke int,
                            startdato varchar(25),
                            slutdato varchar(25),
                            PRIMARY KEY(kontrakt_id),
                            FOREIGN KEY(kunde_id) REFERENCES kunder(kunde_id),
                            FOREIGN KEY(vognnummer) REFERENCES lejebiler(vognnummer));

CREATE TABLE skadesrapporter(
                                kontrakt_id int,
                                overkørte_kilometer int,
                                manglende_service boolean,
                                manglende_rengøring boolean,
                                manglende_dækskifte boolean,
                                lakfelt_skade int,
                                alufælg_skade int,
                                stenslag_skade int);

CREATE TABLE forhåndsaftale(
                               kontrakt_id int,
                               købspris double,
                               vilkår_periode varchar(50),
                               FOREIGN KEY(kontrakt_id) REFERENCES lejeaftaler(kontrakt_id));
