package utils;

import org.jfree.chart.*;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.chart.renderer.category.StackedBarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.DefaultIntervalXYDataset;

import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * @author manish.m
 */
public class GraphUI {

    static DefaultCategoryDataset dataset;
    static DefaultIntervalXYDataset dataset1;
    static JFreeChart chart;
    static ChartFrame frame;
    public static void onStartTest() {
        dataset = new DefaultCategoryDataset();
    }
    public static void onSuccessTest(String Description ) {
        String[] parts = Description.split("@");
        dataset.addValue(Double.parseDouble(parts[2]),"Passed",parts[0]);
    }
    public static void onFailureTest(String Description ) {
        String[] parts = Description.split("@");
        dataset.addValue(Double.parseDouble(parts[2]),"Failed",parts[0]);
    }

    public static void onFinishTest() throws IOException {
//        chart= ChartFactory.createBarChart("Unite Chart","Scenario","Time Taken:",dataset, PlotOrientation.VERTICAL,true,true,false);
        chart= ChartFactory.createLineChart("Unite Chart","Scenario","Time Taken:",dataset, PlotOrientation.VERTICAL,true,true,false);
        BarRenderer renderer = new BarRenderer();
//        LineAndShapeRenderer renderer = new LineAndShapeRenderer();
        renderer.setDrawBarOutline(true);
        renderer.setMaximumBarWidth(0.1);
        renderer.setMaximumBarWidth(0.5);
//        renderer.setDrawOutlines(true);
        renderer.setSeriesPaint(0, Color.GREEN);
        renderer.setSeriesPaint(1, Color.RED);

        chart.getCategoryPlot().setRenderer(renderer);
        frame = new ChartFrame("Unite",chart);
        frame.pack();
        frame.setVisible(true);
        ChartUtils.saveChartAsPNG(new File(".//Graph.png"),chart, frame.getWidth(), frame.getHeight());

    }

}
