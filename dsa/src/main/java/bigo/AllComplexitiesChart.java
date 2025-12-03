package bigo;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.annotations.XYTextAnnotation;
import org.jfree.chart.axis.LogarithmicAxis;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import java.io.File;
import java.io.IOException;

public class AllComplexitiesChart {
    public static void main(String[] args) throws IOException {
        XYSeriesCollection dataset = new XYSeriesCollection();

        XYSeries o1 = new XYSeries("O(1)");
        XYSeries oLog = new XYSeries("O(log n)");
        XYSeries oN = new XYSeries("O(n)");
        XYSeries oN2 = new XYSeries("O(n^2)");
        XYSeries oN3 = new XYSeries("O(n^3)");
        XYSeries o2N = new XYSeries("O(2^n)");
        XYSeries oFact = new XYSeries("O(n!)");

        for (int n = 0; n <= 12; n++) {
            o1.add(n, 1);
            oLog.add(n, Math.log(n+1) / Math.log(2));
            oN.add(n, n);
            oN2.add(n, n * n);
            oN3.add(n, n * n * n);
            o2N.add(n, Math.pow(2, n));
            oFact.add(n, factorial(n));
        }

        dataset.addSeries(o1);
        dataset.addSeries(oLog);
        dataset.addSeries(oN);
        dataset.addSeries(oN2);
        dataset.addSeries(oN3);
        dataset.addSeries(o2N);
        dataset.addSeries(oFact);

        JFreeChart chart = ChartFactory.createXYLineChart(
                "Growth of Common Time Complexities (Annotated)",
                "Input Size (n)",
                "Time (log scale)",
                dataset
        );
        // ✅ Use safe logarithmic axis
        LogarithmicAxis logAxis = new LogarithmicAxis("Steps (log scale)");
        logAxis.setAllowNegativesFlag(false);
        logAxis.setStrictValuesFlag(false);
        chart.getXYPlot().setRangeAxis(logAxis);
        chart.getXYPlot().getDomainAxis().setRange(0, 13);
        chart.getXYPlot().getRangeAxis().setRange(0, factorial(12)*2);

        // ✅ Apply darker, high-contrast colors manually
        var renderer = chart.getXYPlot().getRenderer();
        renderer.setSeriesPaint(0, new java.awt.Color(30, 30, 30));     // O(1) - blackish gray
        renderer.setSeriesPaint(1, new java.awt.Color(50, 90, 255));    // O(log n) - bright blue
        renderer.setSeriesPaint(2, new java.awt.Color(0, 180, 0));      // O(n) - deep green
        renderer.setSeriesPaint(3, new java.awt.Color(255, 128, 0));    // O(n²) - orange
        renderer.setSeriesPaint(4, new java.awt.Color(200, 50, 255));   // O(n³) - purple
        renderer.setSeriesPaint(5, new java.awt.Color(255, 0, 0));      // O(2ⁿ) - red
        renderer.setSeriesPaint(6, new java.awt.Color(128, 0, 0));      // O(n!) - dark maroon

        // ✅ Bold fonts for chart title and annotations
        chart.getTitle().setFont(new java.awt.Font("SansSerif", java.awt.Font.BOLD, 18));
        chart.getXYPlot().getDomainAxis().setLabelFont(new java.awt.Font("SansSerif", java.awt.Font.BOLD, 14));
        chart.getXYPlot().getRangeAxis().setLabelFont(new java.awt.Font("SansSerif", java.awt.Font.BOLD, 14));

        // Annotations beside last points
        chart.getXYPlot().addAnnotation(new XYTextAnnotation("O(1)", 12.3, 1));
        chart.getXYPlot().addAnnotation(new XYTextAnnotation("O(log n)", 12.3, Math.log(12) / Math.log(2)));
        chart.getXYPlot().addAnnotation(new XYTextAnnotation("O(n)", 12.3, 12));
        chart.getXYPlot().addAnnotation(new XYTextAnnotation("O(n²)", 12.3, 12 * 12));
        chart.getXYPlot().addAnnotation(new XYTextAnnotation("O(n³)", 12.3, 12 * 12 * 12));
        chart.getXYPlot().addAnnotation(new XYTextAnnotation("O(2ⁿ)", 12.3, Math.pow(2, 12)));
        chart.getXYPlot().addAnnotation(new XYTextAnnotation("O(n!)", 12.3, factorial(12)));

        // ✅ Ensure images folder exists
        File folder = new File("dsa/src/main/java/bigo/images");
        if (!folder.exists()) {
            boolean created = folder.mkdirs();
            if (created) {
                System.out.println("Created folder: " + folder.getAbsolutePath());
            }
        }

        // ✅ Save inside the proper folder
        File imageFile = new File(folder, "all_complexities_annotated.png");
        ChartUtils.saveChartAsPNG(imageFile, chart, 900, 600);
        System.out.println("✅ Annotated graph saved at: " + imageFile.getAbsolutePath());
    }

    private static double factorial(int n) {
        double result = 1;
        for (int i = 2; i <= n; i++) result *= i;
        return result;
    }
}
