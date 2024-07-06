package org.Jona;


import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.MutableGraph;
import org.apache.commons.math3.util.Pair;

import java.io.File;
import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import static guru.nidi.graphviz.model.Factory.mutGraph;
import static guru.nidi.graphviz.model.Factory.mutNode;


public class KnapsackSolver {

    private KnapsackItem[] items;
    private int capacity;
    private int bestProfit;
    private boolean[] bestItems;
    private Queue<String> Nodes;
    private Queue<Pair<String, String>> Edges;
    private int nodeIdCounter;


    public KnapsackSolver(KnapsackItem[] items, int capacity) {
        this.items = items;
        this.capacity = capacity;
        this.bestProfit = 0;
        this.bestItems = new boolean[items.length];
        this.nodeIdCounter = 0;
        this.Nodes = new ConcurrentLinkedQueue<>();
        this.Edges = new ConcurrentLinkedQueue<>();
    }

    public void solve(KnapsackItem[] items) {
        KnapsackItem[] I = items;
        int[] indices = new int[items.length];
        for (int i = 0; i < items.length; i++) {
            indices[i] = i;
        }
        String rootNode = new String(createNodeLabel(0, 0, 0));
        branchAndBound(I, 0, 0, 0, new boolean[items.length], rootNode);
    }

    private void branchAndBound(KnapsackItem[] items, int index, int currentWeight, int currentProfit, boolean[] currentItemSelection, String parentNode) {
        if (currentWeight > capacity) {
            return;
        }

        if (currentProfit > bestProfit) {
            bestProfit = currentProfit;
            bestItems = currentItemSelection.clone();
        }

        if (index == items.length) {
            return;
        }
//
            String leftNode = createNodeLabel(nodeIdCounter++, currentWeight, currentProfit);
            Nodes.add(leftNode);
            Edges.add(Pair.create(parentNode, leftNode));

        // Branch without including the current item

        branchAndBound(items, index + 1, currentWeight, currentProfit, currentItemSelection, leftNode);
        //String rightNode = createNodeLabel(nodeIdCounter++, currentWeight, currentProfit);
        if (currentWeight + items[index].getRuntime() <= capacity) {
          String  rightNode = createNodeLabel(nodeIdCounter++, currentWeight +items[index].getRuntime() , currentProfit + items[index].getValue());
            Nodes.add(rightNode);
            Edges.add(Pair.create(parentNode, rightNode));


            // Branch including the current item
            currentItemSelection[index] = true;
            branchAndBound(items, index + 1, currentWeight + items[index].getRuntime(), currentProfit + items[index].getValue(), currentItemSelection, rightNode);
            currentItemSelection[index] = false;
        }
    }

    public boolean[] getBestItems() {
        return bestItems;
    }

    private String createNodeLabel(int index, int weight, int value) {
        String nodeLabel = "index " + (index) + " weight " + (weight) + " value " + value;
        return nodeLabel;
    }

    public int getBestProfit() {
        return bestProfit;
    }


    public void drawDecisionTree() throws IOException {
        MutableGraph g = mutGraph("example1").setDirected(true).use((gr, ctx) -> {
            for(int i = 0; i < Nodes.size(); ++i ){
                mutNode(Nodes.remove());
            }
           for(int i = 0; i < Edges.size(); ++i ){
               mutNode(Edges.peek().getKey()).addLink(mutNode(Edges.remove().getValue()));
           }

        });

        Graphviz.fromGraph(g).width(2000).render(Format.PNG).toFile(new File("/home/jona/IdeaProjects/KnapsackSolver/src/main/java/org/Jona/ex1i.png"));

    }
}
