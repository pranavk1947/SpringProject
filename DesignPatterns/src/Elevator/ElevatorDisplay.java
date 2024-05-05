package Elevator;

public class ElevatorDisplay {
	private int floorId;
	private Direction direction;
	
	public void showDisplay()
	{
		System.out.println(floorId);
		System.out.println(direction);
	}
	
	public void setDisplay(int floor, Direction direction) {
		this.floorId = floor;
		this.direction = direction;
	}
	
	public int getFloorId() {
        return floorId;
    }

    public Direction getDirection() {
        return direction;
    }
	
}
