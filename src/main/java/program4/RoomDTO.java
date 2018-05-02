package program4;

public class RoomDTO {

	public int RoomID;
	public int RoomNumber;
	public float Rent;
	public String Loc;

	 RoomDTO(int RoomID, int RoomNumber, float Rent) {
		this.RoomID = RoomID;
		this.RoomNumber = RoomNumber;
		this.Rent = Rent;
	}

	public void setLocation(String Loc) {
		this.Loc = Loc;
	}
}
