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
    public String postStudent(@RequestParam int StudentID, @RequestParam String StuName, @RequestParam String Address, @RequestParam String Phone, @RequestParam String Email, @RequestParam String Gender, @RequestParam String DOB, @RequestParam String Category, @RequestParam String Major, @RequestParam String Minor, @RequestParam int AdvisorID, Model model) {
        String sql = "insert into Student values (?,?,?,?,?,?,?,?,?,?,?)";
				java.sql.Date sqlDate = new java.sql.Date(new java.util.Date().getTime());
        jdbcTemplate.update(sql, StudentID, StuName, Address, Phone, Email, Gender,DOB, Category, Major, Minor, AdvisorID);
        return "addPerson";
    }
		
		@GetMapping("/addPerson/postStaff")
    public String postStaff(@RequestParam int StaffID, @RequestParam String StaffName, @RequestParam String Email, @RequestParam String Address, @RequestParam String DOB, @RequestParam String Gender, @RequestParam String Title, @RequestParam String Location, Model model) {
        String sql = "insert into Staff values (?,?,?,?,?,?,?,?)";
				java.sql.Date sqlDate = new java.sql.Date(new java.util.Date().getTime());
        jdbcTemplate.update(sql,StaffID, StaffName, Email, Address, sqlDate, Gender, Title, Location);
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
    public String updateRent(){
        return "updateRent";        
    }
		
		@GetMapping("/deleteStudent")
    public String deleteStudent(){
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
}
