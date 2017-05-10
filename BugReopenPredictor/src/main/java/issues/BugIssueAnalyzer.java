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
        if (description.contains("should") || description.contains("must") || description.contains("instead of")
                || description.contains("behaviour")) {
            behaviour = true;
        }
        return behaviour;
    }

    public boolean isStepToReproduce() {
        boolean stepBool = false;
        if (description == null) {
            return false;
        }

        if ((description.contains("step") || description.contains("can")) &&
                (description.contains("reproduce") || description.contains("recreate") ||
                        description.contains("observed"))) {
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
