package org.example.locationjava;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

public class Location implements Serializable {
    static int count = 0;
    private Client client;
    public Vehicule vehicule;
    private int numContract;
    private Date datePrise;
    private Date dateRetour;
    private Date dateRetourPrévu;


    public Location(Client client, Vehicule vehicule) {
        this.client = client;
        this.vehicule = vehicule;
        this.numContract = count++;
        this.datePrise = new Date();
        // increment the date by Days
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(datePrise);
        int Days = 8;
        calendar.add(Calendar.DAY_OF_MONTH, Days);

        this.dateRetourPrévu = calendar.getTime();

    }

    public int getNumContract() {
        return numContract;
    }

    public Client getClient() {
        return client;
    }

    public Vehicule getVehicule() {
        return vehicule;
    }

    public Date getDatePrise() {
        return datePrise;
    }

    public Date getDateRetour() {
        return dateRetour;
    }

    public Date getDateRetourPrévu() {
        return dateRetourPrévu;
    }

    public int getState(){
        // 0 est rendu
        // 1 est en cours
        // 2 est en retard
        if (dateRetour != null) {
            if (dateRetour.after(dateRetourPrévu)) {
                return 2;
            }else {
                return 0;
            }
        }else{
            Date current = new Date();
            if (current.after(dateRetourPrévu)) {
                return 2;
            }else{
                return 1;
            }
        }
    }
    public void locationReturn(){
        this.dateRetour = new Date();
    }

}
