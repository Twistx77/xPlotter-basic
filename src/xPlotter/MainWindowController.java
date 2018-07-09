package xPlotter;

import javafx.application.Platform;
import javafx.embed.swing.SwingNode;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
//import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;


import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;



import org.knowm.xchart.QuickChart;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.XYChart;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicInteger;



public class MainWindowController implements Initializable  {


    private TimeSeriesCollection timeSeriesCollection; // Collection of time series data
    private XYDataset xyDataset; // dataset that will be used for the chart
    private TimeSeries seriesX; // X series data
    private TimeSeries seriesY; // Y series data
    private TimeSeries seriesZ; // X series data


    private AtomicInteger tick = new AtomicInteger(0);


    @FXML
    private ComboBox<String> cbPorts, cbBaudrates;


    @FXML
    SwingNode node;

    ComProtocol comProtocol;

    double phase = 0;
    double[][] initdata = getSineData(phase);




    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void setCommProtocol(ComProtocol comProtocol)
    {

        this.comProtocol = comProtocol;

        cbPorts.getItems().setAll(comProtocol.getAvailableSerialPorts());
        cbPorts.getSelectionModel().select(0);
        startChart();
    }



    public void startChart(){




        JFreeChart chartJFree = createChart();
        ChartPanel chartPanel = new ChartPanel(chartJFree);
        chartPanel.setFillZoomRectangle(true);
        chartPanel.setMouseWheelEnabled(true);
        chartPanel.setPreferredSize(new java.awt.Dimension(800,100));

        node.setContent(chartPanel);





        /*Thread updateThread = new Thread(() -> {
            while (true) {
                try {

                    phase += 2 * Math.PI * 2 / 20.0;

                    Thread.sleep(10);

                    //final double[][] data = getSineData(phase);

                    //series.getData().add(new XYChart.Data<>(tick.incrementAndGet(), (int) (Math.random() * 100)));
                    //Platform.runLater(() -> series.getData().add(new XYChart.Data<>(tick.incrementAndGet(), (int) (Math.random() * 100))));
                    //Platform.runLater(() -> chart.updateXYSeries("sine", data[0], data[1], null));

                    this.timeSeriesCollection.getSeries(0).add(new Millisecond(),1000);
                    this.timeSeriesCollection.getSeries(1).add(new Millisecond(),200);
                    //this.timeSeriesCollection.getSeries(2).add(new Millisecond(),in_z);

                   // sw.repaintChart();
                    //System.out.println(tick.incrementAndGet() + " " + data[0]);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        updateThread.setDaemon(true);

        System.out.println("hola");
        updateThread.start();
        */





    }


    @FXML
    public void btnConnectPressed(){
        comProtocol.connect(cbPorts.getValue(),115200);
    }


    private JFreeChart createChart() {

        JFreeChart chart = ChartFactory.createTimeSeriesChart(
                "",  // title
                "",             // x-axis label
                "",   // y-axis label
                timeSeriesCollection,            // data
                true,               // create legend?
                true,               // generate tooltips?
                false               // generate URLs?
        );

        chart.setBackgroundPaint(new Color(0xF4, 0xF4, 0xF4));

        XYPlot plot = (XYPlot) chart.getPlot();

        XYLineAndShapeRenderer r1 = new XYLineAndShapeRenderer();
        r1.setSeriesPaint(0, new Color(77, 196, 240));
        r1.setSeriesPaint(1, new Color(0x00, 0xff, 0xff));
        r1.setSeriesShapesVisible(0,  false);
        r1.setSeriesShapesVisible(1,  false);
        plot.getRenderer().setSeriesPaint(0, new Color(166, 226, 46));
        plot.getRenderer().setSeriesPaint(1, new Color(77, 196, 240));

        DateAxis axis = (DateAxis) plot.getDomainAxis();
        axis.setAutoRange(true);
        //axis.setFixedAutoRange(6000.0);

        return chart;
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

    public void addNewValues(ArrayList<Float> values)
    {

        for (int i = 0; i< values.size(); i++) {
            this.timeSeriesCollection.getSeries(i).add(new Millisecond(), values.get(i));
        }
    }
}
