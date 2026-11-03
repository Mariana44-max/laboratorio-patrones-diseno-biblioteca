
package biblioteca;

import java.time.LocalDate;
import java.time.LocalTime;

public class Main {
    public static void main(String[] args) {

        RepositorioSalas repositorioSalas =
                new RepositorioSalasMemoria();

        RepositorioReservas repositorioReservas =
                new RepositorioReservasMemoria();

        ReservaFacade facade =
                new ReservaFacade(repositorioSalas, repositorioReservas);

        Estudiante estudiante = new Estudiante("1001", "Laura");

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
