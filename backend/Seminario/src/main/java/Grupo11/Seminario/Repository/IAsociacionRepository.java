package Grupo11.Seminario.Repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import Grupo11.Seminario.Entities.Asociacion;

@Repository
public interface IAsociacionRepository extends CrudRepository<Asociacion,Integer>{
    Asociacion findByJugadorId(Integer idJugador);
}
