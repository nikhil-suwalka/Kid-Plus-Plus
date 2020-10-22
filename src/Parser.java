import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class Parser {
    public static void main(String[] args) {
        
        boolean b = false;
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
                    if (!line.isEmpty() && !line.startsWith("//")) {
                        UtilityFunctions.handler(line);
                    }
                }
                scanner.close();
                
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    
}
