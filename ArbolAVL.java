import java.util.ArrayDeque;
import java.util.Queue;

public class ArbolAVL {
    static final String INTEGRANTES = "Angel Manuel Espinoza Lopez, Geovani Gael Carmona Barbosa, Victor Manuel Leyva Perez";

    private NodoAVL raiz;
    private int cantidad;
    private final StringBuilder rotaciones = new StringBuilder();

    public ArbolAVL() {
        raiz = null;
        cantidad = 0;
    }

    public void insertar(Empleado empleado) {
        validar(empleado);
        if (buscar(empleado.getId()) != null) {
            throw new IllegalArgumentException("El identificador " + empleado.getId() + " ya existe.");
        }
        raiz = insertar(raiz, empleado);
        cantidad++;
    }

    private NodoAVL insertar(NodoAVL nodo, Empleado empleado) {
        if (nodo == null) {
            return new NodoAVL(empleado);
        }
        if (empleado.getId() < nodo.dato.getId()) {
            nodo.izquierdo = insertar(nodo.izquierdo, empleado);
        } else {
            nodo.derecho = insertar(nodo.derecho, empleado);
        }
        return balancear(nodo);
    }

    public boolean eliminar(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("El identificador no puede ser nulo.");
        }
        if (buscar(id) == null) {
            return false;
        }
        raiz = eliminar(raiz, id);
        cantidad--;
        return true;
    }

    private NodoAVL eliminar(NodoAVL nodo, int id) {
        if (nodo == null) {
            return null;
        }
        if (id < nodo.dato.getId()) {
            nodo.izquierdo = eliminar(nodo.izquierdo, id);
        } else if (id > nodo.dato.getId()) {
            nodo.derecho = eliminar(nodo.derecho, id);
        } else {
            if (nodo.izquierdo == null) {
                return nodo.derecho;
            }
            if (nodo.derecho == null) {
                return nodo.izquierdo;
            }
            NodoAVL sucesor = nodo.derecho;
            while (sucesor.izquierdo != null) {
                sucesor = sucesor.izquierdo;
            }
            nodo.dato = sucesor.dato;
            nodo.derecho = eliminar(nodo.derecho, sucesor.dato.getId());
        }
        return balancear(nodo);
    }

    public Empleado buscar(Integer id) {
        if (id == null) {
            return null;
        }
        NodoAVL actual = raiz;
        while (actual != null) {
            if (id.equals(actual.dato.getId())) {
                return actual.dato;
            }
            actual = id < actual.dato.getId() ? actual.izquierdo : actual.derecho;
        }
        return null;
    }

    public int obtenerAltura(NodoAVL nodo) {
        return nodo == null ? 0 : nodo.altura;
    }

    public int factorEquilibrio(NodoAVL nodo) {
        return nodo == null ? 0 : obtenerAltura(nodo.derecho) - obtenerAltura(nodo.izquierdo);
    }

    private void actualizarAltura(NodoAVL nodo) {
        nodo.altura = 1 + Math.max(obtenerAltura(nodo.izquierdo), obtenerAltura(nodo.derecho));
    }

    private NodoAVL rotacionIzquierda(NodoAVL x) {
        NodoAVL y = x.derecho;
        x.derecho = y.izquierdo;
        y.izquierdo = x;
        actualizarAltura(x);
        actualizarAltura(y);
        return y;
    }

    private NodoAVL rotacionDerecha(NodoAVL y) {
        NodoAVL x = y.izquierdo;
        y.izquierdo = x.derecho;
        x.derecho = y;
        actualizarAltura(y);
        actualizarAltura(x);
        return x;
    }

    private NodoAVL balancear(NodoAVL nodo) {
        actualizarAltura(nodo);
        int fe = factorEquilibrio(nodo);
        int id = nodo.dato.getId();
        if (fe > 1) {
            if (factorEquilibrio(nodo.derecho) < 0) {
                rotaciones.append("Rotacion doble Derecha-Izquierda en el nodo ").append(id).append("\n");
                nodo.derecho = rotacionDerecha(nodo.derecho);
            } else {
                rotaciones.append("Rotacion simple Izquierda en el nodo ").append(id).append("\n");
            }
            return rotacionIzquierda(nodo);
        }
        if (fe < -1) {
            if (factorEquilibrio(nodo.izquierdo) > 0) {
                rotaciones.append("Rotacion doble Izquierda-Derecha en el nodo ").append(id).append("\n");
                nodo.izquierdo = rotacionIzquierda(nodo.izquierdo);
            } else {
                rotaciones.append("Rotacion simple Derecha en el nodo ").append(id).append("\n");
            }
            return rotacionDerecha(nodo);
        }
        return nodo;
    }

    public String mostrarInOrden() {
        StringBuilder sb = new StringBuilder();
        inOrden(raiz, sb);
        return sb.toString();
    }

    private void inOrden(NodoAVL nodo, StringBuilder sb) {
        if (nodo == null) {
            return;
        }
        inOrden(nodo.izquierdo, sb);
        sb.append(nodo.dato).append("\n");
        inOrden(nodo.derecho, sb);
    }

    public String mostrarPreOrden() {
        StringBuilder sb = new StringBuilder();
        preOrden(raiz, sb);
        return sb.toString();
    }

    private void preOrden(NodoAVL nodo, StringBuilder sb) {
        if (nodo == null) {
            return;
        }
        sb.append(nodo.dato).append("\n");
        preOrden(nodo.izquierdo, sb);
        preOrden(nodo.derecho, sb);
    }

    public String mostrarPostOrden() {
        StringBuilder sb = new StringBuilder();
        postOrden(raiz, sb);
        return sb.toString();
    }

    private void postOrden(NodoAVL nodo, StringBuilder sb) {
        if (nodo == null) {
            return;
        }
        postOrden(nodo.izquierdo, sb);
        postOrden(nodo.derecho, sb);
        sb.append(nodo.dato).append("\n");
    }

    public int amplitud() {
        if (raiz == null) {
            return 0;
        }
        int maximo = 0;
        Queue<NodoAVL> cola = new ArrayDeque<>();
        cola.add(raiz);
        while (!cola.isEmpty()) {
            int tam = cola.size();
            maximo = Math.max(maximo, tam);
            for (int i = 0; i < tam; i++) {
                NodoAVL n = cola.poll();
                if (n.izquierdo != null) {
                    cola.add(n.izquierdo);
                }
                if (n.derecho != null) {
                    cola.add(n.derecho);
                }
            }
        }
        return maximo;
    }

    public int profundidad() {
        return obtenerAltura(raiz);
    }

    public void destruirArbol() {
        raiz = null;
        cantidad = 0;
        rotaciones.setLength(0);
    }

    public boolean estaVacio() {
        return raiz == null;
    }

    public int getCantidad() {
        return cantidad;
    }

    public String tomarRotaciones() {
        String r = rotaciones.toString();
        rotaciones.setLength(0);
        return r;
    }

    public String dibujar() {
        if (raiz == null) {
            return "(arbol vacio)";
        }
        StringBuilder sb = new StringBuilder();
        dibujar(raiz, 0, sb);
        return sb.toString();
    }

    private void dibujar(NodoAVL nodo, int nivel, StringBuilder sb) {
        if (nodo == null) {
            return;
        }
        dibujar(nodo.derecho, nivel + 1, sb);
        sb.append("      ".repeat(nivel)).append(nodo.dato.getId())
                .append(" [FE=").append(factorEquilibrio(nodo))
                .append(" h=").append(nodo.altura).append("]\n");
        dibujar(nodo.izquierdo, nivel + 1, sb);
    }

    private void validar(Empleado e) {
        if (e == null) {
            throw new IllegalArgumentException("El empleado no puede ser nulo.");
        }
        if (e.getId() == null) {
            throw new IllegalArgumentException("El identificador es obligatorio.");
        }
        if (e.getNombre() == null || e.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre es obligatorio.");
        }
        if (e.getPuesto() == null || e.getPuesto().trim().isEmpty()) {
            throw new IllegalArgumentException("El puesto es obligatorio.");
        }
        if (e.getDepartamento() == null || e.getDepartamento().trim().isEmpty()) {
            throw new IllegalArgumentException("El departamento es obligatorio.");
        }
        if (e.getSalario() == null || e.getSalario().isNaN() || e.getSalario().isInfinite() || e.getSalario() < 0) {
            throw new IllegalArgumentException("El salario debe ser un numero finito no negativo.");
        }
    }
}
