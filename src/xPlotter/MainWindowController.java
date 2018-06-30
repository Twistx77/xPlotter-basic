package xPlotter;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;

public class MainWindowController {


    @FXML
    ComboBox cbPorts,cbBaudrates;




    public void initialize(ComProtocol comProtocol) {

        cbPorts.getItems().addAll(comProtocol.getAvailableSerialPorts());
        cbPorts.getSelectionModel().select(0);


    }

}
