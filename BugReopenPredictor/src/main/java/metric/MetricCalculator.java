package metric;

import com.github.mauricioaniche.ck.CK;
import com.github.mauricioaniche.ck.CKNumber;
import com.github.mauricioaniche.ck.CKReport;
import commit.CommitConverter;
import commit.CommitObject;
import commit.GitCommands;
import issues.*;
import model.Reporter;
import org.apache.commons.io.FileUtils;
import org.json.simple.parser.ParseException;
import social.DeveloperSocialHistory;
import textui.LOC;
import util.MapCommitFixBugIssue;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Talal on 01.05.17.
 */
public class MetricCalculator {

    private String repoDir;
    private String destinationFolder;
    private String modifiedFilesDir = "ModifiedFiles";
    private String bugIssueJsonFilePAth;
    private String logFilePath;
    String mapFilePath;


    public MetricCalculator(String repoDir, String destinationFolder, String bugIssueJsonFilePAth, String logFilePath, String mapFilePath) {
        this.repoDir = repoDir;
        this.destinationFolder = destinationFolder;
        this.bugIssueJsonFilePAth = bugIssueJsonFilePAth;
        this.logFilePath = logFilePath;
        this.mapFilePath = mapFilePath;

    }

    public void calculateAllMetrics(String fileName) throws IOException, ParseException, InterruptedException {

        FileInputStream fs = new FileInputStream("config.txt");
        BufferedReader br = new BufferedReader(new InputStreamReader(fs));
        String username = br.readLine();
        String password = br.readLine();
        destinationFolder = br.readLine();

        String gitCommand = br.readLine();

        List<BugIssue> listBug = new IssueConverter(bugIssueJsonFilePAth).getIssuesList();
        List<CommitObject> listCommit = new CommitConverter(logFilePath).getCommitList();

        HashMap<BugIssue, CommitObject> map = new MapCommitFixBugIssue(listBug, listCommit, mapFilePath).mapCommitFixToBugIssue();

        DeveloperSocialHistory developerSocialHistory = new DeveloperSocialHistory(mapFilePath, listCommit, listBug);

        List<Metric> metricList = new ArrayList<>();

        boolean stepToReproduce;
        boolean expectedBehaviour;
        int numberOfCommenter = 0;
        int descriptionLength;

        for (BugIssue bugIssue : map.keySet()
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

            int issueDeveloperCommitExperience = developerSocialHistory.numberOfPastCommitsOfDevelopperWhoOpenedTheBugIssue(bugIssue.getOpenedBy(), bugIssue.getOpenedOn());
            int issueDeveloperFixCommitExperience = developerSocialHistory.numberofBugFixingCommitOfDevelopperWhoOpenedTheBugIssue(bugIssue.getOpenedBy(), bugIssue.getOpenedOn());

            CommitObject fixCommit = map.get(bugIssue);

            int fixingDeveloperCommitExperience = developerSocialHistory.numberOfCommitsOftheDeveloperWhoFixedTheBug(fixCommit.getAuthorName(), fixCommit.getDate());
            int fixingDeveloperFixingExperience = developerSocialHistory.numberOfFixCommitsOftheDeveloperWhoFixedTheBug(fixCommit.getAuthorName(), fixCommit.getDate());

            double socialStrength = developerSocialHistory.SocialNetworkStrength(bugIssue);

            if (fixCommit.getModifiedFiles().size() > 0) {

                // Ck metric calculation Fix Commit
                GitCommands.applyCheckout(fixCommit.getId(), destinationFolder, gitCommand, destinationFolder, repoDir);
                copyFiles(fixCommit.getModifiedFiles());


                CKReport ckFix = new CK().calculate(modifiedFilesDir);
                Iterator itFix = ckFix.all().iterator();
                List<Integer> locFix = new ArrayList<>();
                List<Integer> wmcFix = new ArrayList<>();
                while (itFix.hasNext()) {
                    CKNumber ckNumberFix = (CKNumber) itFix.next();
                    locFix.add(ckNumberFix.getLoc());
                    wmcFix.add(ckNumberFix.getWmc());
                }

                double locFixMean = getMean(locFix);
                double locFixMedian = getMedian(locFix);
                double wmcFixMean = getMean(wmcFix);
                double wmcFixMedian = getMedian(wmcFix);

                // Ck metric calculation PreFix Commit
                //checkout to previousCommit
                GitCommands.applyCheckout(fixCommit.getId() + "^", destinationFolder, gitCommand, destinationFolder, repoDir);
                //copy the files which has been modified in the Fix commit
                copyFiles(fixCommit.getModifiedFiles());

                //Ck attributes
                CKReport ckPrefix = new CK().calculate(modifiedFilesDir);
                Iterator itPreFix = ckPrefix.all().iterator();
                List<Integer> locPreFix = new ArrayList<>();
                List<Integer> wmcPreFix = new ArrayList<>();
                List<Integer> CBO = new ArrayList<>();
                List<Integer> DIT = new ArrayList<>();
                List<Integer> NOC = new ArrayList<>();
                List<Integer> NOF = new ArrayList<>();
                List<Integer> NOPF = new ArrayList<>();
                List<Integer> NOSF = new ArrayList<>();
                List<Integer> NOM = new ArrayList<>();
                List<Integer> NOPM = new ArrayList<>();
                List<Integer> NOSM = new ArrayList<>();
                List<Integer> NOSI = new ArrayList<>();
                List<Integer> RFC = new ArrayList<>();
                List<Integer> LCOM = new ArrayList<>();

                while (itPreFix.hasNext()) {
                    CKNumber ckNumberPreFix = (CKNumber) itPreFix.next();
                    locPreFix.add(ckNumberPreFix.getLoc());
                    wmcPreFix.add(ckNumberPreFix.getWmc());
                    CBO.add(ckNumberPreFix.getCbo());
                    DIT.add(ckNumberPreFix.getDit());
                    NOC.add(ckNumberPreFix.getNoc());
                    NOF.add(ckNumberPreFix.getNof());
                    NOPF.add(ckNumberPreFix.getNopf());
                    NOSF.add(ckNumberPreFix.getNosf());
                    NOM.add(ckNumberPreFix.getNom());
                    NOPM.add(ckNumberPreFix.getNopm());
                    NOSM.add(ckNumberPreFix.getNosm());
                    NOSI.add(ckNumberPreFix.getNosi());
                    RFC.add(ckNumberPreFix.getRfc());
                    LCOM.add(ckNumberPreFix.getLcom());
                }

                double locPreFixMean = getMean(locPreFix);
                double locPreFixMedian = getMedian(locPreFix);

                double wmcPreFixMean = getMean(wmcPreFix);
                double wmcPreFixMedian = getMedian(wmcPreFix);

                double CBOMean = getMean(CBO);
                double CBOMedian = getMedian(CBO);

                double DITMean = getMean(DIT);
                double DITMedian = getMedian(DIT);

                double NOCMean = getMean(NOC);
                double NOCMedian = getMedian(NOC);

                double NOFMean = getMean(NOF);
                double NOFMedian = getMedian(NOF);

                double NOPFMean = getMean(NOPF);
                double NOPFMedian = getMedian(NOPF);

                double NOSFMean = getMean(NOSF);
                double NOSFMedian = getMedian(NOSF);

                double NOMMean = getMean(NOM);
                double NOMMedian = getMedian(NOM);

                double NOPMMean = getMean(NOPM);
                double NOPMMedian = getMedian(NOPM);

                double NOSMMean = getMean(NOSM);
                double NOSMMedian = getMedian(NOSM);

                double NOSIMean = getMean(NOSF);
                double NOSIMedian = getMedian(NOSF);

                double RFCMean = getMean(RFC);
                double RFCMedian = getMedian(RFC);

                double LCOMMean = getMean(LCOM);
                double LCOMMedian = getMedian(LCOM);


                List<Double> readabilityList = getReadability(modifiedFilesDir);
                double readabilityMean = getMeanDouble(readabilityList);
                double readabilityMedian = getMedianDouble(readabilityList);

                List<Double> CD = getCommentDensity(modifiedFilesDir);
                double cdMean = getMeanDouble(CD);
                double cdMedian = getMedianDouble(CD);

                double deltaWMCmean = wmcFixMean - wmcPreFixMean;
                double delaWMCmedian = wmcFixMedian - wmcPreFixMedian;


                //create Metric object


                Metric metric = new Metric(((stepToReproduce) ? 1.0 : 0.0)
                        , ((expectedBehaviour) ? 1.0 : 0.0)
                        , (double) descriptionLength
                        , (double) numberOfCommenter
                        , (double) issueDeveloperCommitExperience
                        , (double) issueDeveloperFixCommitExperience
                        , (double) fixingDeveloperCommitExperience
                        , (double) fixingDeveloperFixingExperience
                        , socialStrength
                        , locFixMean
                        , locFixMedian
                        , deltaWMCmean
                        , delaWMCmedian
                        , readabilityMean
                        , readabilityMedian
                        , cdMean
                        , cdMedian
                        , CBOMean
                        , CBOMedian
                        , DITMean
                        , DITMedian
                        , locPreFixMean
                        , locPreFixMedian
                        , LCOMMean
                        , LCOMMedian
                        , NOCMean
                        , NOCMedian
                        , NOFMean
                        , NOFMedian
                        , NOMMean
                        , NOMMedian
                        , NOPMMean
                        , NOPMMedian
                        , NOPFMean
                        , NOPFMedian
                        , NOSMMean
                        , NOSMMedian
                        , NOSFMean
                        , NOSFMedian
                        , NOSIMean
                        , NOSIMedian
                        , RFCMean
                        , RFCMedian
                        , wmcPreFixMean
                        , wmcPreFixMedian
                        , bugIssue.isReopen()

                );

                metricList.add(metric);
            }
        }
        getBalancedList(metricList, fileName);

    }

