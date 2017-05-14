package issues;

import java.util.List;

/**
 * Created by Talal on 30.04.17.
 */
public class BugIssueAnalyzer {
    BugIssue bugIssue;
    String description;
    List<CommentObject> commentsList;

    public BugIssueAnalyzer(BugIssue bugIssue) {
        this.bugIssue = bugIssue;
        this.description = bugIssue.getDescription();
        this.commentsList = bugIssue.getCommentList();
    }

    public boolean isExpectedBehaviour() {
        boolean behaviour = false;
        if (description == null) {
            return false;
        }
        if (description.toLowerCase().contains("should") || description.toLowerCase().contains("must") || description.toLowerCase().contains("instead of")
                || description.toLowerCase().contains("behaviour")) {
            behaviour = true;
        }
        return behaviour;
    }

    public boolean isStepToReproduce() {
        boolean stepBool = false;
        if (description == null) {
            return false;
        }

        if ((description.toLowerCase().contains("step") || description.toLowerCase().contains("can")) &&
                (description.toLowerCase().contains("reproduce") || description.toLowerCase().contains("recreate") ||
                        description.toLowerCase().contains("observed"))) {
            stepBool = true;
        }

        return stepBool;
    }

    public int getCommentsDescriptionWordsCount() {
        if (description == null) {
            return 0;
        }
        return description.split(" ").length;
    }

}
