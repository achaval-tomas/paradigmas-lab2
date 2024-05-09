package namedEntities.heuristics;

import java.util.List;

public interface Heuristic {
    List<String> extractCandidates(String text);
}
