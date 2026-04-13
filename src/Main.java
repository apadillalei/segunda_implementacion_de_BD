import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("========================================");
        System.out.println("   UNIVERSIDAD CENFOTEC - ÁRBOL B+");
        System.out.println("========================================");

        try {
            System.out.print("Defina el orden (m) del árbol: ");
            int m = Integer.parseInt(reader.readLine());
            ArbolBMas arbol = new ArbolBMas(m);
            boolean salir = false;

            while (!salir) {
                System.out.println("\n----------- MENU PRINCIPAL -----------");
                System.out.println("1. Insertar elemento");
                System.out.println("2. Buscar elemento");
                System.out.println("3. Eliminar elemento");
                System.out.println("4. Recorrer rango");
                System.out.println("5. Mostrar estructura del árbol");
                System.out.println("6. Prueba automática (llenar árbol)");
                System.out.println("0. Salir");
                System.out.print("Seleccione una opción: ");

                String opInput = reader.readLine();
                if (opInput == null || opInput.isEmpty()) continue;
                int opcion = Integer.parseInt(opInput);

                switch (opcion) {
                    case 1:
                        System.out.print("Clave: ");
                        int c = Integer.parseInt(reader.readLine());
                        System.out.print("Valor: ");
                        String v = reader.readLine();
                        arbol.insertar(c, v);
                        break;
                    case 2:
                        System.out.print("Clave a buscar: ");
                        int cb = Integer.parseInt(reader.readLine());
                        String r = arbol.buscar(cb);
                        System.out.println(r != null ? "Encontrado: " + r : "No existe.");
                        break;
                    case 3:
                        System.out.print("Clave a eliminar: ");
                        arbol.eliminar(Integer.parseInt(reader.readLine()));
                        break;
                    case 4:
                        System.out.print("Clave inicio: ");
                        int ci = Integer.parseInt(reader.readLine());
                        System.out.print("Cantidad: ");
                        int n = Integer.parseInt(reader.readLine());
                        arbol.recorrerRango(ci, n);
                        break;
                    case 5:
                        arbol.mostrarEstructura();
                        break;
                    case 6:
                        int[] pruebas = {16, 4, 7, 1, 2, 3, 5, 6, 8, 10, 11, 24, 18, 21, 26, 28, 27, 29};
                        for (int p : pruebas) arbol.insertar(p, "Dato-" + p);
                        System.out.println(">> Árbol llenado correctamente.");
                        arbol.mostrarEstructura();
                        break;
                    case 0:
                        salir = true;
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}