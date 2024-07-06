package org.Jona;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        System.setProperty("org.graphstream.ui", "swing");

        KnapsackItem[] items = {
                //Dont remove First Item idk why
                new KnapsackItem(0, 0),
                //Dont remove
                new KnapsackItem(2, 40),
                new KnapsackItem(5, 30),
                new KnapsackItem(10, 50),
                new KnapsackItem(5, 10),
        };
        int capacity = 16;

        KnapsackSolver solver = new KnapsackSolver(items, capacity);
        solver.solve(items);

        System.out.println("Best Profit: " + solver.getBestProfit());
        boolean[] bestItems = solver.getBestItems();
        for (int i = 0; i < bestItems.length; i++) {
            if (bestItems[i]) {
                if (bestItems[i]) {
                    System.out.println("Item " + i + ": " + items[i]);
                }
            }


        }
        try {
            solver.drawDecisionTree();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

