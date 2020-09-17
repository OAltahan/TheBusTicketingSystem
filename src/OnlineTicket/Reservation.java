package OnlineTicket;

import java.io.Serializable;

public class Reservation implements Serializable {


    private Ticket departureTicket;
    private Ticket returnTicket;
    private Person person;
    private int cost;
    private String id;

    public String getId() {
        return id;
    }

    public Reservation setId(String id) {
        this.id = id;
        return this;
    }

    public int getCost() {
        //also, it should not be in a state where a departureTicket is null,
        //but we call getDepartureTicket() in order to be in safe side
        int result = getDepartureTicket().getJourney().getPrice();

        //since a Reservation may not have a return ticket
        if (returnTicket != null)
            result += returnTicket.getJourney().getPrice();

        return result;
    }

    public boolean hasDepartureTicket() {
        return departureTicket != null;
    }


    private Ticket getDepartureTicket() {
        if (departureTicket == null)
            departureTicket = new Ticket();
        return departureTicket;
    }

    public Ticket getTicketByID(int id) {
        id = resolveTOEitherReturnOrDep(id);
        return id == Ticket.RETURN_TICKET ? getReturnTicket() : getDepartureTicket();
    }

    //since a ticket may be called by either DepratrureList or DepratureSeat
    // or other so it should be changed to be a single thing
    private static int resolveTOEitherReturnOrDep(int id) {
        switch (id) {
            case MyView.RETURN_LIST:
            case MyView.RETURN_SEAT:
                return Ticket.RETURN_TICKET;
        }
        return Ticket.DEPARTURE_TICKET;
    }
    private Ticket getReturnTicket() {
        if (returnTicket == null)
            returnTicket = new Ticket();
        return returnTicket;
    }


    public Person getPerson() {
        if (person == null)
            person = new Person();
        return person;
    }
}
