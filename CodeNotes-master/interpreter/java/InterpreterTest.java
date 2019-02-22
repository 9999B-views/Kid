import java.util.Arrays;
import java.util.HashMap;

public class InterpreterTest {

    public static String input_text = "START\n" +
            "IF My mom is older than me\n" +
            "Draw a zebra\n" +
            "ELSE\n" +
            "Draw a horse\n" +
            "END\n";

    public static String input_text1 = "START\n" +
            "FOR 10 times\n" +
            "Draw a dolphin\n" +
            "END\n";

    public static String input_text2 = "START\n" +
            "IF 3 is larger than 7\n" +
            "FOR 10 times\n" +
            "Draw a zebra\n" +
            "END\n";
            "ELSE\n" +
            "Draw a horse\n" +
            "END\n";

    public static void main(String[] args) {
        int[] y = CodeNotesInterpreter.compile(input_text);
        System.out.println(y[0]+" "+y[1]);
        y = CodeNotesInterpreter.compile(input_text1);
        System.out.println(y[0]+" "+y[1]);
        y = CodeNotesInterpreter.compile(input_text2);
        System.out.println(y[0]+" "+y[1]);
    }
}
