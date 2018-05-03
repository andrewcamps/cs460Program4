package program4;

/*
Authors: Garrett Seale, Andrew Camps
CSC460
Program 4
AdvisorDTO.java: This class holds attributes for Advisor that need to be displayed on the addStudents page to make picking an advisor easy
*/
public class AdvisorDTO {
	public int id;
	public String info;

	public AdvisorDTO(int id, String info) {
		this.id = id;
		this.info = info;
	}

}
