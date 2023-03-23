package aed.collections;    
import java.util.Arrays;
import java.util.Iterator;
import java.util.Random;

public class UnrolledLinkedList<T> implements IList<T>{

    private class Node {
        private T[] items;
        private int itemCount;
        private Node next;
        private Node previous;

        @SuppressWarnings("unchecked")
        public Node() {
            this.items = (T[]) new Object[blockSize];
            this.itemCount = 0;
            this.next = null;
            this.previous = null;
        }
    }

    private Node first;
    private int size;
    private int blockSize;
    private Node last;
    private int half;

    public UnrolledLinkedList() {
        this.blockSize = 32;
        this.first = new Node();
        this.size = 0;
        this.last = first;
        this.half = this.blockSize/2;
    }

    public UnrolledLinkedList(int blockSize) {
        this.blockSize = blockSize;
        this.first = new Node();
        this.size = 0;
        this.last = first;
        this.half = this.blockSize/2;
    }

    private class UnrolledLinkedListIterator implements Iterator<T> {
        Node it;
        private int i = 0;
        private int itinside = 0;

        UnrolledLinkedListIterator () {
            it = first;
        }

        public boolean hasNext() {
            return i < size;
        }

        public T next() {
            T result = null;
            
            if (itinside < it.itemCount) {
                result = it.items[itinside++];
                i++;
            }
            else {
                if (it.next != null) {
                    itinside -= it.itemCount;
                    it = it.next;
                    result = it.items[itinside++];
                    i++;
                }
            }            
            return result;
        }
        public void remove(){
            throw new UnsupportedOperationException("It doesn't support removal");
        }   
    }

    @Override
    public Iterator<T> iterator() {
        return new UnrolledLinkedListIterator();
    }

    @Override
    public UnrolledLinkedList<T> shallowCopy() {
        UnrolledLinkedList<T> copyTest = new UnrolledLinkedList<>(this.blockSize);
        copyTest.size = this.size;
        Node n = first;
        Node m = copyTest.first;
        for (int i = 0; i < n.itemCount; i++) {
            m.items[m.itemCount++] = n.items[i];
        }
        while (n.next != null) {
            n = n.next;
            Node novo = new Node();
            m.next = novo;
            novo.previous = m;
            m = novo;
            for (int i = 0; i < n.itemCount; i++) {
                m.items[m.itemCount++] = n.items[i];
            }
        }
        return copyTest;
    }

    public void add(T item) {
        if (last.itemCount < this.blockSize) {
            last.items[last.itemCount++] = item;
            this.size++;
            return;
        }
        else {
            Node n = last;
            last = new Node();
            n.next = last;
            last.previous = n;
            for (int i = this.half; i < blockSize; i++) {
                last.items[last.itemCount++] = n.items[i];
                n.items[i] = null;
            }
            n.itemCount = this.half;
            last.items[last.itemCount++] = item;
            this.size++;
        }   
    }

    
    public boolean isEmpty() {
        return this.size == 0;
    }

    public int size() {
        return this.size;
    }

    public T remove() {
        if (size != 0) {
            Node n = last;
            T result = n.items[n.itemCount-1];
            n.items[--n.itemCount] = null;
            this.size--;
            if (n.itemCount == 0 && n.previous != null) {
                this.last = n.previous;
                n = n.previous;
                n.next = null;
            }
            return result;
        }
        return null;
    }

    public T get (int index) {
        if(index >= size || index < 0) {
            return null;
        }
        Node n = first;
        while (index >= n.itemCount) {
            index -= n.itemCount;
            n = n.next;
        }
        return n.items[index];
    }

    public void set (int index, T item) {
        if (index < size) {
            Node n = first;
            while (index >= n.itemCount) {
                index -= n.itemCount;
                n = n.next;
            }
            n.items[index] = item;
        }
    }

    public T remove (int index) {
        
        if (index < size && index >= 0 && size != 0) {
            Node n = first;
            while (index >= n.itemCount) {
                index -= n.itemCount;
                n = n.next;
            }
            T result = n.items[index];
            n.items[index] = null;
            n.itemCount--;
            this.size--;

            //Shifting elements
            for (int i = index; i < blockSize-1; i++) {
                n.items[i] = n.items[i+1];
            }
            n.items[blockSize-1] = null;

            //first node
            if (n.itemCount == 0 && n.previous == null && n.next != null) {
                this.first = this.first.next;
                //serve para cortar ligações com o antigo first
                this.first.previous = null;
            }
            //middle nodes
            else if (n.itemCount == 0 && n.previous != n.next) {
                n.previous.next = n.next;
                n.next.previous = n.previous;
            }
            //last nodes
            else if (n.itemCount == 0 && n.previous != null) {
                n.previous.next = null;
            }

            return result;
        }
        return null;
    }

    public void PrintNodes() {
        
        Node n = this.first;
        
        if (size != 0) {
            while (n != null) {
                System.out.print("[");
                System.out.print(n.items[0]);
                for (int j = 1; j < this.blockSize; j++) {
                    System.out.print(",");
                    System.out.print(n.items[j]);
                }
                System.out.print("]");
                System.out.print(" - " + n.itemCount + " - ");
                n = n.next;
            }
        }
    }

