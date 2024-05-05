package Elevator;

public class InternalButton {
    private int floorNumber;

    public InternalButton(int floorNumber) {
        this.floorNumber = floorNumber;
    }

    public int getFloorNumber() {
        return floorNumber;
    }

    public void press() {
        System.out.println("Internal button pressed for floor " + floorNumber);
        // Add logic to handle button press, such as notifying the elevator system
    }
}
