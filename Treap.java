package aed.tables;

import java.util.Random;
import java.util.LinkedList;
import java.util.Queue;

public class Treap<Key extends Comparable<Key>,Value> {

    //if you want to use a different organization than a set of nodes with pointers, you can do it, but you will have to change
    //the implementation of the getHeapArray method, and eventually the printing method (so that is prints a "node" in the same way)
    private class Node {
        private Key key;
        private Value value;

        private int priority;
        private int size;

        private Node left;
        private Node right;        

        private Node() {
            this.key = key;
            this.value = value;

            this.priority = random.nextInt();
            this.size = 0;

            this.left = null;
            this.right = null;            
        }

        public Node (Key key, Value value, int priority) {
            this.key = key;
            this.value = value;

            this.priority = priority;
            this.size = 0;

            this.left = null;
            this.right = null;
        }
        
        public String toString() {
        	return "[k:" + this.key + " v:" + this.value + " p:" + this.priority + " s:" + this.size + "]";
        }

        //if needed, you can add additional methods to the private class Node
    }

    Node root;
    private Random random;

    public Treap () {
        this.root = null;
        this.random = new Random();
    }

    public Treap (Random r) {
        this.root = null;
        this.random = r;
    }

    private int size(Node n){
        if(n==null) return 0;
        else return n.size;
    }

    public int size() {
        return size(this.root);
    }

    private Value get(Node n, Key k) {
        if(n == null) return null;
        int cmp = k.compareTo(n.key);
        if (cmp < 0) return get(n.left,k);
        else if(cmp > 0) return get(n.right,k);
        else return n.value;
    }

    public Value get(Key k) {
        return get(this.root, k);
    }

    public boolean containsKey(Key k)
    {
    	if (this.root == null) {
            return false;
        }
        Node n = this.root;
        while (n != null) {
            int compare = k.compareTo(n.key);
            if (compare < 0) {
                n = n.left;
            }
            else if (compare > 0) {
                n = n.right;
            }
            else {
                return true;
            }
        }
        return false;
    }

    private Node rotateLeft(Node n) {
        Node filho = n.right;
        Node neto = filho.left;
        filho.left = n;
        n.right = neto;
        filho.size = n.size;
        n.size = size(n.left) + size(n.right) + 1;
        return filho;
    }

    private Node rotateRight(Node n) {
        Node filho = n.left;
        Node neto = filho.right;
        filho.right = n;
        n.left = neto;
        filho.size = n.size;
        n.size = size(n.left) + size(n.right) + 1;
        return filho;
    }

    private Node put (Node n, Key key, Value value, int priority) {
        if (value == null) {
            delete(key);
            return this.root;
        }
        if (n == null) {
            Node novo = new Node(key, value, priority);
            novo.size++;
            return novo;
        }
        
        int compare = key.compareTo(n.key);
        if (compare < 0) {
            n.left = put (n.left, key, value, priority);
            if (n.left.priority > n.priority) {
                n = rotateRight(n);
            }
        }
        else if (compare > 0) {
            n.right = put (n.right, key, value, priority);
            if (n.right.priority > n.priority) {
                n = rotateLeft(n);
            }
        }
        else {
            n.value = value;
            n.priority = priority;
        }
        n.size = 1 + size(n.left) + size(n.right);
        return n;
    }
    
    public void put (Key key, Value value) {
        this.root = put(this.root, key, value, this.random.nextInt());
    }

    private Node delete (Node n, Key k) {
        if (n.left != null || n.right != null) {
            int cmp = k.compareTo(n.key);
            if (cmp < 0) {
                n.left = delete(n.left, k);
            }
            else if (cmp > 0) {
                n.right = delete(n.right, k);
            }
            else {
                n.priority = Integer.MIN_VALUE;
                if (n.left != null || n.right != null) {
                    if (n.left == null || (n.right != null && n.right.priority > n.left.priority)) {
                        n = rotateLeft(n);
                        n.left = delete(n.left, k);
                    }
                    else if (n.right == null || (n.left != null && n.left.priority > n.right.priority)) {
                        n = rotateRight(n);
                        n.right = delete(n.right, k);
                    }
                }
            }
            n.size = size(n.left) + size(n.right) + 1;
        }
        if (n.left == null && n.right == null && k.compareTo(n.key) == 0) {
            return null;
        }
        return n;
    }
    
