package program4;
/*
Authors: Garrett Seale, Andrew Camps
CSC460
Program 4
StudentDTO.java: This class holds attributes for Students that need to be displayed on the deleteStudent page.
*/
public class StudentDTO {

	public int StudentID;
	public String StuName;

	 public StudentDTO(int StudentID, String StuName) {
		this.StudentID = StudentID;
		this.StuName = StuName;
	}
}
