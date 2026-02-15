package org.example.Controlleurs.BudgetControlleur;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.Model.Budget.Categorie;
import org.example.Service.BudgetService.BudgetService;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class CategorieCreateController implements Initializable {

    // Form Fields
    @FXML private TextField tfNomCategorie;
    @FXML private TextField tfBudgetPrevu;
    @FXML private TextField tfSeuilAlerte;

    // Preview Labels
    @FXML private Label lblPreviewPercentage;
    @FXML private Label lblPreviewStatut;

    private BudgetService BS;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        BS = new BudgetService();

        setupNumericValidation();
        setupPreviewListeners();
    }

    private void setupNumericValidation() {
        // Budget field - only numbers and decimal point
        tfBudgetPrevu.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*\\.?\\d*")) {
                tfBudgetPrevu.setText(oldValue);
            }
        });

        // Seuil field - only numbers and decimal point
        tfSeuilAlerte.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*\\.?\\d*")) {
                tfSeuilAlerte.setText(oldValue);
            }
        });
    }

    private void setupPreviewListeners() {
        // Update preview when budget or seuil changes
        tfBudgetPrevu.textProperty().addListener((obs, old, newVal) -> updatePreview());
        tfSeuilAlerte.textProperty().addListener((obs, old, newVal) -> updatePreview());
    }

    private void updatePreview() {
        try {
            String budgetText = tfBudgetPrevu.getText().trim();
            String seuilText = tfSeuilAlerte.getText().trim();

            if (budgetText.isEmpty() || seuilText.isEmpty()) {
                lblPreviewPercentage.setText("0%");
                lblPreviewStatut.setText("-");
                lblPreviewStatut.setStyle("-fx-text-fill: #64748b;");
                return;
            }

            double budget = Double.parseDouble(budgetText);
            double seuil = Double.parseDouble(seuilText);

            if (budget == 0) {
                lblPreviewPercentage.setText("N/A");
                lblPreviewStatut.setText("Budget invalide");
                lblPreviewStatut.setStyle("-fx-text-fill: #ef4444;");
                return;
            }

            double percentage = (seuil / budget) * 100;
            lblPreviewPercentage.setText(String.format("%.1f%%", percentage));

            // Set statut and color
            if (percentage < 30) {
                lblPreviewStatut.setText("🟢 OK");
                lblPreviewStatut.setStyle("-fx-text-fill: #22c55e;");
                lblPreviewPercentage.setStyle("-fx-text-fill: #22c55e;");
            } else if (percentage < 70) {
                lblPreviewStatut.setText("🟡 Attention");
                lblPreviewStatut.setStyle("-fx-text-fill: #f59e0b;");
                lblPreviewPercentage.setStyle("-fx-text-fill: #f59e0b;");
            } else {
                lblPreviewStatut.setText("🔴 Critique");
                lblPreviewStatut.setStyle("-fx-text-fill: #ef4444;");
                lblPreviewPercentage.setStyle("-fx-text-fill: #ef4444;");
            }

        } catch (NumberFormatException e) {
            lblPreviewPercentage.setText("0%");
            lblPreviewStatut.setText("-");
        }
    }

    @FXML
    private void createCategorie() {
        if (!validateInput()) {
            return;
        }

        try {
            String nom = tfNomCategorie.getText().trim();
            double budget = Double.parseDouble(tfBudgetPrevu.getText().trim());
            double seuil = Double.parseDouble(tfSeuilAlerte.getText().trim());



            Categorie categorie = new Categorie(nom, budget, seuil);

            if (BS.add(categorie)) {
                showSuccessAlert("Succès",
                        "La catégorie \"" + nom + "\" a été créée avec succès!");
                goBackToListe();
            } else {
                showErrorAlert("Erreur",
                        "Erreur lors de la création de la catégorie.");
            }

        } catch (NumberFormatException e) {
            showErrorAlert("Erreur de saisie",
                    "Veuillez entrer des nombres valides pour le budget et le seuil.");
        }
    }

    @FXML
    private void resetForm() {
        tfNomCategorie.clear();
        tfBudgetPrevu.clear();
        tfSeuilAlerte.clear();

        lblPreviewPercentage.setText("0%");
        lblPreviewStatut.setText("-");
        lblPreviewStatut.setStyle("-fx-text-fill: #64748b;");
        lblPreviewPercentage.setStyle("-fx-text-fill: #64748b;");

        // Remove error styles
        tfNomCategorie.getStyleClass().removeAll("error", "success");
        tfBudgetPrevu.getStyleClass().removeAll("error", "success");
        tfSeuilAlerte.getStyleClass().removeAll("error", "success");

        tfNomCategorie.requestFocus();
    }

    @FXML
    private void goBackToListe() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Budget/CategorieListeGUI.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) tfNomCategorie.getScene().getWindow();
            Scene scene = new Scene(root);

            stage.setScene(scene);
            stage.setTitle("Liste des Catégories");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showErrorAlert("Erreur", "Impossible de retourner à la liste.");
        }
    }

    private boolean validateInput() {
        StringBuilder errors = new StringBuilder();
        boolean isValid = true;

        // Validate nom
        if (tfNomCategorie.getText().trim().isEmpty()) {
            errors.append("- Le nom de la catégorie est obligatoire.\n");
            tfNomCategorie.getStyleClass().add("error");
            isValid = false;
        } else {
            tfNomCategorie.getStyleClass().remove("error");
            tfNomCategorie.getStyleClass().add("success");
        }

        // Validate budget
        if (tfBudgetPrevu.getText().trim().isEmpty()) {
            errors.append("- Le budget prévu est obligatoire.\n");
            tfBudgetPrevu.getStyleClass().add("error");
            isValid = false;
        } else {
            try {
                double budget = Double.parseDouble(tfBudgetPrevu.getText().trim());
                if (budget <= 0) {
                    errors.append("- Le budget doit être supérieur à 0.\n");
                    tfBudgetPrevu.getStyleClass().add("error");
                    isValid = false;
                } else {
                    tfBudgetPrevu.getStyleClass().remove("error");
                    tfBudgetPrevu.getStyleClass().add("success");
                }
            } catch (NumberFormatException e) {
                errors.append("- Le budget doit être un nombre valide.\n");
                tfBudgetPrevu.getStyleClass().add("error");
                isValid = false;
            }
        }

        // Validate seuil
        if (tfSeuilAlerte.getText().trim().isEmpty()) {
            errors.append("- Le seuil d'alerte est obligatoire.\n");
            tfSeuilAlerte.getStyleClass().add("error");
            isValid = false;
        } else {
            try {
                double seuil = Double.parseDouble(tfSeuilAlerte.getText().trim());
                double budget = tfBudgetPrevu.getText().isEmpty() ? 0 :
                        Double.parseDouble(tfBudgetPrevu.getText().trim());

                if (seuil <= 0) {
                    errors.append("- Le seuil doit être supérieur à 0.\n");
                    tfSeuilAlerte.getStyleClass().add("error");
                    isValid = false;
                } else if (seuil > budget) {
                    errors.append("- Le seuil doit être inférieur ou égal au budget.\n");
                    tfSeuilAlerte.getStyleClass().add("error");
                    isValid = false;
                } else {
                    tfSeuilAlerte.getStyleClass().remove("error");
                    tfSeuilAlerte.getStyleClass().add("success");
                }
            } catch (NumberFormatException e) {
                errors.append("- Le seuil doit être un nombre valide.\n");
                tfSeuilAlerte.getStyleClass().add("error");
                isValid = false;
            }
        }

        if (!isValid) {
            showErrorAlert("Erreur de validation", errors.toString());
        }

        return isValid;
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

    private void showWarningAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}