    public void delete(Key k) {
        if (this.root == null) {
            return;
        }
        this.root = delete (this.root, k);        
    }
    
    // private Node maxprio (Node n, Key k) {
    //     if (n.key != k) {
    //         int cmp = k.compareTo(n.key);
    //         if (cmp < 0) {
    //             n.left = maxprio(n.left, k);
    //         }
    //         else if (cmp > 0) {
    //             n.right = maxprio(n.right, k);
    //         }
    //     }
    //     else {
    //         n.priority = Integer.MAX_VALUE;
    //     }
    //     return n; 
    // }

    @SuppressWarnings({"rawtypes", "unchecked"})
	public Treap[] split(Key k) {
        this.root = put(this.root, k, this.root.value, Integer.MAX_VALUE);
        Treap array[] = new Treap[2];
        Treap left = new Treap(random);
        Treap right = new Treap(random);
        left.root = root.left;
        right.root  = root.right;
        array[0]  = left;
        array[1] = right;
        return array;
    }

    public Key min() {
        if (this.root == null) {
            return null;
        }
        Node n = this.root;
        while (n.left != null) {
            n = n.left;
        }
        return n.key;
    }

    public Key max() {
        if (this.root == null) {
            return null;
        }
        Node n = this.root;
        while(n.right != null) {
            n = n.right;
        }
        return n.key;
    }
    
    public void deleteMin() {
        if (this.root == null) {
            return;
        }
        Node n = this.root;
        if (n.left != null) {
            Node novo = n.left;
            n.size--;
            while (novo.left != null) {
                novo.size--;
                n = n.left;
                novo = novo.left;
            }
            n.left = novo.right;
        }
        else if (n.left == null) {
            this.root.size--;
            this.root = n.right;
        }
    }
    
    public void deleteMax() {
        if (this.root == null) {
            return;
        }
        Node n = this.root;
        if (n.right != null) {
            Node novo = n.right;
            n.size--;
            while(novo.right != null) {
                novo.size--;
                n = n.right;
                novo = novo.right;
            }
            n.right = novo.left;
        }
        else if (n.right == null) {
            this.root.size--;
            this.root = n.left;
        }
    }

    private int rank (Node n, Key key) {
        int total = 0;
        while (n != null) {
            int compare = key.compareTo(n.key);
            if (compare < 0) {
                n = n.left;
            }
            else if (compare > 0) {
                if (n.left != null) {
                    total += (n.left.size+1); 
                }
                else {
                    total++;
                }
                n = n.right;
            }
            else {
                if (n.left != null) {
                    return total + n.left.size;
                }
                break;
            }
        }

        return total;
    }

    public int rank(Key k)
    {
        if (this.root == null) {
            return 0;
        }
        
        return rank(this.root,k);
    }

    private int size (Node n, Key min, Key max, int i) {
        if (n != null) {
            int compareMin = min.compareTo(n.key);
            int compareMax = max.compareTo(n.key);
            if (compareMin > 0) {
                i = size (n.right, min, max, i);
            }
            else if (compareMax < 0) {
                i = size (n.left, min, max, i);
            }
            else if (compareMin <= 0 && compareMax >= 0) {
                i = size(n.left, min, max, i);
                i++;
                i = size(n.right, min, max, i);
            }
        }
        return i;
    }

    public int size(Key min, Key max) {
        if (this.root == null || min.compareTo(max) > 0) {
            return 0;
        }
        int i = 0;
        return size(this.root, min, max, i);
    }

    // private boolean contains(Key hi) {
    //     return get(hi) != null;
    // }

    // public int size(Key lo, Key hi) {
    //     if (lo.compareTo(hi) > 0) return 0;
    //     if (contains(hi)) return rank(hi) - rank(lo) + 1;
    //     else              return rank(hi) - rank(lo);
    // }

    private Key select (Node n, int selec) {
        if (n.left != null) {
            if (selec < n.left.size) {
                return select(n.left, selec);
            }
            else if (selec > n.left.size) {
                if (n.right != null) {
                    return select(n.right, selec-1-n.left.size);
                }
                else {
                    return null;
                }
            }
            else {
                return n.key;
            }
        }
        else if (n.left == null && n.right != null) {
            if (selec != 0) {
                selec--;
                return select(n.right, selec);
            }
            else {
                return n.key;
            }
        }
        
        return n.key;
    }
    
