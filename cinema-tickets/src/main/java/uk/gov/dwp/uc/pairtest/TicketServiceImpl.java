package uk.gov.dwp.uc.pairtest;

import thirdparty.paymentgateway.TicketPaymentService;
import thirdparty.seatbooking.SeatReservationService;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;


/**
 * @author Mark Start
 * @Date 29/11/2022
 */


public class TicketServiceImpl implements TicketService {


    private int adultTicketCost = 20;
    private int childTicketCost = 10;
    private int infantTicketCost = 0;

    private SeatReservationService seatReservationService;
    private TicketPaymentService ticketPaymentService;


    public TicketServiceImpl ( SeatReservationService seatReservationService, TicketPaymentService ticketPaymentService ) {

        this.seatReservationService = seatReservationService;
        this.ticketPaymentService = ticketPaymentService;

    }


    public int getAdultTicketCost() {
        return adultTicketCost;
    }

    public int getChildTicketCost() {
        return childTicketCost;
    }

    public int getInfantTicketCost() {
        return infantTicketCost;
    }


    /**
     * Should only have private methods other than the one below.
     */


    @Override
    public void purchaseTickets( Long accountId, TicketTypeRequest[] ticketTypeRequests ) throws InvalidPurchaseException {

        if ( !validRequest( accountId, ticketTypeRequests ) ) throw new InvalidPurchaseException( accountId, ticketTypeRequests );

        int totalCost = calculateTotalTicketCost( ticketTypeRequests );
        int totalSeats = calculateTotalSeatsToReserve( ticketTypeRequests );

        paymentWith3rdParty( accountId, totalCost);
        reserveSeatsWith3rdParty( accountId, totalSeats );

    }


    private boolean validRequest( Long accountID, TicketTypeRequest[] ticketTypeRequests ) {

        // Java uses short-circuit evaluation. Will return false upon the first false conditional.

        return accountID > 0 &&
                validTicketTypeRequest(ticketTypeRequests) &&
                validTicketTotal(ticketTypeRequests) &&
                validTicketTypes(ticketTypeRequests);

    }


    private boolean validTicketTypeRequest( TicketTypeRequest[] ticketTypeRequests ) throws NullPointerException {

        if ( ticketTypeRequests == null ) throw new NullPointerException();

        if ( ticketTypeRequests.length > 0 && ticketTypeRequests.length <= 20 ) return true;
        else return false;

    }


    private boolean validTicketTotal( TicketTypeRequest[] ticketTypeRequests ) {

        int totalTickets = 0;

        for (TicketTypeRequest t: ticketTypeRequests) {
            totalTickets += t.getNoOfTickets();
            if (totalTickets > 20) return false;
        }

        if ( totalTickets > 0 && totalTickets <= 20 ) return true;
        else return false;

    }


    private boolean validTicketTypes( TicketTypeRequest[] ticketTypeRequests ) {

        int totalAdults = 0;
        int totalInfants = 0;

        for (TicketTypeRequest t: ticketTypeRequests) {
            if (t.getTicketType() == TicketTypeRequest.Type.ADULT) totalAdults += t.getNoOfTickets();
            else if (t.getTicketType() == TicketTypeRequest.Type.INFANT ) totalInfants += t.getNoOfTickets();
        }

        return ( totalAdults > 0 && totalInfants <= totalAdults );

        // An adult ticket must be purchased to purchase a Child or Infant ticket (Business Requirement).
        // Assumes 1 infant per lap. Infants cannot exceed Adults laps to sit on. Infants are not reserved a seat and sit on laps (Business Objective).

    }


    private int calculateTotalTicketCost( TicketTypeRequest[] ticketTypeRequests ) {

        int totalCost = 0;

        for ( TicketTypeRequest t: ticketTypeRequests ) {

            if ( t.getTicketType() == TicketTypeRequest.Type.ADULT ) totalCost += ( this.adultTicketCost * t.getNoOfTickets() );

            else if ( t.getTicketType() == TicketTypeRequest.Type.CHILD ) totalCost += ( this.childTicketCost * t.getNoOfTickets() );

            // Infant tickets are free.

        }

        return totalCost;

    }


    private int calculateTotalSeatsToReserve( TicketTypeRequest[] ticketTypeRequests ) {

        int totalSeats = 0;

        for ( TicketTypeRequest t: ticketTypeRequests ) {

            if ( t.getTicketType() == TicketTypeRequest.Type.ADULT || t.getTicketType() == TicketTypeRequest.Type.CHILD ) {

                totalSeats += t.getNoOfTickets();

                // Infant tickets are not allocated a seat.

            }

        }

        return totalSeats;

    }


    private void paymentWith3rdParty(Long accountId, int totalCost ) {
        ticketPaymentService.makePayment( accountId, totalCost );
    }


    private void reserveSeatsWith3rdParty( Long accountId, int totalSeats ) {
        // reserveSeat() takes the total of seats to reserve not just a single seat!
        seatReservationService.reserveSeat( accountId, totalSeats );
    }




}



