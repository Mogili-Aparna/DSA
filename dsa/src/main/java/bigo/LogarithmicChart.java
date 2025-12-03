package bigo;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import java.io.File;

public class LogarithmicChart {
    public static void main(String[] args) throws Exception {
        XYSeries series = new XYSeries("O(log n)");
        for (int n = 1; n <= 1000; n++) {
            series.add(n, Math.log(n) / Math.log(2)); // log base 2
        }

        XYSeriesCollection dataset = new XYSeriesCollection(series);
        JFreeChart chart = ChartFactory.createXYLineChart(
                "Logarithmic Time Complexity (O(log n))",
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
        File imageFile = new File(folder, "logarithmic.png");
        ChartUtils.saveChartAsPNG(imageFile, chart, 900, 600);
        System.out.println("✅ Logarithmic graph saved at: " + imageFile.getAbsolutePath());
    }
}
