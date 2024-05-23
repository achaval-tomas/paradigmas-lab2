package utils;

public record FeedsData(String label, String url, String type) {
    public void print() {
        System.out.println("Feed: " + label);
        System.out.println("URL: " + url);
        System.out.println("Type: " + type);
    }
}
