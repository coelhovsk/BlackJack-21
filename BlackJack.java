import java.util.*;

public class BlackJack {
    //#region Iniciando CORES
    public static final String RESETAR_COR = "\u001B[0m";
    public static final String COR_VERMELHA = "\u001B[31m";
    public static final String COR_VERDE = "\u001B[32m";
    //#endregion

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

        // Inicializa o HashMap com os jogadores  | a chave é o nome do jogador "nome" e o conteúdo é um ArrayList que contém as cartas do jogador
        String nomeDoJogador = null;
        for (int i = 1; i <= quantidadeJogadores; i++) {
            nomeDoJogador = nomeDoJogador(i); // Nome do jogador i
            ArrayList<Integer> cartas = new ArrayList<>();
            cartasDosJogadores.put(nomeDoJogador, cartas);  // Adiciona a chave com o nome do jogador e a lista de suas cartas
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
        while (true) {
            comprarCartas(cartasDosJogadores, baralho, as);
        }
    }
    
    public static String nomeDoJogador(int numeroDoJogador){
        if (numeroDoJogador == 1) {
            SC.nextLine(); // a primeira leitura não está funcionando, se tirar essa linha vai quebrar o codigo.
        }
        System.out.println("Nome do jogador "+numeroDoJogador+": ");
        String nome = SC.nextLine();
        return nome;
    }

    // Método para verificar a soma das cartas e condição de Blackjack
    public static void verificarSomaEBlackjack(String jogador, ArrayList<Integer> cartasJogador, int as, HashMap<String, ArrayList<Integer>> cartasDosJogadores) throws InterruptedException {
        int somaCartas = calcularSomaCartas(cartasJogador, as);
        
        if (somaCartas > 21) {
            System.out.println(COR_VERMELHA+ jogador +" perdeu!"+RESETAR_COR);
            cartasJogador.clear();
            jogadoresDesativados++;
            // comentar  dps ///////////////////////////////////// 
            checarDesativados(proximoJogador(cartasDosJogadores, jogador));
        } else if (somaCartas == 21) {
            finalizarJogo(proximoJogador(cartasDosJogadores, jogador), cartasJogador);
        }
    }
    public static String proximoJogador(HashMap<String, ArrayList<Integer>> cartasDosJogadores, String jogador){
        String proxJogador = null;
        int ping = 0;
        for(String jogadores: cartasDosJogadores.keySet()){
            if(jogador.equals(jogadores)){
                ping = 1;
                continue;
            }
            else if (ping == 1){
                proxJogador = jogadores;
                break;
            }
        }
        return proxJogador;
    }
    
    public static void checarDesativados(String jogador) {
            if (jogadoresDesativados == quantidadeJogadores - 1) {
                System.out.println("-------------------------------");
                System.out.println(COR_VERDE+jogador +" é o último jogador restante e ganhou!"+RESETAR_COR);
                System.out.println("Fim do jogo.");
                System.exit(0);
            }
    }

    // Método para distribuir cartas iniciais para cada jogador
    public static void distribuirCartas(HashMap<String, ArrayList<Integer>> jogadoresCartas, int[] baralho, int as) throws InterruptedException {
        for (String jogador : jogadoresCartas.keySet()) { // Foreach jogador para todas as chaves do Hashmap   ↓↓↓↓↓↓↓ 
            Thread.sleep(1000); // Delay de 1 segundo
            ArrayList<Integer> maoDoJogador = jogadoresCartas.get(jogador); // pegando valor da mão do player usando a chave "jogador"

            for (int i = 0; i < 2; i++) { // for com 2 loops, para as duas cartas iniciais de cada jogador
                int carta = 0;
                do {
                    carta = ROLL.nextInt(13) + 1; // pegando carta aleatória do baralho ( simulando embaralhamento )
                } while (baralho[carta - 1] == 0); // enquanto a carta estiver no baralho

                baralho[carta - 1] -= 1; // remove a carta do baralho, pois foi passada a um jogador
                // Caso a carta seja de figura, adiciona 10 nas cartas do jogador
                if (carta > 10) { // definindo as cartas de figura como valor 10
                    carta = 10;
                }
                if (carta == 1) { // definindo Ás como valor do Ás (1 ou 11 (escolhido pelo usuario no começo))
                    carta = as;
                }

                maoDoJogador.add(carta); // adiciona a carta na mão do jogador
            }

            verificarSomaEBlackjack(proximoJogador(jogadoresCartas, jogador), maoDoJogador, as, jogadoresCartas); // Verifica se ocorreu um BlackJack
            System.out.println(jogador + ", suas cartas iniciais são: " + maoDoJogador); // Mostra as cartas de cada jogador
        }
    }

