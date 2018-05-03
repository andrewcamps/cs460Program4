package program4;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.sql.*;

/*
Authors: Garrett Seale, Andrew Camps
CSC460
Program 4
MainController.java: This controller houses all of our endpoints that return html pages and data from our queries.
*/
@Controller
public class MainController {

	@Autowired
    private DataSource dataSource;
    private JdbcTemplate jdbcTemplate;

    @PostConstruct
    private void postConstruct() {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    //Navigate to page that allows the addition of students/staff to the DB
	@GetMapping("/addStudent")
    public String addStudent(Model model) {
		String sql = "select advisorid, advname, department from advisor";

		List<AdvisorDTO> adv = jdbcTemplate.query(sql, new RowMapper<AdvisorDTO>() {
            public AdvisorDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
				int id = rs.getInt("advisorid");
				String info = rs.getString("advname") + " (" + rs.getString("department") + ")";
			
                return new AdvisorDTO(id, info);
            }
        });
		
		model.addAttribute("advisor", adv);
        return "addStudent";        
    }

	@GetMapping("/addStaff")
	public String addStaff() {
		return "addStaff";
	}
	
    //Navigate to page that allows the addition of leases to the DB
    @GetMapping("/addLease")
    public String addLease(Model model){
		String sql = "select stuname, studentid from student";
		String sql2 = "select roomid, roomnum, rent, hallname from room join residence_hall using (hallid)";
        String sql3 = "select roomid, roomnum, rent, apartmentid from room join apartment using (apartmentid)";

        List<StudentDTO> stu = jdbcTemplate.query(sql, new RowMapper<StudentDTO>() {
            public StudentDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
                int id = rs.getInt("studentid");
                String info = rs.getString("stuname") + " - " + id;

                return new StudentDTO(id, info);
            }
        });

