package OnlineTicket;

import javafx.animation.FadeTransition;
import javafx.animation.RotateTransition;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class NewReservationPane extends GridPane implements EventHandler, MyViewInterface {

    private RadioButton rBOneWay;
    private RadioButton rBRoundTrip;

    private Text textHaveATicket;

    private MyComboBox cBFrom;
    private MyComboBox cBTo;

    private DatePicker dpFrom;
    private DatePicker dpTo;

    private ImageView switchIcon;

    private Text toDateText;

    NewReservationPane() {
        initRadioButton();
        initHaveATicketText();
        initComboPlace();
        initSwitchIcon();

        //setGridLinesVisible(true);//**********************************//


        setHgap(25);
        setVgap(8);

        addChildren();
        adjustSize();

        setPadding(new Insets(75, 0, 0, 37));
        MyView.currentReservation = new Reservation();

    }

    private static double height;
    private static double width;

    private void adjustSize() {
        if (height + width == 0)
            Platform.runLater(() -> {
                height = getHeight();
                width = getWidth() * .90;
                adjustSizes();
            });
        else {
            adjustSizes();
        }
    }

    private void adjustSizes() {
        setMinWidth(width);
        double heightP = height * .08;

        dpFrom.setMinSize(width - 10, heightP);
        dpTo.setMinSize(width - 10, heightP);
        switchIcon.setFitHeight(heightP);
        switchIcon.setFitWidth(heightP);

        double widthP = ((width - heightP) / 2) - 30;
        cBFrom.setMaxSize(widthP, heightP);
        cBFrom.setMinSize(widthP, heightP);
        cBTo.setMaxSize(widthP, heightP);
        cBTo.setMinSize(widthP, heightP);

        double heightP2 = height * .030;

        cBFrom.getEditor().setFont(Font.font("Serif", FontWeight.NORMAL, heightP2 * .8));
        cBTo.getEditor().setFont(Font.font("Serif", FontWeight.NORMAL, heightP2 * .8));
        dpFrom.getEditor().setFont(Font.font("Serif", FontWeight.NORMAL, heightP2 * 1.1));
        dpTo.getEditor().setFont(Font.font("Serif", FontWeight.NORMAL, heightP2 * 1.1));

        rBOneWay.setFont(Font.font("Times New Roman", FontWeight.NORMAL, heightP2));
        rBRoundTrip.setFont(Font.font("Times New Roman", FontWeight.NORMAL, heightP2));
        textHaveATicket.setFont(Font.font("Times New Roman", FontWeight.BOLD, heightP2 * 1.1));
        GridPane.setMargin(textHaveATicket, new Insets(2, 00, 2, width / 3 / 5));
    }


    private void addChildren() {
        add(rBOneWay, 1, 0);
        add(rBRoundTrip, 0, 0);
        add(textHaveATicket, 3, 0);
        add(myLabel("From"), 0, 1);
        add(myLabel("Destination"), 3, 1);
        add(cBFrom, 0, 2, 2, 1);
        add(switchIcon, 2, 2);
        add(cBTo, 3, 2);
        add(myLabel("Date"), 0, 3);
        add(dpFrom = myDatePicker(), 0, 4, 4, 1);
        add(toDateText = myLabel("Return Date"), 0, 5);
        add(dpTo = myDatePicker(), 0, 6, 4, 1);
    }

    private void initHaveATicketText() {
        textHaveATicket = new Text("\uD83D\uDC49 I already have a ticket!");
        textHaveATicket.setFill(Color.rgb(253, 130, 4));
        //GridPane.setMargin(textHaveATicket, new Insets(0, 0, 0, 25));
        textHaveATicket.setOnMouseEntered(event -> textHaveATicket.setStyle("-fx-underline: true;"));
        textHaveATicket.setOnMouseExited(event -> textHaveATicket.setStyle("-fx-underline: false;"));
        textHaveATicket.setCursor(Cursor.HAND);
        //textHaveATicket.setOnMouseClicked(event -> Project.Main.setScene(Main.HAVE_A_TICKET));
    }

    private void initRadioButton() {
        ToggleGroup group = new ToggleGroup();
        rBOneWay = myRadioButton("One Way", group);
        rBRoundTrip = myRadioButton("Round Trip", group);
        rBRoundTrip.setSelected(true);

    }

    private Text myLabel(String label) {
        Text text = new Text(label);
        Platform.runLater(() -> text.setFont(Font.font("Times New Roman", FontWeight.NORMAL, getHeight() * .0375)));
        text.setFill(Color.GRAY);
        GridPane.setMargin(text, new Insets(20, 10, 0, 0));
        return text;
    }

    private static final ObservableList<String> stops = FXCollections.observableArrayList(OnlineTransactions.locationsKeys.keySet());

    private void initComboPlace() {
        cBFrom = new MyComboBox(stops);
        cBTo = new MyComboBox(stops);
        cBFrom.setPrefSize(204, 35);
        cBTo.setPrefSize(204, 35);
    }

    private void initSwitchIcon() {
        switchIcon = new ImageView(new Image("swap.png"));
        switchIcon.setCursor(Cursor.HAND);
        RotateTransition rt = new RotateTransition(Duration.millis(300), switchIcon);
        rt.setByAngle(360);
        rt.setCycleCount(1);
        rt.setAutoReverse(true);
        switchIcon.setOnMouseClicked(event -> {
            rt.play();
            NewReservationPane.this.handle(event);
        });

    }

    private DatePicker myDatePicker() {
        DatePicker datePicker = new DatePicker();
        datePicker.setStyle("-fx-font: 20px \"Serif\";" +
                "-fx-background-insets: 0;" +
                "-fx-padding: 0;");
        datePicker.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue)
                    datePicker.show();
            }
        });
        datePicker.setDayCellFactory((p) -> new DateCell() {
            @Override
            public void updateItem(LocalDate ld, boolean bln) {
                super.updateItem(ld, bln);
                setDisable(ld.isBefore(LocalDate.now()) || ld.isAfter(LocalDate.now().plusYears(1)));
            }
        });

        Platform.runLater(() -> {
            datePicker.getEditor().clear();
        });


        return datePicker;
    }

    private RadioButton myRadioButton(String text, ToggleGroup group) {
        RadioButton radioButton = new RadioButton(text);
        radioButton.setCursor(Cursor.HAND);
        radioButton.setToggleGroup(group);
        radioButton.setFocusTraversable(false);
        radioButton.setOnAction(this);
        return radioButton;
    }

    @Override
    public void handle(Event event) {
        if (event.getSource() == switchIcon) {
            String temp = cBFrom.getSelectionModel().getSelectedItem();
            cBFrom.getSelectionModel().select(cBTo.getSelectionModel().getSelectedItem());
            cBTo.getSelectionModel().select(temp);
            cBFrom.isSwitched();
            cBTo.isSwitched();
            return;
        } else {
            boolean isOneWay = rBOneWay.isSelected();
            mySetVisible(dpTo, isOneWay);
            mySetVisible(toDateText, isOneWay);
        }
    }

    private void mySetVisible(Node node, boolean vis) {
        FadeTransition ft = new FadeTransition(Duration.millis(500), node);
        ft.setFromValue(vis ? 1 : 0);
        ft.setToValue(vis ? 0 : 1);
        node.setDisable(vis);
        ft.play();
    }

    public boolean isReady() {
        String from = (String) cBFrom.getSelectionModel().getSelectedItem();
        String to = (String) cBTo.getSelectionModel().getSelectedItem();
        String message = "";

        if (from == null || to == null || from.isEmpty() || to.isEmpty()) {
            message = "Fill both\nFrom  and  Destination points!!";
            
        } else if (to.equals(from)) {
            message = "Fill both From  and  Destination points\nWith different values!!";
        } else if (dpFrom.getEditor().getText().isEmpty()) {
            message = "Please, Select a Departure Date";
        } else if (dpTo.getEditor().getText().isEmpty() && !dpTo.isDisable()) {
            message = "Please, Select a Return Date\nOr choose One-Way!";
        } else if (!dpTo.isDisable() && dpTo.getValue().isBefore(dpFrom.getValue())) {
            message = "Return Date should NOT be before Departure date!";
        }

        if (!message.isEmpty()) {
            Main.showErrorMessage(message);
            return false;
        }

        if (!saveIfNew())
            return false;

        if (OnlineTransactions.getList(true)) {
            if (OnlineTransactions.depList.isEmpty()) {
                Main.showErrorMessage("No Journey found!\nTry with different data");
                return false;
            }
        } else {
            Main.showErrorMessage("An Error occurred while connection to internet\nTry again");
            return false;
        }


        return true;
    }


    @Override
    public int nextView() {
        if (rBOneWay.isSelected())
            return MyView.ONE_WAY_LIST;

        return MyView.DEPARTURE_LIST;
    }

    @Override
    public boolean saveIfNew() {
        boolean isNew;
        if (isNew = MyView.currentReservation == null)
            MyView.currentReservation = new Reservation();

        String from = cBFrom.getSelectionModel().getSelectedItem();
        String to = cBTo.getSelectionModel().getSelectedItem();
        String date = dpFrom.getValue().format(BOARDING_DATE_FORMAT);
        boolean isSelected = rBRoundTrip.isSelected();

        Ticket ticket = MyView.currentReservation.getTicketByID(Ticket.RETURN_TICKET);

        if (!isNew) {
            if (MyView.currentReservation.hasDepartureTicket()) {
                isAboutBeingNew = false;
            } else if (from.equals(MyView.currentReservation.getTicketByID(Ticket.DEPARTURE_TICKET).getJourney().getFrom()) &&
                    to.equals(MyView.currentReservation.getTicketByID(Ticket.DEPARTURE_TICKET).getJourney().getDestination()) &&
                    date.equals(MyView.currentReservation.getTicketByID(Ticket.DEPARTURE_TICKET).getJourney().getDate()) &&
                    ((isSelected && ticket != null) || (!isSelected && ticket == null))) {
                isAboutBeingNew = true;
                return false;
            }
        }

        MyView.currentReservation.getTicketByID(Ticket.DEPARTURE_TICKET)
                .getJourney()
                .setFrom(from)
                .setDestination(to)
                .setDate(date);


        if (isSelected)
            MyView.currentReservation.getTicketByID(Ticket.RETURN_TICKET)
                    .getJourney()
                    .setFrom(to)
                    .setDestination(from)
                    .setDate(dpTo.getValue().format(BOARDING_DATE_FORMAT));


        return true;
    }

    private boolean isAboutBeingNew;

    @Override
    public boolean isAboutBeingNew() {
        return isAboutBeingNew;
    }


    public static final DateTimeFormatter BOARDING_DATE_FORMAT = DateTimeFormatter.ofPattern("dd.MM.yyyy");

}
