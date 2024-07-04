package org.Jona;

import com.mxgraph.swing.mxGraphComponent;
import org.jgrapht.Graph;
import org.jgrapht.ext.JGraphXAdapter;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;

import javax.swing.*;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

public class KnapsackSolver {

    private KnapsackItem[] items;
    private int capacity;
    private int bestProfit;
    private boolean[] bestItems;
    private Graph<String, DefaultEdge> decisionTree;
    private int nodeIdCounter;

    public KnapsackSolver(KnapsackItem[] items, int capacity) {
        this.items = items;
        this.capacity = capacity;
        this.bestProfit = 0;
        this.bestItems = new boolean[items.length];
        this.decisionTree = new DefaultDirectedGraph<>(DefaultEdge.class);
        this.nodeIdCounter = 0;
    }

    public void solve(KnapsackItem[] items) {
        KnapsackItem[] I = items;
        int[] indices = new int[items.length];
        for (int i = 0; i < items.length; i++) {
            indices[i] = i;
        }
        String rootNode = createNodeLabel(0, 0, 0);
        decisionTree.addVertex(rootNode);
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

            decisionTree.addVertex(leftNode);
            decisionTree.addEdge(parentNode, leftNode);
       // }

      //  if (currentWeight + items[index].getRuntime() <= capacity) {


       //}
        // Branch without including the current item
        branchAndBound(items, index + 1, currentWeight, currentProfit, currentItemSelection, leftNode);
        String rightNode = createNodeLabel(nodeIdCounter++, currentWeight, currentProfit);
        if (currentWeight + items[index].getRuntime() <= capacity) {
            rightNode = createNodeLabel(nodeIdCounter++, currentWeight+ items[index].getRuntime(), currentProfit + items[index].getValue());
            decisionTree.addVertex(rightNode);
            decisionTree.addEdge(parentNode, rightNode);

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
        JGraphXAdapter<String, DefaultEdge> graphAdapter = new JGraphXAdapter<>(decisionTree);
        mxGraphComponent graphComponent = new mxGraphComponent(graphAdapter);

        mxHierarchicalLayout layout = new mxHierarchicalLayout(graphAdapter);
        layout.setIntraCellSpacing(40);

        layout.setInterHierarchySpacing(70);
        layout.setParentBorder(7000);


//        layout.setInterRankCellSpacing(70);
//        layout.setParallelEdgeSpacing(70);
        layout.execute(graphAdapter.getDefaultParent());

        // Adding mouse wheel listener for zoom functionality
        graphComponent.getGraphControl().addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                if (e.getWheelRotation() < 0) {
                    graphComponent.zoomIn();
                } else {
                    graphComponent.zoomOut();
                }
            }
        });
        // Set initial scale
        graphComponent.zoomTo(2.0, true); // Initial zoom level set to 200%

        // Increase the preferred size of the graph component
        graphComponent.setGridVisible(true);


        JFrame frame = new JFrame();
        frame.getContentPane().add(graphComponent);
        frame.setTitle("Branch and Bound Decision Tree");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }


}
