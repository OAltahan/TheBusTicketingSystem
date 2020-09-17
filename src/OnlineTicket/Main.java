package OnlineTicket;

import Project.FirstSceneStackRouteTime;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import javax.xml.soap.Text;
import java.awt.*;
import java.util.ArrayList;

public class Main extends Application{

    private static final Toolkit toolit = Toolkit.getDefaultToolkit();

    public static void beep() {
        toolit.beep();
    }

    public static void main(String[] args) {

        IO.load();
        launch(args);

    }

    @Override
    public void start(Stage primaryStage) {
        StackPane sp = new StackPane();


        MyView mp1 = MyView.newView(MyView.NEW_RESERVATION);

        sp.getChildren().add(mp1.refresh());

        Dimension a = Toolkit.getDefaultToolkit().getScreenSize();
        Scene scene = new Scene(sp,a.getWidth(),a.getHeight());

        scene.getRoot().setId("scene");
        scene.getStylesheets().add(this.getClass().getResource("/OnlineTicket/style.css").toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void showErrorMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message);
        alert.setHeaderText(null);
        alert.initStyle(StageStyle.UNDECORATED);
        alert.show();
        Main.beep();
    }
}
