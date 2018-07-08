package xPlotter;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Platform;
import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;
import jssc.SerialPortList;

/**
 * Manages all low level serial communication
 *
 * @author Alejandro Méndez A.
 *
 * @version 1.0
 *
 * July 2013
 *
 */
public class SerialHandler {// implements SerialPortEventListener {

    SerialPort serialPort;
    ComProtocol commProtocol;


    public SerialHandler(ComProtocol commProtocol) {
        this.commProtocol = commProtocol;
    }

    /**
     * Gets the names of the available serial ports to connect.
     *
     * @return String array with the names of the serial ports found.
     */
    public String[] getAvailableSerialPorts() {

        return SerialPortList.getPortNames();
    }

    /**
     * Connects to a determined serial port.
     *
     * @param portName Name of the port to be connected.
     * @param baudRate Baudrate of the connection.
     */
    public void connect(String portName, int baudRate) throws Exception {

        serialPort = new SerialPort(portName);
        System.out.println(portName);
        try {
            System.out.println("Port opened: " + serialPort.openPort());
            System.out.println("Params setted: " + serialPort.setParams(baudRate, 8, 1, 0));

            int mask = SerialPort.MASK_RXCHAR;
            serialPort.setEventsMask(mask);//Set mask

            serialPort.addEventListener(new SerialPortReader());//Add SerialPortEventListener

        } catch (SerialPortException ex) {
            System.out.println(ex);
        }
    }

    public void sendByte(byte _byte)  {
        try {
            serialPort.writeByte(_byte);
            //System.out.print(" 0x" + Integer.toHexString(_byte));
        } catch (SerialPortException ex) {
            Logger.getLogger(SerialHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Sends array of bytes.
     *
     * @param serialOutputBuffer Array with the data to be sent.
     */
    public void sendPacket(byte[] serialOutputBuffer)  {

        try {
            //  for (int i = 0; i < serialOutputBuffer.length; i++) {

            //  serialPort.writeBytes(serialOutputBuffer[]);
            serialPort.writeBytes(serialOutputBuffer);

            System.out.print("Output Packet:");
            for (int i = 0; i < serialOutputBuffer.length; i++) {
                System.out.print(" 0x" + Integer.toHexString(serialOutputBuffer[i]));
            }
            System.out.println("");
        } catch (SerialPortException ex) {
            ex.printStackTrace();
            System.exit(-1);
        }

    }

    /**
     * Disconnects serial port.
     */
    public void disconnect() {
        try {
            serialPort.closePort();
        } catch (SerialPortException ex) {
            Logger.getLogger(SerialHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    class SerialPortReader implements SerialPortEventListener {

        byte buffer[] = new byte[2];

        StringBuilder message = new StringBuilder();

        public void serialEvent(SerialPortEvent event) {
            if (event.isRXCHAR()) {//If data is available
                try {
                    /*while (event.getEventValue() > 0) {
                        buffer = serialPort.readBytes(1);
                        serialPort.readString();*/

                        byte buffer[] = serialPort.readBytes();
                        for (byte b: buffer) {
                            if ( ( b == '\n') && message.length() > 0) {
                                String toProcess = message.toString();
                                Platform.runLater(new Runnable() {
                                    @Override public void run() {

                                        String[] strings = toProcess.split(",");

                                        ArrayList<Float> values  = new ArrayList<Float>();

                                        for (int i = 0; i < strings.length; i++ )
                                        {
                                            values.add(Float.parseFloat(strings[0]));
                                        }
                                        commProtocol.addNewValues(values);
                                    }
                                });
                                message.setLength(0);
                            }
                            else {
                                message.append((char)b);
                            }
                        }
                        //System.out.print(Integer.toHexString(buffer[0]&0xFF)+" ");
                       // comProtocol.receivedByte(buffer[0]);

                } catch (SerialPortException ex) {
                    System.err.println(ex);
                }

            }
        }

    }
}

