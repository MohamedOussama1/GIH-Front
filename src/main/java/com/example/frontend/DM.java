package com.example.frontend;

public class DM {
int id;

    String nom;

    int quantite;
    String typeDM;


    public DM(){}

    public DM(String nom, int quantite, String typeDM) {
        this.nom = nom;
        this.quantite = quantite;
        this.typeDM=typeDM;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQuantite() {
        return quantite;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getTypeDM() {
        return typeDM;
    }

    public void setTypeDM(String typeDM) {
        this.typeDM = typeDM;
    }
}


