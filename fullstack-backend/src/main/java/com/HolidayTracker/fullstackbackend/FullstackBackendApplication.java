package com.HolidayTracker.fullstackbackend;
import com.HolidayTracker.fullstackbackend.model.TestDB;
import com.HolidayTracker.fullstackbackend.repository.TestDBRepo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class FullstackBackendApplication {

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(FullstackBackendApplication.class, args);
// Create an instance of TestDB and populate it with data
		TestDB test = new TestDB();
		test.setTestName("Sample Test");
		test.setTestHour(5);

		// Get the TestDBRepo bean and save the test data
		TestDBRepo testDBRepo = context.getBean(TestDBRepo.class);
		testDBRepo.save(test);

		// Print all test data from the database
		System.out.println(testDBRepo.findAll());

	}

}
