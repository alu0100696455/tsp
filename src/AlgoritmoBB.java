import java.util.ArrayList;
import java.util.HashSet;

/**
 * Clase que implementa el algoritmo BB
 * 
 * @author Jonathan Expósito Martín y Sergio Rodríguez Martín
 *
 */
public class AlgoritmoBB {
	Matriz distancias;
	double mejorCosto;
	ArrayList<Integer> mejorTour;

	public AlgoritmoBB(Matriz distancias) {
		this.distancias = distancias;
	}

	/**
	 * Calcula el tour más corto entre una serie de nodos
	 */
	public ArrayList<Integer> calcular() {
		HashSet<Integer> conjuntoPosicion = new HashSet<Integer>(
				distancias.getTam());
		for (int i = 0; i < distancias.getTam(); i++)
			conjuntoPosicion.add(i);

		mejorCosto = buscarCostoVoraz(0, conjuntoPosicion, distancias);

		ArrayList<Integer> conjunto = new ArrayList<Integer>(
				distancias.getTam());
		for (int i = 0; i < distancias.getTam(); i++)
			conjunto.add(i);

		Nodo raiz = new Nodo(null, 0, distancias, conjunto, 0);
		atravesar(raiz);

		return mejorTour;
	}

	/**
	 * Método para obtener el costo actual
	 */
	public double getCost() {
		return mejorCosto;
	}

	/**
	 * Método que busca el costo voraz de un conjunto de posiciones
	 *
	 * @param i
	 *            Posición actual
	 * @param conjuntoPosicion
	 *            Conjunto de las posiciones restantes
	 * @param distancias
	 *            Matriz que contiene las distancias de los puntos
	 * @return El costo voraz
	 */
	private double buscarCostoVoraz(int i, HashSet<Integer> conjuntoPosicion,
			Matriz distancias) {
		if (conjuntoPosicion.isEmpty())
			return distancias.getElem(0, i);

		conjuntoPosicion.remove(i);

		double menor = Double.MAX_VALUE;
		int cercano = 0;
		for (int posicion : conjuntoPosicion) {
			double costo = distancias.getElem(i, posicion);
			if (costo < menor) {
				menor = costo;
				cercano = posicion;
			}
		}

		return menor + buscarCostoVoraz(cercano, conjuntoPosicion, distancias);
	}

	/**
	 * Método recursivo para ir a través del arbol buscando y podando
	 * trayectorias
	 */
	private void atravesar(Nodo padre) {
		Nodo[] hijos = padre.generarHijo();

		for (Nodo hijo : hijos) {
			if (hijo.isFinal()) {
				double costo = hijo.getCostoTour();
				if (costo < mejorCosto) {
					mejorCosto = costo;
					mejorTour = hijo.getTour();
				}
			} else if (hijo.getLimiteInferior() <= mejorCosto) {
				atravesar(hijo);
			}
		}
	}
}