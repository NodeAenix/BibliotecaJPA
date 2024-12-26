package util;

import entity.Ejemplar;
import entity.Libro;
import entity.Prestamo;
import entity.Usuario;
import model.GenericDAO;
import model.PrestamoDAO;
import model.UsuarioDAO;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Stream;

public class Console {

    private static final Scanner sc = new Scanner(System.in);
    private static final GenericDAO<Libro, String> libroDAO = new GenericDAO<>(Libro.class);
    private static final GenericDAO<Ejemplar, Integer> ejemplarDAO = new GenericDAO<>(Ejemplar.class);
    private static final PrestamoDAO prestamoDAO = new PrestamoDAO();
    private static final UsuarioDAO usuarioDAO = new UsuarioDAO();

    private Console() {}

    // -------------------
    //      MAIN MENU
    // -------------------
    public static void displayMenu() {
        boolean loop = true;
        while (loop) {
            System.out.println("------- INICIO -------");
            System.out.println("1: Iniciar sesión");
            System.out.println("0: Salir");
            System.out.print("~> ");
            switch (sc.next()) {
                case "0" -> {
                    System.out.println("Saliendo...");
                    loop = false;
                }
                case "1" -> {
                    System.out.print("email ~> ");
                    String email = sc.next();
                    System.out.print("contraseña ~> ");
                    String password = sc.next();
                    Usuario usuario = usuarioDAO.getUserByEmailAndPassword(email, password);
                    if (usuario == null) {
                        showInvalidCredentialsMsg();
                    } else {
                        if (usuario.getTipo().equals("normal")) {
                            loop = false;
                            displayUsuarioPrestamos(usuario.getNombre(), usuario.getId());
                        } else {
                            loop = false;
                            displayManagerMenu();
                        }
                    }
                }
                default -> showInvalidInputMsg();
            }
        }
    }

    // -------------------------
    //   PRÉSTAMOS DEL USUARIO
    // -------------------------
    private static void displayUsuarioPrestamos(String username, int userId) {
        System.out.println("¡Hola " + username + "!\nEstos son tus préstamos:");
        prestamoDAO.getUserPrestamos(userId).forEach(System.out::println);
    }

    // ------------------
    //    MANAGER MENU
    // ------------------
    private static void displayManagerMenu() {
        boolean loop = true;
        while (loop) {
            System.out.println("-------- MENU --------");
            System.out.println("1: Gestionar libros");
            System.out.println("2: Gestionar ejemplares");
            System.out.println("3: Gestionar préstamos");
            System.out.println("0: Cerrar sesión");
            System.out.print("~> ");
            switch (sc.next()) {
                case "0" -> {
                    loop = false;
                    displayMenu();
                }
                case "1" -> displayLibrosMenu();
                case "2" -> displayEjemplaresMenu();
                case "3" -> displayPrestamosMenu();
                default -> showInvalidInputMsg();
            }
        }
    }

    // -------------------
    //     LIBROS MENU
    // -------------------
    private static void displayLibrosMenu() {
        boolean loop = true;
        while (loop) {
            System.out.println("Introduzca una de las siguientes opciones:");
            System.out.println("1: Listar libros");
            System.out.println("2: Crear o actualizar libro");
            System.out.println("3: Borrar libro");
            System.out.println("0: Volver");
            System.out.print("~> ");
            switch (sc.next()) {
                case "0" -> loop = false;
                case "1" -> displayListLibrosMenu();
                case "2" -> displayInsertUpdateLibroMenu();
                case "3" -> displayDeleteLibroMenu();
                default -> showInvalidInputMsg();
            }
        }
    }

    private static void displayListLibrosMenu() {
        boolean loop = true;
        while (loop) {
            System.out.println("Introduzca una de las siguientes opciones:");
            System.out.println("1: Buscar un libro");
            System.out.println("2: Listar todos los libros");
            System.out.println("0: Volver");
            System.out.print("~> ");
            switch (sc.next()) {
                case "0" -> loop = false;
                case "1" -> {
                    System.out.print("ISBN ~> ");
                    Libro libro = libroDAO.getById(sc.next());
                    Optional.ofNullable(libro)
                            .ifPresentOrElse(System.out::println, () -> System.out.println("Libro no encontrado."));
                }
                case "2" -> {
                    System.out.println("------ LISTA DE LIBROS ------");
                    libroDAO.getAll().forEach(System.out::println);
                    System.out.println("-----------------------------");
                }
                default -> showInvalidInputMsg();
            }
        }
    }

