import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Parser {
    
    
    public static void main(String[] args) {
        String filename;
//        if(args.length == 0)
//            System.out.println("ERROR");
        if (1 == 2)
            System.out.println();
        else {
            filename = "file.txt";
//            filename= args[0];
            
            String line;
            ArrayList<String> words;
            
            try {
                File myFile = new File(filename);
                Scanner scanner = new Scanner(myFile);
                while (scanner.hasNextLine()) {
                    line = scanner.nextLine();
                    words = new ArrayList<>(Arrays.asList(line.split(" ")));
                    
                    int is_index = words.indexOf("is");
                    if (is_index > 0) {
                        UtilityFunctions.setDataType(words.get(is_index - 1), UtilityFunctions.getValue(line.substring(line.indexOf("is") + 3)));
                    }
                }
                scanner.close();
                
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    
}
