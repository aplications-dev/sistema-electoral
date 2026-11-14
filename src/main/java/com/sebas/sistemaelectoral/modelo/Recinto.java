package com.sebas.sistemaelectoral.modelo;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "recinto")
public class Recinto implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_recinto")
    private Integer idRecinto;

    @Column(name = "nombre", nullable = false, length = 150)
    private String nombre;

    @Column(name = "jrv_fem")
    private Integer jrvFem;

    @Column(name = "jrv_masc")
    private Integer jrvMasc;

    @Column(name = "electores")
    private Integer electores;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_parroquia", nullable = false)
    private Parroquia parroquia;

    public Recinto() {}

    public Integer getIdRecinto() { return idRecinto; }
    public void setIdRecinto(Integer idRecinto) { this.idRecinto = idRecinto; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public Integer getJrvFem() { return jrvFem; }
    public void setJrvFem(Integer jrvFem) { this.jrvFem = jrvFem; }

    public Integer getJrvMasc() { return jrvMasc; }
    public void setJrvMasc(Integer jrvMasc) { this.jrvMasc = jrvMasc; }

    public Integer getElectores() { return electores; }
    public void setElectores(Integer electores) { this.electores = electores; }

    public Parroquia getParroquia() { return parroquia; }
    public void setParroquia(Parroquia parroquia) { this.parroquia = parroquia; }
}
