package com.sebas.sistemaelectoral.modelo;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "junta")
public class Junta implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_junta")
    private Integer idJunta;

    @Column(name = "nombre", nullable = false, length = 150)
    private String nombre;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_lista", nullable = false)
    private Lista lista;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_parroquia", nullable = false)
    private Parroquia parroquia;

    public Junta() {}

    public Integer getIdJunta() { return idJunta; }
    public void setIdJunta(Integer idJunta) { this.idJunta = idJunta; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public Lista getLista() { return lista; }
    public void setLista(Lista lista) { this.lista = lista; }

    public Parroquia getParroquia() { return parroquia; }
    public void setParroquia(Parroquia parroquia) { this.parroquia = parroquia; }
}
