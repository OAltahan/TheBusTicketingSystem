package OnlineTicket;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Cursor;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import java.util.ArrayList;


public class SeatSelectionPane extends StackPane implements MyViewInterface, EventHandler {

    private final int viewId;

    SeatSelectionPane(int viewId) {
        ImageView iv = new ImageView("/OnlineTicket/bus.png");
        getChildren().add(iv);
        StackPane.setMargin(iv, new Insets(-35, 0, 0, 0));
        this.viewId = viewId;
        seats = new GridPane();

        seats.setMaxSize(510, 150);

        seats.setVgap(2);
        seats.setHgap(2);

        StackPane.setMargin(seats, new Insets(-35, 0, 0, 80));

        ArrayList list = MyView.currentReservation.getTicketByID(viewId).getJourney().getReservedSeatsList();

        int r = 1;
        for (int i = 0; i < 14; i++) {
            for (int e = 0; e < 5; e++) {
                if (e == 2 || e == 3 || ((i == 7 || i == 8) && e != 4) || (i == 13 && e == 4))
                    continue;

                Label a = myLabel("" + r++);
                if (list.contains(r-1)){
                    System.out.println("list contains " + r);
                    isReserved(a);
                }
                seats.add(a, i, e);
            }
        }
        {
            Label t = new Label(" ");
            t.setMinSize(35, 19);
            seats.add(t, 0, 2);
        }
        {
            Label t = new Label(" ");
            t.setMinSize(35, 19);
            seats.add(t, 0, 3);
        }
        getChildren().add(seats);
    }

    private GridPane seats;

    private Label myLabel(String te) {
        Label text = new Label(te);
        text.setMinSize(34, 37);
        text.setCursor(Cursor.HAND);
        text.setOnMouseClicked(this);

        text.setContentDisplay(ContentDisplay.CENTER);
        text.setAlignment(Pos.CENTER);
        GridPane.setValignment(text, VPos.BOTTOM);
        text.setId("seat");

        return text;
    }

    private void isReserved(Label l){
        l.setStyle(style2);
    }

    @Override
    public boolean isReady() {
        if (prevLabel == null)
        {
            Main.showErrorMessage("Select a Seat First!");
            return false;
        }

        saveIfNew();


        if(viewId == MyView.DEPARTURE_SEAT)
            OnlineTransactions.getList(false);



        return true;
    }

    @Override
    public int nextView() {
        if (((MyView) getParent()).getViewId() == MyView.DEPARTURE_SEAT)
            return MyView.RETURN_LIST;
        return MyView.PERSONAL_INFO;
    }

    @Override
    public boolean isAboutBeingNew() {
        return false;
    }

    @Override
    public boolean saveIfNew() {
        MyView.currentReservation.getTicketByID(viewId).reserveSeat(Integer.parseInt(prevLabel.getText()));
        return false;
    }

    Label prevLabel;

    private final static String style = "-fx-background-color: #55c6ff;-fx-border-color: #0003ff;";

    private final static String style2 = "-fx-background-color: #ff9a7f; -fx-border-color: #ff002c;";

    @Override
    public void handle(Event event) {
        Label source = (Label) event.getSource();

        if (source.getStyle().equals(style2))
        {
            Main.beep();
            return;
        }

        if (prevLabel != null) {
            prevLabel.setStyle("");
        }

        source.setStyle(style);
        prevLabel = source;
    }
}
