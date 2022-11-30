package uk.gov.dwp.uc.pairtest;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import thirdparty.paymentgateway.TicketPaymentService;
import thirdparty.seatbooking.SeatReservationService;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.Assert.assertEquals;


/**
 * @author Mark Start
 * @Date 29/11/2022
 */



public class TicketServiceImplTest {


    TicketServiceImpl ticketServiceImpl;

    // Some useful values:
    Long validId;
    TicketTypeRequest validTicketRequests[];
    int adultTicketCost;
    int childTicketCost;
    int infantTicketCost;


    @Before
    public void setUp() {

        SeatReservationService seatReservationService = Mockito.mock( SeatReservationService.class );
        TicketPaymentService ticketPaymentService = Mockito.mock( TicketPaymentService.class );

        this.ticketServiceImpl = new TicketServiceImpl( seatReservationService, ticketPaymentService );

        validId = 1L;

        TicketTypeRequest a = new TicketTypeRequest( TicketTypeRequest.Type.ADULT, 1);
        TicketTypeRequest b = new TicketTypeRequest( TicketTypeRequest.Type.CHILD, 1);
        TicketTypeRequest c = new TicketTypeRequest( TicketTypeRequest.Type.INFANT, 1);

        validTicketRequests = new TicketTypeRequest[] {a,b,c};

        adultTicketCost = ticketServiceImpl.getAdultTicketCost();
        childTicketCost = ticketServiceImpl.getChildTicketCost();
        infantTicketCost = ticketServiceImpl.getInfantTicketCost();

    }


    /**
     *    Simple Test:
     */


    @Test
    public void givenValidIdAndValidTicketRequests_whenPurchaseTickets_thenPass() {

        ticketServiceImpl.purchaseTickets(validId, validTicketRequests );

    }



    /**
     *    Id range checks :
     */


    @Test(expected = InvalidPurchaseException.class)
    public void givenInvalidLowId_WhenPurchaseTickets_ThenThrows() {

        Long invalidId = 0L;
        ticketServiceImpl.purchaseTickets(invalidId, validTicketRequests );

    }


    @Test(expected = InvalidPurchaseException.class)
    public void givenInvalidNegativeId_WhenPurchaseTickets_ThenThrows() {

        Long invalidId = -1L;
        ticketServiceImpl.purchaseTickets(invalidId, validTicketRequests );

    }


    @Test(expected = InvalidPurchaseException.class)
    public void givenAVeryLowInvalidId_WhenPurchaseTickets_ThenThrows() {

        Long invalidId = Long.MIN_VALUE;
        ticketServiceImpl.purchaseTickets(invalidId, validTicketRequests );

    }


    @Test
    public void givenValidId_WhenPurchaseTickets_ThenPass () {

        Long validId = 1L;
        ticketServiceImpl.purchaseTickets(validId, validTicketRequests);

    }


    @Test
    public void givenValidVeryHighId_WhenPurchaseTickets_ThenPass () {

        Long validId = Long.MAX_VALUE;
        ticketServiceImpl.purchaseTickets(validId, validTicketRequests );

    }



    /**
     *  Valid TicketTypeRequest[] checks { null, [], [].length > 20}:
     */


    @Test(expected = NullPointerException.class)
    public void givenNullTicketTypeRequest_whenPurchaseTickets_thenThrow() {

        ticketServiceImpl.purchaseTickets( validId, null );

    }


    @Test(expected = InvalidPurchaseException.class)
    public void givenEmptyTicketTypeRequestArray_whenPurchaseTickets_thenThrow() {

        TicketTypeRequest[] ticketTypeRequests = new TicketTypeRequest[]{};

        ticketServiceImpl.purchaseTickets( validId, ticketTypeRequests );

    }


    @Test(expected = InvalidPurchaseException.class)
    public void givenInvalidOverMaximumTicketsRequest_whenPurchaseTickets_thenThrow() {

        // [].length > 20
        // 21 TicketTypeRequests of 1 ticket each.

        TicketTypeRequest ticketTypeRequest = new TicketTypeRequest( TicketTypeRequest.Type.ADULT, 1);
        TicketTypeRequest[] ticketTypeRequests = new TicketTypeRequest[21];

        for (int i=0; i < 21; i++) ticketTypeRequests[i] = ticketTypeRequest;

        ticketServiceImpl.purchaseTickets(validId, ticketTypeRequests );

    }



    /**
     *    Valid and invalid TicketTypeRequest[] arrangements :
     */


