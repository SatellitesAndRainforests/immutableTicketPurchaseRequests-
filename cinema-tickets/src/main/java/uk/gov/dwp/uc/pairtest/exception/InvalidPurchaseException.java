package uk.gov.dwp.uc.pairtest.exception;

import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;


/**
 * @author Mark Start
 * @Date 29/11/2022
 */


public class InvalidPurchaseException extends RuntimeException {



    public InvalidPurchaseException( String message ) {
        super(message);
    }



    // Used for debugging
    public InvalidPurchaseException( Long accountId, TicketTypeRequest[] ticketTypeRequests ) {

        super( "Invalid Purchase Exception" );

        StringBuilder sb = new StringBuilder();
        sb.append("Invalid Purchase Request for AccountID:" + "\t");
        sb.append(accountId.toString() + ",\t");
        sb.append("TicketTypeRequest[]:" + "\t");

        sb.append("[ ");
        for ( TicketTypeRequest t: ticketTypeRequests) {
            sb.append("{");
            sb.append("ticketType: " +t.getTicketType() + ", ");
            sb.append("#ofTickets: " + t.getNoOfTickets() + " ");
            sb.append("}, ");
        }
        sb.append("]");

        String message = sb.toString();

        System.err.println(message);

    }






}