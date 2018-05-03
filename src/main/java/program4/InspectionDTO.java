package program4;

/*
Authors: Garrett Seale, Andrew Camps
CSC460
Program 4
InspectionDTO.java: This class holds attributes for Inspections that need to be displayed on the getInspections page for a room.
*/
public class InspectionDTO {

	public int id;
	public String date;
	public String name;
	public String status;
	public String action;

	InspectionDTO(int id, String date, String name, String status, String action) {
		this.id = id;
		this.date = date;
		this.name = name;
		this.status = status;
		this.action = action;
	}

}
