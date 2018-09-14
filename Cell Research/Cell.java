import java.util.*;
public class Cell {
 
 
    public int[] loc;   // We will use the array location to denote the coordinates
    public float neighbor;      // The number of neighbors
 
 
    public Cell(int[] l, float n) {
 
        if (l.length != 3 || n < 0) {
            throw new IllegalArgumentException("Erroneous location or rate");
        } else {
            loc= l;
            neighbor = n;
        }
    }
    
    public double divRate() {
      return Math.log(2) * (neighbor / 26);
    } 
    
    public void updateN() {
      neighbor--;
   }
   
   public String toString() {
      return "The location of this cell is : " + loc[0] + ", " + loc[1] + ", " + loc[2];
   } 
}
