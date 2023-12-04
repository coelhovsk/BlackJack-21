import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

public class Dealer {
    final static Random ROLL = new Random();
    final static Scanner SC = new Scanner(System.in);
    public static int qtdJogadoresPassaram = BlackJack.qtdJogadoresPassaram;
    public static int as = BlackJack.as;
    public static int jogadoresDesativados = BlackJack.jogadoresDesativados;
    public static int quantidadeJogadores = BlackJack.quantidadeJogadores;
    // #region SETANDO BARALHO
    public static int[] baralho(int[] baralho, int quantidadeJogadores) {

        for (int i = 0; i < baralho.length; i++) {
            if (quantidadeJogadores <= 4) {// caso tenha 4 players ou menos os usuários jogarão com um único baralho
                baralho[i] = 4;
            } else { // caso tenha mais que 4players os usuários jogarão com dois baralhos
                baralho[i] = 8;
            }
        }
        return baralho;
    }
    // #endregion

    // #region DISTRIBUINDO CARTAS
    public static void distribuirCartas(HashMap<String, ArrayList<Integer>> jogadoresCartas, int[] baralho)
            throws InterruptedException {
        for (String jogador : jogadoresCartas.keySet()) { // Foreach jogador para todas as chaves do Hashmap ↓↓↓↓↓↓↓
            Thread.sleep(1000); // Delay de 1 segundo
            ArrayList<Integer> maoDoJogador = jogadoresCartas.get(jogador); // pegando valor da mão do player usando a
                                                                            // chave "jogador"

            for (int i = 0; i < 2; i++) { // for com 2 loops, para as duas cartas iniciais de cada jogador
                int carta = 0;
                do {
                    carta = ROLL.nextInt(13) + 1; // pegando carta aleatória do baralho ( simulando embaralhamento )
                } while (baralho[carta - 1] == 0); // enquanto a carta estiver no baralho

                baralho[carta - 1] -= 1; // remove a carta do baralho, pois foi passada a um jogador
                // Caso a carta seja de figura, adiciona 10 nas cartas do jogador

                maoDoJogador.add(carta); // adiciona a carta na mão do jogador
            }

            Verificacoes.verificarSomaEBlackjack(jogador, maoDoJogador, jogadoresCartas); // Verifica se ocorreu um BlackJack
            System.out.println(jogador + ", suas cartas iniciais são: " + maoDoJogador); // Mostra as cartas de cada
                                                                                         // jogador
        }
    }
    //#endregion

    //#region COMPRANDO CARTAS
    public static void comprarCartas(HashMap<String, ArrayList<Integer>> cartasDosJogadores, int[] baralho)
            throws InterruptedException {
        while (true) {
            for (String jogador : cartasDosJogadores.keySet()) {
                ArrayList<Integer> cartasJogador = cartasDosJogadores.get(jogador);

                // verifica se o jogador possui cartas para poder participar da compra
                if (cartasJogador.isEmpty()) { // verifica se o jogador estiver sem cartas(ou seja, eliminado)
                    System.out.println(jogador + " está fora do jogo (sem cartas).");
                    continue; // passa para o próximo jogador
                }

                String compraString = null; // declarando variavel para usar no do{
                boolean compra = false; // declarando variavel para usar no do{
                int gambiarraDoDiabo = 1; // gambiarra pro loop do do{ funcionar de modo simples

                //#region PERGUNTA DESEJA COMPRAR
                do { // criado sem um else pois isso gastaria mais "memória"
                    System.out.println("-------------------------------");
                    System.out.println(jogador + ", suas cartas atuais são: " + cartasJogador); // mostra cartas atuais
                    System.out.println(jogador + ", deseja comprar?");
                    System.out.println("-------------------------------");
                    compraString = SC.next().toUpperCase();
                    if ((compraString.charAt(0) == 'S' || compraString.charAt(0) == 'Y')
                            && compraString.length() <= 3) {
                        compra = true;// caso a palavra se assemelhe com sim ou yes
                        gambiarraDoDiabo = 1;
                    } else if ((compraString.charAt(0) != 'N' && compraString.length() <= 3)
                            || compraString.length() > 3) {
                        System.out.println(Cores.COR_VERMELHA + "Apenas diga sim ou não" + Cores.RESETAR_COR);
                        gambiarraDoDiabo = 0; // caso a palavra não se assemelhe com nada
                    }
                } while (gambiarraDoDiabo == 0);
                //#endregion

                Thread.sleep(500);

                // #region COMPRAR CARTAS
                // faz basicamente a mesma coisa que o distribuir
                if (compra) { // se a compra for "sim"
                    int comprado = 0;
                    do {
                        comprado = ROLL.nextInt(13) + 1;
                    } while (baralho[comprado - 1] == 0);

                    baralho[comprado - 1] -= 1;
                    if (comprado > 10) {
                        comprado = 10;
                    }
                    if (comprado == 1) {
                        comprado = as;
                    }

                    cartasJogador.add(comprado);
                    System.out.println(jogador + " comprou a carta: " + comprado);
                    System.out.println("Suas cartas atuais são: " + cartasJogador);
                    // #endregion

                    //////////////////// comentar dps
                    Verificacoes.verificarSomaEBlackjack(jogador, cartasJogador, cartasDosJogadores);
                    ////////////////////

                    System.out.println("-------------------------------");
                    qtdJogadoresPassaram = 0;
                } else {
                    qtdJogadoresPassaram++;
                    if(qtdJogadoresPassaram == (quantidadeJogadores-jogadoresDesativados)){
                        Verificacoes.todosPassaram(cartasDosJogadores);
                    }
                   
                }
            }
        }

    }
    //#endregion
}
