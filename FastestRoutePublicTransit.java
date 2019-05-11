/**
 * Public Transit
 * Author: Kaven Vohra and Carolyn Yao  
 * 
 * Collaborated with David Dataram
 * Does this compile? Yes
 * 
 * All citations are includes in the WriteUp
 */
/*
 * TO-DO:
 * Shortest path - Done
 * Shortest Path including train time - Done
 * Infinite while loop - On specific edge cases
 * Frequency of arrival - Pending
 * Fix negative times - Done
 */
/**
 * This class contains solutions to the Public, Public Transit problem in the
 * shortestTimeToTravelTo method. There is an existing implementation of a
 * shortest-paths algorithm. As it is, you can run this class and get the
 * solutions from the existing shortest-path algorithm.
 */
public class FastestRoutePublicTransit {

    /**
     * The algorithm that could solve for shortest travel time from a station S to a
     * station T given various tables of information about each edge (u,v)
     *
     * @param S         the s th vertex/station in the transit map, start From
     * @param T         the t th vertex/station in the transit map, end at
     * @param startTime the start time in terms of number of minutes from 5:30am
     * @param lengths   lengths[u][v] The time it takes for a train to get between
     *                  two adjacent stations u and v
     * @param first     first[u][v] The time of the first train that stops at u on
     *                  its way to v, int in minutes from 5:30am
     * @param freq      freq[u][v] How frequently is the train that stops at u on
     *                  its way to v
     * @return shortest travel time between S and T
     */
    public int myShortestTravelTime(int S, int T, int startTime, int[][] lengths, int[][] first, int[][] freq) {
        // Source station
        int source = S;
        // Destination Station
        int destination = T;
        // Path stores the shortest times for each station, this is the modified
        // Dijkstra's Algorithm
        int path[] = modDSP(lengths, source, destination);
        // shortest time to get to destination station
        int bestPath = 0;
        // time it takes for the train to get to the next station
        int arrivingTrainTime;
        // current elapsed time
        int currentTime = startTime;
        // Total number of stations we start from the destination
        int index = first.length - 1;
        // We back track to find the source, the station that we will start from
        while (index > 0) {
            if (path[index] == source) {
                break;
            } else {
                index--;
            }
        }
        // iterating from the beginning of the shortest path
        for (int i = index; i >= 1; i--) {
            // Starting at the first station
            int numOfStations = 0;
            // System.out.println("index in the for" + index);
            // set the current index equal to the current station
            int currStation = path[i];
            // since we are traversing backwards this is the next station leading to
            // the beginning of the shortest path
            int arrivingStation = path[i - 1];
            // time the train took to get to this station
            int distanceTraveled = first[currStation][arrivingStation];

            // Distance traveled
            distanceTraveled = first[currStation][arrivingStation]
                    + (numOfStations * freq[currStation][arrivingStation]);
            numOfStations++;
            //Fix frequency 

            // The next train total time including the time so far
            arrivingTrainTime = distanceTraveled + lengths[currStation][arrivingStation];
            // The shortest path is updated to be the time the next rain will take - the
            // current time
            bestPath = bestPath + (arrivingTrainTime - currentTime);
            // Update the current time taken to include the next train
            currentTime = arrivingTrainTime;

            // System.out.println(currentTime);

        }
        return bestPath;
    }

    // Modified Dijkstra's shortest path algorithm to find the shortest path from specific station
    // alot of the code is similar to the implementation on GeeksForGeeks.com
    
