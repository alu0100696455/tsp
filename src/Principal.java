public class Principal {
	public static void main(String[] args) {
		AlgoritmoTour algoritmo = new AlgoritmoTour("src/gr17.xml");
		System.out.println(algoritmo);
		algoritmo.computoCotaSuperior();
	}
}
