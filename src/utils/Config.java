package utils;

import namedEntities.heuristics.Heuristic;

public class Config {
    private boolean printFeed = false;
    private boolean computeNamedEntities = false;
    private String feedKey;
    private Heuristic heuristic;
    private String heuristicName;
    private StatisticsFormat statsFormat;

    public Config(boolean printFeed, boolean computeNamedEntities, String feedKey,
                  Heuristic heuristic, String heuristicName, StatisticsFormat statsFormat) {
        this.printFeed = printFeed;
        this.computeNamedEntities = computeNamedEntities;
        this.feedKey = feedKey;
        this.heuristic = heuristic;
        this.heuristicName = heuristicName;
        this.statsFormat = statsFormat;
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

    public Heuristic getHeuristic() { return heuristic; }

    public String getHeuristicName() { return heuristicName; }

    public StatisticsFormat getStatsFormat() {
        return statsFormat;
    }
}
