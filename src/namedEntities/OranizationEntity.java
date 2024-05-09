package namedEntities;

import java.util.List;

public class OranizationEntity extends NamedEntity {
    private final String orgName;
    private final String type; // ONG, SA, SRL

    public OranizationEntity(String label, List<String> topics, List<String> keywords,
                             String orgName, String type) {
        super(label, topics, keywords);
        this.orgName = orgName;
        this.type = type;
    }

    public String getOrgName() {
        return orgName;
    }

    public String getType() {
        return type;
    }
}
