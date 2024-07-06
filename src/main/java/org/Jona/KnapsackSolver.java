package org.Jona;

import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.layout.HierarchicalLayout;
import org.graphstream.ui.view.Viewer;

public class KnapsackSolver {

    private KnapsackItem[] items;
    private int capacity;
    private int bestProfit;
    private boolean[] bestItems;
    private Graph decisionTree;
    private int nodeIdCounter;

    public KnapsackSolver(KnapsackItem[] items, int capacity) {
        this.items = items;
        this.capacity = capacity;
        this.bestProfit = 0;
        this.bestItems = new boolean[items.length];
        this.decisionTree = new SingleGraph("BBTree");
        HierarchicalLayout hl = new HierarchicalLayout();
        decisionTree.setAttribute("ui.stylesheet", "node{\n" +
                "    size: 30px, 30px;\n" +
                "    fill-color: #f7f7f0;\n" +
                "    text-mode: normal; \n" +
                "}");
        this.nodeIdCounter = 0;
    }

    public void solve(KnapsackItem[] items) {
        KnapsackItem[] I = items;
        int[] indices = new int[items.length];
        for (int i = 0; i < items.length; i++) {
            indices[i] = i;
        }
        String rootNode = createNodeLabel(0, 0, 0);
        decisionTree.addNode(rootNode);
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
        String leftNode = createNodeLabel(nodeIdCounter++, currentWeight, currentProfit);
        //if (currentWeight + items[index].getRuntime() <= capacity) {
            leftNode = createNodeLabel(nodeIdCounter++, currentWeight, currentProfit);
            decisionTree.addNode(leftNode).setAttribute("ui.label", leftNode);;
            decisionTree.addEdge(parentNode + leftNode,parentNode, leftNode).setAttribute("ui.label", parentNode +"not taken");;
      //  }

      //  if (currentWeight + items[index].getRuntime() <= capacity) {


       //}
        // Branch without including the current item
        branchAndBound(items, index + 1, currentWeight, currentProfit, currentItemSelection, leftNode);
        String rightNode = createNodeLabel(nodeIdCounter++, currentWeight, currentProfit);
        if (currentWeight + items[index].getRuntime() <= capacity) {
            rightNode = createNodeLabel(nodeIdCounter++, currentWeight+ items[index].getRuntime(), currentProfit + items[index].getValue());
            decisionTree.addNode(rightNode).setAttribute("ui.label", rightNode);;
            decisionTree.addEdge(parentNode + rightNode,parentNode, rightNode).setAttribute("ui.label", parentNode +"taken");;

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


    public void drawDecisionTree() {
       Viewer viewer = decisionTree.display();
       //viewer.disableAutoLayout();
    }


}
