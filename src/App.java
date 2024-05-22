import feed.Article;
import feed.FeedParser;
import namedEntities.NamedEntity;
import namedEntities.heuristics.CapitalizedWordHeuristic;
import namedEntities.heuristics.Heuristic;
import namedEntities.heuristics.NotInDictionaryHeuristic;
import namedEntities.heuristics.SubjectAndVerbHeuristic;
import org.xml.sax.SAXException;
import utils.*;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class App {

    public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException {
        var heuristics = new ArrayList<Heuristic>();
        heuristics.add(new CapitalizedWordHeuristic());
        heuristics.add(new SubjectAndVerbHeuristic());
        heuristics.add(new NotInDictionaryHeuristic());

        List<FeedsData> feedsDataArray = new ArrayList<>();
        try {
            feedsDataArray = JSONParser.parseJsonFeedsData("src/data/feeds.json");
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        UserInterface ui = new UserInterface();
        Config config = ui.handleInput(args, heuristics);

        run(config, feedsDataArray, heuristics);
    }

    private static void run(Config config, List<FeedsData> feedsDataArray, List<Heuristic> heuristics)
            throws ParserConfigurationException, IOException, SAXException {

        if (config.getPrintHelp()) {
            printHelp(feedsDataArray, heuristics);
            return;
        }

        if (feedsDataArray == null || feedsDataArray.isEmpty()) {
            System.out.println("No feeds data found");
            return;
        }

        List<Article> articles = new ArrayList<>();
        for (FeedsData feed : config.getFeedsData()) {
            String xml;
            try {
                xml = FeedParser.fetchFeed(feed.getUrl());
            } catch (Exception e) {
                System.out.println("Failed to fetch feed with message: " + e.getMessage());
                System.exit(1);
                return;
            }
            articles.addAll(FeedParser.parseXML(xml));
        }

        Heuristic heuristic = config.getHeuristic();


        if (config.getPrintFeed()) {
            System.out.println("Printing feed(s) ");
            for (Article article : articles) {
                article.print();
            }
        }

        if (config.getComputeNamedEntities()) {
            computeNamedEntities(config, articles, heuristic);
        }
    }

    private static void computeNamedEntities(Config config, List<Article> articles, Heuristic heuristic) throws IOException {
        System.out.printf("Computing named entities using %s heuristic.\n", config.getHeuristic().getLongName());

        var candidates = new ArrayList<String>();
        for (Article article : articles) {
            candidates.addAll(heuristic.extractCandidates(article.getDescription()));
            candidates.addAll(heuristic.extractCandidates(article.getTitle()));
        }
        for (String candidate : candidates) {
            System.out.println(candidate);
        }

        List<NamedEntity> namedEntities = extractNamedEntities(candidates);

        System.out.println("\nStats: ");
        switch (config.getStatsFormat()) {
            case Category -> printStatsByCategory(namedEntities);
            case Topic -> printStatsByTopic(namedEntities);
        }
        System.out.println("-".repeat(80));
    }

    private static void printStatsByCategory(List<NamedEntity> namedEntities) {
        var statsByCategory = new HashMap<String, HashMap<NamedEntity, Integer>>();
        for (NamedEntity namedEntity : namedEntities) {
            var category = namedEntity.getCategoryName();
            var categoryCounts = statsByCategory.computeIfAbsent(category, k -> new HashMap<>());

            var count = categoryCounts.getOrDefault(namedEntity, 0);
            categoryCounts.put(namedEntity, count + 1);
        }

        for (var category : statsByCategory.keySet()) {
            System.out.printf("Category: %s\n", category);
            var stats = statsByCategory.get(category);
            for (var entity : stats.keySet()) {
                System.out.printf("        %s (%d)\n", entity.getLabel(), stats.get(entity));
            }
        }
    }

    private static void printStatsByTopic(List<NamedEntity> namedEntities) {
        var statsByTopic = new HashMap<String, HashMap<NamedEntity, Integer>>();
        for (NamedEntity namedEntity : namedEntities) {
            var topics = namedEntity.getTopics();

            for (var topic : topics) {
                var categoryCounts = statsByTopic.computeIfAbsent(topic, k -> new HashMap<>());

                var count = categoryCounts.getOrDefault(namedEntity, 0);
                categoryCounts.put(namedEntity, count + 1);
            }
        }
    }


    // TODO: Maybe relocate this function where it makes more sense
    private static void printHelp(List<FeedsData> feedsDataArray, List<Heuristic> heuristics) {
        System.out.println("Usage: make run ARGS=\"[OPTION]\"");
        System.out.println("Options:");
        System.out.println("  -h, --help: Show this help message and exit");
        System.out.println("  -f, --feed <feedKey>:                Fetch and process the feed with");
        System.out.println("                                       the specified key");
        System.out.println("                                       Available feed keys are: ");
        for (FeedsData feedData : feedsDataArray) {
            System.out.println("                                       " + feedData.getLabel());
        }
        System.out.println("  -ne, --named-entity <heuristicName>: Use the specified heuristic to extract");
        System.out.println("                                       named entities");

        System.out.println("        Available heuristic names are: ");
        for (Heuristic h : heuristics) {
            System.out.printf("                %s, \"%s\": %s\n", h.getShortName(), h.getLongName(), h.getDescription());
        }

        System.out.println("  -pf, --print-feed:                   Print the fetched feed");
        System.out.println("  -sf, --stats-format <format>:        Print the stats in the specified format");
        System.out.println("                                       Available formats are: ");
        System.out.println("                                       cat: Category-wise stats");
        System.out.println("                                       topic: Topic-wise stats");
    }

    private static List<NamedEntity> extractNamedEntities(List<String> candidates) throws IOException {
        HashMap<String, NamedEntity> namedEntitiesByKeywords = JSONParser.parseJsonDict("src/data/dictionary.json");

        var namedEntities = new ArrayList<NamedEntity>();
        for (String candidate : candidates) {
            var entity = namedEntitiesByKeywords.get(StringUtils.simplify(candidate));
            if (entity == null) {
                continue;
            }

            namedEntities.add(entity);
        }

        return namedEntities;
    }
}
