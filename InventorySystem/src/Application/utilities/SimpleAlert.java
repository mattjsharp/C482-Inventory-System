package Application.utilities;

import javafx.scene.control.Alert;

/**
 * Class containing a static method that displays a simple alert to the user.
 * Can be used by multiple controllers to reduce unnecessary lines.
 * Makes use of a default constructor.
 */
public class SimpleAlert {
    /**
     * Displays a simple alert to the user.
     *
     * @param input The alert content.
     * @param title The alert title.
     */
    public static void showAlert(String input, String title) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(input);
        alert.showAndWait();
    }
}
