package com.wpruszak.log.analyzer;

import com.wpruszak.log.analyzer.node.Tree;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class Main {

    private static final int SAMPLE_SIZE = 250;

    public static void main(String[] args) {

        final long startTime = System.nanoTime();
        final int minimumSupport = 2;
        final Tree siteHitTree = measureCreatingTreeTime();
        final Set<List<Integer>> frequentSequences = measureFindingSequenceTime(siteHitTree, minimumSupport);
        final Map<Integer, Set<List<Integer>>> sortedSequences = measureTimeSortingSequences(frequentSequences);
        final long endTime = System.nanoTime();

        printStatistics(frequentSequences, sortedSequences, timeInMilliseconds(startTime, endTime), minimumSupport);
    }

    public static Tree measureCreatingTreeTime() {

        System.out.println(String.format("Creating sequence tree for '%dk' sessions.", SAMPLE_SIZE));

        final TreeBuilder creator = new TreeBuilder(SAMPLE_SIZE);

        final long startTimeCreatingTree = System.nanoTime();
        final Tree tree = creator.buildTree();
        final long endTimeCreatingTree = System.nanoTime();

        System.out.println(String.format(
                "%dms spent on creating sequence tree.",
                timeInMilliseconds(startTimeCreatingTree, endTimeCreatingTree))
        );

        return tree;
    }

    public static Set<List<Integer>> measureFindingSequenceTime(final Tree tree, final int minimumSupport) {

        final FrequentSequenceFinder finder = new FrequentSequenceFinder(minimumSupport);

        final long startTimeFinding = System.nanoTime();
        final Set<List<Integer>> frequentSequences = finder.findFrequentSequences(tree);
        final long endTimeFinding = System.nanoTime();

        System.out.println(String.format(
                "%dms spent on finding frequent sequences with minimum support set to: '%d'.",
                timeInMilliseconds(startTimeFinding, endTimeFinding),
                minimumSupport
        ));

        return frequentSequences;
    }

    public static Map<Integer, Set<List<Integer>>> measureTimeSortingSequences(final Set<List<Integer>> sequences) {
        final BucketSort bucketSort = new BucketSort();

        final long startTimeSorting = System.nanoTime();
        final Map<Integer, Set<List<Integer>>> sortedSequences = bucketSort.bucketSort(sequences);
        final long endTimeSorting = System.nanoTime();

        System.out.println(String.format(
                "%dms spent on sorting '%d' sequences.",
                timeInMilliseconds(startTimeSorting, endTimeSorting),
                sequences.size()
        ));

        return sortedSequences;
    }

    public static void printStatistics(
            final Set<List<Integer>> frequentSequences,
            final Map<Integer, Set<List<Integer>>> sortedSequences,
            final long timeInSeconds,
            final int minimumSupport
    ) {
        final int maxSequenceSize = sortedSequences.entrySet().stream().mapToInt(entry -> entry.getKey())
                .max()
                .orElseGet(() -> 0);

        final int maxSizeSequenceCount = sortedSequences.get(maxSequenceSize).size();

        System.out.println(String.format("Overall, computing everything took '%d' milliseconds.", timeInSeconds));

        System.out.println(String.format(
                "Maximum size of single frequent sequence with minimum support set to '%d' is: '%d'. Found '%d' sequences of this size.",
                minimumSupport,
                maxSequenceSize,
                maxSizeSequenceCount
        ));

        System.out.println(String.format("Overall, found '%d' frequent sequences.", frequentSequences.size()));

        sortedSequences.entrySet().forEach(entry -> {
            System.out.println(String.format(
                    "Found '%d' frequent sequences with size: '%d' (minimum support set to '%d').",
                    entry.getValue().size(),
                    entry.getKey(),
                    minimumSupport
            ));
        });
    }

    private static long timeInMilliseconds(final long startTime, final long endTime) {
        return TimeUnit.MILLISECONDS.convert(endTime - startTime, TimeUnit.NANOSECONDS);
    }
}
