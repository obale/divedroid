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
 * 
 * @author Alex Oberhauser
 *
 */
public class Dive extends RDFParser {
	private String filename = null;
	private String path = null;
	private String nodeid = null;
	private int id = -1;
	
	public Dive(File _file, String _nodeID) throws DocumentException {
		super();
		this.filename = _file.getAbsolutePath();
		this.path = _file.getParent() + "/";
		this.nodeid = _nodeID;
		this.document = this.reader.read(_file);
		this.namespace.put("dive", "http://scubadive.networld.to/dive.rdf#");
		this.namespace.put("foaf", "http://xmlns.com/foaf/0.1/");
		this.namespace.put("geo", "http://www.w3.org/2003/01/geo/wgs84_pos#");
		this.queryPrefix = "/rdf:RDF/dive:Dive[@rdf:ID='" + _nodeID + "']";
		this.setID();
	}
	
	private void setID() {
		List<Element> ids = this.getLinkNodes(this.queryPrefix + "/dive:id");
		if ( ids.size() > 0 ) {
			try {
				this.id = new Integer(ids.get(0).getTextTrim());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public String getActivity() { return this.getSingleNode("dive:activity"); }
	public String getDateTime() { return this.getSingleNode("dive:dateTime"); }
	public String getDivesite() { return this.getSingleNode("dive:divesite"); }
	public String getCountry() { return this.getSingleResourceNode("dive:country", "rdfs:label"); }
	public String getLocation() { return this.getSingleNode("dive:location"); }
	public String getComment() { return this.getSingleNode("dive:comment"); }
	public String getEntranceType() { return this.getSingleNode("dive:entrancetype"); }
	public String getBoatName() { return this.getSingleNode("dive:boatname"); }
	public String getWaterType() { return this.getSingleNode("dive:watertype"); }
	public String getWeight() { return this.getSingleNode("dive:weight"); }
	public String getExposureProtection() { return this.getSingleNode("dive:exposureprotection"); }
	public String getMaxDepth() { return this.getSingleNode("dive:maxdepth"); }
	public String getBottomTime() { return this.getSingleNode("dive:bottomtime"); }
	
	public String getLatitude() { return this.getSingleNode("/geo:lat"); }
	public String getLongitude() { return this.getSingleNode("/geo:long"); }
	
	public String getScubaTankIn() { return this.getSingleNode("dive:scubatankin"); }
	public String getScubaTankOut() { return this.getSingleNode("dive:scubatankout"); }
	
	public String getWaterVisibility() { return this.getSingleNode("dive:waterVisibility"); }
	public String getAirTemperature() { return this.getSingleNode("dive:airTemperature"); }
	public String getBottomTemperature() { return this.getSingleNode("dive:bottomTemperature"); }
	public String getWeatherCondition() { return this.getSingleNode("dive:weatherCondition"); }
	
	public String getProfileLink() { return this.getSingleResourceNode("dive:seeDiveProfile", "rdf:resource"); }
	
	public String getGeoImage() {
		String geoImage = this.getSingleResourceNode("geo:image", "rdf:resource");
		if ( geoImage == null ) return null;
		if ( geoImage.startsWith("/"))
			return geoImage;
		else
			return this.path + geoImage;
	}
	
	public Vector<Buddy> getBuddies() {
		Vector<Buddy> buddies = new Vector<Buddy>();
		List<Element> nodeList = this.getLinkNodes(this.queryPrefix + "/dive:partner");
		for ( Element entry : nodeList ) {
			String buddyURI = entry.valueOf("@rdf:resource");
			try {
				Buddy buddy = new Buddy(new File(this.filename), buddyURI.replace("#", ""));
				buddies.add(buddy);
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}
		}
		return buddies;
	}
	
	public String getFilename() { return this.filename; }
	public String getPath() { return this.path; }
	public String getNodeID() { return this.nodeid; }
	
	public int getID() { return this.id; }
	public String getName() { return this.getSingleNode("dive:name"); }
}
