import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.FileNotFoundException;
import java.io.IOException;

public class BatallaNaval {
    static final int tamanoTablero = 8;
    static final int agua = 0;
    static final int barco = 1;
    static final int aguaDisparada = 2;
    static final int barcoImpactado = 3;
    static int[][] tableroCPU = new int[tamanoTablero][tamanoTablero];
    static int[][] tableroJugador = new int[tamanoTablero][tamanoTablero];
    static int barcosHundidosCPU = 0;
    static int barcosHundidosJugador = 0;
    static int disparosRealizados = 0;
    static int totalBarcos = 0;
    static String nombreJugador = "";
    static Scanner scanner = new Scanner(System.in);

    // FUNCIONES (MÉTODOS)

    static void mostrarMenu() {
        System.out.println("\n========================================");
        System.out.println("      BATALLA NAVAL - MENÚ PRINCIPAL");
        System.out.println("========================================");
        System.out.println("1. Iniciar nueva partida");
        System.out.println("2. Salir del juego");
        System.out.println("========================================");
        System.out.print("Seleccione una opción: ");
    }

    // validación
    static int leerOpcionMenu(Scanner scanner) {
        int opcion = 0;
        boolean entradaValida = false;

        while (!entradaValida) {
            try {
                String entrada = scanner.nextLine();
                opcion = Integer.parseInt(entrada);
                if (opcion == 1 || opcion == 2) {
                    entradaValida = true;
                } else {
                    System.out.print("Opción inválida. Ingrese 1 o 2: ");
                }
            } catch (Exception e) {
                System.out.print("Entrada inválida. Por favor ingrese un número (1 o 2): ");
            }
        }

        return opcion;
    }

    static void iniciarPartida(Scanner scanner) {
        barcosHundidosCPU = 0;
        barcosHundidosJugador = 0;
        disparosRealizados = 0;

        System.out.print("\nIngrese su nombre: ");
        nombreJugador = scanner.nextLine();

        // Obtener la cantidad de barcos desde el archivo de configuración.
        totalBarcos = leerConfiguracionBarcos();

        // Inicializar ambos tableros con el valor agua (0).
        inicializarTablero(tableroCPU);
        inicializarTablero(tableroJugador);

        // Colocar la cantidad de barcos definida aleatoriamente en ambos tableros.
        colocarBarcosAleatorios(tableroCPU, totalBarcos);
        colocarBarcosAleatorios(tableroJugador, totalBarcos);

        // Mensajes de inicio del juego.
        System.out.println("\n¡Bienvenido " + nombreJugador + "!");
        System.out.println("Se han desplegado " + totalBarcos + " barcos para cada jugador.");
        System.out.println("¡Comienza la batalla!");

        // Iniciar el bucle principal de la partida.
        jugarPartida(scanner);
    }

    static int leerConfiguracionBarcos() {
        int numBarcos = 5;

        try {
            BufferedReader reader = new BufferedReader(new FileReader("barcos_config.txt"));

            String lineaConfig = reader.readLine();
            if (lineaConfig != null) {
                numBarcos = Integer.parseInt(lineaConfig.trim());

                if (numBarcos < 5) {
                    System.out.println("Advertencia: El archivo indica menos de 5 barcos. Se usarán 5 barcos.");
                    numBarcos = 5;
                }
            }

            reader.close();
            System.out.println("Configuración cargada desde archivo: " + numBarcos + " barcos");

        } catch (FileNotFoundException e) {
            System.out.println("ERROR: Archivo barcos_config.txt no encontrado.");
            System.out.println("Por favor, crea el archivo con la cantidad de barcos en la primera línea (ejemplo: 5)");
            System.out.println("Usando configuración por defecto: " + numBarcos + " barcos");
        } catch (IOException e) {
            System.out.println("Error al leer el archivo. Usando configuración por defecto: " + numBarcos + " barcos");
        } catch (NumberFormatException e) {
            System.out.println("Error: La primera línea debe contener un número. Usando configuración por defecto: "
                    + numBarcos + " barcos");
        } catch (Exception e) {
            System.out.println(
                    "Error en formato del archivo. Usando configuración por defecto: " + numBarcos + " barcos");
        }

        return numBarcos;
    }

    // Función para inicializar el tablero con agua
    static void inicializarTablero(int[][] tablero) {
        for (int i = 0; i < tamanoTablero; i++) {
            for (int j = 0; j < tamanoTablero; j++) {
                tablero[i][j] = agua;
            }
        }
    }

    static void colocarBarcosAleatorios(int[][] tablero, int cantidadBarcos) {
        int barcosColocados = 0;

        while (barcosColocados < cantidadBarcos) {
            int fila = (int) (Math.random() * tamanoTablero);
            int columna = (int) (Math.random() * tamanoTablero);
            // Verificar que la posición esté vacía
            if (tablero[fila][columna] == agua) {
                tablero[fila][columna] = barco;
                barcosColocados++;
            }
        }
    }

