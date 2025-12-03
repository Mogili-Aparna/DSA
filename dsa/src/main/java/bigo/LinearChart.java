package bigo;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import java.io.File;

public class LinearChart {
    public static void main(String[] args) throws Exception {
        XYSeries series = new XYSeries("O(n)");
        for (int n = 0; n <= 100; n++) {
            series.add(n, n); // linear growth
        }

        XYSeriesCollection dataset = new XYSeriesCollection(series);
        JFreeChart chart = ChartFactory.createXYLineChart(
                "Linear Time Complexity (O(n))",
                "Input Size (n)",
                "Steps",
                dataset
        );
        // ✅ Ensure images folder exists
        File folder = new File("dsa/src/main/java/bigo/images");
        if (!folder.exists()) {
            boolean created = folder.mkdirs();
            if (created) {
                System.out.println("Created folder: " + folder.getAbsolutePath());
            }
        }

        // ✅ Save inside the proper folder
        File imageFile = new File(folder,"linear.png");
        ChartUtils.saveChartAsPNG(imageFile, chart, 900, 600);
        System.out.println("✅ Linear graph saved at: " + imageFile.getAbsolutePath());
    }
}