    // Referenced shortestTime method provided
    /**
     * Keep track of previous station
     * @param graph					all the stations in the subway
     * @param startingStation		source, or first station we are starting from 
     * @param destination			target station we want to reach
     * @return
     */
    public int[] modDSP(int[][] graph, int startingStation, int destination) {

        // number of verticies
        int lastNode = graph[0].length;
        // will keep track if the station is in the shortest path
        Boolean includeNode[] = new Boolean[lastNode];
        // all shortest paths from stations
        int[] timeToStation = new int[lastNode];
        // previous stations visited
        int[] prevStation = new int[lastNode];
        // shortest from each station
        int[] shortestFromStation = new int[lastNode];

        // by default we set the previous station to -1 since there isnt a previous
        // station from the first one
        prevStation[startingStation] = -1;

        /**
         * By default we will initalize all of the arrays to hold max -1 or false values
         * that way we can keep track of what position in the arrays have been altered
         */
        for (int i = 0; i < lastNode; i++) {
            // time from source to itself is 0
            timeToStation[i] = Integer.MAX_VALUE;
            shortestFromStation[i] = -1;
            includeNode[i] = false;
        }
        timeToStation[startingStation] = 0;

        // Find all of the shortest paths from all stations
        for (int count = 0; count < lastNode - 1; count++) {

            // use the findNextToProcess helper method to find the
            int next = findNextToProcess(timeToStation, includeNode);

            includeNode[next] = true;

            // Update time value of all the adjacent vertices of the picked vertex.
            // Iterate through stations
            for (int curNode = 0; curNode < lastNode; curNode++) {
                if (!includeNode[curNode] && graph[next][curNode] != 0 && timeToStation[next] != Integer.MAX_VALUE
                        && timeToStation[next] + graph[next][curNode] < timeToStation[curNode]) {
                    timeToStation[curNode] = timeToStation[next] + graph[next][curNode];
                    /**
                     * In the modification we keep track of the previous station
                     */
                    prevStation[curNode] = next;
                }
            }
        }
        /**
         * Array out of bounds error
         */
        // Finds the shortest time between T and source.
        int numofStation = 0;
        // While station isnt the starting station
        while (destination != startingStation) {
            // From the modified Dijkstra's algorithm, we are using the shortest path that
            // we found to set the array of the shortest path to the station from the
            // starting station
            shortestFromStation[numofStation++] = destination;
            destination = prevStation[destination];
            // System.out.println(stations);
        }
        shortestFromStation[numofStation] = destination;
        return shortestFromStation;

        /**
         * https://www.geeksforgeeks.org/dijkstras-shortest-path-algorithm-greedy-algo-7/
         */

    }

    /**
     * Finds the vertex with the minimum time from the source that has not been
     * processed yet.
     * 
     * @param times     The shortest times from the source
     * @param processed boolean array tells you which vertices have been fully
     *                  processed
     * @return the index of the vertex that is next vertex to process
     */
    public int findNextToProcess(int[] times, Boolean[] processed) {
        int min = Integer.MAX_VALUE;
        int minIndex = -1;

        for (int i = 0; i < times.length; i++) {
            if (processed[i] == false && times[i] <= min) {
                min = times[i];
                minIndex = i;
            }
        }
        return minIndex;
    }

    public void printShortestTimes(int times[]) {
        System.out.println("Vertex Distances (time) from Source");
        for (int i = 0; i < times.length; i++)
            System.out.println(i + ": " + times[i] + " minutes");
    }

    /**
     * Given an adjacency matrix of a graph, implements
     * 
     * @param graph  The connected, directed graph in an adjacency matrix where if
     *               graph[i][j] != 0 there is an edge with the weight graph[i][j]
     * @param source The starting vertex
     */
    public void shortestTime(int graph[][], int source) {
        int numVertices = graph[0].length;

        // This is the array where we'll store all the final shortest times
        int[] times = new int[numVertices];

        // processed[i] will true if vertex i's shortest time is already finalized
        Boolean[] processed = new Boolean[numVertices];

        // Initialize all distances as INFINITE and processed[] as false
        for (int v = 0; v < numVertices; v++) {
            times[v] = Integer.MAX_VALUE;
            processed[v] = false;
        }

        // Distance of source vertex from itself is always 0
        times[source] = 0;

        // Find shortest path to all the vertices
        for (int count = 0; count < numVertices - 1; count++) {

            int u = findNextToProcess(times, processed);
            processed[u] = true;

            // Update time value of all the adjacent vertices of the picked vertex.
            for (int currentVertex = 0; currentVertex < numVertices; currentVertex++) {
                // Update time[v] only if is not processed yet, there is an edge from u to v,
                // and total weight of path from source to v through u is smaller than current
                // value of time[v]
                if (!processed[currentVertex] && graph[u][currentVertex] != 0 && times[u] != Integer.MAX_VALUE
                        && times[u] + graph[u][currentVertex] < times[currentVertex]) {
                    times[currentVertex] = times[u] + graph[u][currentVertex];
                }
            }
        }

        printShortestTimes(times);
    }

