package to.networld.android.divedroid.model.rdf;

import java.io.File;
import java.util.Vector;

import org.dom4j.DocumentException;

public class Diver extends RDFParser implements IDiver {
	private String filename = null;
	private String nodeid = null;
	
	public Diver(File _file, String _nodeID) throws Exception {
		this.filename = _file.getAbsolutePath();
		this.nodeid = _nodeID;
		this.document = this.reader.read(_file);
		this.namespace.put("dive", "http://scubadive.networld.to/dive.rdf#");
		this.namespace.put("foaf", "http://xmlns.com/foaf/0.1/");
		this.namespace.put("geo", "http://www.w3.org/2003/01/geo/wgs84_pos#");
		this.queryPrefix = "/rdf:RDF/dive:DiverProfile[@rdf:ID='" + _nodeID + "']";
	}
	
	public String getFilename() { return this.filename; }
	public String getNodeID() { return this.nodeid; }
	
	public String getRole() { return null; }
	
	public String getName() { return this.getSingleNode("foaf:name"); }
	
	public String getEMail() { 
		String email = this.getSingleResourceNode("foaf:mbox", "rdf:resource");
		if ( email != null )
			return email.replace("mailto:", "");
		else
			return null;
	}
	
	public String getPhone() { return this.getSingleResourceNode("foaf:phone", "rdf:resource"); }
	
	public String getCertOrg() { return this.getSingleNode("dive:certorg"); }
	public String getCertNr() { return this.getSingleNode("dive:certnr"); }
	public String getCertDate() { return this.getSingleNode("dive:certdate"); }
	
	public String getTotalDives() { return this.getSingleNode("dive:totalDives"); }
	
	public Vector<Equipment> getEquipment() {
		Vector<String> equipmentIDs = this.getResourceNodes("dive:hasEquipment", "rdf:resource");
		Vector<Equipment> equipment = new Vector<Equipment>();
		for ( String id : equipmentIDs ) {
			try {
				Equipment entry = new Equipment(new File(this.filename), id.replace("#", ""));
				equipment.add(entry);
			} catch (DocumentException e) {
				e.printStackTrace();
			}
		}
		return equipment;
	}
		
}
