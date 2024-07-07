package org.Jona;

import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;


class Node {
    int level, profit, bound;
    float weight;

    Node(int level, int profit, float weight) {
        this.level = level;
        this.profit = profit;
        this.weight = weight;
    }

    public int getLevel() {
        return level;
    }

    public int getBound() {
        return bound;
    }

    public float getWeight() {
        return weight;
    }

    public int getProfit() {
        return profit;
    }
}


public class BBKnapsack {
    //Ration Comparator
    static Comparator<KnapsackItem> itemComparator = (a, b) -> {
        double ratio1 = (double) a.getValue() / a.getRuntime();
        double ratio2 = (double) b.getValue() / b.getRuntime();
        // Sorting in decreasing order of value per unit weight
        return Double.compare(ratio2, ratio1);
    };

    // Calculation of Bound
    static int bound(Node u, int n, int Weight, KnapsackItem[] arr) {
        if (u.weight >= Weight)
            return 0;

        int profitBound = u.profit;
        int j = u.level + 1;
        float totalWeight = u.weight;

        while (j < n && totalWeight + arr[j].getRuntime() <= Weight) {
            totalWeight += arr[j].getRuntime();
            profitBound += arr[j].getValue();
            j++;
        }

        if (j < n)
            profitBound += (int) ((Weight - totalWeight) * arr[j].getValue() / arr[j].getRuntime());

        return profitBound;
    }

    static int knapsack(int Weight, KnapsackItem[] arr, int n) throws IOException {
        Graph g1 = new Graph();
        Arrays.sort(arr, itemComparator);
        PriorityQueue<Node> priorityQueue =
                new PriorityQueue<>((a, b) -> Integer.compare(b.bound, a.bound));
        Node u, v;

        u = new Node(-1, 0, 0);
        priorityQueue.offer(u);

        int maxProfit = 0;

        while (!priorityQueue.isEmpty()) {
            u = priorityQueue.poll();

            if (u.level == -1) {
                v = new Node(0, 0, 0);

            } else if (u.level == n - 1)
                continue;
            else
                v = new Node(u.level + 1, u.profit, u.weight);
            g1.enqueNodes("Weight: " + v.getWeight() + ", Level: " + v.getLevel() + ", Profit: " + v.getProfit());
            System.out.println(g1.getEdges().toString());
            g1.setEdges(
                    "Weight: " + u.getWeight() + ", Level: " + u.getLevel() + ", Profit: " + u.getProfit(),
                    "Weight: " + v.getWeight() + ", Level: " + v.getLevel() + ", Profit: " + v.getProfit()
            );

            v.weight += arr[v.level].getRuntime();
            v.profit += arr[v.level].getValue();

            if (v.weight <= Weight && v.profit > maxProfit) {
                maxProfit = v.profit;
                g1.enqueNodes("Weight: " + v.getWeight() + ", Level: " + v.getLevel() + ", Profit: " + v.getProfit());
                g1.setEdges(
                        "Weight: " + u.getWeight() + ", Level: " + u.getLevel() + ", Profit: " + u.getProfit(),
                        "Weight: " + v.getWeight() + ", Level: " + v.getLevel() + ", Profit: " + v.getProfit()
                );
            }
            v.bound = bound(v, n, Weight, arr);
            if (v.bound > maxProfit) {
                g1.enqueNodes("Weight: " + u.getWeight() + ", Level: " + u.getLevel() + ", Profit: " + u.getProfit());
                g1.setEdges(
                        "Weight: " + u.getWeight() + ", Level: " + u.getLevel() + ", Profit: " + u.getProfit(),
                        "Weight: " + v.getWeight() + ", Level: " + v.getLevel() + ", Profit: " + v.getProfit()
                );
            }
            priorityQueue.offer(v);

            v = new Node(u.level + 1, u.profit, u.weight);
            v.bound = bound(v, n, Weight, arr);

            if (v.bound > maxProfit) {
                g1.enqueNodes("Weight: " + u.getWeight() + ", Level: " + u.getLevel() + ", Profit: " + u.getProfit());
                g1.enqueNodes("Weight: " + v.getWeight() + ", Level: " + v.getLevel() + ", Profit: " + v.getProfit());
                g1.setEdges(
                        "Weight: " + u.getWeight() + ", Level: " + u.getLevel() + ", Profit: " + u.getProfit(),
                        "Weight: " + v.getWeight() + ", Level: " + v.getLevel() + ", Profit: " + v.getProfit()
                );
            }
            priorityQueue.offer(v);
        }

        g1.drawDecisionTree();
        return maxProfit;

    }

    public static void calcBB(int W, KnapsackItem[] arr) throws IOException {
        int n = arr.length;
        int maxProfit = knapsack(W, arr, n);
        System.out.println("Maximum possible profit = " + maxProfit);
    }
}