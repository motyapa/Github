package misc.graphs;

import datastructures.concrete.ArrayDisjointSet;
import datastructures.concrete.ArrayHeap;
import datastructures.concrete.ChainedHashSet;
import datastructures.concrete.DoubleLinkedList;
import datastructures.concrete.dictionaries.ChainedHashDictionary;
import datastructures.interfaces.IDictionary;
import datastructures.interfaces.IList;
import datastructures.interfaces.IPriorityQueue;
import datastructures.interfaces.ISet;
import misc.Searcher;
import misc.exceptions.NoPathExistsException;

/**
 * Represents an undirected, weighted graph, possibly containing self-loops, parallel edges,
 * and unconnected components.
 *
 * Note: This class is not meant to be a full-featured way of representing a graph.
 * We stick with supporting just a few, core set of operations needed for the
 * remainder of the project.
 */
public class Graph<V, E extends Edge<V> & Comparable<E>> {
    IDictionary<V, ISet<E>> graph;
    int numVertex;
    IList<E> allEdges;
    ISet<V> allVertices;
    // NOTE 1:
    //
    // Feel free to add as many fields, private helper methods, and private
    // inner classes as you want.
    //
    // And of course, as always, you may also use any of the data structures
    // and algorithms we've implemented so far.
    //
    // Note: If you plan on adding a new class, please be sure to make it a private
    // static inner class contained within this file. Our testing infrastructure
    // works by copying specific files from your project to ours, and if you
    // add new files, they won't be copied and your code will not compile.
    //
    //
    // NOTE 2:
    //
    // You may notice that the generic types of Graph are a little bit more
    // complicated then usual.
    //
    // This class uses two generic parameters: V and E.
    //
    // - 'V' is the type of the vertices in the graph. The vertices can be
    //   any type the client wants -- there are no restrictions.
    //
    // - 'E' is the type of the edges in the graph. We've contrained Graph
    //   so that E *must* always be an instance of Edge<V> AND Comparable<E>.
    //
    //   What this means is that if you have an object of type E, you can use
    //   any of the methods from both the Edge interface and from the Comparable
    //   interface
    //
    // If you have any additional questions about generics, or run into issues while
    // working with them, please ask ASAP either on Piazza or during office hours.
    //
    // Working with generics is really not the focus of this class, so if you
    // get stuck, let us know we'll try and help you get unstuck as best as we can.

    /**
     * Constructs a new graph based on the given vertices and edges.
     *
     * @throws IllegalArgumentException  if any of the edges have a negative weight
     * @throws IllegalArgumentException  if one of the edges connects to a vertex not
     *                                   present in the 'vertices' list
     */
    public Graph(IList<V> vertices, IList<E> edges) {
        graph = new ChainedHashDictionary<V, ISet<E>>();
        allEdges = new DoubleLinkedList<E>();
        allVertices = new ChainedHashSet<V>();
        
        for (V vertice : vertices) {
            if (!graph.containsKey(vertice)) {
                graph.put(vertice, new ChainedHashSet<E>());
            }
            allVertices.add(vertice);
        }
        for (E edge : edges) {
            if (edge.getWeight() < 0.0) {
                throw new IllegalArgumentException();
            }
            V vertex1 = edge.getVertex1();
            V vertex2 = edge.getVertex2();
            if (!graph.containsKey(vertex1) || !graph.containsKey(vertex2)) {
                throw new IllegalArgumentException();
            }
            ISet<E> temp1 = graph.get(vertex1);
            ISet<E> temp2 = graph.get(vertex2);
            temp1.add(edge);
            temp2.add(edge);
            graph.put(vertex1, temp1);
            graph.put(vertex2, temp2);
            allEdges.add(edge);
         }
    }

    /**
     * Sometimes, we store vertices and edges as sets instead of lists, so we
     * provide this extra constructor to make converting between the two more
     * convenient.
     */
    public Graph(ISet<V> vertices, ISet<E> edges) {
        // You do not need to modify this method.
        this(setToList(vertices), setToList(edges));
    }

