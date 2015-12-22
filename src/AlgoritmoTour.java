import java.util.ArrayList;

/**
 * 
 * Clase que implementa el algoritmo NN y 2OPT
 * 
 * @author Jonathan Expósito Martín y Sergio Rodríguez Martín
 *
 */
public class AlgoritmoTour {
	private Matriz matriz; // Matriz donde se almacenan los costos del grafo
	private MejorTour mejorTour;
	private double cotaSuperior;

	/**
	 * Constructor a partir de un fichero xml
	 * 
	 * @param xml
	 *            Nombre y/o camino del fichero xml
	 */
	public AlgoritmoTour(String xml) {
		matriz = new LecturaTSP(xml).getMatriz();
		cotaSuperior = 1E100;
	}

	/**
	 * Método para encontrar la mejor cota superior usando los algoritmos NN y
	 * 2OPT
	 */
	public void computoCotaSuperior() {
		algoritmoNN();
		setCotaSuperior();
		System.out.println("---------------------------");
		System.out.println("Tour NN: " + getMejorTour().getVertices());
		System.out.println("---------------------------");
		algoritmo2OPT();
		setCotaSuperior();
		System.out.println("Tour 2OPT: " + getMejorTour().getVertices());
		System.out.println("---------------------------");
		System.out.println("Cota superior: " + getCotaSuperior());
		System.out.println("---------------------------");
		AlgoritmoBB bbbb = new AlgoritmoBB(getMat(), 0);
		System.out.println(bbbb.execute());
	}

	/**
	 * Método que soluciona el algoritmo NN para la matriz de costos dada
	 */
	private void algoritmoNN() {

		setMejorTour(new MejorTour(getMatriz()[0].length));

		getMejorTour().getVertices().set(0, 0);
		int ciudadActual = 0;

		// Recorre todas las ciudades
		int i = 1;
		while (i < getMejorTour().getVertices().size()) {
			int ciudadSiguiente = encontrarMin(getMatriz()[ciudadActual]);
			// Si la ciudad no es -1 (existe una ciudad siguiente)
			if (ciudadSiguiente != -1) {
				// añade la ciudad a getMejorTour().getVertices()
				getMejorTour().getVertices().set(i, ciudadSiguiente);
				// actualiza ciudadActual e i
				ciudadActual = ciudadSiguiente;
				i++;
			}
		}
	}

	/**
	 * Método que busca la ciudad siguiente con menor costo
	 * 
	 * @param fila
	 *            Costos de las ciudades
	 * @return Índice la ciudad siguiente con menor costo
	 */
	private int encontrarMin(double[] fila) {

		int ciudadSiguiente = -1;
		int i = 0;
		double min = Double.MAX_VALUE;

		while (i < fila.length) {
			if (isCiudadEnTrayecto(getMejorTour().getVertices(), i) == false
					&& fila[i] < min) {
				min = fila[i];
				ciudadSiguiente = i;
			}
			i++;
		}
		return ciudadSiguiente;
	}

	/**
	 * Método que soluciona el algoritmo 2OPT para la matriz de costos dada
	 */
	private void algoritmo2OPT() {
		double mejorGanancia = Double.MAX_VALUE;
		int mejorI = Integer.MAX_VALUE;
		int mejorJ = Integer.MAX_VALUE;

		while (mejorGanancia >= 0) {
			mejorGanancia = 0;
			for (int i = 0; i < getMejorTour().getVertices().size(); i++) {
				for (int j = 0; j < getMejorTour().getVertices().size(); j++) {
					if (i != j) {
						double ganancia = calcularGanancia(i, j);
						if (ganancia < mejorGanancia) {
							mejorGanancia = ganancia;
							mejorI = i;
							mejorJ = j;
						}
					}
				}
			}

			if (mejorI != Integer.MAX_VALUE && mejorJ != Integer.MAX_VALUE) {
				intercambio(mejorI, mejorJ);
			}
		}

	}

