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
                          vognnummer int auto_increment,
                          stelnummer int,
                          fabrikant varchar(25),
                          model varchar(50),
                          udstyrspakke varchar(25),
                          lejepris double,
                          købspris double,
                          stålpris double,
                          co2_niveau double,
                          reg_afgift double,
                          lejebil_status varchar(25) default 'Ledig',
                          lejebil_farve varchar(25),
                          kilometerpakke int,
                          PRIMARY KEY(vognnummer));

CREATE TABLE lejeaftaler(
                            kontrakt_id int auto_increment,
                            kunde_id int,
                            vognnummer int,
                            aftaletype varchar(25),
                            startdato varchar(25),
                            slutdato varchar(25),
                            kontrakt_status varchar(25) DEFAULT 'igangværende',
                            PRIMARY KEY(kontrakt_id),
                            FOREIGN KEY(kunde_id) REFERENCES kunder(kunde_id),
                            FOREIGN KEY(vognnummer) REFERENCES lejebiler(vognnummer));

CREATE TABLE skadesrapporter(
                                rapport_id int auto_increment,
                                kontrakt_id int,
                                overkørte_kilometer int,
                                manglende_service boolean,
                                manglende_rengøring boolean,
                                manglende_dækskifte boolean,
                                lakfelt_skade int,
                                alufælg_skade int,
                                stenslag_skade int,
                                PRIMARY KEY(rapport_id),
                                FOREIGN KEY(kontrakt_id) REFERENCES lejeaftaler(kontrakt_id));

