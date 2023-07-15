package Application.Controllers;

import Application.Entities.Part;
import Application.Entities.Inventory;
import Application.Entities.Product;
import Application.utilities.NumberFormatter;
import Application.utilities.SimpleAlert;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
 * Controller class for the Add/Modify Product forms. Both forms reference the
 * same FXML document. The MainController determines which whether or not
 * products will be added or modified.
 */
public class ProductFormController implements Initializable {

    @FXML
    Label actionLabel;

    @FXML
    TextField idField,
            nameField,
            inventoryField,
            priceField,
            maxField,
            minField,
            partSearchField;

    @FXML
    TableView allPartsTable, associatedPartsTable;

    @FXML
    TableColumn<Part, Integer> partIdColumn, associatedPartIdColumn;
    @FXML
    TableColumn<Part, String> partNameColumn, associatedPartNameColumn;
    @FXML
    TableColumn<Part, Integer> partInventoryLevelColumn, associatedPartInventoryLevelColumn;
    @FXML
    TableColumn<Part, Double> partPriceColumn, associatedPartPriceColumn;

    FilteredList<Part> filteredParts = new FilteredList<>(Inventory.getAllParts());

    private int productId;
    private boolean modified = false;
    private final ObservableList<Part> associatedParts = FXCollections.observableArrayList();

    /**
     * Initializes certain FXML components after the scene is set. Sets
     * TextFields with default format options. Populates all tables and adds and
     * event handler to the search TextField.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        allPartsTable.setItems(filteredParts);
        allPartsTable.setPlaceholder(new Label("No Parts Listed"));
        partIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInventoryLevelColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        associatedPartsTable.setItems(associatedParts);
        associatedPartsTable.setPlaceholder(new Label("No Associated Parts Listed"));
        associatedPartIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        associatedPartNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        associatedPartInventoryLevelColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        associatedPartPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        partSearchField.textProperty().addListener((o, oldVal, newVal) -> {
            filteredParts.setPredicate((Part part) -> {
                if (newVal == null || newVal.isEmpty()) {
                    return true;
                } else if (part.getName().toLowerCase().contains(newVal.toLowerCase()) || String.valueOf(part.getId()).contains(newVal.toLowerCase())) {
                    return true;
                } else {
                    allPartsTable.setPlaceholder(new Label("No part Listed"));
                    return false;
                }

            });
        });

        inventoryField.setTextFormatter(NumberFormatter.integerFormatter());
        minField.setTextFormatter(NumberFormatter.integerFormatter());
        maxField.setTextFormatter(NumberFormatter.integerFormatter());
        priceField.setTextFormatter(NumberFormatter.doubleFormatter());
    }

    /**
     * Loads product attributes into TextFields. Called by MainController when a
     * product is to be modified. Sets the "modified" variable to true
     * which tells saveProduct that a product is being modified rather than
     * added.
     * 
     * RUNTIME ERROR
     * Had an error where the associated part would update without saving.
     * This is because lists are passed by reference rather than value.
     * That is the whole reason the associatedParts list is created then populated
     * with copies of all the parts associated with the product stored in the
     * inventory.
     *
     * @param selectedProduct The selected product on the main form.
     */
    public void setProduct(Product selectedProduct) {
        actionLabel.setText("Modify Product");
        idField.setText(String.valueOf(selectedProduct.getId()));
        nameField.setText(selectedProduct.getName());
        inventoryField.setText(String.valueOf(selectedProduct.getStock()));
        priceField.setText(String.valueOf(selectedProduct.getPrice()));
        maxField.setText(String.valueOf(selectedProduct.getMax()));
        minField.setText(String.valueOf(selectedProduct.getMin()));

        for (Part part : selectedProduct.getAllAssociatedParts()) {
            associatedParts.add(part);
        }
        associatedPartsTable.setItems(associatedParts);

        productId = Inventory.getAllProducts().indexOf(selectedProduct);
        modified = true;
    }

    /**
     * Adds a selected part in the allPartsTable to the associatdPartsTable.
     * Displays an error to the user whenever a part is not selected. Holds the
     * list in variable associatedParts which is used when saving the product.
     * Called when the "Add" Button is clicked.
     * 
     *
     * FUTURE ENHANCEMENT
     * Implementation of quantities on associated parts table.
     * Parts with identical IDs could have an extra attribute of quantity which
     * would reflect how many of each type of part are listed in the table.
     *  
     */
    public void addAssociatedPart() {
        
        Part selectedPart = (Part) allPartsTable.getSelectionModel().getSelectedItem();

        if (selectedPart == null) {
            SimpleAlert.showAlert("No part selected.", "No Part Selected");
        } else {
            associatedParts.add(selectedPart);
        }
    }

