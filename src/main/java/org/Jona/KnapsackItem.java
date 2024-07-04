package org.Jona;

public class KnapsackItem {
    private int runtime;
    private int value;

    public KnapsackItem(int weight, int value) {
        this.runtime = weight;
        this.value = value;
    }

    public int getRuntime() {
        return runtime;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "Weight: " + runtime + ", Value: " + value;
    }
}