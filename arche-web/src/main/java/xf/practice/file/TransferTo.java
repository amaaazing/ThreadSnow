package xf.practice.file;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

// 将一个通道和另一个相连，有复制作用
public class TransferTo {
    public static void main(String [] args) throws IOException{
        if(args.length != 2){
            System.out.println("arugments: sourcefile destfile");
            System.exit(1);
        }
        FileChannel in = new FileInputStream(args[0]).getChannel();
        FileChannel out = new FileOutputStream(args[1]).getChannel();
        in.transferTo(0,in.size(),out);
        // or  out.transferTo(in,0,in.size());
    }
}
