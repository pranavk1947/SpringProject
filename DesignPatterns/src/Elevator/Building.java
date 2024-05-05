package Elevator;

import java.util.ArrayList;
import java.util.List;

public class Building {
    private int noOfFloors;
    private int noOfElevators;
    private List<ElevatorCar> elevators;

    public Building(int noOfFloors, int noOfElevators) {
        this.noOfFloors = noOfFloors;
        this.noOfElevators = noOfElevators;
        this.elevators = new ArrayList<>();
        initializeElevators();
    }

    private void initializeElevators() {
        for (int i = 1; i <= noOfElevators; i++) {
            ElevatorCar elevator = new ElevatorCar(i,noOfFloors);
            elevators.add(elevator);
        }
    }

    // Getters for number of floors and elevators
    public int getNoOfFloors() {
        return noOfFloors;
    }

    public int getNoOfElevators() {
        return noOfElevators;
    }

    // Getter for list of elevators
    public List<ElevatorCar> getElevators() {
        return elevators;
    }

    // Other methods related to building management can go here
}
