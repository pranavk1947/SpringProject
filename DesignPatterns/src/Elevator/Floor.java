package Elevator;

public class Floor {
	int floorId;
    ExternalButton externalButton;

    public Floor(int floorId) {
        this.floorId = floorId;
        this.externalButton = new ExternalButton(floorId, Direction.UP); // Assuming default direction is UP
    }

//    public Floor(int i) {
//		// TODO Auto-generated constructor stub
//	}

	public int getFloorId() {
        return floorId;
    }

    public ExternalButton getExternalButton() {
        return externalButton;
    }
}
