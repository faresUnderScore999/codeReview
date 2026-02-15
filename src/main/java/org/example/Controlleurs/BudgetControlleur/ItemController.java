package org.example.Controlleurs.BudgetControlleur;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.example.Model.Budget.Categorie;
import org.example.Model.Budget.Item;
import org.example.Service.BudgetService.BudgetService;
import org.example.Service.BudgetService.ItemService;

public class ItemController {

    // 🔹 Services
    private final ItemService itemService = new ItemService();
    private final BudgetService budgetService = new BudgetService();

    private final ObservableList<Item> itemList = FXCollections.observableArrayList();
    private final ObservableList<Categorie> categorieList = FXCollections.observableArrayList();

    // 🔹 Inputs
    @FXML
    private TextField tfLibelle;

    @FXML
    private TextField tfMontant;

    @FXML
    private ComboBox<Categorie> cbCategorie;

    // 🔹 Table
    @FXML
    private TableView<Item> tableItem;

    @FXML
    private TableColumn<Item, String> colLibelle;

    @FXML
    private TableColumn<Item, Double> colMontant;

    @FXML
    private TableColumn<Item, String> colCategorie;

    // ================= INIT =================
    @FXML
    public void initialize() {

        colLibelle.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getLibelle()));

        colMontant.setCellValueFactory(data ->
                new javafx.beans.property.SimpleObjectProperty<>(data.getValue().getMontant()));

        colCategorie.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(
                        data.getValue().getCategorie().getNomCategorie()));

        loadCategories();
        loadItems();
    }

    // ================= LOAD =================
    private void loadCategories() {
        categorieList.setAll(budgetService.ReadAll());
        cbCategorie.setItems(categorieList);
    }

    private void loadItems() {
        itemList.setAll(itemService.ReadAll());
        tableItem.setItems(itemList);
    }

    // ================= ADD =================
    @FXML
    private void ajouterItem() {

        if (!validerChamps()) return;

        Item item = new Item();
        item.setLibelle(tfLibelle.getText());
        item.setMontant(Double.parseDouble(tfMontant.getText()));
        item.setCategorie(cbCategorie.getValue());

        itemService.Add(item);
        loadItems();
        clearFields();
    }

    // ================= DELETE =================
    @FXML
    private void supprimerItem() {
        Item selected = tableItem.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showAlert("Veuillez sélectionner un item !");
            return;
        }

        itemService.Delete(selected.getIdItem());
        loadItems();
    }

    // ================= UPDATE =================
    @FXML
    private void modifierItem() {
        Item selected = tableItem.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showAlert("Veuillez sélectionner un item !");
            return;
        }

        if (!validerChamps()) return;

        selected.setLibelle(tfLibelle.getText());
        selected.setMontant(Double.parseDouble(tfMontant.getText()));
        selected.setCategorie(cbCategorie.getValue());

        itemService.Update(selected);
        loadItems();
    }

    // ================= VALIDATION =================
    private boolean validerChamps() {

        if (tfLibelle.getText().isEmpty()
                || tfMontant.getText().isEmpty()
                || cbCategorie.getValue() == null) {

            showAlert("Tous les champs sont obligatoires !");
            return false;
        }

        try {
            Double.parseDouble(tfMontant.getText());
        } catch (NumberFormatException e) {
            showAlert("Le montant doit être numérique !");
            return false;
        }

        return true;
    }

    private void clearFields() {
        tfLibelle.clear();
        tfMontant.clear();
        cbCategorie.setValue(null);
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setContentText(msg);
        alert.show();
    }
}
