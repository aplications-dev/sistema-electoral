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

@Named("actaJuntaBean")
@ViewScoped
public class ActaJuntaBean implements Serializable {

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
    private List<VotoJunta> listaVotos;
    private List<Object[]> actasRegistradas;
    private boolean modoEdicion = false;

    @PostConstruct
    public void init() {
        parroquias = catalogoService.listarParroquiasRurales();
        listaVotos = new ArrayList<>();
        cargarActasRegistradas();
    }

    public void cargarActasRegistradas() {
        // En JTA/JPA las consultas de agregación devuelven tipos nativos en el Object[]
        actasRegistradas = votoService.listarActasJunta(loginBean.getUsuarioLogueado());
    }

    public void editarActa(Object idRecintoObj, Object nMesaObj) {
        // Conversión segura de Object a Integer cuidando el origen de datos de la consulta
        Integer idRecintoEditar = Integer.valueOf(idRecintoObj.toString());
        Integer nMesaEditar = Integer.valueOf(nMesaObj.toString());

        Recinto r = catalogoService.buscarRecintoPorId(idRecintoEditar);
        this.idParroquia = r.getParroquia().getIdParroquia();
        this.recintos = catalogoService.listarRecintosPorParroquia(idParroquia);
        this.idRecinto = idRecintoEditar;
        this.nMesa = nMesaEditar;
        this.listaVotos = votoService.obtenerVotosJuntaPorMesa(idRecintoEditar, nMesaEditar);
        this.modoEdicion = true;
    }

    public void alSeleccionarParroquia() {
        idRecinto = null;
        listaVotos = new ArrayList<>();

        if (idParroquia != null) {
            recintos = catalogoService.listarRecintosPorParroquia(idParroquia);
            List<Junta> juntasFiltradas = catalogoService.listarJuntasPorParroquia(idParroquia);

            for (Junta j : juntasFiltradas) {
                VotoJunta vj = new VotoJunta();
                vj.setJunta(j);
                vj.setNVoto(0);
                listaVotos.add(vj);
            }
        } else {
            recintos = new ArrayList<>();
        }
    }

    public void cargarParaEditar(Integer idRecintoEdit, Integer nMesaEdit) {
        this.listaVotos = votoService.obtenerVotosJuntaPorMesa(idRecintoEdit, nMesaEdit);
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
                    new FacesMessage(FacesMessage.SEVERITY_WARN, "Atención", "Esta parroquia no tiene candidatos de Junta Parroquial registrados."));
            return;
        }

        if (!modoEdicion && votoService.existeActaJunta(idRecinto, nMesa)) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN, "Atención", "Ya existe un acta de junta para esa mesa en este recinto."));
            return;
        }

        if (!modoEdicion) {
            Recinto recinto = catalogoService.buscarRecintoPorId(idRecinto);
            Usuario usuario = loginBean.getUsuarioLogueado();
            for (VotoJunta v : listaVotos) {
                v.setRecinto(recinto);
                v.setNMesa(nMesa);
                v.setUsuario(usuario);
            }
        }

        votoService.guardarOActualizarActaJunta(listaVotos);
        String msg = modoEdicion ? "Acta de Junta actualizada correctamente." : "Acta de Junta registrada correctamente.";
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

        votoService.eliminarActaJunta(idRecintoDel, nMesaDel);
        if (modoEdicion && idRecintoDel.equals(this.idRecinto) && nMesaDel.equals(this.nMesa)) {
            cancelarEdicion();
        }
        cargarActasRegistradas();
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Acta de Juntas eliminada correctamente."));
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

    public List<VotoJunta> getListaVotos() {
        return listaVotos;
    }

    public List<Object[]> getActasRegistradas() {
        return actasRegistradas;
    }

    public boolean isModoEdicion() {
        return modoEdicion;
    }
}
