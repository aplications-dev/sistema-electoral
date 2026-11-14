package com.sebas.sistemaelectoral.modelo;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "voto_alcalde")
public class VotoAlcalde implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_voto_alcalde")
    private Integer idVotoAlcalde;

    @Column(name = "n_mesa", nullable = false)
    private Integer nMesa;

    @Column(name = "n_voto", nullable = false)
    private Integer nVoto;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_alcalde", nullable = false)
    private Alcalde alcalde;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_recinto", nullable = false)
    private Recinto recinto;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    public VotoAlcalde() {}

    public Integer getIdVotoAlcalde() { return idVotoAlcalde; }
    public void setIdVotoAlcalde(Integer idVotoAlcalde) { this.idVotoAlcalde = idVotoAlcalde; }

    public Integer getNMesa() { return nMesa; }
    public void setNMesa(Integer nMesa) { this.nMesa = nMesa; }

    public Integer getNVoto() { return nVoto; }
    public void setNVoto(Integer nVoto) { this.nVoto = nVoto; }

    public Alcalde getAlcalde() { return alcalde; }
    public void setAlcalde(Alcalde alcalde) { this.alcalde = alcalde; }

    public Recinto getRecinto() { return recinto; }
    public void setRecinto(Recinto recinto) { this.recinto = recinto; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
}
