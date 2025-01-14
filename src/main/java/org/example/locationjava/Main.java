package org.example.locationjava;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws inexistantLocationException {
        Scanner sc = new Scanner(System.in);
        Agence ag = new Agence();
        int choice, id;
        Vehicule vehicule=null;
        Client client=null;
        Location location=null;
        do {
            choice=menu();
            switch(choice) {
                case 1:
                    client = new Client(sc.next(), sc.next(), sc.nextInt());
                    ag.addClient(client);
                    ag.showClients();
                    System.out.println("Client added");
                    break;
                case 2:
                    vehicule = new Vehicule(sc.next(),sc.next(),sc.next(), sc.nextInt(),sc.nextInt());
                    try {
                        ag.addVehicule(vehicule);
                        System.out.println("Vehicule added");
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                case 3:
                    vehicule=null;
                    client=null;
                    try{
                        client=ag.searchClient(sc.nextInt());
                        vehicule=ag.searchVehicule(sc.next());
                        location = new Location(client,vehicule);
                        ag.addLocation(location);
                        System.out.println("Location added");
                    }
                    catch(inexistantClientException e){
                        System.out.println(e.getMessage());
                    }
                    catch(inexistantVehiculeException e){
                        System.out.println(e.getMessage());
                    }
                    break;
                case 4:
                    id=sc.nextInt();
                    client=null;
                    try{
                        client = ag.searchClient(id);
                        ag.clients.remove(client);
                        System.out.println("Client has been removed");
                    } catch (inexistantClientException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 5:
                    String mat = sc.next();
                    vehicule=null;
                    try{
                        vehicule = ag.searchVehicule(mat);
                        ag.vehicules.remove(vehicule);
                        System.out.println("Vehicule has been removed");
                    }
                    catch(inexistantVehiculeException e){
                        System.out.println(e.getMessage());
                    }
                    break;
                case 6:
                    vehicule=null;
                    client=null;
                    try{
                        client=ag.searchClient(sc.nextInt());
                        vehicule=ag.searchVehicule(sc.next());
                        location = ag.searchLocation(client.getClientId(), vehicule.getMatricule());
                        ag.locations.remove(location);
                        System.out.println("Location has been removed");
                    }
                    catch(inexistantClientException e){
                        System.out.println(e.getMessage());
                    }
                    catch(inexistantVehiculeException e){
                        System.out.println(e.getMessage());
                    }
                    catch (inexistantLocationException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 7:
                    ag.showClients();
                    break;
                case 8:
                    ag.showVehicules();
                    break;
                case 9:
                    ag.showLocations();
                    break;
                case 10:
                    ag.loadClients();
                    System.out.println("Clients loaded");
                    break;
                case 11:
                    ag.loadVehicules();
                    System.out.println("Vehicules loaded");
                    break;
                case 12:
                    ag.loadLocations();
                    System.out.println("Locations loaded");
                    break;
            }
        }while(choice!=0);
    }
    public static int menu(){
        Scanner sc = new Scanner(System.in);
        int choice;
        do {
            System.out.println("1. Add Client");
            System.out.println("2. Add Vehicule");
            System.out.println("3. Add Location");
            System.out.println("4. Delete Client");
            System.out.println("5. Delete Vehicule");
            System.out.println("6. Delete Location");
            System.out.println("7. Show Clients");
            System.out.println("8. Show Vehicules");
            System.out.println("9. Show Locations");
            System.out.println("10. Load Client");
            System.out.println("11. Load Vehicule");
            System.out.println("12. Load Location");
            System.out.println("0. Exit");
            choice = sc.nextInt();
        }while(choice>12 || choice<1);
        return choice;
    }
}

