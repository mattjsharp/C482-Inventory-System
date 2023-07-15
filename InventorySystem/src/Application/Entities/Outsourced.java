package Application.Entities;

/**
 * Template class for Outsourced Parts. It is a child class of the Part class and 
 * adds the  companyName member.
 */
public class Outsourced extends Part {
    private String companyName;
    
    /**
     * Sets initial values for each instance. All members except for companyName are inherited from Part class. 
     * 
     * @param id
     * @param name
     * @param price
     * @param stock
     * @param min
     * @param max
     * @param companyName 
     */
    public Outsourced(int id, String name, double price, int stock, int min, int max, String companyName) {
        super(id, name, price, stock, min, max);
        this.companyName = companyName;
    }
    
    /**
     * Setter method for Outsourced Parts.
     * @param companyName The name to be set
     */
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
    
    /**
     * Getter method for Outsourced Parts.
     * @return companyName The name of the company.
     */
    public String getCompanyName() {
        return this.companyName;
    }
}
