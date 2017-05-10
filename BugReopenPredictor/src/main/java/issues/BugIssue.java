package issues;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Talal on 23.03.17.
 */
public class BugIssue {

    String issueid;
    String number;
    String title;
    String description;

    List<CommentObject> commentList;
    String openedBy;
    String openedOn;

    boolean reopen;
    String reopenOn;
    String closedOn;
    String closedBy;

    public BugIssue(String issueid, String number, String title, String description, List<CommentObject> commentList, String openedBy, String openedOn, boolean reopen,
                    String closedOn,String reopenOn, String closedBy) {
        this.issueid = issueid;
        this.number = number;
        this.title = title;
        this.description = description;
        this.commentList = commentList;
        this.openedBy = openedBy;
        this.openedOn = openedOn;
        this.reopen = reopen;
        this.closedOn = closedOn;
        this.reopenOn = reopenOn;
        this.closedBy= closedBy;
    }

    public String getIssueid() {
        return issueid;
    }

    public String getNumber() {
        return number;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public List<CommentObject> getCommentList() {
        return commentList;
    }

    public String getOpenedBy() {
        return openedBy;
    }

    public String getOpenedOn() {
        return openedOn;
    }

    public boolean isReopen() {
        return reopen;
    }

    public String getClosedOn() {
        return closedOn;
    }

    public String getReopenOn() {
        return reopenOn;
    }

    public String getClosedBy() {
        return closedBy;
    }
}
