package Grupo11.Seminario.Entities;

import Grupo11.Seminario.Entities.Enum.Categoria;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.PrimaryKeyJoinColumn;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

// Se determina el nombre de la clase en la BD
@Entity(name = "Jugador")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
// Establece que la CP de la entidad, la cual se conectara a la entidad extendida
@PrimaryKeyJoinColumn(name = "usuario_id")
public class Jugador extends Usuario{
    
    @Column(nullable = false, name = "socio")
    private Boolean socio=false;

    @Column(nullable = false, name = "profesor")
    private Boolean profesor=false;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "categoria")
    private Categoria categoria;
    
    
}
