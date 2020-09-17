package OnlineTicket;

import javafx.geometry.Insets;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class PersonalInfoPane extends StackPane implements MyViewInterface {

    private TextField tc,name,surname;


    PersonalInfoPane() {
        GridPane gp = new GridPane();
        gp.setMaxSize(300,250);
        gp.add(myLabel("TC       "),0,0);
        gp.add(myLabel("Name     "),0,1);
        gp.add(myLabel("Surname"),0,2);

        //gp.setGridLinesVisible(true);

        gp.add(tc=myTextField("Enter Your TC"),1,0);
        gp.add(name=myTextField("Enter Your Name"),1,1);
        gp.add(surname=myTextField("Enter Your Surname"),1,2);

        gp.setVgap(25);
        gp.setHgap(25);

        StackPane.setMargin(gp,new Insets(40,0,0,0));

        getChildren().add(gp);
    }

    private TextField myTextField(String text) {
        TextField tf = new TextField();
        tf.setPromptText(text);
        tf.setFont(Font.font("Times New Roman", FontWeight.NORMAL, 23));
        return tf;
    }

    private Text myLabel(String label) {
        Text text = new Text(label);
        text.setFont(Font.font("Times New Roman", FontWeight.NORMAL, 23));
        text.setFill(Color.GRAY);
        //GridPane.setMargin(text, new Insets(20, 10, 0, 0));
        return text;
    }

    @Override
    public boolean isReady() {
        if(tc.getText().trim().length()!= 11)
        {
            Main.showErrorMessage("TC should be 11 Digits.");
            return false;
        }

        try{Long.parseLong(tc.getText());}catch (Exception e){
            Main.showErrorMessage("TC should be numbers only!");
            return false;
        }

        if(name.getText().trim().isEmpty()){
            Main.showErrorMessage("Enter Your Name!");
            return false;
        }

        if(surname.getText().trim().isEmpty()){
            Main.showErrorMessage("Enter Your Surname!");
            return false;
        }

        saveIfNew();

        return true;
    }

    @Override
    public int nextView() {
        return MyView.PAYMENT;
    }

    @Override
    public boolean isAboutBeingNew() {
        return false;
    }

    @Override
    public boolean saveIfNew() {
        MyView.currentReservation.getPerson().setName(name.getText()).setTc(tc.getText()).setSurename(surname.getText());
        return false;
    }
}
