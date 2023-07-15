package Application.Controllers;

import Application.Entities.Part;
import Application.Entities.InHouse;
import Application.Entities.Inventory;
import Application.Entities.Outsourced;
import Application.utilities.NumberFormatter;
import Application.utilities.SimpleAlert;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Controller class for the Add/Modify Part forms. Both forms reference the same
 * FXML document. The MainController determines whether or not a part will be
 * added or modified.
 */
public class PartFormController implements Initializable {

    @FXML
    Label actionLabel, sourceLabel;

    @FXML
    RadioButton inHouseButton, outSourcedButton;

    @FXML
    TextField idField,
            nameField,
            inventoryField,
            priceField,
            maxField,
            minField,
            sourceField;

    private int partId;
    private boolean modified = false;

    /**
     * Initializes certain FXML components after the scene is set. Sets
     * TextFields with default format options.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        inventoryField.setTextFormatter(NumberFormatter.integerFormatter());
        minField.setTextFormatter(NumberFormatter.integerFormatter());
        maxField.setTextFormatter(NumberFormatter.integerFormatter());
        priceField.setTextFormatter(NumberFormatter.doubleFormatter());
        sourceField.setTextFormatter(NumberFormatter.integerFormatter());
    }

    /**
     * Loads part attributes into TextFields. Called by MainController
     * when a part is to be modified. Sets the "modified" boolean variable to
     * true which tells savePart method that a part is being modified
     * rather than added.
     *
     * RUNTIME ERROR
     * The set source method only runs whenever one of the Radio buttons would change.
     * This did not work when initially setting a part because the default option is
     * In-House. The solution was just to set the source whenever it is being loaded.
     * 
     * @param selectedPart The selected part on the main form.
     */
    public void setPart(Part selectedPart) {
        actionLabel.setText("Modify Part");
        idField.setText(String.valueOf(selectedPart.getId()));
        nameField.setText(selectedPart.getName());
        inventoryField.setText(String.valueOf(selectedPart.getStock()));
        priceField.setText(String.valueOf(selectedPart.getPrice()));
        maxField.setText(String.valueOf(selectedPart.getMax()));
        minField.setText(String.valueOf(selectedPart.getMin()));

        if (selectedPart instanceof Outsourced) {
            sourceLabel.setText("Company Name");
            Outsourced o = (Outsourced) selectedPart;
            outSourcedButton.setSelected(true);
            sourceField.setTextFormatter(null);
            sourceField.setText(o.getCompanyName());
        } else {
            sourceLabel.setText("Machine ID");
            InHouse iH = (InHouse) selectedPart;
            inHouseButton.setSelected(true);
            sourceField.setText(String.valueOf(iH.getMachineId()));
        }

        // part index is used later in Inventory.updatePart();
        partId = Inventory.getAllParts().indexOf(selectedPart);
        modified = true;

    }

    /**
     * Toggles between Machine ID and Company Name whenever one of the source
     * radio buttons are changed. It also changes the text formatter to be only
     * integers whenever the part is in-house.
     * 
     * RUNTIME ERROR
     * The difference between InHouse and Outsourced is that the last field in
     * Outsourced is a string (Company Name) and InHouse is an integer (Machine ID).
     * Whenever a string is passed as integer it causes an Exception to be raised. The
     * solution I devised was to clear the field and toggle the TextFormatter between
     * switches.
     * 
     * FUTURE ENHANCEMENT
     * Another way would be a way to hold the value of sourceField in a separate
     * variable so it would not have to be cleared each time between switching.
     * 
     */
    public void setSource() {
        sourceField.setText("");
        if (inHouseButton.isSelected()) {
            sourceLabel.setText("Machine ID");
            sourceField.setTextFormatter(NumberFormatter.integerFormatter());
        } else {
            sourceLabel.setText("Company Name");
            sourceField.setTextFormatter(null);
        }
    }

    /**
     * Adds/Modifies a part to the inventory. Evaluates all TextFields for
     * completeness/correctness and performs a few logical checks before making
     * a submission.
     *
     * FUTURE ENHANCEMENT
     * Implement a way to submit by pressing enter.
     * 
     * @param e The event object.
     * @throws IOException
     */
    public void savePart(ActionEvent e) throws IOException {        
        String name = nameField.getText(), companyName, errorLog = "";
        double price;
        int id = modified ? Integer.parseInt(idField.getText()) : NumberFormatter.generateId(),
                stock,
                max,
                min,
                machineId;
        boolean empty = false;
        boolean valid = true;
        Part newPart;

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

        // Check for max is not left blank
        try {
            max = Integer.parseInt(maxField.getText());
        } catch (NumberFormatException error) {
            max = 0;
            errorLog = errorLog + "Maximum must be a valid number\n\n";
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

            // If all fields are filled correctly the next action will be perfomed based off of weather or not the part is being modified.
            if (valid) {
                // Making machineId and comapnyName on optional field with a default value
                if (inHouseButton.isSelected()) {
                    try {
                        machineId = Integer.parseInt(sourceField.getText());
                    } catch (NumberFormatException error) {
                        machineId = 0;
                    }

                    newPart = new InHouse(id, name, price, stock, min, max, machineId);
                } else {
                    // Making Company have a defualt value of "NOT SPECIFIED" if left blank.
                    companyName = sourceField.getText();
                    if (companyName.isBlank()) {
                        companyName = "NOT SPECIFIED";
                    }
                    newPart = new Outsourced(id, name, price, stock, min, max, companyName);
                }

                if (modified) {
                    Inventory.updatePart(partId, newPart);
                } else {
                    Inventory.addPart(newPart);
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
     * Cancels the creation/modification of a part. Returns to the Main form
     * which clears every field. Called when cancel button is clicked.
     *
     * FUTURE ENHANCEMENT
     * Confirm if a user wants to leave without saving.
     * If yes, then the part would be lost. Otherwise, it would just cancel
     * the cancellation. This would reduce potential user errors.
     * 
     * @param e The event source.
     * @throws java.io.IOException
     */
    public void cancelPart(ActionEvent e) throws IOException {
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
