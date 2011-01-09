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
import org.dom4j.DocumentException;

import to.networld.android.divedroid.model.rdf.DiveProfile;
import to.networld.android.divedroid.model.rdf.Sample;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;

public class DiveProfileGraph extends AbstractChart {
	private final File file;
	private final String nodeid;
	
	public DiveProfileGraph(File _file, String _nodeid) {
		this.file = _file;
		this.nodeid = _nodeid;
	}
	
	public Intent execute(Context _context) {
		DiveProfile diveProfile;
		try {
			diveProfile = new DiveProfile(this.file, this.nodeid);
			Vector<Sample> samples = diveProfile.getSamples();
		  
		    String[] titles = new String[] { "Depth (Meter)" };
		    
		    double maxTime = 0;
		    double maxDepth = 0;
		    double[] timeX1 = new double[samples.size()];
		    double[] depthY1 = new double[samples.size()];
		    int count = 0;
			Vector<Integer> bookmarks = new Vector<Integer>();
		    for ( Sample entry : samples ) {
		    	double time = entry.getSampleTime();
		    	if ( time > maxTime) maxTime = time;
		    	timeX1[count] = time;
		    	
		    	double depth = entry.getDepth();
		    	if ( depth > maxDepth ) maxDepth = depth;
		    	depthY1[count] = depth;
		    	
		    	String bookmark = entry.getBookmark();
		    	if ( bookmark != null )
		    		bookmarks.add(count);
		    	
		    	count++;
		    }
		    List<double[]> timeX = new ArrayList<double[]>();
		    timeX.add(timeX1);
		    List<double[]> depthY = new ArrayList<double[]>();
		    depthY.add(depthY1);
		    
		    int[] colors = new int[] { Color.CYAN };
		    PointStyle[] styles = new PointStyle[] { PointStyle.CIRCLE };
		    XYMultipleSeriesRenderer renderer = buildRenderer(colors, styles);
		    int length = renderer.getSeriesRendererCount();
		    
		    for (int i = 0; i < length; i++) {
		    	((XYSeriesRenderer) renderer.getSeriesRendererAt(i)).setFillPoints(true);
		    	((XYSeriesRenderer) renderer.getSeriesRendererAt(i)).setFillBelowLine(true);
		    }
		    setChartSettings(renderer, "", "", 0, maxTime, maxDepth, 0,
		        Color.LTGRAY, Color.GRAY);
		    renderer.setXLabels(18);
		    renderer.setYLabels(22);
		    renderer.setShowGrid(false);
		    
		    Intent intent = ChartFactory.getLineChartIntent(_context, buildDataset(titles, timeX, depthY),
		        renderer, "Dive Profile Graph");
		    return intent;
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return null;
	  
	}

}
