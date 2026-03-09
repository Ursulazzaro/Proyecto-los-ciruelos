package Grupo11.Seminario.Repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import Grupo11.Seminario.Entities.Cancha;

@Repository
public interface ICanchaRepository extends CrudRepository<Cancha, Integer> {
    
    public Optional<Cancha> findByNumero(Integer numero);
}
