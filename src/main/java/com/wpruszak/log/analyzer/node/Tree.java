package com.wpruszak.log.analyzer.node;

import java.util.LinkedHashMap;
import java.util.Map;

public class Tree {

    private final Map<Integer, Node> rootNodes;

    public Tree() {
        this.rootNodes = new LinkedHashMap<>();
    }

    public Map<Integer, Node> getRootNodes() {
        return this.rootNodes;
    }

    public Node getRootNode(Integer siteId) {
        return this.rootNodes.get(siteId);
    }

    public void addRootNode(Integer siteId, Node rootNode) {
        this.rootNodes.put(siteId, rootNode);
    }

    public boolean hasRootNode(Integer siteId) {
        return this.rootNodes.containsKey(siteId);
    }
}
