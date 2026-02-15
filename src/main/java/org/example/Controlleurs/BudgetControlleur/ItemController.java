package org.example.Controller.BudgetController;

import javafx.scene.control.Alert;
import org.example.Model.Budget.Item;
import org.example.Model.Budget.Categorie;
import org.example.Service.BudgetService.ItemService;

public class ItemController {

    private ItemService itemService;

    public ItemController() {
        itemService = new ItemService();
    }

    /**
     * Crée un item avec validation des saisies et alertes JavaFX
     */
    public Item creerItem(String libelle, double montant, Categorie categorie) {

        // --------- CONTROLES DE SAISIE ---------
        if (libelle == null || libelle.trim().isEmpty()) {
            showError("Libellé obligatoire", "Le libellé de l'item est obligatoire !");
            return null;
        }

        if (montant < 0) {
            showError("Montant invalide", "Le montant de l'item ne peut pas être négatif !");
            return null;
        }

        if (categorie == null) {
            showError("Catégorie obligatoire", "L'item doit être associé à une catégorie !");
            return null;
        }

        // --------- CREATION DE L'OBJET ---------
        Item item = new Item();
        item.setLibelle(libelle);
        item.setMontant(montant);
        item.setCategorie(categorie);

        // --------- ENREGISTREMENT EN BASE VIA SERVICE ---------
        if (itemService.create(item)) {
            showSuccess("Succès", "Item créé avec succès !");
            return item;
        } else {
            showError("Erreur", "Erreur lors de la création de l'item en base !");
            return null;
        }
    }

    /**
     * Modification d'un item
     */
    public boolean modifierItem(Item item, String nouveauLibelle, double nouveauMontant) {
        if (item == null) {
            showError("Erreur", "Aucun item sélectionné !");
            return false;
        }

        if (nouveauLibelle == null || nouveauLibelle.trim().isEmpty()) {
            showError("Libellé obligatoire", "Le libellé de l'item est obligatoire !");
            return false;
        }

        if (nouveauMontant < 0) {
            showError("Montant invalide", "Le montant ne peut pas être négatif !");
            return false;
        }

        item.setLibelle(nouveauLibelle);
        item.setMontant(nouveauMontant);

        if (itemService.update(item)) {
            showSuccess("Succès", "Item modifié avec succès !");
            return true;
        } else {
            showError("Erreur", "Erreur lors de la modification de l'item !");
            return false;
        }
    }

    /**
     * Suppression d'un item
     */
    public boolean supprimerItem(Item item) {
        if (item == null) {
            showError("Erreur", "Aucun item sélectionné !");
            return false;
        }

        if (itemService.delete(item.getIdItem())) {
            showSuccess("Succès", "Item supprimé avec succès !");
            return true;
        } else {
            showError("Erreur", "Erreur lors de la suppression de l'item !");
            return false;
        }
    }

    // --------- Méthodes utilitaires pour alertes ---------
    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showSuccess(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
