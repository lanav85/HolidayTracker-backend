package com.HolidayTracker.fullstackbackend;
import com.HolidayTracker.fullstackbackend.model.HolidaysRequest;
import com.HolidayTracker.fullstackbackend.model.User;
import com.HolidayTracker.fullstackbackend.repository.holidayRequests.RequestDAOImpl;
import com.HolidayTracker.fullstackbackend.repository.user.UserDaoImpl;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.SQLException;
import java.util.List;

@SpringBootApplication
public class FullstackBackendApplication {

    public static void main(String[] args) throws SQLException {
        RequestDAOImpl userDAO = new RequestDAOImpl();
        HolidaysRequest deleteUser = userDAO.get(3);
        System.out.println(deleteUser);

        int result = userDAO.delete(deleteUser);
        System.out.println(result);
    }

    }
  /*//=======================================================
//CREATE INSTANCE TO TEST DAO - DELETE
        UserDaoImpl userDAO = new UserDaoImpl();
        User deleteUser = userDAO.get(5);
        System.out.println(deleteUser);

        int result = userDAO.delete(deleteUser);
        System.out.println(result);

//=======================================================
//CREATE INSTANCE TO TEST DAO - UPDATE

        UserDao userDAO = new UserDaoImpl();
        User updatedUser = new User(10,"maryjane@gmail.com", "IT", "{\"age\": 28, \"city\": \"Galway\"}", 2, "Employee", 45);
        userDAO.update (updatedUser);

        System.out.println(updatedUser);
//=======================================================
//CREATE INSTANCE TO TEST DAO - INSERT
        UserDao userDAO = new UserDaoImpl();
        User user = new User("lanaviana.lo@gmail.com", "IT", "{\"age\": 28, \"city\": \"Galway\"}", 2, "Employee", 45);
        int result = userDAO.insert(user);

        System.out.println(result);

//=======================================================
//CREATE INSTANCE TO TEST DAO - Retrieve all
        UserDao userDAO = new UserDaoImpl();
        List<User> user = userDAO.getAll();
        System.out.println(user);
//=======================================================
//CREATE INSTANCE TO TEST DAO - Retrieve    public static void main(String[] args) throws SQLException {

        UserDao userDAO = new UserDaoImpl();
        User user = userDAO.get(3);
        System.out.println(user);
//=======================================================

Connection con = Database.getConnection();
if (con !=null){
	System.out.println("Database connection successful");

//=======================================================
// Create an instance of TestDB and populate it with data

		ApplicationContext context = SpringApplication.run(FullstackBackendApplication.class, args);
		TestDB test = new TestDB();
		test.setTestName("Sample Test");
		test.setTestHour(5);

		// Get the TestDBRepo bean and save the test data
		TestDBRepo testDBRepo = context.getBean(TestDBRepo.class);
		testDBRepo.save(test);

		// Print all test data from the database
		System.out.println(testDBRepo.findAll());
*/