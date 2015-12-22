import java.util.ArrayList;
import java.util.HashSet;

/**
 * Solves the traveling salesman problem using Branch and Bound by utilizing Node's
 */
public class AlgoritmoBB {
	Matriz distances;
	double best_cost;
	ArrayList<Integer> best_path;

	/**
	 * Constructs a new Solver and initializes distances array
	 *
	 * @param cities An ArrayList of City's
	 */
	public AlgoritmoBB(Matriz distances) {
		this.distances = distances;
	}

	/**
	 * Calculates the shortest (non-repeating) path between a series of nodes
	 *
	 * @return An array with the locations of the best path
	 */
	public ArrayList<Integer> calculate() {
		HashSet<Integer> location_set = new HashSet<Integer>(distances.getTam());
		for(int i = 0; i < distances.getTam(); i++)
			location_set.add(i);

		best_cost = findGreedyCost(0, location_set, distances);

		ArrayList<Integer> active_set = new ArrayList<Integer>(distances.getTam());
		for(int i = 0; i < distances.getTam(); i++)
			active_set.add(i);

		Nodo root = new Nodo(null, 0, distances, active_set, 0);
		traverse(root);

		return best_path;
	}

	/**
	 * Get current path cost
	 *
	 * @return The cost
	 */
	public double getCost() {
		return best_cost;
	}

	/**
	 * Find the greedy cost for a set of locations
	 *
	 * @param i The current location
	 * @param location_set Set of all remaining locations
	 * @param distances The 2D array containing point distances
	 * @return The greedy cost
	 */
	private double findGreedyCost(int i, HashSet<Integer> location_set, Matriz distances) {
		if(location_set.isEmpty())
			return distances.getElem(0, i);

		location_set.remove(i);

		double lowest = Double.MAX_VALUE;
		int closest = 0;
		for(int location : location_set) {
			double cost = distances.getElem(i, location);
			if(cost < lowest) {
				lowest = cost;
				closest = location;
			}
		}

		return lowest + findGreedyCost(closest, location_set, distances);
	}

	/**
	 * Recursive method to go through the tree finding and pruning paths
	 *
	 * @param parent The root/parent node
	 */
	private void traverse(Nodo parent) {
		Nodo[] children = parent.generateChildren();

		for(Nodo child : children) {
			if(child.isTerminal()) {
				double cost = child.getPathCost();
				if(cost < best_cost) {
					best_cost = cost;
					best_path = child.getPath();
				}
			}
			else if(child.getLowerBound() <= best_cost) {
				traverse(child);
			}
		}
	}
}