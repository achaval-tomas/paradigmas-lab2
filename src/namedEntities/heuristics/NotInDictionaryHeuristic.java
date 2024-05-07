package namedEntities.heuristics;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NotInDictionaryHeuristic {
    private HashSet<String> dictionary;

    public NotInDictionaryHeuristic() throws IOException {
        dictionary = new HashSet<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("./spanish_dict.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                dictionary.add(line.trim());
            }
        }
    }

    public List<String> extractCandidates(String text)  {
        text = text.replaceAll("[-+.^:,\"]", "");
        text = Normalizer.normalize(text, Normalizer.Form.NFC);
        text = text.replaceAll("\\p{M}", "");

        Pattern pattern = Pattern.compile("[A-Za-záéíóúñ]+");
        Matcher matcher = pattern.matcher(text);

        List<String> candidates = new ArrayList<>();

        while (matcher.find()) {
            String word = matcher.group();

            if (!dictionary.contains(word.toLowerCase())) {
                candidates.add(matcher.group());
            }
        }

        return candidates;
    }
}
