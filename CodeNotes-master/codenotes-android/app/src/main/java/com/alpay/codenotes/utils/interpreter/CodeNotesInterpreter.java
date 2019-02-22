package com.alpay.codenotes.utils.interpreter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class CodeNotesInterpreter {

    int conditionalResult;
    static Map conditionalMap = new HashMap(8);
    static Map outputMap = new HashMap(8);
    static String[] out = {"-1", "NOOUTPUT"};

    public static void init() {
        /* init default values of conditionalMap */
        conditionalMap.put("3IS", -1);
        conditionalMap.put("7IS", 1);
        conditionalMap.put("IAM", -1);
        conditionalMap.put("MYMOM", 1);
        conditionalMap.put("MICROSOFT", -1);
        conditionalMap.put("APPLE", 1);
        conditionalMap.put("FORK", -1);
        conditionalMap.put("SPOON", 1);

        /*init default values of outputMap */
        outputMap.put("HORSE", "horse.ndjson");
    }

    private static String createCodeFromText(String codeText) {
        codeText = codeText.replaceAll("\\n", "-");
        codeText = codeText.replaceAll("\\s+", "");
        codeText = codeText.toUpperCase();
        return codeText;
    }

    private static String drawLineIfNecessary(String codeText) {
        int lastIndex = 0;
        while (lastIndex != -1) {
            lastIndex = codeText.indexOf("IF", lastIndex);
            if (lastIndex != -1) {
                codeText = codeText.substring(0, lastIndex + 2) + "-" + codeText.substring(lastIndex + 2, codeText.length());
                lastIndex += 1;
            }
        }
        lastIndex = 0;
        while (lastIndex != -1) {
            lastIndex = codeText.indexOf("FOR", lastIndex);
            if (lastIndex != -1) {
                codeText = codeText.substring(0, lastIndex + 3) + "-" + codeText.substring(lastIndex + 3, codeText.length());
                lastIndex += 1;
            }
        }
        lastIndex = 0;
        while (lastIndex != -1) {
            lastIndex = codeText.indexOf("WHILE", lastIndex);
            if (lastIndex != -1) {
                codeText = codeText.substring(0, lastIndex + 5) + "-" + codeText.substring(lastIndex + 5, codeText.length());
                lastIndex += 1;
            }
        }
        return codeText;
    }

    public static String[] compile(String codeText) {
        out = new String[]{"1", "NOOUTPUT"};
        codeText = createCodeFromText(codeText);
        codeText = checkStartEnd(codeText);
        codeText = drawLineIfNecessary(codeText);
        List<String> tokens = parse(codeText);
        eval(tokens);
        return out;
    }

    private static List<String> parse(String codeText) {
        String[] tokens = codeText.split("-");
        return Arrays.asList(tokens);
    }

    private static String eval(List<String> tokens) {
        int index = 0;
        out[1] = "NOOUTPUT";
        if (tokens.size() == 1 && tokens.get(0).contentEquals(""))
            return out[1];
        while (tokens.size() > 0) {
            String token = tokens.get(0);
            if (token.contentEquals("IF")) {
                int indexOfElse = lastOccuranceInList(tokens, "ELSE");
                if(indexOfElse != -1){
                    if (mapIntegerKeyIterator(conditionalMap, tokens.get(index + 1)) > 0) {
                        tokens = tokens.subList(index + 2, indexOfElse);
                    } else {
                        tokens = tokens.subList(indexOfElse + 1, tokens.size());
                    }
                }else{
                    if (mapIntegerKeyIterator(conditionalMap, tokens.get(index + 1)) > 0) {
                        tokens = tokens.subList(index + 2, tokens.size());
                    }
                }

            } else if (token.contentEquals("FOR")) {
                String times = tokens.get(index + 1).substring(0, firstAppearenceInString(tokens.get(index + 1), "TIMES"));
                int indexForEnd = lastOccuranceInList(tokens, "IF", "ELSE", "FOR", "WHILE");
                if (indexForEnd <= 0)
                    indexForEnd = tokens.size();
                tokens = tokens.subList(2, indexForEnd);
                if (Integer.valueOf(out[0]) > 1)
                    out[0] = String.valueOf(Integer.valueOf(times) * Integer.valueOf(out[0]));
                else
                    out[0] = times;
            } else if (isOutputStatement(token)) {
                out[1] = mapStringKeyIterator(outputMap, token);
                return out[1];
            }
        }
        return out[1];
    }

    private static String checkStartEnd(String code) {
        try {
            if (code.substring(0, 6).compareTo("START-") == 0) {
                if (code.substring((code.length() - 4), code.length()).compareTo("END-") == 0) {
                    String subCode = code.substring(6, code.length() - 4);
                    if (!(subCode.contains("START") || subCode.contains("END"))) {
                        return subCode;
                    }
                }
            }
        } catch (StringIndexOutOfBoundsException se) {
            return "";
        }
        return "";
    }

    private static String mapStringKeyIterator(Map hm, String code) {
        Iterator it = hm.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            if (code.contains((CharSequence) pair.getKey())) {
                return (String) pair.getValue();
            }
            it.remove(); // avoids a ConcurrentModificationException
        }
        return "NOOUTPUT";
    }

    private static int mapIntegerKeyIterator(Map hm, String code) {
        Iterator it = hm.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            if (code.contains((CharSequence) pair.getKey())) {
                return (int) pair.getValue();
            }
            it.remove(); // avoids a ConcurrentModificationException
        }
        return -1;
    }

    private static int firstAppearenceInString(String text, String word) {
        for (int i = 0; i < text.length() - 1; i++) {
            if (text.indexOf(word) != -1)
                return text.indexOf(word);
        }
        return -1;
    }

    private static int lastOccuranceInList(List<String> tokens, String... words) {
        ArrayList<Integer> indexList = new ArrayList<>();
        int result = -1;
        for (Integer i = 0; i < tokens.size() - 1; i++) {
            for (String word : words) {
                if (tokens.get(i).contentEquals(word)) {
                    indexList.add(i);
                }
            }
        }
        if (indexList.size() > 0)
            result = Collections.min(indexList);
        return result;
    }

    private static boolean isOutputStatement(String codeline) {
        boolean result = false;
        if (!mapStringKeyIterator(outputMap, codeline).contentEquals("NOOUTPUT")) {
            result = true;
        }
        return result;
    }

    private static boolean isLogicalStatement(String codeline) {
        boolean result = false;
        if (mapIntegerKeyIterator(conditionalMap, codeline) > 0) {
            result = true;
        }
        return result;
    }
}


