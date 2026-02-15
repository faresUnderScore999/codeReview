package org.example.Controller.BudgetController;

import javafx.scene.control.Alert;
import org.example.Model.Budget.Categorie;
import org.example.Service.BudgetService.CategorieService;

public class CategorieController {

    private CategorieService categorieService;

    public CategorieController() {
        categorieService = new CategorieService();
    }

    /**
     * Crée une catégorie avec validation des saisies et alertes JavaFX
     */
    public Categorie creerCategorie(String nomCategorie, double budgetPrevu, double seuilAlerte) {

        // --------- CONTROLES DE SAISIE ---------
        if (nomCategorie == null || nomCategorie.trim().isEmpty()) { //trim c enlever tous les espaces au debut et a la fin de la chaine
            showError("Nom obligatoire", "Le nom de la catégorie est obligatoire !");
            return null;
        }

        if (budgetPrevu < 0) {
            showError("Budget invalide", "Le budget prévu ne peut pas être négatif !");
            return null;
        }

        if (seuilAlerte < 0) {
            showError("Seuil invalide", "Le seuil d'alerte ne peut pas être négatif !");
            return null;
        }

        if (seuilAlerte > budgetPrevu) {
            showError("Seuil trop élevé", "Le seuil d'alerte ne peut pas dépasser le budget prévu !");
            return null;
        }

        // --------- CREATION DE L'OBJET ---------
        Categorie cat = new Categorie(nomCategorie, budgetPrevu, seuilAlerte);

        // --------- ENREGISTREMENT EN BASE VIA SERVICE ---------
        if (categorieService.create(cat)) {
            showSuccess("Succès", "Catégorie créée avec succès !");
            return cat;
        } else {
            showError("Erreur", "Erreur lors de la création de la catégorie en base !");
            return null;
        }
    }

    /**
     * Modification du budget prévu
     */
    public boolean modifierBudget(Categorie cat, double nouveauBudget) {
        if (cat == null) {
            showError("Erreur", "Aucune catégorie sélectionnée !");
            return false;
        }

        if (nouveauBudget < 0) {
            showError("Budget invalide", "Le budget ne peut pas être négatif !");
            return false;
        }

        if (cat.getSeuilAlerte() > nouveauBudget) {
            showError("Seuil trop élevé", "Le seuil d'alerte dépasse le nouveau budget !");
            return false;
        }

        cat.setBudgetPrevu(nouveauBudget);

        if (categorieService.update(cat)) {
            showSuccess("Succès", "Budget modifié avec succès !");
            return true;
        } else {
            showError("Erreur", "Erreur lors de la modification du budget !");
            return false;
        }
    }

    /**
     * Modification du seuil d'alerte
     */
    public boolean modifierSeuilAlerte(Categorie cat, double nouveauSeuil) {
        if (cat == null) {
            showError("Erreur", "Aucune catégorie sélectionnée !");
            return false;
        }

        if (nouveauSeuil < 0) {
            showError("Seuil invalide", "Le seuil d'alerte ne peut pas être négatif !");
            return false;
        }

        if (nouveauSeuil > cat.getBudgetPrevu()) {
            showError("Seuil trop élevé", "Le seuil d'alerte ne peut pas dépasser le budget !");
            return false;
        }

        cat.setSeuilAlerte(nouveauSeuil);

        if (categorieService.update(cat)) {
            showSuccess("Succès", "Seuil d'alerte modifié avec succès !");
            return true;
        } else {
            showError("Erreur", "Erreur lors de la modification du seuil !");
            return false;
        }
    }

    /**
     * Suppression d'une catégorie
     */
    public boolean supprimerCategorie(Categorie cat) {
        if (cat == null) {
            showError("Erreur", "Aucune catégorie sélectionnée !");
            return false;
        }

        if (categorieService.delete(cat.getIdCategorie())) {
            showSuccess("Succès", "Catégorie supprimée avec succès !");
            return true;
        } else {
            showError("Erreur", "Erreur lors de la suppression de la catégorie !");
            return false;
        }
    }

    // --------- Méthodes utilitaires pour alertes ---------
    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);//Alert est une fenêtre pop-up fournie par JavaFX indique qu'il ya une erreur
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showSuccess(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);//fenetre indiquant que la categorie ajoute avec succes
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
