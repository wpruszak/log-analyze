package com.wpruszak.log.analyzer;

import com.wpruszak.log.analyzer.node.Node;
import com.wpruszak.log.analyzer.node.Tree;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TreeBuilder {

    private final String LOG_FILE_PATH;

    private LineIterator lineIterator;

    public TreeBuilder(final int sampleSize) {
        this.LOG_FILE_PATH = String.format("src/main/resources/%dk", sampleSize);
        this.lineIterator = this.buildLineIterator();
    }

    public Tree buildTree() {

        Tree tree = new Tree();

        while (this.lineIterator.hasNext()) {
            List<Integer> siteIds = this.extractSiteIds(this.lineIterator.nextLine());

            Node node;
            Integer siteId = siteIds.get(0);
            siteIds.remove(0);
            if(tree.hasRootNode(siteId)) {
                node = tree.getRootNode(siteId);
            } else {
                node = new Node();
                tree.addRootNode(siteId, node);
            }
            node.incrementHits();

            this.buildBranch(node, siteIds);
        }

        return tree;
    }

    private void buildBranch(Node node, List<Integer> siteIds) {

        if(siteIds.size() <= 0) {
            return;
        }

        Integer siteId = siteIds.get(0);
        siteIds.remove(0);

        Node child;
        if(node.hasChild(siteId)) {
            child = node.getChild(siteId);
        } else {
            child = new Node();
            node.addChild(siteId, child);
        }
        child.incrementHits();

        this.buildBranch(child, siteIds);
    }

    private List<Integer> extractSiteIds(String rawHits) {

        Scanner scanner = new Scanner(rawHits);
        List<Integer> list = new ArrayList<>();

        while (scanner.hasNextInt()) {
            list.add(scanner.nextInt());
        }

        return list;
    }

    private LineIterator buildLineIterator() {

        try {
            return FileUtils.lineIterator(FileUtils.getFile(LOG_FILE_PATH));
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        return null;
    }
}
