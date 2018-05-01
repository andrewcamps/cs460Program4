package program4;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
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
    public String postStudent(@RequestParam int StudentID, @RequestParam String FirstName, @RequestParam String LastName, @RequestParam String Address, @RequestParam String Phone, @RequestParam String Email, @RequestParam String Gender, @RequestParam String DOB, @RequestParam String Category, @RequestParam String ClassYear, @RequestParam String Major, @RequestParam String Minor, @RequestParam int AdvisorID, Model model) {
        String sql = "insert into Student values (?,?,?,?,?,?,?,?,?,?,?,?,?)";
				java.sql.Date sqlDate = new java.sql.Date(new java.util.Date().getTime());
        jdbcTemplate.update(sql, StudentID, FirstName, LastName, Address, Phone, Email, Gender, sqlDate, Category, ClassYear, Major, Minor, AdvisorID);
        return "addPerson";
    }
		
		@GetMapping("/addPerson/postStaff")
    public String postStaff(@RequestParam int StaffID, @RequestParam String FirstName, @RequestParam String LastName, @RequestParam String Email, @RequestParam String Address, @RequestParam String DOB, @RequestParam String Gender, @RequestParam String Title, @RequestParam String Location, Model model) {
        String sql = "insert into Staff values (?,?,?,?,?,?,?,?,?)";
				java.sql.Date sqlDate = new java.sql.Date(new java.util.Date().getTime());
        jdbcTemplate.update(sql,StaffID, FirstName, LastName, Email, Address, sqlDate, Gender, Title, Location);
        return "addPerson";
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
		public String Info(){
			List<String> info = getHallInfo();
			
			return "hallInfo";
		}
		
		/* */
		public List<String> getHallInfo() {
			String sql = "select name, firstname, lastname, phone from andrewcamps.residence_hall join andrewcamps.staff using (staffid)";
			
			List hallList = jdbcTemplate.query(sql, new ResultSetExtractor<List<String>>() {
				@Override
				public List<String> extractData(ResultSet rs) throws SQLException, DataAccessException {
				    List<String> list = new ArrayList<String>();
				    while (rs.next()){
				        String info = "";
								info += rs.getString("NAME");
								info += rs.getString("FIRSTNAME");
								info += rs.getString("LASTNAME");
								info += rs.getString("PHONE");
								
								System.out.println(info);
				        list.add(info);
				    }
				    return list;
				}
      });
			
			System.out.println("Done");
			
      return hallList;
		}
}




