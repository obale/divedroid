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

public abstract class EquipmentTypes {
	public static String getTypeName(String _typeURI) {
		if ( _typeURI.equals("http://scubadive.networld.to/equipment.rdf#mask") )
			return "Mask";
		else if ( _typeURI.equals("http://scubadive.networld.to/equipment.rdf#snorkel") )
			return "Snorkel";
		else if ( _typeURI.equals("http://scubadive.networld.to/equipment.rdf#fins") )
			return "Fins";
		else if ( _typeURI.equals("http://scubadive.networld.to/equipment.rdf#wetSuit") )
			return "Wet Suit";
		else if ( _typeURI.equals("http://scubadive.networld.to/equipment.rdf#regulator") )
			return "Regulator";
		else if ( _typeURI.equals("http://scubadive.networld.to/equipment.rdf#firstStage") )
			return "First Stage";
		else if ( _typeURI.equals("http://scubadive.networld.to/equipment.rdf#secondStage") )
			return "Second Stage";
		else if ( _typeURI.equals("http://scubadive.networld.to/equipment.rdf#octobus") )
			return "Octobus";
		else if ( _typeURI.equals("http://scubadive.networld.to/equipment.rdf#depthGauge") )
			return "Depth Gauge";
		else if ( _typeURI.equals("http://scubadive.networld.to/equipment.rdf#spg") )
			return "SPG";
		else if ( _typeURI.equals("http://scubadive.networld.to/equipment.rdf#compass") )
			return "Compass";
		else if ( _typeURI.equals("http://scubadive.networld.to/equipment.rdf#bcd") )
			return "BCD";
		else if ( _typeURI.equals("http://scubadive.networld.to/equipment.rdf#diveComputer") )
			return "Dive Computer";
		else if ( _typeURI.equals("http://scubadive.networld.to/equipment.rdf#diveLight") )
			return "Dive Light";
		else if ( _typeURI.equals("http://scubadive.networld.to/equipment.rdf#diveKnife") )
			return "Dive Knife";
		else if ( _typeURI.equals("http://scubadive.networld.to/equipment.rdf#gearBag") )
			return "Gear Bag";
		else
			return "unknown";
	}
}
