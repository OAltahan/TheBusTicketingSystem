package OnlineTicket;

import javafx.geometry.Insets;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class PaymentPane extends StackPane implements MyViewInterface {

    TextField name, number, month, year, cvc;

    PaymentPane() {
        GridPane gp = new GridPane();
        gp.setMaxSize(350,330);
        gp.add(myLabel("Name  "),0,0);
        gp.add(myLabel("Number"),0,1);
        gp.add(myLabel("Month "),0,2);
        gp.add(myLabel("Year  "),0,3);
        gp.add(myLabel("CVC   "),0,4);

        //gp.setGridLinesVisible(true);

        gp.add(name=myTextField("Card Owner Full Name"),1,0);
        gp.add(number=myTextField("Card Number"),1,1);
        gp.add(month=myTextField("Card Expiry Month"),1,2);
        gp.add(year=myTextField("Card Expiry Year"),1,3);
        gp.add(cvc=myTextField("Card CVC Code"),1,4);

        gp.setVgap(20);
        gp.setHgap(20);

        StackPane.setMargin(gp,new Insets(40,0,0,0));

        getChildren().add(gp);
    }

    private TextField myTextField(String text) {
        TextField tf = new TextField();
        tf.setPromptText(text);
        tf.setFont(Font.font("Times New Roman", FontWeight.NORMAL, 20));
        return tf;
    }

    private Text myLabel(String label) {
        Text text = new Text(label);
        text.setFont(Font.font("Times New Roman", FontWeight.NORMAL, 20));
        text.setFill(Color.GRAY);
        //GridPane.setMargin(text, new Insets(20, 10, 0, 0));
        return text;
    }


    @Override
    public boolean isReady()
    {
        if(name.getText().trim().isEmpty()){
            Main.showErrorMessage("Enter Your Name!");
            return false;
        }
        if(number.getText().trim().length() != 16){
            Main.showErrorMessage("Card Number should be 16 Digits!");
            return false;
        }

        try{Long.parseLong(number.getText());}catch (Exception e){
            Main.showErrorMessage("Card Number should be numbers only!");
            return false;
        }

        if(month.getText().trim().length() > 2 || month.getText().trim().length() < 1){
            Main.showErrorMessage("Check month field!");
            return false;
        }
        if(year.getText().trim().length() != 4){
            Main.showErrorMessage("Year Should be 4 digits!");
            return false;
        }

        if(cvc.getText().trim().length() != 3){
            Main.showErrorMessage("CVC Should be 3 digits!");
            return false;
        }

        int m,y;

        try{m =Integer.parseInt(month.getText().trim());
            y = Integer.parseInt(year.getText().trim());
            Integer.parseInt(cvc.getText().trim());
        }catch (Exception e){
            Main.showErrorMessage("Month, Year, and CVC should contain only digits!");
            return false;
        }

        if(m < 1|| m>12)
        {
            Main.showErrorMessage("Month should be between 1 and 12");
            return false;
        }

        if (y < 2017 || y > 2030)
        {
            Main.showErrorMessage("Year of card seems to be invalid!");
            return false;
        }

        saveIfNew();

        return true;
    }

    @Override
    public int nextView() {
        return MyView.CONFIRMATION;
    }

    @Override
    public boolean isAboutBeingNew() {
        return false;
    }

    @Override
    public boolean saveIfNew() {
        return false;
    }
}
