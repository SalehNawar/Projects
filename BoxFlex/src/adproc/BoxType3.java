package adproc;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 *a model for creating a Type 3 box
 * @author UP822718, UP918156, UP879389, UP821837
 */
public class BoxType3 extends Box
{
    //Constructor of Type 3 box
    /**
     * @param width the width of the box
     * @param length the length of the box
     * @param height the height of the box
     * @param grade the grade of the box
     * @param quantity the quantity of boxes
     * @param seltop the sealable top option of the box
    */
    public BoxType3(double width, double length, double height, int grade,int quantity, boolean seltop)
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
        double colour = calcolour( price , 2);
        double sealable = calSealable(price, seltop );
        return (colour+price+sealable) * quantity;
    } 
   
    /**
    *Return a string describing the box
    * @return description of box 
    */
    public String showoff()
    {
     return "Type 3 Box - " + " Width: " + width+ ", Height: " + height + ", Length: " + length + ", Quantity: " + quantity + "\n"; 
    }
    
}
