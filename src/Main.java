import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        String caminhoArquivo = "";
        if(args.length < 1) {
            System.out.println("Nenhum arquivo foi passado como argumento, utilizando input.txt...\n");
            caminhoArquivo = "input.txt";
        }
        else {
            caminhoArquivo = args[0];
        }
        TabularAnalyzer analyzer = new TabularAnalyzer(caminhoArquivo);
        System.out.println("Conjunto FIRST:");
        System.out.println(analyzer.printAllFirst());
        System.out.println("Conjunto FOLLOW:");
        System.out.println(analyzer.printAllFollow());
        System.out.println("Definição da Tabela de Análise Preditiva:");
        System.out.println(analyzer.printTabularAnalyzer());
        
        Scanner in = new Scanner(System.in);
        int stop;
        do {
            System.out.println("Opções:\n[ 0 ] Parar programa \n[ 1 ] Reconhecer sentença");
            stop = Integer.parseInt(in.nextLine());
            if (stop == 0) break;
            System.out.print("Insira uma sentença para ser reconhecida pelo Analisador: ");
            String sentence = in.nextLine().strip();
            System.out.println(analyzer.readSentence(sentence));
        } while (stop == 0);
    }
}
