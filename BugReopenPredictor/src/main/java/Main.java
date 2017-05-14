import commit.GitCommands;
import issues.IssuesService;
import metric.Metric;
import metric.MetricCalculator;
import org.json.simple.parser.ParseException;
import util.NameLoginMapFile;

import java.io.*;
import java.util.List;

/**
 * Created by Talal on 23.04.17.
 */
public class Main {

    //https://github.com/elastic/elasticsearch
    static String username;
    static String password;

    public static void main(String[] args) throws IOException, InterruptedException, ParseException {

        String url = "https://github.com/spring-projects/spring-boot";
        FileInputStream fs = new FileInputStream("config.txt");
        BufferedReader br = new BufferedReader(new InputStreamReader(fs));
        username = br.readLine();
        password = br.readLine();
        String destinationFolder = br.readLine();
        String gitCommand = br.readLine();
        br.close();


        //commit.GitCommands.cloneGitRepositoryUnix(url,destinationFolder,gitCommand,destinationFolder);
        //GitCommands.logFile(destinationFolder,gitCommand,destinationFolder,"spring-boot");

//        int reopenBug= new IssuesService(username,password,92,"spring-projects","spring-boot").getNumberOfReopenBug();
//        System.out.println("# of reopen Bug " + reopenBug);
//        createBugIssuesFile("output/springBugIssues.json");

//
        // new NameLoginMapFile("output/springMapFile.txt","output/springBugIssues.json");

//        MetricCalculator calculator = new metric.MetricCalculator("elasticsearch", destinationFolder, "output/elasticsearchBugIssues.json",
//                "output/logElasticSearch.txt", "output/elasticMapFile.txt");
//        calculator.calculateAllMetrics("output/elastic");

//        MetricCalculator calculator = new metric.MetricCalculator("spring-boot", destinationFolder, "output/springBugIssues.json",
//                "output/logSpring.txt", "output/springMapFile.txt");
//        calculator.calculateAllMetrics("output/spring");

        MetricCalculator calculator = new metric.MetricCalculator("RxJava", destinationFolder, "output/rxJavaBugIssues.json",
                "output/logRx.txt", "output/rxMapFile.txt");
        calculator.calculateAllMetrics("output/rx");

    }


    public static void createBugIssuesFile(String fileName) throws ParseException, InterruptedException, IOException {
        IssuesService issuesService = new IssuesService(username, password, 92, "spring-projects", "spring-boot");
        issuesService.createBugJsonIssuesFile(fileName);
    }


}
