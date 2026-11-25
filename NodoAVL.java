public class NodoAVL {
    static final String INTEGRANTES = "Angel Manuel Espinoza Lopez, Geovani Gael Carmona Barbosa, Victor Manuel Leyva Perez";

    Empleado dato;
    NodoAVL izquierdo;
    NodoAVL derecho;
    Integer altura;

    public NodoAVL(Empleado dato) {
        this.dato = dato;
        this.izquierdo = null;
        this.derecho = null;
        this.altura = 1;
    }

    public Empleado getDato() {
        return dato;
    }

    public NodoAVL getIzquierdo() {
        return izquierdo;
    }

    public NodoAVL getDerecho() {
        return derecho;
    }

    public Integer getAltura() {
        return altura;
    }
}