    private List<Double> getCommentDensity(String modifiedFilesDir) {

        List<Double> cdList = new ArrayList<>();
        Iterator it = FileUtils.iterateFiles(new File(modifiedFilesDir), null, false);
        while (it.hasNext()) {
            File file = ((File) it.next());
            Reporter loc = new LOC().run(new String[]{file.getPath()});
            int lineOfcode = Integer.parseInt(loc.getReport().toString().split(" ")[0]);
            Reporter locComment = new LOC().run(new String[]{file.getPath(), "-n"});

            int lineOfCodeWithNoComment = Integer.parseInt(locComment.getReport().toString().split(" ")[0]);

            int commentsLine = lineOfcode - lineOfCodeWithNoComment;

            cdList.add((double) lineOfcode / (double) commentsLine);

        }

        return cdList;
    }

    private List<Double> getReadability(String modifiedFilesDir) throws IOException {
        List<Double> list = new ArrayList<>();

        Iterator it = FileUtils.iterateFiles(new File(modifiedFilesDir), null, false);
        while (it.hasNext()) {
            File file = ((File) it.next());
            String fileContent = FileUtils.readFileToString(file);
            list.add(raykernel.apps.readability.eval.Main.getReadability(fileContent));

        }
        return list;
    }


    private static double getMedian(List<Integer> loc) {
        if (loc.size() == 0) {
            return 0.0;
        }
        double sum = 0.0;
        for (int value : loc
                ) {
            sum += value;
        }
        return sum / loc.size();
    }

