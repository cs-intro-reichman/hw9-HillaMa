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
		if (freeList.getSize() == 0) {
			return -1;
		}

		Node freeListCur = freeList.getFirst();
		int freeListcurIndex = 0;
		int memAdd = 0;

		if(freeListCur.block.length == length) {
			memAdd = freeListCur.block.baseAddress;

			allocatedList.addLast(freeListCur.block);
			freeList.remove(freeListCur.block);

			return memAdd;
		}
		else if(freeListCur.block.length > length) {
			memAdd = freeListCur.block.baseAddress;

			MemoryBlock updatedAlloBlock = new MemoryBlock(freeListCur.block.baseAddress, length);
			allocatedList.addLast(updatedAlloBlock);

			MemoryBlock updatedFreeBlock = new MemoryBlock(freeListCur.block.baseAddress + length, freeListCur.block.length - length);
			freeList.add(freeListcurIndex, updatedFreeBlock);
			freeList.remove(freeListcurIndex + 1);

			return memAdd;
		}


		while(freeListCur.next != null && freeListCur.next.block.length < length) {
			freeListCur = freeListCur.next;
			freeListcurIndex++;
		}

		if(freeListCur.next == null) {
			return -1;
		}

		if(freeListCur.next.block.length == length) {
			memAdd = freeListCur.next.block.baseAddress;

			allocatedList.addLast(freeListCur.next.block);
			freeList.remove(freeListCur.next.block);

			return memAdd;
		}

		else {
			memAdd = freeListCur.next.block.baseAddress;

			MemoryBlock updatedAlloBlock = new MemoryBlock(freeListCur.next.block.baseAddress, length);
			allocatedList.addLast(updatedAlloBlock);

			MemoryBlock updatedFreeBlock = new MemoryBlock(freeListCur.next.block.baseAddress + length, freeListCur.next.block.length - length);
			freeList.add(freeListcurIndex + 1, updatedFreeBlock);
			freeList.remove(freeListcurIndex + 2);

			return memAdd;
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
		if(allocatedList.getFirst() == null) {
			throw new IllegalArgumentException("List is empty");
		}
		else {
			Node alloListCur = allocatedList.getFirst();

			if(alloListCur.block.baseAddress == address) {
				freeList.addLast(alloListCur.block);
				allocatedList.remove(alloListCur.block);
			}

			else {
				while(alloListCur.next != null && alloListCur.next.block.baseAddress != address) {
					alloListCur = alloListCur.next;
				}

				if(alloListCur.next != null) {
					freeList.addLast(alloListCur.next.block);
					allocatedList.remove(alloListCur.next);
				}
			}
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
		if(freeList.getFirst() == null) {
			throw new IllegalArgumentException("List is empty");
		}
		else {
			Node curCheked = freeList.getFirst();
			Node curRunning = curCheked.next;
			int checkedIndex = 0;
			int runningIndex = 1;

			for(;curCheked.next != null; curCheked = curCheked.next) {
				for(;curRunning != null; curRunning = curRunning.next) {
					if(curRunning.block.baseAddress == curCheked.block.baseAddress + curCheked.block.length) {
						freeList.add(checkedIndex, new MemoryBlock(curCheked.block.baseAddress, curCheked.block.length + curRunning.block.length));
						
						freeList.remove(runningIndex + 1);
                    	freeList.remove(checkedIndex + 1);

                    	curCheked = freeList.getFirst();
                    	for (int i = 0; i < checkedIndex; i++) {
                        	curCheked = curCheked.next;
                    	}
                    	curRunning = curCheked.next;
                    	runningIndex = checkedIndex + 1;
                	} 		
					else {
                    	curRunning = curRunning.next;
                    	runningIndex++;
                	}	
				}
				checkedIndex++;
				runningIndex = checkedIndex + 1;
				curRunning = curCheked.next.next;
			}
		}
	}
}
