package com.sebas.sistemaelectoral.negocio;

import com.sebas.sistemaelectoral.modelo.Alcalde;
import com.sebas.sistemaelectoral.modelo.Concejal;
import com.sebas.sistemaelectoral.modelo.Junta;
import com.sebas.sistemaelectoral.modelo.Parroquia;
import com.sebas.sistemaelectoral.modelo.Recinto;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class CatalogoService {

    @PersistenceContext(unitName = "ElectoralPU")
    private EntityManager em;

    public List<Parroquia> listarParroquias() {
        return em.createQuery("SELECT p FROM Parroquia p ORDER BY p.nombre", Parroquia.class)
                .getResultList();
    }

    public List<Parroquia> listarParroquiasRurales() {
        return em.createQuery(
                "SELECT p FROM Parroquia p WHERE UPPER(TRIM(p.tipo)) = 'RURAL' ORDER BY p.nombre", Parroquia.class)
                .getResultList();
    }

    public Parroquia buscarParroquiaPorId(Integer idParroquia) {
        return em.find(Parroquia.class, idParroquia);
    }

    public List<Recinto> listarRecintosPorParroquia(Integer idParroquia) {
        return em.createQuery(
                "SELECT r FROM Recinto r WHERE r.parroquia.idParroquia = :idParroquia ORDER BY r.nombre", Recinto.class)
                .setParameter("idParroquia", idParroquia)
                .getResultList();
    }

    public Recinto buscarRecintoPorId(Integer idRecinto) {
        return em.find(Recinto.class, idRecinto);
    }

    public List<Alcalde> listarAlcaldes() {
        return em.createQuery("SELECT a FROM Alcalde a ORDER BY a.lista.idLista", Alcalde.class)
                .getResultList();
    }

    // Filtra concejales según el tipo de la parroquia (URBANO o RURAL) ignorando mayúsculas/minúsculas
    public List<Concejal> listarConcejalesPorTipo(String tipo) {
        return em.createQuery(
                "SELECT c FROM Concejal c WHERE UPPER(TRIM(c.tipo)) = UPPER(TRIM(:tipo)) ORDER BY c.lista.idLista, c.nombre", Concejal.class)
                .setParameter("tipo", tipo)
                .getResultList();
    }

    // Filtra candidatos de junta únicamente de la parroquia seleccionada
    public List<Junta> listarJuntasPorParroquia(Integer idParroquia) {
        return em.createQuery(
                "SELECT j FROM Junta j WHERE j.parroquia.idParroquia = :idParroquia ORDER BY j.lista.idLista, j.nombre", Junta.class)
                .setParameter("idParroquia", idParroquia)
                .getResultList();
    }
}
