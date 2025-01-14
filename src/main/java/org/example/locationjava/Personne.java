package org.example.locationjava;

import java.io.Serializable;

public abstract class Personne implements Serializable {
    private static final long serialVersionUID = 1L;
    private String nom;
    private String prenom;
    private int age;

    public Personne(String nom, String prenom, int age) {
        this.nom = nom;
        this.prenom = prenom;
        this.age = age;
    }

    public Personne(){
        this.nom = "";
        this.prenom = "";
        this.age = 0;
    }

    public String getNom() {
        return nom;
    }
    public String getPrenom() {
        return prenom;
    }
    public int getAge() {
        return age;
    }

    @Override
    public String toString() {
        return "nom: " +nom + "| prénom: " + prenom + "| age: " + age;
    }



}
