package com.sebas.sistemaelectoral.modelo;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "alcalde")
public class Alcalde implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_alcalde")
    private Integer idAlcalde;

    @Column(name = "nombre", nullable = false, length = 150)
    private String nombre;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_lista", nullable = false)
    private Lista lista;

    public Alcalde() {}

    public Integer getIdAlcalde() { return idAlcalde; }
    public void setIdAlcalde(Integer idAlcalde) { this.idAlcalde = idAlcalde; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public Lista getLista() { return lista; }
    public void setLista(Lista lista) { this.lista = lista; }
}
