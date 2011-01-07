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

import java.util.Vector;

/**
 * Models a RDF dive collection as described in:
 * http://scubadive.networld.to/dive.rdf#DiveCollection
 * 
 * @author Alex Oberhauser
 *
 */
public class DiveCollection {
	private final static String FACTBOOK_PREFIX = "http://www4.wiwiss.fu-berlin.de/factbook/data/";
	
	private String uri;
	private String factbookURI = null;
	
	/**
	 * @see http://scubadive.networld.to/dive.rdf#startDate
	 */
	private String startDate;
	
	/**
	 * @see http://scubadive.networld.to/dive.rdf#stopDate
	 */
	private String stopDate;
	
	/**
	 * @see http://scubadive.networld.to/dive.rdf#divebase
	 */
	private String diveBase;
	
	/**
	 * @see http://scubadive.networld.to/dive.rdf#country
	 */
	private String country;
	
	private double latitude;
	private double longitude;
	
	/**
	 * @see http://scubadive.networld.to/dive.rdf#dive
	 */
	private final Vector<Dive> dives = new Vector<Dive>();
	
	public DiveCollection(String _uri) {
		this.uri = _uri;
	}
	
	public void setStartDate(String _startDate) {
		this.startDate = _startDate;
	}
	
	public void setStopDate(String _stopDate) {
		this.stopDate = _stopDate;
	}
	
	public void setDiveBase(String _diveBase) {
		this.diveBase = _diveBase;
	}
	
	public void setLatitude(double _latitude) {
		this.latitude = _latitude;
	}
	
	public void setLongitude(double _longitude) {
		this.longitude = _longitude;
	}
	
	public void setCountry(String _country) {
		if ( _country.startsWith(FACTBOOK_PREFIX) ) {
			this.factbookURI = _country;
			this.country = _country.substring(FACTBOOK_PREFIX.length());
		} else {
			this.country = _country;
		}
	}
	
	public void addDive(Dive _dive) {
		this.dives.add(_dive);
	}
	
	public String getURI() { return this.uri; }
	public String getStartDate() { return this.startDate; }
	public String getStopDate() { return this.stopDate; }
	public String getDiveBase() { return this.diveBase; }
	public double getLatitude() { return this.latitude; }
	public double getLongitude() { return this.longitude; }
	public String getCountry() { return this.country; }
	public String getFactbookURI() { return this.factbookURI; }
	public Vector<Dive> getDives() { return this.dives; }
}
