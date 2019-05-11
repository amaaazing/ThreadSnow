package xf.practice.file;

import java.io.*;

public class BinaryFile {
    public static byte[] read(File file) throws IOException {
        BufferedInputStream bf = new BufferedInputStream(new FileInputStream(file));
        try {
            // bf.available()用来产生恰当的数组大小
            byte[] data = new byte[bf.available()];
            bf.read(data);
            return data;
        }finally {
            bf.close();
        }
    }

    public static byte[] read(String file) throws IOException{
        return read(new File(file).getAbsoluteFile());
    }

    public static void main(String [] args){

    }
}
