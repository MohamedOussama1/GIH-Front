package com.example.frontend;

public class DetailDemandeDm {
    private int id;
    private int qte;
    private String typeDm;
    private String nomDm;
    private String etat;

    public DetailDemandeDm() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public DetailDemandeDm(String etat, int id, int qte, String typeDm, String nomDm) {
        this.etat = etat;
        this.id = id;
        this.qte = qte;
        this.typeDm = typeDm;
        this.nomDm = nomDm;
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
