package utils;

import namedEntities.heuristics.Heuristic;

import java.util.List;

public record Config(
        boolean printFeed,
        boolean computeNamedEntities,
        List<FeedsData> feedsData,
        Heuristic heuristic,
        StatisticsFormat statsFormat
) {
}
