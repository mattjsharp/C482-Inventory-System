package Application.utilities;

import java.time.LocalDateTime;
import javafx.scene.control.TextFormatter;

/**
 * Class containing static methods that can be called by multiple controllers to
 * reduce unnecessary lines.
 * Makes use of a default constructor.
 */
public class NumberFormatter {
    /**
     * Returns a TextFormatter that only allows non-negative integers to be passed into
     * a TextFeild.
     * @return TextFormatter a text formatter containing a lambda function that checks if
     * user input matches a certain regex patten.
     */
    public static TextFormatter<Integer> integerFormatter() {
        return new TextFormatter<>(change -> {
            if (change.getControlNewText().matches("\\d*")) {
                return change;
            } else {
                return null;
            }
        });
    }

    /**
     * Returns a TextFormatter that only allows non-negative decimal point numbers to be passed into
     * a TextFeild.
     * @return TextFormatter a text formatter containing a lambda function that checks if
     * user input matches a certain regex patten.
     */
    public static TextFormatter<Double> doubleFormatter() {
        return new TextFormatter<>(change -> {
            if (change.getControlNewText().matches("\\d*(\\.\\d*)?")) {
                return change;
            } else {
                return null;
            }
        });
    }
    
    /**
     * Generates a number based off of the current date down to the second.
     * @return Integer A unique number based off of the date.
     */
    public static int generateId() {
        LocalDateTime date = LocalDateTime.now();
        String idSequence = String.valueOf(date.getYear()).substring(2) + String.valueOf(date.getDayOfYear()) + String.valueOf(date.getMinute()) + String.valueOf(date.getSecond());
        return Integer.parseInt(idSequence);
    }
}
