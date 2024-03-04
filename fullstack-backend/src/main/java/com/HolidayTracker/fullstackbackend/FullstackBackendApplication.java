package com.HolidayTracker.fullstackbackend;

import com.HolidayTracker.fullstackbackend.model.TestDB;
import com.HolidayTracker.fullstackbackend.model.User;
import com.HolidayTracker.fullstackbackend.repository.Database;
import com.HolidayTracker.fullstackbackend.repository.TestDBRepo;
import com.HolidayTracker.fullstackbackend.repository.UserDao;
import com.HolidayTracker.fullstackbackend.repository.UserDaoImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@SpringBootApplication
public class FullstackBackendApplication {

    public static void main(String[] args) throws SQLException {
        UserDao userDAO = new UserDaoImpl();
        User user = new User(0, "lanaviana.o@gmail.com", "IT", "{\"age\": 28, \"city\": \"Galway\"}", 2, "Employee", 45);
        int result = userDAO.insert(user);

        System.out.println(result);
    }
}
/*//=======================================================
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

	/*public static void main(String[] args) throws SQLException {
Connection con = Database.getConnection();
if (con !=null){
	System.out.println("Database connection successful");
}
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



