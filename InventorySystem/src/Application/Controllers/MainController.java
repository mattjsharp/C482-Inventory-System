package Application.Controllers;

import Application.Entities.Inventory;
import Application.Entities.Part;
import Application.Entities.Product;
import Application.utilities.SimpleAlert;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/**
 * Controller for the Main form in the application. Contains controls to
 * navigate to different forms. Populates tables with items reflected in the
 * Inventory class and allows users to query results based off of provided
 * filters. Provides the capability to delete items from the Inventory through
 * the tables. Allows the user to exit the application through the Exit button.
 */
public class MainController implements Initializable {

    @FXML
    TextField partSearchField, productSearchField;

    @FXML
    TableView<Part> partsTable;
    @FXML
    TableColumn<Part, Integer> partIdColumn;
    @FXML
    TableColumn<Part, String> partNameColumn;
    @FXML
    TableColumn<Part, Integer> partInventoryLevelColumn;
    @FXML
    TableColumn<Part, Double> partPriceColumn;

    @FXML
    TableView<Product> productsTable;
    @FXML
    TableColumn<Product, Integer> productIdColumn;
    @FXML
    TableColumn<Product, String> productNameColumn;
    @FXML
    TableColumn<Product, Integer> productInventoryLevelColumn;
    @FXML
    TableColumn<Product, Double> productPriceColumn;

    FilteredList<Part> filteredParts = new FilteredList<>(Inventory.getAllParts());
    FilteredList<Product> filteredProducts = new FilteredList<>(Inventory.getAllProducts());

    /**
     * Initializes the controller class. Populates the tables and adds an event
     * listeners to the search fields.
     * 
     * FUTURE ENHANCEMENT
     * Implement a way to deselect parts after they are clicked on rather than just clicking on another one.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        partsTable.setItems(filteredParts);
        partsTable.setPlaceholder(new Label("No Parts Listed"));
        partIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInventoryLevelColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        productsTable.setItems(filteredProducts);
        productsTable.setPlaceholder(new Label("No Products Listed"));
        productIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        productNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        productInventoryLevelColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        productPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        partSearchField.textProperty().addListener((o, oldVal, newVal) -> {
            filteredParts.setPredicate((Part part) -> {
                if (newVal == null || newVal.isEmpty()) {
                    return true;
                } else if (part.getName().toLowerCase().contains(newVal.toLowerCase()) || String.valueOf(part.getId()).contains(newVal.toLowerCase())) {
                    return true;
                } else {
                    partsTable.setPlaceholder(new Label("No part Listed"));
                    return false;
                }

            });
        });

        productSearchField.textProperty().addListener((o, oldVal, newVal) -> {
            filteredProducts.setPredicate((Product product) -> {
                if (newVal == null || newVal.isEmpty()) {
                    return true;
                } else if (product.getName().toLowerCase().contains(newVal.toLowerCase()) || String.valueOf(product.getId()).contains(newVal.toLowerCase())) {
                    return true;
                } else {
                    productsTable.setPlaceholder(new Label("No product Listed"));
                    return false;
                }

            });
        });
    }

    /**
     * Takes the user to the Add part form.
     *
     * @param e The event object.
     * @throws IOException
     */
    public void addProductForm(ActionEvent e) throws IOException {
        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        Scene scene = new Scene(FXMLLoader.load(getClass().getResource("../fxml/ProductForm.fxml")));
        stage.setScene(scene);
        stage.setTitle("Add Product");
        stage.show();
    }

    /**
     * Takes the user to the Modify Part form. Communicates the the part form to
     * load a part into the TextFields before the scene is even set. Prompts the
     * user when no part is selected.
     *
     * @param e The event object.
     * @throws IOException
     */
    public void modifyProductForm(ActionEvent e) throws IOException {
        Product selectedProduct = productsTable.getSelectionModel().getSelectedItem();

        if (selectedProduct == null) {
            SimpleAlert.showAlert("No Product Selected", "No Product Selected");
        } else {
            Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/ProductForm.fxml"));
            Scene scene = new Scene(loader.load());
            ProductFormController controller = loader.getController();
            controller.setProduct(selectedProduct);
            stage.setScene(scene);
            stage.setTitle("Modify Product");
            stage.show();
        }
    }

    /**
     * Method to delete products. Tied to the Delete Product button. Waits for
     * confirmation to ensure products are correctly deleted. 
     * Denies deletion of any part that has parts associated with it.
     * Prompts the user if no product is selected.
     */
    public void deleteProduct() {
        Product selectedProduct = productsTable.getSelectionModel().getSelectedItem();

        if (selectedProduct == null) {
            SimpleAlert.showAlert("No Product Selected", "No Product Selected");
        } else {
            // Checking to see if there are any associated parts with the selected product.
            if (selectedProduct.getAllAssociatedParts().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation");
                alert.setHeaderText("Are you sure you want to delete this product?");
                alert.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        Inventory.deleteProduct(selectedProduct);
                    }
                });
            } else {
                SimpleAlert.showAlert("This product has parts associated with it.\n\n", "");
            }
        }
    }

    /**
     * Takes the user to the Add product form.
     *
     * @param e The event object.
     * @throws IOException
     */
    public void appPartForm(ActionEvent e) throws IOException {
        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        Scene scene = new Scene(FXMLLoader.load(getClass().getResource("../fxml/PartForm.fxml")));
        stage.setScene(scene);
        stage.setTitle("Add Part");
        stage.show();
    }

    /**
     * Takes the user to the Modify Product form. Communicates the the product
     * form to load a product into the TextFields before the scene is even set.
     * Prompts the user when no product is selected.
     *
     * @param e The event object.
     * @throws IOException
     */
    public void modifyPartForm(ActionEvent e) throws IOException {
        Part selectedPart = partsTable.getSelectionModel().getSelectedItem();

        if (selectedPart == null) {
            SimpleAlert.showAlert("No Part Selected", "No Part Selected");
        } else {
            Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/PartForm.fxml"));
            Scene scene = new Scene(loader.load());
            PartFormController controller = loader.getController();
            controller.setPart(selectedPart);
            stage.setScene(scene);
            stage.setTitle("Modify Part");
            stage.show();
        }

    }

    /**
     * Method to delete parts. Tied to the Delete Part button. Waits for
     * confirmation to ensure parts are correctly deleted. Prompts the user if
     * no part is selected.
     */
    public void deletePart() {
        Part selectedPart = partsTable.getSelectionModel().getSelectedItem();

        if (selectedPart == null) {
            SimpleAlert.showAlert("No Part Selected", "No Part Selected");
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText("Are you sure you want to delete this part?");
            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    Inventory.deletePart(selectedPart);
                }
            });
        }
    }

    /**
     * Method to exit the program. Attached to the Exit button. Prompts the user
     * if either parts or products is not empty.
     */
    public void exitProgram() {
        if (!(Inventory.getAllParts().isEmpty() && Inventory.getAllProducts().isEmpty())) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText("Are you sure you want to exit?\n\nAll of your data will be deleted");
            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    Platform.exit();
                }
            });
        } else {
            Platform.exit();
        }
    }
}
