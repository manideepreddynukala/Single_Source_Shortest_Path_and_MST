import java.util.HashSet;
import java.util.LinkedList;
import java.util.*;
import java.io.*;

public class DijkstraandPrim{
    // taking hashmap to take unique vertices
    private static HashMap<Character, Integer> mapVertices = new HashMap<>();
    private static final int BUFFER_LENGTH = 16;
    private static final int MARK_LENGTH = 24 * BUFFER_LENGTH;
    // Each edge has source, destinaton and weight of the edge
    static class Edge{
        int source;
        int destination;
        int weight;
        public Edge(int source, int destination, int weight) {
            this.source = source;
            this.destination = destination;
            this.weight = weight;
        }
    }
    // This HeapNode is used for getting the minimum weight edge
    static class HeapNode{
        int vertex;
        int distance;
        // previous Vertex is used to backtrack for printing the path
        int previousVertex;
    }
    // Used to store parent and weight of the edge
    static class Answer{
        int parent;
        int weight;
    }
    // Graph DataStructure Consruction
    static class Graph {
        int vertices;
        LinkedList<Edge>[] adjacencylist;
        Graph(int vertices) {
            this.vertices = vertices;
            adjacencylist = new LinkedList[vertices];
            for (int i = 0; i <vertices ; i++) {
                adjacencylist[i] = new LinkedList<>();
            }
        }
    // add the edge to the Graph (Undirected)
        public void addEdge(int source, int destination, int weight) {
            Edge edge = new Edge(source, destination, weight);
            adjacencylist[source].addFirst(edge);
            edge = new Edge(destination, source, weight);
            adjacencylist[destination].addFirst(edge); //for undirected graph
        }
    // add the edge to the Graph(Directed)
        public void addEdgeDirected(int source, int destination, int weight) {
            Edge edge = new Edge(source, destination, weight);
            adjacencylist[source].addFirst(edge);
        }

    public void dijkstra_GetMinDistances(int sourceVertex){
        int INFINITY = Integer.MAX_VALUE;
        boolean[] SPT = new boolean[vertices];

        HeapNode [] heapNodes = new HeapNode[vertices];
        for (int i = 0; i <vertices ; i++) {
            heapNodes[i] = new HeapNode();
            heapNodes[i].vertex = i;
            heapNodes[i].distance = INFINITY;
            heapNodes[i].previousVertex = 0;
        }

        heapNodes[sourceVertex].distance = 0;
        MinHeap minHeap = new MinHeap(vertices);
        for (int i = 0; i <vertices ; i++) {
            minHeap.insert(heapNodes[i]);
        }
        while(!minHeap.isEmpty()){

            HeapNode extractedNode = minHeap.extractMin();

            int extractedVertex = extractedNode.vertex;
            SPT[extractedVertex] = true;
            LinkedList<Edge> list = adjacencylist[extractedVertex];
            for (int i = 0; i <list.size() ; i++) {
                Edge edge = list.get(i);
                int destination = edge.destination;

                if(SPT[destination]==false ) {

                    int newKey =  heapNodes[extractedVertex].distance + edge.weight ;
                    int currentKey = heapNodes[destination].distance;
                    if(currentKey>newKey){
                        relax(minHeap, newKey, destination);
                        heapNodes[destination].distance = newKey;
                        heapNodes[destination].previousVertex = extractedVertex;
                    }
                }
            }
        }
        printDijkstra(heapNodes, sourceVertex);
    }

        //method contains implementation for Prim's algorithm
        public void primMST(){
            boolean[] Visited = new boolean[vertices];
            Answer[] resultSet = new Answer[vertices];
            int [] key = new int[vertices];
            HeapNode [] heapNodes = new HeapNode[vertices];
            for (int i = 0; i <vertices ; i++) {
                heapNodes[i] = new HeapNode();
                heapNodes[i].vertex = i;
                heapNodes[i].distance = Integer.MAX_VALUE;
                resultSet[i] = new Answer();
                resultSet[i].parent = -1;
                Visited[i] = true;
                key[i] = Integer.MAX_VALUE;
            }

            heapNodes[0].distance = 0;


            MinHeap minHeap = new MinHeap(vertices);
            // Add vertices to the minimum heap
            for (int i = 0; i <vertices ; i++) {
                minHeap.insert(heapNodes[i]);
            }

            //Loop until minheap is not empty
            while(!minHeap.isEmpty()){
                //extract the min vertex
                HeapNode extractedNode = minHeap.extractMin();
                int extractedVertex = extractedNode.vertex;
                Visited[extractedVertex] = false;
                //iterate through all the adjacent vertices
                LinkedList<Edge> list = adjacencylist[extractedVertex];
                for (int i = 0; i <list.size() ; i++) {
                    Edge edge = list.get(i);
                    if(Visited[edge.destination]) {
                        int destination = edge.destination;
                        int newKey = edge.weight;
                        if(key[destination]>newKey) {
                            relax(minHeap, newKey, destination);
                            resultSet[destination].parent = extractedVertex;
                            resultSet[destination].weight = newKey;
                            key[destination] = newKey;
                        }
                    }
                }
            }
            //Print the minimum spanning tree
            printMST(resultSet);
        }

        //relaxing the edges
        public void relax(MinHeap minHeap, int newKey, int vertex){
            //get the index which distance's needs a decrease;
            int index = minHeap.indices[vertex];
            //get the node and update its value
            HeapNode node = minHeap.minHeap[index];
            node.distance = newKey;
            minHeap.upHeap(index);
                }
                public String getVertexKey(Integer getVertexValue)
                {
                    String sourceVertexChar;
                    for (Map.Entry entry : mapVertices.entrySet()) {
                        if(getVertexValue.equals(entry.getValue())) {
                    sourceVertexChar = entry.getKey().toString();
                    return sourceVertexChar;
                }
            }
            return null;
        }