    static void jugarPartida(Scanner scanner) {
        boolean juegoTerminado = false;

        while (!juegoTerminado) {
            System.out.println("\n========================================");
            System.out.println("          TURNO DEL JUGADOR");
            System.out.println("========================================");
            mostrarEstadisticas();
            mostrarTableroJugador(tableroCPU);

            boolean disparoValido = false;
            while (!disparoValido) {
                System.out.print("\nIngrese coordenada para disparar (ejemplo: A5, C7): ");
                String coordenada = scanner.nextLine().trim().toUpperCase();

                if (validarCoordenada(coordenada)) {
                    int[] indices = convertirCoordenada(coordenada);
                    int fila = indices[0];
                    int columna = indices[1];

                    if (tableroCPU[fila][columna] == aguaDisparada ||
                            tableroCPU[fila][columna] == barcoImpactado) {
                        System.out.println("¡Ya disparaste a esta posición! Intenta otra coordenada.");
                    } else {
                        disparoValido = true;
                        procesarDisparo(tableroCPU, fila, columna, true);
                        disparosRealizados++;
                        // Verificar si el Jugador ganó
                        if (barcosHundidosCPU == totalBarcos) {
                            System.out.println("\n¡FELICIDADES " + nombreJugador + "! ¡HAS GANADO LA BATALLA!");
                            juegoTerminado = true;
                            guardarResultados(true);
                        }
                    }
                } else {
                    System.out.println("Coordenada inválida. Use formato letra+número (A-H, 1-8). Ejemplo: A5");
                }
            }
            // Turno de la CPU si el juego no terminó
            if (!juegoTerminado) {
                System.out.println("\n========================================");
                System.out.println("          TURNO DE LA CPU");
                System.out.println("========================================");
                turnoCPU();
                // Verificar si la CPU ganó
                if (barcosHundidosJugador == totalBarcos) {
                    System.out.println("\n¡LA CPU HA GANADO LA BATALLA!");
                    System.out.println("¡No te rindas, inténtalo de nuevo!");
                    juegoTerminado = true;
                    guardarResultados(false);
                }
            }
        }
    }

    static void mostrarEstadisticas() {
        System.out.println("Disparos realizados: " + disparosRealizados);
        System.out.println("Barcos hundidos (CPU): " + barcosHundidosCPU + "/" + totalBarcos);
        System.out.println("Tus barcos restantes: " + (totalBarcos - barcosHundidosJugador) + "/" + totalBarcos);
    }

    static void mostrarTableroJugador(int[][] tablero) {
        System.out.println("\n  A B C D E F G H");

        for (int i = 0; i < tamanoTablero; i++) {
            System.out.print((i + 1) + " ");
            for (int j = 0; j < tamanoTablero; j++) {
                if (tablero[i][j] == agua || tablero[i][j] == barco) {
                    System.out.print("~ ");
                } else if (tablero[i][j] == aguaDisparada) {
                    System.out.print("O ");
                } else if (tablero[i][j] == barcoImpactado) {
                    System.out.print("X ");
                }
            }
            System.out.println();
        }
    }

    // Función para validar formato de coordenada
    static boolean validarCoordenada(String coordenada) {
        if (coordenada.length() < 2 || coordenada.length() > 2) {
            return false;
        }

        char letra = coordenada.charAt(0);
        char numero = coordenada.charAt(1);

        if (letra < 'A' || letra > 'H') {
            return false;
        }

        if (numero < '1' || numero > '8') {
            return false;
        }

        return true;
    }

    // Función para convertir coordenada de texto a índices de matriz
    static int[] convertirCoordenada(String coordenada) {
        int[] indices = new int[2];
        char letra = coordenada.charAt(0);
        char numero = coordenada.charAt(1);

        indices[0] = numero - '1';
        indices[1] = letra - 'A';

        return indices;
    }

    static void procesarDisparo(int[][] tablero, int fila, int columna, boolean esJugador) {
        if (tablero[fila][columna] == barco) {
            System.out.println("¡IMPACTO! ¡Has hundido un barco enemigo!");
            tablero[fila][columna] = barcoImpactado;

            if (esJugador) {
                barcosHundidosCPU++;
            } else {
                barcosHundidosJugador++;
            }
        } else {
            System.out.println("¡AGUA! No había ningún barco en esa posición.");
            tablero[fila][columna] = aguaDisparada;
        }
    }

    static void turnoCPU() {
        boolean disparoValido = false;

        while (!disparoValido) {
            int fila = (int) (Math.random() * tamanoTablero);
            int columna = (int) (Math.random() * tamanoTablero);

            if (tableroJugador[fila][columna] != aguaDisparada &&
                    tableroJugador[fila][columna] != barcoImpactado) {
                disparoValido = true;

                char letraColumna = (char) ('A' + columna);
                int numeroFila = fila + 1;
                System.out.println("La CPU dispara a: " + letraColumna + numeroFila);

                procesarDisparo(tableroJugador, fila, columna, false);
            }
        }
    }

    static void guardarResultados(boolean ganoJugador) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("battleship_scores.txt", true));

            writer.write("========================================");
            writer.newLine();
            writer.write("RESULTADO DE PARTIDA");
            writer.newLine();
            writer.write("========================================");
            writer.newLine();
            writer.write("Jugador: " + nombreJugador);
            writer.newLine();
            writer.write("Barcos hundidos (enemigos): " + barcosHundidosCPU + "/" + totalBarcos);
            writer.newLine();
            writer.write("Disparos realizados: " + disparosRealizados);
            writer.newLine();
            writer.write("Resultado: " + (ganoJugador ? "VICTORIA" : "DERROTA"));
            writer.newLine();
            writer.write("========================================");
            writer.newLine();
            writer.newLine();

            writer.close();
            System.out.println("\n¡Resultados guardados en battleship_scores.txt!");

        } catch (IOException e) {
            System.out.println("Error al guardar los resultados.");
        }
    }

    // MÉTODO PRINCIPAL (main)

    public static void main(String[] args) {

        int opcion;

        do {
            mostrarMenu();
            opcion = leerOpcionMenu(scanner);

            switch (opcion) {
                case 1:
                    iniciarPartida(scanner);
                    break;
                case 2:
                    System.out.println("\n¡Gracias por jugar Batalla Naval! ¡Hasta pronto!");
                    break;
                default:
                    System.out.println("Opción inválida. Por favor, ingrese 1 o 2.");
                    break;
            }

        } while (opcion != 2);

        scanner.close();
    }
}