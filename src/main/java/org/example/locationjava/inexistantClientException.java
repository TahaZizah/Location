package org.example.locationjava;

public class inexistantClientException extends Exception{
    public inexistantClientException(){
        super("Ce client n'existe pas !");
    }
}
