/**
 * Represents a list of Nodes. 
 */
public class LinkedList {
	
	private Node first; // pointer to the first element of this list
	private Node last;  // pointer to the last element of this list
	private int size;   // number of elements in this list
	
	/**
	 * Constructs a new list.
	 */ 
	public LinkedList () {
		first = null;
		last = first;
		size = 0;
	}
	
	/**
	 * Gets the first node of the list
	 * @return The first node of the list.
	 */		
	public Node getFirst() {
		return this.first;
	}

	/**
	 * Gets the last node of the list
	 * @return The last node of the list.
	 */		
	public Node getLast() {
		return this.last;
	}
	
	/**
	 * Gets the current size of the list
	 * @return The size of the list.
	 */		
	public int getSize() {
		return this.size;
	}
	
	/**
	 * Gets the node located at the given index in this list. 
	 * 
	 * @param index
	 *        the index of the node to retrieve, between 0 and size
	 * @throws IllegalArgumentException
	 *         if index is negative or greater than the list's size
	 * @return the node at the given index
	 */		
	public Node getNode(int index) {
		if (index < 0 || index > size) {
			throw new IllegalArgumentException(
					"index must be between 0 and size");
		}
		Node cur = first;
		for(int i = 0; i < index; i++) {
			cur = cur.next;
		}
		return cur;
	}
	
	/**
	 * Creates a new Node object that points to the given memory block, 
	 * and inserts the node at the given index in this list.
	 * <p>
	 * If the given index is 0, the new node becomes the first node in this list.
	 * <p>
	 * If the given index equals the list's size, the new node becomes the last 
	 * node in this list.
     * <p>
	 * The method implementation is optimized, as follows: if the given 
	 * index is either 0 or the list's size, the addition time is O(1). 
	 * 
	 * @param block
	 *        the memory block to be inserted into the list
	 * @param index
	 *        the index before which the memory block should be inserted
	 * @throws IllegalArgumentException
	 *         if index is negative or greater than the list's size
	 */
	public void add(int index, MemoryBlock block) {
		if (index < 0 || index > size) {
			throw new IllegalArgumentException(
					"index must be between 0 and size");
		}
		Node newNode = new Node(block);
		if(index == 0) {
			newNode.next = first;
			first = newNode;
			if(size == 0) {
				last = newNode;
			}
		}
		else if(index == size) {
			last.next = newNode;
			last = newNode;
		}
		else {
			Node cur = first;
			int curIndex = 0;
			while(curIndex != index - 1) {
				cur = cur.next;
				curIndex++;
			}
			newNode.next = cur.next;
			cur.next = newNode;
		}
		size++;
	}

	/**
	 * Creates a new node that points to the given memory block, and adds it
	 * to the end of this list (the node will become the list's last element).
	 * 
	 * @param block
	 *        the given memory block
	 */
	public void addLast(MemoryBlock block) {
		Node newNode = new Node(block);
		if(first == null) {
			first = newNode;
			last = newNode;
		}
		else{
			last.next = newNode;
			last = newNode;
		}
		size++;
	}
	
	/**
	 * Creates a new node that points to the given memory block, and adds it 
	 * to the beginning of this list (the node will become the list's first element).
	 * 
	 * @param block
	 *        the given memory block
	 */
	public void addFirst(MemoryBlock block) {
		Node newNode = new Node(block);
		if(first == null) {
			first = newNode;
			last = newNode;
		}
		else{
			newNode.next = first;
			first = newNode;
		}
		size++;
	}

	/**
	 * Gets the memory block located at the given index in this list.
	 * 
	 * @param index
	 *        the index of the retrieved memory block
	 * @return the memory block at the given index
	 * @throws IllegalArgumentException
	 *         if index is negative or greater than or equal to size
	 */
	public MemoryBlock getBlock(int index) {
		if (index < 0 || index >= size) {
			throw new IllegalArgumentException("index must be between 0 and size");
		}

		Node cur = first;
		int curIndex = 0;
		while(curIndex != index) {
			cur = cur.next;
			curIndex++;
		}
		return cur.block;
	}	

	/**
	 * Gets the index of the node pointing to the given memory block.
	 * 
	 * @param block
	 *        the given memory block
	 * @return the index of the block, or -1 if the block is not in this list
	 */
	public int indexOf(MemoryBlock block) {
		Node cur = first;
		int curIndex = 0;
		while(cur != null) {
			if(cur.block.equals(block)) {
				return curIndex;
			}
			cur = cur.next;
			curIndex++;
		}
		return -1;
	}

	/**
	 * Removes the given node from this list.	
	 * 
	 * @param node
	 *        the node that will be removed from this list
	 */
	public void remove(Node node) {
		if (node == null) {
			throw new NullPointerException("Node cannot be null");
		}
		if (first == null) {
			throw new IllegalArgumentException("List is empty");
		}

		if(node == first) {
			first = first.next;
			if (first == null) { 
				last = null;
			}
			size--;
		}
		else{
			Node cur = first;
			while(cur != null && cur.next != node) {
				cur = cur.next;
			}
			if(cur != null && cur.next == node) {
				cur.next = node.next;
				if(node == last) {
					last = cur;
				}
				size--;
			}
		}
	}

	/**
	 * Removes from this list the node which is located at the given index.
	 * 
	 * @param index the location of the node that has to be removed.
	 * @throws IllegalArgumentException
	 *         if index is negative or greater than or equal to size
	 */
	public void remove(int index) {
		if (index < 0 || index >= size) {
			throw new IllegalArgumentException("index must be between 0 and size");
		}
		if (first == null) {
			throw new IllegalArgumentException("index must be between 0 and size");
		}

		if(index == 0) {
			first = first.next;
			if (first == null) {
				last = null;
			}
		}
		else {
			Node cur = first;
			int curIndex = 0;

			while(curIndex < index - 1) {
				cur = cur.next;
				curIndex++;
			}

			if(index == size - 1) {
				last = cur;
			}

			cur.next = cur.next.next;
		}
		size--;
	}

	/**
	 * Removes from this list the node pointing to the given memory block.
	 * 
	 * @param block the memory block that should be removed from the list
	 * @throws IllegalArgumentException
	 *         if the given memory block is not in this list
	 */
	public void remove(MemoryBlock block) {
		if (first == null) {
			throw new IllegalArgumentException("List is empty");
		}

		if(first.block.equals(block)) {
			first = first.next;
			if (first == null) {
				last = null;
			}
		}
		else {
			Node cur = first;
			while(cur != null && !cur.next.block.equals(block)) {
				cur = cur.next;
			}

			if (cur.next == null) { 
				throw new IllegalArgumentException("Block not found in the list");
			}

			if(cur.next == last) {
				last = cur;
			}

			cur.next = cur.next.next;
		}
		size--;
	}	

	/**
	 * Returns an iterator over this list, starting with the first element.
	 */
	public ListIterator iterator(){
		return new ListIterator(first);
	}
	
	/**
	 * A textual representation of this list, for debugging.
	 */
	public String toString() {
		if (first == null) {
			return "Empty list";
		}
		
		Node cur = first;
		String toString = "";
		while(cur != null) {
			toString = toString + " -> " + cur;
			cur = cur.next;
		}
		return toString;
	}
}