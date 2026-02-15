package org.example.Model.Budget;

import java.util.List;

public class Categorie {

    // Attributs
    private int idCategorie;
    private String nomCategorie;
    private double budgetPrevu;
    private double seuilAlerte;

    // Relation inverse
    private List<Item> items;

    // Constructeurs
    public Categorie() {}

    public Categorie(String nomCategorie, double budgetPrevu, double seuilAlerte) {
        this.nomCategorie = nomCategorie;
        this.budgetPrevu = budgetPrevu;
        this.seuilAlerte = seuilAlerte;
    }

    // Getters & Setters
    public int getIdCategorie() {
        return idCategorie;
    }

    public void setIdCategorie(int idCategorie) {
        this.idCategorie = idCategorie;
    }

    public String getNomCategorie() {
        return nomCategorie;
    }

    public void setNomCategorie(String nomCategorie) {
        this.nomCategorie = nomCategorie;
    }

    public double getBudgetPrevu() {
        return budgetPrevu;
    }

    public void setBudgetPrevu(double budgetPrevu) {
        this.budgetPrevu = budgetPrevu;
    }

    public double getSeuilAlerte() {
        return seuilAlerte;
    }

    public void setSeuilAlerte(double seuilAlerte) {
        this.seuilAlerte = seuilAlerte;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return "Categorie{" +
                "idCategorie=" + idCategorie +
                ", nomCategorie='" + nomCategorie + '\'' +
                ", budgetPrevu=" + budgetPrevu +
                ", seuilAlerte=" + seuilAlerte +
                '}';
    }
}
