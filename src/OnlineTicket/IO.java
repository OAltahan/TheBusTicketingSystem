package OnlineTicket;

import java.io.*;
import java.util.ArrayList;

public class IO {


    public static boolean save() {

        try {
            File biz = new File("database.txt");

            if (!biz.exists()) {

                biz.createNewFile();

            }

            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(biz));
            oos.writeObject(MyView.reservations);
            oos.writeObject(MyView.generatedJourney);
            oos.flush();
            oos.close();

        } catch (IOException e) {
            e.printStackTrace();
            Main.beep();
            return false;
        }

        return true;
    }

    public static boolean load() {
        try {
            File biz = new File("database.txt");

            if (!biz.exists()) {
                MyView.reservations = new ArrayList<>();
                MyView.generatedJourney = new ArrayList<>();


                //Main.showErrorMessage("File does not exist!");
                return false;

            }

            ObjectInputStream oos = new ObjectInputStream(new FileInputStream(biz));
            MyView.reservations = (ArrayList<Reservation> )oos.readObject();
            MyView.generatedJourney = (ArrayList<Journey>) oos.readObject();
            oos.close();

        } catch (Exception e) {
            e.printStackTrace();
            Main.beep();
            return false;
        }

        return true;
    }
}
