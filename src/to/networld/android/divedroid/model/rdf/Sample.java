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

package to.networld.android.divedroid.model.rdf;

public class Sample {
	private final double sampleTime;
	private double depth = 0.0;
	private double pressure = 0.0;
	private double temperature = 0.0;
	private String bookmark = null;
	
	public Sample(double _sampleTime) {
		this.sampleTime = _sampleTime;
	}
	
	public void setDepth(double _depth) {
		this.depth = _depth;
	}
	
	public void setPressure(double _pressure) {
		this.pressure = _pressure;
	}
	
	public void setTemperature(double _temperature) {
		this.temperature = _temperature;
	}
	
	public void setBookmark(String _bookmark) {
		this.bookmark = _bookmark;
	}
	
	public double getSampleTime() { return this.sampleTime; }
	public double getDepth() { return this.depth; }
	public double getPressure() { return this.pressure; }
	public double getTemperature() { return this.temperature; }
	public String getBookmark() { return this.bookmark; }
}
