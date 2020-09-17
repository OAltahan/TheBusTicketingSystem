package OnlineTicket;


import java.io.Serializable;

public class Ticket implements Serializable {

    public final static int DEPARTURE_TICKET = MyView.DEPARTURE_LIST;
    public final static int RETURN_TICKET = MyView.RETURN_LIST;

    private int seat;
    private Journey journey;

    public Ticket setJourney(Journey journey) {
        this.journey = journey;
        return this;
    }

    public int getSeat() {
        return seat;
    }

    public Ticket reserveSeat(int newSeat) {
        if (newSeat != seat) {
            //so we need to replace the old seat
            if (seat != 0)
                if (!journey.getReservedSeatsList().remove(new Integer(seat)))
                    return this;

            journey.getReservedSeatsList().add(seat=newSeat);
        }
        return this;
    }

    public Journey getJourney() {
        if (journey == null)
            journey = new Journey();

        return journey;
    }
}
