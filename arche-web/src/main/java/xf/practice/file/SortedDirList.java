package xf.practice.file;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.regex.Pattern;

public class SortedDirList {

    private File filepath;
    public SortedDirList(){
        this(new File("."));
    }
    public SortedDirList(File filepath){
        this.filepath = filepath;
    }

    // 目录列表器
    public String[] dirList(){
        String[] list = filepath.list();
        Arrays.sort(list,String.CASE_INSENSITIVE_ORDER);
        return list;
    }

    // 目录过滤器
    public String[] dirList(final String regex){
        String[] list = filepath.list(new FilenameFilter() {
            private Pattern pattern = Pattern.compile(regex);
            public boolean accept(File dir, String name) {
                return pattern.matcher(name).matches();
            }
        });
        Arrays.sort(list,String.CASE_INSENSITIVE_ORDER);
        return  list;
    }
}
