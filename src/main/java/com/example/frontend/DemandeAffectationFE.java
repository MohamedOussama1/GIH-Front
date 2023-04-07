package com.example.frontend;

import java.time.LocalDate;

public class DemandeAffectationFE {
    private int id;
    private String etatDemande;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private int qte;
    private String typeLit;
    private String modelLit;
    private String departement;

    public DemandeAffectationFE() {
    }

    public DemandeAffectationFE(int id, String etatDemande, LocalDate dateDebut, int qte, String typeLit, String modelLit, String departement) {
        this.id = id;
        this.etatDemande = etatDemande;
        this.dateDebut = dateDebut;
        this.qte = qte;
        this.typeLit = typeLit;
        this.modelLit = modelLit;
        this.departement = departement;
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

    public int getQte() {
        return qte;
    }

    public void setQte(int qte) {
        this.qte = qte;
    }

    public String getTypeLit() {
        return typeLit;
    }

    public void setTypeLit(String typeLit) {
        this.typeLit = typeLit;
    }

    public String getModelLit() {
        return modelLit;
    }

    public void setModelLit(String modelLit) {
        this.modelLit = modelLit;
    }

    public String getDepartement() {
        return departement;
    }

    public void setDepartement(String departement) {
        this.departement = departement;
    }

    public LocalDate getDateFin() {
        return dateFin;
    }

    public void setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
    }
}
