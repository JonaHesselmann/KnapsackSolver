package org.Jona;

public class Main {
    public static void main(String[] args) {
        System.setProperty("org.graphstream.ui", "swing");

        KnapsackItem[] items = {
                new KnapsackItem(4, 40),
                new KnapsackItem(7, 42),
                new KnapsackItem(5, 25),
                new KnapsackItem(3, 12)
        };
        int capacity = 10;

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
        solver.drawDecisionTree();
    }
}

