package xf.practice.file;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * ByteBuffer 容纳的是普通的字节，也就是只能保存字节类型的数据
 * 在向ByteBuffer输入的时候，进行编码， java.nio.charset.Charset
 * 输出时进行解码
 * 这样就能实现转换成字符的功能
 */
public class GetChannel {
    private static final int BSIZE = 2048;
    private static final String FILE_PATH ="F:\\Amys\\data.txt";

    public static void main(String [] args) throws IOException {
        // 写
        FileChannel fc = new FileOutputStream(FILE_PATH).getChannel();
        fc.write(ByteBuffer.wrap("I love fanger.".getBytes()));
        fc.close();

        // 在文件的末尾追加
        fc = new RandomAccessFile(FILE_PATH,"rw").getChannel();
        fc.position(fc.size());// move to the end
        fc.write(ByteBuffer.wrap("3.1415926".getBytes()));
        fc.close();

        // 读
        fc = new FileInputStream(FILE_PATH).getChannel();
        ByteBuffer buff = ByteBuffer.allocate(BSIZE);
        fc.read(buff);
        buff.flip();// 让别人做好读取字节的准备
        while(buff.hasRemaining())
            System.out.println(buff.get());
    }
}
