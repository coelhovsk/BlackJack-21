import java.util.Scanner;
import java.util.Random;
import java.util.ArrayList;

public class BlackJack {

    final static Scanner ler = new Scanner(System.in);
    final static Random roll = new Random();

//está certo
    public static void restantes(ArrayList<Integer> restantes, int players){
        for (int i = 0; i < players; i++) {
            restantes.add(null);
        }
    }
    public static void main(String[] args) {
        System.out.println("-------------------------------");
        System.out.println("BLACKJACK --- 21");
        int players = qtdPlay();
        ArrayList<Integer> restantes = new ArrayList<>();
        restantes(restantes, players);
        int as = valorDoAs();

        int[] baralho = new int[13];
        baralho = baralho(baralho, players);

        int[] dandoCartas = new int[players * 2];
        for (int i = 0; i < dandoCartas.length; i++) {
            do {dandoCartas[i] = roll.nextInt(13) + 1;
            } while (baralho[dandoCartas[i] - 1] == 0);
            baralho[dandoCartas[i] - 1] -= 1;
        }

        dandoCartas(dandoCartas, as, restantes);

        int[] somaCartas = new int[players];
        while(restantes.size() > 1){
        somaCartas = somaCartas(somaCartas, dandoCartas, as, restantes);
        somaCartas = compraCartas(somaCartas, baralho, as, restantes);
        }
    }
//trabalha nisso
    public static int[] compraCartas(int[] somaCartas, int[] baralho, int as, ArrayList<Integer> restantes) {
        for (int i = 0; i < somaCartas.length; i++) {

            System.out.println("Jogador " + (i + 1) + " deseja comprar?");
            System.out.println("-------------------------------");
            
            boolean compra = ler.nextBoolean();

            System.out.println("-------------------------------");

            if (compra == true) {
                int comprado = 0;
                do {
                    comprado = roll.nextInt(13) + 1;
                } while (baralho[comprado - 1] == 0);
                System.out.println("Jogador " + (i + 1) + " comprou um " + comprado);
                baralho[comprado - 1] -= 1;
                if (comprado > 10) {
                    comprado = 10;
                }
                if (comprado == 1) {
                    comprado = as;
                }
                somaCartas[i] += comprado;
                if (somaCartas[i] > 21) {
                    System.out.println("Jogador " + (i + 1) + " perdeu!");
                    restantes.remove(i);
                } else if (somaCartas[i] == 21) {
                System.out.println("Jogador " + (i + 1) + " ganhou!");
                restantes.clear();
                restantes.add(null);
                //perguntar ao oda como forçar finalização
                }
                System.out.println("-------------------------------");
            }
        }
        return somaCartas;
    }
//trabalha nisso
    public static int[] somaCartas(int[] somaCartas, int[] dandoCartas, int as, ArrayList<Integer> restantes) {
        for (int i = 0, j = 0; j < somaCartas.length; i+=2, j++) {
            if (dandoCartas[i] > 10) {
                dandoCartas[i] = 10;
            }
            if (dandoCartas[i] == 1) {
                dandoCartas[i] = as;
            }

            if (dandoCartas[i + 1] > 10) {
                dandoCartas[i + 1] = 10;
            }
            if (dandoCartas[i + 1] == 1) {
                dandoCartas[i + 1] = as;
            }
            somaCartas[j] = dandoCartas[i] + dandoCartas[i + 1];
            if (somaCartas[j] == 21) {
                System.out.println("Jogador " + (i + 1) + " ganhou!");
                restantes.clear();
                restantes.add(null);
                //perguntar ao oda como forçar finalização
            }
        }
        return somaCartas;
    }
//está certo
    public static void dandoCartas(int[] dandoCartas, int as, ArrayList<Integer> restantes) {
        for (int i = 0, j = 1, jogadores = 1; i < dandoCartas.length; i += 2, j +=2, jogadores ++) {
            System.out.println("-------------------------------");
            System.out.println("Jogador " + jogadores);
            System.out.println(dandoCartas[i] + " " + dandoCartas[j]);
            
            if (dandoCartas[i] > 10) {
                dandoCartas[i] = 10;
            }
            if (dandoCartas[i] == 1) {
                dandoCartas[i] = as;
            }

            if (dandoCartas[j] > 10) {
                dandoCartas[j] = 10;
            }
            if (dandoCartas[j] == 1) {
                dandoCartas[j] = as;
            }
            if(dandoCartas[i] + dandoCartas[j] == 21){
                System.out.println("-------------------------------");
                System.out.println("Jogador " + jogadores +" conseguiu um BlackJack!!!");
                restantes.clear();
                restantes.add(null);
            }
            
            if (i == dandoCartas.length - 2) {
                System.out.println("-------------------------------");
            }
        }
        
    }
//está certo
    public static int[] baralho(int[] baralho, int players) {
        for (int i = 0; i < baralho.length; i++) {
            if (players <= 4) {
                baralho[i] = 4;
            } else {
                baralho[i] = 8;
            }
        }
        return baralho;
    }
//está certo
    public static int valorDoAs() {
        int n = 0;
        do {
            System.out.println("-------------------------------");
            System.out.println("Qual o valor do Ás (1 ou 11)?");
            n = ler.nextInt();
        } while (n != 1 && n != 11);
        return n;
    }
//está certo
    public static int qtdPlay() {
        int n = 0;
        System.out.println("-------------------------------");
        System.out.println("Quantos jogadores vão jogar?");
        do {
            n = ler.nextInt();
            if (n < 2) {
                System.out.println("-------------------------------");
                System.out.println("Mínimo de 2 jogadores");
                System.out.println("-------------------------------");
            } else if (n > 8) {
                System.out.println("-------------------------------");
                System.out.println("Máximo de 8 jogadores");
                System.out.println("-------------------------------");
            }
        } while (n < 2 || n > 8);
        return n;
    }
}
