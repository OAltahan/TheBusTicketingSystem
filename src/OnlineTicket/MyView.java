package OnlineTicket;

import javafx.animation.FadeTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.util.ArrayList;

public class MyView extends StackPane implements EventHandler {


    //WELCOME variable may not be used
    public final static int WELCOME = 0;

    public final static int NEW_RESERVATION = 1;

    public final static int ONE_WAY_LIST = 2;
    public final static int DEPARTURE_LIST = 3;
    public final static int RETURN_LIST = 4;

    public final static int ONE_WAY_SEAT = 5;
    public final static int DEPARTURE_SEAT = 6;
    public final static int RETURN_SEAT = 7;

    public final static int PERSONAL_INFO = 8;

    public final static int PAYMENT = 9;

    public final static int CONFIRMATION = 10;

    public static Reservation currentReservation;

    public static ArrayList<Reservation> reservations ;
    public static ArrayList<Journey> generatedJourney ;


    public int getViewId() {
        return id;
    }

    private final int id;
    private Insets imageInsets;
    private final static ImageView imageView = new ImageView("/OnlineTicket/label.png");
    private static Text title = headerText();

    private MyView previousPage;
    private MyView nextPage;

    private Button toNext;

    private final int width;
    private final int height;
    private ImageView toPrevious;

    private final Pane pane;

    private MyView(int w, int h, int id, Pane pane) {
        this.id = id;

        this.width = w;
        this.height = h;

        this.pane = pane;

        setMinSize(w, h);
        setMaxSize(w, h);
        setId("myView");

        initChildren();

        if (pane != null)//so it wont throw an exception
            getChildren().add(pane);

        getChildren().add(toNext);
    }

    private void initChildren() {
        initHeaderImage();
        initToNextButton();
    }

    private void initToPreviousLabel() {
        toPrevious = new ImageView("/OnlineTicket/back.jpg");
        toPrevious.setFitHeight(50);
        toPrevious.setFitWidth(70);
        toPrevious.setCursor(Cursor.HAND);
        toPrevious.setOnMouseClicked(this);
        StackPane.setMargin(toPrevious, new Insets(-height + 80, 0, 0, -width + 150));
    }

    private void initToNextButton() {
        toNext = new Button(findNextButtonText());
        toNext.setPadding(new Insets(10, 0, 10, 0));
        toNext.setPrefWidth(width / 6 * 5);
        toNext.setCursor(Cursor.HAND);
        toNext.setOnAction(this);
        StackPane.setMargin(toNext, new Insets(height - 111, 0, 0, 0));
        toNext.setId("nextButton");
    }

    private void initHeaderImage() {
        imageInsets = new Insets(-(height - 31), 0, 0, 0);
        StackPane.setMargin(imageView, imageInsets);
        StackPane.setMargin(title, imageInsets);
    }

    private String findNextButtonText() {
        switch (id) {
            case NEW_RESERVATION:
                return "List";
            case ONE_WAY_LIST:
            case DEPARTURE_LIST:
            case RETURN_LIST:
                return "Choose Seat";
            case ONE_WAY_SEAT:
            case RETURN_SEAT:
                return "My Info.";
            case DEPARTURE_SEAT:
                return "Return";
            case PERSONAL_INFO:
                return "Payment";
            case PAYMENT:
                return "Pay";
            case CONFIRMATION:
                return "Done";
            default:
                return "Next";
        }
    }

    private static Text headerText() {
        title = new Text("\nNEW TICKET");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        title.setFill(Color.WHITE);
        headerTextEffect(title);
        return title;
    }

    private static void headerTextEffect(Text text) {
        //Transition
        FadeTransition ft = new FadeTransition(Duration.millis(1000), text);
        ft.setFromValue(1.0);
        ft.setToValue(0.1);
        ft.setCycleCount(Timeline.INDEFINITE);
        ft.setAutoReverse(true);
        ft.play();

        //Shadow
        DropShadow ds = new DropShadow();
        ds.setOffsetY(3.0f);
        ds.setColor(Color.color(0.1f, 0.1f, 0.1f));
        text.setEffect(ds);
    }


    //It is necessary to refresh each MyView before adding it,
    //so it takes the header title and image
    //It also set the title text to be the proper one
    MyView refresh() {
        StackPane.setMargin(imageView, imageInsets);
        StackPane.setMargin(title, imageInsets);
        getChildren().addAll(imageView, title);
        title.setText(setTitle(id));

        //to prevent the first ComboBox from auto sliding
        Platform.runLater(() -> {
            toNext.requestFocus();
        });

        return this;
    }

    @Override
    public void handle(Event event) {
        try {
            //to prevent pressing next quickly
            //Thread.sleep(100);
        } catch (Exception interrupted) {}

        if (event.getSource() == toNext) {
            if (!((MyViewInterface) pane).isReady())
            {
                if( !((MyViewInterface)pane).isAboutBeingNew() )
                  return;
            }else {
                setNextPage(newView(((MyViewInterface) pane).nextView()));
            }
        }

        //Actually I can just set the new child to be in th first index,
        //since it is the only child in children, but to avoid exception
        //if any child is added later to this.parent
        ObservableList<Node> children = ((StackPane) getParent()).getChildren();
        children.remove(this);
        children.add(event.getSource().equals(toNext) ? nextPage.refresh() : previousPage.refresh());
    }

    private static String setTitle(int id) {
        //String needs  new line to shift the text of label down to be fitted with label
        //so i can use the insets of label to be the text insets
        switch (id) {
            case NEW_RESERVATION:
                return "\nNEW TICKET";
            case ONE_WAY_LIST:
            case DEPARTURE_LIST:
                return "\nDEP. TIME";
            case RETURN_LIST:
                return "\nRETURN TIME";
            case ONE_WAY_SEAT:
            case DEPARTURE_SEAT:
                return "\nDEP. SEAT";
            case RETURN_SEAT:
                return "\nRETURN SEAT";
            case PERSONAL_INFO:
                return "\nYOUR INFO";
            case PAYMENT:
                return "\nPAYMENT";
            case CONFIRMATION:
                return "\nYOUR TICKET";
            default:
                return "\nDETAILS";
        }
    }

    public MyView getPreviousPage() {
        return previousPage;
    }

    public void setPreviousPage(MyView previousPage) {
        //since there is a previous page, a previous button should be added
        initToPreviousLabel();
        getChildren().add(toPrevious);
        this.previousPage = previousPage;
    }

    public MyView getNextPage() {
        return nextPage;
    }

    public void setNextPage(MyView nextPage) {
        this.nextPage = nextPage;
        if (id != CONFIRMATION)
            nextPage.setPreviousPage(this);
    }

    public ImageView getToPrevious() {
        return toPrevious;
    }

    public static MyView newView(int id) {
        switch (id) {

            case NEW_RESERVATION:
                return new MyView(600, 500,MyView.NEW_RESERVATION,new NewReservationPane());

            case DEPARTURE_LIST:
            case ONE_WAY_LIST:
            case RETURN_LIST:
                return new MyView(600, 600,id,new ListChoosingPane(id));

            case DEPARTURE_SEAT:
            case ONE_WAY_SEAT:
            case RETURN_SEAT:
                return new MyView(700, 400,id,new SeatSelectionPane(id));

            case PERSONAL_INFO:
                return new MyView(500, 400,id,new PersonalInfoPane());
            case PAYMENT:
                return new MyView(550, 500,id,new PaymentPane());

            case CONFIRMATION:
                return new MyView(550, 500,id,new ConfirmationInfoPane());

            default:
                    return new MyView(100, 100,id,null);
        }
        //return null;
    }

}