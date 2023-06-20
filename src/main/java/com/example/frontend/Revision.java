package com.example.frontend;

import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;

public class Revision {

    private int id;
    private int ambulanceId;

    private String startDate;
    private String endDate;
    private String description;
    private String ancienEtat;
    private String nouvelEtat;
    private int  nouvelKm;
    private int ancienKm;
    private String type;

    public int getId() {
        return id;
    }

    public Revision(int id, String startDate, String ancienEtat, int ancienKm, String type) {
        this.id = id;
        this.startDate = startDate;
        this.ancienEtat = ancienEtat;
        this.ancienKm = ancienKm;
        this.type = type;
    }
    public Revision(String startDate, String ancienEtat, int ancienKm, String type) {
        this.startDate = startDate;
        this.ancienEtat = ancienEtat;
        this.ancienKm = ancienKm;
        this.type = type;
    }

    public Revision(int id, String startDate, String endDate, String description, String ancienEtat, String nouvelEtat, int nouvelKm, int ancienKm, String type) {
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
        this.description = description;
        this.ancienEtat = ancienEtat;
        this.nouvelEtat = nouvelEtat;
        this.nouvelKm = nouvelKm;
        this.ancienKm = ancienKm;
        this.type = type;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAmbulanceId() {
        return ambulanceId;
    }

    public void setAmbulanceId(int ambulanceId) {
        this.ambulanceId = ambulanceId;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAncienEtat() {
        return ancienEtat;
    }

    public void setAncienEtat(String ancienEtat) {
        this.ancienEtat = ancienEtat;
    }

    public String getNouvelEtat() {
        return nouvelEtat;
    }

    public void setNouvelEtat(String nouvelEtat) {
        this.nouvelEtat = nouvelEtat;
    }

    public int getNouvelKm() {
        return nouvelKm;
    }

    public void setNouvelKm(int nouvelKm) {
        this.nouvelKm = nouvelKm;
    }

    public int getAncienKm() {
        return ancienKm;
    }

    public void setAncienKm(int ancienKm) {
        this.ancienKm = ancienKm;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
