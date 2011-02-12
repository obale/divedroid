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

import java.io.File;
import java.util.List;
import java.util.Vector;

import org.dom4j.DocumentException;
import org.dom4j.Element;

/**
 * Models a RDF dive collection as described in:
 * http://scubadive.networld.to/dive.rdf#DiveCollection
 * 
 * @author Alex Oberhauser
 *
 */
public class DiveCollection extends RDFParser {
	private final static String FACTBOOK_PREFIX = "http://www4.wiwiss.fu-berlin.de/factbook/data/";
	private final File file;
	public DiveCollection(File _file) throws DocumentException {
		super();
		this.file = _file;
		this.document = this.reader.read(_file);
		this.namespace.put("dive", "http://scubadive.networld.to/dive.rdf#");
		this.namespace.put("foaf", "http://xmlns.com/foaf/0.1/");
		this.namespace.put("rdf", "http://www.w3.org/1999/02/22-rdf-syntax-ns#");
		this.namespace.put("geo", "http://www.w3.org/2003/01/geo/wgs84_pos#");
		this.queryPrefix = "/rdf:RDF/dive:DiveCollection";
	}
	
	public boolean isCollection() {
		List<Element> dives = this.getLinkNodes(this.queryPrefix + "/dive:dive");
		if ( dives.size() > 0 )
			return true;
		return false;
	}
	
	public String getID() { return this.getSingleResourceNode(".", "rdf:ID"); }
	
	public Vector<Dive> getDives() {
		Vector<Dive> diveElements = new Vector<Dive>();
		List<Element> dives = this.getLinkNodes(this.queryPrefix + "/dive:dive");
		for ( Element dive : dives ) {
			try {
				Dive diveObj = new Dive(this.file, dive.valueOf("@rdf:resource").replace("#", ""));
				diveElements.add(diveObj);
			} catch (DocumentException e) {
				e.printStackTrace();
				break;
			}
		}
		return diveElements;
	}
	
	public String getStartDate() { return this.getSingleNode("dive:startDate"); }
	public String getStopDate() { return this.getSingleNode("dive:stopDate"); }
	
	public String getDiveBase() { return this.getSingleNode("dive:divebase"); }
	
	public String getLatitude() { return this.getSingleNode("/geo:lat"); }
	public String getLongitude() { return this.getSingleNode("/geo:long"); }
	
	public String getCountry() {
		String country = this.getSingleResourceNode("dive:country", "rdfs:label");
		if ( country != null && !country.equals("") )
			return country;
		
		country = this.getSingleResourceNode("dive:country", "rdf:resource");
		if ( country == null )
			return null;
		else if ( country.startsWith(FACTBOOK_PREFIX) )
			return country.substring(FACTBOOK_PREFIX.length());
		else
			return country;
	}
	
	public String getFactbookURI() {
		String factbookURI = this.getSingleResourceNode("dive:country", "resource");
		if ( factbookURI == null )
			return null;
		else if ( factbookURI.startsWith(FACTBOOK_PREFIX) )
			return factbookURI;
		else
			return null;
	}
}
