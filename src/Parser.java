import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Parser {
    public static String findDataType(String value){
        Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");
        if(value.isEmpty()){
            return null;
        }else{
            if(pattern.matcher(value).matches()){
                //its a number
                if(value.contains(".")){
                    return "Double";
                }
                else {
                    return "Long";
                }
            }else if((value.charAt(0)=='"' && value.charAt(value.length()-1)=='"')||(value.charAt(0)=='\'' && value.charAt(value.length()-1)=='\'')){
                return "String";
            }else{
                return null;
            }
        }
    }

    public static boolean checkVariableName(String var){
        Pattern pattern = Pattern.compile("^[a-zA-Z_$][a-zA-Z_$0-9]*$");
        return (pattern.matcher(var).matches());
    }

    public static boolean isANumberOrOperator(String var){
        Pattern pattern = Pattern.compile("[()+\\-*/.%]|[0-9]+");
        return pattern.matcher(var).matches();
    }

    public static String fixSpace(String data){
        List<Character> dataArr = data.chars().mapToObj(c -> (char) c).collect(Collectors.toList());
        ArrayList<Character> operator = new ArrayList<>(Arrays.asList('+','-','*','/','%','(',')'));
        for(int i=0;i<dataArr.size();i++){
            if(operator.contains(dataArr.get(i))){
                if(i==0 || dataArr.get(i-1) !=' '){
                    dataArr.add(i,' ');
                    i++;
                }
                if(i==dataArr.size()-1 || dataArr.get(i + 1) !=' '){
                    dataArr.add(i+1,' ');
                    i++;
                }
            }
        }
        if(dataArr.get(0) ==' ')
            dataArr.remove(0);
        if(dataArr.get(dataArr.size()-1)==' ')
            dataArr.remove(dataArr.size()-1);
        return dataArr.stream().map(String::valueOf).collect(Collectors.joining());
    }

    

    public static void main(String[] args) {
        System.out.println(fixSpace("'John' 'Wick' (1/(2)+9)"));
        System.out.println(fixSpace("(1+2+9)"));
    }
}
