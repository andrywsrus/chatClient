package chatClient.controllers;

import chatClient.models.Network;
import chatClient.StartClient;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import java.text.DateFormat;
import java.util.Date;

public class ChatController {
    private String dateFormatString;
    private String message;
    @FXML
    private TextField inputField;
    @FXML
    private Label usernameTitle;
    @FXML
    private TextArea chatHistory;
    @FXML
    private ListView <String> usersList;
    @FXML
    private Button sendButton;
    private Network network;
    private String selectedRecipient;
    private StartClient startClient;

    public ChatController() {
    }

    @FXML
    void initialize() {
        sendButton.setOnAction(event -> sendMessage());
        inputField.setOnAction(event -> sendMessage());

        usersList.setCellFactory(lv -> {
            MultipleSelectionModel<String> selectionModel = usersList.getSelectionModel();
            ListCell<String> cell = new ListCell<>();
            cell.textProperty().bind(cell.itemProperty());
            cell.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
                usersList.requestFocus();
                if (!cell.isEmpty()) {
                    int index = cell.getIndex();
                    if (selectionModel.getSelectedIndices().contains(index)) {
                        selectionModel.clearSelection(index);
                        selectedRecipient = null;
                    } else {
                        selectionModel.select(index);
                        selectedRecipient = cell.getItem();
                    }
                    event.consume();
                }
            });
            return cell;
        });
    }

    private void sendMessage(){
        message = inputField.getText().trim();
        inputField.clear();
        if(message.isEmpty()){
            return;
        }
        if (selectedRecipient != null) {
            network.sendPrivateMessage(selectedRecipient, message);
        } else {
            network.sendMessage(message);
        }

    }
    public void appendMessage(String sender, String message) {
        String timeStamp = DateFormat.getInstance().format(new Date());
        chatHistory.appendText(timeStamp);
        chatHistory.appendText(System.lineSeparator());
        chatHistory.appendText(String.format("%s: %s", sender, message));
        chatHistory.appendText(System.lineSeparator());
        chatHistory.appendText(System.lineSeparator());
    }

    public void appendServerMessage(String message) {
        chatHistory.appendText(String.format("Внимание! %s", message));
        chatHistory.appendText(System.lineSeparator());
        chatHistory.appendText(System.lineSeparator());

    }

    public void userOnline() {
        usersList.getItems().clear();
        usersList.setItems(FXCollections.observableArrayList(network.getOnlineClient()));
    }

    public void setNetwork(Network network) {
        this.network = network;
    }

    public void setStartClient(StartClient startClient) {
        this.startClient = startClient;
    }
    public StartClient getStartClient() {
        return startClient;
    }

    public void setUsernameTitle(String usernameTitleStr) {
        this.usernameTitle.setText(usernameTitleStr);
    }
}