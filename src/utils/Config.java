package utils;

import namedEntities.heuristics.Heuristic;

public class Config {
    private boolean printHelp;
    private boolean printFeed = false;
    private boolean computeNamedEntities = false;
    private String feedKey;
    private Heuristic heuristic;
    private StatisticsFormat statsFormat;

    public Config(boolean printHelp, boolean printFeed, boolean computeNamedEntities, String feedKey,
                  Heuristic heuristic, StatisticsFormat statsFormat) {
        this.printHelp = printHelp;
        this.printFeed = printFeed;
        this.computeNamedEntities = computeNamedEntities;
        this.feedKey = feedKey;
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

    public String getFeedKey() {
        return feedKey;
    }

    public Heuristic getHeuristic() {
        return heuristic;
    }

    public StatisticsFormat getStatsFormat() {
        return statsFormat;
    }
}
