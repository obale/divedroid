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
	private String name = null;
	
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
		this.setName();
	}
	
	private void setID() {
		List<Element> ids = this.getLinkNodes(this.queryPrefix + "/dive:id", this.namespace);
		if ( ids.size() > 0 ) {
			try {
				this.id = new Integer(ids.get(0).getTextTrim());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private void setName() {
		this.name = this.getSingleNode("dive:name");
	}
	
	public String getActivity() { return this.getSingleNode("dive:activity"); }
	public String getDate() { return this.getSingleNode("dive:date"); }
	public String getDivesite() { return this.getSingleNode("dive:divesite"); }
	public String getCountry() { return this.getSingleResourceNode("dive:country", "rdfs:label"); }
	public String getLocation() { return this.getSingleNode("dive:location"); }
	public String getComment() { return this.getSingleNode("dive:comment"); }
	public String getEntranceType() { return this.getSingleNode("dive:entrancetype"); }
	public String getWaterType() { return this.getSingleNode("dive:watertype"); }
	public String getWeight() { return this.getSingleNode("dive:weight"); }
	public String getExposureProtection() { return this.getSingleNode("dive:exposureprotection"); }
	public String getMaxDeep() { return this.getSingleNode("dive:maxdeep"); }
	public String getBottomTime() { return this.getSingleNode("dive:bottomtime"); }
	public String getLatitude() { return this.getSingleNode("/geo:lat"); }
	public String getLongitude() { return this.getSingleNode("/geo:long"); }
	
	public String getGeoImage() {
		String geoImage = this.getSingleResourceNode("geo:image", "rdf:resource");
		if ( geoImage.startsWith("/"))
			return geoImage;
		else
			return this.path + geoImage;
	}
	
	public Vector<Buddy> getBuddies() {
		Vector<Buddy> buddies = new Vector<Buddy>();
		List<Element> nodeList = this.getLinkNodes(this.queryPrefix + "/dive:partner", this.namespace);
		for ( Element entry : nodeList ) {
			String buddyURI = entry.valueOf("@rdf:resource");
			try {
				Buddy buddy = new Buddy(new File(this.filename), buddyURI.replace("#", ""));
				buddies.add(buddy);
			} catch (DocumentException e) {
				e.printStackTrace();
				continue;
			}
		}
		return buddies;
	}
	
	public String getFilename() { return this.filename; }
	public String getNodeID() { return this.nodeid; }
	
	public int getID() { return this.id; }
	public String getName() { return this.name; }
}
