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


    @GetMapping("/greeting")
    public String greeting(@RequestParam(name="name", required=false, defaultValue="World") String name, Model model) {
        model.addAttribute("name", name);
        String sql = "insert into people values (?)";
        jdbcTemplate.update(sql, name);
        return "greeting";
    }

@GetMapping("/addPerson")
    public String addPerson(){
        return "addPerson";        
    }


   @GetMapping("/addPerson/postStudent")
    public String postStudent(@RequestParam int StudentID, @RequestParam String Name, @RequestParam String Address, @RequestParam String Phone, @RequestParam String Email, @RequestParam String Gender, @RequestParam String DOB, @RequestParam String Category, @RequestParam String Major, @RequestParam String Minor, @RequestParam int AdvisorID, Model model) {
        String sql = "insert into Student values (?,?,?,?,?,?,?,?,?,?,?)";
	java.sql.Date sqlDate = new java.sql.Date(new java.util.Date().getTime());
        jdbcTemplate.update(sql, StudentID, Name, Address, Phone, Email, Gender,sqlDate, Category, Major, Minor, AdvisorID);
        return "addPerson";
    }
@GetMapping("/addPerson/postStaff")
    public String postStaff(@RequestParam int StaffID, @RequestParam String Name, @RequestParam String Email, @RequestParam String Address, @RequestParam String DOB, @RequestParam String Gender, @RequestParam String Title, @RequestParam String Location, Model model) {
        String sql = "insert into Staff values (?,?,?,?,?,?,?,?)";
	java.sql.Date sqlDate = new java.sql.Date(new java.util.Date().getTime());
        jdbcTemplate.update(sql,StaffID, Name, Email, Address, sqlDate, Gender, Title, Location);
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


