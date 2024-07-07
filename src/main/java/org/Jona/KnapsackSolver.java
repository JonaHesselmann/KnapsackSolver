package org.Jona;


import java.io.IOException;


public class KnapsackSolver {

    private KnapsackItem[] items;
    private int capacity;
    private int bestProfit;
    private boolean[] bestItems;
    private int nodeIdCounter;
    private Graph g1;


    public KnapsackSolver(KnapsackItem[] items, int capacity) {
        this.items = items;
        this.capacity = capacity;
        this.bestProfit = 0;
        this.bestItems = new boolean[items.length];
        this.nodeIdCounter = 0;
        g1 = new Graph();
    }

    public void solve(KnapsackItem[] items) {
        KnapsackItem[] I = items;
        String rootNode = new String(createNodeLabel(0, 0, 0));
        g1.enqueNodes(rootNode);
        Backtrack(I, 0, 0, 0, new boolean[items.length], rootNode);
    }

    private void Backtrack(KnapsackItem[] items, int index, int currentWeight, int currentProfit, boolean[] currentItemSelection, String parentNode) {
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
        g1.enqueNodes(leftNode);
        g1.setEdges(parentNode, leftNode);


        Backtrack(items, index + 1, currentWeight, currentProfit, currentItemSelection, leftNode);
        String rightNode = createNodeLabel(nodeIdCounter++, currentWeight, currentProfit);
        if (currentWeight + items[index].getRuntime() <= capacity) {
            rightNode = createNodeLabel(nodeIdCounter++, currentWeight + items[index].getRuntime(), currentProfit + items[index].getValue());
            g1.enqueNodes(rightNode);
            g1.setEdges(parentNode, rightNode);
            currentItemSelection[index] = true;
            Backtrack(items, index + 1, currentWeight + items[index].getRuntime(), currentProfit + items[index].getValue(), currentItemSelection, rightNode);
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

    public void renderTree() throws IOException {
        try {
            g1.drawDecisionTree();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


}
