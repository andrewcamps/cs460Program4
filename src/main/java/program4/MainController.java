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
import java.util.*;
import java.sql.*;

@Controller
public class MainController {

	@Autowired
    private DataSource dataSource;
    private JdbcTemplate jdbcTemplate;

    @PostConstruct
    private void postConstruct() {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

	@GetMapping("/addPerson")
    public String addPerson(){
        return "addPerson";        
    }

   	@GetMapping("/addPerson/postStudent")
    public String postStudent(@RequestParam int StudentID, @RequestParam String StuName, @RequestParam String Address, @RequestParam String Phone, @RequestParam String Email, @RequestParam String Gender, @RequestParam String DOB, @RequestParam String Category, @RequestParam String ClassYear, @RequestParam String Major, @RequestParam String Minor, @RequestParam int AdvisorID, Model model) {
        String sql = "insert into Student values (?,?,?,?,?,?,?,?,?,?,?,?)";
        
		jdbcTemplate.update(sql, StudentID, StuName, Address, Phone, Email, Gender, DOB, Category, ClassYear, Major, Minor, AdvisorID);
        return "addPerson";
    }
		
	@GetMapping("/addPerson/postStaff")
    public String postStaff(@RequestParam int StaffID, @RequestParam String StaffName, @RequestParam String Email, @RequestParam String Address, @RequestParam String DOB, @RequestParam String Gender, @RequestParam String Title, @RequestParam String Location, Model model) {
        String sql = "insert into Staff values (?,?,?,?,?,?,?,?)";

        jdbcTemplate.update(sql,StaffID, StaffName, Email, Address, DOB, Gender, Title, Location);
        return "addPerson";
    }

	@GetMapping("/addLease/postLease")
    public String postLease(@RequestParam int LeaseID, @RequestParam int Duration, @RequestParam String LName, @RequestParam float Cost, @RequestParam String StartDate, @RequestParam int StudentID, @RequestParam int RoomID, Model model) {
        String sql = "insert into Lease values (?,?,?,?,?,?,?)";
        jdbcTemplate.update(sql,LeaseID, Duration, LName, Cost,StartDate,StudentID,RoomID);
        return "addLease";
    }

	@GetMapping("/addLease")
    public String addLease(){
        return "addLease";        
    }
		
	@GetMapping("/updateRent")
    public String updateRent(Model model){
       		 String sql = "select RoomID, RoomNum, Rent from room";
		List<RoomDTO> rooms = jdbcTemplate.query(sql, new RowMapper<RoomDTO>() {
			public RoomDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
				return new RoomDTO(rs.getInt("RoomID"), rs.getInt("RoomNum"), rs.getInt("Rent"));
			}
		});
		model.addAttribute("rooms", rooms);
       		return "updateRent";               
    }
	@GetMapping("/updateRent/update")
    public String updateRoomRent(@RequestParam float Rent, @RequestParam int RoomID, Model model){
       		 String sql = "UPDATE room SET Rent = " + Rent + " WHERE RoomID = " + RoomID;
		 jdbcTemplate.execute(sql);
       		return "updateRent";               
    }
		
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

	@GetMapping("/deleteStudent/delete")
    	public String postLease(@RequestParam int StudentID, Model model) {
        	String sql1 = "DELETE FROM Student where StudentID = " + StudentID;
		String sql2 = "DELETE FROM Lease where StudentID = " + StudentID;
		String sql3 = "DELETE FROM Invoice where LeaseID in (select LeaseID from Lease where StudentID = " + StudentID + " )";
		jdbcTemplate.execute(sql3);
        	jdbcTemplate.execute(sql2);
		jdbcTemplate.execute(sql1);
       		 return "deleteStudent";
   	 }

	@GetMapping("/hallInfo")
	public String getHallInfo(Model model) {
		String sql = "select hallname, staffname, phone from residence_hall join staff using (staffid)";

		List<HallInfo> info = jdbcTemplate.query(sql, new RowMapper<HallInfo>() {
			public HallInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
				return new HallInfo(rs.getString("hallname"), rs.getString("staffname"), rs.getString("phone"));
			}
		});

		model.addAttribute("info", info);
		return "hallInfo";	
	}

//TODO: GET CUR Date
	@GetMapping("/vacantRooms")
	public String getVacantRooms(Model model) {
		String sql = "select roomid, roomnum, hallname, rent from (select roomid from room minus select roomid from lease where startdate <= '02-MAY-2018' and enddate >= '02-MAY-2018' group by roomid) join room using (roomid) join residence_hall using (hallid)";

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

		model.addAttribute("vacant", rm);
		model.addAttribute("count", 0);

		return "vacantRoom";
	}

	@GetMapping("/unpaidInvoices")
	public String unpaidInfo(Model model) {
		String sql = "select lname, paymentdue, roomnum, hallname from invoice join lease using (leaseid) join room using (roomid) join residence_hall using (hallid) where datepaid is NULL union select lname, paymentdue, roomnum, NULL from invoice join lease using (leaseid) join room using (roomid) join apartment using (apartmentid) where datepaid is NULL";
		String sql2 = "select sum(paymentdue) from invoice where datepaid is NULL";		

		int sum = jdbcTemplate.queryForObject(sql2, Integer.class);

		List<UnpaidInfo> info = jdbcTemplate.query(sql, new RowMapper<UnpaidInfo>() {
			public UnpaidInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
				String lname = rs.getString("lname");
				int paydue = rs.getInt("paymentdue");
				int rnum = rs.getInt("roomnum");
				String hname = rs.getString("hallname");
				
				return new UnpaidInfo(lname, paydue, rnum, hname);
			}
		});
	
		model.addAttribute("sum", sum);
		model.addAttribute("unpaidInfo", info);
		return "unpaidInfo";
	}

	@GetMapping("/leaseInfo")
	public String getLeaseInfo(Model model) {
		int fyhall = jdbcTemplate.queryForObject("select count(*) from lease l, student s, room r where s.StudentID = l.StudentID and l.RoomID = r.RoomID and r.HallID IS NOT NULL and s.ClassYear = 'first-year'",Integer.class);
		int fyapt = jdbcTemplate.queryForObject("select count(*) from student s, lease l, room r where s.StudentID = l.StudentID and l.RoomID = r.RoomID and r.ApartmentID IS NOT NULL and s.ClassYear = 'first-year'",Integer.class);
		int underhall = jdbcTemplate.queryForObject("select count(*) from student s, lease l, room r where s.StudentID = l.StudentID and l.RoomID = r.RoomID and r.HallID IS NOT NULL and s.Category = 'undergraduate'",Integer.class);
		int underapt = jdbcTemplate.queryForObject("select count(*) from student s, lease l, room r where s.StudentID = l.StudentID and l.RoomID = r.RoomID and r.ApartmentID IS NOT NULL and s.Category = 'undergraduate'",Integer.class);
		int gradhall = jdbcTemplate.queryForObject("select count(*) from student s, lease l, room r where s.StudentID = l.StudentID and l.RoomID = r.RoomID and r.HallID IS NOT NULL and s.Category = 'postgraduate'",Integer.class);
		int gradapt = jdbcTemplate.queryForObject("select count(*) from student s, lease l, room r where s.StudentID = l.StudentID and l.RoomID = r.RoomID and r.ApartmentID IS NOT NULL and s.Category = 'postgraduate'",Integer.class);
		
		model.addAttribute("leaseInfo", new LeaseInfo(fyhall,fyapt,underhall,underapt,gradhall,gradapt));
		return "leaseInfo";	
	}


}
