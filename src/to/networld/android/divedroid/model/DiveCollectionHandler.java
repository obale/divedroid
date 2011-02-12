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

package to.networld.android.divedroid.model;

import java.io.File;
import java.util.Vector;

import org.dom4j.DocumentException;

import to.networld.android.divedroid.model.rdf.DiveCollection;
import to.networld.android.divedroid.model.rdf.RDFParser;

/**
 * Handles the dive collections that are stored on the device.
 * 
 * @author Alex Oberhauser
 *
 */
public class DiveCollectionHandler extends RDFParser {
	private final Vector<DiveCollection> collections = new Vector<DiveCollection>();
	
	public DiveCollectionHandler() {
		super();
		this.namespace.put("dive", "http://scubadive.networld.to/dive.rdf#");
		this.namespace.put("foaf", "http://xmlns.com/foaf/0.1/");
		this.namespace.put("geo", "http://www.w3.org/2003/01/geo/wgs84_pos#");
		this.queryPrefix = "/rdf:RDF/dive:DiveCollection";
	}
	
	public boolean addDiveCollection(File _file) {
		try {
			DiveCollection diveCollection = new DiveCollection(_file);
			if ( diveCollection.isCollection() ) {
				this.collections.add(diveCollection);
				return true;
			}
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public Vector<DiveCollection> getDiveCollections() { return this.collections; }
}
