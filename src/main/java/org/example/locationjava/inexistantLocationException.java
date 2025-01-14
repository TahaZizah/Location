package org.example.locationjava;

public class inexistantLocationException extends Exception {
    public inexistantLocationException() {
        super("Cette Location n'existe pas !");
    }
}
