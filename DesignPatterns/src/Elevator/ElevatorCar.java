package Elevator;

import java.util.ArrayList;
import java.util.List;


public class ElevatorCar {
    int id;
    ElevatorDisplay display;
    List<InternalButton> internalButtons = new ArrayList<>();
    ElevatorState state;
    Direction direction;
    ElevatorDoor door;

    public ElevatorCar(int id, int noOfFloors) {
        this.id = id;
        this.display = new ElevatorDisplay();
        this.state = ElevatorState.IDLE;
        this.direction = Direction.UP;
        this.door = new ElevatorDoor();
        
        // Create internal buttons for each floor and add them to the internalButtons list
        for (int i = 1; i <= noOfFloors; i++) {
            InternalButton internalButton = new InternalButton(i);
            internalButtons.add(internalButton);
        }
    }
}

