package org.example.Service.ProductService;
import org.example.Interfaces.InterfaceGlobal;
import org.example.Model.Product.ClassProduct.Product;
import org.example.Model.Product.EnumProduct.ProductCategory;
import org.example.Utils.MaConnexion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductService implements InterfaceGlobal<Product> {

    Connection cnx = MaConnexion.getInstance().getCnx();
    @Override
    public void Add(Product p) {
        String req = "INSERT INTO `product`(`category`, `price`, `description`, `createdAt`) " +
                "VALUES ('"+p.getCategory()+"','"+p.getPrice()+"','"+p.getDescription()+"','"+p.getCreatedAt()+"')";
        try {
            Statement st = cnx.createStatement();
            st.executeUpdate(req);
            System.out.println("Produit ajoutee avec succes!");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    
    public boolean add(Product p) {
        String req = "INSERT INTO `product`(`category`, `price`, `description`, `createdAt`)" +
                " VALUES (?,?,?,?)";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setString(1, p.getCategory().name());
            ps.setDouble(2, p.getPrice());
            ps.setString(3, p.getDescription());
            ps.setTimestamp(4, Timestamp.valueOf(p.getCreatedAt()));
            ps.executeUpdate();
            System.out.println("Produit ajoutée avec succes 2");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

    }


    @Override
    public void Delete(Integer id) {
        String req = "DELETE FROM `product` WHERE productId = ?";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setInt(1, id);
            ps.executeUpdate();
            System.out.println("Produit Supprimer avec succes");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void Update(Product p) {
        String req = "UPDATE `product` SET category = ?, price=?, description = ?, createdAt = ? WHERE productId = ?";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setString(1, p.getCategory().name());
            ps.setDouble(2, p.getPrice());
            ps.setString(3, p.getDescription());
            ps.setTimestamp(4, Timestamp.valueOf(p.getCreatedAt()));
            ps.setInt(5, p.getProductId());
            ps.executeUpdate();
            System.out.println("Produit ajoutée avec succes 2");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }


    @Override
    public List<Product> ReadAll() {
        List<Product> products = new ArrayList<>();
        String req = "SELECT * FROM `product`";
        try {
            Statement st = cnx.createStatement();
            ResultSet res = st.executeQuery(req);
            while (res.next()){
                Product p =new Product();
                p.setProductId(res.getInt(1));
                p.setCategory(ProductCategory.valueOf(res.getString(2)));
                p.setPrice(res.getDouble(3));
                p.setDescription(res.getString(4));
                p.setCreatedAt(res.getTimestamp(5).toLocalDateTime());
                products.add(p);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return products;
    }


    @Override
    public Product ReadId(Integer id) {
        Product p = new Product();
        String req = "SELECT * FROM `product`  WHERE productId = ?";
        try {
            Statement st = cnx.createStatement();
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setInt(1, id);
            ResultSet res = st.executeQuery(req);
            while (res.next()){
                p.setProductId(res.getInt(1));
                p.setCategory(ProductCategory.valueOf(res.getString(2)));
                p.setPrice(res.getDouble(3));
                p.setDescription(res.getString(4));
                p.setCreatedAt(res.getTimestamp(5).toLocalDateTime());
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return p;
    }


}
