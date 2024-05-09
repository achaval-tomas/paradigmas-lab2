package namedEntities;

import java.util.List;

abstract class NamedEntity {
    protected final String label;
    protected final List<String> topics;
    protected final List<String> keywords;

    protected NamedEntity(String label, List<String> topics, List<String> keywords) {
        this.label = label;
        this.topics = topics;
        this.keywords = keywords;
    }

    public String getLabel() {
        return label;
    }

    public List<String> getTopics() {
        return topics;
    }

    public List<String> getKeywords() {
        return keywords;
    }
}
