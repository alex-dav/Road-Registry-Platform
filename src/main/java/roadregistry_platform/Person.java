package roadregistry_platform;

import java.util.HashMap;
import java.util.Map;
import java.util.Calendar;
import java.util.Date;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.PrintWriter;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.Period;

public class Person {
	private String personID;
	private String firstName;
	private String lastName;
	private String address;
	private String birthdate;
	private HashMap<LocalDate, Integer> demeritPoints;
	private boolean isSuspended = false;
	
	// Constructor for adding person details to a TXT file
	public Person(String personID, String firstName, String lastName, String address, String birthdate) {
		this.personID = personID;
		this.firstName = firstName;
		this.lastName = lastName;
		this.address = address;
		this.birthdate = birthdate;
		this.demeritPoints = new HashMap<LocalDate, Integer>();
	}
	
	public Person(String personID, String firstName, String lastName, String address, String birthdate, HashMap<LocalDate, Integer> demeritPoints, boolean isSuspended) {
		this.personID = personID;
		this.firstName = firstName;
		this.lastName = lastName;
		this.address = address;
		this.birthdate = birthdate;
		this.demeritPoints = new HashMap<LocalDate, Integer>();
		this.isSuspended = isSuspended;
	}
	
	public boolean addPerson() throws IOException {
		int personIDLength = this.personID.length();
		int personIDSpecialCharCount = 0;
		int personIDSecondLastCharIdx = personIDLength - 2;
		int personIDLastCharIdx = personIDLength - 1;
		int personBirthdayLength = this.birthdate.length();
		char personIDFirstChar = this.personID.charAt(0);
		char personIDSecondChar = this.personID.charAt(1);
		char personIDSecondLastChar = this.personID.charAt(personIDSecondLastCharIdx);
		char personIDLastChar = this.personID.charAt(personIDLastCharIdx);
		String[] personAddressParts = this.address.split("\\|");
		String personStreetNumber = personAddressParts[0];
		String personStreet = personAddressParts[1];
		String personCity = personAddressParts[2];
		String personCountry = personAddressParts[4];
		boolean personIDIsValid = false;
		boolean personAddressIsValid = false;
		boolean personBirthdayIsValid = false;
		
		/*
		 * CONDITION 1
		 * personID has to be 10 characters long
		 * personID first and second characters have to be digits between 2 and 9
		 */
		if ((personIDLength == 10) && Character.isDigit(personIDFirstChar) && Character.isDigit(personIDSecondChar)) {
			if ((personIDFirstChar >= '2') && (personIDFirstChar <= '9') && (personIDSecondChar >= '2') && (personIDSecondChar <= '9')) {
				personIDIsValid = true;
			}
			else {
				return false;
			}
		}
		else {
			return false;
		}
		
		// personID has to have at least 2 special characters between positions 2 and 7 in the string
		for (int i = 2; i <= 7; i++) {
			char personIDChar = this.personID.charAt(i);
			if (!Character.isLetterOrDigit(personIDChar) && !Character.isWhitespace(personIDChar)) {
				personIDSpecialCharCount++;
			}
			if (personIDSpecialCharCount >= 2) {
				personIDIsValid = true;
				break;
			}
			else if (i == 7) {
				return false;
			}
		}
		
		// personID second last and last characters have to be upper case letters
		if (Character.isLetter(personIDSecondLastChar) && Character.isLetter(personIDLastChar)) {
			if (Character.isUpperCase(personIDSecondLastChar) && Character.isUpperCase(personIDLastChar)) {
				personIDIsValid = true;
			}
			else {
				return false;
			}
		}
		else {
			return false;
		}
		
		/* 
		 * CONDITION 2
		 * address has to include street number, street, city, state and country
		 * address street number has to all contain digits
		 */
		if (personAddressParts.length == 5) {
			for (int i = 0; i < personStreetNumber.length(); i++) {
				if (Character.isDigit(personStreetNumber.charAt(i))) {
					personAddressIsValid = true;
				}
				else {
					return false;
				}
			}
		}
		else {
			return false;
		}
		
		/*
		 * address street has to be at least 2 words
		 * address street has to all contain letters or whitespace characters
		 */
		if (personStreet.split(" ").length >= 2) {
			for (int i = 0; i < personStreet.length(); i++) {
				char personStreetChar = personStreet.charAt(i);
				if (Character.isLetter(personStreetChar) || Character.isWhitespace(personStreetChar)) {
					personAddressIsValid = true;
				}
				else {
					return false;
				}
			}
		}
		else {
			return false;
		}
		
		// address city has to all contain letters or whitespace characters
		for (int i = 0; i < personCity.length(); i++) {
			char personCityChar = personCity.charAt(i);
			if (Character.isLetter(personCityChar) || Character.isWhitespace(personCityChar)) {
				personAddressIsValid = true;
			}
			else {
				return false;
			}
		}
		
		// address state has to be the string "Victoria"
		if (personAddressParts[3].equals("Victoria")) {
			personAddressIsValid = true;
		}
		else {
			return false;
		}
		
		// address country has to all contain letters or whitespace characters
		for (int i = 0; i < personCountry.length(); i++) {
			char personCountryChar = personCountry.charAt(i);
			if (Character.isLetter(personCountryChar) || Character.isWhitespace(personCountryChar)) {
				personAddressIsValid = true;
			}
			else {
				return false;
			}
		}
		
		/*
		 * CONDITION 3
		 * birthday has to be 10 characters long
		 * birthday first, second, fourth, fifth, seventh, eighth, ninth and tenth characters have to be digits
		 * birthday third and sixth characters have to be a dash '-'
		 */
		if (personBirthdayLength == 10) {
			for (int i = 0; i < personBirthdayLength; i++) {
				char personBirthdayChar = this.birthdate.charAt(i);
				if ((i != 2) && (i != 5) && Character.isDigit(personBirthdayChar)) {
					personBirthdayIsValid = true;
				}
				else if (((i == 2) || (i == 5)) && (personBirthdayChar == '-')) {
					personBirthdayIsValid = true;
				}
				else {
					return false;
				}
			}
		}
		else {
			return false;
		}
		
		if (personIDIsValid && personAddressIsValid && personBirthdayIsValid) {
			this.addPersonToTxtFile();
			return true;
		}
		else {
			return false;
		}
	}
	