    // método para permitir que os jogadores comprem cartas
    public static void comprarCartas(HashMap<String, ArrayList<Integer>> cartasDosJogadores, int[] baralho, int as) throws InterruptedException {
        while (true) {
            for (String jogador : cartasDosJogadores.keySet()) {
                ArrayList<Integer> cartasJogador = cartasDosJogadores.get(jogador);

                // verifica se o jogador possui cartas para poder participar da compra
                if (cartasJogador.isEmpty()) { // verifica se o jogador estiver sem cartas(ou seja, eliminado)
                    System.out.println(jogador + " está fora do jogo (sem cartas).");
                    continue; // passa para o próximo jogador
                }
                
                String compraString = null; //declarando variavel para usar no do{
                boolean compra = false; //declarando variavel para usar no do{
                int gambiarraDoDiabo = 1; //gambiarra pro loop do do{ funcionar de modo simples
                do { // criado sem um else pois isso gastaria mais "memória"
                System.out.println("-------------------------------");
                System.out.println(jogador + ", suas cartas atuais são: " + cartasJogador); // mostra cartas atuais
                System.out.println(jogador + ", deseja comprar?");
                System.out.println("-------------------------------");
                compraString = SC.next().toUpperCase();
                    if ((compraString.charAt(0) == 'S' || compraString.charAt(0) == 'Y') && compraString.length() <= 3) {
                        compra = true;//caso a palavra se assemelhe com sim ou yes
                    } else if ((compraString.charAt(0) != 'N' && compraString.length() <= 3) || compraString.length() > 3) {
                        System.out.println(COR_VERMELHA + "Apenas diga sim ou não" + RESETAR_COR);
                        gambiarraDoDiabo = 0; //caso a palavra não se assemelhe com nada 
                    }
                } while (gambiarraDoDiabo == 0);
                Thread.sleep(500);

                //#region COMPRAR CARTAS
                // faz basicamente a mesma coisa que o distribuir
                int passou = 0; //qtd de pessoas que passam
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
                //#endregion


                    //////////////////// comentar dps
                    verificarSomaEBlackjack(jogador, cartasJogador, as, cartasDosJogadores);
                    ////////////////////
                    
                    System.out.println("-------------------------------");
                    
                }else{
                    passou++;
                }

                if(passou == cartasDosJogadores.size()){ //verifico se todo mundo passou
                    
                }
            }
        }

        
    }

    // método para calcular a soma das cartas de um jogador
    public static int calcularSomaCartas(ArrayList<Integer> cartasJogador, int as) {
        int soma = 0;
        for (int carta : cartasJogador) {
            soma += (carta == 1) ? as : carta; // operador ternário para somar: variavel =/*soma+=*/ (condição) ? valor se for verdadeiro : valor se for falso;
        }
        return soma;
    }

    // metodo para verificar se um jogador venceu com Blackjack
    public static boolean verificarBlackjack(ArrayList<Integer> cartasJogador) { // verifica se a quantidade de cartas é 2 ( mão de saída )
        return cartasJogador.size() == 2 && calcularSomaCartas(cartasJogador, 11) == 21;
    }

    // Método para encerrar o programa quando um jogador vence
    public static void finalizarJogo(String jogador, ArrayList<Integer> cartasJogador)throws InterruptedException {
        System.out.println("-------------------------------");
        if (verificarBlackjack(cartasJogador)) { // Verifica se o jogador fez BJ a partir do metodo verificarBlackJack
            System.out.println(COR_VERDE+jogador + " venceu com um Blackjack!"+RESETAR_COR);
        } else { // caso não tenha feito printa uma mensagem de venceu normal.
            System.out.println(COR_VERDE+jogador + " venceu!"+RESETAR_COR);
        }
        System.out.println("Fim do jogo.");
        Thread.sleep(500);
        System.exit(0); // Encerra o programa
    }

    // #region SETANDO BARALHO
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

    // #region DEFININDO O VALOR DO ÁS
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

    // #region DEFININDO QUANTIDADE DE JOGADORES
    public static int quantidadeJogadores() throws InterruptedException{
        int quantidade = 0;
        System.out.println("-------------------------------");
        System.out.print("Quantos jogadores vão jogar: ");
        do {
            quantidade = SC.nextInt(); //
            if (quantidade < 2) {
                Thread.sleep(500);
                System.out.println(COR_VERMELHA+"-------------------------------");
                System.out.println("Mínimo de 2 jogadores");
                System.out.println("-------------------------------"+RESETAR_COR);
                Thread.sleep(700);
                System.out.print("Quantos jogadores vão jogar: ");
            } else if (quantidade > 8) {
                Thread.sleep(500);
                System.out.println(COR_VERMELHA+"-------------------------------");
                System.out.println("Máximo de 8 jogadores");
                System.out.println("-------------------------------"+RESETAR_COR);
                Thread.sleep(700);
                System.out.print("Quantos jogadores vão jogar: ");
            }
        } while (quantidade < 2 || quantidade > 8);
        Thread.sleep(250);
        return quantidade;
    }
    // #endregion
}