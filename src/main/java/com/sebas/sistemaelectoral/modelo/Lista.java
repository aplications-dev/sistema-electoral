package com.sebas.sistemaelectoral.modelo;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "lista")
public class Lista implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_lista")
    private Integer idLista;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    public Lista() {}

    public Integer getIdLista() { return idLista; }
    public void setIdLista(Integer idLista) { this.idLista = idLista; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
}
