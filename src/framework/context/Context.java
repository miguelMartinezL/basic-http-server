package framework.context;

public class Context {
    public static void init(String[] packages) {
        for(String pack : packages){
            System.out.println(pack);
        }

    }
}
