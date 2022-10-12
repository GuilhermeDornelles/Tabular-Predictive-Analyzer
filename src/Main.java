public class Main {
    public static void main(String[] args) {
        String caminhoArquivo = "";
        if(args.length < 1) {
            System.out.println("Nenhum arquivo foi passado como argumento, utilizando input.txt...");
            caminhoArquivo = "./input.txt";
        }
        else {
            caminhoArquivo = args[0];
        }
        TabularAnalyzer analyzer = new TabularAnalyzer(caminhoArquivo);
        // TabularAnalyzer analyzer = new TabularAnalyzer("../input.txt");
        System.out.println(analyzer.printAllFirst());
    }
}
