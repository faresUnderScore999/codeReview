package org.example.Controlleurs.ProductControlleur;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.example.Interfaces.InterfaceGlobal;
import org.example.Model.Product.ClassProduct.Product;
import org.example.Model.Product.EnumProduct.ProductCategory;
import org.example.Service.ProductService.ProductService;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;

public class ListeProductControlleur implements Initializable {


    // TableView
    @FXML
    private TableView<Product> productTable;
    @FXML private TableColumn<Product, Integer> colProductId;
    @FXML private TableColumn<Product, ProductCategory> colCategory;
    @FXML private TableColumn<Product, Double> colPrice;
    @FXML private TableColumn<Product, String> colDescription;
    @FXML private TableColumn<Product, LocalDate> colCreatedAt;
    @FXML private TableColumn<Product, Void> colUpdate;
    @FXML private TableColumn<Product, Void> colDelete;

    @FXML private TextField searchField;
    @FXML private Label totalProductsLabel;

    private ObservableList<Product> productList = FXCollections.observableArrayList();
    private ObservableList<Product> filteredList = FXCollections.observableArrayList();
    private ProductService PS;


    private void loadProductData() {
        productList.clear();
        productList.addAll(PS.ReadAll());
        productTable.setItems(productList);
        filteredList.setAll(productList);
        productTable.setItems(filteredList);
        updateTotalLabel();
    }
    @FXML
    private void goToCreatePage(ActionEvent event) {

        try {
            Parent root = FXMLLoader.load(
                    getClass().getResource("/Product/CreateProductGUI.fxml")
            );

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Créer Produit");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        PS = new ProductService();

        setupTableColumns();
        setupActionButtons();
        loadProductData();
        setupSearchListener();
    }


    private void setupTableColumns() {
        colProductId.setCellValueFactory(new PropertyValueFactory<>("productId"));
        colCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colCreatedAt.setCellValueFactory(new PropertyValueFactory<>("createdAt"));


        // Format price column to show currency
        colPrice.setCellFactory(column -> new TableCell<Product, Double>() {
            @Override
            protected void updateItem(Double price, boolean empty) {
                super.updateItem(price, empty);
                if (empty || price == null) {
                    setText(null);
                } else {
                    setText(String.format("%.2f DT", price));
                }
            }
        });
        // Format category column with badges
        colCategory.setCellFactory(column -> new TableCell<Product, ProductCategory>() {
            @Override
            protected void updateItem(ProductCategory category, boolean empty) {
                super.updateItem(category, empty);
                if (empty || category == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(formatCategoryName(category.name()));
                    setStyle(getCategoryStyle(category.name()));
                }
            }
        });

        // Format description to show truncated text
        colDescription.setCellFactory(column -> new TableCell<Product, String>() {
            @Override
            protected void updateItem(String description, boolean empty) {
                super.updateItem(description, empty);
                if (empty || description == null) {
                    setText(null);
                    setTooltip(null);
                } else {
                    String truncated = description.length() > 40
                            ? description.substring(0, 40) + "..."
                            : description;
                    setText(truncated);

                    if (description.length() > 40) {
                        Tooltip tooltip = new Tooltip(description);
                        setTooltip(tooltip);
                    }
                }
            }
        });

    }

    private void setupActionButtons() {
        // Update Button Column
        colUpdate.setCellFactory(new Callback<TableColumn<Product, Void>, TableCell<Product, Void>>() {
            @Override
            public TableCell<Product, Void> call(TableColumn<Product, Void> param) {
                return new TableCell<Product, Void>() {
                    private final Button updateBtn = new Button("Modifier");

                    {
                        updateBtn.getStyleClass().add("btn-update");
                        updateBtn.setOnAction(event -> {
                            Product product = getTableView().getItems().get(getIndex());
                            handleUpdate(product);
                        });
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(updateBtn);
                            setAlignment(Pos.CENTER);
                        }
                    }
                };
            }
        });

        // Delete Button Column
        colDelete.setCellFactory(new Callback<TableColumn<Product, Void>, TableCell<Product, Void>>() {
            @Override
            public TableCell<Product, Void> call(TableColumn<Product, Void> param) {
                return new TableCell<Product, Void>() {
                    private final Button deleteBtn = new Button("Supprimer");

                    {
                        deleteBtn.getStyleClass().add("btn-delete");
                        deleteBtn.setOnAction(event -> {
                            Product product = getTableView().getItems().get(getIndex());
                            handleDelete(product);
                        });
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(deleteBtn);
                            setAlignment(Pos.CENTER);
                        }
                    }
                };
            }
        });
    }



    private void setupSearchListener() {
        if (searchField != null) {
            searchField.textProperty().addListener((observable, oldValue, newValue) -> {
                filterProducts(newValue);
            });
        }
    }

    private void filterProducts(String searchText) {
        if (searchText == null || searchText.isEmpty()) {
            filteredList.setAll(productList);
        } else {
            filteredList.clear();
            String lowerCaseFilter = searchText.toLowerCase();

            for (Product product : productList) {
                if (product.getCategory().name().toLowerCase().contains(lowerCaseFilter) ||
                        product.getDescription().toLowerCase().contains(lowerCaseFilter) ||
                        String.valueOf(product.getProductId()).contains(lowerCaseFilter)) {
                    filteredList.add(product);
                }
            }
        }
        updateTotalLabel();
    }

    private void updateTotalLabel() {
        if (totalProductsLabel != null) {
            int total = filteredList.size();
            totalProductsLabel.setText(String.format("Total: %d produit%s", total, total > 1 ? "s" : ""));
        }
    }

    @FXML
    private void handleUpdate(Product product) {
        // Show confirmation dialog or navigate to edit page
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Modification");
        alert.setHeaderText("Modifier le produit #" + product.getProductId());
        alert.setContentText("Voulez-vous modifier ce produit?\n\n" +
                "Catégorie: " + formatCategoryName(product.getCategory().name()) + "\n" +
                "Prix: " + String.format("%.2f €", product.getPrice()));

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // TODO: Navigate to edit page or open edit dialog
            // For now, just show info
            showInfoAlert("Information", "Fonctionnalité de modification à implémenter.\n" +
                    "Vous pouvez naviguer vers la page d'édition ici.");
        }
    }

    @FXML
    private void handleDelete(Product product) {
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirmation de suppression");
        confirmAlert.setHeaderText("Supprimer le produit #" + product.getProductId());
        confirmAlert.setContentText("Êtes-vous sûr de vouloir supprimer ce produit?\n\n" +
                "Catégorie: " + formatCategoryName(product.getCategory().name()) + "\n" +
                "Prix: " + String.format("%.2f €", product.getPrice()) + "\n\n" +
                "Cette action est irréversible!");

        // Style the alert
        DialogPane dialogPane = confirmAlert.getDialogPane();
        dialogPane.getStylesheets().add(getClass().getResource("/Styles/StyleProduct.css").toExternalForm());

        Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            if (PS.delete(product.getProductId())) {
                showSuccessAlert("Succès", "Le produit a été supprimé avec succès!");
                loadProductData(); // Refresh table
            } else {
                showErrorAlert("Erreur", "Erreur lors de la suppression du produit.");
            }
        }
    }

    @FXML
    private void handleSearch() {
        String searchText = searchField.getText();
        filterProducts(searchText);
    }

    @FXML
    private void handleReset() {
        searchField.clear();
        filteredList.setAll(productList);
        updateTotalLabel();
    }

    @FXML
    private void goToCreatePage() {
        // TODO: Navigate to create product page
        showInfoAlert("Navigation", "Navigation vers la page de création de produit.");
    }

    // Utility methods for formatting
    private String formatCategoryName(String category) {
        if (category == null) return "";
        return category.replace("_", " ");
    }

    private String getCategoryStyle(String category) {
        if (category == null) return "";

        if (category.startsWith("COMPTE")) {
            return "-fx-background-color: #e3f2fd; -fx-text-fill: #1976D2; " +
                    "-fx-background-radius: 12px; -fx-padding: 4px 12px; -fx-font-weight: bold;";
        } else if (category.startsWith("CARTE")) {
            return "-fx-background-color: #f3e5f5; -fx-text-fill: #7B1FA2; " +
                    "-fx-background-radius: 12px; -fx-padding: 4px 12px; -fx-font-weight: bold;";
        } else if (category.startsWith("EPARGNE") || category.contains("DEPOT") || category.contains("PLACEMENT")) {
            return "-fx-background-color: #e8f5e9; -fx-text-fill: #2e7d32; " +
                    "-fx-background-radius: 12px; -fx-padding: 4px 12px; -fx-font-weight: bold;";
        } else if (category.startsWith("ASSURANCE")) {
            return "-fx-background-color: #fff3e0; -fx-text-fill: #e65100; " +
                    "-fx-background-radius: 12px; -fx-padding: 4px 12px; -fx-font-weight: bold;";
        }

        return "-fx-background-color: #f5f5f5; -fx-text-fill: #666666; " +
                "-fx-background-radius: 12px; -fx-padding: 4px 12px; -fx-font-weight: bold;";
    }

    // Alert methods
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

    private void showInfoAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
