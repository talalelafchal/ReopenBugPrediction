package issues;

import issues.CommentObject;

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

    private Set<String> commenterSet() {
        Set<String> commenter = new HashSet<>();
        for (CommentObject comment : commentList
                ) {
            commenter.add(comment.getAuthor());
        }
        return commenter;
    }

    public int getNumberOfCommenter() {

        return commenterSet().size();
    }

    public List<List<String>> getAllCommentersPair() {
        List<List<String>> commentersPairList = new ArrayList<>();
        String[] commenterArray = (String[]) commenterSet().toArray(new String[commentersPairList.size()]);
        for (int i = 0; i < commenterArray.length; i++) {
            for (int j = i + 1; j < commenterArray.length; j++) {
                List<String> pair = new ArrayList<>();
                pair.add(commenterArray[i]);
                pair.add(commenterArray[j]);
                commentersPairList.add(pair);
            }
        }
        return commentersPairList;
    }

}
