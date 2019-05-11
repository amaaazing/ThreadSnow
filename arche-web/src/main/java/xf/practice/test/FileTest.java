package xf.practice.test;

import edu.princeton.cs.algs4.StdOut;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.regex.Pattern;

/**
 * File类不仅仅只代表存在的文件或者目录，还可用来创建新的目录或尚不存在的整个目录路径
 *  File类代表,更适合的名称Filepath类
 *  1： 一个特定文件名称
 *  2：一个目录下的一组文件的名称,返回的是数组，因为元素的个数是固定的
 *
 */
public class FileTest {
    public static void main(String [] args){
        File path = new File("F:\\terry\\GitHub\\ThreadSnow\\arche-web\\src\\main\\java\\xf\\practice\\test");
        String[] list;
        if(args.length == 0){
            list = path.list(); // 目录列表器
        }else{
            list = path.list(new DirFilter(args[0])); // 目录过滤器
        }

        Arrays.sort(list,String.CASE_INSENSITIVE_ORDER);

        // The File.length( ) method reads the number of bytes in the file.
        long total = 0;
        long fs;
        for (String dirItem : list) {
            fs = new File(path, dirItem).length();
            StdOut.println(dirItem + ", " + fs + " byte(s)");
            total += fs;
        }
        StdOut.println("=======================");
        StdOut.println(list.length + " file(s), " + total + " bytes");

    }


}

// 还可以写成匿名内部类的形式
class DirFilter implements FilenameFilter {
    private Pattern pattern;
    public DirFilter(String regex){
        this.pattern = Pattern.compile(regex);
    }
    // 回调函数
    public boolean accept(File dir, String name) {
        return pattern.matcher(name).matches();
    }
}