package com.sebas.sistemaelectoral.controlador;

import com.sebas.sistemaelectoral.modelo.Parroquia;
import com.sebas.sistemaelectoral.negocio.CatalogoService;
import com.sebas.sistemaelectoral.negocio.ReporteService;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named("reportesBean")
@ViewScoped
public class ReportesBean implements Serializable {

    @EJB
    private ReporteService reporteService;

    @EJB
    private CatalogoService catalogoService;

    private String tipoConcejalFiltro;
    private Integer idParroquiaFiltro;
    private List<Parroquia> parroquias;

    @PostConstruct
    public void init() {
        parroquias = catalogoService.listarParroquias();
    }

    public List<Object[]> getResultadosAlcalde() {
        return reporteService.obtenerResultadosAlcalde();
    }

    public List<Object[]> getResultadosConcejal() {
        return reporteService.obtenerResultadosConcejal(tipoConcejalFiltro);
    }

    public List<Object[]> getResultadosJunta() {
        return reporteService.obtenerResultadosJunta(idParroquiaFiltro);
    }

    public String getTipoConcejalFiltro() { return tipoConcejalFiltro; }
    public void setTipoConcejalFiltro(String tipoConcejalFiltro) { this.tipoConcejalFiltro = tipoConcejalFiltro; }

    public Integer getIdParroquiaFiltro() { return idParroquiaFiltro; }
    public void setIdParroquiaFiltro(Integer idParroquiaFiltro) { this.idParroquiaFiltro = idParroquiaFiltro; }

    public List<Parroquia> getParroquias() { return parroquias; }
}
