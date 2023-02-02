package framework.context;

import java.io.*;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class ComponentScanner {
    Map<Class, Object> context = new HashMap<>();
    public static void scan(String[] packages) throws IOException {
        // iterar por el sistema de archivos
        // cada vez que encuentre un archivo .java
        /* pasamos ese nombre de archivo al class loader
        despues de cargar la clase la vamos a inspeccionar usando reflection
        -anotaciones de controlador y de servicio
        usando reflection vemos si tiene alguna de estas anotaciones
         y dependiendo de eso tomar desicion
         1. un mapa (contexto) que la llave sea el objeto class y  el objeto como tal sea la instanci de esa clase
         Map< Class, Object>
         @Autowired busca la clase en el contexto de la aplicacion
        * */
        for(String pack : packages){
            String path = pack.replace('.','/');
            ClassLoader cLoader = Thread.currentThread().getContextClassLoader();
            Enumeration<URL> resources = cLoader.getResources(path);
            File f;
            while(resources.hasMoreElements()){
                URL url = resources.nextElement();
                System.out.println(url.toString());
                f = new File(url.getFile());
                File[] files = f.listFiles();
                for(File file : files){
                    if(file.getName().endsWith(".class"))
                    System.out.println(file.getName());
                }

            }
        }
    }
}
