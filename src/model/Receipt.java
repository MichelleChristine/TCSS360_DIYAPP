package model;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Everything the user purchases for a project can be made into a receipt.
 * A user can get a receipt from a Material they purchased in the shop,
 * or they can enter the information from an item purchased elsewhere.
 * 
 * @author Michelle
 */
public class Receipt implements Serializable {
    
    /**
     * generated serial id.
     */
    private static final long serialVersionUID = -8491951796110188642L;

    public String title;

    public double cost;
    
    public LocalDate date;
    
    public String note;
    
    /**
     * Constructor for a Material bought in the shop.
     * 
     * @param material
     */
    public Receipt(Material material) {
        title = material.myName;
        cost = material.myPrice;
        date = LocalDate.now();
        note = "measurement: " + material.myMeasurement.toString() + "\nquantity: " + material.myAmount;
    }
    
    /**
     * Constructor for user inputting a material
     * not bought in the program's store feature.
     * 
     * @param cost
     * @param note
     */
    public Receipt(String title, double cost, String date, String note) { //date in the form MM/dd/yyyy
        this.cost = cost;
        this.date = LocalDate.now().minusDays(0/*days to subtract to get the actual time purchased.
        should use the string passed in to determine how long ago it was*/); //TODO figure this out
        this.note = note;
    }
    
    public String toString() {
        return "Material: title\n cost: " + cost + "\n date purchased: " + date.toString() + "\n note:\n" + note;
    }
    
}