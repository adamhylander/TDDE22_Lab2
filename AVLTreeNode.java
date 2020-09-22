package lab2;

public class AVLTreeNode<T extends Comparable<T>> {

	private int height;
	private T element;
	private AVLTreeNode<T> left, right;

	/**
	 * Constructor.
	 *
	 * @param data - Generic parameter containing the data element for the node.
	 */
	public AVLTreeNode(T data) {
		element = data;
	}

	/**
	 * Constructor.
	 *
	 * @param data - Generic parameter containing the data element for the node.
	 * @param l    - AVLTreeNode<T> containing the intended left child for the node
	 *             being created.
	 * @param r    - AVLTreeNode<T> containing the intended right child for the node
	 *             being created.
	 */
	public AVLTreeNode(T data, AVLTreeNode<T> l, AVLTreeNode<T> r) {
		element = data;
		left = l;
		right = r;
	}

	/**
	 * Print this node.
	 *
	 * @indent depth - The current indentation of the output.
	 */
	public void print(int depth) {
		if (right != null) {
			right.print(depth + 2);
//			System.out.println(" ".repeat(depth + 1) + "/");
		}

//		System.out.println(" ".repeat(depth) + element);

		if (left != null) {
//			System.out.println(" ".repeat(depth + 1) + "\\");
			left.print(depth + 2);
		}
	}

	/**
	 * Adjust the height for a given node n.
	 *
	 * @param n - AVLTreeNode for which to calculate the height.
	 */
	private void calculateHeight(AVLTreeNode<T> n) {
		n.height = 1 + Math.max(nodeHeight(n.left), nodeHeight(n.right));
	}

	/**
	 * Insert element into the tree originating from t. Currently does not balance
	 * the tree. TODO: Implement balancing.
	 *
	 * @param data - Generic element parameter to be inserted.
	 * @return AVLTreeNode containing the appropriate node for the call position,
	 *         after insertion.
	 */
	public AVLTreeNode<T> insert(T data) {
		if (data.compareTo(element) < 0) {
// the key we insert is smaller than the current node
			if (left == null) {
				left = new AVLTreeNode<>(data);
			} else {
				left = left.insert(data);
			}
		} else if (element.compareTo(data) < 0) {
// the key we insert is greater than the current node
			if (right == null) {
				right = new AVLTreeNode<>(data);
			} else {
				right = right.insert(data);
			}
		} else {
			throw new AVLTreeException("Element already exists.");
		}
		// Anropa calculateHeight på this för att kolla så att höjden stämmer efter vi kanske har ändrat runt i trädet.
		// Eftersom att den kommer börja längst ner i trädet så funkar metoden.
		calculateHeight(this);
		
		// Balance är för att kolla så att höjden är -1, 0 eller 1. Annars är det obalans någonstans i trädet.
		int balance = nodeHeight(left) - nodeHeight(right);
		System.out.println(element + "'s balance is " + balance);
		
		// Om det är obalans behöver vi göra en av fyra rotationer. I varje if-sats kollar vi om balansen är större än 1 eller mindre än -1
		// Den första vi kollar är doubleRotationWithRightChild. Här behöver vi veta om nyckeln vi stoppar in är större eller mindre än högerbarnets element.
		if (balance < -1 && data.compareTo(right.element) < 0) {
			System.out.println("double rotation with right child");
			return doubleRotationWithRightChild(this);
		}
		
		// Samma princip för doubleRotationWithLeftChild
		if (balance > 1 && data.compareTo(left.element) > 0) {
			System.out.println("double rotation with left child");
			return doubleRotationWithLeftChild(this);
		}

		// Om balansen är större än 1 och vi inte gjorde en doubleRotationWithRightChild innebär det att det måste vara en enkel rotation vi ska göra.
		if (balance > 1) {
			System.out.println("single rotation with left child");
			return singleRotationWithLeftChild(this);
		}
		
		// Samma princip som för singleRotationWithLeftChild
		if (balance < -1) {
			System.out.println("single rotation with right child");
			return singleRotationWithRightChild(this);
		}

		return this;
	}

	/**
	 * Returns the height of (sub) tree node.
	 *
	 * @param n - AVLTreeNode for which to get the height.
	 */
	private int nodeHeight(AVLTreeNode<T> n) {
		if (n != null) {
			return n.height;
		}
		return -1;
	}

	/**
	 * Search for a node containing the key. If found: return the node, otherwise
	 * return null.
	 *
	 * @param key - Generic key type for which to search.
	 * @return AVLTreeNode containing the key, if not found null be returned.
	 */
	public AVLTreeNode<T> find(T key) {
		if (key.compareTo(element) < 0) {
			if (left == null) {
				return null;
			}
			return left.find(key);
		} else if (element.compareTo(key) < 0) {
			if (right == null) {
				return null;
			}
			return right.find(key);
		} else {
			return this;
		}
	}

	/**
	 * Looks for the node with the smallest value in the tree (the leftmost node),
	 * and returns that node. If the tree is empty, null will be returned.
	 *
	 * @return AVLTreeNode with the smallest value in the tree, null if the tree is
	 *         empty.
	 */
	public AVLTreeNode<T> findMin() {
		if (left == null) {
			return this;
		} else {
			return left.findMin();
		}
	}

