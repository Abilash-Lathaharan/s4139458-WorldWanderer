// Author: Abilash Lathaharan (S4139458)

package test;


import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import flight.FlightSearch;

// test class
public class FlightSearchTest {

    private FlightSearch flightSearch;  

    @BeforeEach
    void setup() {
        flightSearch = new FlightSearch(); // before the test, create a FlightSearch object
    }

    /* Parameter order: departureDate, departureAirportCode, emergencyRowSeating, 
       returnDate, destinationAirportCode, seatingClass, 
       adultPassengerCount, childPassengerCount, infantPassengerCount)
     */
    
    // Validate total passenger count between 1–9
    @Test
    void testPassengerCountBoundary() {
    	// adult = 0, children = 0, infant = 0, so total passengers = 0 which is less than 9
        assertFalse(flightSearch.runFlightSearch("17/11/2025", "mel", false, "18/11/2025", "syd", "economy", 0, 0, 0));
        assertNull(flightSearch.getDepartureAirportCode()); // null after the test
        // adult = 9, children = 1, infant = 1, so total passengers = 11 which is greater than 9 
        assertFalse(flightSearch.runFlightSearch("17/11/2025", "mel", false, "18/11/2025", "syd", "economy", 9, 1, 0));
        assertNull(flightSearch.getDepartureAirportCode());
        // Invalid: negative child count
        assertFalse(flightSearch.runFlightSearch("17/10/2025", "mel", false, "18/11/2025", "syd", "economy", 2, -1, 0));
        assertNull(flightSearch.getDepartureAirportCode());
        // Invalid: negative infant count
        assertFalse(flightSearch.runFlightSearch("17/10/2025", "mel", false, "18/11/2025", "syd", "economy", 2, 0, -1));
        assertNull(flightSearch.getDepartureAirportCode());
        // adult = 9, children = 0, infant = 0, so total passengers = 9 which is correct (1-9
        assertTrue(flightSearch.runFlightSearch("17/11/2025", "mel", false, "18/11/2025", "syd", "economy", 9, 0, 0));
        assertEquals("mel", flightSearch.getDepartureAirportCode()); // have value for one attribute after the test
    }

    // validate Children not allowed in emergency row or first class
    @Test
    void testChildEmergencyAndClass() {
    	// Invalid (non-economy can’t have emergency row)
        assertFalse(flightSearch.runFlightSearch("17/11/2025", "mel", true, "25/11/2025", "pvg", "first", 1, 1, 0));
        assertNull(flightSearch.getDepartureAirportCode()); // null after the test
        // Valid (economy can be emergency row)
        assertTrue(flightSearch.runFlightSearch("17/11/2025", "mel", true, "25/11/2025", "pvg", "economy", 1, 1, 0));
        assertEquals("mel", flightSearch.getDepartureAirportCode()); // have value for one attribute after the test
        // Valid (non-emergency first class allowed for children) 
        assertTrue(flightSearch.runFlightSearch("17/11/2025", "mel", false, "25/11/2025", "pvg", "first", 1, 1, 0));
        assertEquals("mel", flightSearch.getDepartureAirportCode()); // have value for one attribute after the test
    }

    // validate Infants not allowed in emergency row or business class
    @Test
    void testInfantRestrictions() {
    	// Invalid (infant in emergency row) 
        assertFalse(flightSearch.runFlightSearch("17/11/2025", "mel", true, "25/11/2025", "pvg", "economy", 1, 0, 1));
        assertNull(flightSearch.getDepartureAirportCode()); // null after the test
        // Invalid (non-economy emergency) 
        assertFalse(flightSearch.runFlightSearch("17/11/2025", "mel", false, "25/11/2025", "pvg", "business", 1, 0, 1));
        assertNull(flightSearch.getDepartureAirportCode()); // null after the test
        // Valid (non-emergency allowed) 
        assertTrue(flightSearch.runFlightSearch("17/11/2025", "mel", false, "25/11/2025", "pvg", "economy", 1, 0, 1));
        assertEquals("mel", flightSearch.getDepartureAirportCode()); // have value for one attribute after the test
    }

    // validate Each adult can supervise up to 2 children
    @Test
    void testAdultChildRatio() {
    	// Invalid (exceeds limit)
        assertFalse(flightSearch.runFlightSearch("17/11/2025", "mel", false, "25/11/2025", "pvg", "economy", 1, 3, 0));
        assertNull(flightSearch.getDepartureAirportCode()); // null after the test
        // Valid (2 per adult) 
        assertTrue(flightSearch.runFlightSearch("17/11/2025", "mel", false, "25/11/2025", "pvg", "economy", 2, 4, 0));
        assertEquals("mel", flightSearch.getDepartureAirportCode()); // have value for one attribute after the test
    }

    // validate Each adult can have only 1 infant
    @Test
    void testAdultInfantRatio() {
    	// invalid (limit exceeded)
        assertFalse(flightSearch.runFlightSearch("17/11/2025", "mel", false, "25/12/2025", "pvg", "economy", 1, 0, 2));
        assertNull(flightSearch.getDepartureAirportCode()); // null after the test
        // valid 
        assertTrue(flightSearch.runFlightSearch("17/11/2025", "mel", false, "25/12/2025", "pvg", "economy", 2, 0, 2));
        assertEquals("mel", flightSearch.getDepartureAirportCode()); // have value for one attribute after the test
    }

