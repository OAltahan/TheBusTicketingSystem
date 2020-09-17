package OnlineTicket;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

import java.awt.*;
import java.util.HashMap;
import java.util.stream.Collectors;


public class MyComboBox extends ComboBox<String> implements EventHandler<KeyEvent> {

    private ObservableList<String> data;
    private ObservableList<String> subData;
    private TextField editor;
    private boolean hasSelection;
    private int keyTyped;

    public MyComboBox(ObservableList<String> items) {
        super(items);

        data = getItems();
        editor = getEditor();
        editor.addEventFilter(KeyEvent.KEY_TYPED, this);
        setEditable(true);
        focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) show();
            else hide();
        });
    }

    public void isSwitched() {
        keyTyped = editor.getText().length();
    }


    private String getFromList(String text) {
        subData = FXCollections.observableArrayList();

        subData.addAll(data.stream().filter(i -> toEnglish(i).startsWith(text)).collect(Collectors.toList()));

        if (subData.size() == 0)
            return null;
        setItems(subData);
        getSelectionModel().select(subData.get(0));
        return subData.get(0);
    }

    private void selectRange(int len) {
        hasSelection = keyTyped < len;
        editor.selectRange(keyTyped, len);
    }

    private final HashMap<Character, Character> map = new HashMap<>();
    {
        map.put('İ', 'I');
        map.put('Ö', 'O');
        map.put('Ü', 'U');
        map.put('Ğ', 'G');
        map.put('Ş', 'S');
        map.put('Ç', 'C');
    }

    // "{"boarding":"ISTANBUL AND","landing":"ANKARA","boardingDate":"08.12.2017","returnDate":"09.12.2017","isBothWay":true}"

    private String toEnglish(String turkish){
        String result = "";
        for (char i : turkish.toCharArray()) {
            result+=map.containsKey(i)?map.get(i):i;
        }
        return result;
    }

    @Override
    public void handle(KeyEvent event) {
        show();

        event.consume();

        String key = event.getCharacter();
        String text = editor.getText();
        int length = text.length();


        if (key.equals("\t") || key.equals("\r")) {
            keyTyped = text.length();
            editor.positionCaret(keyTyped);
            hide();
            return;
        }


        if (key.equals("\b") || key.charAt(0) == '\u007F') {
            if (!hasSelection && length > 0) {
                length++;
            }
            switch (length) {
                case 1:
                case 0:
                    setItems(data);
                    editor.setText("");
                    keyTyped = 0;
                    return;
                default:
                    String fromList = getFromList(text.substring(0, keyTyped = length - 1));
                    editor.positionCaret(keyTyped);
                    selectRange(fromList.length());
                    break;
            }
            return;
        }

        if (map.containsKey(key=key.toUpperCase())) {
            key = String.valueOf(map.get(key.charAt(0)));
        }

        text = toEnglish(text);

        String fromList = getFromList(text.substring(0, keyTyped).concat(key));

        if (fromList != null) {
            keyTyped++;
            selectRange(fromList.length());
        } else {
            Main.beep();
        }
    }
}