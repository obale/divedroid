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
import org.dom4j.Node;

public class DiveProfile extends RDFParser {
	private final String filename;
	private final String nodeid;
	
	public DiveProfile(File _file, String _nodeID) throws DocumentException {
		super();
		this.filename = _file.getAbsolutePath();
		this.nodeid = _nodeID;
		this.document = this.reader.read(_file);
		this.namespace.put("dive", "http://scubadive.networld.to/dive.rdf#");
		this.namespace.put("rdf", "http://www.w3.org/1999/02/22-rdf-syntax-ns#");
		this.queryPrefix = "/rdf:RDF/dive:DiveProfile[@rdf:ID='" + _nodeID + "']";
	}
	
	public String getNodeID() { return this.nodeid; }
	public String getFilename() { return this.filename; }

	public Vector<Sample> getSamples() {
		Vector<Sample> samples = new Vector<Sample>();
		List<Element> sampleList = this.getLinkNodes(this.queryPrefix + "/dive:hasSample/dive:Sample");
		for ( Element entry : sampleList ) {
			Node sampleTimeNode = entry.selectSingleNode("dive:sampleTime");
			Sample tmpSample = null;
			if ( sampleTimeNode != null ) {
				try {
					double timeMinutes = (new Double(sampleTimeNode.getText()) / 60.0);
					tmpSample = new Sample(timeMinutes);
			
					Node depthNode = entry.selectSingleNode("dive:depth");
					if ( depthNode != null ) {
						try {
							double depth = new Double(depthNode.getText().replace(",", "."));
							tmpSample.setDepth(depth);
						} catch (Exception e) {}
					}
				
					Node pressureNode = entry.selectSingleNode("dive:pressure");
					if ( pressureNode != null ) {
						try {
							double pressure = new Double(pressureNode.getText().replace(",", "."));
							tmpSample.setPressure(pressure);
						} catch (Exception e) {}
					}
			
					Node temperatureNode = entry.selectSingleNode("dive:temperature");
					if ( temperatureNode != null ) {
						double temperature = new Double(temperatureNode.getText().replace(",", "."));
						tmpSample.setTemperature(temperature);
					}
		
					Node bookmarkNode = entry.selectSingleNode("dive:bookmark");
					if ( bookmarkNode != null )
						tmpSample.setBookmark(bookmarkNode.getText());
			
					samples.add(tmpSample);
				} catch (Exception e) {
					// No valid time, no valid sample.
				}
			}
		}
		return samples;
	}
}
