package com.example.frontend;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DemandeDm {
    private int id;
    private String etatDemande;
    private LocalDate dateDebut;
    private String departement;
    public List<DetailDemandeDm> detailsDemandeDm = new ArrayList<>();

    public DemandeDm() {
    }

    public DemandeDm(int id, String etatDemande, LocalDate dateDebut, String departement) {
        this.id = id;
        this.etatDemande = etatDemande;
        this.dateDebut = dateDebut;
        this.departement = departement;
    }

    public List<DetailDemandeDm> getDetailsDemandeDm() {
        return detailsDemandeDm;
    }

    public void setDetailsDemandeDm(List<DetailDemandeDm> detailsDemandeDm) {
        this.detailsDemandeDm = detailsDemandeDm;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEtatDemande() {
        return etatDemande;
    }

    public void setEtatDemande(String etatDemande) {
        this.etatDemande = etatDemande;
    }

    public LocalDate getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
    }


    public String getDepartement() {
        return departement;
    }

    public void setDepartement(String departement) {
        this.departement = departement;
    }

    @Override
    public String toString() {
        return "DemandeDm{" +
                "id=" + id +
                ", etatDemande='" + etatDemande + '\'' +
                ", dateDebut=" + dateDebut +
                ", departement='" + departement + '\'' +
                '}';
    }
}
