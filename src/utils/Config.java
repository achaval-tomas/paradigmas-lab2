package utils;

import namedEntities.heuristics.Heuristic;

import java.util.List;

public class Config {
    private final boolean printFeed;
    private final boolean computeNamedEntities;
    private final List<FeedsData> feedsData;
    private final Heuristic heuristic;
    private final StatisticsFormat statsFormat;

    public Config(boolean printFeed, boolean computeNamedEntities, List<FeedsData> feedsData,
                  Heuristic heuristic, StatisticsFormat statsFormat) {
        this.printFeed = printFeed;
        this.computeNamedEntities = computeNamedEntities;
        this.feedsData = feedsData;
        this.heuristic = heuristic;
        this.statsFormat = statsFormat;
    }

    public boolean getPrintFeed() {
        return printFeed;
    }

    public boolean getComputeNamedEntities() {
        return computeNamedEntities;
    }

    public List<FeedsData> getFeedsData() {
        return feedsData;
    }

    public Heuristic getHeuristic() {
        return heuristic;
    }

    public StatisticsFormat getStatsFormat() {
        return statsFormat;
    }
}
