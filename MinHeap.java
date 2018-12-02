//
// MinHeap DataStructure
public class MinHeap{
    int capacity;
    int currentSize;
    DijkstraandPrim.HeapNode[] minHeap;
    int [] indices;


    public MinHeap(int capacity) {
        this.capacity = capacity;
        minHeap = new DijkstraandPrim.HeapNode[capacity + 1];
        indices = new int[capacity];
        minHeap[0] = new DijkstraandPrim.HeapNode();
        minHeap[0].distance = Integer.MIN_VALUE;
        minHeap[0].vertex=-1;
        currentSize = 0;
    }

    public void insert(DijkstraandPrim.HeapNode x) {
        currentSize++;
        int index = currentSize;
        minHeap[index] = x;
        indices[x.vertex] = index;
        upHeap(index);
    }

    public void upHeap(int pos) {
        int parentIndex = pos/2;
        int currentIndex = pos;
        while (currentIndex > 0 && minHeap[parentIndex].distance > minHeap[currentIndex].distance) {
            DijkstraandPrim.HeapNode currentNode = minHeap[currentIndex];
            DijkstraandPrim.HeapNode parentNode = minHeap[parentIndex];
            indices[currentNode.vertex] = parentIndex;
            indices[parentNode.vertex] = currentIndex;
            swap(currentIndex,parentIndex);
            currentIndex = parentIndex;
            parentIndex = parentIndex/2;
        }
    }

    public DijkstraandPrim.HeapNode extractMin() {
        DijkstraandPrim.HeapNode min = minHeap[1];
        DijkstraandPrim.HeapNode lastNode = minHeap[currentSize];

        indices[lastNode.vertex] = 1;
        minHeap[1] = lastNode;
        minHeap[currentSize] = null;
        downHeap(1);
        currentSize--;
        return min;
    }

    public void downHeap(int k) {
        int smallest = k;
        int leftChildIndex = 2 * k;
        int rightChildIndex = 2 * k+1;
        if (leftChildIndex < heapSize() && minHeap[smallest].distance > minHeap[leftChildIndex].distance) {
            smallest = leftChildIndex;
        }
        if (rightChildIndex < heapSize() && minHeap[smallest].distance > minHeap[rightChildIndex].distance) {
            smallest = rightChildIndex;
        }
        if (smallest != k) {
            DijkstraandPrim.HeapNode smallestNode = minHeap[smallest];
            DijkstraandPrim.HeapNode kNode = minHeap[k];
            indices[smallestNode.vertex] = k;
            indices[kNode.vertex] = smallest;
            swap(k, smallest);
            downHeap(smallest);
        }
    }

    public void swap(int a, int b) {
        DijkstraandPrim.HeapNode temp = minHeap[a];
        minHeap[a] = minHeap[b];
        minHeap[b] = temp;
    }

    public boolean isEmpty() {
        return currentSize == 0;
    }

    public int heapSize(){
        return currentSize;
    }
}