import java.util.*;

public class BlackJack {

    //#region Iniciando SCANNER,RANDOM,JogadoresAtivos, quantidadeJogadores
    public static int jogadoresDesativados = 0; // Definindo variaveis globais para maior facilidade de acesso.
    public static int quantidadeJogadores = 0;
    final static Scanner SC = new Scanner(System.in);
    final static Random ROLL = new Random();
    //#endregion

    // Método principal
    public static void main(String[] args) throws InterruptedException {
        System.out.println("-------------------------------");
        System.out.println("BLACKJACK --- 21");
        //#region Iniciando JOGADORES e BARALHO

        // Solicita a quantidade de jogadores
        quantidadeJogadores = quantidadeJogadores();

        // HashMap para armazenar as cartas de cada jogador, onde a chave é o jogador "Jogador%d"
        HashMap<String, ArrayList<Integer>> cartasDosJogadores = new HashMap<>();

        // Inicializa o HashMap com os jogadores  | a chave é o jogador "Jogador%d" e o conteúdo é um ArrayList que contém as cartas do jogador
        String nomeDoJogador = null;
        for (int i = 1; i <= quantidadeJogadores; i++) {
            nomeDoJogador = nomeDoJogador(i);
            ArrayList<Integer> cartas = new ArrayList<>();
            cartasDosJogadores.put(nomeDoJogador, cartas);
        }

        // Define o valor do Ás
        int as = valorDoAs();

        // Inicializa o baralho
        int[] baralho = new int[13];
        baralho = baralho(baralho);

        //#endregion

        // Distribui as cartas iniciais para cada jogador
        distribuirCartas(cartasDosJogadores, baralho, as);

        // Enquanto algum jogador estiver ativo, permite a compra de cartas
        while (algumJogadorAtivo(cartasDosJogadores)) {
            comprarCartas(cartasDosJogadores, baralho, as);
        }
    }
    
    public static String nomeDoJogador(int numeroDoJogador){
        if (numeroDoJogador == 1) {
            String nome = SC.nextLine(); // a primeira leitura não está funcionando, se tirar essa linha vai quebrar o codigo.
        }
        System.out.println("Nome do jogador"+numeroDoJogador+": ");
        String nome = SC.nextLine();
        return nome;
    }

    // Método para verificar a soma das cartas e condição de Blackjack
    public static void verificarSomaEBlackjack(String jogador, ArrayList<Integer> cartasJogador, int as) throws InterruptedException {
        int somaCartas = calcularSomaCartas(cartasJogador, as);

        if (somaCartas > 21) {
            System.out.println(jogador + " perdeu!");
            cartasJogador.clear();
            jogadoresDesativados++;
            // comentar  dps ///////////////////////////////////// 
            checarDesativados(jogador);
        } else if (somaCartas == 21) {
            finalizarJogo(jogador, cartasJogador);
        }
    }
    
    public static void checarDesativados(String jogador) {
            if (jogadoresDesativados == quantidadeJogadores - 1) {
                System.out.println("-------------------------------");
                System.out.println(jogador + " é o último jogador restante e ganhou!");
                System.out.println("Fim do jogo.");
                System.exit(0);
            }
    }

    // Método para distribuir cartas iniciais para cada jogador
    public static void distribuirCartas(HashMap<String, ArrayList<Integer>> jogadoresCartas, int[] baralho, int as) throws InterruptedException {
        for (String jogador : jogadoresCartas.keySet()) {
            Thread.sleep(1000);
            ArrayList<Integer> aryListCartasJogador = jogadoresCartas.get(jogador);

            for (int i = 0; i < 2; i++) {
                int carta = 0;
                do {
                    carta = ROLL.nextInt(13) + 1;
                } while (baralho[carta - 1] == 0);

                baralho[carta - 1] -= 1;
                // Caso a carta seja de figura, adiciona 10 nas cartas do jogador
                if (carta > 10) {
                    carta = 10;
                }
                if (carta == 1) {
                    carta = as;
                }

                aryListCartasJogador.add(carta);
            }

            verificarSomaEBlackjack(jogador, aryListCartasJogador, as);
            System.out.println(jogador + ", suas cartas iniciais são: " + aryListCartasJogador);
        }
    }

