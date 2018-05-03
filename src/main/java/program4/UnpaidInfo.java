package program4;
/*
Authors: Garrett Seale, Andrew Camps
CSC460
Program 4
UnpaidInfo.java: Holds attributes that need to be displayed on the unpaidLeases page.
*/
public class UnpaidInfo {

	public String StuName;
	public int PayDue;
	public int RoomNum;	
	public String ResidenceHall;

	UnpaidInfo(String StuName, int PayDue,int RoomNum, String ResidenceHall) {
		this.StuName = StuName;
		this.PayDue = PayDue;
		this.RoomNum = RoomNum;
		this.ResidenceHall = ResidenceHall;
	}
}
