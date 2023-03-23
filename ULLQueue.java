package aed.collections;

import java.util.Iterator;
import java.util.Arrays;
import java.util.Random;


public class ULLQueue<T> implements IQueue<T>{

    private class Node {
        private T[] items;
        private int itemCount;
        private Node next;
        private int toRemove;

        @SuppressWarnings("unchecked")
        public Node() {
            this.items = (T[]) new Object[blockSize];
            this.itemCount = 0;
            this.next = null;
            this.toRemove = 0;
        }
    }

    private Node first;
    private int size;
    private int blockSize;
    private Node last;

    public ULLQueue() {
        this.blockSize = 64;
        this.first = new Node();
        this.size = 0;
        this.last = first;
    }

    public ULLQueue(int blockSize) {
        this.blockSize = blockSize;
        this.first = new Node();
        this.size = 0;
        this.last = first;
    }
    
    @Override
    public boolean isEmpty() {
        return this.size == 0;
    }

    @Override
    public int size() {
        return this.size;
    }

    private class ULLQueueIterator implements Iterator<T> {
        Node it;
        private int i = 0;
        private int itinside;
        
        ULLQueueIterator () {
            it = first;
            itinside  = first.toRemove;
        }
        public boolean hasNext() {
            return i < size;
        }

        public T next() {
            T result = null;
            if (itinside != blockSize) {
                result = it.items[itinside++];
                i++;
            }
            else if (it.next != null && itinside == blockSize) {
                it = it.next;
                itinside = it.toRemove;
                result = it.items[itinside++];
                i++;
            }      
            return result;
        }
        public void remove(){
            throw new UnsupportedOperationException("It doesn't support removal");
        }   
    }
    
    @Override
    public Iterator<T> iterator() {
        return new ULLQueueIterator();
    }

    @Override
    public IQueue<T> shallowCopy() {
        ULLQueue<T> copyTest = new ULLQueue<>(this.blockSize);
        copyTest.size = this.size;
        Node n = first;
        Node m = copyTest.first;
        for (int i = first.toRemove; i < n.itemCount; i++) { // serve para ser copiado so o numero de items desde que ele teve um elemento eliminado ate ao numero de items total
            m.items[m.itemCount++] = n.items[i];
        }
        while (n.next != null) {
            n = n.next;
            Node novo = new Node();
            m.next = novo;
            m = novo;
            for (int i = 0; i < n.itemCount; i++) { // nao se mete i = first.toRemove pk eles nao sao eliminados a meio dos blocos
                m.items[m.itemCount++] = n.items[i];
            }
        }
        return copyTest;
    }

    @Override
    public void enqueue(T item) {
        if (last.itemCount < this.blockSize) {
            last.items[last.itemCount++] = item;
        }
        else {
            Node n = last;
            last = new Node();
            n.next = last;
            last.items[last.itemCount++] = item;
        }
        this.size++;
    }

    @Override
    public T dequeue() {
        T result = null;
        if (this.size > 0) {
            result = first.items[first.toRemove];
            first.items[first.toRemove++] = null;
    
            if (first.toRemove == this.blockSize && first.next != null) {
                first = first.next;
            }
            else if (first.toRemove == this.blockSize && first.next == null) {
                first.toRemove = 0;
            }
            this.size--;
        }
        return result;
    }

    @Override
    public T peek() {
        return this.first.items[first.toRemove];
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

    public static void main(String[] args) {
        ULLQueue<Integer> test = new ULLQueue<>(4);
        
        Random random = new Random();
        int bound = 1024000;
        // int number = random.nextInt(bound);
        double doublingRatio = 0;

        for (int i = 0; i <= 125; i++) {
            test.enqueue(i);
        }

        System.out.println("Testes a ULLQueue com muitos elementos:\n");

        System.out.println("Para o enqueue(T item):\n");
        var oldClock = new Stopwatch();
        for (int i = 1; i <= 125; i++) {
            test.enqueue(random.nextInt(i));
        }
        double oldTime = oldClock.elapsedTime();

        for (int i = 250; i <= bound; i*=2) {
            var clock = new Stopwatch();
            for (int j = 1; j <= i; j++) {
                test.enqueue(random.nextInt(i));
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
        if (Math.round((Math.log(doublingRatio) / Math.log(2))) < 0) {
            System.out.println("Valor da Razao Dobrada: " + 1);
        }
        else {
            System.out.println("Valor da Razao Dobrada: " + Math.round((Math.log(doublingRatio) / Math.log(2))));
        }

        System.out.println("Para o dequeue(int index):\n");

        double doublingRatio1 = 0;

        var oldClock1 = new Stopwatch();
        for (int i = 1; i <= 125; i++) {
            test.dequeue();
        }
        double oldTime1 = oldClock1.elapsedTime();

        for (int i = 250; i <= bound; i*=2) {
            var clock1 = new Stopwatch();
            for (int j = 1; j <= i; j++) {
                test.dequeue();
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

        if (Math.round((Math.log(doublingRatio1) / Math.log(2))) < 0) {
            System.out.println("Valor da Razao Dobrada: " + 1);
        }
        else {
            System.out.println("Valor da Razao Dobrada: " + Math.round((Math.log(doublingRatio1) / Math.log(2))));
        }
    }
}
