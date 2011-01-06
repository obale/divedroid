package to.networld.android.divedroid.model.rdf;

import java.io.File;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;

public class Buddy extends RDFParser {
	private String filename = null;
	private String path = null;
	private String nodeid = null;
	
	public Buddy(File _file, String _nodeID) throws DocumentException {
		this.filename = _file.getAbsolutePath();
		this.path = _file.getParent() + "/";
		this.nodeid = _nodeID;
		this.document = this.reader.read(_file);
		this.namespace.put("dive", "http://scubadive.networld.to/dive.rdf#");
		this.namespace.put("foaf", "http://xmlns.com/foaf/0.1/");
		this.namespace.put("geo", "http://www.w3.org/2003/01/geo/wgs84_pos#");
		this.queryPrefix = "/rdf:RDF/dive:Diver[@rdf:ID='" + _nodeID + "']";
	}
	
	public String getFilename() { return this.filename; }
	public String getNodeID() { return this.nodeid; }
	
	public String getRole() { return this.getSingleNode("dive:role"); }
	private String[] getDiverProfileURI() { return this.getSingleResourceNode("dive:seeDiverProfile", "rdf:resource").split("#"); }
	
	private String getExternProfileElement(String _nodeName, String _resourceName) {
		String [] uri = this.getDiverProfileURI();
		if ( uri.length > 1 ) {
			if ( !uri[0].startsWith("/") )
				uri[0] = this.path + uri[0];
			File profileFile = new File(uri[0]);
			String prefix = "/rdf:RDF/dive:DiverProfile[@rdf:ID='" + uri[1] + "']";
			Document oldDocument = this.document;
			try {
				this.document = this.reader.read(profileFile);
				List<Element> nodeList = null;
				if ( _resourceName == null ) {
					nodeList = this.getLinkNodes(prefix + "/" + _nodeName, this.namespace);
					if ( nodeList.size() > 0 )
						return nodeList.get(0).getTextTrim();
				} else
					return this.getResourceLinkNodes(prefix + "/" + _nodeName, _resourceName, this.namespace);
			} catch (DocumentException e) {
				e.printStackTrace();
			} finally {
				this.document = oldDocument;	
			}
		}
		return null;
	}
	
	public String getName() { return this.getExternProfileElement("foaf:name", null); }
	public String getEMail() { return this.getExternProfileElement("foaf:mbox", "rdf:resource"); }
	public String getPhone() { return this.getExternProfileElement("foaf:phone", "rdf:resource"); }
	public String getCertOrg() { return this.getExternProfileElement("dive:certorg", null); }
	public String getCertNr() { return this.getExternProfileElement("dive:certnr", null); }
	public String getCertDate() { return this.getExternProfileElement("dive:certdate", null); }
}
