
import java.io.*;
import java.util.*;
 
// This is where we begin the algorithm.
 
public class cellDivision{
    
   public static void main(String[] args){
      int n = 10000;
      int[] first = new int[3];
      for (int i = 0; i < 3; i++) {
         first[i] = 0;
      }   
      Cell First = new Cell(first, 26);
      ArrayList<Cell> allCells = new ArrayList<Cell>();
      HashMap<String, Cell> allCellsMap = new HashMap<String, Cell>();
      allCells.add(First);
      allCellsMap.put(Arrays.toString(First.loc), First);
      for (int i = 0; i < 100; i++) {
         newCell(allCells, allCellsMap);       
      }
   }  
   
   //looks at all of the x, y, z coordinates nearby after a cell is made
   //to determine how many open spots that cell has to expand to
   //as well as reduce the surrounding cells open spots by 1   
   public static void updateNeighbors(Cell C, HashMap<String, Cell> m) {
      for (int i = C.loc[0] - 1; i < C.loc[0] + 2; i++) {
         for (int j = C.loc[1] - 1; j < C.loc[1] + 2; j++) {
            for (int k = C.loc[2] - 1; k < C.loc[2] + 2; k++) {
               int[] check = {i, j, k};
               if (m.containsKey(Arrays.toString(check))) {
                  C.updateN();
                  if (!Arrays.toString(check).equals(Arrays.toString(C.loc))) { 
                     m.get(Arrays.toString(check)).updateN();
                  }   
               }
            }
         }          
      }  
   }
   
   //creates a new cell psuedo-randomly
   //i.e. which cell to duplicate is chosen based on algorithm
	public static double newCell(ArrayList<Cell> allCells, HashMap<String, Cell> m, double time) {
		Random r = new Random();
		double rate = totalDivRate(allCells);
		double div = rate * r.nextDouble(); //picks a double between 0 and the total divRate    
		int tracker = 0;
		while (div - allCells.get(tracker).divRate() > 0) { //strictly greater than 0 or greater/equal to 0?
			div -= allCells.get(tracker).divRate();
			tracker++;
		}
		int[] newLocation = newLocation(allCells.get(tracker), m);
		Cell newC = new Cell(newLocation, 27);
		allCells.add(newC);
		m.put(Arrays.toString(newLocation), newC); 
		updateNeighbors(newC, m);
		return Math.log(1-r.nextDouble())/(-rate);
	}
   
   //sums together all the division rates of the cells   
   public static double totalDivRate(ArrayList<Cell> allCells) {
      int size = allCells.size();
      double total = 0;
      for (int i = 0; i < size; i++) {
         total += allCells.get(i).divRate();
      }
      return total;
   }
   
   //randomly creates a new location near a cell   
   public static int[] newLocation(Cell toCopy, HashMap<String, Cell> m) {
      int[] current = new int[3];
      Random r = new Random();
      for (int i = 0; i < 3; i++) { 
         int getCoord = toCopy.loc[i];
         int newCoord = r.nextInt(3) - 1;
         current[i] = getCoord - newCoord;
      }  
      if (m.containsKey(Arrays.toString(current))) {
         return newLocation(toCopy, m);
      } else {
         return current;
      }      
   }  
   	   
	public static double maxDistance(ArrayList<Cell> cells) {
	   double max = 0;
	   for (Cell c: cells) {
	      int[] coords = c.loc;
	      double distance = Math.sqrt(coords[0] * coords[0] + coords[1] * coords[1] + coords[2] * coords[2]);
	      if (distance > max) {
	         max = distance;
	      }
	   }
	   return Math.round(max * 100.0) / 100.0;      
	}                      
}
    
 