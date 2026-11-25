import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.nio.file.Files;
import java.util.List;

public class Main extends JFrame {
    static final String INTEGRANTES = "Angel Manuel Espinoza Lopez, Geovani Gael Carmona Barbosa, Victor Manuel Leyva Perez";

    private ArbolAVL arbol;
    private final JTextField txtId = new JTextField(8);
    private final JTextField txtNombre = new JTextField(14);
    private final JTextField txtPuesto = new JTextField(10);
    private final JTextField txtDepartamento = new JTextField(10);
    private final JTextField txtSalario = new JTextField(8);
    private final JTextArea areaMensajes = new JTextArea();
    private final JTextArea areaArbol = new JTextArea();
    private final JLabel lblEstado = new JLabel(" ");

    public Main() {
        super("Arbol AVL - Gestion de Empleados - " + INTEGRANTES);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel campos = new JPanel(new FlowLayout(FlowLayout.LEFT));
        campos.add(new JLabel("ID"));
        campos.add(txtId);
        campos.add(new JLabel("Nombre"));
        campos.add(txtNombre);
        campos.add(new JLabel("Puesto"));
        campos.add(txtPuesto);
        campos.add(new JLabel("Departamento"));
        campos.add(txtDepartamento);
        campos.add(new JLabel("Salario"));
        campos.add(txtSalario);

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.LEFT));
        agregarBoton(botones, "Crear arbol", () -> crearArbol());
        agregarBoton(botones, "Agregar", () -> agregarEmpleado());
        agregarBoton(botones, "Buscar", () -> buscarEmpleado());
        agregarBoton(botones, "Eliminar", () -> eliminarEmpleado());
        agregarBoton(botones, "InOrden", () -> mostrarRecorrido(1));
        agregarBoton(botones, "PreOrden", () -> mostrarRecorrido(2));
        agregarBoton(botones, "PostOrden", () -> mostrarRecorrido(3));
        agregarBoton(botones, "Amplitud", () -> calcularAmplitud());
        agregarBoton(botones, "Profundidad", () -> calcularProfundidad());
        agregarBoton(botones, "Destruir", () -> destruirArbol());
        agregarBoton(botones, "Precargar ejemplo", () -> precargarEjemplo());
        agregarBoton(botones, "Cargar archivo", () -> cargarArchivo());

        JPanel norte = new JPanel(new GridLayout(2, 1));
        norte.add(campos);
        norte.add(botones);
        add(norte, BorderLayout.NORTH);

        areaMensajes.setEditable(false);
        areaArbol.setEditable(false);
        areaArbol.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));

        JScrollPane sp1 = new JScrollPane(areaMensajes);
        JScrollPane sp2 = new JScrollPane(areaArbol);
        sp1.setBorder(null);
        sp2.setBorder(null);
        JSplitPane division = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, sp1, sp2);
        division.setResizeWeight(0.5);
        division.setBorder(null);
        add(division, BorderLayout.CENTER);
        add(lblEstado, BorderLayout.SOUTH);

        setSize(1150, 600);
        setLocationRelativeTo(null);
        mostrar("Presione Crear arbol para comenzar.");
    }

    private void agregarBoton(JPanel panel, String texto, Runnable accion) {
        JButton b = new JButton(texto);
        b.addActionListener(e -> accion.run());
        panel.add(b);
    }

    private void mostrar(String texto) {
        areaMensajes.setText(texto);
        areaMensajes.setCaretPosition(0);
        actualizarVista();
    }

    private void actualizarVista() {
        if (arbol == null) {
            areaArbol.setText("(sin arbol)");
            lblEstado.setText("Sin arbol creado");
            return;
        }
        areaArbol.setText("Raiz a la izquierda, hijo derecho arriba, hijo izquierdo abajo\n\n" + arbol.dibujar());
        areaArbol.setCaretPosition(0);
        lblEstado.setText("Empleados: " + arbol.getCantidad() + " | Profundidad: " + arbol.profundidad() + " | Amplitud: " + arbol.amplitud());
    }

    private boolean hayArbol() {
        if (arbol == null) {
            mostrar("Primero debe crear el arbol AVL.");
            return false;
        }
        return true;
    }

    private Integer leerId() {
        String t = txtId.getText().trim();
        if (t.isEmpty()) {
            throw new IllegalArgumentException("El identificador es obligatorio.");
        }
        try {
            return Integer.valueOf(t);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("El identificador debe ser un numero entero valido.");
        }
    }

    private Double leerSalario() {
        String t = txtSalario.getText().trim();
        if (t.isEmpty()) {
            throw new IllegalArgumentException("El salario es obligatorio.");
        }
        try {
            Double d = Double.valueOf(t);
            if (d.isNaN() || d.isInfinite() || d < 0) {
                throw new IllegalArgumentException("El salario debe ser un numero finito no negativo.");
            }
            return d;
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("El salario debe ser numerico.");
        }
    }

    private void crearArbol() {
        arbol = new ArbolAVL();
        mostrar("Arbol AVL creado y listo para utilizarse.");
    }

    private void agregarEmpleado() {
        if (!hayArbol()) {
            return;
        }
        try {
            Integer id = leerId();
            Double salario = leerSalario();
            Empleado e = new Empleado(id, txtNombre.getText().trim(), txtPuesto.getText().trim(), txtDepartamento.getText().trim(), salario);
            arbol.insertar(e);
            String rot = arbol.tomarRotaciones();
            mostrar("Empleado agregado:\n" + e + "\n\n" + (rot.isEmpty() ? "No hubo rotaciones." : "Rebalanceo automatico:\n" + rot));
            limpiarCampos();
        } catch (IllegalArgumentException ex) {
            arbol.tomarRotaciones();
            mostrar("No se pudo agregar: " + ex.getMessage());
        }
    }

    private void buscarEmpleado() {
        if (!hayArbol()) {
            return;
        }
        try {
            Integer id = leerId();
            if (arbol.estaVacio()) {
                mostrar("El arbol esta vacio.");
                return;
            }
            Empleado e = arbol.buscar(id);
            mostrar(e == null ? "No existe un empleado con ID " + id + "." : "Empleado encontrado:\n" + e);
        } catch (IllegalArgumentException ex) {
            mostrar("No se pudo buscar: " + ex.getMessage());
        }
    }

    private void eliminarEmpleado() {
        if (!hayArbol()) {
            return;
        }
        try {
            Integer id = leerId();
            if (arbol.estaVacio()) {
                mostrar("El arbol esta vacio.");
                return;
            }
            if (arbol.eliminar(id)) {
                String rot = arbol.tomarRotaciones();
                mostrar("Empleado con ID " + id + " eliminado.\n\n" + (rot.isEmpty() ? "No hubo rotaciones." : "Rebalanceo automatico:\n" + rot));
            } else {
                mostrar("No existe un empleado con ID " + id + ".");
            }
        } catch (IllegalArgumentException ex) {
            mostrar("No se pudo eliminar: " + ex.getMessage());
        }
    }

    private void mostrarRecorrido(int tipo) {
        if (!hayArbol()) {
            return;
        }
        if (arbol.estaVacio()) {
            mostrar("El arbol esta vacio.");
            return;
        }
        if (tipo == 1) {
            mostrar("Recorrido InOrden:\n" + arbol.mostrarInOrden());
        } else if (tipo == 2) {
            mostrar("Recorrido PreOrden:\n" + arbol.mostrarPreOrden());
        } else {
            mostrar("Recorrido PostOrden:\n" + arbol.mostrarPostOrden());
        }
    }

    private void calcularAmplitud() {
        if (hayArbol()) {
            mostrar("Amplitud (maximo de nodos en un nivel): " + arbol.amplitud());
        }
    }

    private void calcularProfundidad() {
        if (hayArbol()) {
            mostrar("Profundidad del arbol: " + arbol.profundidad());
        }
    }

    private void destruirArbol() {
        if (!hayArbol()) {
            return;
        }
        arbol.destruirArbol();
        mostrar("Arbol destruido. Ahora esta vacio.");
    }

    private void limpiarCampos() {
        txtId.setText("");
        txtNombre.setText("");
        txtPuesto.setText("");
        txtDepartamento.setText("");
        txtSalario.setText("");
    }

    private void precargarEjemplo() {
        arbol = new ArbolAVL();
        String[] datos = {
            "10,Ana Torres,Gerente,Administracion,35000",
            "20,Luis Perez,Contador,Finanzas,22000",
            "30,Maria Lopez,Analista,Sistemas,20000",
            "40,Jose Ramirez,Programador,Sistemas,25000",
            "50,Laura Gomez,Disenadora,Marketing,18000",
            "25,Pedro Sanchez,Auditor,Finanzas,21000",
            "5,Sofia Cruz,Recepcionista,Administracion,12000",
            "15,Carlos Diaz,Soporte,Sistemas,14000",
            "35,Elena Ruiz,Vendedora,Ventas,16000",
            "45,Miguel Castro,Vendedor,Ventas,16500",
            "60,Rosa Flores,Abogada,Legal,28000",
            "70,Diego Mora,Almacenista,Logistica,13000",
            "1,Paola Reyes,Practicante,Sistemas,8000",
            "12,Hugo Vargas,Chofer,Logistica,11000",
            "80,Irene Salas,Directora,Direccion,50000"
        };
        StringBuilder sb = new StringBuilder("Ejemplo precargado:\n");
        for (String linea : datos) {
            sb.append(procesarLinea(linea));
        }
        sb.append("\nRotaciones realizadas:\n").append(arbol.tomarRotaciones());
        mostrar(sb.toString());
    }

    private void cargarArchivo() {
        JFileChooser fc = new JFileChooser(new File("."));
        if (fc.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) {
            return;
        }
        try {
            List<String> lineas = Files.readAllLines(fc.getSelectedFile().toPath());
            arbol = new ArbolAVL();
            StringBuilder sb = new StringBuilder("Archivo: " + fc.getSelectedFile().getName() + "\n");
            for (String linea : lineas) {
                if (!linea.trim().isEmpty()) {
                    sb.append(procesarLinea(linea));
                }
            }
            sb.append("\nRotaciones realizadas:\n").append(arbol.tomarRotaciones());
            mostrar(sb.toString());
        } catch (Exception ex) {
            mostrar("No se pudo leer el archivo: " + ex.getMessage());
        }
    }

    private String procesarLinea(String linea) {
        String[] p = linea.split(",");
        if (p.length != 5) {
            return "Linea invalida (se esperan 5 campos): " + linea + "\n";
        }
        try {
            Empleado e = new Empleado(Integer.valueOf(p[0].trim()), p[1].trim(), p[2].trim(), p[3].trim(), Double.valueOf(p[4].trim()));
            arbol.insertar(e);
            return "Agregado: " + e + "\n";
        } catch (IllegalArgumentException ex) {
            return "Rechazado (" + ex.getMessage() + "): " + linea + "\n";
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Main().setVisible(true));
    }
}
