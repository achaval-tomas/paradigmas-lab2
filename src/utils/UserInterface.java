package utils;

import namedEntities.heuristics.Heuristic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

    public Config handleInput(String[] args) {

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

        String feedKey = optionDict.get("-f");

        Heuristic heuristic = null;
        String heuristicName = "";
        if (computeNamedEntities) {
            heuristicName = optionDict.get("-ne");
            String className = "namedEntities.heuristics." + heuristicName + "Heuristic";

            try {
                heuristic = (Heuristic) Class.forName(className)
                        .getDeclaredConstructor().newInstance();
            } catch (ClassNotFoundException e) {
                System.out.println("The provided heuristic does not exist");
                System.exit(1);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        return new Config(printFeed, computeNamedEntities, feedKey, heuristic, heuristicName, statsFormat);
    }
}
