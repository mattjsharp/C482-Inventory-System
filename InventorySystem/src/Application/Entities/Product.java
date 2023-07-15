package Application.Entities;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Template class for products. Parent class for both InHouse and Outsourced
 */
public class Product {
    private final ObservableList<Part> associatedParts = FXCollections.observableArrayList();
    private int id, stock, min, max;
    private String name;
    private double price;
    
    /**
     * Sets attributes with default values. Executed each time a new product is created.
     * 
     * @param id
     * @param name
     * @param price
     * @param stock
     * @param min
     * @param max 
     */
    public Product (int id, String name, double price, int stock, int min, int max) {
       this.id = id;
       this.name = name;
       this.price = price;
       this.stock = stock;
       this.min = min;
       this.max = max;
    }
    
    /**
     * Setter method for product Id.
     * @param id The ID to set.
     */
    public void setId(int id) {
        this.id = id;
    }
    
    /** 
     * Getter method for product Id.
     * @return id The product ID.
     */
    public int getId() {
        return id;
    }
    
    /** 
     * Setter method for product name.
     * @param name The name to set.
     */
    public void setName(String name) {
        this.name = name;
    }
    
    /** 
     * Getter method for product name.
     * @retun name The product name.
     */
    public String getName() {
        return name; 
    }
    
    /**
     * Setter method for product price.
     * @param price The price to set.
     */
    public void setPrice(double price) {
        this.price = price;
    }
    
    /**
     * Getter method for Product price.
     * @return price The product price.
     */
    public double getPrice() {
        return price;
    }
    
    /**
     * Setter method for Product stock.
     * @param stock The amount of current inventory.
     */
    public void setStock(int stock) {
        this.stock = stock;
    }
    
    /**
     * Getter method for Product stock.
     * @return stock The amount of current inventory.
     */
    public int getStock() {
        return stock;
    }
    
    /**
     * Setter method for the minimum amount of products.
     * @param min Sets the current minimum. 
     */
    public void setMin(int min) {
        this.min = min;
    }
    
    /** 
     * Getter method for obtaining the specified minimum amount of products.
     * @return min Returns the specified minimum. 
     */
    public int getMin() {
        return min;
    }
    
    /**
     * Setter method for the maximum amount of products.
     * @param max Sets the current minimum. 
     */
    public void setMax(int max) {
        this.max = max;
    }
    
    /** 
     * Getter method for obtaining the specified maximum amount of products.
     * @return max Returns the specified maximum. 
     */
    public int getMax() {
        return max;
    }
    
    /** 
     * Adds a part to a Product associated part list.
     * @param part The part to be added.
     */
    public void addAssociatedPart(Part part){
        associatedParts.add(part);
    }
    
    /**
     * Deletes associated part from a product associated part list.
     * @param selectedAssociatedPart the part to be deleted.
     * @return boolean Returns true if part exists
     */
    public boolean deleteAssociatedPart(Part selectedAssociatedPart) {
        return associatedParts.remove(selectedAssociatedPart);
    }
    
    /** 
     * Getter method for retrieving a list of all parts associated with a Product.
     * @return associatedParts A list of associated parts.
     */
    public ObservableList<Part> getAllAssociatedParts() {
        return this.associatedParts;
    }
}
