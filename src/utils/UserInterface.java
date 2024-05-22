package utils;

import namedEntities.heuristics.Heuristic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class UserInterface {

    private HashMap<String, String> optionDict;

    private List<Option> options;

    public UserInterface() {
        options = new ArrayList<>();
        options.add(new Option("-h", "--help", 0));
        options.add(new Option("-f", "--feed", 1));
        options.add(new Option("-ne", "--named-entity", 1));
        options.add(new Option("-pf", "--print-feed", 0));
        options.add(new Option("-sf", "--stats-format", 1));

        optionDict = new HashMap<>();
    }

    public Config handleInput(String[] args, List<Heuristic> heuristics) {

        for (int i = 0; i < args.length; i++) {
            for (Option option : options) {
                if (option.getName().equals(args[i]) || option.getLongName().equals(args[i])) {
                    if (option.getnumValues() == 0) {
                        optionDict.put(option.getName(), null);
                    } else {
                        if (i + 1 < args.length && !args[i + 1].startsWith("-")) {
                            optionDict.put(option.getName(), args[i + 1]);
                            i++;
                        } else {
                            System.out.println("Invalid inputs");
                            System.exit(1);
                        }
                    }
                }
            }
        }

        boolean printHelp = optionDict.containsKey("-h");
        boolean printFeed = optionDict.containsKey("-pf");
        boolean computeNamedEntities = optionDict.containsKey("-ne");
        StatisticsFormat statsFormat = StatisticsFormat.Category;
        if (optionDict.containsKey("-sf")) {
            String statsFormatOpt = optionDict.get("-sf");
            statsFormat = switch (statsFormatOpt) {
                case "cat" -> StatisticsFormat.Category;
                case "topic" -> StatisticsFormat.Topic;
                default -> statsFormat;
            };
        }

        List<FeedsData> feedsData = null;
        try {
            feedsData = JSONParser.parseJsonFeedsData("src/data/feeds.json");
        } catch (IOException e) {
            System.out.println("Failed to read file with message: " + e.getMessage());
            System.exit(1);
        }

        List<FeedsData> chosenFeeds = new ArrayList<>();
        if (!optionDict.containsKey("-f")) {
            chosenFeeds.addAll(feedsData);
        } else {
            String feedKey = optionDict.get("-f");
            feedsData.stream()
                    .filter(feed -> feed.getLabel().equals(feedKey))
                    .findFirst()
                    .ifPresent(chosenFeeds::add);
        }

        if (chosenFeeds.isEmpty()) {
            System.out.println("Feed not found");
            System.exit(1);
        }

        Heuristic heuristic = null;
        if (computeNamedEntities) {
            String heuristicName = optionDict.get("-ne").trim().toLowerCase();

            for (Heuristic h : heuristics) {
                if (h.getShortName().toLowerCase().equals(heuristicName)
                        || h.getLongName().toLowerCase().equals(heuristicName)) {
                    heuristic = h;
                    break;
                }
            }

            if (heuristic == null) {
                System.out.println("The provided heuristic does not exist");
                System.exit(1);
            }
        }

        return new Config(printHelp, printFeed, computeNamedEntities, chosenFeeds, heuristic, statsFormat);
    }
}
