package com.sebas.sistemaelectoral.modelo;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "concejal")
public class Concejal implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_concejal")
    private Integer idConcejal;

    @Column(name = "nombre", nullable = false, length = 150)
    private String nombre;

    @Column(name = "tipo", length = 50)
    private String tipo;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_lista", nullable = false)
    private Lista lista;

    public Concejal() {}

    public Integer getIdConcejal() { return idConcejal; }
    public void setIdConcejal(Integer idConcejal) { this.idConcejal = idConcejal; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public Lista getLista() { return lista; }
    public void setLista(Lista lista) { this.lista = lista; }
}