    private double getMean(List<Integer> wmc) {
        Collections.sort(wmc);
        if (wmc.size() == 0) {
            return 0.0;
        }

        if (wmc.size() == 1) {
            return wmc.get(0);
        }
        int middle = wmc.size() / 2;
        if (wmc.size() % 2 == 1) {
            return wmc.get(middle);
        } else {

            return (wmc.get(middle - 1) + wmc.get(middle)) / 2.0;
        }
    }


    private double getMedianDouble(List<Double> loc) {
        if (loc.size() == 0) {
            return 0.0;
        }


        double sum = 0.0;
        for (Number value : loc
                ) {
            sum += value.doubleValue();
        }
        return sum / loc.size();
    }

    private double getMeanDouble(List<Double> wmc) {
        Collections.sort(wmc);
        if (wmc.size() == 0) {
            return 0.0;
        }

        if (wmc.size() == 1) {
            return wmc.get(0);
        }
        int middle = wmc.size() / 2;
        if (wmc.size() % 2 == 1) {
            return wmc.get(middle);
        } else {

            return (wmc.get(middle - 1) + wmc.get(middle)) / 2.0;
        }
    }


    private void copyFiles(List<String> modifiedFiles) {
        try {
            File destDir = new File(modifiedFilesDir);
            FileUtils.cleanDirectory(destDir);
            for (String file : modifiedFiles
                    ) {
                if (file.contains(".java")) {
                    File srcFile = new File(destinationFolder + repoDir + "/" + file);
                    FileUtils.copyFileToDirectory(srcFile, destDir);
                }
            }


        } catch (Exception e) {
        }

    }


