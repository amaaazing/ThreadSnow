package xf.practice.file;

import java.io.File;
import java.io.IOException;

public class ProcessFiles {
    /**
     * 根据Strategy对象来处理目录中的文件，策略设计模式
     */
    public interface Strategy{
        void process(File file);
    }
    private ProcessFiles.Strategy strategy;
    private String regex;

    public ProcessFiles(Strategy strategy ,String regex){
        this.strategy = strategy;
        this.regex = regex;
    }
    public void processDirectoryTree(File root) throws IOException {
        for(File file : Driectory.walk(root.getAbsolutePath(),regex)){
            strategy.process(file.getCanonicalFile());
        }
    }

    public void start(String[] args) {

        try {
            if (args.length == 0)
                processDirectoryTree(new File("."));
            else {
                for (String arg : args) {
                    File fileArg = new File(arg);
                    if (fileArg.isDirectory()) {
                        processDirectoryTree(fileArg);
                    } else {
                        if (arg.matches(regex))
                            strategy.process(new File(arg).getCanonicalFile());
                    }
                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String [] args){
        new ProcessFiles(new ProcessFiles.Strategy(){
            public void process(File file) {
                System.out.println(file);
            }
        },".*mkv").start(args);
    }
}