    public static void main (String[] args) {
        int first[][];
        int lengths[][];
        int freq[][];
        int totalTime;

        int lengthTimeGraph[][] = new int[][]{
                {0, 4, 0, 0, 0, 0, 0, 8, 0},
                {4, 0, 8, 0, 0, 0, 0, 11, 0},
                {0, 8, 0, 7, 0, 4, 0, 0, 2},
                {0, 0, 7, 0, 9, 14, 0, 0, 0},
                {0, 0, 0, 9, 0, 10, 0, 0, 0},
                {0, 0, 4, 14, 10, 0, 2, 0, 0},
                {0, 0, 0, 0, 0, 2, 0, 1, 6},
                {8, 11, 0, 0, 0, 0, 1, 0, 7},
                {0, 0, 2, 0, 0, 0, 6, 7, 0}
        };
        FastestRoutePublicTransit t = new FastestRoutePublicTransit();
        t.shortestTime(lengthTimeGraph, 0);
        
        // You can create a test case for your implemented method for extra credit below
     
        System.out.println("Test Case");

        lengths = new int [][]{
                {0, 0, 8, 5, 0, 0, 0, 0, 1},
                {0, 0, 0, 0, 0, 3, 0, 7, 0},
                {9, 0, 0, 1, 3, 2, 0, 0, 4},
                {5, 0, 9, 0, 0, 0, 4, 0, 0},
                {0, 0, 5, 5, 0, 4, 0, 0, 0},
                {0, 3, 0, 0, 0, 1, 2, 0, 0},
                {0, 0, 0, 0, 0, 2, 0, 2, 0},
                {0, 7, 0, 0, 0, 0, 2, 0, 5},
                {1, 0, 4, 0, 0, 0, 0, 5, 0}
        };
        
        first = new int [][]{
                {0, 0, 4, 5, 0, 0, 0, 0, 1},
                {0, 0, 0, 0, 0, 12, 0, 17, 0},
                {9, 0, 0, 11, 15, 0, 0, 0, 14},
                {6, 0, 10, 0, 0, 0, 0, 0, 0},
                {0, 0, 14, 15, 0, 24, 0, 0, 0},
                {0, 6, 0, 0, 0, 0, 12, 0, 0},
                {0, 0, 0, 0, 0, 9, 0, 2, 0},
                {0, 6, 0, 0, 0, 0, 8, 0, 15},
                {1, 0, 4, 0, 0, 0, 0, 8, 0}
        };
      
        freq = new int [][]{
                {0, 0, 19,18, 0, 0, 0, 0, 17},
                {0, 0, 0, 0, 0, 3, 0, 7, 0},
                {9, 0, 0, 8, 7, 0, 0, 0, 6},
                {5, 0, 4, 0, 0, 0, 0, 0, 0},
                {0, 0, 5, 4, 0, 3, 0, 0, 0},
                {0, 3, 0, 0, 0, 0, 2, 0, 0},
                {0, 0, 0, 0, 0, 2, 0, 1, 0},
                {0, 17, 0, 0, 0, 0, 12, 0, 15},
                {11, 0, 14, 0, 0, 0, 0, 12, 0}};
	    
	    int firstStation = 1;
	    int targetStation = 4;
	
	    totalTime = t.myShortestTravelTime(firstStation, targetStation, 8, lengths, first, freq);
	    
	    System.out.println("You will arrive at station "  + targetStation + " from station " + firstStation + " in "
	            + totalTime + " minutes.");
                
                
    }
}


 

  


  


  



 


