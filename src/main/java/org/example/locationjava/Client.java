package org.example.locationjava;

import java.io.Serializable;

public class Client extends Personne implements Serializable {
    private static final long serialVersionUID = 1L;
    int clientId;
    static int compteur = 1;

    public Client(String nom, String prenom, int age) {
        super(nom, prenom, age);
        clientId = compteur++;
    }

    public Client(){
        super("","",0);
    }

    public int getClientId() {
        return clientId;
    }


    @Override
    public String toString() {
        return super.toString() + "| ClientId: " + clientId;
    }
}













