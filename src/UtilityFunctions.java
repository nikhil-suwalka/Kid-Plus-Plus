import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class UtilityFunctions {
    
    static final ArrayList<String> keywords = new ArrayList<>(Arrays.asList("is", "has", "print", "add", "find", "sort", "remove", "at", "if", "then", "else", "loop", "till", "stop", "end", "from", "to", "as", "gets", "function", "call", "with"));
    static Map<String, Object> map = new HashMap<>();
    
    public static String fixSpace(String data) {
        List<Character> dataArr = data.chars().mapToObj(c -> (char) c).collect(Collectors.toList());
        ArrayList<Character> operator = new ArrayList<>(Arrays.asList('+', '-', '*', '/', '%', '(', ')'));
        for (int i = 0; i < dataArr.size(); i++) {
            if (operator.contains(dataArr.get(i))) {
                if (i == 0 || dataArr.get(i - 1) != ' ') {
                    dataArr.add(i, ' ');
                    i++;
                }
                if (i == dataArr.size() - 1 || dataArr.get(i + 1) != ' ') {
                    dataArr.add(i + 1, ' ');
                    i++;
                }
            }
        }
        if (dataArr.get(0) == ' ')
            dataArr.remove(0);
        if (dataArr.get(dataArr.size() - 1) == ' ')
            dataArr.remove(dataArr.size() - 1);
        return dataArr.stream().map(String::valueOf).collect(Collectors.joining());
    }
    
    public static boolean isNumeric(String strNum) {
        Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");
        
        if (strNum == null) {
            return false;
        }
        return pattern.matcher(strNum).matches();
    }
    
    //  1:String 2:Long 3:Double 4:Boolean -1:Invalid
    public static int findDataType(String value) {
        Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");
        if (value.isEmpty()) {
            return -1;
        } else {
            if (pattern.matcher(value).matches()) {
                //its a number
                if (value.contains(".")) {
                    return 3;
                } else {
                    return 2;
                }
            }
            if ((value.charAt(0) == '"' && value.charAt(value.length() - 1) == '"') || (value.charAt(0) == '\'' && value.charAt(value.length() - 1) == '\'')) {
                return 1;
            }
            if (value.equals("true") || value.equals("false")) {
                return 4;
                
            } else {
                return -1;
            }
        }
    }

    public static Object convertToObject(String str) throws InvalidExpression {
        int datatype = findDataType(str);
        if (datatype == 1)
            return str;
        else if (datatype == 2)
            return Long.parseLong(str);
        else if (datatype == 3)
            return Double.parseDouble(str);
        else if (datatype == 4)
            return Boolean.parseBoolean(str);
        throw new InvalidExpression();
    }
    
    
    public static boolean checkVariableName(String var) {
        Pattern pattern = Pattern.compile("^[a-zA-Z_$][a-zA-Z_$0-9]*$");
        return pattern.matcher(var).matches();
    }
    
    public static boolean createVariable(String name, String value) {
        
        if (findDataType(value) == 1) {
            map.put(name, value);
            return true;
        } else if (findDataType(value) == 2) {
            map.put(name, Long.parseLong(value));
            return true;
        } else if (findDataType(value) == 3) {
            map.put(name, Double.parseDouble(value));
            return true;
        } else if (findDataType(value) == 4) {
            map.put(name, Boolean.parseBoolean(value));
            return true;
        } else return false;
    }

    public static void createArray(String name,ArrayList<Object> value){
        map.put(name,value);
    }
    
    public static boolean isANumberOrOperator(String var) {
        Pattern pattern = Pattern.compile("[()+\\-*/.%]|[0-9]+");
        return pattern.matcher(var).matches();
    }
    
    static ArrayList<String> convertToArray(String str) throws CantFindSymbol, InvalidOperation {
        
        ArrayList<String> list = new ArrayList<>();
        StringBuilder stringBuilder = new StringBuilder();
        
        
        ArrayList<String> words = new ArrayList<>(Arrays.asList(str.split(",")));
        ArrayList<String> split_word;
        
        int i = 0;
        String word, w;
        
        while (i < words.size()) {
            word = words.get(i).trim();
            if ((word.startsWith("\"") && word.endsWith("\"")) || (word.startsWith("'") && word.endsWith("'"))) {
                list.add(word);
            } else {
                
                word = fixSpace(word);
                split_word = new ArrayList<>(Arrays.asList(word.split(" ")));
                stringBuilder.setLength(0);
                if (split_word.size() > 1) {
                    for (int l = 0; l < split_word.size(); l++) {
                        w = split_word.get(l);
                        if (!isANumberOrOperator(w)) {
                            if (map.containsKey(w)) {
                                if (map.get(w) instanceof Long)
                                    stringBuilder.append(map.get(w)).append(" ");
                                else if (map.get(w) instanceof Double)
                                    stringBuilder.append(map.get(w)).append(" ");
                                else {
                                    word = word.replaceAll(w, "\"" + map.get(w) + "\"");
                                    throw new InvalidOperation(word);
                                }
                            } else throw new CantFindSymbol(w);
                        } else
                            stringBuilder.append(w).append(" ");
                    }
                    list.add(stringBuilder.toString().trim());
                    stringBuilder.setLength(0);
                    
                } else if (isNumeric(word)) {
                    list.add(word.trim() + " ");
                } else {
                    if (word.equals("true") || word.equals("false")) {
                        list.add(word);
                    } else if (map.containsKey(word)) {
                        Object ob = map.get(word);
                        if (ob instanceof String)
                            list.add((String) ob);
                        else if (ob instanceof Long)
                            list.add(Long.toString((long) ob));
                        else if (ob instanceof Double)
                            list.add(Double.toString((Double) ob));
                        else if (ob instanceof Boolean)
                            list.add(Boolean.toString((Boolean) ob));
                    } else throw new CantFindSymbol(word);
                    
                }
            }
            
            i++;
        }
        
        if (stringBuilder.length() > 0)
            list.add(stringBuilder.toString().trim());
        return list;
    }
    
    public static String evaluateExpression(String rhs) {
        try {
            //setDataType(data.get(0),ExpEval.evaluate().toString());
            ArrayList<String> out = UtilityFunctions.convertToArray(rhs);
            StringBuilder result = new StringBuilder();
            String temp;
            int dType;
            //  1:String 2:Long 3:Double 4:Boolean -1:Invalid
            for (int i = 0; i < out.size(); i++) {
                dType = findDataType(out.get(i));
                if (dType == 1 || dType == 2 || dType == 3 || dType == 4) {
                    result.append(out.get(i));
                } else {
                    temp = ExpEval.evaluate(out.get(i)).toString();
                    result.append(temp);
                }
            }
            return result.toString();
        } catch (NotANumber | InvalidExpression | CantFindSymbol | InvalidOperation invalidOperation) {
            invalidOperation.printStackTrace();
        }
        return null;
    }
    
    public static void handler(String code) throws InvalidVariableName, InvalidExpression {
        String[] words = code.split(" ");
        if (!keywords.contains(words[0])) {//new variable
            if(UtilityFunctions.checkVariableName(words[0])) {
                if (words[1].equals("is")) {
                    ArrayList<String> data = new ArrayList<>(Arrays.asList(code.split(" is ")));
                    String result = evaluateExpression(data.get(1));
                    boolean opCode = createVariable(data.get(0), result);
                } else if (words[1].equals("has")) {
                    ArrayList<String> data = new ArrayList<>(Arrays.asList(code.split(" has ")));
                    ArrayList<String> elements = new ArrayList<>(Arrays.asList(data.get(1).split(",")));
                    ArrayList<Object> result = new ArrayList<>();

                    for (String element : elements) {
                        result.add(convertToObject(evaluateExpression(element)));
                    }
                  
                    createArray(words[0],result);
                }
            }
            else {
                throw new InvalidVariableName(words[0]);
            }
        } else if (words[0].equals("print")) {
            String[] exp = code.split("print ");
            System.out.println(evaluateExpression(exp[1]).replaceAll("[\"']", ""));
        }
    }
    
}
