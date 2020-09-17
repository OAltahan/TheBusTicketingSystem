package OnlineTicket;

import javafx.beans.property.SimpleStringProperty;

public class MyTableList {
    private final SimpleStringProperty depTime;
    private final SimpleStringProperty duration;
    private final SimpleStringProperty arrTime;
    private final SimpleStringProperty seats;
    private final SimpleStringProperty price;

    public MyTableList(String depTime, String duration, String arrTime, String seats, String price) {
        this.depTime = new SimpleStringProperty(depTime);
        this.duration = new SimpleStringProperty(duration);
        this.arrTime = new SimpleStringProperty(arrTime);
        this.seats = new SimpleStringProperty(seats);
        this.price = new SimpleStringProperty(price);
    }


    //Getters are necessary here because of data observable list of the table
    public String getDuration() {
        return duration.get();
    }


    public String getDepTime() {
        return depTime.get();
    }

    public String getArrTime() {
        return arrTime.get();
    }

    public String getSeats() {
        return seats.get();
    }


    public String getPrice() {
        return price.get();
    }

}
