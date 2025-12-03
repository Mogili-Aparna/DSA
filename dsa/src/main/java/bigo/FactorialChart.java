package bigo;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import java.io.File;

public class FactorialChart {

    // Utility factorial method (using double for large numbers)
    private static double factorial(int n) {
        double result = 1;
        for (int i = 2; i <= n; i++) result *= i;
        return result;
    }

    public static void main(String[] args) throws Exception {
        // ✅ Series creation
        XYSeries series = new XYSeries("O(n!)");

        // Plot for n = 1 to 12 (beyond that grows too huge)
        for (int n = 1; n <= 12; n++) {
            series.add(n, factorial(n));
        }

        // ✅ Dataset and chart creation
        XYSeriesCollection dataset = new XYSeriesCollection(series);
        JFreeChart chart = ChartFactory.createXYLineChart(
                "Factorial Time Complexity (O(n!))",
                "Input Size (n)",
                "Steps",
                dataset
        );

        // ✅ Styling: darker color, clean background
        var renderer = chart.getXYPlot().getRenderer();
        renderer.setSeriesPaint(0, new java.awt.Color(128, 0, 0)); // Dark maroon

        chart.setBackgroundPaint(java.awt.Color.WHITE);
        chart.getXYPlot().setBackgroundPaint(new java.awt.Color(245, 245, 245));
        chart.getXYPlot().setDomainGridlinePaint(java.awt.Color.LIGHT_GRAY);
        chart.getXYPlot().setRangeGridlinePaint(java.awt.Color.LIGHT_GRAY);

        chart.getTitle().setFont(new java.awt.Font("SansSerif", java.awt.Font.BOLD, 18));
        chart.getXYPlot().getDomainAxis().setLabelFont(new java.awt.Font("SansSerif", java.awt.Font.BOLD, 14));
        chart.getXYPlot().getRangeAxis().setLabelFont(new java.awt.Font("SansSerif", java.awt.Font.BOLD, 14));

        // ✅ Axis range: keep Y visible in readable scale
        chart.getXYPlot().getDomainAxis().setRange(1, 12);
        chart.getXYPlot().getRangeAxis().setRange(1, factorial(12) * 1.5);

        // ✅ Ensure image folder exists
        File folder = new File("dsa/src/main/java/bigo/images");
        if (!folder.exists()) {
            boolean created = folder.mkdirs();
            if (created) System.out.println("📁 Created folder: " + folder.getAbsolutePath());
        }

        // ✅ Save chart
        File imageFile = new File(folder, "factorial.png");
        ChartUtils.saveChartAsPNG(imageFile, chart, 900, 600);
        System.out.println("🟤 Factorial graph saved at: " + imageFile.getAbsolutePath());
    }
}
