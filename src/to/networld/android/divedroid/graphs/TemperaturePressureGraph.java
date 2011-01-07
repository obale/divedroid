package to.networld.android.divedroid.graphs;

import java.util.ArrayList;
import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.chart.PointStyle;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;

public class TemperaturePressureGraph extends AbstractChart {
  
  /**
   * Executes the chart demo.
   * @param context the context
   * @return the built intent
   */
  public Intent execute(Context _context) {
    String[] titles = new String[] { "Temperatue (°C)", "Pressure (Bar)" };
    List<double[]> x = new ArrayList<double[]>();
    for (int count=0; count < titles.length; count++ )
    	x.add(new double[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16,
    			17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30,
    			31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50,
    			51, 52, 53, 54, 55, 56, 57, 58, 59, 60 });
    
    List<double[]> values = new ArrayList<double[]>();
    values.add(new double[] { 25, 26, 26, 26, 26, 26, 26, 26, 26, 27, 27, 27, 27, 27, 28, 28, 28, 
    		28, 28, 28, 28, 29, 29, 29, 29, 30, 30, 30, 30, 30, 30,
    		31, 31, 31, 31, 31, 29, 29, 29, 29, 28, 28, 28, 27, 27, 26, 25, 24, 24, 24, 24,
			25, 25, 26, 26, 27, 28, 28, 29, 29, 29});
    
    values.add(new double[] { 217, 215, 212, 208, 204, 195, 190, 189, 182, 175, 169, 164, 160, 155, 149, 141, 135, 
    		130, 125, 121, 114, 110, 105, 100, 95, 89, 82, 77, 72, 65, 60,
    		57, 53, 50, 47, 45, 40, 37, 34, 32, 30, 28, 27, 27, 25, 25, 20, 15, 15, 15, 15,
    		15, 15, 15, 15, 15, 15, 15, 15, 10, 5 });
    
    int[] colors = new int[] { Color.CYAN, Color.GREEN };
    PointStyle[] styles = new PointStyle[] { PointStyle.POINT, PointStyle.POINT };
    XYMultipleSeriesRenderer renderer = buildRenderer(colors, styles);
    int length = renderer.getSeriesRendererCount();
    
    for (int i = 0; i < length; i++) {
      ((XYSeriesRenderer) renderer.getSeriesRendererAt(i)).setFillPoints(true);
    }
    setChartSettings(renderer, "", "", 0, 60 + 5, 0, 220,
        Color.LTGRAY, Color.GRAY);
    renderer.setXLabels(12);
    renderer.setYLabels(10);
    renderer.setShowGrid(true);
    
    Intent intent = ChartFactory.getLineChartIntent(_context, buildDataset(titles, x, values),
        renderer, "Temperature / Pressure Graph");
    return intent;
  }

}
