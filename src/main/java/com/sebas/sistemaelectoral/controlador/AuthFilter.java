package com.sebas.sistemaelectoral.controlador;

import com.sebas.sistemaelectoral.modelo.Usuario;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter("*.xhtml")
public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        HttpSession session = req.getSession(false);

        String uri = req.getRequestURI();
        boolean esLogin = uri.endsWith("/login.xhtml");
        boolean esRecurso = uri.contains("jakarta.faces.resource");

        Usuario usuario = (session != null) ? (Usuario) session.getAttribute("usuario") : null;

        if (usuario != null || esLogin || esRecurso) {
            if (usuario != null && uri.contains("/admin/") && usuario.getRol().getIdRol() != 1) {
                res.sendRedirect(req.getContextPath() + "/inicio.xhtml");
                return;
            }
            chain.doFilter(request, response);
        } else {
            res.sendRedirect(req.getContextPath() + "/login.xhtml");
        }
    }
}
