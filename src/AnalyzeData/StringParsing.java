package AnalyzeData;

import java.io.*;
import java.util.Scanner;
import javax.swing.JFileChooser;

public class StringParsing{

    private StringBuilder preCode;
    private String parsedCode;
		
    public StringParsing() {
        preCode = new StringBuilder();
        parsedCode = new String();
    }
	public void output() {
        System.out.println(parsedCode);
    }
    public void parsing() throws Exception {
        parsedCode = preCode.substring(0);
        parsedCode = deleteComments(parsedCode);
        parsedCode = deleteKeyWord(parsedCode, "package");
        parsedCode = deleteKeyWord(parsedCode, "import");
        parsedCode = deleteSpaces(parsedCode);
    }
    public boolean isEmpty() {
        if (parsedCode.contains("class"))
            return false;
        if (parsedCode.contains("interface"))
            return false;
        return true;
    }
    public String getParsedCode() {
        int count = 1;
        int i = parsedCode.indexOf("{")+1;
        while (true) {
            if ( parsedCode.charAt(i) == '{' )
                count++;
            else if ( parsedCode.charAt(i) == '}' )
                count--;
            if ( count == 0 )
                break;
            i++;
        }
        String str = parsedCode.substring(0, i+2);
        parsedCode = parsedCode.replace(str, "");
        return str;
    }

    public void readFile(File file) throws Exception {
        Scanner input = new Scanner(file);
        while (input.hasNext()) {
            String str = input.nextLine();
            str = deleteBlockString(str);
            str = deleteLineComments(str);
            preCode.append(str);
        }
        input.close();
    }
    
    private String deleteBlockString(String javaCode) {
        javaCode = javaCode.replaceAll("\'.\'", "");
        javaCode = javaCode.replaceAll("(\\\\\\\")+", "");
        javaCode = javaCode.replaceAll("\"[^\"]*\"", "");
        javaCode = javaCode.replaceAll("@[^\\s]*[\\s\\t\\n]?", "");
        return javaCode;
    }
	
    private String deleteLineComments(String javaCode) {
        return ( javaCode.replaceAll("\\/\\/.*", "") );
    }

    private String deleteComments(String javaCode) {
        return ( javaCode.replaceAll("(?:/\\*(?:[^*]|(?:\\*+[^*/]))*\\*+/)|(?://.*)", "") );
    }

    private String deleteSpaces(String javaCode) {
        javaCode = javaCode.trim(); // delete begin spaces and end spaces
        javaCode = javaCode.replaceAll("\\}", "};"); // insert ";" after "}"
        javaCode = javaCode.replaceAll("\\s+", " ");  // delete all extra spaces
        javaCode = javaCode.replaceAll("(\\{ )", " {"); // insert space front "{"
        javaCode = javaCode.replaceAll("(; )\\b", ";"); // delete space behind ";"
        javaCode = javaCode.replaceAll("(, )\\b", ","); // delete space behind ","
        javaCode = javaCode.replaceAll("\\s+", " ");  // delete all extra spaces
        return javaCode;
    }

    private String deleteKeyWord(String javaCode, String keyWord) {
        while(javaCode.indexOf(keyWord) > -1) {
            int startIndex = javaCode.indexOf(keyWord);
            int endIndex = javaCode.indexOf(";", startIndex);
            String toBeReplaced = javaCode.substring(startIndex, endIndex + 1);
            javaCode = javaCode.replace(toBeReplaced, "");
        }
        return javaCode;
    }
}