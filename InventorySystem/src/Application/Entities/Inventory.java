package Application.Entities;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Stores data the entire life of the program. 
 * Contains methods for adding, updating, removing, and deleting parts/products.
 */
public class Inventory {

    private static final ObservableList<Part> allParts = FXCollections.observableArrayList();
    private static final ObservableList<Product> allProducts = FXCollections.observableArrayList();

    /**
     * Adds a new part to the Inventory
     * @param newPart The part to be added.
     */
    public static void addPart(Part newPart) {
        allParts.add(newPart);
    }

    /**
     * Adds a new product to the Inventory
     * @param newProduct The product to be added.
     */
    public static void addProduct(Product newProduct) {
        allProducts.add(newProduct);
    }

    /**
     * Returns a part in the inventory that matches the id the user entered.
     * @param partId
     * @return Part The part that matches the id the user entered.
     * @deprecated Not used. Part search boxes provide an easier way of filtering parts.
     */
    public static Part lookupPart(int partId) {
        for (Part part : allParts) {
            if (part.getId() == partId) {
                return part;
            }
        }

        return null;
    }

    /**
     * Retrieves an ObservableList populated with parts that have a partName matching a specified input string.
     * @returns newList A list of products that have names values containing the partName parameter.
     * @deprecated Not used. Part search boxes provide an easier way of filtering parts.
     * 
     */
    public static ObservableList<Part> lookupPart(String partName) {
        ObservableList<Part> newList = FXCollections.observableArrayList();
        for (Part part : allParts) {
            if (part.getName().toLowerCase().contains(partName.toLowerCase())) {
                newList.add(part);
            }
        }

        return newList;
    }

    /**
     * Returns a product in the inventory that matches the id the user entered.
     * @param productId
     * @return Product The Product that matches the id the user entered.
     * @deprecated Not used. Product search boxes provide an easier way of filtering products.
     */
    public static Product lookupProduct(int productId) {
        for (Product product : allProducts) {
            if (product.getId() == productId) {
                return product;
            }
        }

        return null;
    }

    /**
     * Retrieves an ObservableList populated with products that have a productName matching a specified input string.
     * @returns newList A list of products that have names values containing the productName parameter.
     * @deprecated Not used. Product search boxes provide an easier way of filtering products.
     */
    public static ObservableList<Product> lookupProduct(String productName) {
        ObservableList<Product> newList = FXCollections.observableArrayList();
        for (Product product : allProducts) {
            if (product.getName().toLowerCase().contains(productName.toLowerCase())) {
                newList.add(product);
            }
        }

        return newList;
    }

    /**
     * Updates a part already in the inventory that matches an id a user enters.
     * @param index The id of the part.
     * @param selectedPart The part that will be replacing the old one.
     */
    public static void updatePart(int index, Part selectedPart) {
        allParts.set(index, selectedPart);
    }

    /**
     * Updates a product already in the inventory that matches an id a user enters.
     * @param index The id of the product.
     * @param selectedPart The product that will be replacing the old one.
     */
    public static void updateProduct(int index, Product newProduct) {
        allProducts.set(index, newProduct);
    }

    /**
     * Deletes a part from the inventory.
     * 
     * @param selectedPart The part to be deleted.
     * @return boolean Returns true if operation is successful.
     */
    public static boolean deletePart(Part selectedPart) {
        return allParts.remove(selectedPart);
    }

    /**
     * Deletes a product from the inventory.
     * 
     * @param selectedProduct The product to be deleted.
     * @return boolean Returns true if operation is successful.
     */
    public static boolean deleteProduct(Product selectedProduct) {
        return allProducts.remove(selectedProduct);
    }

    /**
     * Getter method for retrieving a complete list of parts contained in the Inventory.
     * 
     * @return allParts An observableList of all parts in the inventory.
     */
    public static ObservableList<Part> getAllParts() {
        return allParts;
    }

    /**
     * Getter method for retrieving a complete list of products contained in the Inventory.
     * 
     * @return allProducts An observableList of all products in the inventory.
     */
    public static ObservableList<Product> getAllProducts() {
        return allProducts;
    }
}
