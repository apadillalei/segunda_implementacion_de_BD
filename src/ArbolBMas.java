import java.util.Collections;
import java.util.ArrayList;

/**
 * Implementación de un Árbol B+ para la gestión eficiente de datos.
 * Cumple con las propiedades de balanceo automático y almacenamiento de datos en hojas.
 */
public class ArbolBMas {
    private NodoArbolBMas raiz;
    private int m;

    public ArbolBMas(int orden) {
        this.m = orden;
        this.raiz = new NodoArbolBMas(true);
    }

    // --- MÉTODOS DE INSERCIÓN ---

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
            for (int j = mid; j < hijo.listaClaves.size(); j++) {
                nuevoNodo.listaClaves.add(hijo.listaClaves.get(j));
                nuevoNodo.listaValores.add(hijo.listaValores.get(j));
            }
            nuevoNodo.siguiente = hijo.siguiente;
            hijo.siguiente = nuevoNodo;

            int originalSize = hijo.listaClaves.size();
            for (int j = mid; j < originalSize; j++) {
                hijo.listaClaves.remove(mid);
                hijo.listaValores.remove(mid);
            }
            padre.listaClaves.add(i, nuevoNodo.listaClaves.get(0));
        } else {
            int claveSubida = hijo.listaClaves.get(mid);
            for (int j = mid + 1; j < hijo.listaClaves.size(); j++) {
                nuevoNodo.listaClaves.add(hijo.listaClaves.get(j));
            }
            for (int j = mid + 1; j < hijo.listaHijos.size(); j++) {
                nuevoNodo.listaHijos.add(hijo.listaHijos.get(j));
            }

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
            while (i >= 0 && k < nodo.listaClaves.get(i)) i--;
            i++;
            if (nodo.listaHijos.get(i).listaClaves.size() == m - 1) {
                dividirHijo(nodo, i, nodo.listaHijos.get(i));
                if (k >= nodo.listaClaves.get(i)) i++;
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

    // --- MÉTODOS DE BÚSQUEDA ---

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
        System.out.println("\n--- Recorrido de Rango (" + n + " elementos) ---");
        while (actual != null && cont < n) {
            for (int i = 0; i < actual.listaClaves.size() && cont < n; i++) {
                if (actual.listaClaves.get(i) >= claveInicio) {
                    System.out.println("Clave: " + actual.listaClaves.get(i) + " | Valor: " + actual.listaValores.get(i));
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

    // --- MÉTODOS DE ELIMINACIÓN Y REBALANCEO ---

    public void eliminar(int clave) {
        if (raiz.listaClaves.isEmpty()) {
            System.out.println("El árbol está vacío.");
            return;
        }
        eliminarRecursivo(raiz, clave);
        if (!raiz.esHoja && raiz.listaClaves.isEmpty()) {
            raiz = raiz.listaHijos.get(0);
        }
    }

    private void eliminarRecursivo(NodoArbolBMas actual, int clave) {
        int i = 0;
        while (i < actual.listaClaves.size() && clave > actual.listaClaves.get(i)) i++;

        if (actual.esHoja) {
            if (i < actual.listaClaves.size() && actual.listaClaves.get(i) == clave) {
                actual.listaClaves.remove(i);
                actual.listaValores.remove(i);
                System.out.println(">> Clave " + clave + " eliminada.");
            } else {
                System.out.println(">> Clave no encontrada.");
            }
            return;
        }

        if (i < actual.listaClaves.size() && actual.listaClaves.get(i) == clave) i++;

        NodoArbolBMas hijo = actual.listaHijos.get(i);
        eliminarRecursivo(hijo, clave);

        // Rebalanceo: m/2 es el mínimo de hijos, por lo tanto m/2 - 1 es el mínimo de claves
        int minClaves = (int) Math.ceil(m / 2.0) - 1;
        if (hijo.listaClaves.size() < minClaves) {
            rebalancear(actual, i);
        }
    }

    private void rebalancear(NodoArbolBMas padre, int idxHijo) {
        if (idxHijo > 0 && padre.listaHijos.get(idxHijo - 1).listaClaves.size() > (int) Math.ceil(m / 2.0) - 1) {
            prestarIzquierdo(padre, idxHijo);
        } else if (idxHijo < padre.listaHijos.size() - 1 && padre.listaHijos.get(idxHijo + 1).listaClaves.size() > (int) Math.ceil(m / 2.0) - 1) {
            prestarDerecho(padre, idxHijo);
        } else {
            if (idxHijo > 0) fusionar(padre, idxHijo - 1);
            else fusionar(padre, idxHijo);
        }
    }

    private void prestarIzquierdo(NodoArbolBMas padre, int idx) {
        NodoArbolBMas hijo = padre.listaHijos.get(idx);
        NodoArbolBMas izq = padre.listaHijos.get(idx - 1);
        hijo.listaClaves.add(0, izq.listaClaves.remove(izq.listaClaves.size() - 1));
        if (hijo.esHoja) {
            hijo.listaValores.add(0, izq.listaValores.remove(izq.listaValores.size() - 1));
            padre.listaClaves.set(idx - 1, hijo.listaClaves.get(0));
        } else {
            hijo.listaHijos.add(0, izq.listaHijos.remove(izq.listaHijos.size() - 1));
            padre.listaClaves.set(idx - 1, hijo.listaClaves.get(0));
        }
    }

    private void prestarDerecho(NodoArbolBMas padre, int idx) {
        NodoArbolBMas hijo = padre.listaHijos.get(idx);
        NodoArbolBMas der = padre.listaHijos.get(idx + 1);
        hijo.listaClaves.add(der.listaClaves.remove(0));
        if (hijo.esHoja) {
            hijo.listaValores.add(der.listaValores.remove(0));
            padre.listaClaves.set(idx, der.listaClaves.get(0));
        } else {
            hijo.listaHijos.add(der.listaHijos.remove(0));
            padre.listaClaves.set(idx, der.listaClaves.get(0));
        }
    }

    private void fusionar(NodoArbolBMas padre, int idx) {
        NodoArbolBMas izq = padre.listaHijos.get(idx);
        NodoArbolBMas der = padre.listaHijos.remove(idx + 1);
        izq.listaClaves.addAll(der.listaClaves);
        izq.listaValores.addAll(der.listaValores);
        izq.listaHijos.addAll(der.listaHijos);
        if (izq.esHoja) izq.siguiente = der.siguiente;
        padre.listaClaves.remove(idx);
    }

    // --- VISUALIZACIÓN ---

    public void mostrarEstructura() {
        System.out.println("\n--- Representación Visual del Árbol ---");
        mostrarRecursivo(raiz, "", true);
    }

    private void mostrarRecursivo(NodoArbolBMas nodo, String prefijo, boolean esUltimo) {
        System.out.print(prefijo + (esUltimo ? "└── " : "├── "));
        System.out.println((nodo.esHoja ? "[H] " : "[I] ") + nodo.listaClaves);
        if (!nodo.esHoja) {
            for (int i = 0; i < nodo.listaHijos.size(); i++) {
                mostrarRecursivo(nodo.listaHijos.get(i), prefijo + (esUltimo ? "    " : "│   "), i == nodo.listaHijos.size() - 1);
            }
        }
    }
}