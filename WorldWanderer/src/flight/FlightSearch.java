// Author: Abilash Lathaharan (S4139458)

package flight;

// import classes
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


// class for Flight Search
public class FlightSearch {
	// private attributes
    private String departureDate;
    private String returnDate;
    private String departureAirportCode;
    private String destinationAirportCode;
    private String seatingClass;
    private boolean emergencyRowSeating;
    private int adultPassengerCount;
    private int childPassengerCount;
    private int infantPassengerCount;
    private int totalPassengers;

    // fixed classes and valid airports; which are stored as final attributes in array list
    private static final List<String> VALID_CLASSES = Arrays.asList("economy", "business", "first");
    private static final List<String> VALID_AIRPORTS = Arrays.asList("syd", "mel", "lax", "cdg", "del", "pvg", "doh");

    // runFlightSearch method; if all conditions are paased, return true; else return false
    public boolean runFlightSearch(String departureDate, String departureAirportCode, boolean emergencyRowSeating, 
    		String returnDate, String destinationAirportCode, String seatingClass, 
    		int adultPassengerCount, int childPassengerCount, int infantPassengerCount) {

        // Condition 1: Total passengers between 1 and 9 
    	totalPassengers = adultPassengerCount + childPassengerCount + infantPassengerCount;
        if (totalPassengers < 1 || totalPassengers > 9) return false;

        // Condition 9: Seating class must be valid 
        if (!VALID_CLASSES.contains(seatingClass)) return false;

        // Condition 10: Only economy can have emergency row 
        if (emergencyRowSeating && !seatingClass.equals("economy")) return false;

        // Condition 2 & 3: Seating restrictions for children/infants 
        if (childPassengerCount > 0 && seatingClass.equals("first") && emergencyRowSeating) return false;
        if (infantPassengerCount > 0 && (seatingClass.equals("business") || emergencyRowSeating)) return false;

        // Condition 4: Each adult can supervise up to 2 children 
        if (childPassengerCount > adultPassengerCount * 2) return false;

        // Condition 5: Each adult can have only 1 infant
        if (infantPassengerCount > adultPassengerCount) return false;

        // Condition 7: Date format validation 
        // call isValidDateFormat method
        if (!isValidDateFormat(departureDate) || !isValidDateFormat(returnDate)) return false;

        // Condition 8: Validate date order and calendar logic 
        Date dep = parseDate(departureDate); // change the departure and return date to Date data type
        Date ret = parseDate(returnDate);
        if (dep == null || ret == null) return false;
        if (ret.before(dep)) return false;

        // Condition 6: Departure date cannot be in the past 
        Date today = getTodayDate();
        if (dep.before(today)) return false;

        // Condition 11: Validate airports
        if (!VALID_AIRPORTS.contains(departureAirportCode) || !VALID_AIRPORTS.contains(destinationAirportCode)) return false;
        if (departureAirportCode.equals(destinationAirportCode)) return false;

        // If all validations pass, initialize attributes
        this.departureDate = departureDate;
        this.returnDate = returnDate;
        this.departureAirportCode = departureAirportCode;
        this.destinationAirportCode = destinationAirportCode;
        this.seatingClass = seatingClass;
        this.emergencyRowSeating = emergencyRowSeating;
        this.adultPassengerCount = adultPassengerCount;
        this.childPassengerCount = childPassengerCount;
        this.infantPassengerCount = infantPassengerCount;
        this.totalPassengers = adultPassengerCount + childPassengerCount + infantPassengerCount;

        return true;
    }

    // Validates date DD/MM/YYYY format 
    private boolean isValidDateFormat(String dateStr) {
        if (dateStr == null || !dateStr.matches("\\d{2}/\\d{2}/\\d{4}")) return false;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        sdf.setLenient(false);
        try {
            sdf.parse(dateStr);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    // Parses a date string in DD/MM/YYYY format 
    private Date parseDate(String dateStr) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            sdf.setLenient(false);
            return sdf.parse(dateStr);
        } catch (Exception e) {
            return null;
        }
    }

    // Returns today's date (00:00 hrs) 
    // [1]
    private Date getTodayDate() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    // getters for testing purpose
    // getter for Departure Date
    public String getDepartureDate() { 
    	return departureDate; 
    }
    
    // getter for Return date
    public String getReturnDate() { 
    	return returnDate; 
    }
    
    // getter for Departure Airport
    public String getDepartureAirportCode() { 
    	return departureAirportCode; 
    }
    
    // getter for Destination Airport
    public String getDestinationAirportCode() { 
    	return destinationAirportCode; 
    }
    
    // getter for Seating Class
    public String getSeatingClass() { 
    	return seatingClass; 
    }
    
    // getter for EmergencyRowSeating
    public boolean isEmergencyRowSeating() { 
    	return emergencyRowSeating; 
    }
    
    // get Adult Passenger count
    public int getAdultPassengerCount() { 
    	return adultPassengerCount; 
    }
    
    // get children count
    public int getChildPassengerCount() { 
    	return childPassengerCount; 
    }
    
    // get infant count
    public int getInfantPassengerCount() { 
    	return infantPassengerCount; 
    }
}


//  Reference: [1] https://www.w3schools.com/java/java_date.asp