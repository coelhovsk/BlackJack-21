import java.util.ArrayList;
import java.util.HashMap;

public class Verificacoes {
    public static int jogadoresDesativados = BlackJack.jogadoresDesativados;
    public static int quantidadeJogadores = BlackJack.quantidadeJogadores;
    // #region Método para verificar a soma das cartas e condição de Blackjack
    public static void verificarSomaEBlackjack(String jogador, ArrayList<Integer> cartasJogador,
            HashMap<String, ArrayList<Integer>> cartasDosJogadores) throws InterruptedException {
        int somaCartas = Operacoes.calcularSomaCartas(cartasJogador);
        if (cartasJogador.size() == 2 && somaCartas == 21) {
            BlackJack.finalizarJogo(jogador, cartasJogador);
        } else {
            if (somaCartas > 21) {
                System.out.println(Cores.COR_VERMELHA + jogador + " perdeu!" + Cores.RESETAR_COR);
                cartasJogador.clear();
                
                jogadoresDesativados++;
                // comentar dps /////////////////////////////////////
                checarDesativados(proximoJogador(cartasDosJogadores, jogador));
            } else if (somaCartas == 21) {
                BlackJack.finalizarJogo(jogador, cartasJogador);
            }
        }

    }
    // #endregion

    // #region MÉTODO PARA VERIFICAR OS JOGADORES DESATIVADOS
    public static void checarDesativados(String jogador) {
        if (jogadoresDesativados == quantidadeJogadores - 1) {
            System.out.println("-------------------------------");
            System.out
                    .println(Cores.COR_VERDE + jogador + " é o último jogador restante e ganhou!" + Cores.RESETAR_COR);
            System.out.println("Fim do jogo.");
            System.exit(0);
        }
    }
    // #endregion

    // #region PROCURA O PROXIMO PLAYER
    public static String proximoJogador(HashMap<String, ArrayList<Integer>> cartasDosJogadores, String jogador) {
        String proxJogador = null;
        int ping = 0;
        for (String jogadores : cartasDosJogadores.keySet()) {
            if (jogador.equals(jogadores)) {
                ping = 1;
                continue;
            } else if (ping == 1) {
                proxJogador = jogadores;
                break;
            }
        }
        return proxJogador;
    }
    // #region

    // #region VERIFICA BLACKJACK
    // Método para verificar se um jogador venceu com Blackjack
    public static boolean verificarBlackjack(ArrayList<Integer> cartasJogador) { // verifica se a quantidade de cartas é
        // 2 ( mão de saída )
        return (cartasJogador.size() == 2 && Operacoes.calcularSomaCartas(cartasJogador) == 21);
    }
    // #endregion

    // #region VERIFICA SE TODOS OS PLAYERS DA RODADA PASSARAM
    public static void todosPassaram(HashMap<String, ArrayList<Integer>> cartasDosJogadores) {
        HashMap<String, Integer> somaDosPlayer = new HashMap<>();
        for (String chave : cartasDosJogadores.keySet()) {
            ArrayList<Integer> maoDoPlayer = cartasDosJogadores.get(chave);
            somaDosPlayer.put(chave, Operacoes.calcularSomaCartas(maoDoPlayer)); // Adicionar a lista clonada ao HashMap
                                                                                 // clonado
        }
        ArrayList<String> vencedor = new ArrayList<String>(); // crio uma listo contendo qm vai ganhar
        int maior = 0;
        vencedor.add(null); // adiciono uma casa nula na lista pois vai ser removida
        for (String chave : somaDosPlayer.keySet()) {
            if (somaDosPlayer.get(chave) > maior) { // verifico se o atual é maior que o maior visto
                maior = somaDosPlayer.get(chave); // se for, o maior muda
                vencedor.add(chave); // o nome do maior valor vai pra lista
                vencedor.remove(0); // retiro o nome anterior da lista
            } else if (somaDosPlayer.get(chave) == maior) { // se o valor for igual ao do maior (empate)
                vencedor.add(chave); // adiciono ele na lista também
            }
        }

        // para o caso de ter jogadores que não possuem o maior valor na lista
        for (int i = 0; i < vencedor.size(); i++) {
            if (somaDosPlayer.get(vencedor.get(i)) != maior) { // vejo se ele não se encaixa
                vencedor.remove(i); // e então tiro ele
            }
        }

        if (vencedor.size() == 1) {
            BlackJack.finalizarJogo(vencedor.get(0), cartasDosJogadores.get(vencedor.get(0)));
        } else {
            BlackJack.empate(vencedor);
        }
    }
    // #endregion
}
