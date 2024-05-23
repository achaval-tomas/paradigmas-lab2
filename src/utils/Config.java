package utils;

import namedEntities.heuristics.Heuristic;

import java.util.List;

public class Config {
    private boolean printFeed = false;
    private boolean computeNamedEntities = false;
    private List<FeedsData> feedsData;
    private Heuristic heuristic;
    private StatisticsFormat statsFormat;

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
