package com.example.frontend;

public class DetailDemandeDm {
    private int id;
    private int qte;
    private String typeDm;
    private String nomDm;
    private int idDm;
    private String etat;
    private String departement;

    public DetailDemandeDm() {
    }

    public String getDepartement() {
        return departement;
    }

    public void setDepartement(String departement) {
        this.departement = departement;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public DetailDemandeDm(String etat, int id, int qte, String typeDm, String nomDm, int idDm) {
        this.etat = etat;
        this.id = id;
        this.qte = qte;
        this.typeDm = typeDm;
        this.nomDm = nomDm;
        this.idDm = idDm;
    }


    public int getIdDm() {
        return idDm;
    }

    public void setIdDm(int idDm) {
        this.idDm = idDm;
    }

    public DetailDemandeDm(int qte, String typeDm, String nomDm) {
        this.qte = qte;
        this.typeDm = typeDm;
        this.nomDm = nomDm;
        this.etat = "confirmé";
    }

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public int getQte() {
        return qte;
    }

    public void setQte(int qte) {
        this.qte = qte;
    }

    public String getTypeDm() {
        return typeDm;
    }

    public void setTypeDm(String typeDm) {
        this.typeDm = typeDm;
    }

    public String getNomDm() {
        return nomDm;
    }

    public void setNomDm(String nomDm) {
        this.nomDm = nomDm;
    }

    @Override
    public String toString() {
        return "DetailDemandeDm{" +
                "qte=" + qte +
                ", typeDm='" + typeDm + '\'' +
                ", nomDm='" + nomDm + '\'' +
                '}';
    }
}
