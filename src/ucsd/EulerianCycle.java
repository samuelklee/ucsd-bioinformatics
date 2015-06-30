package ucsd;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

public class EulerianCycle {
    private Map<String, List<String>> graph;
    private Set<String> nodesVisitedWithUnusedEdges = new HashSet<>();

    public EulerianCycle(Map<String, List<String>> graph) {
        this.graph = graph;
    }

    /**
     * Nested class for constructing both subcycles and Eulerian cycle.
     */
    private class Cycle {
        private LinkedList<String> nodesVisited = new LinkedList<>();

        private Random random = new Random();

        /**
         * Constructs a random cycle (not Eulerian).  Removes edges from graph as they are visited and adds to set
         * of nodes visited with unused edges.  Use this for making new subcycles when constructing Eulerian cycle.
         * @param startNode                     starting node
         * @return                              cycle starting at given node
         */
        public Cycle(String startNode) {
//            System.out.println(EulerianCycle.this.graph);
            String currentNode = startNode;
            String nextNode;
            do {
                this.nodesVisited.offer(currentNode);
                List<String> nextNodes = EulerianCycle.this.graph.get(currentNode);

                if (nextNodes.size() > 1) {
                    //if more than one available next node, add current node to set of nodes with unused edges and
                    //pick next node randomly
                    EulerianCycle.this.nodesVisitedWithUnusedEdges.add(currentNode);
                    nextNode = (String) nextNodes.toArray()[this.random.nextInt(nextNodes.size())];
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
//            System.out.println(this.nodesVisited.stream().collect(Collectors.joining("->")));
//            System.out.println(EulerianCycle.this.graph);
        }

        // Constructor for starting with list of nodes visited.  Use this to construct Eulerian cycle from subcycles.
        public Cycle(LinkedList<String> nodesVisited) {
            this.nodesVisited = new LinkedList<>(nodesVisited);
        }

        /**
         * Return list of nodes visited on cycle, looped such that list starts at given node.
         * @param startNode node to start list at
         * @return          list of visited nodes
         */
        public LinkedList<String> getNodesVisited(String startNode) {
            LinkedList<String> reorderedNodesVisited = new LinkedList<>();

            int numNodesVisited = this.nodesVisited.size();
            int startIndex = this.nodesVisited.indexOf(startNode);

            reorderedNodesVisited.addAll(this.nodesVisited.subList(startIndex, numNodesVisited));
            if (startIndex > 0) {
                reorderedNodesVisited.addAll(this.nodesVisited.subList(0, startIndex));
            }

            return reorderedNodesVisited;
        }
    }

    /**
     * Return an Eulerian cycle, given the adjacency list for a de Brujin graph.
     * @return      list of Strings giving cycle
     */
    public LinkedList<String> getNodesVisited() {

        Random random = new Random();

        //form an initial cycle by randomly walking
        Set<String> nodeSet = this.graph.keySet();
        String startNode = (String) nodeSet.toArray()[random.nextInt(nodeSet.size())];
        Cycle subcycle = new Cycle(startNode);

        while (!this.graph.isEmpty()) {
//            System.out.println("Nodes visited with edges remaining: " + subcycle.getNodesVisitedWithUnusedEdges());

            //randomly pick new start node from set of nodes with unused edges visited in previous cycles
            int startIndex = random.nextInt(this.nodesVisitedWithUnusedEdges.size());
            startNode = (String) this.nodesVisitedWithUnusedEdges.toArray()[startIndex];

            //get new cycle starting from new start node
            Cycle subcyclePrime = new Cycle(startNode);
            List<String> subcyclePrimeNodesVisited = subcyclePrime.getNodesVisited(startNode);

            //loop previous cycle starting at new start node and join new cycle
            LinkedList<String> nodesVisited = new LinkedList<>();
            nodesVisited.addAll(subcycle.getNodesVisited(startNode));
            nodesVisited.addAll(subcyclePrimeNodesVisited);
            subcycle = new Cycle(nodesVisited);
//            System.out.println("Cycle + subcycle: " + nodesVisited.stream().collect(Collectors.joining("->")));
        }

        return subcycle.getNodesVisited(startNode);
    }

    /**
     * Return an Eulerian cycle, given the adjacency list for a de Brujin graph and a specified starting node.
     * @param startNode starting node
     * @return          list of Strings giving cycle
     */
    public LinkedList<String> getNodesVisited(String startNode) {
        Cycle subcycle = new Cycle(getNodesVisited());
        return subcycle.getNodesVisited(startNode);
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader((new InputStreamReader(System.in)));

        Map<String, List<String>> graph = new HashMap<>();

        String edgeInputString;
        while ((edgeInputString = br.readLine()) != null) {
            String[] nodeStrings = edgeInputString.split(" -> ");
            String node1 = nodeStrings[0];
            List<String> node2 = new ArrayList<>(Arrays.asList(nodeStrings[1].split(",")));
            graph.put(node1, node2);
        }

        EulerianCycle cycle = new EulerianCycle(graph);

        List<String> nodesVisited = cycle.getNodesVisited();
        nodesVisited.add(nodesVisited.get(0));

        System.out.println(nodesVisited.stream().collect(Collectors.joining("->")));
    }
}
