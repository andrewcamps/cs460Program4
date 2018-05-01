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

	@GetMapping("/unpaidInvoices")
	public String getUnpaidInvoices(Model model) {
		String sql1 = "select lname, paymentdue, roomnum, hallname from (select leaseid, paymentdue, datepaid from invoice minus select";
		sql1 += " leaseid, paymentdue, datepaid from invoice where datepaid < to_date('01-01-5000', 'DD-MM-YYYY')) join lease using (leaseid)";
		sql1 += " join room using (roomid) join residence_hall using (hallid) union select lname, paymentdue, roomnum, NULL from (select";
		sql1 += " leaseid, paymentdue, datepaid from invoice minus select leaseid, paymentdue, datepaid from invoice where datepaid";
		sql1 += " < to_date('01-01-5000', 'DD-MM-YYYY')) join lease using (leaseid) join room using (roomid) join apartment using (apartmentid)";

		String sql2 = "select sum(paymentdue) from (select leaseid, paymentdue, datepaid from invoice minus select leaseid, paymentdue, datepaid from invoice where datepaid < to_date('01-01-5000', 'DD-MM-YYYY')) join lease using (leaseid) join room using (roomid)";

	//	List<UnpaidInfo> payInfo = jdbcTemplate.query(sql1, new RowMapper<UnpaidInfo>() {
	//		public UnpaidInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
	//			return new UnpaidInfo(rs.getString("lname"), rs.getInt("paymentdue"), rs.getInt("roomnum"), rs.getString("hallname"));
	//		}
	//	});

//		String sum = (String) jdbcTemplate.query(sql2, String.class);

//		System.out.println(payInfo.size());
		System.out.println(sum);

	//	model.addArribute("info", info);
	//	model.addArribute("sum", sum);
		return "unpaidInfo";
	}	


}
