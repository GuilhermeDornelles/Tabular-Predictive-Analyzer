import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Utils {

    public static final String EMPTY_SYMBOL = "E";

    public static boolean isTerminal(String value){
        if(value.toLowerCase() == value){
            return true;
        }
        return false;
    }
    public static boolean isCharValid(String strChar){
        return strChar.compareTo(Utils.EMPTY_SYMBOL) != 0;
    }

    public static <T> ArrayList<T> transformArrayToArrayList(T[] array){
        return new ArrayList<>(Arrays.asList(array));
    }

    public static ArrayList<String> returnLinesFromFile(String filePath){
        File file = new File(filePath);
        ArrayList<String> lines = new ArrayList<>();
        try {
            Scanner sc = new Scanner(file);
            while (sc.hasNextLine()) {
                lines.add(sc.nextLine());
            }
            sc.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return lines;
    }

    public static String returnFirstChar(String string) {
        if (string.length() == 0)
            return "";
        String firstChar = Character.toString(string.charAt(0));
        // logic to concatenate in firstChar all ' in case we have A', A'', A''', etc
        if (string.length() >= 1) {
            for (int i = 1; i < string.length(); i++) {
                if (Character.toString(string.charAt(i)).compareTo("'") == 0) {
                    firstChar += string.charAt(i);
                } else
                    break;
            }
        }
        return firstChar;
    }

    public static String removeLastChar(String string){
        if(string.length() <= 1){
            return "";
        }
        String newString = "";
        String lastChar = String.valueOf(string.charAt(string.length()-1));
        if(!lastChar.equals("'")){
            newString = string.substring(0, string.length() -1);
        } else {
            newString = string.substring(0, string.length() -2);
        }
        return newString;
    }

    public static String returnLastChar(String string) {
        if (string.length() == 0)
            return "";
        // String lastChar = Character.toString(string.charAt(string.length()-1));
        // to pensando
        // o lastChar começa com o ultimo ja
        // se ele for ' 
        // logic to concatenate in firstChar all ' in case we have A', A'', A''', etc
        String lastChar = "";
        if (string.length() >= 1) {
            int sizeOfLastChar = 0;
            for (int i = string.length()-1; i > 0; i--) { // :D
                if (Character.toString(string.charAt(i)).compareTo("'") == 0) {
                    sizeOfLastChar++;
                } else
                    break;
            }
            lastChar = string.substring((string.length()-1)-sizeOfLastChar);
        }
        return lastChar;
    }
}