    public Key select(int n) {
        if (this.root == null) {
            return null;
        }
        if (n >= 0 && n < this.root.size) {
            return select(this.root, n);
        }
        return null;
    }

    private Queue<Key> williteratekeys (Queue<Key> iterable, Node n) {
        if (n.left != null) {
            williteratekeys(iterable, n.left);
        }
        iterable.add(n.key);
        if (n.right != null) {
            williteratekeys(iterable, n.right);
        }
        return iterable;
    }
    
    public Iterable<Key> keys() {
        Queue<Key> iterable = new LinkedList<>();
        iterable = williteratekeys(iterable, this.root);
        return iterable;
    }

    private Queue<Value> williteratevalues(Queue<Value> iterable, Node n) {
        if (n.left != null) {
            williteratevalues(iterable, n.left);
        }
        iterable.add(n.value);
        if (n.right != null) {
            williteratevalues(iterable, n.right);
        }
        return iterable;
    }
    
    public Iterable<Value> values() {
        Queue<Value> iterable = new LinkedList<>();
        iterable = williteratevalues(iterable, this.root);
        return iterable;
    }

    private Queue<Integer> williteratepriorities(Queue<Integer> iterable, Node n) {
        if (n.left != null) {
            williteratepriorities(iterable, n.left);
        }
        iterable.add(n.priority);
        if (n.right != null) {
            williteratepriorities(iterable, n.right);
        }
        return iterable;
    }

    public Iterable<Integer> priorities() {
        Queue<Integer> iterable = new LinkedList<>();
        iterable = williteratepriorities(iterable, this.root);
        return iterable;
    }

    private Queue<Key> williterateminmaxkeys(Queue<Key> iterable, Node n, Key min, Key max) {
        if (n != null) {
            int compareMin = min.compareTo(n.key);
            int compareMax = max.compareTo(n.key);
            if (compareMin <= 0 && compareMax >= 0) {
                if (n.left != null) {
                    iterable = williterateminmaxkeys(iterable, n.left, min, max);
                }
                iterable.add(n.key);
                if (n.right != null) {
                    iterable = williterateminmaxkeys(iterable, n.right, min, max);
                }
            }
            else if ((compareMin > 0 || compareMax < 0)){
                if (n.left != null && min.compareTo(n.left.key) <= 0) {
                    iterable = williterateminmaxkeys(iterable, n.left, min, max);
                }
                iterable = williterateminmaxkeys(iterable, n.right, min, max);
            }
        }
        return iterable;
    }
    
    public Iterable<Key> keys(Key min, Key max) {
        Queue<Key> iterable = new LinkedList<>();
        iterable = williterateminmaxkeys(iterable, this.root, min, max);
        return iterable;
    }

    private Node copyofnodes(Node n) {
        Node novo = new Node (n.key, n.value, n.priority);
        novo.size = n.size;
        if (n.left != null) {
            novo.left = copyofnodes(n.left);
        }
        if (n.right != null) {
            novo.right = copyofnodes(n.right);
        }
        return novo;
    }
    
    public Treap<Key,Value> shallowCopy() {
        Treap<Key, Value> copy = new Treap<Key, Value>(random);
        if (this.root != null) {
            copy.root = copyofnodes(this.root);
        }
        return copy;
    }

    //helper method that uses the treap to build an array with a heap structure
    private void fillHeapArray(Node n, Object[] a, int position)
    {
    	if(n == null) return;

    	if(position < a.length)
    	{
    		a[position] = n;
        	fillHeapArray(n.left,a,2*position);
        	fillHeapArray(n.right,a,2*position+1);
    	}
    }

    //if you want to use a different organization that a set of nodes with pointers, you can do it, but you will have to change
    //this method to be consistent with your implementation
	private Object[] getHeapArray()
    {
    	//we only store the first 31 elements (position 0 is not used, so we need a size of 32), to print the first 5 rows of the treap
    	Object[] a = new Object[32];
    	fillHeapArray(this.root,a,1);
    	return a;
    }
    
    private void printCentered(String line)
    {
    	//assuming 120 characters width for a line
    	int padding = (120 - line.length())/2;
    	if(padding < 0) padding = 0;
    	String paddingString = "";
    	for(int i = 0; i < padding; i++)
    	{
    		paddingString +=" ";
    	}
    	
    	System.out.println(paddingString + line);
    }

