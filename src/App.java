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

        List<FeedData> feedsData = JSONParser.parseJsonFeedsData("src/data/feeds.json");

        UserInterface ui = new UserInterface();
        Config config = ui.handleInput(args, feedsData, heuristics);

        run(config);
    }

    private static void run(Config config) throws ParserConfigurationException, IOException, SAXException {
        if (config.feedsData().isEmpty()) {
            System.out.println("No feeds data found");
            return;
        }

        List<Article> articles = new ArrayList<>();
        for (FeedData feed : config.feedsData()) {
            String xml;
            try {
                xml = FeedParser.fetchFeed(feed.url());
            } catch (Exception e) {
                System.out.println("Failed to fetch feed with message: " + e.getMessage());
                System.exit(1);
                return;
            }
            articles.addAll(FeedParser.parseXML(xml));
        }

        if (config.printFeed()) {
            printFeed(articles);
        }

        if (config.heuristic() != null) {
            computeNamedEntities(articles, config.heuristic(), config.statsFormat());
        }
    }

    private static void printFeed(List<Article> articles) {
        System.out.println("Printing feed(s) ");
        for (Article article : articles) {
            article.print();
        }
    }

    private static void computeNamedEntities(List<Article> articles, Heuristic heuristic, StatisticsFormat statsFormat)
            throws IOException {
        System.out.printf("Computing named entities using '%s' heuristic.\n", heuristic.getLongName());

        var candidates = new ArrayList<String>();
        for (Article article : articles) {
            candidates.addAll(heuristic.extractCandidates(article.description()));
            candidates.addAll(heuristic.extractCandidates(article.title()));
        }

        List<NamedEntity> namedEntities = extractNamedEntities(candidates);

        System.out.println();
        switch (statsFormat) {
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
        for (var topic : statsByTopic.keySet()) {
            System.out.printf("Topic: %s\n", topic);
            var stats = statsByTopic.get(topic);
            for (var entity : stats.keySet()) {
                System.out.printf("        %s (%d)\n", entity.getLabel(), stats.get(entity));
            }
        }
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
