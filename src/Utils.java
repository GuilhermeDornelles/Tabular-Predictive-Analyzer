public class Utils {
    public static boolean isTerminal(String value){
        if(value.toLowerCase() == value){
            return true;
        }
        return false;
    }
    public static boolean isCharValid(String strChar){
        return strChar.compareTo("E") != 0;
    }
}
