package ATMDesign;

//Enum to represent note values
enum Notes {
	Rs_100(100), 
	Rs_500(500);

	private int value;

	Notes(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}
}