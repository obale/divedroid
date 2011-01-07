/**
 * DiveDroid
 *
 * Copyright (C) 2010-2011 by Networld Project
 * Written by Alex Oberhauser <oberhauseralex@networld.to>
 * All Rights Reserved
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by 
 * the Free Software Foundation, version 3 of the License.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this software.  If not, see <http://www.gnu.org/licenses/>
 */


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

public class DiveProfileGraph extends AbstractChart {
  
  /**
   * Executes the chart demo.
   * @param context the context
   * @return the built intent
   */
  public Intent execute(Context _context) {
    String[] titles = new String[] { "Depth (Meter)" };
    List<double[]> x = new ArrayList<double[]>();
    x.add(new double[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16,
    		17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30,
    		31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50,
    		51, 52, 53, 54, 55, 56, 57, 58, 59, 60 });
    
    List<double[]> values = new ArrayList<double[]>();
    values.add(new double[] { 0, 3, 5, 10, 15, 20, 25, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 
    		30, 30, 30, 30, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25,
    		25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25,
			25, 25, 20, 15, 10, 5, 5, 5, 5, 0});
    
    int[] colors = new int[] { Color.CYAN };
    PointStyle[] styles = new PointStyle[] { PointStyle.POINT };
    XYMultipleSeriesRenderer renderer = buildRenderer(colors, styles);
    int length = renderer.getSeriesRendererCount();
    
    for (int i = 0; i < length; i++) {
      ((XYSeriesRenderer) renderer.getSeriesRendererAt(i)).setFillPoints(true);
    }
    setChartSettings(renderer, "", "", 0, 60 + 5, 35, 0,
        Color.LTGRAY, Color.GRAY);
    renderer.setXLabels(12);
    renderer.setYLabels(10);
    renderer.setShowGrid(true);
    
    Intent intent = ChartFactory.getLineChartIntent(_context, buildDataset(titles, x, values),
        renderer, "Dive Profile Graph");
    return intent;
  }

}