    private static void displayInsertUpdateLibroMenu() {
        Libro libro = new Libro();
        String[] data = new String[3];
        System.out.print("ISBN ~> ");
        data[0] = sc.next();
        sc.nextLine(); // clear scanner buffer
        System.out.print("Titulo ~> ");
        data[1] = sc.nextLine();
        System.out.print("Autor ~> ");
        data[2] = sc.nextLine();

        if (Stream.of(data).anyMatch(String::isBlank)) {
            showInvalidFieldMsg();
        } else {
            libro.setIsbn(data[0]);
            libro.setTitulo(data[1]);
            libro.setAutor(data[2]);
            libroDAO.update(libro); // si no existe, lo inserta
            System.out.println("-> ¡HECHO! <-");
        }
    }

    private static void displayDeleteLibroMenu() {
        System.out.println("Introduzca el ISBN del libro que desea borrar.");
        System.out.print("ISBN ~> ");
        String isbn = sc.next();
        if (isbn.isBlank()) {
            showInvalidFieldMsg();
        } else {
            Libro libro = libroDAO.getById(isbn);
            Optional.ofNullable(libro)
                    .ifPresentOrElse(l -> {
                        libroDAO.delete(l);
                        System.out.println("-> ¡HECHO! <-");
                    }, () -> System.out.println("Libro no encontrado."));
        }
    }

    // ---------------------
    //    EJEMPLARES MENU
    // ---------------------
    private static void displayEjemplaresMenu() {
        boolean loop = true;
        while (loop) {
            System.out.println("Introduzca una de las siguientes opciones:");
            System.out.println("1: Listar ejemplares");
            System.out.println("2: Crear ejemplar");
            System.out.println("3: Actualizar ejemplar");
            System.out.println("4: Borrar ejemplar");
            System.out.println("0: Volver");
            System.out.print("~> ");
            switch (sc.next()) {
                case "0" -> loop = false;
                case "1" -> displayListEjemplaresMenu();
                case "2" -> displayInsertOrUpdateEjemplarMenu(false);
                case "3" -> displayInsertOrUpdateEjemplarMenu(true);
                case "4" -> displayDeleteEjemplarMenu();
                default -> showInvalidInputMsg();
            }
        }
    }

    private static void displayListEjemplaresMenu() {
        boolean loop = true;
        while (loop) {
            System.out.println("Introduzca una de las siguientes opciones:");
            System.out.println("1: Buscar un ejemplar");
            System.out.println("2: Listar todos los ejemplares");
            System.out.println("0: Volver");
            System.out.print("~> ");
            switch (sc.next()) {
                case "0" -> loop = false;
                case "1" -> {
                    System.out.print("ID ~> ");
                    Ejemplar ejemplar = ejemplarDAO.getById(Integer.parseInt(sc.next()));
                    Optional.ofNullable(ejemplar)
                            .ifPresentOrElse(System.out::println, () -> System.out.println("Ejemplar no encontrado."));
                }
                case "2" -> {
                    System.out.println("---- LISTA DE EJEMPLARES ----");
                    ejemplarDAO.getAll().forEach(System.out::println);
                    System.out.println("-----------------------------");
                }
                default -> showInvalidInputMsg();
            }
        }
    }

    private static void displayInsertOrUpdateEjemplarMenu(boolean update) {
        Ejemplar ejemplar = new Ejemplar();
        String[] data;

        if (update) {
            data = new String[3];
            System.out.print("ID ~> ");
            data[2] = sc.next();
        } else {
            data = new String[2];
        }
        System.out.print("ISBN ~> ");
        data[0] = sc.next();
        System.out.print("Estado ~> ");
        data[1] = sc.next();

        if (Stream.of(data).anyMatch(String::isBlank)) {
            showInvalidFieldMsg();
        } else {
            Libro libro = libroDAO.getById(data[0]);
            if (libro == null) {
                System.out.println("ISBN no encontrado.");
            } else {
                ejemplar.setIsbn(libro);
                ejemplar.setEstado(data[1]);
                if (update) {
                    ejemplar.setId(Integer.parseInt(data[2]));
                    ejemplarDAO.update(ejemplar);
                } else {
                    ejemplarDAO.add(ejemplar);
                }
                System.out.println("-> ¡HECHO! <-");
            }
        }
    }

    private static void displayDeleteEjemplarMenu() {
        System.out.println("Introduzca el ID del ejemplar que desea borrar.");
        System.out.print("ID ~> ");
        String id = sc.next();
        if (id.isBlank()) {
            showInvalidFieldMsg();
        } else {
            Ejemplar ejemplar = ejemplarDAO.getById(Integer.parseInt(id));
            Optional.ofNullable(ejemplar)
                    .ifPresentOrElse(e -> {
                        ejemplarDAO.delete(e);
                        System.out.println("-> ¡HECHO! <-");
                    }, () -> System.out.println("Ejemplar no encontrado."));
        }
    }

