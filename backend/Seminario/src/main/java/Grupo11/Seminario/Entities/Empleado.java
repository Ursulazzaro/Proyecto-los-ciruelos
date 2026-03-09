package Grupo11.Seminario.Entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

// Se determina el nombre de la clase en la BD
@Entity(name = "Empleado")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
// Establece que la CP de la entidad, la cual se conectara a la entidad extendida
@PrimaryKeyJoinColumn(name = "usuario_id")
public class Empleado extends Usuario{
    
    @Column(nullable = false, name = "duenio")
    private Boolean duenio;
}
