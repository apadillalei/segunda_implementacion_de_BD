import java.util.Collections;

public class ArbolBMas {
    private NodoArbolBMas raiz;
    private int m;

    public ArbolBMas(int orden) {
        this.m = orden;
        this.raiz = new NodoArbolBMas(true);
    }

    public void insertar(int clave, String valor) {
        NodoArbolBMas r = raiz;
        if (r.listaClaves.size() == m - 1) {
            NodoArbolBMas s = new NodoArbolBMas(false);
            raiz = s;
            s.listaHijos.add(r);
            dividirHijo(s, 0, r);
            insertarNoLleno(s, clave, valor);
        } else {
            insertarNoLleno(r, clave, valor);
        }
    }

    public void dividirHijo(NodoArbolBMas padre, int i, NodoArbolBMas hijo) {
        NodoArbolBMas nuevoNodo = new NodoArbolBMas(hijo.esHoja);
        int mid = hijo.listaClaves.size() / 2;

        if (hijo.esHoja) {
            // Caso Hoja: Copiamos desde mid hasta el final
            for (int j = mid; j < hijo.listaClaves.size(); j++) {
                nuevoNodo.listaClaves.add(hijo.listaClaves.get(j));
                nuevoNodo.listaValores.add(hijo.listaValores.get(j));
            }
            nuevoNodo.siguiente = hijo.siguiente;
            hijo.siguiente = nuevoNodo;

            // Limpiar claves movidas del original
            int originalSize = hijo.listaClaves.size();
            for (int j = mid; j < originalSize; j++) {
                hijo.listaClaves.remove(mid);
                hijo.listaValores.remove(mid);
            }
            padre.listaClaves.add(i, nuevoNodo.listaClaves.get(0));
        } else {
            // Caso Nodo Interno: La clave del medio sube
            int claveSubida = hijo.listaClaves.get(mid);

            // Mover claves e hijos al nuevo nodo
            for (int j = mid + 1; j < hijo.listaClaves.size(); j++) {
                nuevoNodo.listaClaves.add(hijo.listaClaves.get(j));
            }
            for (int j = mid + 1; j < hijo.listaHijos.size(); j++) {
                nuevoNodo.listaHijos.add(hijo.listaHijos.get(j));
            }

            // Limpiar hijo original
            int sizeClaves = hijo.listaClaves.size();
            for (int j = mid; j < sizeClaves; j++) {
                hijo.listaClaves.remove(mid);
            }
            int sizeHijos = hijo.listaHijos.size();
            for (int j = mid + 1; j < sizeHijos; j++) {
                hijo.listaHijos.remove(mid + 1);
            }

            padre.listaClaves.add(i, claveSubida);
        }
        padre.listaHijos.add(i + 1, nuevoNodo);
    }

    private void insertarNoLleno(NodoArbolBMas nodo, int k, String v) {
        int i = nodo.listaClaves.size() - 1;
        if (nodo.esHoja) {
            nodo.listaClaves.add(k);
            nodo.listaValores.add(v);
            ordenarHoja(nodo);
        } else {
            while (i >= 0 && k < nodo.listaClaves.get(i)) {
                i--;
            }
            i++;
            if (nodo.listaHijos.get(i).listaClaves.size() == m - 1) {
                dividirHijo(nodo, i, nodo.listaHijos.get(i));
                if (k >= nodo.listaClaves.get(i)) {
                    i++;
                }
            }
            insertarNoLleno(nodo.listaHijos.get(i), k, v);
        }
    }

    private void ordenarHoja(NodoArbolBMas nodo) {
        for (int i = 0; i < nodo.listaClaves.size(); i++) {
            for (int j = i + 1; j < nodo.listaClaves.size(); j++) {
                if (nodo.listaClaves.get(i) > nodo.listaClaves.get(j)) {
                    Collections.swap(nodo.listaClaves, i, j);
                    Collections.swap(nodo.listaValores, i, j);
                }
            }
        }
    }

    public String buscar(int clave) {
        return buscarRecursivo(raiz, clave);
    }

    private String buscarRecursivo(NodoArbolBMas actual, int clave) {
        int i = 0;
        while (i < actual.listaClaves.size() && clave > actual.listaClaves.get(i)) i++;
        if (actual.esHoja) {
            if (i < actual.listaClaves.size() && actual.listaClaves.get(i) == clave) {
                return actual.listaValores.get(i);
            }
            return null;
        }
        return buscarRecursivo(actual.listaHijos.get(i), clave);
    }

    public void recorrerRango(int claveInicio, int n) {
        NodoArbolBMas actual = encontrarHoja(raiz, claveInicio);
        int cont = 0;
        System.out.println("\n--- Recorrido de Rango ---");
        while (actual != null && cont < n) {
            for (int i = 0; i < actual.listaClaves.size() && cont < n; i++) {
                if (actual.listaClaves.get(i) >= claveInicio) {
                    System.out.println("[" + actual.listaClaves.get(i) + "]: " + actual.listaValores.get(i));
                    cont++;
                }
            }
            actual = actual.siguiente;
        }
    }

    private NodoArbolBMas encontrarHoja(NodoArbolBMas nodo, int clave) {
        if (nodo.esHoja) return nodo;
        int i = 0;
        while (i < nodo.listaClaves.size() && clave >= nodo.listaClaves.get(i)) i++;
        return encontrarHoja(nodo.listaHijos.get(i), clave);
    }

    public void eliminar(int clave) {
        NodoArbolBMas hoja = encontrarHoja(raiz, clave);
        for (int i = 0; i < hoja.listaClaves.size(); i++) {
            if (hoja.listaClaves.get(i) == clave) {
                hoja.listaClaves.remove(i);
                hoja.listaValores.remove(i);
                System.out.println("Clave " + clave + " eliminada.");
                return;
            }
        }
        System.out.println("Clave no encontrada.");
    }

    public void mostrarEstructura() {
        System.out.println("\nEstructura actual del Arbol B+:");
        mostrarRecursivo(raiz, "", true);
    }

    private void mostrarRecursivo(NodoArbolBMas nodo, String prefijo, boolean esUltimo) {
        System.out.print(prefijo + (esUltimo ? "+- " : "|- "));
        String tipo = nodo.esHoja ? "[HOJA]" : "[INTERNO]";
        System.out.println(tipo + " " + nodo.listaClaves);
        if (!nodo.esHoja) {
            for (int i = 0; i < nodo.listaHijos.size(); i++) {
                mostrarRecursivo(nodo.listaHijos.get(i), prefijo + (esUltimo ? "   " : "|  "), i == nodo.listaHijos.size() - 1);
            }
        }
    }
}