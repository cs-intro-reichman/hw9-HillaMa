/**
 * Represents a managed memory space. The memory space manages a list of allocated 
 * memory blocks, and a list free memory blocks. The methods "malloc" and "free" are 
 * used, respectively, for creating new blocks and recycling existing blocks.
 */
public class MemorySpace {
	
	// A list of the memory blocks that are presently allocated
	private LinkedList allocatedList;

	// A list of memory blocks that are presently free
	private LinkedList freeList;

	/**
	 * Constructs a new managed memory space of a given maximal size.
	 * 
	 * @param maxSize
	 *            the size of the memory space to be managed
	 */
	public MemorySpace(int maxSize) {
		// initiallizes an empty list of allocated blocks.
		allocatedList = new LinkedList();
	    // Initializes a free list containing a single block which represents
	    // the entire memory. The base address of this single initial block is
	    // zero, and its length is the given memory size.
		freeList = new LinkedList();
		freeList.addLast(new MemoryBlock(0, maxSize));
	}

	/**
	 * Allocates a memory block of a requested length (in words). Returns the
	 * base address of the allocated block, or -1 if unable to allocate.
	 * 
	 * This implementation scans the freeList, looking for the first free memory block 
	 * whose length equals at least the given length. If such a block is found, the method 
	 * performs the following operations:
	 * 
	 * (1) A new memory block is constructed. The base address of the new block is set to
	 * the base address of the found free block. The length of the new block is set to the value 
	 * of the method's length parameter.
	 * 
	 * (2) The new memory block is appended to the end of the allocatedList.
	 * 
	 * (3) The base address and the length of the found free block are updated, to reflect the allocation.
	 * For example, suppose that the requested block length is 17, and suppose that the base
	 * address and length of the the found free block are 250 and 20, respectively.
	 * In such a case, the base address and length of of the allocated block
	 * are set to 250 and 17, respectively, and the base address and length
	 * of the found free block are set to 267 and 3, respectively.
	 * 
	 * (4) The new memory block is returned.
	 * 
	 * If the length of the found block is exactly the same as the requested length, 
	 * then the found block is removed from the freeList and appended to the allocatedList.
	 * 
	 * @param length
	 *        the length (in words) of the memory block that has to be allocated
	 * @return the base address of the allocated block, or -1 if unable to allocate
	 */
	public int malloc(int length) {		
		Node checkNodeFreeList = freeList.getFirst();
		Node allocatedNode = null;

		while (checkNodeFreeList != null){
			if (checkNodeFreeList.block.length >= length) {
				allocatedNode = checkNodeFreeList;
				checkNodeFreeList = null;
			}
			else {
				checkNodeFreeList = checkNodeFreeList.next;
			}
		}

		if(allocatedNode == null){
			return -1;
		}
		else if (allocatedNode.block.length > length){
			MemoryBlock newMemory = new MemoryBlock(allocatedNode.block.baseAddress, length);
			allocatedList.addLast(newMemory);

			allocatedNode.block.length = allocatedNode.block.length - length;
			allocatedNode.block.baseAddress = allocatedNode.block.baseAddress + length;

			return newMemory.baseAddress;
		}
		else {
			freeList.remove(allocatedNode);
			allocatedList.addLast(allocatedNode.block);

			return allocatedNode.block.baseAddress;
		}
	}

	/**
	 * Frees the memory block whose base address equals the given address.
	 * This implementation deletes the block whose base address equals the given 
	 * address from the allocatedList, and adds it at the end of the free list. 
	 * 
	 * @param baseAddress
	 *            the starting address of the block to freeList
	 */
	public void free(int address) {
		if (allocatedList.getSize() == 0) {
			throw new IllegalArgumentException(
					"index must be between 0 and size");
		}

		Node checkNodeAllocated = allocatedList.getFirst();
		Node newNode = null;

		while (checkNodeAllocated != null){
			if (checkNodeAllocated.block.baseAddress == address){
				newNode = checkNodeAllocated;
				checkNodeAllocated = null;
			}
			else{
				checkNodeAllocated = checkNodeAllocated.next;
			}
		}

		if (newNode != null){
			allocatedList.remove(newNode);
			freeList.addLast(newNode.block);
		}
	}
	
	/**
	 * A textual representation of the free list and the allocated list of this memory space, 
	 * for debugging purposes.
	 */
	public String toString() {
		return freeList.toString() + "\n" + allocatedList.toString();		
	}
	
	/**
	 * Performs defragmantation of this memory space.
	 * Normally, called by malloc, when it fails to find a memory block of the requested size.
	 * In this implementation Malloc does not call defrag.
	 */
	public void defrag() {
		Node checkNode = freeList.getFirst();
		Node selectedNode1 = null;
		Node selectedNode2 = null;

		while (checkNode != null){
			Node runingNode = checkNode.next;
			while (runingNode != null){
				if (checkNode.block.baseAddress + checkNode.block.length == runingNode.block.baseAddress){
					
					freeList.remove(runingNode);
					checkNode.block.length = checkNode.block.length + runingNode.block.length;

					runingNode = checkNode;
				}
				else {
					runingNode = runingNode.next;
				}
			}
			if (selectedNode1 == null && selectedNode2 == null){
				checkNode = checkNode.next;
			}

		}

	}
}