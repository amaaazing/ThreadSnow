package xf.jmockit;

public class SimpleTool {
	
	private  String  name = "千层雪";
	
    public String fun1(String str) {
        return "real: public String fun1(" + str + ")";
    }

    private String privateMethod(String str) {
        return "real: privateMethod";
    }

    public  String fun3(String str) {
        return "real: public String fun3(" + str + ")";
    }

    private static String privateStaticMthod(String str){
    	return "real: publicStaticMthod";
    }
    
    public String fun4(String str) {
        return privateStaticMthod(str);
    }
    
    public String fetchData(String name){
        System.out.println("call SimpleTool.fetchData");
        return this.name;
    }
}
