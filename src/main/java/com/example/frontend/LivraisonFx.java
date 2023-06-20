package com.example.frontend;

import java.util.List;

public class LivraisonFx {
    int id;
    String nomFournisseur;
    String date;
    List<DetailLivraisonFX> detailLivraisonFXES;

    public List<DetailLivraisonFX> getDetailLivraisonFXES() {
        return detailLivraisonFXES;
    }

    public void setDetailLivraisonFXES(List<DetailLivraisonFX> detailLivraisonFXES) {
        this.detailLivraisonFXES = detailLivraisonFXES;
    }

    public int getId() {
        return id;
    }

    public LivraisonFx(int id, String nomFournisseur, String date) {
        this.id = id;
        this.nomFournisseur = nomFournisseur;
        this.date = date;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNomFournisseur() {
        return nomFournisseur;
    }

    public void setNomFournisseur(String nomFournisseur) {
        this.nomFournisseur = nomFournisseur;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