	public void addPersonToTxtFile() throws IOException {
		File file = new File("src" + File.separator + "main" + File.separator + "resources" + File.separator + this.personID + ".txt");
		FileOutputStream fileOStream = new FileOutputStream(file);
		PrintWriter oFileStream = new PrintWriter(fileOStream);
		
		oFileStream.println("Person ID: " + this.personID);
		oFileStream.println("Firstname: " + this.firstName);
		oFileStream.println("Lastname: " + this.lastName);
		oFileStream.println("Address: " + this.address);
		oFileStream.println("Birthdate: " + this.birthdate);
		oFileStream.println();
		
		oFileStream.close();
	}
	
	public boolean updatePersonalDetails(Person newPerson) {
		// CONDITION 1
		// If age < 18, address cannot be changed
		String birthdate = this.birthdate;
		int birthdateDay = Integer.parseInt(birthdate.substring(0, 2));
		int birthdateMonth = Integer.parseInt(birthdate.substring(3, 5));
		int birthdateYear = Integer.parseInt(birthdate.substring(6, 10));
		Calendar currentDate = Calendar.getInstance();
		int currentDay = currentDate.get(Calendar.DATE);
		int currentMonth = currentDate.get(Calendar.MONTH);
		int currentYear = currentDate.get(Calendar.YEAR);
		Boolean canChangeAddress = true;
		// if age < 18, address cannot be changed
		if (currentYear - birthdateYear < 18){ // check year
			if (currentMonth - birthdateMonth < 0){ // check month
				if (currentDay - birthdateDay < 0) { // check day
					canChangeAddress = false; 
				}
			}
			canChangeAddress = false; 
		}

		// CONDITION 2
		//	If persons birthdate is going to be changed, no other detail (ID, firstName, address, etc) can be changed
		String oldBirthdate = this.birthdate;
		String newBirthdate = newPerson.birthdate;
		Boolean canChangePersonalDetails = true;
		// compare to new birthdate, if different then no other details can be changed
		if (!(oldBirthdate.equals(newBirthdate))){
			canChangePersonalDetails = false;
		}


		// CONDITION 3
		//	If first char/digit of ID is an even number, ID cannot be changed
		char firstCharOfID = this.personID.charAt(0);
		Boolean canChangeID = true;
		// if even, ID cannot be changed
		if (firstCharOfID % 2 == 0){
			canChangeID = false;
		}

		// actually update info
		Person updatedPerson = new Person(this.personID, this.firstName, this.lastName, this.address, this.birthdate);
		if (canChangePersonalDetails) {
			// update personID
			if (canChangeID) {
				updatedPerson.personID = newPerson.personID;
			}
			// update firstName
			updatedPerson.firstName = newPerson.firstName;
			// update lastName
			updatedPerson.lastName = newPerson.lastName;
			// update address
			if (canChangeAddress) {
				updatedPerson.address = newPerson.address;
			}
			// update birthdate
			updatedPerson.birthdate = newPerson.birthdate;
		}
		// remove oldPerson.txt
		File oldFile = new File("src" + File.separator + "main" + File.separator + "resources" + File.separator + this.personID + ".txt");
		try {
			Files.delete(oldFile.toPath());
		} catch (IOException e) {
			System.out.println("Error, old file not found");
			e.printStackTrace();
		}
		// add updatedPerson .txt
		try {
			updatedPerson.addPersonToTxtFile();
		} catch (IOException e) {
			System.out.println("Error, new file unable to be created");
			e.printStackTrace();
		}

		// return conditions for unit tests
		if (!canChangeAddress || !canChangePersonalDetails || !canChangeID){
			return false;
		}
		return true;
	}

