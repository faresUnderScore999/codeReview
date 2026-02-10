package org.example.Model.Product.ClassProduct;

import org.example.Model.Product.EnumProduct.ProductCategory;

import java.sql.Date;
import java.time.LocalDateTime;

public class Product {

    //Attributes
    private int productId;
    private ProductCategory category;
    private Double price;
    private String description;
    private LocalDateTime createdAt;


    //Constructors
    public Product() {
        this.createdAt = LocalDateTime.now();
    }

    public Product( ProductCategory category, String description, Double price) {
        this.category = category;
        this.description = description;
        this.createdAt = LocalDateTime.now();
        this.price = price;
    }
    public Product(int productId, ProductCategory category, String description, Double price) {
        this.productId = productId;
        this.category = category;
        this.description = description;
        this.createdAt = LocalDateTime.now();
        this.price = price;
    }


    //Getters & Setters

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public ProductCategory getCategory() {
        return category;
    }

    public void setCategory(ProductCategory category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Product{" +
                "productId=" + productId +
                ", category=" + category +
                ", price=" + price +
                ", description='" + description + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }


}
