package xPlotter;

import javafx.application.Platform;
import javafx.embed.swing.SwingNode;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
//import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;
import org.knowm.xchart.QuickChart;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.XYChart;

import javax.swing.*;
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
    NumberAxis xAxis;

    @FXML
    NumberAxis yAxis;

    @FXML
    SwingNode node;

    ComProtocol comProtocol;

    double phase = 0;
    double[][] initdata = getSineData(phase);




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

        double[] xData = new double[] { 0.0, 1.0, 2.0 };
        double[] yData = new double[] { 2.0, 1.0, 0.0 };



        final XYChart chart = QuickChart.getChart("Simple XChart Real-time Demo", "Radians", "Sine", "sine", initdata[0], initdata[1]);


        // Show it
        final SwingWrapper<XYChart> sw = new SwingWrapper<XYChart>(chart);
        sw.displayChart();

        JPanel pnlChart = new XChartPanel(chart);
        // panel.setLayout(null);
        //panel.add(new SwingWrapper(chart));

// Show it


        node.setContent(pnlChart);

        xAxis.setAnimated(false);
        xAxis.setLabel("Tick");

        yAxis.setAnimated(false);
        yAxis.setLabel("Value");

       // XYChart.Series<Number, Number> series = new XYChart.Series<>();
       // series.setName("Values");

        //plot = new LineChart<>(xAxis, yAxis);
        //plot = new LineChart<>(xAxis, yAxis);
        plot.setAnimated(false);
      //  plot.getData().add(series);

        Thread updateThread = new Thread(() -> {
            while (true) {
                try {

                    phase += 2 * Math.PI * 2 / 20.0;

                    Thread.sleep(100);

                    final double[][] data = getSineData(phase);

                    //series.getData().add(new XYChart.Data<>(tick.incrementAndGet(), (int) (Math.random() * 100)));
                    //Platform.runLater(() -> series.getData().add(new XYChart.Data<>(tick.incrementAndGet(), (int) (Math.random() * 100))));
                    Platform.runLater(() -> chart.updateXYSeries("sine", data[0], data[1], null));
                    sw.repaintChart();
                    System.out.println(tick.incrementAndGet() + " " + data[0]);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        updateThread.setDaemon(true);

        System.out.println("hola");
        updateThread.start();





    }

    private static double[][] getSineData(double phase) {

        double[] xData = new double[1000];
        double[] yData = new double[1000];
        for (int i = 0; i < xData.length; i++) {
            double radians = phase + (2 * Math.PI / xData.length * i);
            xData[i] = radians;
            yData[i] = Math.sin(radians);
        }
        return new double[][] { xData, yData };
    }
}
