package com.example.frontend;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.glassfish.jersey.jackson.JacksonFeature;

public class FournisseurFx {
    private Client client = ClientBuilder.newClient().register(JacksonFeature.class);
    private WebTarget target = client.target(Connextion_info.url);
    private  int id;
    private  String nom;
    private  String email;
    private  String ville;
    private  String codeape;
    private  String formejuridique;
    private  String numsiren;
    private  String tel;

    public FournisseurFx(int id,String nom,String email, String ville,String codeape,String formejuridique,String numsiren, String tel){
        this.id = id;
        this.nom=nom;
        this.email=email;
        this.ville=ville;
        this.codeape=codeape;
        this.formejuridique=formejuridique;
        this.numsiren=numsiren;
        this.tel=tel;
    }
    public int getId(){
        return id;
    }

    public String getNom() {
        return nom;
    }

    public String getEmail() {
        return email;
    }

    public String getVille() {
        return ville;
    }

    public String getCodeape() {
        return codeape;
    }

    public String getFormejuridique() {
        return formejuridique;
    }

    public String getNumsiren() {
        return numsiren;
    }

    public String getTel() {
        return tel;
    }

    public void modifierFournisseur(String nom, String email, String ville, String codeape, String formejuridique, String numsiren, String tel) {
        // update the information of the Fournisseur
        this.nom = nom;
        this.email = email;
        this.ville = ville;
        this.codeape = codeape;
        this.formejuridique = formejuridique;
        this.numsiren = numsiren;
        this.tel = tel;

        // save the updated Fournisseur to the database
        Response response = target.path("/fournisseur")
                .path("/updateFournisseur")
                .queryParam("fournisseur_id", this.id)
                .queryParam("fournisseur_nom", this.nom)
                .queryParam("fournisseur_email", this.email)
                .queryParam("fournisseur_ville", this.ville)
                .queryParam("fournisseur_codeape", this.codeape)
                .queryParam("fournisseur_formejuridique", this.formejuridique)
                .queryParam("fournisseur_numsiren", this.numsiren)
                .queryParam("fournisseur_tel", this.tel)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .put(Entity.json(""));
        //System.out.println(response);
    }}
