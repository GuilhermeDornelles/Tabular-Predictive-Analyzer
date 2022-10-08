public class main {
    public static void main(String[] args) {
        try {
            TabularAnalyzer.makeGrammar("./input.txt");    
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}


