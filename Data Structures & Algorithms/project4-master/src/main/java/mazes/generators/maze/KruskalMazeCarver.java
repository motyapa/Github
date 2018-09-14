package mazes.generators.maze;

import java.util.Random;

import datastructures.concrete.ChainedHashSet;
import datastructures.interfaces.ISet;
import mazes.entities.Maze;
import mazes.entities.Room;
import mazes.entities.Wall;
import misc.graphs.Graph;

/**
 * Carves out a maze based on Kruskal's algorithm.
 *
 * See the spec for more details.
 */
public class KruskalMazeCarver implements MazeCarver {
    @Override
    public ISet<Wall> returnWallsToRemove(Maze maze) {
        Random rand = new Random();
        // Note: make sure that the input maze remains unmodified after this method is over.
        //
        // In particular, if you call 'wall.setDistance()' at any point, make sure to
        // call 'wall.resetDistanceToOriginal()' on the same wall before returning.

        ISet<Wall> toRemove = new ChainedHashSet<>();
        ISet<Wall> allWalls = new ChainedHashSet<>();
        ISet<Room> allRooms = maze.getRooms();
        
        for (Wall wall : maze.getWalls()) {
            double r = rand.nextDouble();
            Wall newWall = new Wall(wall.getRoom1(), wall.getRoom2(), wall.getDividingLine(), r);
            allWalls.add(newWall);

        }
        Graph<Room, Wall> newGraph = new Graph<Room, Wall>(allRooms, allWalls);
        toRemove = newGraph.findMinimumSpanningTree();
        
        return toRemove;
    }
}
