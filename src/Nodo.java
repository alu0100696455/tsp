import java.util.ArrayList;

/**
 * Clase que representa un nodo
 * 
 * @author Jonathan Expósito Martín y Sergio Rodríguez Martín
 */
public class Nodo {
	public Nodo padre;
	private double costePadre;

	private Matriz costos;
	private ArrayList<Integer> conjunto;

	public int indice;

	/**
	 * @param padre
	 *            El padre de este nodo
	 * @param costePadre
	 *            El coste entre el nodo padre y este
	 * @param distancias
	 *            El array de distancias entre nodos
	 * @param conjunto
	 *            El conjunto de nodos que están siendo calculados
	 * @param indice
	 *            El índice del nodo a crear
	 */
	public Nodo(Nodo padre, double costePadre, Matriz distancias,
			ArrayList<Integer> conjunto, int indice) {
		this.padre = padre;
		this.costePadre = costePadre;
		this.costos = distancias;
		this.conjunto = conjunto;
		this.indice = indice;
	}

	/**
	 * Comprueba si el nodo es o no final
	 */
	public boolean isFinal() {
		return conjunto.size() == 1;
	}

	/**
	 * Genera y devuelve el nodo hijo
	 */
	public Nodo[] generarHijo() {
		Nodo[] hijo = new Nodo[conjunto.size() - 1];
		ArrayList<Integer> nuevoConjunto = new ArrayList<Integer>(
				conjunto.size() - 1);
		for (int posicion : conjunto) {
			if (posicion == indice)
				continue;

			nuevoConjunto.add(posicion);
		}

		for (int j = 0; j < hijo.length; j++)
			hijo[j] = new Nodo(this, costos.getElem(indice,
					nuevoConjunto.get(j)), costos, nuevoConjunto,
					nuevoConjunto.get(j));

		return hijo;
	}

	/**
	 * Método que obtiene la trayectoria hasta este punto
	 */
	public ArrayList<Integer> getTour() {
		int tam = costos.getTam() - conjunto.size() + 1;
		ArrayList<Integer> tour = new ArrayList<Integer>(tam);
		for (int i = 0; i < tam; i++)
			tour.add(0);
		getIndiceTour(tour, tam - 1);
		return tour;
	}

	/**
	 * Método recursivo para completar el array de tour
	 *
	 * @param tour
	 *            El array que representa la tour
	 * @param ind
	 *            El índice de este nodo
	 */
	public void getIndiceTour(ArrayList<Integer> tour, int ind) {
		tour.set(ind, indice);
		if (padre != null)
			padre.getIndiceTour(tour, ind - 1);
	}

	/**
	 * Método que obtiene el costo límite inferior de este nodo
	 */
	public double getLimiteInferior() {
		double valor = 0;

		if (conjunto.size() == 2)
			return getCostoTour()
					+ costos.getElem(conjunto.get(0), conjunto.get(1));

		for (int posicion : conjunto) {
			double inf1 = Double.MAX_VALUE;
			double inf2 = Double.MAX_VALUE;

			for (int otro : conjunto) {
				if (otro == posicion)
					continue;

				double cost = costos.getElem(posicion, otro);
				if (cost < inf1) {
					inf2 = inf1;
					inf1 = cost;
				} else if (cost < inf2) {
					inf2 = cost;
				}
			}

			valor += inf1 + inf2;
		}

		return getCostoPadre() + valor / 2;
	}

	/**
	 * Método que devuelve el costo entero del tour
	 */
	public double getCostoTour() {
		return costos.getElem(0, indice) + getCostoPadre();
	}

	/**
	 * Método que devuelve el costo hasta el nodo padre en este punto
	 */
	public double getCostoPadre() {
		if (padre == null)
			return 0;

		return costePadre + padre.getCostoPadre();
	}
}