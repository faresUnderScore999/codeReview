package org.example.Controller.BudgetController;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.example.Model.Budget.Categorie;
import org.example.Service.BudgetService.BudgetService;

public class CategorieController {

    // 🔹 Services
    private final BudgetService budgetService = new BudgetService();
    private final ObservableList<Categorie> categorieList = FXCollections.observableArrayList();

    // 🔹 FXML (inputs)
    @FXML
    private TextField tfNomCategorie;

    @FXML
    private TextField tfBudgetPrevu;

    @FXML
    private TextField tfSeuilAlerte;

    // 🔹 TableView
    @FXML
    private TableView<Categorie> tableCategorie;

    @FXML
    private TableColumn<Categorie, String> colNom;

    @FXML
    private TableColumn<Categorie, Double> colBudget;

    @FXML
    private TableColumn<Categorie, Double> colSeuil;

    // ================= INIT =================
    @FXML
    public void initialize() {
        colNom.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getNomCategorie()));

        colBudget.setCellValueFactory(data ->
                new javafx.beans.property.SimpleObjectProperty<>(data.getValue().getBudgetPrevu()));

        colSeuil.setCellValueFactory(data ->
                new javafx.beans.property.SimpleObjectProperty<>(data.getValue().getSeuilAlerte()));

        loadData();
    }

    // ================= LOAD =================
    private void loadData() {
        categorieList.setAll(budgetService.ReadAll());
        tableCategorie.setItems(categorieList);
    }

    // ================= ADD =================
    @FXML
    private void ajouterCategorie() {

        if (!validerChamps()) return;

        Categorie c = new Categorie();
        c.setNomCategorie(tfNomCategorie.getText());
        c.setBudgetPrevu(Double.parseDouble(tfBudgetPrevu.getText()));
        c.setSeuilAlerte(Double.parseDouble(tfSeuilAlerte.getText()));

        budgetService.Add(c);
        loadData();
        clearFields();
    }

    // ================= DELETE =================
    @FXML
    private void supprimerCategorie() {
        Categorie selected = tableCategorie.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showAlert("Veuillez sélectionner une catégorie !");
            return;
        }

        budgetService.Delete(selected.getIdCategorie());
        loadData();
    }

    // ================= UPDATE =================
    @FXML
    private void modifierCategorie() {
        Categorie selected = tableCategorie.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showAlert("Veuillez sélectionner une catégorie !");
            return;
        }

        if (!validerChamps()) return;

        selected.setNomCategorie(tfNomCategorie.getText());
        selected.setBudgetPrevu(Double.parseDouble(tfBudgetPrevu.getText()));
        selected.setSeuilAlerte(Double.parseDouble(tfSeuilAlerte.getText()));

        budgetService.Update(selected);
        loadData();
    }

    // ================= VALIDATION =================
    private boolean validerChamps() {

        if (tfNomCategorie.getText().isEmpty()
                || tfBudgetPrevu.getText().isEmpty()
                || tfSeuilAlerte.getText().isEmpty()) {

            showAlert("Tous les champs sont obligatoires !");
            return false;
        }

        try {
            Double.parseDouble(tfBudgetPrevu.getText());
            Double.parseDouble(tfSeuilAlerte.getText());
        } catch (NumberFormatException e) {
            showAlert("Budget et seuil doivent être numériques !");
            return false;
        }

        return true;
    }

    private void clearFields() {
        tfNomCategorie.clear();
        tfBudgetPrevu.clear();
        tfSeuilAlerte.clear();
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setContentText(msg);
        alert.show();
    }
}