    private void writeToFile(String arffFilePath, List<Metric> metricList) throws FileNotFoundException {

        PrintWriter printer = new PrintWriter(arffFilePath);
        printer.println("@relation reopenBugInFuture");
        printer.println("@attribute StepToReproduce numeric");
        printer.println("@attribute ExpectedBehaviour numeric");
        printer.println("@attribute DescriptionLength numeric");
        printer.println("@attribute DeveloperInvolvedInDiscussion numeric");
        printer.println("@attribute OPastCommit numeric");
        printer.println("@attribute OPastFix numeric");
        printer.println("@attribute FPastCommit numeric");
        printer.println("@attribute FPastFix numeric");
        printer.println("@attribute SocialStrength numeric");
        printer.println("@attribute locFixMean numeric");
        printer.println("@attribute locFixMedian numeric");
        printer.println("@attribute DWMCMean numeric");
        printer.println("@attribute DWMCMedian numeric");
        printer.println("@attribute ReadabilityMean numeric");
        printer.println("@attribute ReadabilityMedian numeric");
        printer.println("@attribute CDMean numeric");
        printer.println("@attribute CDMedian numeric");
        printer.println("@attribute CBOMean numeric");
        printer.println("@attribute CBOMedian numeric");
        printer.println("@attribute DITMean numeric");
        printer.println("@attribute DITMedian numeric");
        printer.println("@attribute LOCPreFixMean numeric");
        printer.println("@attribute LOCPrefixMedian numeric");
        printer.println("@attribute LCOMMean numeric");
        printer.println("@attribute LCOMMedian numeric");
        printer.println("@attribute NOCMean numeric");
        printer.println("@attribute NOCMedian numeric");
        printer.println("@attribute NOFMean numeric");
        printer.println("@attribute NOFMedian numeric");
        printer.println("@attribute NOMMean numeric");
        printer.println("@attribute NOMMedian numeric");
        printer.println("@attribute NOPMMean numeric");
        printer.println("@attribute NOPMMedian numeric");
        printer.println("@attribute NOPFMean numeric");
        printer.println("@attribute NOPFMedian numeric");
        printer.println("@attribute NOSMMean numeric");
        printer.println("@attribute NOSMMedian numeric");
        printer.println("@attribute NOSFMean numeric");
        printer.println("@attribute NOSFMedian numeric");
        printer.println("@attribute NOSIMean numeric");
        printer.println("@attribute NOSIMedian numeric");
        printer.println("@attribute RFCMean numeric");
        printer.println("@attribute RFCMedian numeric");
        printer.println("@attribute WMCMean numeric");
        printer.println("@attribute WMCMedian numeric");
        printer.println("@attribute BugReopen {false,true}");
        printer.println();
        printer.println("@data");
        String DELIMITER = ",";

        for (Metric metric : metricList
                ) {


            printer.println(metric.getStepToReproduce() +
                    DELIMITER + metric.getExpectedBehaviour() +
                    DELIMITER + metric.getDescriptionLength() +
                    DELIMITER + metric.getNumberOfCommenter() +
                    DELIMITER + metric.getIssueDeveloperCommitExperience() +
                    DELIMITER + metric.getIssueDeveloperFixCommitExperience() +
                    DELIMITER + metric.getFixingDeveloperCommitExperience() +
                    DELIMITER + metric.getFixingDeveloperFixingExperience() +
                    DELIMITER + metric.getSocialStrength() +
                    DELIMITER + metric.getLocFixMean() +
                    DELIMITER + metric.getLocFixMedian() +
                    DELIMITER + metric.getDeltaWMCmean() +
                    DELIMITER + metric.getDelaWMCmedian() +
                    DELIMITER + metric.getReadabilityMean() +
                    DELIMITER + metric.getReadabilityMedian() +
                    DELIMITER + metric.getCdMean() +
                    DELIMITER + metric.getCdMedian() +
                    DELIMITER + metric.getCBOMean() +
                    DELIMITER + metric.getCBOMedian() +
                    DELIMITER + metric.getDITMean() +
                    DELIMITER + metric.getDITMedian() +
                    DELIMITER + metric.getLocPreFixMean() +
                    DELIMITER + metric.getLocPreFixMedian() +
                    DELIMITER + metric.getLCOMMean() +
                    DELIMITER + metric.getLCOMMedian() +
                    DELIMITER + metric.getNOCMean() +
                    DELIMITER + metric.getNOCMedian() +
                    DELIMITER + metric.getNOFMean() +
                    DELIMITER + metric.getNOFMedian() +
                    DELIMITER + metric.getNOMMean() +
                    DELIMITER + metric.getNOMMedian() +
                    DELIMITER + metric.getNOPMMean() +
                    DELIMITER + metric.getNOPMMedian() +
                    DELIMITER + metric.getNOPFMean() +
                    DELIMITER + metric.getNOPFMedian() +
                    DELIMITER + metric.getNOSMMean() +
                    DELIMITER + metric.getNOSMMedian() +
                    DELIMITER + metric.getNOSFMean() +
                    DELIMITER + metric.getNOSFMedian() +
                    DELIMITER + metric.getNOSIMean() +
                    DELIMITER + metric.getNOSIMedian() +
                    DELIMITER + metric.getRFCMean() +
                    DELIMITER + metric.getRFCMedian() +
                    DELIMITER + metric.getWmcPreFixMean() +
                    DELIMITER + metric.getWmcPreFixMedian() +
                    DELIMITER + String.valueOf(metric.isReopen())

            );
        }

        printer.close();


    }


    private void getBalancedList(List<Metric> metricList, String fileName) throws FileNotFoundException {
        List<Metric> reopenedList = metricList.stream().filter(x -> x.isReopen()).collect(Collectors.toList());
        List<Metric> notReopenList = metricList.stream().filter(x -> !x.isReopen()).collect(Collectors.toList());
        int nonReopenSize = reopenedList.size() * 2;
        Random randomGenerator = new Random();

        for (int i = 0; i < 10; i++) {
            List<Metric> balancedList = metricList.stream().filter(x -> x.isReopen()).collect(Collectors.toList());
            for (int j = 0; j < nonReopenSize; j++) {
                int index = randomGenerator.nextInt(notReopenList.size());
                balancedList.add(notReopenList.get(index));
            }
            writeToFile(fileName + (i + 1) + ".arff", balancedList);
        }


    }


}
