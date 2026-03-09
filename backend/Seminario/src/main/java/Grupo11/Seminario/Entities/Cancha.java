package Grupo11.Seminario.Entities;

import Grupo11.Seminario.Entities.Enum.Tipo;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Cancha {
    
    // Se define el ID como autoincremental
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, name = "numero")
    private Integer numero;

    @Column(nullable = false, name = "descripcion", length = 300)
    private String descripccion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "tipo")
    private Tipo tipo;
}
