package com.sebas.sistemaelectoral.modelo;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "voto_concejal")
public class VotoConcejal implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_voto_concejal")
    private Integer idVotoConcejal;

    @Column(name = "n_mesa", nullable = false)
    private Integer nMesa;

    @Column(name = "n_voto", nullable = false)
    private Integer nVoto;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_concejal", nullable = false)
    private Concejal concejal;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_recinto", nullable = false)
    private Recinto recinto;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    public VotoConcejal() {}

    public Integer getIdVotoConcejal() { return idVotoConcejal; }
    public void setIdVotoConcejal(Integer idVotoConcejal) { this.idVotoConcejal = idVotoConcejal; }

    public Integer getNMesa() { return nMesa; }
    public void setNMesa(Integer nMesa) { this.nMesa = nMesa; }

    public Integer getNVoto() { return nVoto; }
    public void setNVoto(Integer nVoto) { this.nVoto = nVoto; }

    public Concejal getConcejal() { return concejal; }
    public void setConcejal(Concejal concejal) { this.concejal = concejal; }

    public Recinto getRecinto() { return recinto; }
    public void setRecinto(Recinto recinto) { this.recinto = recinto; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
}
