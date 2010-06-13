package to.networld.android.divedroid.model.rdf;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.jaxen.JaxenException;
import org.jaxen.SimpleNamespaceContext;
import org.jaxen.XPath;
import org.jaxen.dom4j.Dom4jXPath;

/**
 * Superclass that hiddes the low level RDF parsing code. Each class that needs to parse
 * RDF files should extend from this class.
 * 
 * @author Alex Oberhauser
 *
 */
public class RDFParser {
	protected final SAXReader reader;
	protected Document document;
	protected HashMap<String, String> namespace = new HashMap<String, String>();
	protected String queryPrefix = "/";
	
	public RDFParser() {
		this.reader = new SAXReader();
		this.initDefaultNamespace();
	}
	
	protected void initDefaultNamespace() {
		this.namespace.put("rdf", "http://www.w3.org/1999/02/22-rdf-syntax-ns#");
		this.namespace.put("rdfs", "http://www.w3.org/2000/01/rdf-schema#");
	}
	
	@SuppressWarnings("unchecked")
	protected List<Element> getLinkNodes(String _query, HashMap<String, String> _namespaces) {
		try {
			XPath xpath = new Dom4jXPath(_query);
			xpath.setNamespaceContext(new SimpleNamespaceContext(_namespaces));
			return (List<Element>) xpath.selectNodes(this.document);
		} catch (JaxenException e) {
			e.printStackTrace();
			return new LinkedList<Element>();
		}
	}
	
	/**
	 * Please set before you call the function the variables this.namespace and this.queryPrefix.
	 * 
	 * @param _nodeName The name of the node for example 'dive:name'
	 * @return
	 */
	protected String getSingleNode(String _nodeName) {
		List<Element> nodeList = this.getLinkNodes(this.queryPrefix + "/" + _nodeName, this.namespace);
		if ( nodeList.size() > 0 )
			return nodeList.get(0).getTextTrim();
		else
			return null;
	}
	
	/**
	 * Please set before you call the function the variables this.namespace and this.queryPrefix.
	 * 
	 * @param _nodeName The name of the node for example 'dive:name'
	 * @return
	 */
	protected String getSingleResourceNode(String _nodeName, String _resourceName) {
		List<Element> nodeList = this.getLinkNodes(this.queryPrefix + "/" + _nodeName, this.namespace);
		if ( nodeList.size() > 0 )
			return nodeList.get(0).valueOf("@" + _resourceName);
		else
			return null;
	}
}
