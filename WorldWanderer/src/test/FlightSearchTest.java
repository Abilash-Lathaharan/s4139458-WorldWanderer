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

    /* departureDate, departureAirportCode, emergencyRowSeating, 
       returnDate, destinationAirportCode, seatingClass, 
       adultPassengerCount, childPassengerCount, infantPassengerCount)
     */
    
    // Validate total passenger count between 1–9
    @Test
    void testPassengerCountBoundary() {
    	// adult = 0, children = 0, infant = 0, so total passengers = 0 which is less than 9
        assertFalse(flightSearch.runFlightSearch("17/10/2025", "mel", false, "18/11/2025", "syd", "economy", 0, 0, 0));
        // adult = 9, children = 1, infant = 1, so total passengers = 11 which is greater than 9 
        assertFalse(flightSearch.runFlightSearch("17/10/2025", "mel", false, "18/11/2025", "syd", "economy", 9, 1, 1));
        // adult = 9, children = 0, infant = 0, so total passengers = 9 which is correct (1-9
        assertTrue(flightSearch.runFlightSearch("17/10/2025", "mel", false, "18/11/2025", "syd", "economy", 9, 0, 0));
    }

    // validate Children not allowed in emergency row or first class
    @Test
    void testChildEmergencyAndClass() {
        assertFalse(flightSearch.runFlightSearch("17/10/2025", "mel", true, "25/10/2025", "pvg", "first", 1, 1, 0));
        assertTrue(flightSearch.runFlightSearch("17/10/2025", "mel", true, "25/10/2025", "pvg", "economy", 1, 1, 0));
    }

    // validate Infants not allowed in emergency row or business class
    @Test
    void testInfantRestrictions() {
        assertFalse(flightSearch.runFlightSearch("17/10/2025", "mel", true, "25/10/2025", "pvg", "economy", 1, 0, 1)); // invalid emergency
        assertFalse(flightSearch.runFlightSearch("17/10/2025", "mel", false, "25/10/2025", "pvg", "business", 1, 0, 1)); // invalid business infant
        assertTrue(flightSearch.runFlightSearch("17/10/2025", "mel", false, "25/10/2025", "pvg", "economy", 1, 0, 1));
    }

    // validate Each adult can supervise up to 2 children
    @Test
    void testAdultChildRatio() {
        assertFalse(flightSearch.runFlightSearch("17/10/2025", "mel", false, "25/10/2025", "pvg", "economy", 1, 3, 0));
        assertTrue(flightSearch.runFlightSearch("17/10/2025", "mel", false, "25/10/2025", "pvg", "economy", 2, 4, 0));
    }

    // validate Each adult can have only 1 infant
    @Test
    void testAdultInfantRatio() {
        assertFalse(flightSearch.runFlightSearch("17/10/2025", "mel", false, "25/10/2025", "pvg", "economy", 1, 0, 2));
        assertTrue(flightSearch.runFlightSearch("17/10/2025", "mel", false, "25/10/2025", "pvg", "economy", 2, 0, 2));
    }

    // validate Departure date cannot be in the past
    @Test
    void testDateInPast() {
        assertFalse(flightSearch.runFlightSearch("01/01/2020", "mel", false, "05/01/2020", "pvg", "economy", 1, 0, 0));
        assertTrue(flightSearch.runFlightSearch("01/11/2026", "mel", false, "01/12/2026", "pvg", "economy", 1, 0, 0));
    }

    // validate Date must be in strict format DD/MM/YYYY
    @Test
    void testDateMustBeInStrictFormat() {
        // Invalid format (uses /yyyy instead of dd/MM/yyyy)
        assertFalse(flightSearch.runFlightSearch("2025/10/25", "mel", false, "29/10/2025", "pvg", "economy", 1, 0, 0));

        // Valid strict format (DD/MM/YYYY)
        assertTrue(flightSearch.runFlightSearch("25/10/2025", "mel", false, "29/10/2025", "pvg", "economy", 1, 0, 0));
    }
    
    // Validate real calendar date (leap year rule)
    @Test
    void testValidateRealCalendarDateLeapYearRule() {
        // Invalid — 2026 not leap year
        assertFalse(flightSearch.runFlightSearch("29/02/2026", "mel", false, "01/03/2026", "pvg", "economy", 1, 0, 0));

        // Valid — 2028 is leap year
        assertTrue(flightSearch.runFlightSearch("29/02/2028", "mel", false, "01/03/2028", "pvg", "economy", 1, 0, 0));
    }

    // validate Return date cannot be before departure date
    @Test
    void testReturnBeforeDeparture() {
        assertFalse(flightSearch.runFlightSearch("25/10/2025", "mel", false, "24/10/2025", "pvg", "economy", 1, 0, 0));
    }

    // validate Seating class must be valid + emergency row rule
    @Test
    void testInvalidClassAndEmergencyRules() {
        assertFalse(flightSearch.runFlightSearch("25/10/2025", "mel", true, "28/10/2025", "pvg", "business", 1, 0, 0)); // invalid emergency for business
        assertTrue(flightSearch.runFlightSearch("25/10/2025", "mel", false, "28/10/2025", "pvg", "business", 1, 0, 0)); // valid non-emergency
    }

    // validate Airport codes valid and not identical
    @Test
    void testAirportCodes() {
        assertFalse(flightSearch.runFlightSearch("25/10/2025", "mel", false, "28/10/2025", "mel", "economy", 1, 0, 0));
        assertTrue(flightSearch.runFlightSearch("25/10/2025", "mel", false, "28/10/2025", "pvg", "economy", 1, 0, 0));
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
