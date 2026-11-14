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

@Named("actaAlcaldeBean")
@ViewScoped
public class ActaAlcaldeBean implements Serializable {

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
    private List<VotoAlcalde> listaVotos;
    private List<Object[]> actasRegistradas;
    private boolean modoEdicion = false;

    @PostConstruct
    public void init() {
        parroquias = catalogoService.listarParroquias();
        prepararFormularioVotos();
        cargarActasRegistradas();
    }

    public void cargarActasRegistradas() {
        actasRegistradas = votoService.listarActasAlcalde(loginBean.getUsuarioLogueado());
    }

    public void cargarRecintos() {
        if (idParroquia != null) {
            recintos = catalogoService.listarRecintosPorParroquia(idParroquia);
        } else {
            recintos = new ArrayList<>();
        }
        idRecinto = null;
    }

    public void prepararFormularioVotos() {
        listaVotos = new ArrayList<>();
        for (Alcalde a : catalogoService.listarAlcaldes()) {
            VotoAlcalde va = new VotoAlcalde();
            va.setAlcalde(a);
            va.setNVoto(0);
            listaVotos.add(va);
        }
    }

    public void cargarParaEditar(Integer idRecintoEdit, Integer nMesaEdit) {
        this.listaVotos = votoService.obtenerVotosAlcaldePorMesa(idRecintoEdit, nMesaEdit);
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
        prepararFormularioVotos();
    }

    public void guardarActa() {
        if (!modoEdicion && votoService.existeActaAlcalde(idRecinto, nMesa)) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN, "Atención", "Ya existe un acta registrada para esa mesa en este recinto."));
            return;
        }

        if (!modoEdicion) {
            Recinto recinto = catalogoService.buscarRecintoPorId(idRecinto);
            Usuario usuario = loginBean.getUsuarioLogueado();
            for (VotoAlcalde v : listaVotos) {
                v.setRecinto(recinto);
                v.setNMesa(nMesa);
                v.setUsuario(usuario);
            }
        }

        votoService.guardarOActualizarActaAlcalde(listaVotos);
        String msg = modoEdicion ? "Acta de Alcaldes actualizada correctamente." : "Acta de Alcaldes registrada correctamente.";
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

        votoService.eliminarActaAlcalde(idRecintoDel, nMesaDel);
        if (modoEdicion && idRecintoDel.equals(this.idRecinto) && nMesaDel.equals(this.nMesa)) {
            cancelarEdicion();
        }
        cargarActasRegistradas();
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Acta de Alcaldes eliminada correctamente."));
    }


    public List<Parroquia> getParroquias() { return parroquias; }
    public List<Recinto> getRecintos() { return recintos; }
    public Integer getIdParroquia() { return idParroquia; }
    public void setIdParroquia(Integer idParroquia) { this.idParroquia = idParroquia; }
    public Integer getIdRecinto() { return idRecinto; }
    public void setIdRecinto(Integer idRecinto) { this.idRecinto = idRecinto; }
    public Integer getNMesa() { return nMesa; }
    public void setNMesa(Integer nMesa) { this.nMesa = nMesa; }
    public List<VotoAlcalde> getListaVotos() { return listaVotos; }
    public List<Object[]> getActasRegistradas() { return actasRegistradas; }
    public boolean isModoEdicion() { return modoEdicion; }
}
