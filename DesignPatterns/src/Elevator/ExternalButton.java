package Elevator;

public class ExternalButton implements Button {
	private Direction direction;
	private int floorId;
	
	public ExternalButton(int floorId,Direction direction) {
		super();
		this.direction = direction;
		this.floorId = floorId;
	}

	public Direction getDirection() {
		return direction;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}

	public int getFloorId() {
		return floorId;
	}

	public void setFloorId(int floorId) {
		this.floorId = floorId;
	}

	@Override
	public void press() {
        System.out.println("External Button pressed for floor " + floorId + " with direction " + direction);
        // Implement logic to notify the elevator system about the button press
    }
	

}