    @SuppressWarnings("unchecked")
    public T[][] getArrayOfBlocks() {
        if (size != 0) {
            int result = 0;
            Node n = first;
            while (n.next != null) {
                result++;
                n = n.next;
            }
            result++;
            T[][] matrix = (T[][]) new Object[result][this.blockSize];
            
            int i = 0;
            n = first;
            while (n.next != null) {
                matrix[i++] = n.items;
                n = n.next;
            }
            matrix[i] = n.items;

            return matrix;
        }
        return null;
    }

    public void addAt (int index, T item) {
        Node n = this.first;

        // 1º e 2º caso feitos
        if (index == size) {
            add(item);
        }
        //3º caso feito
        else if (index < size && size > 0) {
            while (index >= n.itemCount) {
                index -= n.itemCount;
                n = n.next;
            }
            if (n.itemCount < blockSize) {
                for(int i = n.itemCount-1; i >= index; i--) {
                    n.items[i+1] = n.items[i];
                }
                n.items[index] = item;
                n.itemCount++;
                this.size++;
            }
            else if (n.itemCount >= blockSize){
                Node novo = new Node();
                if (n.next == null) {
                    n.next = novo;
                    novo.previous = n;
                    last = novo;
                }
                else {
                    novo.next = n.next;
                    n.next.previous = novo;
                    n.next = novo;
                    novo.previous = n;
                }

                //copia os elementos para o novo array
                for (int i = this.half; i < this.blockSize; i++) {
                    novo.items[novo.itemCount++] = n.items[i];
                    n.items[i] = null;
                }
                n.itemCount = half;

                if (index >= this.half) {
                    //shift
                    for (int i = this.half-1; i >= index-half; i--) {
                        novo.items[i+1] = novo.items[i];
                    }
                    novo.items[index-half] = item;
                    novo.itemCount++;
                }
                else {
                    //shift de elementos
                    for (int i = this.half-1; i >= index; i--) {
                        n.items[i+1] = n.items[i];
                    }
                    n.items[index] = item;
                    n.itemCount++;
                }
                this.size++;
            }
        }
        else if (index > size){
            return;
        }
    }

    public void showListStats() {
        Node n = first;
        int z = 0;
        System.out.println("Node #" + z + " stats:");
        System.out.println("Items: " + Arrays.toString(n.items));
        System.out.println("Items count: " + n.itemCount);
        System.out.println();
        
        while(n.next != null) {
            n = n.next;
            z++;
            System.out.println("Node #" + z + " stats:");
            System.out.println("Items: " + Arrays.toString(n.items));
            System.out.println("Items count: " + n.itemCount);
            System.out.println();
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

    public static void main (String[] args) {
        
        UnrolledLinkedList<Integer> test = new UnrolledLinkedList<>(4);

        Random random = new Random();
        int bound = 64000;
        // int number = random.nextInt(bound);
        double doublingRatio = 0;

        for (int i = 0; i <= 125; i++) {
            test.add(i);
        }

        System.out.println("Testes a UnrolledLinkedList com muitos elementos:\n");

        System.out.println("Para o addAt(int index, T item):\n");
        var oldClock = new Stopwatch();
        for (int i = 1; i <= 125; i++) {
            test.addAt(random.nextInt(i), random.nextInt(i));
        }
        double oldTime = oldClock.elapsedTime();

        for (int i = 250; i <= bound; i*=2) {
            var clock = new Stopwatch();
            for (int j = 1; j <= i; j++) {
                test.addAt(random.nextInt(i), random.nextInt(i));
            }
            double newTime = clock.elapsedTime();
            if (oldTime > 0) {
                doublingRatio = newTime/oldTime;
            }
            else {
                doublingRatio = 0;
            }
            oldTime = newTime;
            System.out.println("Elements: " + i + " " + "New Time: " + newTime + " " + "Double Ratio: " + doublingRatio);
            System.out.println();
        }
        System.out.println("Valor da Razao Dobrada: " + Math.round((Math.log(doublingRatio) / Math.log(2))));

        System.out.println("Para o get(int index):\n");

        double doublingRatio1 = 0;

        var oldClock1 = new Stopwatch();
        for (int i = 1; i <= 125; i++) {
            test.get(random.nextInt(i));
        }
        double oldTime1 = oldClock1.elapsedTime();

        for (int i = 250; i <= bound; i*=2) {
            var clock1 = new Stopwatch();
            for (int j = 1; j <= i; j++) {
                test.get(random.nextInt(i));
            }
            double newTime1 = clock1.elapsedTime();
            if (oldTime1 > 0) {
                doublingRatio1 = newTime1/oldTime1;
            }
            else {
                doublingRatio1 = 0;
            }
            oldTime1 = newTime1;
            System.out.println("Elements: " + i + " " + "New Time: " + newTime1 + " " + "Double Ratio: " + doublingRatio1);
            System.out.println();
        }

        System.out.println("Valor da Razao Dobrada: " + Math.round((Math.log(doublingRatio1) / Math.log(2))));

    }
}