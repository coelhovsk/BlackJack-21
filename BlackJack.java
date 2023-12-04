import java.util.*;

public class BlackJack {

    // #region Iniciando SCANNER,RANDOM,JogadoresAtivos, quantidadeJogadores
    public static int jogadoresDesativados = 0; // Definindo variaveis globais para maior facilidade de acesso.
    public static int quantidadeJogadores = 0;
    public static int qtdJogadoresPassaram = 0;
    public static int as = 0;
    final static Scanner SC = new Scanner(System.in);
    final static Random ROLL = new Random();
    // #endregion

    // Método principal
    public static void main(String[] args) throws InterruptedException {
        System.out.println("-------------------------------");
        System.out.println("BLACKJACK --- 21");
        // #region Iniciando JOGADORES e BARALHO

        // Solicita a quantidade de jogadores
        quantidadeJogadores = quantidadeJogadores();

        // HashMap para armazenar as cartas de cada jogador, onde a chave é o jogador
        // "Jogador%d"
        HashMap<String, ArrayList<Integer>> cartasDosJogadores = new HashMap<>();

        // Inicializa o HashMap com os jogadores | a chave é o nome do jogador "nome" e
        // o conteúdo é um ArrayList que contém as cartas do jogador
        String nomeDoJogador = null;
        for (int i = 1; i <= quantidadeJogadores; i++) {
            nomeDoJogador = Input.nomeDoJogador(i); // Nome do jogador i
            ArrayList<Integer> cartas = new ArrayList<>();
            cartasDosJogadores.put(nomeDoJogador, cartas); // Adiciona a chave com o nome do jogador e a lista de suas
                                                           // cartas
        }

        // Define o valor do Ás
        as = Input.valorDoAs();

        // Inicializa o baralho
        int[] baralho = new int[13];
        baralho = Dealer.baralho(baralho, quantidadeJogadores);

        // #endregion

        // Distribui as cartas iniciais para cada jogador
        Dealer.distribuirCartas(cartasDosJogadores, baralho);

        // Enquanto algum jogador estiver ativo, permite a compra de cartas
        while (true) {
            Dealer.comprarCartas(cartasDosJogadores, baralho);
        }
    }
    
    // Método quando dá empate, encerra o programa quando acontece
    public static void empate(ArrayList<String> vencedores){
        System.out.println("-------------------------------");
        for (int i = 0; i < vencedores.size(); i++) {
            if(i == 0){
                System.out.printf("%sOs jogadores %s", Cores.COR_AMARELA, vencedores.get(i));
            }else if(i == vencedores.size() - 1){
                System.out.printf(" e %s empataram\n%s",vencedores.get(i), Cores.RESETAR_COR);
            }else{
                System.out.printf(", %s",vencedores.get(i));
            }
        }
        System.out.println("Fim do jogo.");
        System.exit(0); // Encerra o programa
    }
    // Método para encerrar o programa quando um jogador vence
    public static void finalizarJogo(String jogador, ArrayList<Integer> cartasJogador) {
        System.out.println("-------------------------------");
        if (Verificacoes.verificarBlackjack(cartasJogador)) { // Verifica se o jogador fez BJ a partir do metodo verificarBlackJack
            System.out.println(jogador + ", suas cartas iniciais são: " + cartasJogador);
            System.out.println(Cores.COR_VERDE + jogador + " venceu com um Blackjack!" + Cores.RESETAR_COR);
        } else { // caso não tenha feito printa uma mensagem de venceu normal.
            System.out.println(Cores.COR_VERDE + jogador + " venceu!" + Cores.RESETAR_COR);
        }
        System.out.println("Fim do jogo.");
        System.exit(0); // Encerra o programa
    }


    // #region DEFININDO QUANTIDADE DE JOGADORES
    public static int quantidadeJogadores() throws InterruptedException {
        int quantidade = 0;
        System.out.println("-------------------------------");
        System.out.print("Quantos jogadores vão jogar: ");
        do {
            quantidade = SC.nextInt(); //
            if (quantidade < 2) {
                Thread.sleep(500);
                System.out.println(Cores.COR_VERMELHA + "-------------------------------");
                System.out.println("Mínimo de 2 jogadores");
                System.out.println("-------------------------------" + Cores.RESETAR_COR);
                Thread.sleep(700);
                System.out.print("Quantos jogadores vão jogar: ");
            } else if (quantidade > 8) {
                Thread.sleep(500);
                System.out.println(Cores.COR_VERMELHA + "-------------------------------");
                System.out.println("Máximo de 8 jogadores");
                System.out.println("-------------------------------" + Cores.RESETAR_COR);
                Thread.sleep(700);
                System.out.print("Quantos jogadores vão jogar: ");
            }
        } while (quantidade < 2 || quantidade > 8);
        Thread.sleep(250);
        return quantidade;
    }
    // #endregion
}