package com.sebas.sistemaelectoral.negocio;

import com.sebas.sistemaelectoral.modelo.Usuario;
import com.sebas.sistemaelectoral.modelo.VotoAlcalde;
import com.sebas.sistemaelectoral.modelo.VotoConcejal;
import com.sebas.sistemaelectoral.modelo.VotoJunta;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class VotoService {

    @PersistenceContext(unitName = "ElectoralPU")
    private EntityManager em;

    // ==================== ALCALDES ====================
    public boolean existeActaAlcalde(Integer idRecinto, Integer nMesa) {
        Long count = em.createQuery(
                "SELECT COUNT(v) FROM VotoAlcalde v WHERE v.recinto.idRecinto = :idRecinto AND v.nMesa = :nMesa", Long.class)
                .setParameter("idRecinto", idRecinto)
                .setParameter("nMesa", nMesa)
                .getSingleResult();
        return count > 0;
    }

    public void guardarOActualizarActaAlcalde(List<VotoAlcalde> votos) {
        for (VotoAlcalde voto : votos) {
            if (voto.getIdVotoAlcalde() == null) {
                em.persist(voto);
            } else {
                em.merge(voto);
            }
        }
    }

    // Retorna: [0]=idRecinto, [1]=nombreParroquia, [2]=nombreRecinto, [3]=nMesa, [4]=nombreUsuario, [5]=totalVotos
    public List<Object[]> listarActasAlcalde(Usuario usuario) {
        String jpql = "SELECT v.recinto.idRecinto, v.recinto.parroquia.nombre, v.recinto.nombre, v.nMesa, " +
                      "CONCAT(v.usuario.nombre, ' ', v.usuario.apellido), SUM(v.nVoto) " +
                      "FROM VotoAlcalde v ";
        if (usuario.getRol().getIdRol() != 1) {
            jpql += "WHERE v.usuario.idUsuario = :idUsuario ";
        }
        jpql += "GROUP BY v.recinto.idRecinto, v.recinto.parroquia.nombre, v.recinto.nombre, v.nMesa, v.usuario.nombre, v.usuario.apellido " +
                "ORDER BY v.recinto.parroquia.nombre, v.recinto.nombre, v.nMesa";

        var query = em.createQuery(jpql, Object[].class);
        if (usuario.getRol().getIdRol() != 1) {
            query.setParameter("idUsuario", usuario.getIdUsuario());
        }
        return query.getResultList();
    }

    public List<VotoAlcalde> obtenerVotosAlcaldePorMesa(Integer idRecinto, Integer nMesa) {
        return em.createQuery(
                "SELECT v FROM VotoAlcalde v WHERE v.recinto.idRecinto = :idRecinto AND v.nMesa = :nMesa ORDER BY v.alcalde.lista.idLista", VotoAlcalde.class)
                .setParameter("idRecinto", idRecinto)
                .setParameter("nMesa", nMesa)
                .getResultList();
    }

    // ==================== CONCEJALES ====================
    public boolean existeActaConcejal(Integer idRecinto, Integer nMesa) {
        Long count = em.createQuery(
                "SELECT COUNT(v) FROM VotoConcejal v WHERE v.recinto.idRecinto = :idRecinto AND v.nMesa = :nMesa", Long.class)
                .setParameter("idRecinto", idRecinto)
                .setParameter("nMesa", nMesa)
                .getSingleResult();
        return count > 0;
    }

    public void guardarOActualizarActaConcejal(List<VotoConcejal> votos) {
        for (VotoConcejal voto : votos) {
            if (voto.getIdVotoConcejal() == null) {
                em.persist(voto);
            } else {
                em.merge(voto);
            }
        }
    }

    public List<Object[]> listarActasConcejal(Usuario usuario) {
        String jpql = "SELECT v.recinto.idRecinto, v.recinto.parroquia.nombre, v.recinto.nombre, v.nMesa, " +
                      "CONCAT(v.usuario.nombre, ' ', v.usuario.apellido), SUM(v.nVoto) " +
                      "FROM VotoConcejal v ";
        if (usuario.getRol().getIdRol() != 1) {
            jpql += "WHERE v.usuario.idUsuario = :idUsuario ";
        }
        jpql += "GROUP BY v.recinto.idRecinto, v.recinto.parroquia.nombre, v.recinto.nombre, v.nMesa, v.usuario.nombre, v.usuario.apellido " +
                "ORDER BY v.recinto.parroquia.nombre, v.recinto.nombre, v.nMesa";

        var query = em.createQuery(jpql, Object[].class);
        if (usuario.getRol().getIdRol() != 1) {
            query.setParameter("idUsuario", usuario.getIdUsuario());
        }
        return query.getResultList();
    }

    public List<VotoConcejal> obtenerVotosConcejalPorMesa(Integer idRecinto, Integer nMesa) {
        return em.createQuery(
                "SELECT v FROM VotoConcejal v WHERE v.recinto.idRecinto = :idRecinto AND v.nMesa = :nMesa ORDER BY v.concejal.lista.idLista, v.concejal.nombre", VotoConcejal.class)
                .setParameter("idRecinto", idRecinto)
                .setParameter("nMesa", nMesa)
                .getResultList();
    }

    // ==================== JUNTAS ====================
    public boolean existeActaJunta(Integer idRecinto, Integer nMesa) {
        Long count = em.createQuery(
                "SELECT COUNT(v) FROM VotoJunta v WHERE v.recinto.idRecinto = :idRecinto AND v.nMesa = :nMesa", Long.class)
                .setParameter("idRecinto", idRecinto)
                .setParameter("nMesa", nMesa)
                .getSingleResult();
        return count > 0;
    }

    public void guardarOActualizarActaJunta(List<VotoJunta> votos) {
        for (VotoJunta voto : votos) {
            if (voto.getIdVotoJunta() == null) {
                em.persist(voto);
            } else {
                em.merge(voto);
            }
        }
    }

    public List<Object[]> listarActasJunta(Usuario usuario) {
        String jpql = "SELECT v.recinto.idRecinto, v.recinto.parroquia.nombre, v.recinto.nombre, v.nMesa, " +
                      "CONCAT(v.usuario.nombre, ' ', v.usuario.apellido), SUM(v.nVoto) " +
                      "FROM VotoJunta v ";
        if (usuario.getRol().getIdRol() != 1) {
            jpql += "WHERE v.usuario.idUsuario = :idUsuario ";
        }
        jpql += "GROUP BY v.recinto.idRecinto, v.recinto.parroquia.nombre, v.recinto.nombre, v.nMesa, v.usuario.nombre, v.usuario.apellido " +
                "ORDER BY v.recinto.parroquia.nombre, v.recinto.nombre, v.nMesa";

        var query = em.createQuery(jpql, Object[].class);
        if (usuario.getRol().getIdRol() != 1) {
            query.setParameter("idUsuario", usuario.getIdUsuario());
        }
        return query.getResultList();
    }

    public List<VotoJunta> obtenerVotosJuntaPorMesa(Integer idRecinto, Integer nMesa) {
        return em.createQuery(
                "SELECT v FROM VotoJunta v WHERE v.recinto.idRecinto = :idRecinto AND v.nMesa = :nMesa ORDER BY v.junta.lista.idLista, v.junta.nombre", VotoJunta.class)
                .setParameter("idRecinto", idRecinto)
                .setParameter("nMesa", nMesa)
                .getResultList();
    }
    
        public void eliminarActaAlcalde(Integer idRecinto, Integer nMesa) {
        em.createQuery("DELETE FROM VotoAlcalde v WHERE v.recinto.idRecinto = :idRecinto AND v.nMesa = :nMesa")
                .setParameter("idRecinto", idRecinto)
                .setParameter("nMesa", nMesa)
                .executeUpdate();
    }

    public void eliminarActaConcejal(Integer idRecinto, Integer nMesa) {
        em.createQuery("DELETE FROM VotoConcejal v WHERE v.recinto.idRecinto = :idRecinto AND v.nMesa = :nMesa")
                .setParameter("idRecinto", idRecinto)
                .setParameter("nMesa", nMesa)
                .executeUpdate();
    }

    public void eliminarActaJunta(Integer idRecinto, Integer nMesa) {
        em.createQuery("DELETE FROM VotoJunta v WHERE v.recinto.idRecinto = :idRecinto AND v.nMesa = :nMesa")
                .setParameter("idRecinto", idRecinto)
                .setParameter("nMesa", nMesa)
                .executeUpdate();
    }

}

