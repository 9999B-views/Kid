package com.alpay;

import java.util.*;

public class CodeNotesInterpreter {

    static Map conditionalMap = new HashMap(8);
    static Map outputMap = new HashMap(8);
    static int[] out = {1, -1};

    static {
        /* init conditionalMap */
        conditionalMap.put("3IS", -1);
        conditionalMap.put("7IS", 1);
        conditionalMap.put("IAM", -1);
        conditionalMap.put("MYMOM", 1);
        conditionalMap.put("MICROSOFT", -1);
        conditionalMap.put("APPLE", 1);
        conditionalMap.put("FORK", -1);
        conditionalMap.put("SPOON", 1);

        /*init outputMap */
        outputMap.put("HORSE", 101); // draw a tree
        outputMap.put("LION", 102); // draw a lion
        outputMap.put("DOLPHIN", 103);
        outputMap.put("ZEBRA", 104);
        outputMap.put("PARROT", 105);
        outputMap.put("RHINO", 106);
        outputMap.put("PENGUIN", 107);
        outputMap.put("FOX", 108);
    }

    public static String createCodeFromText(String codeText) {
        codeText = codeText.replaceAll("\\n", "-");
        codeText = codeText.replaceAll("\\s+", "");
        codeText = codeText.toUpperCase();
        return codeText;
    }

    public static String drawLineIfNecessary(String codeText) {
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

    public static int[] compile(String codeText) {
        codeText = CodeNotesInterpreter.createCodeFromText(codeText);
        codeText = CodeNotesInterpreter.checkStartEnd(codeText);
        codeText = CodeNotesInterpreter.drawLineIfNecessary(codeText);
        List<String> tokens = parse(codeText);
        eval(tokens);
        return out;
    }

    public static List<String> parse(String codeText) {
        String[] tokens = codeText.split("-");
        return Arrays.asList(tokens);
    }

    public static int eval(List<String> tokens) {
        int index = 0;
        while (tokens.size() > 0) {
            String token = tokens.get(0);
            if (token.contentEquals("IF")) {
                int indexOfElse = lastOccuranceInList(tokens, "ELSE");
                if (mapIterator(conditionalMap, tokens.get(index + 1)) > 0) {
                    tokens = tokens.subList(index + 2, indexOfElse);
                } else {
                    tokens = tokens.subList(indexOfElse + 1, tokens.size());
                }
            } else if (token.contentEquals("FOR")) {
                String times = tokens.get(index + 1).substring(0, firstAppearenceInString(tokens.get(index + 1), "TIMES"));
                int indexForEnd = lastOccuranceInList(tokens, "IF", "ELSE", "FOR", "WHILE");
                if (indexForEnd <= 0)
                    indexForEnd = tokens.size();
                tokens = tokens.subList(2, indexForEnd);
                out[0] = Integer.parseInt(times);
            } else if (isOutputStatement(token)) {
                out[1] = mapIterator(outputMap, token);
                return out[1];
            }
        }
        return -1;
    }

    public static String checkStartEnd(String code) {
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

    public static int mapIterator(Map hm, String code) {
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

    public static int firstAppearenceInString(String text, String word) {
        for (int i = 0; i < text.length() - 1; i++) {
            if (text.indexOf(word) != -1)
                return text.indexOf(word);
        }
        return -1;
    }

    public static int lastOccuranceInList(List<String> tokens, String... words) {
        List<Integer> indexList = new ArrayList<>();
        int result = -1;
        for (Integer i = 0; i < tokens.size() - 1; i++) {
            for (String word : words) {
                if (tokens.get(i).contentEquals(word)) {
                    indexList.add(i);
                }
            }
        }
        result = Collections.min(indexList);
        return result;
    }

    public static boolean isOutputStatement(String codeline) {
        boolean result = false;
        if (mapIterator(outputMap, codeline) > 0) {
            result = true;
        }
        return result;
    }

    public static boolean isLogicalStatement(String codeline) {
        boolean result = false;
        if (mapIterator(conditionalMap, codeline) > 0) {
            result = true;
        }
        return result;
    }
}
