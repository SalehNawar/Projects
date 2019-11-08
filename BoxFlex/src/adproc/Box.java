package adproc;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 *a model for the generic box
 * @author UP822718, UP918156, UP879389, UP821837
 */
public abstract class Box
{
    //Fields
    double length;
    double width;
    double height;
    int grade;
    int quantity;
    boolean seltop; //Sealed top
     
    //Constructor 
    /**
    *constructor to make a box
     * @param width the width of the box
     * @param length the length of the box
     * @param height the height of the box
     * @param grade the grade of the box
     * @param quantity the quantity of boxes
     * @param seltop the sealable top option of the box
    */
    public Box (double width, double length, double height, int grade, int quantity, boolean seltop)
    {
        this.width = width;
        this.length = length;
        this.height = height;
        this.grade = grade;
        this.quantity = quantity;
        this.seltop = seltop;
    }
    
    /**
    *Get the price of the box
    * @return return the price 
    */
    public abstract double price();
    
    /**
    *return a string describing a box
    * @return describes a box 
    */
    public abstract String showoff();
    
    /**
    *Calculates cost of grade multiplied by the size of the cardboard box
     * @param length original length of box
     * @param width original width of box
     * @param height original height of box
     * @param grade  grade of cardboard
    * @return The cost of grade multiplied by the size of the cardboard box 
    */
    protected double CardboardGrade(int grade, double length, double width, double height)
    {
       double size = (width*length*2) + (height*length*2)  + (height*width*2);
        switch (grade)
        {
            case 1:
                return 0.55*size;
            case 2:
                return 0.65*size;
            case 3:
                return 0.82*size;
            case 4:
                return 0.98*size;
            case 5:
                return 1.5*size;
            default:
                throw new AssertionError();
        }
    }
    
    /**
    *Calculate the additional cost of the colour print
     * @param  price original price of box
     * @param  level original level of colour
    * @return colour print cost 
    */
     protected  double calcolour(double price , int level )
    {
        switch (level) {
            case 0:
                return 0 * price;
            case 1:
                return 0.12 * price;
            case 2:
                return 0.15 * price;
            default:
                throw new AssertionError();
        }
    }
     
    /**
    *Calculate the cost of having a reinforced 
     * @param price original price of box
    * @return cost  reinforced bottom
    */
   protected   double calbottom(double price)
    {
        return 0.13 * price; 
    }
   
    /**
    *Calculate the cost of having a corners 
     * @param price original price of box
    * @return cost  corners bottom
    */
     protected double calcorners(double price)
    {
        return 0.12 * price; 
    }
     
    /**
    *Calculate the cost of having a sealable top 
    * @param price  price of box
    * @param seltop to determine if sealable top was selected
    * @return additional cost dependent on sealable top
    */
    protected double calSealable(double price, boolean seltop )
    {
        if (seltop)
        {
            return price*0.10;
        };
        return 0;
    }
    
    /**
    *Calculate cost of surface area 
     * @param length original length of box
     * @param width original width of box
     * @param height original height of box
    * @return cost of surface area
    */
    protected double calculateSurfaceArea(double length, double width, double height )
    { 
        return  (width*length*2) + (height*length*2)  + (height*width*2);
    }
}
