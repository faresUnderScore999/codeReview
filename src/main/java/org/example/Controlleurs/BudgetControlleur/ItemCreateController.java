package org.example.Controlleurs.BudgetControlleur;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import org.example.Model.Budget.Categorie;
import org.example.Model.Budget.Item;
import org.example.Service.BudgetService.ItemService;

public class ItemCreateController {

    @FXML private TextField tfLibelle;
    @FXML private TextField tfMontant;
    @FXML private ComboBox<Categorie> cbCategorie;

    private final ItemService itemService = new ItemService();

    // Référence vers le controller de la liste pour rafraîchir après ajout
    private ItemListController listController;

    public void setListController(ItemListController controller) {
        this.listController = controller;
    }

    @FXML
    private void enregistrer() {
        String libelle = tfLibelle.getText().trim();
        Categorie categorie = cbCategorie.getSelectionModel().getSelectedItem();
        double montant;

        if (libelle.isEmpty() || categorie == null || tfMontant.getText().trim().isEmpty()) {
            showErrorAlert("Erreur", "Tous les champs doivent être remplis !");
            return;
        }

        try {
            montant = Double.parseDouble(tfMontant.getText().trim());
        } catch (NumberFormatException e) {
            showErrorAlert("Erreur", "Montant invalide !");
            return;
        }

        Item item = new Item();
        item.setLibelle(libelle);
        item.setCategorie(categorie);
        item.setMontant(montant);

        itemService.Add(item);

        showSuccessAlert("Succès", "Item créé avec succès !");
        reinitialiser();

        if (listController != null) {
            listController.loadItems();
        }
    }

    @FXML
    private void reinitialiser() {
        tfLibelle.clear();
        tfMontant.clear();
        cbCategorie.getSelectionModel().clearSelection();
    }

    // Méthode pour bouton retour
    @FXML
    private void goBackToListe() {
        // TODO: ajouter la logique pour revenir à la liste des items
        // ex: fermer la fenêtre actuelle ou changer de scène
        reinitialiser();
    }

    private void showSuccessAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}