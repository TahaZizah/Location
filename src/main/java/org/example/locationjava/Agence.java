package org.example.locationjava;

import java.util.ArrayList;
import java.io.*;

public class Agence implements Serializable {
    private static final long serialVersionUID = 1L;
    public ArrayList<Vehicule> vehicules;
    public ArrayList<Client> clients;
    public ArrayList<Location> locations;

    public Agence() {
        vehicules = new ArrayList<>();
        clients = new ArrayList<>();
        locations = new ArrayList<>();
    }

    public Vehicule searchVehicule(String matricule) throws inexistantVehiculeException {
        for (Vehicule vehicule : vehicules) {
            if (vehicule.getMatricule().equals(matricule)) {
                return vehicule;
            }
        }
        throw new inexistantVehiculeException();
    }

    public Client searchClient(int clientid) throws inexistantClientException {
        for (Client client : clients) {
            if (client.getClientId() == clientid) {
                return client;
            }
        }
        throw new inexistantClientException();
    }

    public Location searchLocation(int clientid, String matricule) throws inexistantLocationException {
        for (Location location : locations) {
            if (location.getClient().getClientId() == clientid && location.getVehicule().getMatricule().equals(matricule)) {
                return location;
            }
        }
        throw new inexistantLocationException();
    }

    public static void writeToFile(ArrayList<?> list, String file) throws IOException {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file))) {
            out.writeObject(list);
        } catch (EOFException eof) {
            throw new IOException("End of file reached unexpectedly while writing: " + eof.getMessage());
        }
    }

    public void addVehicule(Vehicule vehicule) throws IOException {
        try {
            searchVehicule(vehicule.getMatricule());
        } catch (inexistantVehiculeException e) {
            vehicules.add(vehicule);
            try {
                writeToFile(vehicules, "Vehicule.txt");
            } catch (IOException er) {
                System.out.println("Failed to save vehicule");
                vehicules.remove(vehicule);
            }
        }
    }

    public void addClient(Client client) {
        try {
            searchClient(client.getClientId()); // Check if the client already exists
        } catch (inexistantClientException e) {
            if (client.getNom() == null || client.getNom().isEmpty()) {
                throw new IllegalArgumentException("Client name cannot be empty.");
            }
            clients.add(client);
            try {
                writeToFile(clients, "Client.txt"); // Save the updated list
            } catch (IOException er) {
                System.out.println("Failed to save client");
                clients.remove(client); // Rollback if saving fails
            }
        }
    }


    public void addLocation(Location location) {
        try {
            searchLocation(location.getClient().getClientId(), location.getVehicule().getMatricule());
        } catch (inexistantLocationException e) {
            locations.add(location);
            try {
                writeToFile(locations, "Location.txt");
            } catch (IOException er) {
                System.out.println("Failed to save location");
                locations.remove(location);
            }
        }
    }

    public void returnVehicle(int clientId, String matricule) throws inexistantLocationException {
        Location location = searchLocation(clientId, matricule); // Find the location based on client ID and matricule
        location.locationReturn(); // Updates `dateRetour` to the current date
        System.out.println("Vehicle returned successfully.");
        try {
            writeToFile(locations, "Location.txt"); // Save updated location list
        } catch (IOException e) {
            System.out.println("Failed to save updated location data: " + e.getMessage());
        }
    }


    public void deleteVehicule(String matricule) throws inexistantVehiculeException {
        try {
            Vehicule vehicule = searchVehicule(matricule);
            vehicules.remove(vehicule);
        } catch (inexistantVehiculeException e) {
            System.out.println(e.getMessage());
        }
    }

    public void deleteClient(int clientid) throws inexistantClientException {
        try {
            Client client = searchClient(clientid);
            clients.remove(client);
        } catch (inexistantClientException e) {
            System.out.println(e.getMessage());
        }
    }

    public void showVehicules() {
        for (Vehicule vehicule : vehicules) {
            System.out.println(vehicule);
        }
    }

    public void showClients() {
        for (Client client : clients) {
            System.out.println(client);
        }
    }

    public void showLocations() {
        for (Location location : locations) {
            String status = switch (location.getState()) {
                case 0 -> "Returned";
                case 1 -> "In Progress";
                case 2 -> "Overdue";
                default -> "Unknown";
            };
            System.out.println("Contract #" + location.getNumContract() +
                    ", Client: " + location.getClient().getNom() +
                    ", Vehicle: " + location.getVehicule().getMatricule() +
                    ", Status: " + status +
                    ", Return Date: " + location.getDateRetourPrévu());
        }
    }

    public static <T> ArrayList<T> readFromFile(String file) throws IOException, ClassNotFoundException {
        File f = new File(file);
        if (!f.exists() || f.length() == 0) {
            return new ArrayList<>();
        }

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(f))) {
            return (ArrayList<T>) in.readObject();
        }
    }


    public void loadClients() {
        try {
            clients = readFromFile("Client.txt");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void loadVehicules() {
        try {
            vehicules = readFromFile("Vehicule.txt");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }


    public void loadLocations() {
        try {
            locations = readFromFile("Location.txt");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    public boolean isCarAvailable(String matricule) {
        // This function checks if a car is available for rent based on its matricule
        Vehicule v;
        try {
            v = searchVehicule(matricule);
            for (Location location : locations) {
                if (location.getVehicule().equals(v) && location.getState() != 0) {
                    return false;
                }
            }
            return true;
        } catch (inexistantVehiculeException e) {
            return false;
        }
    }
}
