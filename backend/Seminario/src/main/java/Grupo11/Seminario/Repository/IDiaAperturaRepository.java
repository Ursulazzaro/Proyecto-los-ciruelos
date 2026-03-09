package Grupo11.Seminario.Repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import Grupo11.Seminario.Entities.DiaApertura;

@Repository
public interface IDiaAperturaRepository extends CrudRepository<DiaApertura,Integer>{
    
    public DiaApertura findByDia(String dia);
}
