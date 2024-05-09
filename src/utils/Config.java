package utils;

import namedEntities.heuristics.Heuristic;

public class Config {
    private boolean printFeed = false;
    private boolean computeNamedEntities = false;
    private String feedKey;
    private Heuristic heuristic;
    private String heuristicName;

    public Config(boolean printFeed, boolean computeNamedEntities, String feedKey,
                  Heuristic heuristic, String heuristicName) {
        this.printFeed = printFeed;
        this.computeNamedEntities = computeNamedEntities;
        this.feedKey = feedKey;
        this.heuristic = heuristic;
        this.heuristicName = heuristicName;
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
}
