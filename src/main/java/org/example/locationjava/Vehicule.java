package org.example.locationjava;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

public class Vehicule implements Comparable<Vehicule>, Serializable {
    private static final long serialVersionUID = 1L;
    public String matricule;
    public String marque;
    public String modele;
    public int prix;
    public int kilometrage;
    private boolean available = true;

    public Vehicule(String matricule, String marque, String modele, int prix, int kilo) {
        this.matricule = matricule;
        this.marque = marque;
        this.prix = prix;
        this.modele = modele;
        this.kilometrage = kilo;
    }


    public String getMatricule() {
        return matricule;
    }
    public String getMarque() {
        return marque;
    }
    public String getModele() {
        return modele;
    }
    public int getPrix() {
        return prix;
    }
    public int getKilometrage() {
        return kilometrage;
    }
    public void setKilometrage(int kilometrage) {
        this.kilometrage = kilometrage;
    }
    public void setPrix(int prix) {
        this.prix = prix;
    }

    @Override
    public String toString() {
        return "Matricule: " + matricule +
                "| Marque: " + marque +
                "| Modele: " + modele +
                "| Kilometrage: " + kilometrage +
                "| Prix: " + prix + "DH";
    }

    @Override
    public int compareTo(Vehicule v) {
        return this.prix - v.prix;
    }

    public boolean isAvailable() {
        try {
            ArrayList<Location> locations = Agence.readFromFile("Location.txt");
            for (Location location : locations) {
                if (location.getVehicule().getMatricule().equals(this.matricule)
                        && location.getState() != 0) {
                    this.available = false;
                    saveAvailabilityStatus();
                    return false;
                }
            }
            if (!this.available) {
                this.available = true;
                saveAvailabilityStatus();
            }
            return true;
        } catch (IOException | ClassNotFoundException e) {
            return this.available;
        }
    }

    public void setAvailable(boolean available) {
        if (this.available != available) {
            this.available = available;
            saveAvailabilityStatus();
        }
    }

    private void saveAvailabilityStatus() {
        try {
            ArrayList<Vehicule> vehicles = Agence.readFromFile("Vehicule.txt");

            for (Vehicule v : vehicles) {
                if (v.getMatricule().equals(this.matricule)) {
                    v.available = this.available;
                    break;
                }
            }
            Agence.writeToFile(vehicles, "Vehicule.txt");
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error saving vehicle availability status: " + e.getMessage());
        }
    }
}
