package org.example.locationjava;

public class inexistantVehiculeException extends Exception {
    public inexistantVehiculeException() {
        super("Cette vehicule n'existe pas !");
    }

}
