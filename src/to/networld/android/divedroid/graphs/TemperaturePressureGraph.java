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

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.achartengine.ChartFactory;
import org.achartengine.chart.PointStyle;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import to.networld.android.divedroid.model.rdf.DiveProfile;
import to.networld.android.divedroid.model.rdf.Sample;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;

public class TemperaturePressureGraph extends AbstractChart {
	private final File file;
	private final String nodeid;
	
	public TemperaturePressureGraph(File _file, String _nodeid) {
		this.file = _file;
		this.nodeid = _nodeid;
	}
	
	public Intent execute(Context _context) {
		DiveProfile diveProfile;
		try {
			diveProfile = new DiveProfile(this.file, this.nodeid);
			Vector<Sample> samples = diveProfile.getSamples();
		    String[] titles = new String[] { "Temperatue (°C)", "Pressure (Bar)" };
		    
		    double maxTime = 0;
		    double maxYValue = 0;
		    double minYValue = 9999;
		    
		    double[] timeX1 = new double[samples.size()];
		    double[] temperatureY1 = new double[samples.size()];
		    double[] pressureY2 = new double[samples.size()];
		    
		    double oldtemperature = 0.0;
		    double oldpressure = 0.0;
		    
		    int count = 0;
		    for ( Sample entry : samples ) {
		    	double time = entry.getSampleTime();
		    	if ( time > maxTime) maxTime = time;
		    	timeX1[count] = time;
		    	
		    	double temperature = entry.getTemperature();
		    	if ( temperature > maxYValue ) maxYValue = temperature;
		    	if ( temperature < minYValue ) minYValue = temperature;
		    	if ( temperature != 0.0) {
		    		temperatureY1[count] = temperature;
		    		oldtemperature = temperature;
		    	} else {
		    		temperatureY1[count] = oldtemperature;
		    	}
		    	
		    	double pressure = entry.getPressure();
		    	if ( pressure > maxYValue ) maxYValue = pressure;
		    	if ( pressure < minYValue ) minYValue = pressure;
		    	if ( pressure != 0.0 ) {
		    		pressureY2[count] = pressure;
		    		oldpressure = pressure;
		    	} else {
		    		pressureY2[count] = oldpressure;
		    	}
		    	
		    	
		    	count++;
		    }
		    List<double[]> timeX = new ArrayList<double[]>();
		    timeX.add(timeX1);
		    timeX.add(timeX1);
		    List<double[]> depthY = new ArrayList<double[]>();
		    depthY.add(temperatureY1);
		    depthY.add(pressureY2);
		   
		    int[] colors = new int[] { Color.CYAN, Color.GREEN };
		    PointStyle[] styles = new PointStyle[] { PointStyle.CIRCLE, PointStyle.CIRCLE };
		    XYMultipleSeriesRenderer renderer = buildRenderer(colors, styles);
		    int length = renderer.getSeriesRendererCount();
		    
		    for (int i = 0; i < length; i++) {
		      ((XYSeriesRenderer) renderer.getSeriesRendererAt(i)).setFillPoints(true);
		    }
		    setChartSettings(renderer, "", "", -1, maxTime + 1, minYValue, maxYValue + 1,
		        Color.LTGRAY, Color.GRAY);
		    renderer.setXLabels(24);
		    renderer.setYLabels(20);
		    renderer.setShowGrid(false);
		    
		    Intent intent = ChartFactory.getLineChartIntent(_context, buildDataset(titles, timeX, depthY),
		        renderer, "Temperature/Pressure Graph");
		    return intent;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
