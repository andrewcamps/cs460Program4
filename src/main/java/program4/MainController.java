package program4;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
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
				
				System.out.println(StudentID + " " + FirstName + " " + Gender);
				String date = new java.sql.Date(new java.util.Date().getTime()).toString();
        jdbcTemplate.update(sql, StudentID, FirstName, LastName, Address, Phone, Email, Gender, date, Category, ClassYear, Major, Minor, AdvisorID);
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

}


