import commit.CommitConverter;
import commit.CommitObject;
import issues.*;
import org.json.simple.parser.ParseException;
import social.DeveloperSocialHistory;
import util.MapCommitFixBugIssue;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Talal on 01.05.17.
 */
public class MetricCalculator {


    public static void main(String[] args) throws IOException, ParseException {
        List<BugIssue> listBug = new IssueConverter("output/elasticsearchBugIssues.json").getIssuesList();
        List<CommitObject> listCommit = new CommitConverter("output/logElasticSearch.txt").getCommitList();
        HashMap<BugIssue, CommitObject> map = new MapCommitFixBugIssue(listBug, listCommit).mapCommitFixToBugIssue();
        DeveloperSocialHistory developerSocialHistory = new DeveloperSocialHistory("output/mapFile.txt", listCommit, listBug);

        boolean stepToReproduce;
        boolean expectedBehaviour;
        int numberOfCommenter = 0;
        int descriptionLength;

        for (BugIssue bugIssue : listBug
                ) {
            BugIssueAnalyzer bugIssueAnalyzer = new BugIssueAnalyzer(bugIssue);
            List<CommentObject> commentList = bugIssue.getCommentList();
            stepToReproduce = bugIssueAnalyzer.isStepToReproduce();
            expectedBehaviour = bugIssueAnalyzer.isExpectedBehaviour();
            descriptionLength = bugIssueAnalyzer.getCommentsDescriptionWordsCount();
            if (commentList != null) {
                CommentsAnalyzer commentsAnalyzer = new CommentsAnalyzer(commentList);
                numberOfCommenter = commentsAnalyzer.getNumberOfCommenter();
            }

            int issueDevelopperCommitExperience = developerSocialHistory.numberOfPastCommitsOfDevelopperWhoOpenedTheBugIssue(bugIssue.getOpenedBy(), bugIssue.getOpenedOn());
            int issueDeveloperFixCommitExperience = developerSocialHistory.numberofBugFixingCommitOfDevelopperWhoOpenedTheBugIssue(bugIssue.getOpenedBy(), bugIssue.getOpenedOn());
            int fixingDeveloperCommitExperience = 0;
            int fixingDeveloperFixingExperience = 0;
            CommitObject fixCommit = map.get(bugIssue);
            if (fixCommit != null) {
                fixingDeveloperCommitExperience = developerSocialHistory.numberOfCommitsOftheDeveloperWhoFixedTheBug(fixCommit.getAuthorName(), fixCommit.getDate());
                fixingDeveloperFixingExperience = developerSocialHistory.numberOfFixCommitsOftheDeveloperWhoFixedTheBug(fixCommit.getAuthorName(), fixCommit.getDate());
            }

            int socialStrength = developerSocialHistory.SocialNetworkStrength(bugIssue);

            System.out.println(bugIssue.getNumber() + " stepToreproduce : " + stepToReproduce + " expectedBeh : "
                    + expectedBehaviour + " numberOfCommenter : " + numberOfCommenter + " description length : " + descriptionLength + " issueDevelopperCommitExperience : " + issueDevelopperCommitExperience
                    + " issueDeveloperFixCommitExperience : " + issueDeveloperFixCommitExperience + " fixingDeveloperCommitExperience :  " + fixingDeveloperCommitExperience
                    + " fixingDeveloperFixingExperience : " + fixingDeveloperFixingExperience + " socialStrength : " + socialStrength);
        }

    }


}
