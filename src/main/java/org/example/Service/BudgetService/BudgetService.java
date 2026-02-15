package org.example.Service.BudgetService;

import org.example.Interfaces.InterfaceGblobal;
import org.example.Model.Budget.Categorie;
import org.example.Model.Product.ClassProduct.Product;
import org.example.Model.Product.EnumProduct.ProductCategory;
import org.example.Utils.MaConnexion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BudgetService implements InterfaceGblobal<Categorie> {
    Connection cnx = MaConnexion.getInstance().getCnx();

    @Override
    public void add(Categorie c) {
        String req = "INSERT INTO `categorie`(`nomCategorie`, `budgetPrevu`, `seuilAlerte`) VALUES" +
                "('" + c.getNomCategorie() + "','" + c.getBudgetPrevu() + "','" + c.getSeuilAlerte() + "')";
        try {
            Statement st = cnx.createStatement();
            st.executeUpdate(req);
            System.out.println("Categorie ajoutee avec succes!");
        } catch (SQLException e) {
            throw new RuntimeException(e);

        }
    }

    @Override
    public void delete(Categorie c) {
        String req = "DELETE FROM `categorie` WHERE idCategorie = ?";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setInt(1, c.getIdCategorie());
            ps.executeUpdate();
            System.out.println("categorie Supprimer avec succes");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void update(Categorie c) {
        String req = "UPDATE `categorie` SET `nomCategorie`=?,`budgetPrevu`=?,`seuilAlerte`=? WHERE idCategorie = ?";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setString(1, c.getNomCategorie());
            ps.setDouble(2, c.getBudgetPrevu());
            ps.setDouble(3, c.getSeuilAlerte());
            ps.setInt(4, c.getIdCategorie());
            ps.executeUpdate();
            System.out.println("Categorie modifiée avec succes");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public List<Categorie> getAll() {
        List<Categorie> categories = new ArrayList<>();
        String req = "SELECT * FROM `categorie`";
        try {
            Statement st = cnx.createStatement();
            ResultSet res = st.executeQuery(req);
            while (res.next()) {
                Categorie c = new Categorie();
                c.setIdCategorie(res.getInt(1));
                c.setNomCategorie(res.getString(2));
                c.setBudgetPrevu(res.getDouble(3));
                c.setSeuilAlerte(res.getDouble(4));
                categories.add(c);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return categories;
    }
}
