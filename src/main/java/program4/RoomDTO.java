package program4;
/*
Authors: Garrett Seale, Andrew Camps
CSC460
Program 4
RoomDTO.java: This class contains that attributes that need to be returned for pages/queries that display room info.
*/
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
