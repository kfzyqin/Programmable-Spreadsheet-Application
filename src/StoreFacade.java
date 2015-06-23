/*
SpreadSheet - a simple spreadsheet application.
Copyright (C) 2015 Eric McCreath and Zhenyue Qin, the Australian National University

This program is free software: you can redistribute it and/or modify it 
under the terms of the Creative Commons Attribution 4.0 International License
 */

import java.awt.Point;
import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
/**
 * StoreFacade - This provides a facade into the standard XML API for saving data of a spreadsheet.
 * 
 * @author Eric McCreath
 *
 */
public class StoreFacade {
	private DocumentBuilderFactory dbf;
	private DocumentBuilder db;
	private Document doc;
	private Element drawinge;
	private Element currentelement;
	private File file;

	public StoreFacade(File file, String name) {
		try {
			dbf = DocumentBuilderFactory.newInstance();
			db = dbf.newDocumentBuilder();
			doc = db.newDocument();
			drawinge = doc.createElement(name);
			currentelement = null;
			this.file = file;
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
	}

	public void start(String name) { // create and start a new element
		
		if (currentelement != null) end();
		currentelement = doc.createElement(name);
		
		
	}
	
	private void end() { // end the element at add it to the drawing element
		drawinge.appendChild(currentelement);
		currentelement = null;
	}
	

	public void addPoint( String namea, Point a) {
		assert(currentelement != null);
		currentelement.appendChild(xmlpoint(namea, a, doc));
	}
	
	
	

	public void close() {
		if (currentelement != null) end();
		try {
			doc.appendChild(drawinge);
			TransformerFactory transformerFactory = TransformerFactory
					.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(file);
			transformer.transform(source, result);
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Problem saving " + file + " " + e);
		}
	}
	

	static private Node xmlpoint(String name, Point p, Document doc) {
		Element e = doc.createElement(name);
		Element ex = doc.createElement("x");
		ex.setTextContent(Integer.toString(p.x));
		Element ey = doc.createElement("y");
		ey.setTextContent(Integer.toString(p.y));
		e.appendChild(ex);
		e.appendChild(ey);
		return e;
	}

	public void addText(String name, String text) {
		assert(currentelement != null);
		currentelement.appendChild(xmltext(name, text, doc));
	}
	
	
	private Node xmltext(String name, String value, Document doc2) {
		Element e = doc.createElement(name);
		e.setTextContent(value);
		return e;
	}

}
