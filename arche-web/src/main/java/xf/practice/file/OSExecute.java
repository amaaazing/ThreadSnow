package xf.practice.file;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class OSExecute {
    public static void command(String command){
        boolean err = false;
        try{
            Process process = new ProcessBuilder(command.split(" ")).start();
            BufferedReader results = new BufferedReader(
                    new InputStreamReader(process.getInputStream())
            );
            String s;
            while ((s = results.readLine())!= null)
                System.out.println(s);
            BufferedReader errs = new BufferedReader(
                    new InputStreamReader(process.getErrorStream())
            );
            while ((s =errs.readLine())!=null){
                System.out.println(s);
                err = true;
            }

        }catch (Exception e){
            throw new RuntimeException(e);
        }
        if(err){
            throw new RuntimeException("Errors executing " + command);
        }
    }

    public static void main(String [] args){
        OSExecute.command("ipconfig");
    }
}
