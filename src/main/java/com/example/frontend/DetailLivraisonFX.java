package com.example.frontend;
public class DetailLivraisonFX {
    private String dm;
    private String qte;

    public DetailLivraisonFX(String dm, String qte) {
        this.dm = dm;
        this.qte = qte;
    }



    public String getDm() {
        return dm;
    }

    public String getQte() {
        return qte;
    }
    public void setQte(String qte){
        this.qte = qte;
    }

}