    /**
     * Removes a selected part from the assocatedPartsTable. Displays and error
     * to the user if no part is selected. 
     * Confirms if user actually wants to remove a part from the associatedParts list.
     * Called when the "Remove Associated Part" button is clicked.
     */
    public void removeAssociatedPart() {
        Part selectedPart = (Part) associatedPartsTable.getSelectionModel().getSelectedItem();

        if (selectedPart == null) {
            SimpleAlert.showAlert("No part selected.", "No Part Selected");
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText("Are you sure you want to disassociate this product?");
            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    associatedParts.remove(selectedPart);
                }
            });
        }
    }

    /**
     * Adds/Modifies a product to the inventory. Evaluates all TextFields for
     * completeness/correctness and and performs a few logical checks.
     * 
     * FUTURE ENHANCEMENT
     * Implement a way to submit by pressing enter.
     *
     * @param e The event object.
     * @throws IOException
     */
    public void saveProduct(ActionEvent e) throws IOException {
        String name = nameField.getText(), errorLog = "";
        int id = modified ? Integer.parseInt(idField.getText()) : NumberFormatter.generateId(),
                stock,
                min,
                max;
        double price;
        boolean empty = false;
        boolean valid = true;
        Product newProduct;

        /*
        Form Validation
        Tediously checking several possible cases
         */
        // Check for name not empty
        if (name.isEmpty()) {
            errorLog = errorLog + "Name cannot be empty\n\n";
            empty = true;
        }

        // Check for price is not left blank
        try {
            price = Double.parseDouble(priceField.getText());
        } catch (NumberFormatException error) {
            price = 0f;
            errorLog = errorLog + "Cost must be a valid number\n\n";
            empty = true;
        }

        // Check for stock is not left blank
        try {
            stock = Integer.parseInt(inventoryField.getText());
        } catch (NumberFormatException error) {
            stock = 0;
            errorLog = errorLog + "Inventory must be a valid number\n\n";
            empty = true;
        }

        // Check for minumum is not left blank
        try {
            min = Integer.parseInt(minField.getText());
        } catch (NumberFormatException error) {
            min = 0;
            errorLog = errorLog + "Minimum must be a valid number\n\n";
            empty = true;
        }

        // Check for max is not left blank
        try {
            max = Integer.parseInt(maxField.getText());
        } catch (NumberFormatException error) {
            max = 0;
            errorLog = errorLog + "Maximum must be a valid number\n\n";
            empty = true;
        }

        // Additional checks after to values are found present
        if (!empty) {
            if (stock > max) {
                errorLog = errorLog + "Current Inventory cannot be greater than maximum quantity\n\n";
                valid = false;
            }
            if (stock < min) {
                errorLog = errorLog + "Current Inventory cannot be less than minimum quantity\n\n";
                valid = false;
            }

            if (min > max) {
                errorLog = errorLog + "Minimum quantity cannot be greater than specified maximum\n\n";
                valid = false;
            }

            if (valid) {
                newProduct = new Product(id, name, price, stock, min, max);

                // Adds the parts held in the associatdPartsTable to the product added to the invnetory.
                for (Part part : associatedParts) {
                    newProduct.addAssociatedPart(part);
                }

                if (modified) {
                    Inventory.updateProduct(productId, newProduct);
                } else {
                    Inventory.addProduct(newProduct);
                }

                this.returnToMain(e);
            } else {
                SimpleAlert.showAlert(errorLog, "Validation Error");
            }
        } else {
            SimpleAlert.showAlert(errorLog, "Validation Error");
        }

    }

    /**
     * Cancels the creation/modification of a product. Returns to the Main form
     * which clears every field. Called when cancel button is clicked.
     *
     * FUTURE ENHANCEMENT
     * Confirm if a user wants to leave without saving.
     * If yes, then the product would be lost. Otherwise, it would just cancel
     * the cancellation. This would reduce potential user errors.
     * 
     * @param e The event object.
     * @throws IOException
     */
    public void cancelProduct(ActionEvent e) throws IOException {
        this.returnToMain(e);
    }

    /**
     * Returns the application to the main form. Sets the Stage scene and title.
     *
     * @param e The event source passed through another object.
     * @throws IOException
     */
    private void returnToMain(ActionEvent e) throws IOException {
        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        Scene scene = new Scene(FXMLLoader.load(getClass().getResource("../fxml/Main.fxml")));

        stage.setScene(scene);
        stage.setTitle("Inventory Management System");
        stage.show();
    }
}
