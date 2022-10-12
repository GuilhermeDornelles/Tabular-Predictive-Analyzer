public class Main {
    public static void main(String[] args) {
        String caminhoArquivo = "";
        if(args.length < 1) {
            System.out.println("Nenhum arquivo foi passado como argumento, utilizando input.txt...\n");
            caminhoArquivo = "./input.txt";
        }
        else {
            caminhoArquivo = args[0];
        }
        TabularAnalyzer analyzer = new TabularAnalyzer(caminhoArquivo);
        System.out.println("Conjunto FIRST:");
        System.out.println(analyzer.printAllFirst());
        System.out.println("Conjunto FOLLOW:");
        System.out.println(analyzer.printAllFollow());
        System.out.println("Definicao da Tabela de Análise Preditiva:");
        System.out.println(analyzer.printTabularAnalyzer());

    }
}
