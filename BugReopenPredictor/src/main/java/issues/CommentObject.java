package issues;

/**
 * Created by Talal on 23.03.17.
 */
public class CommentObject {
    String author;
    String date;
    String text;

    public CommentObject(String author, String date, String text) {
        this.author = author;
        this.date = date;
        this.text = text;
    }

    public String getAuthor() {
        return author;
    }

    public String getDate() {
        return date;
    }

    public String getText() {
        return text;
    }
}
