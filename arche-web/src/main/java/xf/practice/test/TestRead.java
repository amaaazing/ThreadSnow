package xf.practice.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

public class TestRead {

	public static void main(String[] args) {

		readFileByLines("D:/terry/Other/zings/tldy.txt");
	}
	
    /**
     * 以行为单位读取文件，常用于读面向行的格式化文件
     */
    public static void readFileByLines(String fileName) {
        File file = new File(fileName);
        
        BufferedReader reader = null;
        
        try {
            System.out.println("以行为单位读取文件内容，一次读一整行：");
            
            InputStreamReader read = new InputStreamReader(new FileInputStream(file),"GBK");
            reader= new BufferedReader(read);
            
            String tempString = null;
            int line = 1;                    
            
            Scanner scan = new Scanner(System.in);
            
            // 一次读入一行，直到读入null为文件结束
            while(true){
                while ((tempString = reader.readLine()) != null && "x".equals(scan.nextLine())) {
                    // 显示行号
                    System.out.println("line " + line + ": " + tempString);
                    line++;
                }
                System.out.println("请重新输入：");                
            }    
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
    }
}
