package com.sebas.sistemaelectoral.modelo;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "voto_junta")
public class VotoJunta implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_voto_junta")
    private Integer idVotoJunta;

    @Column(name = "n_mesa", nullable = false)
    private Integer nMesa;

    @Column(name = "n_voto", nullable = false)
    private Integer nVoto;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_junta", nullable = false)
    private Junta junta;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_recinto", nullable = false)
    private Recinto recinto;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    public VotoJunta() {}

    public Integer getIdVotoJunta() { return idVotoJunta; }
    public void setIdVotoJunta(Integer idVotoJunta) { this.idVotoJunta = idVotoJunta; }

    public Integer getNMesa() { return nMesa; }
    public void setNMesa(Integer nMesa) { this.nMesa = nMesa; }

    public Integer getNVoto() { return nVoto; }
    public void setNVoto(Integer nVoto) { this.nVoto = nVoto; }

    public Junta getJunta() { return junta; }
    public void setJunta(Junta junta) { this.junta = junta; }

    public Recinto getRecinto() { return recinto; }
    public void setRecinto(Recinto recinto) { this.recinto = recinto; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
}

