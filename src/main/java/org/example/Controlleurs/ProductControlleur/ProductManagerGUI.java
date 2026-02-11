package org.example.Controlleurs.ProductControlleur;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.example.Model.Product.ClassProduct.Product;
import org.example.Interfaces.InterfaceGlobal;
import org.example.Model.Product.EnumProduct.ProductCategory;
import org.example.Service.ProductService.ProductService;

public class ProductManagerGUI {

    // Form Fields
    @FXML
    private ComboBox<String> categoryComboBox;
    @FXML
    private TextField priceField;
    @FXML
    private TextArea descriptionArea;

    public void CreateProduct(ActionEvent actionEvent) {
        if (validateInput()) {
            try {
                ProductService ps=new ProductService();
                Product product = new Product();
                product.setCategory(ProductCategory.valueOf(categoryComboBox.getValue()));
                product.setPrice(Double.parseDouble(priceField.getText()));
                product.setDescription(descriptionArea.getText());

                if (ps.add(product)){
                    showAlert(Alert.AlertType.INFORMATION, "Succès",
                            "Le produit a été ajouté avec succès!");
                    handleClear();
                } else {
                    showAlert(Alert.AlertType.ERROR, "Erreur",
                            "Erreur lors de l'ajout du produit.");
                }
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.ERROR, "Erreur de saisie",
                        "Le prix doit être un nombre valide.");
            }
        }

    }

    private boolean validateInput() {
        StringBuilder errors = new StringBuilder();
        if (categoryComboBox.getValue() == null || categoryComboBox.getValue().isEmpty()) {
            errors.append("- Veuillez sélectionner une catégorie.\n");
        }
        if (priceField.getText().isEmpty()) {
            errors.append("- Le prix est obligatoire.\n");
        } else {
            try {
                double price = Double.parseDouble(priceField.getText());
                if (price < 0) {
                    errors.append("- Le prix doit être positif.\n");
                }
            } catch (NumberFormatException e) {
                errors.append("- Le prix doit être un nombre valide.\n");
            }
        }
        if (descriptionArea.getText().isEmpty()) {
            errors.append("- La description est obligatoire.\n");
        } else if (descriptionArea.getText().length() > 500) {
            errors.append("- La description ne doit pas dépasser 500 caractères.\n");
        }
        if (errors.length() > 0) {
            showAlert(Alert.AlertType.ERROR, "Erreur de validation", errors.toString());
            return false;
        }
        return true;
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    public void handleClear(ActionEvent actionEvent) {
        categoryComboBox.setValue(null);
        priceField.clear();
        descriptionArea.clear();
    }
    public void handleClear() {
        categoryComboBox.setValue(null);
        priceField.clear();
        descriptionArea.clear();
    }
}