    //this is used by some of the automatic tests in Mooshak. It is used to print the first 5 levels of a Treap.
    //feel free to use it for your own tests
    public void printTreapBeginning() {
        Object[] heap = getHeapArray();
        int size = size(this.root);
        int printedNodes = 0;
        String nodeToPrint;
        int i = 1;
        int nodes;
        String line;

        //only prints the first five levels
        for (int depth = 0; depth < 5; depth++) {
            //number of nodes to print at a particular depth
            nodes = (int) Math.pow(2, depth);
            line = "";
            for (int j = 0; j < nodes && i < heap.length; j++) {
                if (heap[i] != null) {
                    nodeToPrint = heap[i].toString();
                    printedNodes++;
                } else {
                    nodeToPrint = "[null]";
                }
                line += " " + nodeToPrint;
                i++;
            }

            printCentered(line);
            if (i >= heap.length || printedNodes >= size) break;
        }
    }
    
    public static class Stopwatch { 
        private final long start;
        public Stopwatch() {
            start = System.currentTimeMillis();
        } 
        public double elapsedTime() {
            long now = System.currentTimeMillis();
            return (now - start) / 1000.0;
        }
    }

    private void efficiencyput (Treap <Integer, Integer> treap, int bound) {
        double doublingRatio = 0;

        System.out.println("Teste de eficiência para o put(Key key, Value value):");
        var oldClock = new Stopwatch();
        for (int i = 1; i <= 125; i++) {
            treap.put(random.nextInt(), random.nextInt());
        }
        double oldTime = oldClock.elapsedTime();
        System.out.print("Elements: " + 125 + " " + "Old Time: " + oldTime + "\n");
        for (int i = 256; i <= bound; i*=2) {
            var clock = new Stopwatch();
            for (int j = 0; j < i; j++) {
                treap.put(random.nextInt(), random.nextInt());
            }
            double newTime = clock.elapsedTime();
            if (oldTime > 0) {
                doublingRatio = newTime/oldTime;
            }
            else {
                doublingRatio = 0;
            }
            oldTime = newTime;
            System.out.print("Elements: " + i + " " + "New Time: " + newTime + " " + "Double Ratio: " + doublingRatio);
            System.out.println();
        }
    }

    private void efficiencyget (Treap<Integer,Integer> treap, int bound) {
        double doublingRatio = 0;
        System.out.println("Teste de eficiência para o get(Key key):");
        var oldClock = new Stopwatch();
        for (int i = 1; i <= 125; i++) {
            treap.get(random.nextInt());
        }
        double oldTime = oldClock.elapsedTime();
        for (int i = 256; i <= bound; i*=2) {
            var clock = new Stopwatch();
            for (int j = 0; j < i; j++) {
                treap.get(random.nextInt());
            }
            double newTime = clock.elapsedTime();
            if (oldTime > 0) {
                doublingRatio = newTime/oldTime;
            }
            else {
                doublingRatio = 0;
            }
            oldTime = newTime;
            System.out.print("Elements: " + i + " " + "New Time: " + newTime + " " + "Double Ratio: " + doublingRatio);
            System.out.println();
        }
    }

