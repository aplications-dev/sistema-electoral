package com.sebas.sistemaelectoral.controlador;

import com.sebas.sistemaelectoral.modelo.Rol;
import com.sebas.sistemaelectoral.modelo.Usuario;
import com.sebas.sistemaelectoral.negocio.UsuarioService;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named("usuarioBean")
@ViewScoped
public class UsuarioBean implements Serializable {

    @EJB
    private UsuarioService usuarioService;

    private List<Usuario> listaUsuarios;
    private List<Rol> listaRoles;
    private Usuario usuarioSeleccionado;
    private Integer idRolSeleccionado;

    @PostConstruct
    public void init() {
        cargarDatos();
        nuevo();
    }

    public void cargarDatos() {
        listaUsuarios = usuarioService.listarTodos();
        listaRoles = usuarioService.listarRoles();
    }

    public void nuevo() {
        usuarioSeleccionado = new Usuario();
        idRolSeleccionado = null;
    }

    public void editar(Usuario u) {
        this.usuarioSeleccionado = u;
        this.idRolSeleccionado = u.getRol().getIdRol();
    }

    public void guardar() {
        try {
            Rol rol = usuarioService.buscarRolPorId(idRolSeleccionado);
            usuarioSeleccionado.setRol(rol);
            usuarioService.guardar(usuarioSeleccionado);
            cargarDatos();
            nuevo();
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Usuario guardado correctamente."));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se pudo guardar el usuario (verifique si la cédula ya existe)."));
        }
    }

    public void eliminar(Integer idUsuario) {
        usuarioService.eliminar(idUsuario);
        cargarDatos();
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Usuario eliminado."));
    }

    public List<Usuario> getListaUsuarios() { return listaUsuarios; }
    public List<Rol> getListaRoles() { return listaRoles; }
    public Usuario getUsuarioSeleccionado() { return usuarioSeleccionado; }
    public void setUsuarioSeleccionado(Usuario usuarioSeleccionado) { this.usuarioSeleccionado = usuarioSeleccionado; }
    public Integer getIdRolSeleccionado() { return idRolSeleccionado; }
    public void setIdRolSeleccionado(Integer idRolSeleccionado) { this.idRolSeleccionado = idRolSeleccionado; }
}