    @Test
    public void givenValidSingleAdultTicket_whenPurchaseTickets_thenPass() {

        TicketTypeRequest a = new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 1);

        TicketTypeRequest[] ttr = new TicketTypeRequest[]{a};

        ticketServiceImpl.purchaseTickets(validId, ttr);

    }



    @Test(expected = InvalidPurchaseException.class)
    public void givenInvalidSingleChildTicket_whenPurchaseTickets_thenThrow () {

        TicketTypeRequest a = new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 1);

        TicketTypeRequest[] ttr = new TicketTypeRequest[]{a};

        ticketServiceImpl.purchaseTickets(validId, ttr);

    }


    @Test(expected = InvalidPurchaseException.class)
    public void givenInvalidSingleInfantTicket_whenPurchaseTickets_thenThrow () {

        TicketTypeRequest a = new TicketTypeRequest(TicketTypeRequest.Type.INFANT, 1);

        TicketTypeRequest[] ttr = new TicketTypeRequest[]{a};

        ticketServiceImpl.purchaseTickets(validId, ttr);

    }


    @Test
    public void givenValidSingleAdultSingleInfantTicket_whenPurchaseTickets_thenPass() {

        TicketTypeRequest a = new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 1);
        TicketTypeRequest b = new TicketTypeRequest(TicketTypeRequest.Type.INFANT, 1);

        TicketTypeRequest[] ttr = new TicketTypeRequest[]{a,b};

        ticketServiceImpl.purchaseTickets(validId, ttr);

    }


    @Test
    public void givenValidSingleAdultSingleChildTicket_whenPurchaseTickets_thenPass() {

        TicketTypeRequest a = new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 1);
        TicketTypeRequest b = new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 1);

        TicketTypeRequest[] ttr = new TicketTypeRequest[]{a,b};

        ticketServiceImpl.purchaseTickets(validId, ttr);

    }


    @Test
    public void givenValidAdultChildInfantMaxRange_whenPurchaseTickets_thenPass() {

        TicketTypeRequest a = new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 1);
        TicketTypeRequest b = new TicketTypeRequest(TicketTypeRequest.Type.INFANT, 1);
        TicketTypeRequest c = new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 18);

        TicketTypeRequest[] ttr = new TicketTypeRequest[]{a,b, c};

        ticketServiceImpl.purchaseTickets(validId, ttr);

    }


    @Test
    public void givenValidAdultInfant_whenPurchaseTickets_thenPass() {

        TicketTypeRequest a = new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 10);
        TicketTypeRequest b = new TicketTypeRequest(TicketTypeRequest.Type.INFANT, 10);

        TicketTypeRequest[] ttr = new TicketTypeRequest[]{a,b};

        ticketServiceImpl.purchaseTickets(validId, ttr);

    }


    @Test(expected = InvalidPurchaseException.class)
    public void givenInvalidAdultInfant_whenPurchaseTickets_thenThrow() {

        TicketTypeRequest a = new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 9);
        TicketTypeRequest b = new TicketTypeRequest(TicketTypeRequest.Type.INFANT, 11);

        TicketTypeRequest[] ttr = new TicketTypeRequest[]{a, b};

        ticketServiceImpl.purchaseTickets(validId, ttr);

    }



    /**
     *  Ticket range checks :
     */


    @Test
    public void givenMaxAdultTicketsSingleElement_whenPurchaseTickets_thenPass() {

        TicketTypeRequest a = new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 20);

        TicketTypeRequest[] ttr = new TicketTypeRequest[]{a};

        ticketServiceImpl.purchaseTickets(validId, ttr);

    }

    // single element ticket request over 21 error is caught by TicketTypeRequest.test

    @Test
    public void givenMaxAdultTicketsManyElements_whenPurchaseTickets_thenPass() {

        TicketTypeRequest a = new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 5);
        TicketTypeRequest b = new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 14);
        TicketTypeRequest c = new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 1);

        TicketTypeRequest[] ttr = new TicketTypeRequest[]{a, b, c};

        ticketServiceImpl.purchaseTickets(validId, ttr);

    }


    @Test(expected = InvalidPurchaseException.class)
    public void givenInvalidMaxAdultTicketsManyElements_whenPurchaseTickets_thenThrow() {

        TicketTypeRequest a = new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 5);
        TicketTypeRequest b = new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 15);
        TicketTypeRequest c = new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 1);

        TicketTypeRequest[] ttr = new TicketTypeRequest[]{a, b, c};

        ticketServiceImpl.purchaseTickets(validId, ttr);

    }



    /**
     * Reflects TicketServiceImpl's private methods { calculateTotalSeatsToReserve(), calculateTotalTicketCost() } :
     */


    private Method getPrivateMethod_calculateTotalSeatsToReserve() throws NoSuchMethodException {

        Method method =  TicketServiceImpl.class.getDeclaredMethod( "calculateTotalSeatsToReserve", TicketTypeRequest[].class);
        method.setAccessible(true);
        return method;

    }


    private Method getPrivateMethod_calculateTotalTicketCost() throws NoSuchMethodException {

        Method method =  TicketServiceImpl.class.getDeclaredMethod( "calculateTotalTicketCost", TicketTypeRequest[].class);
        method.setAccessible(true);
        return method;

    }



    /**
     *      calculateTotalSeatsToReserve() returns the correct # of seats:
     *      (Infants are not reserved a seat. They sit on Adult laps).
     */


    @Test
    public void givenAnAdultChildAndInfant_whenCalculatingSeatsToReserve_thenReturns2not3 () throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {

        TicketTypeRequest a = new TicketTypeRequest( TicketTypeRequest.Type.ADULT, 1);
        TicketTypeRequest b = new TicketTypeRequest( TicketTypeRequest.Type.CHILD, 1);
        TicketTypeRequest c = new TicketTypeRequest( TicketTypeRequest.Type.INFANT, 1);

        TicketTypeRequest[] validTicketRequests = new TicketTypeRequest[] {a,b,c};

        int totalSeatsReserved = (int) getPrivateMethod_calculateTotalSeatsToReserve().invoke( ticketServiceImpl, new Object[] {validTicketRequests} );

        // Infants are not reserved a seat. They sit on Adult's laps. 3 tickets = 2 seat reservations.
        assertEquals(2, totalSeatsReserved );

    }


    @Test
    public void givenValidTicketRequests_whenCalculatingSeatReservations_thenCorrectTotal () throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {

        TicketTypeRequest a = new TicketTypeRequest( TicketTypeRequest.Type.ADULT, 5);
        TicketTypeRequest b = new TicketTypeRequest( TicketTypeRequest.Type.CHILD, 6);
        TicketTypeRequest c = new TicketTypeRequest( TicketTypeRequest.Type.INFANT, 3);

        TicketTypeRequest[] validTicketRequests = new TicketTypeRequest[] {a,b,c};

        int totalSeatsReserved = (int) getPrivateMethod_calculateTotalSeatsToReserve().invoke( ticketServiceImpl, new Object[] {validTicketRequests} );

        // Infants are not reserved a seat.
        assertEquals(11, totalSeatsReserved );

    }



    /**
     * calculateTotalTicketCost() returns the correct total :
     */


    @Test
    public void givenAdultChildInfantSingleTickets_whenCalculateTotalTicketCost_thenReturnCorrectTotal () throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {

        TicketTypeRequest a = new TicketTypeRequest( TicketTypeRequest.Type.ADULT, 1);
        TicketTypeRequest b = new TicketTypeRequest( TicketTypeRequest.Type.CHILD, 1);
        TicketTypeRequest c = new TicketTypeRequest( TicketTypeRequest.Type.INFANT, 1);

        TicketTypeRequest[] validTicketRequests = new TicketTypeRequest[] {a,b,c};

        int totalTicketCost = (int) getPrivateMethod_calculateTotalTicketCost().invoke( ticketServiceImpl, new Object[] {validTicketRequests});

        int expectTotalTicketCost = ( adultTicketCost + childTicketCost + infantTicketCost );

        assertEquals( expectTotalTicketCost, totalTicketCost);

        assertEquals(30, totalTicketCost);

    }


    @Test
    public void givenManyTickets_whenCalculateTotalTicketCost_thenReturnCorrectTotal () throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {

        TicketTypeRequest a = new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 5);
        TicketTypeRequest b = new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 3);
        TicketTypeRequest c = new TicketTypeRequest(TicketTypeRequest.Type.INFANT, 10);

        TicketTypeRequest[] validTicketRequests = new TicketTypeRequest[]{a, b, c};

        int totalTicketCost = (int) getPrivateMethod_calculateTotalTicketCost().invoke(ticketServiceImpl, new Object[]{validTicketRequests});

        int expectTotalTicketCost = ((adultTicketCost * 5) + (childTicketCost * 3) + (infantTicketCost * 100) );

        assertEquals(expectTotalTicketCost, totalTicketCost);

        assertEquals(130, totalTicketCost);


    }




    // ...








}