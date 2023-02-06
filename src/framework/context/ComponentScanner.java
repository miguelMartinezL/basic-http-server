package framework.context;

import java.io.*;
import java.net.URL;
import java.util.*;

public class ComponentScanner {
    Map<Class, Object> context = new HashMap<>();

    public static List<Class> findFiles(File directory, String pack)
    {
        List<Class> classes = new ArrayList<>();
        File[] files = directory.listFiles();
        for (File file : files)
        {
            if(file.isDirectory())
            {
                classes.addAll(findFiles(file, pack + '.' + file.getName()));
            } else if(file.getName().endsWith(".class"))
            {
                try {
                    classes.add(Class.forName(pack + '.' + file.getName().replaceAll(".class", "")));
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return classes;
    }
    public static void scan(String[] packages) throws IOException
    {
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
        for(String pack : packages)
        {
            ClassLoader cLoader = ClassLoader.getSystemClassLoader();
            String path = pack.replace('.','/');
            List<Class> classes = new ArrayList<>();
            Enumeration<URL> resources = cLoader.getResources(path);
            List<File> fileAddres = new ArrayList<File>();

            while(resources.hasMoreElements())
            {
                URL url = resources.nextElement();
                fileAddres.add( new File(url.getFile()));
                System.out.println(url);
            }
            for(File addr : fileAddres){
                   classes.addAll(findFiles(addr,pack));
            }
            for (Class clase : classes){
                System.out.println(clase.getName());

            }
        }
    }
}
