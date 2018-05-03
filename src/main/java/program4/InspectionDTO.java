package program4;

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
