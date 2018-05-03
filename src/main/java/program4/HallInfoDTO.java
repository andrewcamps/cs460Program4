package program4;
/*
Authors: Garrett Seale, Andrew Camps
CSC460
Program 4
HallInfo.java: Holds attributes to be return in the queries that require Residence Halls.
*/
public class HallInfoDTO {
	
    public String hname;
    public String sname;
    public String phone;

    HallInfoDTO(String hname, String sname, String phone) {
        this.hname = hname;
        this.sname = sname;
        this.phone = phone;
    }
}
