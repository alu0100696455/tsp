import java.io.File;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

/**
 * 
 * Clase que contiene los datos de entrada para el problema TSP. La entrada debe darse en formato XML
 * 
 * @author Jonathan Expósito Martín y Sergio Rodríguez Martín
 *
 */
public class LecturaTSP {
	private String nombre;
	private Matriz matriz;

	/**
	 * Constructor a partir de un fichero xml
	 * 
	 * @param nomFichero
	 *            Nombre y/o camino del fichero xml
	 */
	public LecturaTSP(String nomFichero) {
		File FicheroEntrada = new File(nomFichero);

		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(FicheroEntrada);
			doc.getDocumentElement().normalize();

			setNombre(doc.getElementsByTagName("name").item(0).getTextContent());

			NodeList listVertex = doc.getElementsByTagName("vertex");
			setMatriz(new Matriz(listVertex.getLength()));

			for (int i = 0; i < listVertex.getLength(); i++) {
				NodeList listEdge = listVertex.item(i).getChildNodes();
				for (int j = 0; j < listEdge.getLength(); j++) {
					Node nodeEdge = listEdge.item(j);
					if (nodeEdge.getNodeType() == Node.ELEMENT_NODE) {
						Element eEdge = (Element) nodeEdge;
						getMatriz().setElem(
								Double.parseDouble(eEdge.getAttribute("cost")),
								i, Integer.parseInt(eEdge.getTextContent()));
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	/*
	 * Getters & Setters
	 */
	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Matriz getMatriz() {
		return matriz;
	}

	public void setMatriz(Matriz matriz) {
		this.matriz = matriz;
	}
}
