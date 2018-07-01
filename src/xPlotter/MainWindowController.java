package xPlotter;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicInteger;

public class MainWindowController implements Initializable  {


    private AtomicInteger tick = new AtomicInteger(0);


    @FXML
    private ComboBox<String> cbPorts, cbBaudrates;

    @FXML
    private LineChart<Number, Number> plot;

    @FXML
    NumberAxis xAxis = new NumberAxis();

    @FXML
    NumberAxis yAxis = new NumberAxis();


    ComProtocol comProtocol;



    @Override
    public void initialize(URL location, ResourceBundle resources) {




    }

    public void setCommProtocol (ComProtocol comProtocol)
    {

        this.comProtocol = comProtocol;

        cbPorts.getItems().setAll(comProtocol.getAvailableSerialPorts());
        cbPorts.getSelectionModel().select(0);
        startChart();
    }

    public void startChart(){



        xAxis.setAnimated(false);
        xAxis.setLabel("Tick");

        yAxis.setAnimated(false);
        yAxis.setLabel("Value");

        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.setName("Values");

        //plot = new LineChart<>(xAxis, yAxis);
        plot = new LineChart<>(xAxis, yAxis);
        plot.setAnimated(false);
        plot.getData().add(series);

        Thread updateThread = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(1000);
                    //series.getData().add(new XYChart.Data<>(tick.incrementAndGet(), (int) (Math.random() * 100)));
                    Platform.runLater(() -> series.getData().add(new XYChart.Data<>(tick.incrementAndGet(), (int) (Math.random() * 100))));


                    System.out.println(tick.incrementAndGet() + " " + (int) (Math.random() * 100));
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        updateThread.setDaemon(true);

        System.out.println("hola");
        updateThread.start();





    }
}
