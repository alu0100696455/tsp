/**
 * 
 * Clase para representar una matriz cuadrada
 * 
 * @author Jonathan Expósito Martín y Sergio Rodríguez Martín
 *
 */
public class Matriz {
	private int tam;
	private double[][] matriz;

	public Matriz(int tam) {
		setTam(tam);
		setMatriz(new double[getTam()][getTam()]);
	}

	public String toString() {
		String resultado = "";
		for (int i = 0; i < getTam(); i++) {
			for (int j = 0; j < getTam(); j++)
				resultado += getElem(i, j) + "\t";
			resultado += "\n";
		}
		return resultado;
	}

	/*
	 * Getters & Setters
	 */
	public int getTam() {
		return tam;
	}

	public void setTam(int tam) {
		this.tam = tam;
	}

	public double[][] getMatriz() {
		return matriz;
	}

	public void setMatriz(double[][] matriz) {
		this.matriz = matriz;
	}

	public double getElem(int i, int j) {
		return getMatriz()[i][j];
	}

	public void setElem(double valor, int i, int j) {
		getMatriz()[i][j] = valor;
	}
}
