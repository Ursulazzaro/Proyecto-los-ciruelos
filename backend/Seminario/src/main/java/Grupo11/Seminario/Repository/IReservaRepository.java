package Grupo11.Seminario.Repository;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import Grupo11.Seminario.Entities.Reserva;

@Repository
public interface IReservaRepository extends CrudRepository<Reserva, Integer> {
    
    // Método para buscar reservas por el ID del jugador
    List<Reserva> findByJugadorId(Integer jugadorId);
}

