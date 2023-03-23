package aed.collections;
import java.util.Random;


public class MinPriorityQueue<T extends Comparable<T>> {
    
    private UnrolledLinkedList<T> list;
    private static Random random = new Random();

    public MinPriorityQueue() {
    	this.list = new UnrolledLinkedList<T>(451);
    }

    public MinPriorityQueue(int n) {
    	this.list = new UnrolledLinkedList<T>(n);
    }

    public MinPriorityQueue<T> shallowCopy() {
        MinPriorityQueue<T> copy = new MinPriorityQueue<>();
        for (T l: list) {
            copy.insert(l);
        }
    	return copy;
    }

    public Object[] getElements() {
        Object[] array = new Object[this.list.size()];
        int i = 0;
        for (T l: list) {
            array[i++] = l;
        }
    	return array;
    }

    public int binarySearch (int low, int high, T element) {

        int min = 0;
        int middle = 0;
        int max = high; //max esta a apontar para o maior elemento da lista

        while (max+1 != min) {
            middle = (min+max)/2;
            int x = element.compareTo(this.list.get(middle));
            if (x > 0) {
                min = middle;
            }
            else if (x < 0) {
                max = middle;
            }
            else {
                return middle;
            }

            if (middle == min) {
                min++;
            }
        }
        if (element.compareTo(this.list.get(middle)) > 0) {
            middle++;
        }

        return middle;
    }   

    public void insert(T element) {
        int x;
        if (this.list.size() == 0) {
            x = 0;
        }
        else {
            x = binarySearch(0, this.size()-1, element);
        }
        this.list.addAt(x, element);    	
    }

    public T peekMin() { 
        if (this.list.size() != 0) {
            return this.list.get(0);
        }
    	return null;
    }

    public T removeMin() {
        if (this.list.size() != 0) {
            T result = this.list.get(0);
            this.list.remove(0);
            return result;
        }
    	return null;
    }

    public boolean isEmpty() {
    	return this.list.size() == 0;
    }

    public int size() {
    	return this.list.size();
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

    public static void main(String[] args)
    {
    	MinPriorityQueue<Integer> test = new MinPriorityQueue<>();
        int n = 128;
        int bound = 65536;

        System.out.println("Para o test.insert(T element):\n");

        double doublingRatio = 0;
        var clock = new Stopwatch();
        for (int i = 1; i <= n; i++) {
            test.insert(random.nextInt(i));
        }
        double oldTimer = clock.elapsedTime();

        for (int i = n; i <= bound; i*=2) {
            var clock1 = new Stopwatch();
            for (int j = 0; j < i; j++) {
                test.insert(5);
            }
            double newTimer = clock1.elapsedTime();

            if (oldTimer > 0) {
                doublingRatio = newTimer/oldTimer;
            }
            else {
                doublingRatio = 0;
            }
            oldTimer = newTimer;
            System.out.println("Elements: " + i + " " + "New Time: " + newTimer + " " + "Double Ratio: " + doublingRatio);
            System.out.println();
        }
        System.out.println("Valor da Razao Dobrada: " + Math.round((Math.log(doublingRatio) / Math.log(2))));

        System.out.println();

        System.out.println("Para o removeMin():\n");

        double doublingRatio1 = 0;
        var clock2 = new Stopwatch();
        for (int i = 1; i <= n; i++) {
            test.removeMin();
        }
        double oldTimer1 = clock2.elapsedTime();

        for (int i = n; i <= bound; i*=2) {
            var clock3 = new Stopwatch();
            for (int j = 0; j < i; j++) {
                test.removeMin();
            }
            double newTimer1 = clock3.elapsedTime();

            if (oldTimer1 > 0) {
                doublingRatio1 = newTimer1/oldTimer1;
            }
            else {
                doublingRatio1 = 0;
            }
            oldTimer1 = newTimer1;
            System.out.println("Elements: " + i + " " + "New Time: " + newTimer1 + " " + "Double Ratio: " + doublingRatio1);
            System.out.println();
        }
        System.out.println("Valor da Razao Dobrada: " + Math.round((Math.log(doublingRatio1) / Math.log(2))));

        
    }
}