    // Método para permitir que os jogadores comprem cartas
    public static void comprarCartas(HashMap<String, ArrayList<Integer>> cartasDosJogadores, int[] baralho, int as) throws InterruptedException {
        while (algumJogadorAtivo(cartasDosJogadores)) {
            
            for (String jogador : cartasDosJogadores.keySet()) {
                ArrayList<Integer> cartasJogador = cartasDosJogadores.get(jogador);

                // Verifica se o jogador possui cartas para poder participar da compra
                if (cartasJogador.isEmpty()) {
                    System.out.println(jogador + " está fora do jogo (sem cartas).");
                    continue; // Passa para o próximo jogador
                }
                
                System.out.println("-------------------------------");
                System.out.println(jogador + ", suas cartas atuais são: " + cartasJogador);
                System.out.println(jogador + ", deseja comprar? (true/false)");
                System.out.println("-------------------------------");

                boolean compra = SC.nextBoolean();
                Thread.sleep(500);

                if (compra) {
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


                    //////////////////// comentar dps
                    verificarSomaEBlackjack(jogador, cartasJogador, as);
                    ////////////////////
                    
                    System.out.println("-------------------------------");
                    
                }
            }
        }

        
    }

    // Método para calcular a soma das cartas de um jogador
    public static int calcularSomaCartas(ArrayList<Integer> cartasJogador, int as) {
        int soma = 0;
        for (int carta : cartasJogador) {
            soma += (carta == 1) ? as : carta; // operador ternário, variavel = (condição) ? valor se for verdadeiro : valor se for falso;
        }
        return soma;
    }

    // Método para verificar se um jogador venceu com Blackjack
    public static boolean verificarBlackjack(ArrayList<Integer> cartasJogador) {
        return cartasJogador.size() == 2 && calcularSomaCartas(cartasJogador, 11) == 21;
    }

    // Método para encerrar o programa quando um jogador vence
    public static void finalizarJogo(String jogador, ArrayList<Integer> cartasJogador)throws InterruptedException {
        System.out.println("-------------------------------");
        if (verificarBlackjack(cartasJogador)) {
            System.out.println(jogador + " venceu com um Blackjack!");
        } else {
            System.out.println(jogador + " venceu!");
        }
        System.out.println("Fim do jogo.");
        Thread.sleep(500);
        System.exit(0); // Encerra o programa
    }

    // Método para verificar se algum jogador está ativo
    public static boolean algumJogadorAtivo(HashMap<String, ArrayList<Integer>> cartasDosJogadores) {
        for (ArrayList<Integer> cartas : cartasDosJogadores.values()) {
            if (!cartas.isEmpty()) {
                return true;
            }
        }
        return false;
    }

    // #region DEFININDO BARALHO
    public static int[] baralho(int[] baralho) {

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

    // #region DEFINIR VALOR DO ÁS
    public static int valorDoAs() {
        int n = 0;
        do {
            System.out.println("-------------------------------");
            System.out.println("Qual o valor do Ás (1 ou 11)?");
            n = SC.nextInt();
        } while (n != 1 && n != 11);
        return n;
    }
    // #endregion

    // #region QUANTIDADE DE JOGADORES
    public static int quantidadeJogadores() throws InterruptedException{
        int quantidade = 0;
        System.out.println("-------------------------------");
        System.out.print("Quantos jogadores vão jogar: ");
        do {
            quantidade = SC.nextInt(); //
            if (quantidade < 2) {
                Thread.sleep(500);
                System.out.println("-------------------------------");
                System.out.println("Mínimo de 2 jogadores");
                System.out.println("-------------------------------");
                Thread.sleep(700);
                System.out.print("Quantos jogadores vão jogar: ");
            } else if (quantidade > 8) {
                Thread.sleep(500);
                System.out.println("-------------------------------");
                System.out.println("Máximo de 8 jogadores");
                System.out.println("-------------------------------");
                Thread.sleep(700);
                System.out.print("Quantos jogadores vão jogar: ");
            }
        } while (quantidade < 2 || quantidade > 8);
        Thread.sleep(250);
        return quantidade;
    }
    // #endregion
}