    private void efficiencydelete (Treap<Integer,Integer> treap, int bound) {
        double doublingRatio = 0;
        System.out.println("Teste de eficiência para o delete(Key key):");
        var oldClock = new Stopwatch();
        for (int i = 1; i <= 125; i++) {
            treap.delete(random.nextInt());
        }
        double oldTime = oldClock.elapsedTime();
        for (int i = 256; i <= bound; i*=2) {
            var clock = new Stopwatch();
            for (int j = 0; j < i; j++) {
                treap.delete(random.nextInt());
            }
            double newTime = clock.elapsedTime();
            if (oldTime > 0) {
                doublingRatio = newTime/oldTime;
            }
            else {
                doublingRatio = 0;
            }
            oldTime = newTime;
            System.out.print("Elements: " + i + " " + "New Time: " + newTime + " " + "Double Ratio: " + doublingRatio);
            System.out.println();
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static void main (String []args) {

        Random r = new Random(46874);
        Treap<Integer,Integer> treap = new Treap<Integer,Integer>(r);
        int bound = 524288;
        treap.put(1,1);
        treap.put(2,1);
        treap.put(3,1);
        treap.printTreapBeginning();
        System.out.println();
        treap.delete(4);
        treap.delete(1);
        treap.printTreapBeginning();
        // treap.efficiencyput(treap, bound);
        // Teste de eficiência para o put(Key key, Value value) com valores aleatórios:
        // Elements: 125 Old Time: 0.002
        // Elements: 256 New Time: 0.001 Double Ratio: 0.5
        // Elements: 512 New Time: 0.001 Double Ratio: 1.0
        // Elements: 1024 New Time: 0.001 Double Ratio: 1.0
        // Elements: 2048 New Time: 0.002 Double Ratio: 2.0
        // Elements: 4096 New Time: 0.004 Double Ratio: 2.0
        // Elements: 8192 New Time: 0.008 Double Ratio: 2.0
        // Elements: 16384 New Time: 0.025 Double Ratio: 3.125
        // Elements: 32768 New Time: 0.033 Double Ratio: 1.32
        // Elements: 65536 New Time: 0.088 Double Ratio: 2.6666666666666665
        // Elements: 131072 New Time: 0.147 Double Ratio: 1.6704545454545454
        // Elements: 262144 New Time: 0.311 Double Ratio: 2.1156462585034013
        // Elements: 524288 New Time: 1.044 Double Ratio: 3.356913183279743
        // Elements: 1048576 New Time: 2.611 Double Ratio: 2.5009578544061304
        // Elements: 2097152 New Time: 5.757 Double Ratio: 2.2049023362696283
        // Elements: 4194304 New Time: 18.574 Double Ratio: 3.226333159631753
        // Elements: 8388608 New Time: 38.96 Double Ratio: 2.097555723053731
        // r ~ 2, b = 1, linear  

        // treap.efficiencyget(treap, bound);
        // Teste de eficiência para o get(Key key) com valores aleatórios:
        // Elements: 256 New Time: 0.0 Double Ratio: 0.0    
        // Elements: 512 New Time: 0.0 Double Ratio: 0.0    
        // Elements: 1024 New Time: 0.0 Double Ratio: 0.0   
        // Elements: 2048 New Time: 0.0 Double Ratio: 0.0   
        // Elements: 4096 New Time: 0.001 Double Ratio: 0.0 
        // Elements: 8192 New Time: 0.002 Double Ratio: 2.0 
        // Elements: 16384 New Time: 0.001 Double Ratio: 0.5
        // Elements: 32768 New Time: 0.002 Double Ratio: 2.0
        // Elements: 65536 New Time: 0.003 Double Ratio: 1.5
        // Elements: 131072 New Time: 0.005 Double Ratio: 1.6666666666666667
        // Elements: 262144 New Time: 0.003 Double Ratio: 0.6
        // Elements: 524288 New Time: 0.015 Double Ratio: 5.0
        // Elements: 1048576 New Time: 0.012 Double Ratio: 0.8
        // Elements: 2097152 New Time: 0.021 Double Ratio: 1.75
        // Elements: 4194304 New Time: 0.039 Double Ratio: 1.857142857142857
        // r ~ 2, b = 1, linear

        // treap.efficiencydelete(treap, bound);
        // Teste de eficiência para o delete(Key key) com valores aleatórios:
        // Elements: 256 New Time: 0.0 Double Ratio: 0.0
        // Elements: 512 New Time: 0.0 Double Ratio: 0.0
        // Elements: 1024 New Time: 0.0 Double Ratio: 0.0
        // Elements: 2048 New Time: 0.0 Double Ratio: 0.0
        // Elements: 4096 New Time: 0.001 Double Ratio: 0.0
        // Elements: 8192 New Time: 0.001 Double Ratio: 1.0
        // Elements: 16384 New Time: 0.001 Double Ratio: 1.0
        // Elements: 32768 New Time: 0.002 Double Ratio: 2.0
        // Elements: 65536 New Time: 0.002 Double Ratio: 1.0
        // Elements: 131072 New Time: 0.003 Double Ratio: 1.5
        // Elements: 262144 New Time: 0.007 Double Ratio: 2.3333333333333335
        // Elements: 524288 New Time: 0.008 Double Ratio: 1.1428571428571428
        // Elements: 1048576 New Time: 0.011 Double Ratio: 1.375
        // Elements: 2097152 New Time: 0.021 Double Ratio: 1.9090909090909094
        // Elements: 4194304 New Time: 0.04 Double Ratio: 1.9047619047619047
        // r ~ 2, b = 1, linear
    }
}
