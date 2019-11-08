package adproc;

import java.util.LinkedList;
import java.util.List;

/**
 *a model for the order backend
 * @author UP822718, UP918156, UP879389, UP821837
 */
public class OrderingBackEnd
{
    
    LinkedList<Box> box;
    OrderingBackEnd ()
    {
        box = new LinkedList<Box>(); 
    }
    /**
    * make the correct box
    * @param   printiColours the number of colours printed on the box 
    * @param  reinforcementBottom  if the box has a reinforced bottom or not
    * @param  reinforcementCorners if the box has reinforced corners  or not
    * @param seltop if the box has a reinforced sealable top or not
    * @param grade the grade of the cardboard
    * @param width the width of the blocks
    * @param length length of the box
    * @param  height the height of the box
    * @param quantity the quantity of the box created
    * @return then new box 
    */
    public Box MakeBox(int printiColours,boolean reinforcementBottom,boolean reinforcementCorners,
            boolean seltop, int grade, double width, double   length, double height, int quantity)
    {
        if(printiColours == 0 & !reinforcementBottom & !reinforcementCorners & grade >=1 & grade <=3 ) //I
        {
           return  (new BoxType1( width,  length,  height,  grade,quantity, seltop));
        }
        else if(printiColours == 1& !reinforcementBottom & !reinforcementCorners & grade >=2 & grade <=4) //II
        {
           return (new BoxType2( width,  length,  height,  grade,quantity, seltop));
        }
        else if(printiColours == 2 & !reinforcementBottom & !reinforcementCorners & grade >=2 & grade <=5 ) //III
        {
           return (new BoxType3( width,  length,  height,  grade,quantity, seltop));
        }
        else if(printiColours == 2 & reinforcementBottom & !reinforcementCorners & grade >=2 & grade <=5 ) //IV
        {
           return(new BoxType4 (width,length,height,grade,quantity, seltop));
        }
        else if(printiColours == 2 & reinforcementBottom & reinforcementCorners & grade >=3 & grade <=5 ) //V
        {
           return ( new BoxType5( width,  length,  height,  grade,quantity, seltop));
        }
        
        return null;
    }   
    /**
    *get a box form the order
    * @param i the index to get the box at
    * @return the box at the index
    */
    public Box GetBox(int i)
    {
       return box.get(i);
    }
    /**
    *Adds a box to the order
    * @param i the box to be added to the order
    */
    public void SetBox(Box i)
    {
        box.add(i);
    }
    /**
    *get the number of boxes
    * @return get the number of boxes
    */
   public  int GetBoxNum()
    {
       return box.size();  
    }
    /**
    *remove the box at index
    * @param i the index to remove the box at
    */
   public  void RemoveBox(int i)
    {
       box.remove(i);
    }
    /**
    *get all the Prices
    * @return get all the Prices
    */
  public  double GetPrices()
    {
        double cost = 0;
        for (int i = 0; i < box.size(); i++)
        {
            cost += box.get(i).price();
        }
        return cost;
    }
    /**
    *get all the show off strings
    * @return get all the show off strings
    */
   public  String GetShowoffs()
    {
        String cost = "";
        for (int i = 0; i < box.size(); i++)
        {
            cost += box.get(i).showoff() +"\n";
        }
        return cost;
    }
}