	// helper function to get new input
	public static Person getInputForNewPerson() {
		Scanner scanner = new Scanner(System.in);
		// get personID
		System.out.println("Please enter new person ID: ");
		String newPersonID = scanner.nextLine();
		// get first name
		System.out.println("Please enter new person first name: ");
		String newFirstName = scanner.nextLine();
		// get last name
		System.out.println("Please enter new person last name: ");
		String newLastName = scanner.nextLine();
		// get address
		System.out.println("Please enter new person address: ");
		String newAddress = scanner.nextLine();
		// get birthdate
		System.out.println("Please enter new person birthdate: ");
		String newBirthdate = scanner.nextLine();
		// return
		scanner.close();
		Person newPerson = new Person(newPersonID, newFirstName, newLastName, newAddress, newBirthdate);
		return newPerson;
	}
	
	public String addDemeritPoints(String infractionDate, int points) throws IOException {
		
		boolean isValidInfractionDate = false;
		boolean isUnder21 = false;

		//addressing condition 1
		if (infractionDate.length() == 10) {
			for (int i = 0; i < infractionDate.length(); i++) {
				char infractionChar = infractionDate.charAt(i);
				if ((i != 2) && (i != 5) && Character.isDigit(infractionChar)) {
					isValidInfractionDate = true;
				}
				else if (((i == 2) || (i == 5)) && (infractionChar == '-')) {
					isValidInfractionDate = true;
				}
				else {
					return "Invalid";
				}
			}
		}
		else {
			return "Invalid";
		}

		//addressing condition 2
		if(points < 1 || points >= 7) {
			return "Invalid";
		}

		//addressing condition 3 pt1
		//parse infractions date into integers
		int infractionYear, infractionMonth, infractionDay;
		infractionDay = Integer.parseInt(infractionDate.substring(0, 2));
		infractionMonth = Integer.parseInt(infractionDate.substring(3, 5));
		infractionYear = Integer.parseInt(infractionDate.substring(6, 10));

		//parse birthday to compare
		String birthdate = this.birthdate;
		int birthdateYear, birthdateMonth, birthdateDay;
		birthdateDay = Integer.parseInt(birthdate.substring(0, 2));
		birthdateMonth = Integer.parseInt(birthdate.substring(3, 5));
		birthdateYear = Integer.parseInt(birthdate.substring(6, 10));	
		
		//compare dates
		LocalDate birthDateTemp = LocalDate.of(birthdateYear, birthdateMonth, birthdateDay);
		LocalDate infractionDateTemp = LocalDate.of(infractionYear, infractionMonth, infractionDay);
		// LocalDate currentDateTemp = LocalDate.now();

		int ageActual = Period.between(birthDateTemp, infractionDateTemp).getYears();
		
		if(ageActual < 21) {isUnder21 = true;} 
		else {isUnder21 = false;}

		//push infractions to HashMap
		demeritPoints.put(infractionDateTemp, points);

		int twoYearTotal = 0;
		//need method to get all infraction events within two years
		for(Map.Entry<LocalDate, Integer> entry : demeritPoints.entrySet()) {
			if(Period.between(entry.getKey(), infractionDateTemp).getYears() < 2) {
				twoYearTotal += entry.getValue();
			}
		}

//		System.out.println(twoYearTotal);

		//condition 3 pt2
		//is under 21
		if (isUnder21 && twoYearTotal > 6) {
			this.isSuspended = true;
		}
		//is over 21
		else if (twoYearTotal > 12) {
			this.isSuspended = true;
		}
		else {
			this.isSuspended = false;
		}
		
		
		File file = new File("src" + File.separator + "main" + File.separator + "resources" + File.separator + this.personID + ".txt");
		
		//Write to file
		
		FileOutputStream fileOStream = new FileOutputStream(file, true);
		PrintWriter oFileStream = new PrintWriter(fileOStream);
		
		oFileStream.println("Demerit Points: " + twoYearTotal);	
		
		oFileStream.close();
		fileOStream.close();

		return "Success";
		
		
		
		
	}
	
	public boolean isPersonSuspended() {
		if (this.isSuspended) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public static void main(String[] args) throws IOException {
		Person firstPerson = new Person("56s_d%&fAB", "John", "Smith", "32|Highland Street|Melbourne|Victoria|Australia", "19-05-2000");
		firstPerson.addPerson();

//		Person secondPerson = new Person("96s_d%&fAB", "Pierre", "Tan", "64|Highland Street|Melbourne|Victoria|Australia", "19-05-2000");
		// No need for input anymore
		// Person secondPerson = getInputForNewPerson();
		
		// Note, updatePersonalDetails also calls addPerson (when creating new file) to ensure constraints of both functions are applied to person
//		firstPerson.updatePersonalDetails(secondPerson);

		firstPerson.addDemeritPoints("01-06-2025", 3);
	}
}