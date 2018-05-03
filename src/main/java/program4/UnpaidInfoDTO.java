package program4;
/*
Authors: Garrett Seale, Andrew Camps
CSC460
Program 4
UnpaidInfo.java: Holds attributes that need to be displayed on the unpaidLeases page.
*/
public class UnpaidInfoDTO {

	public String StuName;
	public float PayDue;
	public int RoomNum;	
	public String ResidenceHall;

	UnpaidInfoDTO(String StuName, float PayDue,int RoomNum, String ResidenceHall) {
		this.StuName = StuName;
		this.PayDue = PayDue;
		this.RoomNum = RoomNum;
		this.ResidenceHall = ResidenceHall;
	}
}
