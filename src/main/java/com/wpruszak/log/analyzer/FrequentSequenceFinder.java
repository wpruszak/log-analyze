package com.wpruszak.log.analyzer;

import com.wpruszak.log.analyzer.node.Node;
import com.wpruszak.log.analyzer.node.Tree;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FrequentSequenceFinder {

    private final int MINIMUM_SUPPORT;

    private final Set<List<Integer>> frequentSequences;

    public FrequentSequenceFinder(final int minimumSupport) {
        this.frequentSequences = new HashSet<>();
        this.MINIMUM_SUPPORT = minimumSupport;
    }

    /**
     * List of sequences <List of sequence elements <Integer sequence element>>
     */
    public Set<List<Integer>> findFrequentSequences(final Tree tree) {

        tree.getRootNodes().entrySet().forEach(entry -> {
            Integer siteId = entry.getKey();
            Node node = entry.getValue();

            if(node.getHits() < MINIMUM_SUPPORT) {
                return;
            }

            List<Integer> ancestors = new ArrayList<>();
            ancestors.add(siteId);

            findFrequentSequencesInNode(node, ancestors);
        });

        return this.frequentSequences;
    }

    private void findFrequentSequencesInNode(final Node node, final List<Integer> ancestors) {

        node.getChildren().entrySet().forEach(entry -> {
            List<Integer> childAncestors = new ArrayList(ancestors);

            Integer siteId = entry.getKey();
            Node childNode = entry.getValue();

            if(childNode.getHits() < MINIMUM_SUPPORT) {
                return;
            }

            childAncestors.add(siteId);

            this.frequentSequences.add(childAncestors);

            final int maxSize = childAncestors.size();
            if(maxSize > MINIMUM_SUPPORT) {
                for(int i = MINIMUM_SUPPORT; i < maxSize; i++) {
                    List<Integer> subSequence = childAncestors.subList(childAncestors.size() - i, childAncestors.size());
                    this.frequentSequences.add(subSequence);
                }
            }

            this.findFrequentSequencesInNode(childNode, childAncestors);
        });
    }
}
