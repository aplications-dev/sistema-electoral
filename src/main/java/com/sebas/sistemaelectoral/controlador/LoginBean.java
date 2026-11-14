package com.sebas.sistemaelectoral.controlador;

import com.sebas.sistemaelectoral.modelo.Usuario;
import com.sebas.sistemaelectoral.negocio.UsuarioService;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import java.io.Serializable;

@Named("loginBean")
@SessionScoped
public class LoginBean implements Serializable {

    @EJB
    private UsuarioService usuarioService;

    private String cedula;
    private String password;
    private Usuario usuarioLogueado;

    public String iniciarSesion() {
        usuarioLogueado = usuarioService.autenticar(cedula, password);
        if (usuarioLogueado != null) {
            FacesContext.getCurrentInstance().getExternalContext()
                    .getSessionMap().put("usuario", usuarioLogueado);
            return "/inicio.xhtml?faces-redirect=true";
        } else {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Cédula o contraseña incorrectas."));
            return null;
        }
    }

    public String cerrarSesion() {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        usuarioLogueado = null;
        return "/login.xhtml?faces-redirect=true";
    }

    public boolean isAdministrador() {
        return usuarioLogueado != null && usuarioLogueado.getRol().getIdRol() == 1;
    }

    public boolean isDigitador() {
        return usuarioLogueado != null && usuarioLogueado.getRol().getIdRol() == 2;
    }

    public String getCedula() { return cedula; }
    public void setCedula(String cedula) { this.cedula = cedula; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public Usuario getUsuarioLogueado() { return usuarioLogueado; }
}
