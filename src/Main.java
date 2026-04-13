import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Main {
    public static void main(String[] args) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("====================================================");
        System.out.println("   UNIVERSIDAD CENFOTEC - ESTRUCTURAS DE DATOS");
        System.out.println("            PROYECTO: ÁRBOL B+ (SCV3)");
        System.out.println("====================================================");

        try {
            System.out.print("Defina el orden (m) del árbol (mínimo 3): ");
            int m = Integer.parseInt(reader.readLine());

            // Validación básica para evitar árboles inconsistentes
            if (m < 3) {
                System.out.println("El orden debe ser al menos 3. Ajustando a 3...");
                m = 3;
            }

            ArbolBMas arbol = new ArbolBMas(m);
            boolean salir = false;

            while (!salir) {
                System.out.println("\n----------- MENÚ DE OPERACIONES -----------");
                System.out.println("1. Insertar (Clave/Valor)");
                System.out.println("2. Buscar por Clave");
                System.out.println("3. Eliminar Clave (Con Rebalanceo)");
                System.out.println("4. Recorrer Rango");
                System.out.println("5. Ver Estructura del Árbol");
                System.out.println("6. Cargar Datos de Prueba");
                System.out.println("0. Salir");
                System.out.print("Seleccione una opción: ");

                String opInput = reader.readLine();
                if (opInput == null || opInput.isEmpty()) continue;
                int opcion = Integer.parseInt(opInput);

                switch (opcion) {
                    case 1:
                        System.out.print("Ingrese Clave (Entero): ");
                        int c = Integer.parseInt(reader.readLine());
                        System.out.print("Ingrese Valor (Texto): ");
                        String v = reader.readLine();
                        arbol.insertar(c, v);
                        System.out.println(">> Insertado.");
                        break;
                    case 2:
                        System.out.print("Clave a buscar: ");
                        int cb = Integer.parseInt(reader.readLine());
                        String res = arbol.buscar(cb);
                        System.out.println(res != null ? ">> Encontrado: " + res : ">> Error: Clave no existe.");
                        break;
                    case 3:
                        System.out.print("Clave a eliminar: ");
                        int ce = Integer.parseInt(reader.readLine());
                        arbol.eliminar(ce); // El método ya imprime si lo logró o no
                        arbol.mostrarEstructura(); // Mostramos cómo quedó el balanceo
                        break;
                    case 4:
                        System.out.print("Clave de inicio: ");
                        int ci = Integer.parseInt(reader.readLine());
                        System.out.print("¿Cuántos elementos mostrar?: ");
                        int n = Integer.parseInt(reader.readLine());
                        arbol.recorrerRango(ci, n);
                        break;
                    case 5:
                        arbol.mostrarEstructura();
                        break;
                    case 6:
                        // Datos para forzar divisiones y fusiones
                        int[] pruebas = {10, 20, 30, 40, 50, 60, 70, 80, 90, 100};
                        for (int p : pruebas) arbol.insertar(p, "Dato_" + p);
                        System.out.println(">> Datos de prueba insertados.");
                        arbol.mostrarEstructura();
                        break;
                    case 0:
                        System.out.println("Cerrando programa...");
                        salir = true;
                        break;
                    default:
                        System.out.println("Opción no válida.");
                }
            }
        } catch (Exception e) {
            System.out.println("Error en la entrada de datos: " + e.getMessage());
        }
    }
}