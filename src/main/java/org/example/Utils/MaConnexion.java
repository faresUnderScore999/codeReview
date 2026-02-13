package org.example.Utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class MaConnexion {
    //DB
    final String URL = "jdbc:mysql://localhost:3306/PIDEV";
    final String USR = "root";
    final String PWD = "";

    //var
    Connection cnx;
    static MaConnexion instance;

    //Constructeur
    private MaConnexion(){
        try {
            cnx = DriverManager.getConnection(URL, USR, PWD);
            System.out.println("Connexion Etablie avec succes!");
            loadDatabase();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Connection getCnx() {
        return cnx;
    }

    public static MaConnexion getInstance() {
        if(instance == null)
            instance = new MaConnexion();
        return instance;
    }


    public void loadDatabase() {

        try {
            cnx = DriverManager.getConnection(URL, USR, PWD);

            try (Statement st = cnx.createStatement()) {

                // =============================
                // 1️⃣ PRODUCT TABLE
                // =============================
                String productTable = """
                CREATE TABLE IF NOT EXISTS product (
                  productId INT NOT NULL AUTO_INCREMENT,
                  category ENUM(
                    'COMPTE_COURANT',
                    'COMPTE_EPARGNE',
                    'COMPTE_PREMIUM',
                    'COMPTE_JEUNE',
                    'COMPTE_ENTREPRISE',
                    'CARTE_DEBIT',
                    'CARTE_CREDIT',
                    'CARTE_PREMIUM',
                    'CARTE_VIRTUELLE',
                    'EPARGNE_CLASSIQUE',
                    'EPARGNE_LOGEMENT',
                    'DEPOT_A_TERME',
                    'PLACEMENT_INVESTISSEMENT',
                    'ASSURANCE_VIE',
                    'ASSURANCE_HABITATION',
                    'ASSURANCE_VOYAGE'
                  ) NOT NULL DEFAULT 'COMPTE_COURANT',
                  price DOUBLE NOT NULL,
                  description VARCHAR(500) NOT NULL,
                  createdAt DATE NOT NULL,
                  PRIMARY KEY (productId)
                ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
                """;

                st.executeUpdate(productTable);
                // =============================
                // 2️⃣ PRODUCT SUBSCRIPTION TABLE
                // =============================
                String subscriptionTable = """
                CREATE TABLE IF NOT EXISTS productsubscription (
                  subscriptionId INT NOT NULL AUTO_INCREMENT,
                  client INT NOT NULL,
                  product INT NOT NULL,
                  type ENUM('MONTHLY', 'ANNUAL', 'TRANSACTION', 'ONE_TIME') NOT NULL,
                  subscriptionDate DATE NOT NULL,
                  expirationDate DATE NOT NULL,
                  status ENUM('DRAFT', 'ACTIVE', 'SUSPENDED', 'CLOSED') NOT NULL,
                  PRIMARY KEY (subscriptionId),
                  KEY fk_subscription_product (product),
                  CONSTRAINT fk_subscription_product
                    FOREIGN KEY (product)
                    REFERENCES product (productId)
                    ON DELETE CASCADE
                    ON UPDATE CASCADE
                ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
                """;

                st.executeUpdate(subscriptionTable);

                System.out.println("Tables checked/created successfully.");

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
