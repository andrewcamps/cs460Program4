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

/**
 * Authors: Garrett Seale, Andrew Camps
 * CSC460
 * Program 4
 * MainController.java: This controller houses all of our endpoints that return html pages and data from our queries.
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

    /**
	 * Navigate to page that allows the addition of students/staff to the DB
	 */
	@GetMapping("/addStudent")
    public String addStudent(Model model) {
		String sql = "select advisorid, advname, department from advisor";

		/* Maps the tuples from a query to an advisor object */
		List<AdvisorDTO> adv = jdbcTemplate.query(sql, new RowMapper<AdvisorDTO>() {
            public AdvisorDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
				int id = rs.getInt("advisorid");
				String info = rs.getString("advname") + " (" + rs.getString("department") + ")";
			
                return new AdvisorDTO(id, info);
            }
        });
		
		/* Maps to the model */
		model.addAttribute("advisor", adv);
        return "addStudent";        
    }

	/**
	 * The get request for the add staff page
	 */
	@GetMapping("/addStaff")
	public String addStaff() {
		return "addStaff";
	}
	
	/**
     * Navigate to page that allows the addition of leases to the DB
     */
	@GetMapping("/addLease")
    public String addLease(Model model){
		String sql = "select stuname, studentid from student";
		String sql2 = "select roomid, roomnum, rent, hallname from room join residence_hall using (hallid)";
        String sql3 = "select roomid, roomnum, rent, apartmentid from room join apartment using (apartmentid)";

		/* Queries database and maps attributes to an object list */
        List<StudentDTO> stu = jdbcTemplate.query(sql, new RowMapper<StudentDTO>() {
            public StudentDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
                int id = rs.getInt("studentid");
                String info = rs.getString("stuname") + " - " + id;

                return new StudentDTO(id, info);
            }
        });

		/* Queries database and maps attributes to an object list */
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

		/* Queries database and maps attributes to an object list */
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

		/* Combines two lists together */
		for(RoomDTO rm : apt) {
			rh.add(rm);
		}
		
		/* Maps to the model */
        model.addAttribute("student", stu);
        model.addAttribute("room", rh);
		return "addLease";        
    }

	/**
     * Return pages that accepts a HallID
	 */
	@GetMapping("/roomInspections")
    public String roomsList(Model model){
		String sql = "select roomid, roomnum, rent, hallname from room join residence_hall using (hallid)";
        String sql2 = "select roomid, roomnum, rent, apartmentid from room join apartment using (apartmentid)";

		/* Queries database and maps attributes to an object list */
		List<RoomDTO> rh = jdbcTemplate.query(sql, new RowMapper<RoomDTO>() {
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

		/* Queries database and maps attributes to an object list */
        List<RoomDTO> apt = jdbcTemplate.query(sql2, new RowMapper<RoomDTO>() {
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

		/* Comines the lists together */
        for(RoomDTO rm : apt) {
            rh.add(rm);
        }

		/* Maps to the model */
		model.addAttribute("room", rh);
        return "roomInspections";        
    }

	/** 
     * After a HallID is provided to the roomList page, return this page which gives a list of RoomDTOs and displays them as a table
     */
	@GetMapping("/roomInspections/getInspections")
    public String getRooms(@RequestParam String RoomID, Model model){
    	/* Select rooms that match the HallID */
		String sql = "select inspectionid, dateinspected, staffname, satisfactorycondition, action from inspection join staff using (staffid) where roomid = " + RoomID;

		List<InspectionDTO> insp = jdbcTemplate.query(sql, new RowMapper<InspectionDTO>() {
			public InspectionDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
				return new InspectionDTO(rs.getInt("inspectionid"), rs.getString("dateinspected"), rs.getString("staffname"), rs.getString("satisfactorycondition"), rs.getString("action"));
			}
		});

		/* Maps to the model */
		model.addAttribute("inspection", insp);
       	return "getInspections";               
    }
	
	/**
     * Takes all of the attributes of a Student as parameters and inserts that student into the DB
   	 */
	@GetMapping("/addStudent/post")
    public String postStudent(@RequestParam String StuName, @RequestParam String Address, @RequestParam String Phone, @RequestParam String Email, @RequestParam String Gender, @RequestParam String DOB, @RequestParam String Category, @RequestParam String ClassYear, @RequestParam String Major, @RequestParam String Minor, @RequestParam int AdvisorID, Model model) {	
		String sql = "insert into Student values (?,?,?,?,?,?,?,?,?,?,?,?)";
		String sqlId = "select max(studentid) from student";
		int newId = jdbcTemplate.queryForObject(sqlId, Integer.class) + 1;
			     
		jdbcTemplate.update(sql, newId, StuName, Address, Phone, Email, Gender, DOB, Category, ClassYear, Major, Minor, AdvisorID);
        return "addStudent";
    }
	
	/**
	 * Takes all of the attributes of a Staff as parameters and inserts that staff into the DB
	 */
	@GetMapping("/addStaff/post")
    public String postStaff(@RequestParam String StaffName, @RequestParam String Email, @RequestParam String Address, @RequestParam String DOB, @RequestParam String Gender, @RequestParam String Title, @RequestParam String Location, Model model) {
        String sql = "insert into Staff values (?,?,?,?,?,?,?,?)";
		String sqlId = "select max(staffid) from staff";
		int newId = jdbcTemplate.queryForObject(sqlId, Integer.class) + 1;

		/* Sends update to database */
        jdbcTemplate.update(sql,newId, StaffName, Email, Address, DOB, Gender, Title, Location);
        return "addStaff";
    }

	/**
     * Takes the attributes of a lease as input and inserts them into the DB
	 */
	@GetMapping("/addLease/post")
    public String postLease(@RequestParam int Duration, @RequestParam String LName, @RequestParam float Cost, @RequestParam String StartDate, @RequestParam String EndDate, @RequestParam int StudentID, @RequestParam int RoomID, Model model) {
        String sql = "insert into Lease values (?,?,?,?,?,?,?,?)";
		String sqlId = "select max(leaseid) from lease";
        int newId = jdbcTemplate.queryForObject(sqlId, Integer.class) + 1;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        java.sql.Date sqlSD;
		java.sql.Date sqlED;

		/* Parses the date entered and ensures correct format */
		try {
			java.util.Date SD = format.parse(StartDate);
        	java.util.Date ED = format.parse(EndDate);
        	sqlSD = new java.sql.Date(SD.getTime());
        	sqlED = new java.sql.Date(ED.getTime());
		} catch (Exception e) {
			sqlSD = new java.sql.Date(new java.util.Date().getTime());
			sqlED = new java.sql.Date(new java.util.Date().getTime());
		}

		/* Send database update */
        jdbcTemplate.update(sql, newId, Duration, LName, Cost, sqlSD, sqlED, StudentID, RoomID);
        return "addLease";
    }

	/** 
     * This page displays a list of all rooms and their current rent
     * Users can give input which then updates the room rent
	 */
	@GetMapping("/updateRent")
    public String updateRent(Model model){
       	String sql = "select RoomID, RoomNum, Rent, hallname from room join residence_hall using (hallid)";
		String sql2 = "select RoomID, RoomNum, Rent, apartmentid from room join apartment using (apartmentid)";
		
		/* Maps room tuples to a room object */
		List<RoomDTO> rooms = jdbcTemplate.query(sql, new RowMapper<RoomDTO>() {
			public RoomDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
				RoomDTO rm = new RoomDTO(rs.getInt("RoomID"), rs.getInt("RoomNum"), rs.getInt("Rent"));
				rm.setLocation(rs.getString("hallname"));
				return rm;
			}
		});

		/* Maps apartment tuples to an object */
		List<RoomDTO> apt = jdbcTemplate.query(sql2, new RowMapper<RoomDTO>() {
            public RoomDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
                RoomDTO rm = new RoomDTO(rs.getInt("RoomID"), rs.getInt("RoomNum"), rs.getInt("Rent"));
                rm.setLocation(rs.getString("apartmentid") + " - Apartment");
                return rm;
            }
        });

		/* Loop to combine lists into one */
		for(RoomDTO rm : apt) {
			rooms.add(rm);
		}
	
		/* Maps to the model */
		model.addAttribute("rooms", rooms);
       	return "updateRent";               
    }

	/**
     * Takes the RoomID of a room and a new rent value and updates that room in the table
     * Only affects newly created leases
	 */
	@GetMapping("/updateRent/update")
    public String updateRoomRent(@RequestParam float Rent, @RequestParam int RoomID, Model model){
       	String sql = "UPDATE room SET Rent = " + Rent + " WHERE RoomID = " + RoomID;
		
		/* Gets current date for comparison */
		java.sql.Date sqlDate = new java.sql.Date(new java.util.Date().getTime());
		String leaseSql = "update lease set cost = " + Rent + " where roomid = " + RoomID + " and startdate > " + "TO_DATE('" + sqlDate + "', 'yyyy-MM-dd')"; 		
		
		/* Execute and update to tuples */
		jdbcTemplate.execute(sql);
       	jdbcTemplate.execute(leaseSql);
		return "updateRent";               
    }

	/**
	 * Returns list of students that the user can choose to delete
	 */
	@GetMapping("/deleteStudent")
    public String deleteStudent(Model model){
		String sql = "select StudentID, StuName from student";
		
		/* Maps students to a list of objects while a query is performed */
		List<StudentDTO> info = jdbcTemplate.query(sql, new RowMapper<StudentDTO>() {
			public StudentDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
				return new StudentDTO(rs.getInt("StudentID"), rs.getString("StuName"));
			}
		});

		/* Maps to the model */
		model.addAttribute("info", info);
       	return "deleteStudent";        
    }

	/**
     * Takes the ID of a student to delete
     * Cascades down tables that use the student as a foreign key and deletes in the proper order
	 */
	@GetMapping("/deleteStudent/delete")
    public String postLease(@RequestParam int StudentID, Model model) {
        String sql1 = "DELETE FROM Student where StudentID = " + StudentID;
		String sql2 = "DELETE FROM Lease where StudentID = " + StudentID;
		String sql3 = "DELETE FROM Invoice where LeaseID in (select LeaseID from Lease where StudentID = " + StudentID + " )";
		/* First remove all related invoices */
		jdbcTemplate.execute(sql3);
		/* Then remove all related leases */
        jdbcTemplate.execute(sql2);
        /* Finally, remove the student */
		jdbcTemplate.execute(sql1);
       	return "deleteStudent";
   	 }

	/**
   	 * Get the info about staff and their associated hall
	 */
	@GetMapping("/hallInfo")
	public String getHallInfo(Model model) {
		String sql = "select hallname, staffname, phone from residence_hall join staff using (staffid)";
		
		/* Maps the info from the hall to an object during query */
		List<HallInfoDTO> info = jdbcTemplate.query(sql, new RowMapper<HallInfoDTO>() {
			public HallInfoDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
				return new HallInfoDTO(rs.getString("hallname"), rs.getString("staffname"), rs.getString("phone"));
			}
		});

		/* Maps to the model */
		model.addAttribute("info", info);
		return "hallInfo";	
	}

	/**
	 * This returns a list of all vacant rooms and their associated information
	 */
	@GetMapping("/vacantRooms")
	public String getVacantRooms(Model model) {
		java.sql.Date sqlDate = new java.sql.Date(new java.util.Date().getTime());

		String sql = "select roomid, roomnum, hallname, rent from (select roomid from room minus select roomid from lease where startdate <= TO_DATE('" + sqlDate + "', 'yyyy-MM-dd') and enddate >= TO_DATE('" + sqlDate + "', 'yyyy-MM-dd') group by roomid) join room using (roomid) join residence_hall using (hallid)";
		String sql2 = "select roomid, roomnum, apartmentid, rent from (select roomid from room minus select roomid from lease where startdate <= TO_DATE('" + sqlDate + "', 'yyyy-MM-dd') and enddate >= TO_DATE('" + sqlDate + "', 'yyyy-MM-dd') group by roomid) join room using (roomid) join apartment using (apartmentid)";		
		String sql3 = "select count(roomid) from (select roomid from room minus select roomid from lease where startdate <= TO_DATE('" + sqlDate + "', 'yyyy-MM-dd') and enddate >= TO_DATE('" + sqlDate + "', 'yyyy-MM-dd'))";

		/* Maps queried tuples to a list of room objects */
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
		
		/* Maps tuples to a list of apartment objects */
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

		/* Query and loop to attach tuple to a single list */
		int count = jdbcTemplate.queryForObject(sql3, Integer.class);
		for(RoomDTO temp : apt) {
			rm.add(temp);
		}

		/* Maps to the model */
		model.addAttribute("vacant", rm);
		model.addAttribute("count", count);
		return "vacantRoom";
	}

	/**
	 * Return list of all unpaid invoices and the student and room associated with them
	 */
	@GetMapping("/unpaidInvoices")
	public String unpaidInfo(Model model) {
		String sql = "select lname, paymentdue, roomnum, hallname from invoice join lease using (leaseid) join room using (roomid) join residence_hall using (hallid) where datepaid is NULL";
		String sql2 = "select lname, paymentdue, roomnum, apartmentid from invoice join lease using (leaseid) join room using (roomid) join apartment using (apartmentid) where datepaid is NULL";
		String sql3 = "select sum(paymentdue) from invoice where datepaid is NULL";		

		float sum = jdbcTemplate.queryForObject(sql3, Float.class);

		/* Maps tuples to a list of rooms */
		List<UnpaidInfoDTO> info = jdbcTemplate.query(sql, new RowMapper<UnpaidInfoDTO>() {
			public UnpaidInfoDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
				String lname = rs.getString("lname");
				float paydue = rs.getFloat("paymentdue");
				int rnum = rs.getInt("roomnum");
				String hname = rs.getString("hallname");
				
				return new UnpaidInfoDTO(lname, paydue, rnum, hname);
			}
		});

		/* Maps tuples to an apartment object */
		List<UnpaidInfoDTO> apt = jdbcTemplate.query(sql2, new RowMapper<UnpaidInfoDTO>() {
            public UnpaidInfoDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
                String lname = rs.getString("lname");
                float paydue = rs.getFloat("paymentdue");
                int rnum = rs.getInt("roomnum");
                String hname = "Apartment " + rs.getString("apartmentid");

                return new UnpaidInfoDTO(lname, paydue, rnum, hname);
            }
        });

		/* Loops through all the list to add to main list */
		for(UnpaidInfoDTO temp : apt) {
			info.add(temp);
		}
	
		/* Maps to the model */
		model.addAttribute("sum", sum);
		model.addAttribute("unpaidInfo", info);
		return "unpaidInfo";
	}

	
	/**
	 * Gets the number of students associated with each living space type
	 * categorized by class year and categor
	 */
	@GetMapping("/leaseInfo")
	public String getLeaseInfo(Model model) {
		java.sql.Date sqlDate = new java.sql.Date(new java.util.Date().getTime());
		
		String fyUnH = "select count(hallid) from lease join student using (studentid) join room using (roomid) join residence_hall using (hallid) where startdate <= TO_DATE('" + sqlDate + "', 'yyyy-MM-dd') and enddate >= TO_DATE('" + sqlDate + "', 'yyyy-MM-dd') and category = 'undergraduate' and classyear = 'first-year'";
		String fyUnA = "select count(apartmentid) from lease join student using (studentid) join room using (roomid) join apartment using (apartmentid) where startdate <= TO_DATE('" + sqlDate + "', 'yyyy-MM-dd') and enddate >= TO_DATE('" + sqlDate + "', 'yyyy-MM-dd') and category = 'undergraduate' and classyear = 'first-year'";
		String syUnH = "select count(hallid) from lease join student using (studentid) join room using (roomid) join residence_hall using (hallid) where startdate <= TO_DATE('" + sqlDate + "', 'yyyy-MM-dd') and enddate >= TO_DATE('" + sqlDate + "', 'yyyy-MM-dd') and category = 'undergraduate' and classyear = 'second-year'";
		String syUnA = "select count(apartmentid) from lease join student using (studentid) join room using (roomid) join apartment using (apartmentid) where startdate <= TO_DATE('" + sqlDate + "', 'yyyy-MM-dd') and enddate >= TO_DATE('" + sqlDate + "', 'yyyy-MM-dd') and category = 'undergraduate' and classyear = 'second-year'";
		String tyUnH = "select count(hallid) from lease join student using (studentid) join room using (roomid) join residence_hall using (hallid) where startdate <= TO_DATE('" + sqlDate + "', 'yyyy-MM-dd') and enddate >= TO_DATE('" + sqlDate + "', 'yyyy-MM-dd') and category = 'undergraduate' and classyear = 'third-year'";
		String tyUnA = "select count(apartmentid) from lease join student using (studentid) join room using (roomid) join apartment using (apartmentid) where startdate <= TO_DATE('" + sqlDate + "', 'yyyy-MM-dd') and enddate >= TO_DATE('" + sqlDate + "', 'yyyy-MM-dd') and category = 'undergraduate' and classyear = 'third-year'";
		String fryUnH = "select count(hallid) from lease join student using (studentid) join room using (roomid) join residence_hall using (hallid) where startdate <= TO_DATE('" + sqlDate + "', 'yyyy-MM-dd') and enddate >= TO_DATE('" + sqlDate + "', 'yyyy-MM-dd') and category = 'undergraduate' and classyear = 'fourth-year'";
		String fryUnA = "select count(apartmentid) from lease join student using (studentid) join room using (roomid) join apartment using (apartmentid) where startdate <= TO_DATE('" + sqlDate + "', 'yyyy-MM-dd') and enddate >= TO_DATE('" + sqlDate + "', 'yyyy-MM-dd') and category = 'undergraduate' and classyear = 'fourth-year'";
		String gH = "select count(hallid) from lease join student using (studentid) join room using (roomid) join residence_hall using (hallid) where startdate <= TO_DATE('" + sqlDate + "', 'yyyy-MM-dd') and enddate >= TO_DATE('" + sqlDate + "', 'yyyy-MM-dd') and category = 'postgraduate'";
		String gA = "select count(apartmentid) from lease join student using (studentid) join room using (roomid) join apartment using (apartmentid) where startdate <= TO_DATE('" + sqlDate + "', 'yyyy-MM-dd') and enddate >= TO_DATE('" + sqlDate + "', 'yyyy-MM-dd') and category = 'postgraduate'";
		
		/* Queries the database for a single value int */
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

		/* Maps to the model */
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
