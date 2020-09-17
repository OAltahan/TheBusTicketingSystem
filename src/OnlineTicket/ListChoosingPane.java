package OnlineTicket;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;

import java.util.ArrayList;

public class ListChoosingPane extends StackPane implements MyViewInterface {

    private TableView<MyTableList> table;

    private int height = 600;
    private int width = 600;
    private final int viewId;


    ListChoosingPane(int vieId) {
        this.viewId = vieId;
        myTable();
        table.setMaxHeight(height * .7);
        table.setMaxWidth(height * .85);
        StackPane.setMargin(table, new Insets(-height / 25, 0, 0, 0));
        getChildren().add(table);
    }

    private void myTable() {
        table = new TableView();
        //table.setEditable(true);

        //!!!!!!!!!!!!!!!!!!!!!!!!!!!1showed be checked
        String from = MyView.currentReservation.getTicketByID(viewId).getJourney().getFrom();
        String to = MyView.currentReservation.getTicketByID(viewId).getJourney().getDestination();

        TableColumn tc = new TableColumn<>(from);
        tc.prefWidthProperty().bind(table.widthProperty().divide(3.5));
        tc.setCellValueFactory(new PropertyValueFactory<MyTableList, String>("depTime"));
        tc.setSortable(false);

        TableColumn tc2 = new TableColumn<>("Duration");
        tc2.prefWidthProperty().bind(table.widthProperty().divide(6.5));
        tc2.setCellValueFactory(new PropertyValueFactory<MyTableList, String>("duration"));
        tc2.setSortable(false);

        TableColumn tc3 = new TableColumn<>(to);
        tc3.prefWidthProperty().bind(table.widthProperty().divide(3.5));
        tc3.setCellValueFactory(new PropertyValueFactory<MyTableList, String>("arrTime"));
        tc3.setSortable(false);

        TableColumn tc4 = new TableColumn<>("Seats");
        tc4.prefWidthProperty().bind(table.widthProperty().divide(8.7));
        tc4.setCellValueFactory(new PropertyValueFactory<MyTableList, String>("seats"));
        tc4.setSortable(false);


        TableColumn tc5 = new TableColumn<>("Price");
        tc5.prefWidthProperty().bind(table.widthProperty().divide(8.7));
        tc5.setCellValueFactory(new PropertyValueFactory<MyTableList, String>("price"));
        tc5.setSortable(false);

        table.getColumns().addAll(tc, tc2, tc3, tc4, tc5);

        //new Thread(() ->

        initData();

        //).start();

        table.setItems(data);
    }

    private void initData() {

        data = FXCollections.observableArrayList();

        ArrayList<String[]> list ;

        if(viewId == MyView.RETURN_LIST)
            list = OnlineTransactions.returnList;
        else
            list = OnlineTransactions.depList;

        for (int i = 0; i < list.size(); i++) {
            data.add(new MyTableList(
                    adjustLength(list.get(i)[0], 22),
                    adjustLength(list.get(i)[1], 12),
                    adjustLength(list.get(i)[2], 22),
                    adjustLength(list.get(i)[3], 8),
                    adjustLength(list.get(i)[4], 8)));
        }

    }

    private ObservableList<MyTableList> data = FXCollections.observableArrayList();
    ;

    private String adjustLength(String a, int l) {
        for (int i = a.length(); i < l; i++) {
            a = " " + a;
        }
        return a;
    }

    @Override
    public boolean isReady() {
        //since the list starts from 0
        int selectedJourneyStartsFrom0 = table.getSelectionModel().getSelectedIndex();

        //so no journey is selected
        if (selectedJourneyStartsFrom0 == -1) {
            Main.showErrorMessage("Please choose a journey first!");
            return false;
        }

        if (Integer.parseInt(data.get(selectedJourneyStartsFrom0).getSeats().trim()) == 0) {
            Main.showErrorMessage("No seats In this Journey!\nPlease choose another one.");
            return false;
        }

        saveIfNew();

        return true;
    }

    @Override
    public int nextView() {
        return (((MyView) getParent()).getViewId()) + 3;
    }




    //since isAboutBeingNew() is not implemented yet, we ignore the return of this method
    @Override
    public boolean saveIfNew() {
        MyTableList selectedJourney = table.getSelectionModel().getSelectedItem();

        Journey journey = new Journey()
                .setDuration(selectedJourney.getDuration().trim())
                .setDepTime(selectedJourney.getDepTime().trim())
                .setArrivalTime(selectedJourney.getArrTime().trim())
                .setPrice(Integer.parseInt(selectedJourney.getPrice().trim()))
                .setNo(table.getSelectionModel().getSelectedIndex())
                .reserveSeatsFromNew(37-Integer.parseInt(selectedJourney.getSeats().trim()))
                .setDate(MyView.currentReservation.getTicketByID(viewId).getJourney().getDate())
                .setFrom(MyView.currentReservation.getTicketByID(viewId).getJourney().getFrom())
                .setDestination(MyView.currentReservation.getTicketByID(viewId).getJourney().getDestination()
                );

        String id = journey.getId();

        for (Journey i : MyView.generatedJourney){

            if (i.getId().equals(id))
            {
                journey = i;
                break;
            }
        }

        MyView.currentReservation.getTicketByID(viewId).setJourney(journey);

        MyView.generatedJourney.add(journey);

        return false;
    }

    //to avoid recalculation if the user came back to same page again with same info
    //to be implemented...........
    @Override
    public boolean isAboutBeingNew() {
        return false;
    }
    private boolean isAboutBeingNew;
}
