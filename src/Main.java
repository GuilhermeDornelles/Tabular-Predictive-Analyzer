public class Main {
    public static void main(String[] args) {
        // TabularAnalyzer analyzer = new TabularAnalyzer(args[0]);
        TabularAnalyzer analyzer = new TabularAnalyzer("./input.txt");
        System.out.println(analyzer.printAllFirst());
    }
}
