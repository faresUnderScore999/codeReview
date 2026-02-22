package org.example.Controlleurs.BudgetControlleur;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.Model.Budget.Item;
import org.example.Service.BudgetService.ItemService;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class ItemListController implements Initializable {

    @FXML private ListView<Item> itemListView;
    @FXML private Label lblTotalItems;
    @FXML private Label lblMontantTotal;
    @FXML private TextField tfSearch;

    private ItemService itemService;
    private ObservableList<Item> items;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        itemService = new ItemService();
        loadItems();

        // Affiche uniquement le libelle dans la ListView
        itemListView.setCellFactory(lv -> new ListCell<Item>() {
            @Override
            protected void updateItem(Item item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getLibelle());
                }
            }
        });

        // Recherche en temps réel
        tfSearch.textProperty().addListener((obs, oldVal, newVal) -> filterItems(newVal));
    }

    // Charge tous les items depuis le service
    public void loadItems() {
        List<Item> itemList = itemService.ReadAll();
        items = FXCollections.observableArrayList(itemList);
        itemListView.setItems(items);
        updateStats(items);
    }

    // Met à jour le nombre d’items et le total
    private void updateStats(List<Item> itemList) {
        int totalItems = itemList.size();
        double totalMontant = itemList.stream().mapToDouble(Item::getMontant).sum();

        lblTotalItems.setText(String.valueOf(totalItems));
        lblMontantTotal.setText(String.format("%.2f DT", totalMontant));
    }

    // Filtrage par recherche
    private void filterItems(String query) {
        if (query == null || query.isEmpty()) {
            itemListView.setItems(items);
            updateStats(items);
            return;
        }

        List<Item> filtered = items.stream()
                .filter(item -> item.getLibelle().toLowerCase().contains(query.toLowerCase()) ||
                        item.getCategorie().getNomCategorie().toLowerCase().contains(query.toLowerCase()))
                .collect(Collectors.toList());

        itemListView.setItems(FXCollections.observableArrayList(filtered));
        updateStats(filtered);
    }

    @FXML
    private void resetSearch() {
        tfSearch.clear();
        itemListView.setItems(items);
        updateStats(items);
    }

    @FXML
    private void goToCreate() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Budget/ItemCreateGUI.fxml"));
            Parent root = loader.load();

            // Passer la référence de ce controller pour rafraîchir après création
            ItemCreateController createController = loader.getController();
            createController.setListController(this);

            Stage stage = (Stage) itemListView.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Créer un nouvel Item");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showErrorAlert("Erreur", "Impossible d'ouvrir le formulaire de création.");
        }
    }

    @FXML
    private void goBackToMenu() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Budget/MenuGUI.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) itemListView.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Menu Principal");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showErrorAlert("Erreur", "Impossible de revenir au menu.");
        }
    }

    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}