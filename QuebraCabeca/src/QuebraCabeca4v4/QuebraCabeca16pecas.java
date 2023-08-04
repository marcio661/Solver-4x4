package QuebraCabeca4v4;
import java.util.*;



import java.util.*;

public class QuebraCabeca16pecas {
    public static void main(String[] args) {
        int size = getInputBoardSize();
        int[] initialBoard = getInputBoard("Digite a configuração inicial (separe os números por espaço):", size);
        int[] targetBoard = getInputBoard("Digite a configuração desejada (separe os números por espaço):", size);

        List<String> passos = resolverQuebraCabeca(initialBoard, targetBoard, size);

        if (passos == null) {
            System.out.println("Não foi possível encontrar uma solução para o quebra-cabeça.");
        } else {
            System.out.println("Passo a passo para resolver o quebra-cabeça:");
            for (String passo : passos) {
                imprimirTabuleiro(parseTabuleiro(passo), size);
                System.out.println();
            }
        }
    }

    private static int getInputBoardSize() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Digite o tamanho do tabuleiro (3 ou 4):");
        int size;
        do {
            size = scanner.nextInt();
        } while (size != 3 && size != 4);
        return size;
    }

    private static int[] getInputBoard(String mensagem, int size) {
        Scanner scanner = new Scanner(System.in);
        System.out.println(mensagem);
        int[] tabuleiro = new int[size * size];
        for (int i = 0; i < size * size; i++) {
            tabuleiro[i] = scanner.nextInt();
        }
        return tabuleiro;
    }

    private static void imprimirTabuleiro(int[] tabuleiro, int size) {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                System.out.print(tabuleiro[i * size + j] + " ");
            }
            System.out.println();
        }
    }

    private static List<String> resolverQuebraCabeca(int[] tabuleiroInicial, int[] tabuleiroAlvo, int size) {
        Queue<String> fila = new PriorityQueue<>(Comparator.comparingInt(s -> calcularHeuristica(parseTabuleiro(s), tabuleiroAlvo, size)));
        Set<String> visitados = new HashSet<>();
        Map<String, String> pai = new HashMap<>();

        String tabuleiroInicialStr = tabuleiroParaString(tabuleiroInicial);
        fila.add(tabuleiroInicialStr);

        while (!fila.isEmpty()) {
            String tabuleiroAtualStr = fila.poll();
            int[] tabuleiroAtual = parseTabuleiro(tabuleiroAtualStr);

            if (tabuleiroAtualStr.equals(tabuleiroParaString(tabuleiroAlvo))) {
                return reconstruirPassos(pai, tabuleiroAtualStr);
            }

            visitados.add(tabuleiroAtualStr);

            int[] posicaoVazia = encontrarPosicaoVazia(tabuleiroAtual, size);

            for (int[] dir : DIRECOES) {
                int novaLinha = posicaoVazia[0] + dir[0];
                int novaColuna = posicaoVazia[1] + dir[1];

                if (isValidPosition(novaLinha, novaColuna, size)) {
                    trocar(tabuleiroAtual, posicaoVazia[0], posicaoVazia[1], novaLinha, novaColuna, size);
                    String novoTabuleiroStr = tabuleiroParaString(tabuleiroAtual);

                    if (!visitados.contains(novoTabuleiroStr)) {
                        fila.add(novoTabuleiroStr);
                        pai.put(novoTabuleiroStr, tabuleiroAtualStr);
                    }

                    
                    trocar(tabuleiroAtual, novaLinha, novaColuna, posicaoVazia[0], posicaoVazia[1], size);
                }
            }
        }

        return null;
    }

    private static boolean isValidPosition(int linha, int coluna, int size) {
        return linha >= 0 && linha < size && coluna >= 0 && coluna < size;
    }

    public static void trocar(int[] tabuleiro, int linha1, int coluna1, int linha2, int coluna2, int size) {
        int temp = tabuleiro[linha1 * size + coluna1];
        tabuleiro[linha1 * size + coluna1] = tabuleiro[linha2 * size + coluna2];
        tabuleiro[linha2 * size + coluna2] = temp;
    }

    private static int[] encontrarPosicaoVazia(int[] tabuleiro, int size) {
        int[] posicao = new int[2];
        for (int i = 0; i < size * size; i++) {
            if (tabuleiro[i] == 0) {
                posicao[0] = i / size;
                posicao[1] = i % size;
                return posicao;
            }
        }
        return posicao;
    }

    private static String tabuleiroParaString(int[] tabuleiro) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < tabuleiro.length; i++) {
            sb.append(tabuleiro[i]);
            sb.append(" ");
        }
        return sb.toString().trim();
    }

    private static int[] parseTabuleiro(String tabuleiroStr) {
        String[] parts = tabuleiroStr.split(" ");
        int[] tabuleiro = new int[parts.length];
        for (int i = 0; i < parts.length; i++) {
            tabuleiro[i] = Integer.parseInt(parts[i]);
        }
        return tabuleiro;
    }

    private static List<String> reconstruirPassos(Map<String, String> pai, String tabuleiroAtualStr) {
        List<String> passos = new ArrayList<>();

        while (tabuleiroAtualStr != null) {
            passos.add(0, tabuleiroAtualStr);
            tabuleiroAtualStr = pai.get(tabuleiroAtualStr);
        }

        return passos;
    }

    private static int calcularHeuristica(int[] tabuleiro, int[] tabuleiroAlvo, int size) {
        int h = 0;
        for (int i = 0; i < tabuleiro.length; i++) {
            int value = tabuleiro[i];
            if (value != 0) {
                int targetRow = (value - 1) / size;
                int targetCol = (value - 1) % size;
                int currentRow = i / size;
                int currentCol = i % size;
                h += Math.abs(targetRow - currentRow) + Math.abs(targetCol - currentCol);
            }
        }
        return h;
    }

    private static final int[][] DIRECOES = { {-1, 0}, {1, 0}, {0, -1}, {0, 1} };
}

