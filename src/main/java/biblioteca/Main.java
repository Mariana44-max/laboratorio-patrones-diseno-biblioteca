
package biblioteca;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        // Crear repositorios
        RepositorioSalas repositorioSalas =
                new RepositorioSalasMemoria();

        RepositorioReservas repositorioReservas =
                new RepositorioReservasMemoria();

        // Crear observadores
        List<ObservadorReserva> observadores = List.of(
                new NotificadorEstudiante(),
                new RegistroEventos()
        );

        // Crear fachada
        ReservaFacade facade = new ReservaFacade(
                repositorioSalas,
                repositorioReservas,
                observadores
        );

        Estudiante estudiante = new Estudiante("1001", "Laura");

        System.out.println("================================");
        System.out.println(" PRUEBAS DEL PATRON STATE");
        System.out.println("================================");

        // PRUEBA 1: Confirmar una reserva pendiente
        System.out.println("\nPRUEBA 1: Confirmar reserva pendiente");

        Reserva reserva1 = facade.realizarReserva(
                estudiante,
                LocalDate.now().plusDays(7),
                LocalTime.of(14, 0),
                2,
                4,
                new SeleccionPorCapacidad()
        ).orElseThrow();

        System.out.println("Resultado: " + reserva1.getNombreEstado());

        // PRUEBA 2: Cancelar una reserva confirmada
        System.out.println("\nPRUEBA 2: Cancelar reserva confirmada");

        reserva1.cancelar();

        System.out.println("Resultado: " + reserva1.getNombreEstado());

        // PRUEBA 3: Intentar confirmar una reserva cancelada
        System.out.println("\nPRUEBA 3: Confirmar reserva cancelada");

        try {
            reserva1.confirmar();
        } catch (IllegalStateException e) {
            System.out.println("Error esperado: " + e.getMessage());
        }

        // Crear una segunda reserva
        Reserva reserva2 = facade.realizarReserva(
                estudiante,
                LocalDate.now().plusDays(8),
                LocalTime.of(10, 0),
                2,
                2,
                new SeleccionPorCapacidad()
        ).orElseThrow();

        // PRUEBA 4: Finalizar una reserva confirmada
        System.out.println("\nPRUEBA 4: Finalizar reserva confirmada");

        reserva2.finalizar();

        System.out.println("Resultado: " + reserva2.getNombreEstado());

        // PRUEBA 5: Intentar cancelar una reserva finalizada
        System.out.println("\nPRUEBA 5: Cancelar reserva finalizada");

        try {
            reserva2.cancelar();
        } catch (IllegalStateException e) {
            System.out.println("Error esperado: " + e.getMessage());
        }

        System.out.println("\n================================");
        System.out.println(" FIN DE LAS PRUEBAS");
        System.out.println("================================");
    }
}
