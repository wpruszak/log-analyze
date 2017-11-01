package com.wpruszak.log.analyzer;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class BucketSort {

    public Map<Integer, Set<List<Integer>>> bucketSort(Set<List<Integer>> sequences) {

        Map<Integer, Set<List<Integer>>> buckets = new HashMap<>();

        sequences.forEach(sequence -> {
            if (buckets.containsKey(sequence.size())) {
                buckets.get(sequence.size()).add(sequence);
            } else {
                Set<List<Integer>> list = new HashSet<>();
                list.add(sequence);
                buckets.put(sequence.size(), list);
            }
        });

        return buckets;
    }
}
