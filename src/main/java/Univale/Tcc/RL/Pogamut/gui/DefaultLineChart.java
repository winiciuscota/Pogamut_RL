package Univale.Tcc.RL.Pogamut.Gui;

import Univale.Tcc.RL.Pogamut.Services.Statistics.Statistics;


import java.awt.Color;
import java.util.Map;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;


/**
 * A simple demonstration application showing how to create a line chart using data from an
 * {@link XYDataset}.
 *
 */
public class DefaultLineChart extends JFrame {

    /**
     * Creates a new demo.
     *
     * @param title the frame title.
     */

    private Statistics statistics;
    public DefaultLineChart() {

        super("Recompenças x Tempo(Em Minutos)");
        // This will create the dataset
        final XYDataset dataset = createDataset();
        // based on the dataset we create the chart
        final JFreeChart chart = createChart(dataset);
        // we put the chart into a panel
        ChartPanel chartPanel = new ChartPanel(chart);
        // default size
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
        // add it to our application
        setContentPane(chartPanel);
        //final ChartPanel chartPanel = new ChartPanel(chart);
        //chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
        //setContentPane(chartPanel);
        pack();
        setVisible(true);
    }

//    public JFreeChart GetChart()
//    {
//        return getChart();
//    }



    /**
     * Creates a sample dataset.
     *
     * @return a sample dataset.
     */
    private XYDataset createDataset() {



        final XYSeries series2 = new XYSeries("Recopenças");

        statistics = new Statistics();
        Map<Long, Double> result = statistics.getStatistics(1);
        for (Map.Entry<Long, Double> entry : result.entrySet())
        {

            series2.add(entry.getKey(), entry.getValue());
        }

        final XYSeriesCollection dataset = new XYSeriesCollection();

        dataset.addSeries(series2);


        return dataset;

    }

    /**
     * Creates a chart.
     *
     * @param dataset the data for the chart.
     * @return a chart.
     */
    private JFreeChart createChart(final XYDataset dataset) {

        // create the chart...
        final JFreeChart chart = ChartFactory.createXYLineChart(
                "Recompenças x Tempo(Em minutos)",      // chart title
                "Tempo(Em Minutos)",                      // x axis label
                "Recompenças",                      // y axis label
                dataset,                  // data
                PlotOrientation.VERTICAL,
                true,                     // include legend
                true,                     // tooltips
                false                     // urls
        );


        chart.setBackgroundPaint(Color.white);


        final XYPlot plot = chart.getXYPlot();

        final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

        return chart;

    }
}