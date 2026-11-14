package com.sebas.sistemaelectoral.negocio;

import com.sebas.sistemaelectoral.modelo.Rol;
import com.sebas.sistemaelectoral.modelo.Usuario;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class UsuarioService {

    @PersistenceContext(unitName = "ElectoralPU")
    private EntityManager em;

    public Usuario autenticar(String cedula, String password) {
        try {
            return em.createQuery(
                    "SELECT u FROM Usuario u WHERE u.cedula = :cedula AND u.password = :password", Usuario.class)
                    .setParameter("cedula", cedula)
                    .setParameter("password", password)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public void guardar(Usuario usuario) {
        if (usuario.getIdUsuario() == null) {
            em.persist(usuario);
        } else {
            em.merge(usuario);
        }
    }

    public void eliminar(Integer idUsuario) {
        Usuario u = em.find(Usuario.class, idUsuario);
        if (u != null) {
            em.remove(u);
        }
    }

    public List<Usuario> listarTodos() {
        return em.createQuery("SELECT u FROM Usuario u ORDER BY u.apellido, u.nombre", Usuario.class)
                .getResultList();
    }

    public List<Rol> listarRoles() {
        return em.createQuery("SELECT r FROM Rol r ORDER BY r.idRol", Rol.class)
                .getResultList();
    }

    public Rol buscarRolPorId(Integer idRol) {
        return em.find(Rol.class, idRol);
    }
}
