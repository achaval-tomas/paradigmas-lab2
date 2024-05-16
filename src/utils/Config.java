package utils;

import namedEntities.heuristics.Heuristic;

public class Config {
    private boolean printHelp;
    private boolean printFeed = false;
    private boolean computeNamedEntities = false;
    private FeedsData feedData;
    private Heuristic heuristic;
    private StatisticsFormat statsFormat;

    public Config(boolean printHelp, boolean printFeed, boolean computeNamedEntities, FeedsData feedData,
                  Heuristic heuristic, StatisticsFormat statsFormat) {
        this.printHelp = printHelp;
        this.printFeed = printFeed;
        this.computeNamedEntities = computeNamedEntities;
        this.feedData = feedData;
        this.heuristic = heuristic;
        this.statsFormat = statsFormat;
    }

    public boolean getPrintHelp() {
        return printHelp;
    }

    public boolean getPrintFeed() {
        return printFeed;
    }

    public boolean getComputeNamedEntities() {
        return computeNamedEntities;
    }

    public FeedsData getFeedData() {
        return feedData;
    }

    public Heuristic getHeuristic() {
        return heuristic;
    }

    public StatisticsFormat getStatsFormat() {
        return statsFormat;
    }
}
