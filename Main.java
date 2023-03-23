package aed.collections;

//import java.util.Arrays;

public class Main {
    public static void main (String[] args) {

        // PERGUNTAR AO STOR PK É QUE SE EU CORRER O MAIN.JAVA NESTA DIRETORIA O GAJO NAO FUNCIONA
        //

        UnrolledLinkedList<Integer> test = new UnrolledLinkedList<>(4);
        // test.add("I'm ");
        // test.add("good ");
        // test.add("at ");
        // test.add("everything ");
        // test.add("except ");
        // test.add("the ");
        // test.add("things ");
        // test.add("I ");
        // test.add("can't ");
        // test.add("do");
        // test.add(1);
        // test.add(2);
        // test.addAt(2,3);
        // test.addAt(3,4);
        // test.addAt(4,5);
        // test.add(6);
        test.addAt(1,7);
        /*for (String l: test) {
            System.out.println(l);
        }*/
        
        // test.remove(); 
        // test.remove();
        // test.remove();
        // test.remove();
        // test.remove();
        // test.remove();
        // test.add(900);
        // System.out.println(test.get(3));
        // System.out.println(test.get(0));
        // test.addAt(8,10);

        // test.addAt(3,1);
        // test.addAt(3,1);
        // test.addAt(4,1);

        // IList<String> test1 = test.shallowCopy();
        //test.showListStats();
        //test.addAt(1,43); 

        /*Object[][] aob = test.getArrayOfBlocks();

        for(Object[] items : aob) 
            System.out.println(Arrays.toString(items));

        //test.getArrayOfBlocks();*/






        //test.PrintNodes();
        System.out.println("List size: " + test.size());
        System.out.println();

        
        // System.out.println();
    }
    
}
