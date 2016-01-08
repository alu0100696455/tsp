import java.util.ArrayList;

/**
 * 
 * Clase que almacena el mejor tour obtenido hasta el momento
 * 
 * @author Jonathan Expósito Martín y Sergio Rodríguez Martín
 *
 */
public class MejorTour {
	private ArrayList<Integer> vertices;

	public MejorTour(int size) {
		setVertices(new ArrayList<Integer>());
		for (int i = 0; i < size; i++)
			getVertices().add(0);
	}

	/*
	 * Getters & Setters
	 */
	public ArrayList<Integer> getVertices() {
		return vertices;
	}

	public void setVertices(ArrayList<Integer> vertices) {
		this.vertices = vertices;
	}
}
