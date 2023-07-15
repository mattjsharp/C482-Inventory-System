package Application.Entities;

/**
 * Template class for InHouse Parts. It is a child class of the Part class and 
 * adds the  machineId member.
 */
public class InHouse extends Part {
    private int machineId;
    
    /**
     * Sets initial values for each instance. All members except for machineId are inherited from Part class. 
     * 
     * @param id
     * @param name
     * @param price
     * @param stock
     * @param min
     * @param max
     * @param machineId 
     */
    public InHouse(int id, String name, double price, int stock, int min, int max, int machineId) {
        super(id, name, price, stock, min, max);
        this.machineId = machineId;
    }
    
    /**
     * Setter method for InHouse Parts.
     * @param machineId The machineID to be set
     */
    public void setMachineId(int machineId) {
        this.machineId = machineId;
    }
    
    /**
     * Getter method for InHouse Parts.
     * @return machineId The machineId of the part.
     */
    public int getMachineId() {
        return this.machineId;
    }
}
