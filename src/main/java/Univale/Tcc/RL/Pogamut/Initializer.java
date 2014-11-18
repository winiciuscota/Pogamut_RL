package Univale.Tcc.RL.Pogamut;

import Univale.Tcc.RL.Pogamut.Gui.DefaultLineChart;
import Univale.Tcc.RL.Pogamut.Gui.MainForm;
import Univale.Tcc.RL.Pogamut.Gui.MainForm2;
import org.eclipse.swt.widgets.Display;

/**
 * Created by winicius on 16/11/2014.
 */
public class Initializer {

    public static void main(String[] args)
    {
        Display display = new Display();
        MainForm form = new MainForm(display);
        form.open();
        while (!form.isDisposed()) {
            if (!display.readAndDispatch())
                display.sleep();
        }
        display.dispose();
//        DefaultLineChart chart = new DefaultLineChart();

        //MainForm2 form = new MainForm2();

    }
}
