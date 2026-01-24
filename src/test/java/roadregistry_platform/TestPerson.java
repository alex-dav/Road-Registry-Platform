package roadregistry_platform;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

import org.junit.jupiter.api.Test;

class TestPerson {

	// Check the function with valid personID, address and birthday formats
	@Test
	void testAddPerson_testCase1() throws IOException {
		Person person = new Person("56s_d%&fAB", "John", "Smith", "32|Highland Street|Melbourne|Victoria|Australia", "15-11-1990");
		assertTrue(person.addPerson());
	}
	
	// Check the function with invalid personID format
	@Test
	void testAddPerson_testCase2() throws IOException {
		Person person = new Person("6s_d%&fAB", "John", "Smith", "32|Highland Street|Melbourne|Victoria|Australia", "15-11-1990");
		assertFalse(person.addPerson());
	}
	
	// Check the function with invalid address format
	@Test
	void testAddPerson_testCase3() throws IOException {
		Person person = new Person("56s_d%&fAB", "John", "Smith", "One|Highland Street|Melbourne|Victoria|Australia", "15-11-1990");
		assertFalse(person.addPerson());
	}
	
	// Check the function with invalid birthday format
	@Test
	void testAddPerson_testCase4() throws IOException {
		Person person = new Person("56s_d%&fAB", "John", "Smith", "32|Highland Street|Melbourne|Victoria|Australia", "15/11/1990");
		assertFalse(person.addPerson());
	}
	
	// Check the function with invalid personID and address formats
	@Test
	void testAddPerson_testCase5() throws IOException {
		Person person = new Person("16s_d%&fAB", "John", "Smith", "32|HighlandStreet|Melbourne|Victoria|Australia", "15-11-1990");
		assertFalse(person.addPerson());
	}

	// TESTS for method updatePersonalDetails()
	// TEST 1, if age < 18, address cannot be changed
	@Test
	void testUpdatePersonalDetails_testCase1(){
		Person person = new Person("56s_d%&fAB", "John", "Smith", "32|Highland Street|Melbourne|Victoria|Australia", "29-05-2010");
		assertFalse(person.updatePersonalDetails(person));
	}
	// TEST 2, if age >= 18, address can be changed
	@Test
	void testUpdatePersonalDetails_testCase2(){
		Person person = new Person("56s_d%&fAB", "John", "Smith", "32|Highland Street|Melbourne|Victoria|Australia", "19-05-2000");
		assertTrue(person.updatePersonalDetails(person));
	}
	// TEST 3, if person's birthday is to be changed then personal details cannot be changed
	@Test
	void testUpdatePersonalDetails_testCase3(){
		Person oldPerson = new Person("56s_d%&fAB", "John", "Smith", "32|Highland Street|Melbourne|Victoria|Australia", "19-05-2000");
		try {
			oldPerson.addPersonToTxtFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Person newPerson = new Person("56s_d%&fAB", "John", "Smith", "32|Highland Street|Melbourne|Victoria|Australia", "21-06-2021");
		assertFalse(oldPerson.updatePersonalDetails(newPerson));
	}
	// TEST 4, if person's birthday is not to be changed, then personal details can be changed
	@Test
	void testUpdatePersonalDetails_testCase4(){
		Person oldPerson = new Person("56s_d%&fAB", "John", "Smith", "32|Highland Street|Melbourne|Victoria|Australia", "19-05-2000");
		try {
			oldPerson.addPersonToTxtFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Person newPerson = new Person("56s_d%&fAB", "John", "Smith", "32|Highland Street|Melbourne|Victoria|Australia", "19-05-2000");
		assertTrue(oldPerson.updatePersonalDetails(newPerson));
	}
	// TEST 5, if first digit of person's ID if even, ID cannot be changed
	@Test
	void testUpdatePersonalDetails_testCase5(){
		Person person = new Person("86s_d%&fAB", "John", "Smith", "32|Highland Street|Melbourne|Victoria|Australia", "19-05-2000");
		assertFalse(person.updatePersonalDetails(person));
	}
	// TEST 6, if first digit of person's ID is odd, ID can be changed
	// REMOVED due to number of test constraints = 5, tested in TEST 2, if first digit personalID is odd, return true.
	
	//TEST 1, check if the infraction date is of correct length 
	@Test
	void testAddDemeritPoints_testCase1() throws IOException {
		Person badDriverPerson = new Person("56s_d%&fAB", "John", "Smith", "32|Highland Street|Melbourne|Victoria|Australia", "19-05-2000");
		assertEquals("Invalid", badDriverPerson.addDemeritPoints("03-06-25", 3));
	}
	
	//TEST 2, check if the infraction date is of correct format
	@Test
	void testAddDemeritPoints_testCase2() throws IOException {
		Person badDriverPerson = new Person("56s_d%&fAB", "John", "Smith", "32|Highland Street|Melbourne|Victoria|Australia", "19-05-2000");
		assertEquals("Invalid", badDriverPerson.addDemeritPoints("03/06/2025", 3));
	}
	
	//TEST 3, check if demerit points are recognised as false if over 6
	@Test
	void testAddDemeritPoints_testCase3() throws IOException {
		Person badDriverPerson = new Person("56s_d%&fAB", "John", "Smith", "32|Highland Street|Melbourne|Victoria|Australia", "19-05-2000");
		assertEquals("Invalid", badDriverPerson.addDemeritPoints("03-06-2025", 7));
	}
	
	//TEST 4, check if the bad driver while OVER age of 21 gets suspended for getting over 12 demerit points in 2 years
	@Test
	void testAddDemeritPoints_testCase4() throws IOException {
		Person badDriverPerson = new Person("56s_d%&fAB", "John", "Smith", "32|Highland Street|Melbourne|Victoria|Australia", "19-05-2000");
		badDriverPerson.addDemeritPoints("01-06-2025", 6);
		badDriverPerson.addDemeritPoints("02-06-2025", 6);
		badDriverPerson.addDemeritPoints("03-06-2025", 6);
		
		assertTrue(badDriverPerson.isPersonSuspended());
	}
	
	//TEST 5, check if the bad driver while UNDER age of 21 gets suspended for getting over 6 demerit points in 2 years
	@Test
	void testAddDemeritPoints_testCase5() throws IOException {
		Person badDriverPerson = new Person("56s_d%&fAB", "John", "Smith", "32|Highland Street|Melbourne|Victoria|Australia", "19-05-2010");
		badDriverPerson.addDemeritPoints("01-06-2025", 6);
		badDriverPerson.addDemeritPoints("02-06-2025", 6);
		
		assertTrue(badDriverPerson.isPersonSuspended());
	}
}
