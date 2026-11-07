
package biblioteca;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        // Crear los repositorios
        RepositorioSalas repositorioSalas =
                new RepositorioSalasMemoria();

        RepositorioReservas repositorioReservas =
                new RepositorioReservasMemoria();

        // Crear los observadores
        List<ObservadorReserva> observadores = List.of(
                new NotificadorEstudiante(),
                new RegistroEventos()
        );

        // Crear la fachada
        ReservaFacade facade = new ReservaFacade(
                repositorioSalas,
                repositorioReservas,
                observadores
        );

        // Datos del estudiante
        Estudiante estudiante = new Estudiante("1001", "Laura");

        // Realizar la reserva
        facade.realizarReserva(
                estudiante,
                LocalDate.now().plusDays(7),
                LocalTime.of(14, 0),
                2,
                4,
                new SeleccionPorCapacidad()
        );
    }
}
