import java.util.ArrayList;
import java.util.List;

public class NodoArbolBMas {
    List<Integer> listaClaves;
    List<String> listaValores;
    List<NodoArbolBMas> listaHijos;
    boolean esHoja;
    NodoArbolBMas siguiente;

    public NodoArbolBMas(boolean esHoja) {
        this.esHoja = esHoja;
        this.listaClaves = new ArrayList<>();
        this.listaValores = new ArrayList<>();
        this.listaHijos = new ArrayList<>();
        this.siguiente = null;
    }
}