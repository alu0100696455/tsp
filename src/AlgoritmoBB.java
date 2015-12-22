import java.util.ArrayList;

/**
 * 
 * Clase que implementa el algoritmo Branch and Bound
 * 
 * @author Jonathan Expósito Martín y Sergio Rodríguez Martín
 *
 */
public class AlgoritmoBB {
	Matriz distances;
	int sourceCity;
	String result = new String();

	ArrayList<Integer> initialRoute, optimumRoute;
	int nodes = 0;
	double routeCost = 0.0;
	double optimumCost = Double.MAX_VALUE;

	/** Creates a new instance of BranchAndBound */
	public AlgoritmoBB(Matriz matrix, int sourceCity) {

		distances = matrix;
		this.sourceCity = sourceCity;
	}

	/**
	 * executes the algorithm
	 */
	public String execute() {

		initialRoute = new ArrayList<Integer>();
		initialRoute.add(sourceCity);
		optimumRoute = new ArrayList<Integer>();
		nodes++;

		result = "BRANCH AND BOUND SEARCH\n\n";

		long startTime = System.currentTimeMillis();
		search(sourceCity, initialRoute);
		long endTime = System.currentTimeMillis();

		result += "\nBetter solution: " + optimumRoute.toString() + "// Cost: "
				+ optimumCost + "\n";
		result += "Visited Nodes: " + nodes + "\n";
		result += "Elapsed Time: " + (endTime - startTime) + " ms\n";

		return result;
	}

	/**
	 * @param from
	 *            node where we start the search.
	 * @param route
	 *            followed route for arriving to node "from".
	 */
	public void search(int from, ArrayList<Integer> followedRoute) {
		System.out.println(optimumCost);
		// we've found a new solution
		if (followedRoute.size() == distances.getTam()) {
			followedRoute.add(sourceCity);
			nodes++;

			// update the route's cost
			routeCost += distances.getMatriz()[from][sourceCity];

			if (routeCost < optimumCost) {
				optimumCost = routeCost;
				optimumRoute = new ArrayList<Integer>(followedRoute);
			}

			result += followedRoute.toString() + "// Cost: " + routeCost + "\n";

			// update the route's cost (back to the previous value)
			routeCost -= distances.getMatriz()[from][sourceCity];
		} else {
			for (int to = 0; to < distances.getTam(); to++) {
				if (!followedRoute.contains(to)) {

					// update the route's cost
					routeCost += distances.getMatriz()[from][to];

					if (routeCost < optimumCost) {
						ArrayList<Integer> increasedRoute = new ArrayList<Integer>(followedRoute);
						increasedRoute.add(to);
						nodes++;
						search(to, increasedRoute);
					}

					// update the route's cost (back to the previous value)
					routeCost -= distances.getMatriz()[from][to];
				}
			}
		}
	}
}
