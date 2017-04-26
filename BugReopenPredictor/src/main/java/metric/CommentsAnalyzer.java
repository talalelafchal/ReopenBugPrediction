package metric;

import issues.CommentObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Talal on 26.04.17.
 */
public class CommentsAnalyzer {

    List<CommentObject> commentList;


    public CommentsAnalyzer(List<CommentObject> commentList) {
        this.commentList = commentList;
    }

    public boolean stepToreproduce() {
        boolean stepBool = false;
        for (CommentObject comment : commentList
                ) {
            String text = comment.getText();
            if ((text.contains("step") || text.contains("can")) &&
                    (text.contains("reproduce") || text.contains("recreate") || text.contains("observed"))) {
                stepBool = true;
            }

        }
        return stepBool;
    }


    public boolean expectedBehaviour() {
        boolean behaviour = false;
        for (CommentObject comment : commentList
                ) {
            String text = comment.getText();
            if (text.contains("should") || text.contains("must") || text.contains("instead of")) {
                behaviour = true;
            }

        }
        return expectedBehaviour();
    }


    private Set commenterSet() {
        Set<String> commenter = new HashSet<>();
        for (CommentObject comment : commentList
                ) {
            commenter.add(comment.getAuthor());
        }
        return commenter;
    }

    public int numberOfCommenter() {

        return commenterSet().size();
    }

    public List<List<String>> getAllCommentersPair() {
        List<List<String>> commentersPairList = new ArrayList<>();
        String[] commenterArray = (String[]) commenterSet().toArray(new String[commentersPairList.size()]);
        for (int i = 0; i < commenterArray.length; i++) {
            for (int j = i+1; j < commenterArray.length; j++) {
                List<String> pair = new ArrayList<>();
                pair.add(commenterArray[i]);
                pair.add(commenterArray[j]);
                commentersPairList.add(pair);
            }
        }
        return commentersPairList;
    }

}
