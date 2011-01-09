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

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Namespace;
import org.dom4j.QName;
import org.dom4j.io.SAXReader;
import org.jaxen.JaxenException;
import org.jaxen.SimpleNamespaceContext;
import org.jaxen.XPath;
import org.jaxen.dom4j.Dom4jXPath;

/**
 * Superclass that hiddes the low level RDF parsing code. Each class that needs to parse
 * RDF files should extend this class.
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
	protected List<Element> getLinkNodes(String _query) {
		try {
			XPath xpath = new Dom4jXPath(_query);
			xpath.setNamespaceContext(new SimpleNamespaceContext(this.namespace));
			return (List<Element>) xpath.selectNodes(this.document);
		} catch (JaxenException e) {
			e.printStackTrace();
			return new LinkedList<Element>();
		}
	}
	
	@SuppressWarnings("unchecked")
	protected String getResourceLinkNodes(String _query, String _resource) {
		try {
			XPath xpath = new Dom4jXPath(_query);
			xpath.setNamespaceContext(new SimpleNamespaceContext(this.namespace));
			List<Element> elements = (List<Element>)xpath.selectNodes(this.document);
			if ( elements.size() > 0 )
				return elements.get(0).attributeValue(new QName("resource", new Namespace("rdf", "http://www.w3.org/1999/02/22-rdf-syntax-ns#")));
		} catch (JaxenException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Please set before you call the function the variables this.namespace and this.queryPrefix.
	 * 
	 * @param _nodeName The name of the node for example 'dive:name'
	 * @return
	 */
	protected String getSingleNode(String _nodeName) {
		List<Element> nodeList = this.getLinkNodes(this.queryPrefix + "/" + _nodeName);
		if ( nodeList.size() > 0 )
			return nodeList.get(0).getTextTrim();
		else
			return null;
	}
	
	protected Vector<String> getResourceNodes(String _nodeName, String _resourceName) {
		List<Element> resourceList = this.getLinkNodes(this.queryPrefix + "/" + _nodeName);
		Vector<String> retVector = new Vector<String>();
		for ( Element entry : resourceList ) {
			retVector.add(entry.valueOf("@" + _resourceName));
		}
		return retVector;
	}
	
	/**
	 * Please set before you call the function the variables this.namespace and this.queryPrefix.
	 * 
	 * @param _nodeName The name of the node for example 'dive:name'
	 * @return
	 */
	protected String getSingleResourceNode(String _nodeName, String _resourceName) {
		List<Element> nodeList = this.getLinkNodes(this.queryPrefix + "/" + _nodeName);
		if ( nodeList.size() > 0 )
			return nodeList.get(0).valueOf("@" + _resourceName);
		else
			return null;
	}
}