    // --------------------
    //    PRÉSTAMOS MENU
    // --------------------
    private static void displayPrestamosMenu() {
        boolean loop = true;
        while (loop) {
            System.out.println("Introduzca una de las siguientes opciones:");
            System.out.println("1: Listar préstamos");
            System.out.println("2: Crear préstamo");
            System.out.println("3: Actualizar préstamo");
            System.out.println("4: Borrar préstamo");
            System.out.println("0: Volver");
            System.out.print("~> ");
            switch (sc.next()) {
                case "0" -> loop = false;
                case "1" -> displayListPrestamosMenu();
                case "2" -> displayInsertOrUpdatePrestamoMenu(false);
                case "3" -> displayInsertOrUpdatePrestamoMenu(true);
                case "4" -> displayDeletePrestamoMenu();
                default -> showInvalidInputMsg();
            }
        }
    }

    private static void displayListPrestamosMenu() {
        boolean loop = true;
        while (loop) {
            System.out.println("Introduzca una de las siguientes opciones:");
            System.out.println("1: Buscar un préstamo");
            System.out.println("2: Listar todos los préstamos");
            System.out.println("0: Volver");
            System.out.print("~> ");
            switch (sc.next()) {
                case "0" -> loop = false;
                case "1" -> {
                    System.out.print("ID ~> ");
                    Prestamo prestamo = prestamoDAO.getById(Integer.parseInt(sc.next()));
                    Optional.ofNullable(prestamo)
                            .ifPresentOrElse(System.out::println, () -> System.out.println("Préstamo no encontrado."));
                }
                case "2" -> {
                    System.out.println("---- LISTA DE PRÉSTAMOS ----");
                    prestamoDAO.getAll().forEach(System.out::println);
                    System.out.println("----------------------------");
                }
                default -> showInvalidInputMsg();
            }
        }
    }

    private static void displayInsertOrUpdatePrestamoMenu(boolean update) {
        Prestamo prestamo = new Prestamo();
        String[] data;

        if (update) {
            data = new String[3];
            System.out.print("ID ~> ");
            data[2] = sc.next();
        } else {
            data = new String[2];
        }
        System.out.print("ID del usuario ~> ");
        data[0] = sc.next();
        System.out.print("ID del ejemplar ~> ");
        data[1] = sc.next();

        if (Stream.of(data).anyMatch(String::isBlank)) {
            showInvalidFieldMsg();
        } else {
            Usuario usuario = usuarioDAO.getById(Integer.parseInt(data[0]));
            Ejemplar ejemplar = ejemplarDAO.getById(Integer.parseInt(data[1]));
            if (usuario == null) {
                System.out.println("Usuario no encontrado.");
            } else if (ejemplar == null) {
                System.out.println("Ejemplar no encontrado.");
            } else {
                prestamo.setUsuario(usuario);
                prestamo.setEjemplar(ejemplar);
                LocalDate fechaInicio = LocalDate.now();
                LocalDate fechaDevolucion = LocalDate.now().plusDays(15);
                prestamo.setFechaInicio(fechaInicio);
                prestamo.setFechaDevolucion(fechaDevolucion);
                if (update) {
                    prestamo.setId(Integer.parseInt(data[2]));
                    prestamoDAO.update(prestamo);
                } else {
                    prestamoDAO.add(prestamo);
                }
                System.out.println("-> ¡HECHO! <-");
            }
        }
    }

    private static void displayDeletePrestamoMenu() {
        System.out.println("Introduzca el ID del préstamo que desea borrar.");
        System.out.print("ID ~> ");
        String id = sc.next();
        if (id.isBlank()) {
            showInvalidFieldMsg();
        } else {
            Prestamo prestamo = prestamoDAO.getById(Integer.parseInt(id));
            Optional.ofNullable(prestamo)
                    .ifPresentOrElse(p -> {
                        prestamoDAO.delete(p);
                        System.out.println("-> ¡HECHO! <-");
                    }, () -> System.out.println("Préstamo no encontrado."));
        }
    }

    // --------------------
    //    ERROR MESSAGES
    // --------------------
    private static void showInvalidInputMsg() {
        System.out.println("| ERROR\n| Entrada no válida.");
    }

    private static void showInvalidFieldMsg() {
        System.out.println("| ERROR\n| Alguno de los campos no son válidos.");
    }

    private static void showInvalidCredentialsMsg() {
        System.out.println("| ERROR\n| Las credenciales son incorrectas.");
    }
}
