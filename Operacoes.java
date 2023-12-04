import java.util.ArrayList;

public class Operacoes {
    public static int as = BlackJack.as;
    // Método para calcular a soma das cartas de um jogador
    public static int calcularSomaCartas(ArrayList<Integer> cartasJogador) {
        int soma = 0;
        for (int carta : cartasJogador) {
            if(carta >= 10){
                soma+=10;
            }else if(carta == 1){
                soma+= as;
            }else{
                soma+= carta;
            }
        }
        return soma;
    }
}
