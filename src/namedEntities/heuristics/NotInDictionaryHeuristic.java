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
    private final HashSet<String> dictionary;
    private final HashSet<String> prefixes;

    public NotInDictionaryHeuristic() throws IOException {
        dictionary = new HashSet<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("./spanish_dict.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String word = line.trim();
                if (word.isEmpty()) {
                    continue;
                }

                word = Normalizer.normalize(word, Normalizer.Form.NFD);
                word = word.replaceAll("\\p{M}", "");
                dictionary.add(word);
                dictionary.add(word + "es");
                dictionary.add(word + "s");
            }
        }

        prefixes = new HashSet<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("./spanish_prefixes.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String prefix = line.trim().replace("-", "");
                if (prefix.isEmpty()) {
                    continue;
                }

                prefix = Normalizer.normalize(prefix, Normalizer.Form.NFD);
                prefix = prefix.replaceAll("\\p{M}", "");
                prefixes.add(prefix);
            }
        }
    }

    public List<String> extractCandidates(String text) {
        text = text.replaceAll("[-+.^:,\"]", "");
        text = Normalizer.normalize(text, Normalizer.Form.NFD);
        text = text.replaceAll("\\p{M}", "");
        text = text.toLowerCase();

        Pattern pattern = Pattern.compile("[A-Za-z]+");
        Matcher matcher = pattern.matcher(text);

        List<String> candidates = new ArrayList<>();

        while (matcher.find()) {
            String word = matcher.group();

            if (isNamedEntity(word)) {
                candidates.add(word);
            }
        }

        return candidates;
    }

    public boolean isNamedEntity(String word) {
        if (dictionary.contains(word)) {
            return false;
        }

        for (int i = 1; i <= word.length(); i++) {
            var prefix = word.substring(0, i);
            var actualWord = word.substring(i);
            if (prefixes.contains(prefix) && dictionary.contains(actualWord)) {
                return false;
            }
        }

        return true;
    }
}