	/**
	 * Looks for the node with the largest value in the tree (the rightmost node),
	 * and returns that node. If the tree is empty, null will be returned.
	 *
	 * @return AVLTreeNode with the largest value in the tree, null if the tree is
	 *         empty.
	 */

	public AVLTreeNode<T> findMax() {
		if (right == null) {
			return this;
		} else {
			return right.findMax();
		}
	}

	/**
	 * Clear the given tree completely.
	 *
	 * @return null (to be used for clearing the root node.
	 */
	public AVLTreeNode<T> clear() {
		if (left != null) {
			left = left.clear();
		}
		if (right != null) {
			right = right.clear();
		}
		return null;
	}

	/**
	 * Removes the node key from the tree if it exists. Currently removes in a lazy
	 * fashion (does not balance the tree). TODO: Implement balancing.
	 *
	 * @param key - Generic parameter with the key to be deleted.
	 * @return - AVLTreeNode<T>, the correct node for the source position in the
	 *         tree.
	 */
	public AVLTreeNode<T> remove(T key) {
		if (key.compareTo(element) < 0) { // key is smaller than the current node
			if (left == null) {
// key not found in tree
				return this;
			}
			left = left.remove(key);
		} else if (element.compareTo(key) < 0) { // key is greater than the current node
			if (right == null) {
// key not found in tree
				return this;
			}
			right = right.remove(key);
		} else { // node found
			if (left != null && right != null) {
// The node has two children, so we replace it with the next node inorder.
				AVLTreeNode<T> tmp = right.findMin();
				element = tmp.element;
				right = right.remove(tmp.element);
			} else {
// The node has, at most, one child.
				if (left == null) {
					return right;
				} else {
					return left;
				}
			}
		}
		// Anropa calculateHeight på this för att kolla så att höjden stämmer efter vi kanske har ändrat runt i trädet.
		// Eftersom att den kommer börja längst ner i trädet så funkar metoden.
		calculateHeight(this);
		
		// Precis som med insert vill vi veta nodens balans. Alltså deklarerar vi int balance här.
		int balance = nodeHeight(left) - nodeHeight(right);

		// Först kollar vi om balansen är större än 2. Om den är det 
		if (balance > 1 && left.nodeHeight(left) - left.nodeHeight(right) < 0) {
			System.out.println("double rotation with left child");
			return doubleRotationWithLeftChild(this);
		}

		if (balance < -1 && right.nodeHeight(left) - right.nodeHeight(right) > 0) {
			System.out.println("double rotation with right child");
			return doubleRotationWithRightChild(this);
		}

		if (balance > 1) {
			System.out.println("single rotation with left child");
			return singleRotationWithLeftChild(this);
		}
		if (balance < -1) {
			System.out.println("single rotation with right child");
			return singleRotationWithRightChild(this);
		}
		return this;
	}

	/**
	 * Getter for element.
	 *
	 * @return the element in said node.
	 */
	public T getElement() {
		return element;
	}

	/**
	 * Getter for the left hand tree node.
	 *
	 * @return the left hand tree node.
	 */
	public AVLTreeNode<T> getLeft() {
		return left;
	}

	/**
	 * Getter for the right hand tree node.
	 *
	 * @return the right hand tree node.
	 */
	public AVLTreeNode<T> getRight() {
		return right;
	}

	/**
	 * Getter for height in the current node.
	 *
	 * @return the height for the current node.
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * Single rotation, left to right, using pivot as pivot.
	 *
	 * @param pivot - AVLTreeNode used as pivot for the rotation.
	 * @return AVLTreeNode with the new node for the pivot location in the tree.
	 */
	private AVLTreeNode<T> singleRotationWithLeftChild(AVLTreeNode<T> pivot) {
		AVLTreeNode<T> temp = pivot.left;

		pivot.left = temp.right;
		temp.right = pivot;

		calculateHeight(pivot);
		calculateHeight(temp);
		return temp;
	}

	/**
	 * Single rotation, right to left, using pivot as pivot.
	 *
	 * @param pivot - AVLTreeNode used as pivot for the rotation.
	 * @return AVLTreeNode with the new node for the pivot location in the tree.
	 */
	private AVLTreeNode<T> singleRotationWithRightChild(AVLTreeNode<T> pivot) {
		AVLTreeNode<T> temp = pivot.right;

		pivot.right = temp.left;
		temp.left = pivot;

		calculateHeight(pivot);
		calculateHeight(temp);
		return temp;
	}

	/**
	 * Double rotation, left to right, using pivot as pivot.
	 *
	 * @param pivot - AVLTreeNode used as pivot for the rotation.
	 * @return AVLTreeNode with the new node for the pivot location in the tree.
	 */
	private AVLTreeNode<T> doubleRotationWithLeftChild(AVLTreeNode<T> pivot) {
		pivot.left = singleRotationWithRightChild(pivot.left);
		return singleRotationWithLeftChild(pivot);
	}

	/**
	 * Double rotation, right to left, using pivot as pivot.
	 *
	 * @param pivot - AVLTreeNode used as pivot for the rotation.
	 * @return AVLTreeNode with the new node for the pivot location in the tre.
	 */
	private AVLTreeNode<T> doubleRotationWithRightChild(AVLTreeNode<T> pivot) {
		pivot.right = singleRotationWithLeftChild(pivot.right);
		return singleRotationWithRightChild(pivot);
	}

}
