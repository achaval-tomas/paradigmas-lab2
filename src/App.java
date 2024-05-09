import feed.Article;
import feed.FeedParser;
import namedEntities.heuristics.Heuristic;

import org.xml.sax.SAXException;
import utils.Config;
import utils.FeedsData;
import utils.JSONParser;
import utils.UserInterface;



import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class App {

    public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException {

        List<FeedsData> feedsDataArray = new ArrayList<>();
        try {
            feedsDataArray = JSONParser.parseJsonFeedsData("src/data/feeds.json");
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        UserInterface ui = new UserInterface();
        Config config = ui.handleInput(args);

        run(config, feedsDataArray);
    }

    // TODO: Change the signature of this function if needed
    private static void run(Config config, List<FeedsData> feedsDataArray)
            throws ParserConfigurationException, IOException, SAXException {

        byte[] encoded = Files.readAllBytes(Paths.get("news.xml"));
        String xml = new String(encoded);

        List<Article> articles = FeedParser.parseXML(xml);
        Heuristic heuristic = config.getHeuristic();

        if (feedsDataArray == null || feedsDataArray.isEmpty()) {
            System.out.println("No feeds data found");
            return;
        }

        List<Article> allArticles = new ArrayList<>();
        // TODO: Populate allArticles with articles from corresponding feeds

        if (config.getPrintFeed()) {
            System.out.println("Printing feed(s) ");
            // TODO: Print the fetched feed
        }

        if (config.getComputeNamedEntities()) {
            System.out.println("Computing named entities using " + config.getHeuristicName());

            for (Article article : articles) {
                List<String> nmdEntities = heuristic.extractCandidates(article.getDescription());

                for (String entity : nmdEntities) {
                    System.out.println(entity);
                }

                nmdEntities = heuristic.extractCandidates(article.getTitle());

                for (String entity : nmdEntities) {
                    System.out.println(entity);
                }
            }

            // TODO: Print stats
            System.out.println("\nStats: ");
            System.out.println("-".repeat(80));
        }
    }

    // TODO: Maybe relocate this function where it makes more sense
    private static void printHelp(List<FeedsData> feedsDataArray) {
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
        System.out.println("                                       Available heuristic names are: ");
        System.out.println("                                       CapitalizedWord: Match connected capitalized words.");
        System.out.println("                                       NotInDictionary: Match words not found in a common spanish dictionary.");
        System.out.println("                                       SubjectAndVerb: Match subjects that are followed by verbs.");
        System.out.println("  -pf, --print-feed:                   Print the fetched feed");
        System.out.println("  -sf, --stats-format <format>:        Print the stats in the specified format");
        System.out.println("                                       Available formats are: ");
        System.out.println("                                       cat: Category-wise stats");
        System.out.println("                                       topic: Topic-wise stats");
    }

}
