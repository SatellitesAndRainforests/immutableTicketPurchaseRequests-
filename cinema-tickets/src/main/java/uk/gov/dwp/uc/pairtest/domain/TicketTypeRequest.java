package uk.gov.dwp.uc.pairtest.domain;


/**
 * @author Mark Start
 * @Date 30/11/22
 *
 *
 * Immutable Object
 *
 */


public final class TicketTypeRequest {

    private final int noOfTickets;
    private final Type type;

    public enum Type {
        ADULT, CHILD , INFANT
    }

    public TicketTypeRequest( Type type, int noOfTickets ) {

        if ( type == null ) throw new NullPointerException("Type cannot be null");
        if ( noOfTickets < 1 || noOfTickets > 20 ) throw new IllegalArgumentException("Number of tickets must be greater than 0 and <= 20");

        this.type = type;
        this.noOfTickets = noOfTickets;

    }

    public int getNoOfTickets() {
        return noOfTickets;
    }

    public Type getTicketType() {
        return type;
    }


}


