import java.util.ArrayList;

/**
 * A single node in the tree of paths
 */
public class Nodo {
	public Nodo parent;
	private double parent_cost;

	private Matriz costos;
	private ArrayList<Integer> active_set;

	public int index;

	/**
	 * Constructs a new Node
	 *
	 * @param parent This node's parent
	 * @param parent_cost The cost between these nodes
	 * @param distances The 2D array of distance between locations
	 * @param active_set The set of all points (including this node) that are being calculated
	 * @param index The location index of this node
	 */
	public Nodo(Nodo parent, double parent_cost, Matriz distances, ArrayList<Integer> active_set, int index) {
		this.parent = parent;
		this.parent_cost = parent_cost;
		this.costos = distances;
		this.active_set = active_set;
		this.index = index;
	}

	/**
	 * Check if this node is terminal
	 *
	 * @return Whether or not the node is terminal
	 */
	public boolean isTerminal() {
		return active_set.size() == 1;
	}

	/**
	 * Generate and return this node's children
	 *
	 * @precondition Node is not terminal
	 * @return Array of children
	 */
	public Nodo[] generateChildren() {
		Nodo[] children = new Nodo[active_set.size() - 1];
		ArrayList<Integer> new_set = new ArrayList<Integer>(active_set.size() - 1);
		for(int location : active_set) {
			if(location == index)
				continue;

			new_set.add(location);
		}

		for(int j = 0; j < children.length; j++)
			children[j] = new Nodo(this, costos.getElem(index, new_set.get(j)), costos, new_set, new_set.get(j));

		return children;
	}

	/**
	 * Get the path array up to this point
	 *
	 * @return The path
	 */
	public ArrayList<Integer> getPath() {
		int depth = costos.getTam() - active_set.size() + 1;
		ArrayList<Integer> path = new ArrayList<Integer>(depth);
		for(int i = 0; i < depth; i++)
			path.add(0);
		getPathIndex(path, depth - 1);
		return path;
	}

	/**
	 * Recursive method to fill in a path array from this point
	 *
	 * @param path The path array
	 * @param i The index of this node
	 */
	public void getPathIndex(ArrayList<Integer> path, int i) {
		path.set(i, index);
		if(parent != null)
			parent.getPathIndex(path, i - 1);
	}

	/**
	 * Get the lower bound cost of this node
	 *
	 * @return Lower bound cost
	 */
	public double getLowerBound() {
		double value = 0;

		if(active_set.size() == 2)
			return getPathCost() + costos.getElem(active_set.get(0), active_set.get(1));

		for(int location : active_set) {
			double low1 = Double.MAX_VALUE;
			double low2 = Double.MAX_VALUE;

			for(int other: active_set) {
				if(other == location)
					continue;

				double cost = costos.getElem(location, other);
				if(cost < low1) {
					low2 = low1;
					low1 = cost;
				}
				else if(cost < low2) {
					low2 = cost;
				}
			}

			value += low1 + low2;
		}

		return getParentCost() + value / 2;
	}

	/**
	 * Get the cost of the entire path up to this point
	 *
	 * @return Cost of path including return
	 */
	public double getPathCost() {
		return costos.getElem(0, index) + getParentCost();
	}

	/**
	 * Get the cost up to the parent at this point
	 *
	 * @return Cost of path
	 */
	public double getParentCost() {
		if(parent == null)
			return 0;

		return parent_cost + parent.getParentCost();
	}
}