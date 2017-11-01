package com.wpruszak.log.analyzer.node;

import java.util.LinkedHashMap;
import java.util.Map;

public class Node {

    private int hits = 0;

    /**
     * Map<Integer siteId, Node node>
     */
    private Map<Integer, Node> children;

    public Node() {
        this.children = new LinkedHashMap<>();
    }

    public Map<Integer, Node> getChildren() {
        return this.children;
    }

    public void addChild(Integer siteId, Node child) {
        this.children.put(siteId, child);
    }

    public Node getChild(Integer siteId) {
        return this.children.get(siteId);
    }

    public boolean hasChild(Integer siteId) {
        return this.children.containsKey(siteId);
    }

    public int getHits() {
        return this.hits;
    }

    public void incrementHits() {
        this.hits++;
    }
}
