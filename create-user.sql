DROP USER IF EXISTS bilabonnement_user@localhost;
CREATE USER bilabonnement_user@localhost IDENTIFIED BY 'LilleNemKode';

    GRANT SELECT,INSERT,UPDATE, DELETE
        ON bilabonnement.*
        TO bilabonnement_user@localhost;

SELECT User, Host FROM mysql.user;
SHOW GRANTS FOR bilabonnement_user@localhost;