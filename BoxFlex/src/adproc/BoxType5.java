package adproc;

import static com.sun.javafx.geom.BaseBounds.BoundsType.BOX;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 *a model for creating a Type 5 box
 * @author UP822718, UP918156, UP879389, UP821837
 */
public class BoxType5  extends Box
{
    // The reinforced corner option of the box
    boolean corner;
    
    //Constructor of Type 5 box
    /**
     * @param width the width of the box
     * @param length the length of the box
     * @param height the height of the box
     * @param grade the grade of the box
     * @param quantity the quantity of boxes
     * @param seltop the sealable top option of the box
    */
    public BoxType5(double width, double length, double height, int grade,int quantity, boolean seltop)
    {
        super(width, length, height, grade, quantity, seltop);
    }
    
    /**
    *Get the price of the box
    * @return return the price 
    */
    public double price()
    {
        double price  =   CardboardGrade(grade,length,width, height);
        double corners  = calcorners(price);
        double colour = calcolour( price , 2);
        double bottom =  calbottom(price);
        double sealable = calSealable(price, seltop );
        return (colour+price+bottom+corners + sealable) * quantity;
    }
    
    /**
    *Return a string describing the box
    * @return description of box 
    */
    public String showoff()
    {
     return "Type 5 Box - " + " Width: " + width+ ", Height: " + height + ", Length: " + length + ", Quantity: " + quantity + "\n"; 
    }
}
