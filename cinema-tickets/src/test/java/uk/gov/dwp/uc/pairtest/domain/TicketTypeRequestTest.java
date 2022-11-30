package uk.gov.dwp.uc.pairtest.domain;

import org.junit.Test;


/**
 * @author Mark Start
 * @Date 30/11/2022
 */


public class TicketTypeRequestTest {


    /**
     * Instantiation Tests of valid and invalid Types { null, ADULT, CHILD, INFANT }:
     */


    @Test(expected = NullPointerException.class)
    public void givenNullType_WhenInstantiating_thenThrow() {

        TicketTypeRequest.Type type = null;
        int noOfTickets = 1;

        TicketTypeRequest ticketTypeRequest = new TicketTypeRequest( type, noOfTickets );

    }


    @Test
    public void givenValidType_WhenInstantiating_thenPass() {

        TicketTypeRequest.Type type = TicketTypeRequest.Type.ADULT;
        int noOfTickets = 1;

        TicketTypeRequest ticketTypeRequest = new TicketTypeRequest( type, noOfTickets );

    }


    @Test
    public void givenValidTypeInfant_WhenInstantiating_thenPass() {

        TicketTypeRequest.Type type = TicketTypeRequest.Type.INFANT;
        int noOfTickets = 1;

        TicketTypeRequest ticketTypeRequest = new TicketTypeRequest( type, noOfTickets );

    }


    /**
     * Instantiation Tests of valid and invalid noOfTickets { -2147483648, -1, 0, 1, 20, 21, 2147483647 };
     */


    @Test(expected = IllegalArgumentException.class)
    public void givenInvalidNoOfTickets_WhenInstantiating_thenThrow() {

        TicketTypeRequest.Type type = TicketTypeRequest.Type.ADULT;
        int noOfTickets = 0;

        TicketTypeRequest ticketTypeRequest = new TicketTypeRequest( type, noOfTickets );

    }

    @Test(expected = IllegalArgumentException.class)
    public void givenInvalidNegativeNoOfTickets_WhenInstantiating_thenThrow() {

        TicketTypeRequest.Type type = TicketTypeRequest.Type.ADULT;
        int noOfTickets = -1;

        TicketTypeRequest ticketTypeRequest = new TicketTypeRequest( type, noOfTickets );

    }

    @Test(expected = IllegalArgumentException.class)
    public void givenInvalidVeryLowNegativeNoOfTickets_WhenInstantiating_thenThrow() {

        TicketTypeRequest.Type type = TicketTypeRequest.Type.ADULT;
        int noOfTickets = -2147483648;

        TicketTypeRequest ticketTypeRequest = new TicketTypeRequest( type, noOfTickets );

    }

    @Test
    public void givenValidMinNoOfTickets_WhenInstantiating_thenPass() {

        TicketTypeRequest.Type type = TicketTypeRequest.Type.ADULT;
        int noOfTickets = 1;

        TicketTypeRequest ticketTypeRequest = new TicketTypeRequest( type, noOfTickets );

    }

    @Test
    public void givenValidMaxNoOfTickets_WhenInstantiating_thenPass() {

        TicketTypeRequest.Type type = TicketTypeRequest.Type.ADULT;
        int noOfTickets = 20;

        TicketTypeRequest ticketTypeRequest = new TicketTypeRequest( type, noOfTickets );

    }


    @Test(expected = IllegalArgumentException.class)
    public void givenInvalidHighNoOfTickets_WhenInstantiating_thenThrow() {

        TicketTypeRequest.Type type = TicketTypeRequest.Type.ADULT;
        int noOfTickets = 21;

        TicketTypeRequest ticketTypeRequest = new TicketTypeRequest( type, noOfTickets );

    }

    @Test(expected = IllegalArgumentException.class)
    public void givenInvalidVeryHighNoOfTickets_WhenInstantiating_thenThrow() {

        TicketTypeRequest.Type type = TicketTypeRequest.Type.ADULT;
        int noOfTickets = 2147483647;

        TicketTypeRequest ticketTypeRequest = new TicketTypeRequest( type, noOfTickets );

    }







}



