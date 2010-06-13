package to.networld.android.divedroid.model;

import java.io.File;
import java.util.List;
import java.util.Vector;

import org.dom4j.DocumentException;
import org.dom4j.Element;

import to.networld.android.divedroid.model.rdf.Dive;
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
		String filepath = _file.getAbsolutePath();
		DiveCollection diveCollection = new DiveCollection(filepath);
		try {
			this.document = this.reader.read(_file);
			
			diveCollection.setDiveBase(this.getSingleNode("dive:divebase"));
			diveCollection.setStartDate(this.getSingleNode("dive:startDate"));
			diveCollection.setStopDate(this.getSingleNode("dive:stopDate"));
			diveCollection.setCountry(this.getSingleResourceNode("dive:country", "resource"));
			diveCollection.setLatitude(new Double(this.getSingleNode("/geo:lat")));
			diveCollection.setLongitude(new Double(this.getSingleNode("/geo:long")));
			
			List<Element> dives = this.getLinkNodes("/rdf:RDF/dive:DiveCollection/dive:dive", this.namespace);
			for ( Element dive : dives ) {
				Dive diveObj = new Dive(new File(filepath), dive.valueOf("@resource").replace("#", ""));
				diveCollection.addDive(diveObj);
			}
			
			this.collections.add(diveCollection);
			return true;
		} catch (DocumentException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public Vector<DiveCollection> getDiveCollections() { return this.collections; }
}
