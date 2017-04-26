/**
 * Created by Talal on 26.04.17.
 */
public class test {
    public static void main(String[] args) {

        String a = "   public boolean expectedBehaviour() {\n" +
                "        boolean behaviour = false;\n" +
                "        for (CommentObject comment : commentList\n" +
                "                ) {\n" +
                "            String text = comment.getText();\n" +
                "            if (text.contains(\"should\") || text.contains(\"must\") || text.contains(\"instead of\")) {\n" +
                "                behaviour = true;\n" +
                "            }\n" +
                "\n" +
                "        }\n" +
                "        return expectedBehaviour();\n" +
                "    }";
        String b = a.replaceAll("[^a-zA-Z0-9\"]", " ");
        System.out.println(b);
    }
}
