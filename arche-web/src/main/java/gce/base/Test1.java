package gce.base;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;


public class Test1 {

	public static void main(String[] args) throws FileNotFoundException {
		Scanner sc = new Scanner(new File("tinyT.txt"));
        while (sc.hasNext()) {
            int key = sc.nextInt();          
                StdOut.println(key);
                StdOut.println("-----------");
        }
        sc.close();
	}
}