        public void printDijkstra(HeapNode[] resultSet, int sourceVertex) {
            System.out.println("Dijkstra Algorithm: (Adjacency List + Min Heap)");
            Integer value;
            Integer sourceVertexValue;
            String sourceVertexChar=null;
            sourceVertexValue =sourceVertex;
            sourceVertexChar = getVertexKey(sourceVertexValue);

            int counter=0;
            for (int i = 0; i < vertices; i++) {

                value =i;
                for (Map.Entry entry : mapVertices.entrySet()) {

                    if(sourceVertexValue == entry.getValue() && counter<=0)
                    {
                        System.out.println("Source Vertex: " + sourceVertexChar + " to vertex: " + entry.getKey() +
                                " distance: " + resultSet[i].distance);
                        counter++;
                    }
                    else if(value.equals(entry.getValue()) && (sourceVertexValue != entry.getValue())) {
                        int printCounter=0;
                        if(resultSet[i].distance >=250000000 || resultSet[i].distance <= -250000000)
                        {
                            System.out.println("Source Vertex " + sourceVertexChar + " to vertex " + entry.getKey() +
                                    " distance: " + "INFINITY" + " Previous Vertex " + getVertexKey(resultSet[i].previousVertex));
                        }
                        else
                        {
                            System.out.println("Source Vertex " + sourceVertexChar + " to vertex " + entry.getKey() +
                                    " distance: " + resultSet[i].distance + " Previous Vertex " + getVertexKey(resultSet[i].previousVertex));
                        }
                        int pathVertex = Integer.parseInt(entry.getValue().toString());
                        ArrayList<Integer> PathList = new ArrayList<Integer>();
                        if(resultSet[i].distance >=250000000 || resultSet[i].distance <= -250000000) {
                            System.out.println("Path: Can not be reached");
                        }
                        else {
                            while (pathVertex != sourceVertex) {
                                PathList.add(pathVertex);
                                pathVertex = resultSet[pathVertex].previousVertex;
                            }
                            Collections.reverse(PathList);
                            Iterator itr = PathList.iterator();
                            System.out.print("Path:");
                            System.out.print(getVertexKey(sourceVertex));
                            while (itr.hasNext()) {
                                int t = Integer.parseInt(itr.next().toString());
                                System.out.print("-->");
                                System.out.print(getVertexKey(t));
                            }
                            System.out.println();
                        }
                        break;
                    }
                }
            }
        }

        public void printMST(Answer[] resultSet){
            int total_min_weight = 0;
            System.out.println();
            System.out.println();
            System.out.println("Minimum Spanning Tree Using Prims Algorithm: ");
            for (int i = 1; i <vertices ; i++) {
                System.out.println("Edge: " + getVertexKey(i) + " - " + getVertexKey(resultSet[i].parent) +
                        " weight: " + resultSet[i].weight);
                total_min_weight += resultSet[i].weight;
            }
            System.out.println("Total minimum distance: " + total_min_weight);
        }
    }


    public static void main(String[] args) {

        int vertices;
        int edges;
        char source;
        char destination;
        int distance;
        char sourceVertex;
        HashSet<Character> verticesSet = new HashSet<Character>();
        String Type;
        String presentWorkingDirectory = System.getProperty("user.dir");
        File file = new File(presentWorkingDirectory + "//input1.txt");
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String st;
            String st1;
            st = br.readLine();
            String[] A = new String[3];
            A = st.split("\\s+");
            vertices = Integer.parseInt(A[0]);
            edges = Integer.parseInt(A[1]);
            Type = A[2];
            br.mark(MARK_LENGTH);

            int numberVertices =0;
            Graph graph = new Graph(vertices);
            for(int i=0;i<edges;i++)
            {
                st = br.readLine();
                A = st.split("\\s+");
                source = A[0].charAt(0);
                destination = A[1].charAt(0);
                distance = Integer.parseInt(A[2]);
                verticesSet.add(source);
                verticesSet.add(destination);
            }
            br.reset();
            Iterator<Character> itr=verticesSet.iterator();
            while(itr.hasNext()){
                mapVertices.put(itr.next(),numberVertices);
                numberVertices++;
            }
            for(int i=0;i<edges;i++)
            {
                st = br.readLine();
                A = st.split("\\s+");
                source = A[0].charAt(0);
                destination = A[1].charAt(0);
                distance = Integer.parseInt(A[2]);
                if(Type.equals("U"))
                    graph.addEdge(mapVertices.get(source),mapVertices.get(destination),distance);
                else
                    graph.addEdgeDirected(mapVertices.get(source),mapVertices.get(destination),distance);
            }
            st = br.readLine();
            if(st!=null)
            {
                sourceVertex = st.charAt(0);
                graph.dijkstra_GetMinDistances(mapVertices.get(sourceVertex));
                graph.primMST();
            }
            else
            {
                Object[] hashMapKeys = mapVertices.keySet().toArray();
                Object randomKey = hashMapKeys[new Random().nextInt(hashMapKeys.length)];
                sourceVertex = randomKey.toString().charAt(0);
                System.out.println("Source Vertex taken is : " + sourceVertex);
                graph.dijkstra_GetMinDistances(mapVertices.get(sourceVertex));
                graph.primMST();
            }
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
    }
}