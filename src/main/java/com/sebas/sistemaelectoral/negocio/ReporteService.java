package com.sebas.sistemaelectoral.negocio;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class ReporteService {

    @PersistenceContext(unitName = "ElectoralPU")
    private EntityManager em;

    // Retorna arreglo: [0]=Nombre Lista, [1]=Nombre Alcalde, [2]=Total Votos
    public List<Object[]> obtenerResultadosAlcalde() {
        return em.createQuery(
                "SELECT a.lista.nombre, a.nombre, COALESCE(SUM(v.nVoto), 0) " +
                "FROM Alcalde a LEFT JOIN VotoAlcalde v ON v.alcalde.idAlcalde = a.idAlcalde " +
                "GROUP BY a.idAlcalde, a.lista.nombre, a.nombre " +
                "ORDER BY SUM(v.nVoto) DESC", Object[].class)
                .getResultList();
    }

    // Filtra por URBANO, RURAL o muestra todos si tipo es null o vacío
    // Retorna arreglo: [0]=Nombre Lista, [1]=Tipo, [2]=Nombre Concejal, [3]=Total Votos
    public List<Object[]> obtenerResultadosConcejal(String tipo) {
        if (tipo == null || tipo.trim().isEmpty()) {
            return em.createQuery(
                    "SELECT c.lista.nombre, c.tipo, c.nombre, COALESCE(SUM(v.nVoto), 0) " +
                    "FROM Concejal c LEFT JOIN VotoConcejal v ON v.concejal.idConcejal = c.idConcejal " +
                    "GROUP BY c.idConcejal, c.lista.nombre, c.tipo, c.nombre " +
                    "ORDER BY SUM(v.nVoto) DESC", Object[].class)
                    .getResultList();
        } else {
            return em.createQuery(
                    "SELECT c.lista.nombre, c.tipo, c.nombre, COALESCE(SUM(v.nVoto), 0) " +
                    "FROM Concejal c LEFT JOIN VotoConcejal v ON v.concejal.idConcejal = c.idConcejal " +
                    "WHERE UPPER(TRIM(c.tipo)) = UPPER(TRIM(:tipo)) " +
                    "GROUP BY c.idConcejal, c.lista.nombre, c.tipo, c.nombre " +
                    "ORDER BY SUM(v.nVoto) DESC", Object[].class)
                    .setParameter("tipo", tipo)
                    .getResultList();
        }
    }

    // Filtra por parroquia específica o muestra todas si idParroquia es null
    // Retorna arreglo: [0]=Nombre Parroquia, [1]=Nombre Lista, [2]=Nombre Junta, [3]=Total Votos
    public List<Object[]> obtenerResultadosJunta(Integer idParroquia) {
        if (idParroquia == null) {
            return em.createQuery(
                    "SELECT j.parroquia.nombre, j.lista.nombre, j.nombre, COALESCE(SUM(v.nVoto), 0) " +
                    "FROM Junta j LEFT JOIN VotoJunta v ON v.junta.idJunta = j.idJunta " +
                    "GROUP BY j.idJunta, j.parroquia.nombre, j.lista.nombre, j.nombre " +
                    "ORDER BY j.parroquia.nombre ASC, SUM(v.nVoto) DESC", Object[].class)
                    .getResultList();
        } else {
            return em.createQuery(
                    "SELECT j.parroquia.nombre, j.lista.nombre, j.nombre, COALESCE(SUM(v.nVoto), 0) " +
                    "FROM Junta j LEFT JOIN VotoJunta v ON v.junta.idJunta = j.idJunta " +
                    "WHERE j.parroquia.idParroquia = :idParroquia " +
                    "GROUP BY j.idJunta, j.parroquia.nombre, j.lista.nombre, j.nombre " +
                    "ORDER BY SUM(v.nVoto) DESC", Object[].class)
                    .setParameter("idParroquia", idParroquia)
                    .getResultList();
        }
    }
}
