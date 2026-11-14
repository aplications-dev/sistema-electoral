package com.sebas.sistemaelectoral.controlador;

import com.sebas.sistemaelectoral.modelo.*;
import com.sebas.sistemaelectoral.negocio.CatalogoService;
import com.sebas.sistemaelectoral.negocio.VotoService;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Named("actaConcejalBean")
@ViewScoped
public class ActaConcejalBean implements Serializable {

    @EJB
    private CatalogoService catalogoService;

    @EJB
    private VotoService votoService;

    @Inject
    private LoginBean loginBean;

    private List<Parroquia> parroquias;
    private List<Recinto> recintos;
    private Integer idParroquia;
    private Integer idRecinto;
    private Integer nMesa;
    private List<VotoConcejal> listaVotos;
    private List<Object[]> actasRegistradas;
    private boolean modoEdicion = false;

    @PostConstruct
    public void init() {
        parroquias = catalogoService.listarParroquias();
        listaVotos = new ArrayList<>();
        cargarActasRegistradas();
    }

    public void cargarActasRegistradas() {
        actasRegistradas = votoService.listarActasConcejal(loginBean.getUsuarioLogueado());
    }

    public void alSeleccionarParroquia() {
        idRecinto = null;
        listaVotos = new ArrayList<>();

        if (idParroquia != null) {
            recintos = catalogoService.listarRecintosPorParroquia(idParroquia);
            Parroquia parroquia = catalogoService.buscarParroquiaPorId(idParroquia);

            if (parroquia != null && parroquia.getTipo() != null) {
                List<Concejal> concejalesFiltrados = catalogoService.listarConcejalesPorTipo(parroquia.getTipo());
                for (Concejal c : concejalesFiltrados) {
                    VotoConcejal vc = new VotoConcejal();
                    vc.setConcejal(c);
                    vc.setNVoto(0);
                    listaVotos.add(vc);
                }
            }
        } else {
            recintos = new ArrayList<>();
        }
    }

    public void cargarParaEditar(Integer idRecintoEdit, Integer nMesaEdit) {
        this.listaVotos = votoService.obtenerVotosConcejalPorMesa(idRecintoEdit, nMesaEdit);
        if (!this.listaVotos.isEmpty()) {
            Recinto r = this.listaVotos.get(0).getRecinto();
            this.idParroquia = r.getParroquia().getIdParroquia();
            this.recintos = catalogoService.listarRecintosPorParroquia(this.idParroquia);
            this.idRecinto = r.getIdRecinto();
            this.nMesa = nMesaEdit;
            this.modoEdicion = true;
        }
    }

    public void cancelarEdicion() {
        this.modoEdicion = false;
        this.idParroquia = null;
        this.idRecinto = null;
        this.nMesa = null;
        this.recintos = new ArrayList<>();
        this.listaVotos = new ArrayList<>();
    }

    public void guardarActa() {
        if (listaVotos == null || listaVotos.isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN, "Atención", "No hay candidatos cargados para esta parroquia."));
            return;
        }

        if (!modoEdicion && votoService.existeActaConcejal(idRecinto, nMesa)) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN, "Atención", "Ya existe un acta de concejales para esa mesa en este recinto."));
            return;
        }

        if (!modoEdicion) {
            Recinto recinto = catalogoService.buscarRecintoPorId(idRecinto);
            Usuario usuario = loginBean.getUsuarioLogueado();
            for (VotoConcejal v : listaVotos) {
                v.setRecinto(recinto);
                v.setNMesa(nMesa);
                v.setUsuario(usuario);
            }
        }

        votoService.guardarOActualizarActaConcejal(listaVotos);
        String msg = modoEdicion ? "Acta de Concejales actualizada correctamente." : "Acta de Concejales registrada correctamente.";
        cancelarEdicion();
        cargarActasRegistradas();

        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", msg));
    }

    public void eliminarActa(Object idRecintoObj, Object nMesaObj) {
        if (!loginBean.isAdministrador()) {
            return;
        }
        Integer idRecintoDel = Integer.valueOf(idRecintoObj.toString());
        Integer nMesaDel = Integer.valueOf(nMesaObj.toString());

        votoService.eliminarActaConcejal(idRecintoDel, nMesaDel);
        if (modoEdicion && idRecintoDel.equals(this.idRecinto) && nMesaDel.equals(this.nMesa)) {
            cancelarEdicion();
        }
        cargarActasRegistradas();
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Acta de Concejales eliminada correctamente."));
    }

    public List<Parroquia> getParroquias() {
        return parroquias;
    }

    public List<Recinto> getRecintos() {
        return recintos;
    }

    public Integer getIdParroquia() {
        return idParroquia;
    }

    public void setIdParroquia(Integer idParroquia) {
        this.idParroquia = idParroquia;
    }

    public Integer getIdRecinto() {
        return idRecinto;
    }

    public void setIdRecinto(Integer idRecinto) {
        this.idRecinto = idRecinto;
    }

    public Integer getNMesa() {
        return nMesa;
    }

    public void setNMesa(Integer nMesa) {
        this.nMesa = nMesa;
    }

    public List<VotoConcejal> getListaVotos() {
        return listaVotos;
    }

    public List<Object[]> getActasRegistradas() {
        return actasRegistradas;
    }

    public boolean isModoEdicion() {
        return modoEdicion;
    }
}