    // validate Departure date cannot be in the past
    @Test
    void testDateInPast() {
    	// Invalid (past date)
        assertFalse(flightSearch.runFlightSearch("01/01/2020", "mel", false, "05/01/2020", "pvg", "economy", 1, 0, 0));
        assertNull(flightSearch.getDepartureAirportCode()); // null after the test
        // valid (future date)
        assertTrue(flightSearch.runFlightSearch("01/11/2026", "mel", false, "01/12/2026", "pvg", "economy", 1, 0, 0));
        assertEquals("mel", flightSearch.getDepartureAirportCode()); // have value for one attribute after the test
    }

    // validate Date must be in strict format DD/MM/YYYY
    @Test
    void testDateMustBeInStrictFormat() {
        // Invalid format (uses /yyyy instead of dd/MM/yyyy)
        assertFalse(flightSearch.runFlightSearch("2025/10/25", "mel", false, "29/10/2025", "pvg", "economy", 1, 0, 0));
        assertNull(flightSearch.getDepartureAirportCode()); // null after the test

        // Valid strict format (DD/MM/YYYY)
        assertTrue(flightSearch.runFlightSearch("25/10/2025", "mel", false, "29/10/2025", "pvg", "economy", 1, 0, 0));
        assertEquals("mel", flightSearch.getDepartureAirportCode()); // have value for one attribute after the test
    }
    
    // Validate real calendar date (leap year rule)
    @Test
    void testValidateRealCalendarDateLeapYearRule() {
        // Invalid — 2026 not leap year
        assertFalse(flightSearch.runFlightSearch("29/02/2026", "mel", false, "01/03/2026", "pvg", "economy", 1, 0, 0));
        assertNull(flightSearch.getDepartureAirportCode()); // null after the test

        // Valid — 2028 is leap year
        assertTrue(flightSearch.runFlightSearch("29/02/2028", "mel", false, "01/03/2028", "pvg", "economy", 1, 0, 0));
        assertEquals("mel", flightSearch.getDepartureAirportCode()); // have value for one attribute after the test
    }

    // validate Return date cannot be before departure date
    @Test
    void testReturnBeforeDeparture() {
    	// Invalid (Return date cannot be before departure date)
        assertFalse(flightSearch.runFlightSearch("25/10/2025", "mel", false, "24/10/2025", "pvg", "economy", 1, 0, 0));
        assertNull(flightSearch.getDepartureAirportCode()); // null after the test
        // valid return date
        assertTrue(flightSearch.runFlightSearch("25/10/2025", "mel", false, "30/10/2025", "pvg", "economy", 1, 0, 0));
        assertEquals("mel", flightSearch.getDepartureAirportCode()); // have value for one attribute after the test
    }

    // validate Seating class must be valid + emergency row rule
    @Test
    void testInvalidClassAndEmergencyRules() {
    	// invalid emergency for business
        assertFalse(flightSearch.runFlightSearch("25/10/2025", "mel", true, "28/10/2025", "pvg", "business", 1, 0, 0)); 
        assertNull(flightSearch.getDepartureAirportCode()); // null after the test
        // Invalid (class not allowed) 
        assertFalse(flightSearch.runFlightSearch("25/10/2025", "mel", false, "28/10/2025", "pvg", "luxury", 1, 0, 0));
        assertNull(flightSearch.getDepartureAirportCode()); // null after the test
        // valid non-emergency
        assertTrue(flightSearch.runFlightSearch("25/10/2025", "mel", false, "28/10/2025", "pvg", "business", 1, 0, 0)); 
        assertEquals("mel", flightSearch.getDepartureAirportCode()); // have value for one attribute after the test

    }

    // validate Airport codes valid and not identical
    @Test
    void testAirportCodes() {
    	// destination and departure are same; mel
        assertFalse(flightSearch.runFlightSearch("25/10/2025", "mel", false, "28/10/2025", "mel", "economy", 1, 0, 0));
        // departure airport code is not in list / Invalid (Code Not in List)
        assertFalse(flightSearch.runFlightSearch("25/10/2025", "cmb", false, "28/10/2025", "mel", "economy", 1, 0, 0));
        // different airports and valid codes
        assertTrue(flightSearch.runFlightSearch("25/10/2025", "mel", false, "28/10/2025", "pvg", "economy", 1, 0, 0));
        assertEquals("mel", flightSearch.getDepartureAirportCode()); // have value for one attribute after the test
    }

    // validate All valid inputs combined (test for attribute initialization)
    @Test
    void testAllValidInputs() {
        boolean result = flightSearch.runFlightSearch("23/11/2025", "mel", false, "29/11/2025", "pvg", "economy", 2, 1, 0);
        assertTrue(result); 

        // Verify attributes initialized
        assertEquals("mel", flightSearch.getDepartureAirportCode());
        assertEquals("pvg", flightSearch.getDestinationAirportCode());
        assertEquals("economy", flightSearch.getSeatingClass());
        assertEquals(2, flightSearch.getAdultPassengerCount());
    }
}