		List<RoomDTO> rh = jdbcTemplate.query(sql2, new RowMapper<RoomDTO>() {
            public RoomDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
                int id = rs.getInt("roomid");
				int rnum = rs.getInt("roomnum");
				float rent = rs.getFloat("rent");
                String info = rs.getString("hallname") + " - " + rnum;
				RoomDTO rm = new RoomDTO(id, rnum, rent);
				
				rm.setLocation(info);
                return rm;
            }
        });

        List<RoomDTO> apt = jdbcTemplate.query(sql3, new RowMapper<RoomDTO>() {
            public RoomDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
                int id = rs.getInt("roomid");
                int rnum = rs.getInt("roomnum");
                float rent = rs.getFloat("rent");
                String info = rs.getString("apartmentid") + " - " + "Room " + rnum;
				RoomDTO rm = new RoomDTO(id, rnum, rent);				

				rm.setLocation(info);
                return rm;
            }
        });

		for(RoomDTO rm : apt) {
			rh.add(rm);
		}

        model.addAttribute("student", stu);
        model.addAttribute("room", rh);
		return "addLease";        
    }

    //Return pages that accepts a HallID
	@GetMapping("/roomsList")
    public String roomsList(){
        return "roomsList";        
    }

    //After a HallID is provided to the roomList page, return this page which gives a list of RoomDTOs and displays them as a table
    @GetMapping("/roomsList/getRooms")
    public String getRooms(@RequestParam String HallID, Model model){
    	//Select rooms that match the HallID
       	String sql = "select RoomID, RoomNum, Rent from room r,residence_hall h where r.HallID = h.HallID and r.HallID = '" + HallID + "'";
		List<RoomDTO> rooms = jdbcTemplate.query(sql, new RowMapper<RoomDTO>() {
			public RoomDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
				return new RoomDTO(rs.getInt("RoomID"), rs.getInt("RoomNum"), rs.getInt("Rent"));
			}
		});
		model.addAttribute("rooms", rooms);
       	return "getRooms";               
    }

    //Takes all of the attributes of a Student as parameters and inserts that student into the DB
   	@GetMapping("/addStudent/post")
    public String postStudent(@RequestParam String StuName, @RequestParam String Address, @RequestParam String Phone, @RequestParam String Email, @RequestParam String Gender, @RequestParam String DOB, @RequestParam String Category, @RequestParam String ClassYear, @RequestParam String Major, @RequestParam String Minor, @RequestParam int AdvisorID, Model model) {	
		String sql = "insert into Student values (?,?,?,?,?,?,?,?,?,?,?,?)";
		String sqlId = "select max(studentid) from student";
		int newId = jdbcTemplate.queryForObject(sqlId, Integer.class) + 1;
			     
		jdbcTemplate.update(sql, newId, StuName, Address, Phone, Email, Gender, DOB, Category, ClassYear, Major, Minor, AdvisorID);
        return "addStudent";
    }
	
	//Takes all of the attributes of a Staff as parameters and inserts that staff into the DB
	@GetMapping("/addStaff/post")
    public String postStaff(@RequestParam String StaffName, @RequestParam String Email, @RequestParam String Address, @RequestParam String DOB, @RequestParam String Gender, @RequestParam String Title, @RequestParam String Location, Model model) {
        String sql = "insert into Staff values (?,?,?,?,?,?,?,?)";
		String sqlId = "select max(staffid) from staff";
		int newId = jdbcTemplate.queryForObject(sqlId, Integer.class) + 1;

        jdbcTemplate.update(sql,newId, StaffName, Email, Address, DOB, Gender, Title, Location);
        return "addStaff";
    }

    //Takes the attributes of a lease as input and inserts them into the DB
	@GetMapping("/addLease/post")
    public String postLease(@RequestParam int Duration, @RequestParam String LName, @RequestParam float Cost, @RequestParam String StartDate, @RequestParam String EndDate, @RequestParam int StudentID, @RequestParam int RoomID, Model model) {
        String sql = "insert into Lease values (?,?,?,?,?,?,?,?)";
		String sqlId = "select max(leaseid) from lease";
        int newId = jdbcTemplate.queryForObject(sqlId, Integer.class) + 1;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        java.sql.Date sqlSD;
		java.sql.Date sqlED;

		try {
			java.util.Date SD = format.parse(StartDate);
        	java.util.Date ED = format.parse(EndDate);
        	sqlSD = new java.sql.Date(SD.getTime());
        	sqlED = new java.sql.Date(ED.getTime());
		} catch (Exception e) {
			sqlSD = new java.sql.Date(new java.util.Date().getTime());
			sqlED = new java.sql.Date(new java.util.Date().getTime());
		}

        jdbcTemplate.update(sql, newId, Duration, LName, Cost, sqlSD, sqlED, StudentID, RoomID);
        return "addLease";
    }

    //This page displays a list of all rooms and their current rent
    //Users can give input which then updates the room rent
	@GetMapping("/updateRent")
    public String updateRent(Model model){
       	String sql = "select RoomID, RoomNum, Rent, hallname from room join residence_hall using (hallid)";
		String sql2 = "select RoomID, RoomNum, Rent, apartmentid from room join apartment using (apartmentid)";
		
		List<RoomDTO> rooms = jdbcTemplate.query(sql, new RowMapper<RoomDTO>() {
			public RoomDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
				RoomDTO rm = new RoomDTO(rs.getInt("RoomID"), rs.getInt("RoomNum"), rs.getInt("Rent"));
				rm.setLocation(rs.getString("hallname"));
				return rm;
			}
		});

		List<RoomDTO> apt = jdbcTemplate.query(sql2, new RowMapper<RoomDTO>() {
            public RoomDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
                RoomDTO rm = new RoomDTO(rs.getInt("RoomID"), rs.getInt("RoomNum"), rs.getInt("Rent"));
                rm.setLocation(rs.getString("apartmentid") + " - Apartment");
                return rm;
            }
        });

		for(RoomDTO rm : apt) {
			rooms.add(rm);
		}

		model.addAttribute("rooms", rooms);
       	return "updateRent";               
    }

    //Takes the RoomID of a room and a new rent value and updates that room in the table
    //Only affects newly created leases
	@GetMapping("/updateRent/update")
    public String updateRoomRent(@RequestParam float Rent, @RequestParam int RoomID, Model model){
       	String sql = "UPDATE room SET Rent = " + Rent + " WHERE RoomID = " + RoomID;

		java.sql.Date sqlDate = new java.sql.Date(new java.util.Date().getTime());
		String leaseSql = "update lease set cost = " + Rent + " where roomid = " + RoomID + " and startdate > " + "TO_DATE('" + sqlDate + "', 'yyyy-mm-dd')"; 		

		jdbcTemplate.execute(sql);
       	jdbcTemplate.execute(leaseSql);
		return "updateRent";               
    }

	//Returns list of students that the user can choose to delete
	@GetMapping("/deleteStudent")
    public String deleteStudent(Model model){
		String sql = "select StudentID, StuName from student";
		List<StudentDTO> info = jdbcTemplate.query(sql, new RowMapper<StudentDTO>() {
			public StudentDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
				return new StudentDTO(rs.getInt("StudentID"), rs.getString("StuName"));
			}
		});

		model.addAttribute("info", info);
       	return "deleteStudent";        
    }

    //Takes the ID of a student to delete
    //Cascades down tables that use the student as a foreign key and deletes in the proper order
	@GetMapping("/deleteStudent/delete")
    public String postLease(@RequestParam int StudentID, Model model) {
        String sql1 = "DELETE FROM Student where StudentID = " + StudentID;
		String sql2 = "DELETE FROM Lease where StudentID = " + StudentID;
		String sql3 = "DELETE FROM Invoice where LeaseID in (select LeaseID from Lease where StudentID = " + StudentID + " )";
		//First remove all related invoices
		jdbcTemplate.execute(sql3);
		//Then remove all related leases
        jdbcTemplate.execute(sql2);
        //Finally, remove the student
		jdbcTemplate.execute(sql1);
       		 return "deleteStudent";
   	 }
   	 //Get the info about staff and their associated hall
	@GetMapping("/hallInfo")
	public String getHallInfo(Model model) {
		String sql = "select hallname, staffname, phone from residence_hall join staff using (staffid)";
		List<HallInfoDTO> info = jdbcTemplate.query(sql, new RowMapper<HallInfoDTO>() {
			public HallInfoDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
				return new HallInfoDTO(rs.getString("hallname"), rs.getString("staffname"), rs.getString("phone"));
			}
		});

		model.addAttribute("info", info);
		return "hallInfo";	
	}

	//This returns a list of all vacant rooms and their associated information
	@GetMapping("/vacantRooms")
	public String getVacantRooms(Model model) {
		java.sql.Date sqlDate = new java.sql.Date(new java.util.Date().getTime());

		String sql = "select roomid, roomnum, hallname, rent from (select roomid from room minus select roomid from lease where startdate <= TO_DATE('" + sqlDate + "', 'yyyy-mm-dd') and enddate >= TO_DATE('" + sqlDate + "', 'yyyy-mm-dd') group by roomid) join room using (roomid) join residence_hall using (hallid)";
		String sql2 = "select roomid, roomnum, apartmentid, rent from (select roomid from room minus select roomid from lease where startdate <= TO_DATE('" + sqlDate + "', 'yyyy-mm-dd') and enddate >= TO_DATE('" + sqlDate + "', 'yyyy-mm-dd') group by roomid) join room using (roomid) join apartment using (apartmentid)";		
		String sql3 = "select count(roomid) from (select roomid from room minus select roomid from lease where startdate <= TO_DATE('" + sqlDate + "', 'yyyy-mm-dd') and enddate >= TO_DATE('" + sqlDate + "', 'yyyy-mm-dd'))";

		List<RoomDTO> rm = jdbcTemplate.query(sql, new RowMapper<RoomDTO>() {
            public RoomDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
				int id = rs.getInt("roomid");
                int rnum = rs.getInt("roomnum");
                String loc = rs.getString("hallname");
				float rent = rs.getFloat("rent");
				RoomDTO room = new RoomDTO(id, rnum, rent);
				room.setLocation(loc);
                return room;
            }
        });
		List<RoomDTO> apt = jdbcTemplate.query(sql2, new RowMapper<RoomDTO>() {
            public RoomDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
                int id = rs.getInt("roomid");
                int rnum = rs.getInt("roomnum");
                String loc = "Apartment " + rs.getString("apartmentid");
                float rent = rs.getFloat("rent");

                RoomDTO room = new RoomDTO(id, rnum, rent);
                room.setLocation(loc);

                return room;
            }
        });		

		int count = jdbcTemplate.queryForObject(sql3, Integer.class);
		for(RoomDTO temp : apt) {
			rm.add(temp);
		}
		model.addAttribute("vacant", rm);
		model.addAttribute("count", count);
		return "vacantRoom";
	}
	//Return list of all unpaid invoices and the student and room associated with them
	@GetMapping("/unpaidInvoices")
	public String unpaidInfo(Model model) {
		String sql = "select lname, paymentdue, roomnum, hallname from invoice join lease using (leaseid) join room using (roomid) join residence_hall using (hallid) where datepaid is NULL";
		String sql2 = "select lname, paymentdue, roomnum, apartmentid from invoice join lease using (leaseid) join room using (roomid) join apartment using (apartmentid) where datepaid is NULL";
		String sql3 = "select sum(paymentdue) from invoice where datepaid is NULL";		

		float sum = jdbcTemplate.queryForObject(sql3, Float.class);

		List<UnpaidInfoDTO> info = jdbcTemplate.query(sql, new RowMapper<UnpaidInfoDTO>() {
			public UnpaidInfoDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
				String lname = rs.getString("lname");
				float paydue = rs.getFloat("paymentdue");
				int rnum = rs.getInt("roomnum");
				String hname = rs.getString("hallname");
				
				return new UnpaidInfoDTO(lname, paydue, rnum, hname);
			}
		});

		List<UnpaidInfoDTO> apt = jdbcTemplate.query(sql2, new RowMapper<UnpaidInfoDTO>() {
            public UnpaidInfoDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
                String lname = rs.getString("lname");
                float paydue = rs.getFloat("paymentdue");
                int rnum = rs.getInt("roomnum");
                String hname = "Apartment " + rs.getString("apartmentid");

                return new UnpaidInfoDTO(lname, paydue, rnum, hname);
            }
        });

		for(UnpaidInfoDTO temp : apt) {
			info.add(temp);
		}
	
		model.addAttribute("sum", sum);
		model.addAttribute("unpaidInfo", info);
		return "unpaidInfo";
	}

	

	//Get the number of students associated with each living space type
	//Categorize by class year and category
	@GetMapping("/leaseInfo")
	public String getLeaseInfo(Model model) {
		java.sql.Date sqlDate = new java.sql.Date(new java.util.Date().getTime());
		
		String fyUnH = "select count(*) from lease join student using (studentid) join room using (roomid) join residence_hall using (hallid) where startdate <= TO_DATE('" + sqlDate + "', 'yyyy-mm-dd') and enddate >= TO_DATE('" + sqlDate + "', 'yyyy-mm-dd') and category = 'undergraduate' and classyear = 'first-year'";
		String fyUnA = "select count(*) from lease join student using (studentid) join room using (roomid) join apartment using (apartmentid) where startdate <= TO_DATE('" + sqlDate + "', 'yyyy-mm-dd') and enddate >= TO_DATE('" + sqlDate + "', 'yyyy-mm-dd') and category = 'undergraduate' and classyear = 'first-year'";
		String syUnH = "select count(*) from lease join student using (studentid) join room using (roomid) join residence_hall using (hallid) where startdate <= TO_DATE('" + sqlDate + "', 'yyyy-mm-dd') and enddate >= TO_DATE('" + sqlDate + "', 'yyyy-mm-dd') and category = 'undergraduate' and classyear = 'second-year'";
		String syUnA = "select count(*) from lease join student using (studentid) join room using (roomid) join apartment using (apartmentid) where startdate <= TO_DATE('" + sqlDate + "', 'yyyy-mm-dd') and enddate >= TO_DATE('" + sqlDate + "', 'yyyy-mm-dd') and category = 'undergraduate' and classyear = 'second-year'";
		String tyUnH = "select count(*) from lease join student using (studentid) join room using (roomid) join residence_hall using (hallid) where startdate <= TO_DATE('" + sqlDate + "', 'yyyy-mm-dd') and enddate >= TO_DATE('" + sqlDate + "', 'yyyy-mm-dd') and category = 'undergraduate' and classyear = 'third-year'";
		String tyUnA = "select count(*) from lease join student using (studentid) join room using (roomid) join apartment using (apartmentid) where startdate <= TO_DATE('" + sqlDate + "', 'yyyy-mm-dd') and enddate >= TO_DATE('" + sqlDate + "', 'yyyy-mm-dd') and category = 'undergraduate' and classyear = 'third-year'";
		String fryUnH = "select count(*) from lease join student using (studentid) join room using (roomid) join residence_hall using (hallid) where startdate <= TO_DATE('" + sqlDate + "', 'yyyy-mm-dd') and enddate >= TO_DATE('" + sqlDate + "', 'yyyy-mm-dd') and category = 'undergraduate' and classyear = 'fourth-year'";
		String fryUnA = "select count(*) from lease join student using (studentid) join room using (roomid) join apartment using (apartmentid) where startdate <= TO_DATE('" + sqlDate + "', 'yyyy-mm-dd') and enddate >= TO_DATE('" + sqlDate + "', 'yyyy-mm-dd') and category = 'undergraduate' and classyear = 'fourth-year'";
		String gH = "select count(*) from lease join student using (studentid) join room using (roomid) join residence_hall using (hallid) where startdate <= TO_DATE('" + sqlDate + "', 'yyyy-mm-dd') and enddate >= TO_DATE('" + sqlDate + "', 'yyyy-mm-dd') and category = 'postgraduate'";
		String gA = "select count(*) from lease join student using (studentid) join room using (roomid) join apartment using (apartmentid) where startdate <= TO_DATE('" + sqlDate + "', 'yyyy-mm-dd') and enddate >= TO_DATE('" + sqlDate + "', 'yyyy-mm-dd') and category = 'postgraduate'";
		
		int intFyUnH = jdbcTemplate.queryForObject(fyUnH, Integer.class);
		int intFyUnA = jdbcTemplate.queryForObject(fyUnA, Integer.class);
		int intSyUnH = jdbcTemplate.queryForObject(fyUnH, Integer.class);
		int intSyUnA = jdbcTemplate.queryForObject(syUnA, Integer.class);
		int intTyUnH = jdbcTemplate.queryForObject(tyUnH, Integer.class);
		int intTyUnA = jdbcTemplate.queryForObject(tyUnA, Integer.class);
		int intFryUnH = jdbcTemplate.queryForObject(fryUnH, Integer.class);
		int intFryUnA = jdbcTemplate.queryForObject(fryUnA, Integer.class);
		int intGH = jdbcTemplate.queryForObject(gH, Integer.class);
		int intGA = jdbcTemplate.queryForObject(gA, Integer.class);

		model.addAttribute("fyunh", intFyUnH);
		model.addAttribute("fyuna", intFyUnA);
		model.addAttribute("syunh", intSyUnH);
		model.addAttribute("syuna", intSyUnA);
		model.addAttribute("tyunh", intTyUnH);
		model.addAttribute("tyuna", intTyUnA);
		model.addAttribute("fryunh", intFryUnH);
		model.addAttribute("fryuna", intFryUnA);
		model.addAttribute("gh", intGH);
		model.addAttribute("ga", intGA);

		return "leaseInfo";	
	}
}
