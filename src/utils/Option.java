package utils;

public class Option {
    private final String name;
    private final String longName;
    private final Integer numValues;

    public Option(String name, String longName, Integer numValues) {
        this.name = name;
        this.longName = longName;
        this.numValues = numValues;
    }

    public String getName() {
        return name;
    }

    public String getLongName() {
        return longName;
    }

    public Integer getNumValues() {
        return numValues;
    }
}
