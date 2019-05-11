package xf.practice.file;

import xf.utility.TextFile;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class DirList {
    public static void main(final String[] args) {
        File path = new File("F:\\terry\\GitHub\\ThreadSnow\\arche-web\\src\\main\\java\\xf\\practice\\test");
        String[] list;
        if (args.length == 0) list = path.list();
        else list = path.list(new FilenameFilter() {
            private String ext = args[0].toLowerCase();

            public boolean accept(File dir, String name) {
                // Only analyze source files with the specified
                //extension (given as the first command line argument)
                if (name.toLowerCase().endsWith(ext)) {
//                 Only filter upon file extension?
                    if (args.length == 1)
                        return true;
                    // 用正则表达式把整个文件的单词分隔开存进set中
                    Set<String> words = new HashSet<String>(new TextFile(
                            new File(dir, name).getAbsolutePath(), "\\W+"));
                    for (int i = 1; i < args.length; i++)
                        if (words.contains(args[i]))
                            return true;
                }
                return false;
            }
        });
        Arrays.sort(list, String.CASE_INSENSITIVE_ORDER);
        for (String dirItem : list)
            System.out.println(dirItem);
    }
}
