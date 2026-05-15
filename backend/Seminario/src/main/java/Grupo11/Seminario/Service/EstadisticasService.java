package Grupo11.Seminario.Service;

import java.time.LocalDate;
import java.time.Month;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import Grupo11.Seminario.DTO.EstadisticasDTO;
import Grupo11.Seminario.Entities.Jugador;
import Grupo11.Seminario.Entities.Reserva;
import Grupo11.Seminario.Entities.Usuario;

@Service
public class EstadisticasService {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private PerfilService perfilService;

    @Autowired
    private ReservaService reservaService;

    public EstadisticasDTO obtenerEstadisticas(String email) {
        Optional<Usuario> usuarioOptional = usuarioService.buscar_usuario(email);

        if (usuarioOptional.isEmpty()) {
            return new EstadisticasDTO("desconocido", Map.of("error", "Usuario no encontrado"));
        }

        Usuario usuario = usuarioOptional.get();
        Jugador jugador = perfilService.buscar_jugador(usuario.getId());

        if (jugador != null) {
            return obtenerEstadisticasJugador(jugador);
        }

        return new EstadisticasDTO("sin_rol", Map.of("mensaje", "No hay estadísticas disponibles"));
    }

    private EstadisticasDTO obtenerEstadisticasJugador(Jugador jugador) {
        List<Reserva> reservas = reservaService.buscar_reservas(jugador.getId());

        Integer reservasRealizadas = reservas.size();

        Integer reservasCanceladas = (int) reservas.stream()
            .filter(reserva -> reserva.getEstado().toString().equalsIgnoreCase("Cancelada"))
            .count();

        Integer reservasActivas = (int) reservas.stream()
            .filter(reserva ->
                !reserva.getEstado().toString().equalsIgnoreCase("Cancelada") &&
                !reserva.getTurno().getFecha().isBefore(LocalDate.now())
            )
            .count();

        String canchaFavorita = calcularCanchaFavorita(reservas);

        Map<String, Long> reservasPorCancha = calcularReservasPorCancha(reservas);
        Map<String, Long> reservasPorMes = calcularReservasPorMes(reservas);
        Map<String, Long> reservasPorHorario = calcularReservasPorHorario(reservas);

        Map<String, Object> metricas = new HashMap<>();
        metricas.put("reservasRealizadas", reservasRealizadas);
        metricas.put("reservasActivas", reservasActivas);
        metricas.put("reservasCanceladas", reservasCanceladas);
        metricas.put("canchaFavorita", canchaFavorita);
        metricas.put("reservasPorCancha", reservasPorCancha);
        metricas.put("reservasPorMes", reservasPorMes);
        metricas.put("reservasPorHorario", reservasPorHorario);
        metricas.put("ingresosGenerados", 0);

        return new EstadisticasDTO("jugador", metricas);
    }

    private String calcularCanchaFavorita(List<Reserva> reservas) {
        if (reservas.isEmpty()) return "-";

        Map<Integer, Long> cantidadPorCancha = reservas.stream()
            .collect(Collectors.groupingBy(
                reserva -> reserva.getTurno().getCancha().getNumero(),
                Collectors.counting()
            ));

        Integer numeroCanchaFavorita = cantidadPorCancha.entrySet()
            .stream()
            .max(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey)
            .orElse(null);

        return numeroCanchaFavorita == null ? "-" : "Cancha " + numeroCanchaFavorita;
    }

    private Map<String, Long> calcularReservasPorCancha(List<Reserva> reservas) {
        return reservas.stream()
            .collect(Collectors.groupingBy(
                reserva -> "Cancha " + reserva.getTurno().getCancha().getNumero(),
                LinkedHashMap::new,
                Collectors.counting()
            ));
    }

    private Map<String, Long> calcularReservasPorHorario(List<Reserva> reservas) {
        return reservas.stream()
            .collect(Collectors.groupingBy(
                reserva -> reserva.getTurno().getHorarioInicio().toString(),
                LinkedHashMap::new,
                Collectors.counting()
            ));
    }

    private Map<String, Long> calcularReservasPorMes(List<Reserva> reservas) {
        Map<String, Long> meses = new LinkedHashMap<>();

        for (Month mes : Month.values()) {
            meses.put(mes.name(), 0L);
        }

        reservas.forEach(reserva -> {
            String mes = reserva.getTurno().getFecha().getMonth().name();
            meses.put(mes, meses.get(mes) + 1);
        });

        return meses;
    }
}