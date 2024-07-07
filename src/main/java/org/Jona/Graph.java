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

public class Graph {
    private Queue<String> Nodes;
    private Queue<Pair<String, String>> Edges;

    public Graph() {
        this.Nodes = new ConcurrentLinkedQueue<>();
        this.Edges = new ConcurrentLinkedQueue<>();
    }

    public void setEdges(String parent, String child) {

        Edges.add((Pair.create(parent, child)));

    }

    public Queue<String> getNodes() {
        return Nodes;
    }

    public void enqueNodes(String nodes) {

        Nodes.add(nodes);
    }

    public Queue<Pair<String, String>> getEdges() {
        return Edges;
    }


    public void drawDecisionTree() throws IOException {
        MutableGraph g = mutGraph("example1").setDirected(true).use((gr, ctx) -> {
            for (int i = 0; i < Nodes.size(); ++i) {
                mutNode(Nodes.remove());
            }
            for (int i = 0; i < Edges.size(); ++i) {
                mutNode(Edges.peek().getKey()).addLink(mutNode(Edges.remove().getValue()));
            }

        });

        Graphviz.fromGraph(g).width(4000).render(Format.PNG).toFile(new File("./ex2.png"));

    }
}

