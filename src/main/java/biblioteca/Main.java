
package biblioteca;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        // Crear repositorios
        RepositorioSalas salas = new RepositorioSalasMemoria();
        RepositorioReservas reservas = new RepositorioReservasMemoria();

        // Crear observadores
        List<ObservadorReserva> observadores = List.of(
                new NotificadorEstudiante(),
                new RegistroEventos()
        );

        // Crear fachada
        ReservaFacade fachada =
                new ReservaFacade(salas, reservas, observadores);

        Estudiante laura = new Estudiante("E001", "Laura");

        // ESCENARIO PRINCIPAL
        System.out.println("\n=== ESCENARIO PRINCIPAL ===");

        Reserva reserva = fachada.realizarReserva(
                laura,
                LocalDate.now().plusDays(7),
                LocalTime.of(14, 0),
                2,
                4,
                new SeleccionPorCapacidad()
        ).orElseThrow();

        System.out.println(reserva);

        System.out.println("\nLaura cancela la reserva:");
        reserva.cancelar();
        System.out.println(reserva);

        System.out.println("\nIntento inválido: confirmar una reserva cancelada.");

        try {
            reserva.confirmar();
        } catch (IllegalStateException ex) {
            System.out.println("Operación rechazada: " + ex.getMessage());
        }

        // PRUEBA DE SALA PREFERIDA
        System.out.println("\n=== PRUEBA DE SALA PREFERIDA ===");

        fachada.realizarReserva(
                new Estudiante("E002", "Andrés"),
                LocalDate.now().plusDays(8),
                LocalTime.of(10, 0),
                2,
                4,
                new SeleccionSalaPreferida("C")
        ).ifPresent(System.out::println);

        // PRUEBA SIN SALA ADECUADA
        System.out.println("\n=== PRUEBA SIN SALA ADECUADA ===");

        fachada.realizarReserva(
                new Estudiante("E003", "Camila"),
                LocalDate.now().plusDays(9),
                LocalTime.of(9, 0),
                2,
                10,
                new SeleccionPorCapacidad()
        );

        // PRUEBA DE FINALIZACIÓN
        System.out.println("\n=== PRUEBA DE FINALIZACIÓN ===");

        Reserva otra = fachada.realizarReserva(
                new Estudiante("E004", "Samuel"),
                LocalDate.now().plusDays(10),
                LocalTime.of(11, 0),
                1,
                2,
                new SeleccionPorCapacidad()
        ).orElseThrow();

        otra.finalizar();
        System.out.println(otra);
    }
}
