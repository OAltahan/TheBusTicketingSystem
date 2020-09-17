package OnlineTicket;

import javafx.geometry.Insets;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class ConfirmationInfoPane extends StackPane implements MyViewInterface {



    private String reservationCode;
    ConfirmationInfoPane() {
        GridPane gp = new GridPane();
        gp.setMaxSize(400,320);

        //gp.setGridLinesVisible(true);

        gp.add(myLabel("Name       "),1,0);
        gp.add(myLabel("TC         "),1,1);
        gp.add(myLabel("Ref. Code  "),1,2);
        gp.add(myLabel("Dep. Ticket"),1,3);

        gp.add(new TicketInfoPane(MyView.currentReservation.getTicketByID(Ticket.DEPARTURE_TICKET).getJourney().getFrom(),
                MyView.currentReservation.getTicketByID(Ticket.DEPARTURE_TICKET).getJourney().getDestination(),
                MyView.currentReservation.getTicketByID(Ticket.DEPARTURE_TICKET).getJourney().getDate(),
                MyView.currentReservation.getTicketByID(Ticket.DEPARTURE_TICKET).getJourney().getDepTime(),
                MyView.currentReservation.getTicketByID(Ticket.DEPARTURE_TICKET).getJourney().getArrivalTime()),
                0,4,2,1);

        if (MyView.currentReservation.getTicketByID(Ticket.RETURN_TICKET).getSeat() != 0)
        {
            gp.add(myLabel("Ret. Ticket"),2,3);
            gp.add(new TicketInfoPane(MyView.currentReservation.getTicketByID(Ticket.RETURN_TICKET).getJourney().getFrom(),
                            MyView.currentReservation.getTicketByID(Ticket.RETURN_TICKET).getJourney().getDestination(),
                            MyView.currentReservation.getTicketByID(Ticket.RETURN_TICKET).getJourney().getDate(),
                            MyView.currentReservation.getTicketByID(Ticket.RETURN_TICKET).getJourney().getDepTime(),
                            MyView.currentReservation.getTicketByID(Ticket.RETURN_TICKET).getJourney().getArrivalTime()),
                    2,4,2,1);
        }

        //
        //gp.setGridLinesVisible(true);

        gp.add(myTextField(MyView.currentReservation.getPerson().getName()+" "+MyView.currentReservation.getPerson().getSureName()),2,0);
        gp.add(myTextField(MyView.currentReservation.getPerson().getTc()),2,1);
        reservationCode = MyView.currentReservation.getTicketByID(Ticket.DEPARTURE_TICKET).getJourney().getId()+"/"+MyView.currentReservation.getTicketByID(Ticket.DEPARTURE_TICKET).getSeat();
        gp.add(myTextField(reservationCode),2,2);
//        gp.add(myTextField("Card Expiry Year"),1,3);

        gp.setVgap(10);
        gp.setHgap(10);

        StackPane.setMargin(gp,new Insets(40,0,0,0));

        getChildren().add(gp);
    }

    private TextField myTextField(String text) {
        TextField tf = new TextField(text);
        tf.setEditable(false);
        tf.setFont(Font.font("Times New Roman", FontWeight.NORMAL, 16));
        return tf;
    }

    private Text myLabel(String label) {
        Text text = new Text(label);
        text.setFont(Font.font("Times New Roman", FontWeight.NORMAL, 16));
        text.setFill(Color.GRAY);
//        text.maxWidth(500);
        //GridPane.setMargin(text, new Insets(20, 10, 0, 0));
        return text;
    }



    @Override
    public boolean isReady() {
        saveIfNew();
        return true;
    }

    @Override
    public int nextView() {
        return MyView.NEW_RESERVATION;
    }

    @Override
    public boolean isAboutBeingNew() {
        return false;
    }

    @Override
    public boolean saveIfNew() {
        MyView.reservations.add(MyView.currentReservation.setId(reservationCode));
        MyView.currentReservation = new Reservation();

        IO.save();

        return false;
    }

    private class TicketInfoPane extends StackPane{
        TicketInfoPane(String from, String destination, String date, String depTime,String arrTime) {
            GridPane gp = new GridPane();
            gp.setHgap(5);
            gp.setMaxSize(175,200);
            gp.add(myLabel("From       "),0,0);
            gp.add(myLabel("Destination"),0,1);
            gp.add(myLabel("Date       "),0,2);
            gp.add(myLabel("Dep. Time  "),0,3);
            gp.add(myLabel("Arr. Time  "),0,4);

            gp.add(myTextField(from),1,0);
            gp.add(myTextField(destination),1,1);
            gp.add(myTextField(date),1,2);
            gp.add(myTextField(depTime),1,3);
            gp.add(myTextField(arrTime),1,4);
            getChildren().add(gp);
        }
    }
}