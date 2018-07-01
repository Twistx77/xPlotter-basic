package xPlotter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;


public class ComProtocol {


    private SerialHandler serialHandler;
    private MainWindowController mainWindowController;

    public ComProtocol(MainWindowController mainWindowController ) {

        serialHandler = new SerialHandler(this);
        this.mainWindowController = mainWindowController;

    }



    /**
     * Gets the names of the available serial ports to connect.
     *
     * @return ArrayList with the names of the serial ports found.
     */
    public String[] getAvailableSerialPorts() {
        //return new ArrayList(Arrays.asList(serialHandler.getAvailableSerialPorts()));
        return serialHandler.getAvailableSerialPorts();
    }

    /**
     * Connects to a determined serial port.
     *
     * @param portName Name of the port to be connected.
     */
    public void connect(String portName, int baudrate) {
        try {
            serialHandler.connect(portName, baudrate);
        } catch (Exception ex) {
            Logger.getLogger(ComProtocol.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Disconnects serial port.
     */
    public void disconnect() {
        try {
            serialHandler.disconnect();
        } catch (Exception ex) {
            Logger.getLogger(ComProtocol.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
