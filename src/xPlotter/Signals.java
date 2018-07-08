package xPlotter;

import java.util.ArrayList;

public class Signals{

    private static final int MAX_SIGNALS = 10;
    private final MainWindowController mainWindowController;

    private ArrayList<Float> values = new ArrayList<Float>(MAX_SIGNALS);
    private ArrayList<Float> max_values = new ArrayList<Float>(MAX_SIGNALS);
    private ArrayList<Float> min_values = new ArrayList<Float>(MAX_SIGNALS);
    private ArrayList<Float> average_values = new ArrayList<Float>(MAX_SIGNALS);

    private int number_of_samples;

    Signals (MainWindowController mainWindowController)
    {
        this.mainWindowController = mainWindowController;
        number_of_samples = 0;

    }

    public void addNewValues(ArrayList<Float> values)
    {
        number_of_samples++;
       // checkMax(values);
       // checkMin(values);
        //calcAverage(values);

        System.out.println(values.size());
        mainWindowController.addNewValues(values);
    }

    private void calcAverage(ArrayList<Float> values)
    {

       /* for (int i=0; i<values.size(); i++) {
            Float value = values.get(i);
            Float max_value = max_values.get(i);
            if (value > max_value)
                max_values.set(i,value);
        }*/

    }

    private void checkMax(ArrayList<Float> values)
    {

        for (int i=0; i<values.size(); i++) {
            Float value = values.get(i);
            Float max_value = max_values.get(i);
            if (value > max_value)
                max_values.set(i,value);
        }
    }

    private void checkMin(ArrayList<Float> values)
    {
        for (int i=0; i<values.size(); i++) {
            Float value = values.get(i);
            Float max_value = max_values.get(i);
            if (value > max_value)
                max_values.set(i,value);
        }
    }


}
