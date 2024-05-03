package feed;

public class Article {
    String title;
    String description;
    String pubDate;
    String link;

    Article(String title, String description, String pubDate, String link) {
        this.title = title;
        this.description = description;
        this.pubDate = pubDate;
        this.link = link;
    }

    void print() {
        System.out.println("Title: " + title);
        System.out.println("Description: " + description);
        System.out.println("Publication date: " + pubDate);
        System.out.println("Link: " + link);
        System.out.println("********************************************************************************");
    }
}
