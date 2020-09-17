package OnlineTicket;

import java.io.Serializable;
import java.util.ArrayList;

public class Journey implements Serializable {

    private String id;
    private String from;
    private String destination;
    private int price;
    private String duration;
    private String date;
    private String depTime;
    private String arrivalTime;
    private ArrayList<Integer> reservedSeatsList;

    public ArrayList<Integer> getReservedSeatsList() {
        if (reservedSeatsList == null)
            reservedSeatsList = new ArrayList<>();
        return reservedSeatsList;
    }

    public Journey reserveSeatsFromNew(int no) {
        reservedSeatsList = new ArrayList<>();
        while (no -- > 0) {
            int n = (int) ((Math.random() * 10 * 3.5)) + 1;//+1 to avoid having ZERO
            if (getReservedSeatsList().contains(n))
                no++;//to try again
            else
            {
                getReservedSeatsList().add(n);
                System.out.println("Seat No "+n + ", is added randomly!");
            }
        }
        return this;
    }

    public int getNo() {
        return no;
    }

    public Journey setNo(int no) {
        this.no = no;
        return this;
    }

    private int no;

    public String getFrom() {
        return from;
    }

    public String getId() {
        return id != null ? id : (id = generateId());
    }

    private String generateId() {
        int fro = new ArrayList<>(OnlineTransactions.locationsKeys.keySet()).indexOf(from);
        int to = new ArrayList<>(OnlineTransactions.locationsKeys.keySet()).indexOf(destination);
        int year = date.charAt(date.length() - 1);
        int month = Integer.parseInt(date.substring(3, 5));
        int day = Integer.parseInt(date.substring(0, 2));

        return intToStringCode(fro).concat(intToStringCode(to)) + (year) +
                intToChar(month).concat(intToChar(day).concat(intToChar(no)));
    }

    private static String intToStringCode(int x) {
        char a = (char) ((x / 28) + 65);

        if (a > 'Z')
            a += 10;
        return a + intToChar((x % 28) + 65);
    }


    private static String intToChar(int x) {
        char b = (char) ((x % 28) + 65);
        if (b > 'Z')
            b += 8;
        return b > 'Z' ? "" + ((char) (b + 8)) : "" + b;
    }


    public Journey setFrom(String from) {
        this.from = from;
        return this;
    }

    public String getDestination() {
        return destination;
    }

    public Journey setDestination(String destination) {
        this.destination = destination;
        return this;
    }

    public int getPrice() {
        return price;
    }

    public Journey setPrice(int price) {
        this.price = price;
        return this;
    }


    public String getDuration() {
        return duration;
    }

    public Journey setDuration(String duration) {
        this.duration = duration;
        return this;
    }

    public String getDate() {
        return date;
    }

    public Journey setDate(String date) {
        this.date = date;
        return this;
    }

    public String getDepTime() {
        return depTime;
    }

    public Journey setDepTime(String depTime) {
        this.depTime = depTime;
        return this;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public Journey setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
        return this;
    }
}
