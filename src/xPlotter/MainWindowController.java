package xPlotter;


import javafx.fxml.FXML;
import javafx.fxml.Initializable;


import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;


import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.VBox;

import javafx.scene.control.CheckBox;



import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicInteger;


public class MainWindowController implements Initializable  {


    private static final int MAX_DATA_SERIES = 6 ;
    private  int maxDataPoints = 1000;



    private AtomicInteger tick = new AtomicInteger(0);


    @FXML
    private ComboBox<String> cbPorts, cbBaudrates;

    @FXML
    private ToggleButton btnConnect;



    @FXML
    private NumberAxis xAxis;

    @FXML
    private NumberAxis yAxis;

    @FXML
    private VBox vBoxYLimits;

    @FXML
    private CheckBox checkAutoRangeY;

    @FXML
    private CheckBox checkVisibleLegend;

    @FXML
    private TextField tfUpperLimitY;
    @FXML
    private TextField tfLowerLimitY;

    @FXML
    private TextField tfUpperLimitX;
    @FXML
    private TextField tfLowerLimitX;

    ComProtocol comProtocol;

    private String[] strokeColors = { "#409CDA","#C6262E", "#68B723", "#A56DE2","#F37329", "#F9C440"};


    @FXML
    private LineChart<Number,Number> lineChart;


    ArrayList<XYChart.Series<Number, Number>> dataSeries  = new ArrayList<XYChart.Series<Number, Number>>();




    @Override
    public void initialize(URL location, ResourceBundle resources) {

        cbBaudrates.getItems().removeAll(cbBaudrates.getItems());
        cbBaudrates.getItems().addAll("1200","2400","4800","9600","19200","38400","57600","115200","250000","256000");
        cbBaudrates.getSelectionModel().select("115200");
        createChart();

        tfUpperLimitX.setText("1000");
        tfLowerLimitX.setText("0");
        tfUpperLimitY.setText("1000");
        tfLowerLimitY.setText("0");


        checkAutoRangeY.setOnAction((event) -> {

            boolean selected = checkAutoRangeY.isSelected();

            // If Auto Range Y is disabled we adjust the range to specified by text fields.

            vBoxYLimits.setDisable(selected);

            yAxis.setAutoRanging(selected);

            if (!selected)
            {
                yAxis.setUpperBound(Integer.parseInt(tfUpperLimitY.getText()));
                yAxis.setLowerBound(Integer.parseInt(tfLowerLimitY.getText()));
            }
        });
    }

    public void setCommProtocol(ComProtocol comProtocol)
    {
        this.comProtocol = comProtocol;

            cbPorts.getItems().setAll(comProtocol.getAvailableSerialPorts());
            cbPorts.getSelectionModel().select(0);

    }


    public void createChart()
    {
        xAxis.setLowerBound(0 );
        xAxis.setUpperBound(maxDataPoints);
        xAxis.setAnimated(false);

        yAxis.setAnimated(false);
        yAxis.setLabel("");

        for (int i = 0; i<MAX_DATA_SERIES; i++)
        {
            XYChart.Series<Number, Number> dataSerie = new XYChart.Series<>();
            dataSerie.setName("Value" + i);

            dataSeries.add(dataSerie);
            lineChart.getData().add(dataSerie);
            dataSeries.get(i).getNode().lookup(".chart-series-line").setStyle("-fx-stroke: " + strokeColors[i]+";"+"-fx-stroke-width: 1px;"); // Sets lines colors

        }

        lineChart.setAnimated(false);

        lineChart.setLegendVisible(false);
    }


    @FXML
    public void btnConnectPressed()
    {
        if (btnConnect.isSelected() == true)
        {
            comProtocol.connect(cbPorts.getValue(), Integer.parseInt(cbBaudrates.getValue()));
            btnConnect.setText("Disconnect");
            btnConnect.setStyle("-fx-base: #CB3036");
        }
        else if (btnConnect.isSelected() == false)
        {
            comProtocol.disconnect();
            btnConnect.setText("Connect");
            btnConnect.setStyle("-fx-base: #409CDA");
        }
    }

    @FXML
    private void checkVisibleLegendChanged()
    {
        lineChart.setLegendVisible(checkVisibleLegend.isSelected());
    }

    @FXML
    private void cbPortsPressed ()
    {
        cbPorts.getItems().removeAll(cbPorts.getItems());
        cbPorts.getItems().setAll(comProtocol.getAvailableSerialPorts());
        cbPorts.getSelectionModel().select(0);

    }

    @FXML
    private void limitsChangedY()
    {
        int upperLimit = Integer.parseInt(tfUpperLimitY.getText());
        int lowerLimit = Integer.parseInt(tfLowerLimitY.getText());

        yAxis.setUpperBound(upperLimit);
        yAxis.setLowerBound(lowerLimit);
        yAxis.setTickUnit(upperLimit/10);
    }

    @FXML
    private void limitsChangedX()
    {
        int upperLimit = Integer.parseInt(tfUpperLimitX.getText());
        int lowerLimit = Integer.parseInt(tfLowerLimitX.getText());

        maxDataPoints = upperLimit;
        xAxis.setUpperBound(upperLimit);
        xAxis.setLowerBound(lowerLimit);
        xAxis.setTickUnit(upperLimit/10);

    }


    public void addNewValues(ArrayList<Float> values)
    {
        long time = tick.incrementAndGet();

        for (int i = 0; i< values.size(); i++) {

            dataSeries.get(i).getData().add(new XYChart.Data<>(time, values.get(i)));

            // remove points to keep us at no more than maxDataPoints
            if (dataSeries.get(i).getData().size() > maxDataPoints)
            {
                dataSeries.get(i).getData().remove(0, dataSeries.get(i).getData().size() - maxDataPoints);
            }

            // update
            xAxis.setLowerBound(time - maxDataPoints);
            xAxis.setUpperBound(time - 1);
        }
    }
}
