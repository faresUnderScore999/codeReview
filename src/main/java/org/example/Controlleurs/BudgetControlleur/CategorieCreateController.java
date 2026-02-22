package org.example.Controlleurs.BudgetControlleur;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.Model.Budget.Categorie;
import org.example.Service.BudgetService.BudgetService;

public class CategorieCreateController {

    @FXML
    private TextField tfNomCategorie;

    @FXML
    private TextField tfBudgetPrevu;

    @FXML
    private TextField tfSeuilAlerte;

    @FXML
    private Button btnAjouter;

    @FXML
    private Button btnAnnuler;

    private final BudgetService budgetService = new BudgetService();

    // ==============================
    // AJOUTER CATEGORIE
    // ==============================
    @FXML
    private void handleAjouter() {

        try {
            // 🔎 Vérification champs vides
            if (tfNomCategorie.getText().isEmpty() ||
                    tfBudgetPrevu.getText().isEmpty() ||
                    tfSeuilAlerte.getText().isEmpty()) {

                showAlert(Alert.AlertType.ERROR,
                        "Erreur",
                        "Tous les champs sont obligatoires !");
                return;
            }

            String nom = tfNomCategorie.getText().trim();
            double budget = Double.parseDouble(tfBudgetPrevu.getText().trim());
            double seuil = Double.parseDouble(tfSeuilAlerte.getText().trim());

            if (budget <= 0 || seuil <= 0) {
                showAlert(Alert.AlertType.ERROR,
                        "Erreur",
                        "Budget et seuil doivent être supérieurs à 0 !");
                return;
            }

            // 🟢 Création objet
            Categorie categorie = new Categorie();
            categorie.setNomCategorie(nom);
            categorie.setBudgetPrevu(budget);
            categorie.setSeuilAlerte(seuil);

            // 💾 Sauvegarde en base
            budgetService.Add(categorie);

            showAlert(Alert.AlertType.INFORMATION,
                    "Succès",
                    "Catégorie ajoutée avec succès !");

            closeWindow();

        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR,
                    "Erreur",
                    "Budget et seuil doivent être des nombres !");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR,
                    "Erreur",
                    "Une erreur est survenue !");
        }
    }

    // ==============================
    // ANNULER
    // ==============================
    @FXML
    private void handleAnnuler() {
        closeWindow();
    }

    // ==============================
    // UTILITAIRES
    // ==============================
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void closeWindow() {
        Stage stage = (Stage) btnAnnuler.getScene().getWindow();
        stage.close();
    }
}