    // You shouldn't need to call this helper method -- it only needs to be used
    // in the constructor above.
    private static <T> IList<T> setToList(ISet<T> set) {
        IList<T> output = new DoubleLinkedList<>();
        for (T item : set) {
            output.add(item);
        }
        return output;
    }

    /**
     * Returns the number of vertices contained within this graph.
     */
    public int numVertices() {
        return allVertices.size();
    }

    /**
     * Returns the number of edges contained within this graph.
     */
    public int numEdges() {
        return allEdges.size();
    }

    /**
     * Returns the set of all edges that make up the minimum spanning tree of
     * this graph.
     *
     * If there exists multiple valid MSTs, return any one of them.
     *
     * Precondition: the graph does not contain any unconnected components.
     */
    public ISet<E> findMinimumSpanningTree() {
        ISet<E> result = new ChainedHashSet<E>();
        ArrayDisjointSet<V> connectedVertex = new ArrayDisjointSet<V>();
        IList<E> sorted = Searcher.topKSort(allEdges.size(), allEdges);
        
        for (V vertex : allVertices) {
            connectedVertex.makeSet(vertex);
        }
        for (E edge : sorted) {
            V vertex1 = edge.getVertex1();
            V vertex2 = edge.getVertex2();
            if (connectedVertex.findSet(vertex1) != connectedVertex.findSet(vertex2)) {
                connectedVertex.union(vertex1, vertex2);
                result.add(edge);
            }
        }
        return result;   
    }

    /**
     * Returns the edges that make up the shortest path from the start
     * to the end.
     *
     * The first edge in the output list should be the edge leading out
     * of the starting node; the last edge in the output list should be
     * the edge connecting to the end node.
     *
     * Return an empty list if the start and end vertices are the same.
     *
     * @throws NoPathExistsException  if there does not exist a path from the start to the end
     */
    
    
    public IList<E> findShortestPathBetween(V start, V end) {
        IPriorityQueue<Vertex> queue = new ArrayHeap<Vertex>();
        IDictionary<V, Double> cost = new ChainedHashDictionary<V, Double>();
        
        Vertex first = new Vertex(start, 0);
        
        if (start.equals(end)) {
            return first.getPath();
        }
        
        queue.insert(first);
        cost.put(start, 0.0);
        
        while (!queue.isEmpty()) {
            Vertex current = queue.removeMin();
            if (current.getVertex().equals(end)) {
                return current.getPath();
            }
            ISet<E> edges = graph.get(current.getVertex());
            for (E edge : edges) {
                V linkedTo = edge.getOtherVertex(current.getVertex());
                Double dis = current.getDistance() + edge.getWeight();
                if (!cost.containsKey(linkedTo)) {
                    Vertex notVisited = new Vertex(linkedTo, dis);
                    cost.put(linkedTo, dis);
                    IList<E> prevPath = current.getPath();
                    for (E prevEdge : prevPath) {
                        notVisited.addPath(prevEdge);
                    }
                    notVisited.addPath(edge);
                    queue.insert(notVisited);
                } else if (cost.containsKey(linkedTo) && dis < cost.get(linkedTo)) {
                    Vertex update = new Vertex(linkedTo, dis);
                    cost.put(linkedTo, dis);
                    IList<E> prevPath = current.getPath();
                    for (E prevEdge : prevPath) {
                        update.addPath(prevEdge);
                    }
                    update.addPath(edge);
                    queue.insert(update);
                }
            }
            
        }
        throw new NoPathExistsException();
    }
     
    private class Vertex implements Comparable<Vertex> {
        V vertex;
        double distance;
        IList<E> path;
        
        public Vertex(V vertex, double distance) {
            this.vertex = vertex;
            this.distance = distance;
            path = new DoubleLinkedList<E>();
        } 
        
        public V getVertex() {
            return vertex;
        }
   
        public void addPath(E edge) {
            path.add(edge);
        }
        
        public IList<E> getPath() {
            return path;
        }
        
        public double getDistance() {
            return distance;
        }

        @Override
        public int compareTo(Vertex other) {
            if (this.distance < other.distance) {
                return -1;
            } else if (this.distance > other.distance) {
                return 1;
            } else {
                return 0;
            }
        }
        
    }
}
