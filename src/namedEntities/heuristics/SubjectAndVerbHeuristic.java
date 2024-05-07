package namedEntities.heuristics;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SubjectAndVerbHeuristic {
    public List<String> extractCandidates(String text) {
        List<String> candidates = new ArrayList<>();

        text = text.replaceAll("[-+.^:,\"]", "");
        text = Normalizer.normalize(text, Normalizer.Form.NFC);
        text = text.replaceAll("\\p{M}", "");
        
        Pattern pattern = Pattern.compile("(?<name>([A-Z][A-Za-z]+)(?:\\s[A-Z][a-z]+)*)(?:\\sse)?(?:\\s[a-z]+[áéíóú])");

        Matcher matcher = pattern.matcher(text);

        while (matcher.find()) {
            candidates.add(matcher.group("name"));
        }
        return candidates;
    }
}
