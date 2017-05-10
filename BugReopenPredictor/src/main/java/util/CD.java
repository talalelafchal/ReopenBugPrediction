package util;

/**
 * Created by Talal on 03.05.17.
 */
import com.github.mauricioaniche.ck.CKNumber;
import com.github.mauricioaniche.ck.CKReport;
import com.github.mauricioaniche.ck.metric.Metric;
import org.eclipse.jdt.core.dom.*;


import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by giggiux on 3/13/17.
 */
public class CD extends ASTVisitor implements Metric {

    private int linesOfComment = 0;

    private String fileText;

    public boolean visit(Javadoc javadoc) {
        return calculateLinesOfCommentFromNode(javadoc);
    }

    //TODO: Understand why don't find LineComment nodes
    public boolean visit(LineComment lineComment) {
        linesOfComment += 1;
        return true;
    }

    public boolean visit(BlockComment blockComment) {
        return calculateLinesOfCommentFromNode(blockComment);
    }

    private boolean calculateLinesOfCommentFromNode( Comment commentNode) {
        String comment = getCommentText(commentNode, fileText);
        linesOfComment += comment.split("[\n|\r]").length;
        return true;
    }

    @Override
    public void execute(CompilationUnit compilationUnit, CKNumber ckNumber, CKReport ckReport) {
        try {
            fileText = readWholeFile(ckNumber.getFile());
        } catch (IOException e) {
            return;
        }

        compilationUnit.accept(this);
    }

    @Override
    public void setResult(CKNumber ckNumber) {
        ckNumber.addSpecific("linesOfComment", linesOfComment);
        int loc = ckNumber.getLoc();

        double CD = loc/linesOfComment;
//		int cd = Utils.doubleToInt(CD);
        ckNumber.addSpecific("CD", (int) CD);
    }

    public static String readWholeFile(String path) throws IOException{
        return new String(Files.readAllBytes(Paths.get(path)), StandardCharsets.UTF_8);
    }


    public static String getCommentText (Comment commentNode, String fileText) {
        int start = commentNode.getStartPosition();
        int end = start + commentNode.getLength();
        return fileText.substring(start, end);
    }

}