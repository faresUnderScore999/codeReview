package org.example.Service.BudgetService;

import org.example.Interfaces.InterfaceGlobal;
import org.example.Model.Budget.Item;
import org.example.Utils.MaConnexion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ItemService implements InterfaceGlobal<Item> {

    Connection cnx = MaConnexion.getInstance().getCnx();

    // ADD
    @Override
    public void Add(Item item) {
        String req = "INSERT INTO item(libelle, montant, idCategorie) VALUES (?, ?, ?)";
        try (PreparedStatement ps = cnx.prepareStatement(req)) {
            ps.setString(1, item.getLibelle());
            ps.setDouble(2, item.getMontant());
            ps.setInt(3, item.getCategorie().getIdCategorie()); // FIXED: get id from Categorie
            ps.executeUpdate(); // FIXED: actually execute the insert
            System.out.println("✅ Item ajouté avec succès !");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // DELETE
    @Override
    public void Delete(Integer id) {
        String req = "DELETE FROM item WHERE idItem = ?";
        try (PreparedStatement ps = cnx.prepareStatement(req)) {
            ps.setInt(1, id);
            ps.executeUpdate();
            System.out.println("✅ Item supprimé avec succès !");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // UPDATE
    @Override
    public void Update(Item item) {
        String req = "UPDATE item SET libelle=?, montant=?, idCategorie=? WHERE idItem=?";
        try (PreparedStatement ps = cnx.prepareStatement(req)) {
            ps.setString(1, item.getLibelle());
            ps.setDouble(2, item.getMontant());
            ps.setInt(3, item.getCategorie().getIdCategorie()); // FIXED
            ps.setInt(4, item.getIdItem());
            ps.executeUpdate();
            System.out.println("✅ Item modifié avec succès !");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // READ ALL
    @Override
    public List<Item> ReadAll() {
        List<Item> items = new ArrayList<>();
        String req = "SELECT * FROM item";
        try (Statement st = cnx.createStatement();
             ResultSet res = st.executeQuery(req)) {
            while (res.next()) {
                Item item = new Item();
                item.setIdItem(res.getInt("idItem"));
                item.setLibelle(res.getString("libelle"));
                item.setMontant(res.getDouble("montant"));
                // You may need to fetch Categorie object here if needed
                items.add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return items;
    }

    // READ BY ID
    @Override
    public Item ReadId(Integer id) {
        String req = "SELECT * FROM item WHERE idItem = ?";
        try (PreparedStatement ps = cnx.prepareStatement(req)) {
            ps.setInt(1, id);
            ResultSet res = ps.executeQuery();
            if (res.next()) {
                Item item = new Item();
                item.setIdItem(res.getInt("idItem"));
                item.setLibelle(res.getString("libelle"));
                item.setMontant(res.getDouble("montant"));
                // item.setCategorie(...) // set category if needed
                return item;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}