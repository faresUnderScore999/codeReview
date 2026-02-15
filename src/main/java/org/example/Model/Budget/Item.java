package org.example.Model.Budget;

public class Item {

    // Attributs
    private int idItem;
    private String libelle;
    private double montant;
    private Categorie categorie;   // Relation objet

    // Constructeurs
    public Item() {}

    public Item(int idItem, String libelle, double montant, Categorie categorie) {
        this.idItem = idItem;
        this.libelle = libelle;
        this.montant = montant;
        this.categorie = categorie;
    }

    // Getters & Setters
    public int getIdItem() {
        return idItem;
    }

    public void setIdItem(int idItem) {
        this.idItem = idItem;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public double getMontant() {
        return montant;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }

    public Categorie getCategorie() {
        return categorie;
    }

    public void setCategorie(Categorie categorie) {
        this.categorie = categorie;
    }

    @Override
    public String toString() {
        return "Item{" +
                "idItem=" + idItem +
                ", libelle='" + libelle + '\'' +
                ", montant=" + montant +
                ", categorie=" + categorie +
                '}';
    }
}
