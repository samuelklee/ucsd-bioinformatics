package ucsd.IandII;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

public class EulerianCycle {
    private Map<Object, List<Object>> graph;
    private Set<Object> nodesVisitedWithUnusedEdges = new HashSet<>();

    public EulerianCycle(Map<Object, List<Object>> graph) {
        this.graph = graph;
    }

    /**
     * Nested class for constructing both subcycles and Eulerian cycle.
     */
    private class Cycle {
        private LinkedList<Object> nodesVisited = new LinkedList<>();

        private Random random = new Random();

        /**
         * Constructs a random cycle (not Eulerian).  Removes edges from graph as they are visited and adds to set
         * of nodes visited with unused edges.  Use this for making new subcycles when constructing Eulerian cycle.
         * @param startNode                     starting node
         * @return                              cycle starting at given node
         */
        public Cycle(Object startNode) {
//            System.out.println(EulerianCycle.this.graph);
            Object currentNode = startNode;
            Object nextNode;
            do {
                this.nodesVisited.offer(currentNode);
                List<Object> nextNodes = EulerianCycle.this.graph.get(currentNode);

                if (nextNodes.size() > 1) {
                    //if more than one available next node, add current node to set of nodes with unused edges and
                    //pick next node randomly
                    EulerianCycle.this.nodesVisitedWithUnusedEdges.add(currentNode);
                    nextNode = nextNodes.toArray()[this.random.nextInt(nextNodes.size())];
                    nextNodes.remove(nextNode);
                } else {
                    //if only one available next node, pick it and remove current node from set of nodes with unused edges
                    nextNode = nextNodes.get(0);
                    EulerianCycle.this.graph.remove(currentNode);
                    EulerianCycle.this.nodesVisitedWithUnusedEdges.remove(currentNode);
                }
                currentNode = nextNode;
                //stop when hitting a dead end node
            } while (EulerianCycle.this.graph.containsKey(nextNode));
//            System.out.println(this.nodesVisited.stream().map(o -> o.toString()).collect(Collectors.joining("->")));
//            System.out.println(EulerianCycle.this.graph);
        }

        // Constructor for starting with list of nodes visited.  Use this to construct Eulerian cycle from subcycles.
        public Cycle(LinkedList<Object> nodesVisited) {
            this.nodesVisited = new LinkedList<>(nodesVisited);
        }

        /**
         * Return list of nodes visited on cycle, looped such that list starts at given node.
         * @param startNode node to start list at
         * @return          list of visited nodes
         */
        public LinkedList<Object> getNodesVisited(Object startNode) {
            LinkedList<Object> reorderedNodesVisited = new LinkedList<>();

            int numNodesVisited = nodesVisited.size();
            int startIndex = nodesVisited.indexOf(startNode);

            reorderedNodesVisited.addAll(nodesVisited.subList(startIndex, numNodesVisited));
            if (startIndex > 0) {
                reorderedNodesVisited.addAll(nodesVisited.subList(0, startIndex));
            }

            return reorderedNodesVisited;
        }
    }

    /**
     * Return an Eulerian cycle, given the adjacency list for a de Brujin graph.
     * @return      list of Objects giving cycle
     */
    public LinkedList<Object> getNodesVisited() {

        Random random = new Random();

        //form an initial cycle by randomly walking
        Set<Object> nodeSet = graph.keySet();
        Object startNode = nodeSet.toArray()[random.nextInt(nodeSet.size())];
        Cycle subcycle = new Cycle(startNode);

        while (!graph.isEmpty()) {
//            System.out.println("Nodes visited with edges remaining: " + nodesVisitedWithUnusedEdges);

            //randomly pick new start node from set of nodes with unused edges visited in previous cycles
            int startIndex = random.nextInt(nodesVisitedWithUnusedEdges.size());
            startNode = nodesVisitedWithUnusedEdges.toArray()[startIndex];

            //get new cycle starting from new start node
            Cycle subcyclePrime = new Cycle(startNode);
            List<Object> subcyclePrimeNodesVisited = subcyclePrime.getNodesVisited(startNode);

            //loop previous cycle starting at new start node and join new cycle
            LinkedList<Object> nodesVisited = new LinkedList<>();
            nodesVisited.addAll(subcycle.getNodesVisited(startNode));
            nodesVisited.addAll(subcyclePrimeNodesVisited);
            subcycle = new Cycle(nodesVisited);
//            System.out.println("Cycle + subcycle: " + nodesVisited.stream().map(o -> o.toString()).collect(Collectors.joining("->")));
        }

        return subcycle.getNodesVisited(startNode);
    }

    /**
     * Return an Eulerian cycle, given the adjacency list for a de Brujin graph and a specified starting node.
     * @param startNode starting node
     * @return          list of Objects giving cycle
     */
    public LinkedList<Object> getNodesVisited(Object startNode) {
        Cycle subcycle = new Cycle(getNodesVisited());
        return subcycle.getNodesVisited(startNode);
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader((new InputStreamReader(System.in)));

        Map<Object, List<Object>> graph = new HashMap<>();

        Object edgeInputString;
        while ((edgeInputString = br.readLine()) != null) {
            Object[] nodeStrings = edgeInputString.toString().split(" -> ");
            Object node1 = nodeStrings[0];
            List<Object> node2 = new ArrayList<>(Arrays.asList(nodeStrings[1].toString().split(",")));
            graph.put(node1, node2);
        }

        EulerianCycle cycle = new EulerianCycle(graph);

        List<Object> nodesVisited = cycle.getNodesVisited();
        nodesVisited.add(nodesVisited.get(0));

        System.out.println(nodesVisited.stream().map(o -> o.toString()).collect(Collectors.joining("->")));
    }
}
