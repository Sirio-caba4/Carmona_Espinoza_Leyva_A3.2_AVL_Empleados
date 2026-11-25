public class Pruebas {
    static final String INTEGRANTES = "Angel Manuel Espinoza Lopez, Geovani Gael Carmona Barbosa, Victor Manuel Leyva Perez";

    static Empleado emp(int id) {
        return new Empleado(id, "Empleado " + id, "Puesto", "Depto", 1000.0 * id);
    }

    static void insertar(ArbolAVL a, int id) {
        a.insertar(emp(id));
        String r = a.tomarRotaciones();
        System.out.println("Insertar " + id + (r.isEmpty() ? "" : " -> " + r.trim()));
    }

    public static void main(String[] args) {
        System.out.println(INTEGRANTES);
        ArbolAVL a = new ArbolAVL();
        System.out.println("1. Arbol vacio: profundidad=" + a.profundidad() + " amplitud=" + a.amplitud() + " vacio=" + a.estaVacio());

        System.out.println("2. Rotaciones");
        int[] orden = {10, 20, 30, 40, 50, 25, 5, 4, 3, 35, 45};
        for (int id : orden) {
            insertar(a, id);
        }
        System.out.println(a.dibujar());

        System.out.println("3. Duplicado");
        try {
            a.insertar(emp(30));
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }

        System.out.println("4. Buscar 30: " + a.buscar(30));
        System.out.println("   Buscar 999: " + a.buscar(999));

        System.out.println("5. Eliminaciones");
        for (int id : new int[]{3, 4, 5, 10}) {
            a.eliminar(id);
            String r = a.tomarRotaciones();
            System.out.println("Eliminar " + id + (r.isEmpty() ? "" : " -> " + r.trim()));
        }
        System.out.println(a.dibujar());

        System.out.println("6. InOrden:\n" + a.mostrarInOrden());
        System.out.println("   PreOrden:\n" + a.mostrarPreOrden());
        System.out.println("   PostOrden:\n" + a.mostrarPostOrden());

        System.out.println("7. Amplitud=" + a.amplitud() + " Profundidad=" + a.profundidad() + " Cantidad=" + a.getCantidad());

        a.destruirArbol();
        System.out.println("8. Destruido: vacio=" + a.estaVacio() + " profundidad=" + a.profundidad());

        System.out.println("9. Invalidos");
        Empleado[] malos = {
            new Empleado(1, "", "P", "D", 10.0),
            new Empleado(2, "N", " ", "D", 10.0),
            new Empleado(3, "N", "P", "", 10.0),
            new Empleado(4, "N", "P", "D", -1.0),
            new Empleado(5, "N", "P", "D", Double.NaN),
            new Empleado(null, "N", "P", "D", 10.0)
        };
        for (Empleado m : malos) {
            try {
                a.insertar(m);
                System.out.println("ERROR: se acepto");
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
        System.out.println("Eliminar en arbol vacio: " + a.eliminar(1));
    }
}
