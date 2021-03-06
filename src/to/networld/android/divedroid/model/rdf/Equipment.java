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

import org.dom4j.DocumentException;

public class Equipment extends RDFParser {
	private String filename = null;
	private String nodeid = null;
	
	public Equipment(File _file, String _nodeID) throws DocumentException {
		this.filename = _file.getAbsolutePath();
		this.nodeid = _nodeID;
		this.document = this.reader.read(_file);
		this.namespace.put("dive", "http://scubadive.networld.to/dive.rdf#");
		this.namespace.put("foaf", "http://xmlns.com/foaf/0.1/");
		this.namespace.put("geo", "http://www.w3.org/2003/01/geo/wgs84_pos#");
		this.queryPrefix = "/rdf:RDF/dive:Equipment[@rdf:ID='" + _nodeID + "']";
	}
	
	public String getFilename() { return this.filename; }
	public String getNodeID() { return this.nodeid; }
	
	public String getType() { return EquipmentTypes.getTypeName(this.getSingleResourceNode("dive:equipmentType", "rdf:resource")); }
	public String getBrand() {
		String brand = this.getSingleNode("dive:equipmentBrand");
		if ( brand != null )
			return brand;
		else
			return "unknown";
	}
	public String getModel() {
		String model = this.getSingleNode("dive:equipmentModel");
		if ( model != null )
			return model;
		else
			return "unknown";
	}
}
