package commit;

import java.util.List;

/**
 * Created by Talal on 30.04.17.
 */
public class CommitObject {
    String  id;
    String authorName;
    String date;
    String message;
    List<String> modifiedFiles;


    public CommitObject(String id, String authorName, String date, String message, List<String> modifiedFiles) {
        this.id = id;
        this.authorName = authorName;
        this.date = date;
        this.message = message;
        this.modifiedFiles = modifiedFiles;

    }

    public String getId() {
        return id;
    }

    public String getAuthorName() {
        return authorName;
    }

    public String getDate() {
        return date;
    }

    public String getMessage() {
        return message;
    }

    public List<String> getModifiedFiles() {
        return modifiedFiles;
    }


}

