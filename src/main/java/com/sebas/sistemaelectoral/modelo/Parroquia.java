package com.sebas.sistemaelectoral.modelo;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "parroquia")
public class Parroquia implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_parroquia")
    private Integer idParroquia;

    @Column(name = "tipo", length = 50)
    private String tipo;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "recintos")
    private Integer recintos;

    @Column(name = "jrv_fem")
    private Integer jrvFem;

    @Column(name = "jrv_masc")
    private Integer jrvMasc;

    @Column(name = "electores")
    private Integer electores;

    public Parroquia() {}

    public Integer getIdParroquia() { return idParroquia; }
    public void setIdParroquia(Integer idParroquia) { this.idParroquia = idParroquia; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public Integer getRecintos() { return recintos; }
    public void setRecintos(Integer recintos) { this.recintos = recintos; }

    public Integer getJrvFem() { return jrvFem; }
    public void setJrvFem(Integer jrvFem) { this.jrvFem = jrvFem; }

    public Integer getJrvMasc() { return jrvMasc; }
    public void setJrvMasc(Integer jrvMasc) { this.jrvMasc = jrvMasc; }

    public Integer getElectores() { return electores; }
    public void setElectores(Integer electores) { this.electores = electores; }
}
