package com.rampal.abhi.updatetvdata;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.nio.BufferOverflowException;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import java.util.zip.GZIPInputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.net.ftp.FTPClient;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class UploadDownloadFile {
	
	public static final String NZEPG_URL = "http://nzepg.org/freeview.xml.gz";
	public static final String FREEVIEW_XML = "freeview.xml";
	public static final String PARTIAL_XML = "partial.xml";
	public static final String LOGIN_PROPERTIES = "login.properties";

	private static void downloadXmlToLocalMachine() {
		try {
			URL url = new URL(NZEPG_URL);
			URLConnection connection = url.openConnection();

			InputStream stream = connection.getInputStream();

			System.out.println("Content Encoding: "+ connection.getContentEncoding());

			stream = new GZIPInputStream(stream);

			InputSource is = new InputSource(stream);
			InputStream input = new BufferedInputStream(is.getByteStream());
			File file = new File(FREEVIEW_XML);
			if(!file.exists()) {
			    file.createNewFile();
			} 
			OutputStream output = new FileOutputStream(FREEVIEW_XML);

			byte data[] = new byte[3000000];
			int count;
			while ((count = input.read(data)) != -1) {
				output.write(data, 0, count);
			}

			output.flush();
			output.close();
			input.close();
			stream.close();
		} catch (BufferOverflowException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			System.out.println("Unknown Host Exception...");
			downloadXmlToLocalMachine();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/*private static void removeSelectedElements(File sourceFile, int num) {
		try {
			DocumentBuilder builder2 = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			
			Document sourceDom = builder2.parse(new InputSource(new FileReader(sourceFile)));
			sourceDom.getDocumentElement().normalize();

			NodeList nList = sourceDom.getElementsByTagName("programme");
			System.out.println("Total nodes: "+ nList.getLength());
			int totalNodesBefore = nList.getLength();
			int totalNodesAfter = 0;
			while (totalNodesBefore != totalNodesAfter){
				totalNodesBefore = sourceDom.getElementsByTagName("programme").getLength();
				remove(nList, sourceDom, sourceFile, num);
				totalNodesAfter = sourceDom.getElementsByTagName("programme").getLength();
			}
			sourceDom.getDocumentElement().normalize();
			sourceDom.normalize();
			System.out.println(sourceDom.getElementsByTagName("programme").getLength()+ " nodes remaining");
			
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}*/

	private static void removeSelectedElements(File sourceFile) {
	try {
		DocumentBuilder builder2 = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		
		Document sourceDom = builder2.parse(new InputSource(new FileReader(sourceFile)));
		sourceDom.getDocumentElement().normalize();

		NodeList nList = sourceDom.getElementsByTagName("programme");
		System.out.println("Total nodes: "+ nList.getLength());
		int totalNodesBefore = nList.getLength();
		int totalNodesAfter = 0;
		while (totalNodesBefore != totalNodesAfter){
			totalNodesBefore = sourceDom.getElementsByTagName("programme").getLength();
			remove(nList, sourceDom, sourceFile);
			totalNodesAfter = sourceDom.getElementsByTagName("programme").getLength();
		}
		sourceDom.getDocumentElement().normalize();
		sourceDom.normalize();
		System.out.println(sourceDom.getElementsByTagName("programme").getLength()+ " nodes remaining");
		
	} catch (ParserConfigurationException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (FileNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (SAXException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}
	/*public static void remove(NodeList nList, Document sourceDom,File sourceFile, int num) {
		Calendar cal = Calendar.getInstance();
		
		for (int i = 0; i < nList.getLength();) {
			Node node = nList.item(i);
			Element e = (Element) node;
			if (e.getNodeType() == Node.ELEMENT_NODE) {
				Calendar startDate = getDateFromXML(e.getAttribute("start"));
				Calendar endDate = getDateFromXML(e.getAttribute("stop"));
				if (cal.get(Calendar.YEAR) == startDate.get(Calendar.YEAR) && cal.get(Calendar.YEAR) == endDate.get(Calendar.YEAR)
						&& cal.get(Calendar.MONTH) == startDate.get(Calendar.MONTH) && cal.get(Calendar.MONTH) == endDate.get(Calendar.MONTH)
						&& cal.get(Calendar.DAY_OF_MONTH)+num != startDate.get(Calendar.DAY_OF_MONTH)
						&& cal.get(Calendar.DAY_OF_MONTH)+num != endDate.get(Calendar.DAY_OF_MONTH)) {
	
					removeAll(node, "programme");
					TransformerFactory tf = TransformerFactory.newInstance();
					try {
						Transformer t = tf.newTransformer();
						t.transform(new DOMSource(sourceDom), new StreamResult(sourceFile));
					} catch (TransformerException ef) {
						// TODO Auto-generated catch block
						ef.printStackTrace();
					}
				} 
			}
			i++;
		}
	}*/
	
	public static void remove(NodeList nList, Document sourceDom,File sourceFile) {
		Calendar cal = Calendar.getInstance();
		
		for (int i = 0; i < nList.getLength();) {
			Node node = nList.item(i);
			Element e = (Element) node;
			if (e.getNodeType() == Node.ELEMENT_NODE) {
				Calendar startDate = getDateFromXML(e.getAttribute("start"));
				Calendar endDate = getDateFromXML(e.getAttribute("stop"));
				if (cal.get(Calendar.YEAR) == startDate.get(Calendar.YEAR) && cal.get(Calendar.YEAR) == endDate.get(Calendar.YEAR)
						&& (cal.get(Calendar.MONTH) == startDate.get(Calendar.MONTH) && cal.get(Calendar.MONTH) == endDate.get(Calendar.MONTH)||
								cal.get(Calendar.MONTH) != startDate.get(Calendar.MONTH) && cal.get(Calendar.MONTH) != endDate.get(Calendar.MONTH))
						&& cal.get(Calendar.DAY_OF_MONTH) != startDate.get(Calendar.DAY_OF_MONTH)
						&& cal.get(Calendar.DAY_OF_MONTH) != endDate.get(Calendar.DAY_OF_MONTH)) {
	
					removeAll(node, "programme");
					TransformerFactory tf = TransformerFactory.newInstance();
					try {
						Transformer t = tf.newTransformer();
						t.transform(new DOMSource(sourceDom), new StreamResult(sourceFile));
					} catch (TransformerException ef) {
						// TODO Auto-generated catch block
						ef.printStackTrace();
					}
				} 
			}
			i++;
		}
	}
	public static void removeAll(Node node, String name) {
		if ((name == null || node.getNodeName().equals(name))) {
			node.getParentNode().removeChild(node);
		} else {
			NodeList list = node.getChildNodes();
			for (int i = 0; i < list.getLength(); i++) {
				removeAll(list.item(i), name);
			}
		}
	}

	public static Calendar getDateFromXML(String xmlDateFormat) {// 20111223160000
		String year = (String) xmlDateFormat.subSequence(0, 4);
		String month = (String) xmlDateFormat.subSequence(4, 6);
		String day = (String) xmlDateFormat.subSequence(6, 8);
		String hourOfTheDay = (String) xmlDateFormat.subSequence(8, 10);
		String minute = (String) xmlDateFormat.subSequence(10, 12);
		String seconds = (String) xmlDateFormat.subSequence(12, 14);

		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, Integer.parseInt(year));
		cal.set(Calendar.MONTH, Integer.parseInt(month) - 1);
		cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(day));
		cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hourOfTheDay));
		cal.set(Calendar.MINUTE, Integer.parseInt(minute));
		cal.set(Calendar.SECOND, Integer.parseInt(seconds));

		return cal;
	}

	/*public static void FTPUpload(int num) {
		FTPClient client = new FTPClient();
		FileInputStream fis = null;

		try {
			client.connect("homepages.slingshot.co.nz");
			Properties p = new Properties();
			p.load(new FileInputStream(LOGIN_PROPERTIES));
			client.login(p.getProperty("username"), p.getProperty("password"));

			String filename = FREEVIEW_XML;
			fis = new FileInputStream(filename);
			String fileName = "programData"+getDateString(new Date(), num)+".xml";
			System.out.println(fileName);
			client.storeFile("/public_html/"+fileName, fis);
			
			client.logout();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (fis != null) {
					fis.close();
				}
				client.disconnect();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}*/

	public static void FTPUpload() {
		FTPClient client = new FTPClient();
		FileInputStream fis = null;

		try {
			client.connect("homepages.slingshot.co.nz");
			Properties p = new Properties();
			p.load(new FileInputStream(LOGIN_PROPERTIES));
			client.login(p.getProperty("username"), p.getProperty("password"));

			String filename = FREEVIEW_XML;
			fis = new FileInputStream(filename);
			//String fileName = "programData"+getDateString(new Date())+".xml";
			String fileName = "programData.xml";
			System.out.println(fileName);
			client.storeFile("/public_html/"+fileName, fis);
			
			client.logout();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (fis != null) {
					fis.close();
				}
				client.disconnect();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private static String getDateString(Date date){
		Calendar cal = Calendar.getInstance();
		String day = String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
		String month = String.valueOf(cal.get(Calendar.MONTH));
		String year = String.valueOf(cal.get(Calendar.YEAR));
		return day+month+year;
	}
	
	/*private static String getDateString(Date date, int num){
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, num);
		String day = String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
		String month = String.valueOf(cal.get(Calendar.MONTH));
		String year = String.valueOf(cal.get(Calendar.YEAR));
		return day+month+year;
	}*/
	/*public static void main(String[] args) throws SAXException, IOException, ParserConfigurationException {
		for (int i=0; i<6; i++){
			System.out.println("Starting file donwload...");
			downloadXmlToLocalMachine();
			System.out.println("File download finished..");
			File file = new File(FREEVIEW_XML);
			removeSelectedElements(file, i);
			FTPUpload(i);
		}	
	}*/
	
	public static void main(String[] args) throws SAXException, IOException, ParserConfigurationException {

			System.out.println("Starting file donwload...");
			downloadXmlToLocalMachine();
			System.out.println("File download finished..");
			File file = new File(FREEVIEW_XML);
			removeSelectedElements(file);
			FTPUpload();
	}
}
