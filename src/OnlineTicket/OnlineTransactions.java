package OnlineTicket;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

class OnlineTransactions {

    public static Map<String, String> locationsKeys = getLocations();


//public static final Pair
    /*public static void main(String[] args) {
        getLocations();
        getList("ALIBEYKOY TESIS", "ANKARA", new Date());
    }*/

    private static Map getLocations() {
        //! It is better to have done this in a thread, then it will not be blocking.
        Map<String, String> map = new LinkedHashMap<>();
        //new Thread(() -> {
        try {
            Document document = Jsoup
                    .connect("https://www.metroturizm.com.tr/DataProvider/GetTerminals")
                    .timeout(5000)
                    .userAgent("Mozilla")
                    .post();

            JSONObject response = new JSONObject(document.body().text());
            JSONArray array = response.getJSONArray("rc1");
            for (int i = 0, limit = array.length(); i < limit; i++) {
                JSONObject o = array.getJSONObject(i);
                String name = o.getString("BRANCHNAME");
                String code = o.getString("BRANCHCODE");

                map.put(name, code);
            }

        } catch (Exception e) {
            Main.showErrorMessage("Please insure internet connection\n" +
                    "and try again later\n" +
                    "Terminating.....");
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(2500);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                        System.exit(0);
                    }
                    System.exit(0);
                }
            }).start();
        }
        //}).start();
        return map;
    }

    public static ArrayList<String[]> depList = new ArrayList<>();
    public static ArrayList<String[]> returnList = new ArrayList<>();



    public static boolean getList(boolean isDepList) {

        Ticket ticket;
        if (isDepList) {
            depList = new ArrayList<>();
            ticket = MyView.currentReservation.getTicketByID(Ticket.DEPARTURE_TICKET);
        }
        else {
            returnList = new ArrayList<>();
            ticket = MyView.currentReservation.getTicketByID(Ticket.RETURN_TICKET);
        }


        try {
            Document document = Jsoup.connect("https://www.metroturizm.com.tr/DataProvider/GetJourneyList")
                    .timeout(10000)
                    .userAgent("Mozilla")
                    .data("boarding", locationsKeys.get(ticket.getJourney().getFrom()))
                    .data("landing", locationsKeys.get(ticket.getJourney().getDestination()))
                    .data("boardingDate", ticket.getJourney().getDate())
                    .data("returnDate", "30.12.2018")
                    .data("isBothWay", "false")
                    .post();

            JSONObject response = new JSONObject(document.body().text());
            JSONObject oneWay = response.getJSONObject("oneWay");
            JSONArray rc1 = oneWay.getJSONArray("rc1");

            String lastDep = "\000";

            for (int i = 0, limit = rc1.length(); i < limit; i++) {
                JSONObject object = rc1.getJSONObject(i);

                String depTime = object.get("BINIS_SAATI").toString();
                String duration = object.get("SURE").toString();
                String arrivalTime = object.get("INIS_SAATI").toString();
                String price = String.valueOf((int)(double)(Double)object.get("GEN_WEBPRICE"));
                String availableSeats = toAvailableSeat(object.get("GEN_SOLDSEAT"));

                if (depTime.compareTo(lastDep) < 0)
                    break;

                lastDep = depTime;

                if (isDepList)
                    depList.add(new String[]{depTime,duration,arrivalTime,availableSeats,price});
                else
                    returnList.add(new String[]{depTime,duration,arrivalTime,availableSeats,price});
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    private static String toAvailableSeat(Object soldSeatInDouble) {
        int x = (37 - (int)(double)(Double)soldSeatInDouble);
        return String.valueOf(x>0?x:0);
    }
}