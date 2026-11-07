
package biblioteca;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public class ReservaFacade {

    private final RepositorioSalas repositorioSalas;
    private final RepositorioReservas repositorioReservas;
    private final List<ObservadorReserva> observadores;

    public ReservaFacade(RepositorioSalas repositorioSalas,
                         RepositorioReservas repositorioReservas,
                         List<ObservadorReserva> observadores) {

        this.repositorioSalas = repositorioSalas;
        this.repositorioReservas = repositorioReservas;
        this.observadores = List.copyOf(observadores);
    }

    public Optional<Reserva> realizarReserva(
            Estudiante estudiante,
            LocalDate fecha,
            LocalTime hora,
            int duracion,
            int personas,
            EstrategiaSeleccionSala estrategia) {

        List<Sala> disponibles = repositorioSalas.obtenerDisponibles();

        Optional<Sala> sala = estrategia.seleccionar(disponibles, personas);

        if (sala.isEmpty()) {
            System.out.println(
                    "No hay una sala disponible que cumpla la solicitud.");
            return Optional.empty();
        }

        Reserva reserva = new ReservaBuilder()
                .estudiante(estudiante)
                .fecha(fecha)
                .horario(hora)
                .duracionHoras(duracion)
                .cantidadPersonas(personas)
                .sala(sala.get())
                .build();

        // Suscribir los observadores a la reserva
        observadores.forEach(reserva::agregarObservador);

        // Guardar y confirmar la reserva
        repositorioReservas.guardar(reserva);
        reserva.confirmar();

        return Optional.of(reserva);
    }
}
