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