	/**
	 * Método que calcula la ganancia entre dos ciudades
	 */
	private double calcularGanancia(final int indiceCiudad1,
			final int indiceCiudad2) {

		int orig1 = getMejorTour().getVertices().get(indiceCiudad1);
		int orig2 = getMejorTour().getVertices().get(indiceCiudad2);

		int dest1 = getDestino(indiceCiudad1);
		int dest2 = getDestino(indiceCiudad2);

		return ((getMatriz()[dest1][dest2] + getMatriz()[orig1][orig2]) - (getMatriz()[orig1][dest1] + getMatriz()[orig2][dest2]));
	}

	private void intercambio(final int indiceCiudad1, final int indiceCiudad2) {

		int indiceDest1 = getIndiceDestino(indiceCiudad1);
		int indiceDest2 = getIndiceDestino(indiceCiudad2);

		ArrayList<Integer> nuevoTour = new ArrayList<Integer>();

		int i = 0;
		while (i <= indiceCiudad1) {
			if (isCiudadEnTrayecto(nuevoTour,
					getMejorTour().getVertices().get(i)) == false) {
				nuevoTour.add(getMejorTour().getVertices().get(i));
			}
			i++;
		}

		i = indiceCiudad2;
		while (i >= indiceDest1) {
			if (isCiudadEnTrayecto(nuevoTour,
					getMejorTour().getVertices().get(i)) == false) {
				nuevoTour.add(getMejorTour().getVertices().get(i));
			}
			i--;
		}

		i = indiceDest2;
		while (i < getMejorTour().getVertices().size()) {
			if (isCiudadEnTrayecto(nuevoTour,
					getMejorTour().getVertices().get(i)) == false) {
				nuevoTour.add(getMejorTour().getVertices().get(i));
			}
			i++;
		}

		for (int j = 0; j < nuevoTour.size(); j++) {
			getMejorTour().getVertices().set(j, nuevoTour.get(j));
		}

	}

	// Herramientas para los algoritmos

	/**
	 * Método para saber si la ciudad está o no en la trayectoria recorrida.
	 */
	private boolean isCiudadEnTrayecto(ArrayList<Integer> tour, int ciudad) {
		for (int i = 0; i < tour.size(); i++) {
			if (tour.get(i) == ciudad) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Método que devuelve la ciudad siguiente al índice de la ciudad dada
	 */
	private int getDestino(int indiceOrigen) {
		if (indiceOrigen + 1 == getMejorTour().getVertices().size()) {
			return getMejorTour().getVertices().get(0);
		}
		return getMejorTour().getVertices().get(indiceOrigen + 1);
	}

	/**
	 * Método que devuelve el índice en la trayectoria de la ciudad siguiente al
	 * índice de la ciudad dada
	 */
	private int getIndiceDestino(int indiceOrigen) {
		if (indiceOrigen + 1 == getMejorTour().getVertices().size()) {
			return 0;
		}
		return indiceOrigen + 1;
	}

	public String toString() {
		return matriz.toString();
	}

	/*
	 * Getters & Setters
	 */
	public double[][] getMatriz() {
		return matriz.getMatriz();
	}
	
	public Matriz getMat() {
		return matriz;
	}

	public void setMatriz(Matriz matriz) {
		this.matriz = matriz;
	}

	public MejorTour getMejorTour() {
		return mejorTour;
	}

	public void setMejorTour(MejorTour mejorTour) {
		this.mejorTour = mejorTour;
	}

	public double getCotaSuperior() {
		return cotaSuperior;
	}

	public void setCotaSuperior(double cotaSuperior) {
		this.cotaSuperior = cotaSuperior;
	}

	/**
	 * Método para establecer el valor de la cota superior
	 */
	public void setCotaSuperior() {
		double acc = 0.0;
		for (int i = 0; i < getMejorTour().getVertices().size() - 1; i++)
			acc += getMatriz()[getMejorTour().getVertices().get(i)][getMejorTour()
					.getVertices().get(i + 1)];
		if (acc < getCotaSuperior())
			setCotaSuperior(acc);
	}
}
