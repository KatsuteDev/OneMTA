package dev.katsute.onemta.types;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @see TransitAgency
 */
public abstract class AgencyValidation {

    public static void testAgency(final TransitAgency agency){
        assertNotNull(agency);
        assertNotNull(agency.getAgencyID());
        assertNotNull(agency.getAgencyName());
    }

}