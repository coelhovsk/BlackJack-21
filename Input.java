import java.util.Scanner;

public class Input {
    final static Scanner SC = new Scanner(System.in);
    public static int valorDoAs() {
        int n = 0;
        do {
            System.out.println("-------------------------------");
            System.out.println("Qual o valor do Ás (1 ou 11)?");
            n = SC.nextInt();
        } while (n != 1 && n != 11);
        return n;
    }
    public static String nomeDoJogador(int numeroDoJogador) {
        if (numeroDoJogador == 1) {
            SC.nextLine(); // a primeira leitura não está funcionando, se tirar essa linha vai quebrar o
                           // codigo.
        }
        System.out.println("Nome do jogador " + numeroDoJogador + ": ");
        String nome = SC.nextLine();
        return nome;
    }
}
