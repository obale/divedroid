package to.networld.android.divedroid.model.rdf;

import java.io.File;

import org.dom4j.DocumentException;

/**
 * 
 * @author Alex Oberhauser
 *
 */
public class FactbookCountry extends RDFParser {
	private final String fileURI;
	
	public FactbookCountry(String _fileURI) throws DocumentException {
		super();
		this.fileURI = _fileURI;
		this.document = this.reader.read(new File(_fileURI));
		this.namespace.put("factbook", "http://www4.wiwiss.fu-berlin.de/factbook/ns#");
	}
	
	public String getCountry() {
		return null;
	}
	
	public String getFileURI() { return this.fileURI; }
}
