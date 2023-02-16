package framework.context;

import application.NewController;
import application.ProductService;
import framework.MessageLogger;
import framework.RequestMethod;
import framework.annotation.*;

import java.io.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.net.URL;
import java.util.*;

public class ComponentScanner {
    private static Map<Class, Object> context = new HashMap<>();

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
            assert cLoader != null;
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
            try {
                addBean(classes);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void addBean(List<Class> classes) throws ClassNotFoundException {
        /* Si es RestController reviso las anotaciones sobro los fields
        si la annotación es Autowired reviso los beans para ver si ya tengo la instancia, se saca del contexto
        y se inyecta
        reviso que esté anotada con service
        se instancia y se inyecta
        se guarda el restcontroller en el contexto

        Service
        Solamente se instancía
        se guarda en el contexto
        * */
        for (Class cls : classes){
            if(cls.isAnnotationPresent(RestController.class))
            {
                makeBean(cls);
                Object restController = getBean(cls);
                // Obtener campos y checar los que esten anotados con autowired
                Field[] fields = restController.getClass().getDeclaredFields();
                for(Field fld : fields)
                {
                    fld.setAccessible(true);
                    if (fld.isAnnotationPresent(Autowired.class)){
                        Object objClz = null;
                            try {
                                Class clz = Class.forName(fld.getType().getName());
                                if (context.containsKey(clz)){
                                    objClz = getBean(clz);
                                } else if (clz.isAnnotationPresent(Service.class)) {
                                    makeBean(clz);
                                    objClz = getBean(clz);
                                }
                            } catch (ClassNotFoundException e) {
                                throw new RuntimeException(e);
                            }
                            injector(restController,objClz,fld.getName());
                    }
                }
            } else if (cls.isAnnotationPresent(Service.class)) {
                makeBean(cls);
            }
        }

//        Object rcontroller = getBean(NewController.class);
//        Object service = getBean(ProductService.class);
//        try {
//            Field feld = rcontroller.getClass().getField("productService");
//            try {
//                feld.set(rcontroller,(ProductService) service);
//            } catch (IllegalAccessException e) {
//                throw new RuntimeException(e);
//            }
//
//        } catch (NoSuchFieldException e) {
//            throw new RuntimeException(e);
//        }
    }

    public static  void injector(Object ctrlr, Object dpndncy, String fieldName){
        try {
            Field feld = ctrlr.getClass().getField(fieldName);
            try {
                feld.set(ctrlr, dpndncy);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }

        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    public static void makeBean(Class cls) {
        if(!context.containsKey(cls)){
            Object object = null;
            try {
                Constructor<?> objectConstructor = cls.forName(cls.getName()).getConstructor(null);
                object = objectConstructor.newInstance(null);

            } catch (Exception e) {
                MessageLogger.error(e.getMessage());
            }
            context.put(cls, object);
        }
    }

    public static <T> T getBean(Class<?> cls){
        return (T) context.get(cls);
    }

    public static Map<String, Class> getRestControllers() {
        Map<String, Class> restControllers = new HashMap<>();
        for(Map.Entry<Class, Object> entry : context.entrySet()){
            if( entry.getKey().isAnnotationPresent(RestController.class))
            {
                restControllers.put(entry.getKey().getSimpleName(), entry.getValue().getClass());
            }
        }
        return  restControllers;
    }

    public static Map<String, Method> getRestControllerMethods() {
        Map<String, Class> restControllers = getRestControllers();
        Map<String, Method> methods = new HashMap<>();
        for(Map.Entry<String, Class> entry : restControllers.entrySet()) {
            RequestMapping reqMapping = (RequestMapping) entry.getValue().getAnnotation(RequestMapping.class);
            Method[] methList = entry.getValue().getMethods();
            for( Method m : methList) {
                String key = reqMapping.path();
                if(m.isAnnotationPresent(GetMapping.class)){
                    for(Parameter param : m.getParameters()){
                        if(param.isAnnotationPresent(PathVariable.class)){
                            System.out.println(m.getName());
                            PathVariable var = param.getAnnotation(PathVariable.class);
                            System.out.println("Value = " + var.value() + ", name = " + var.name());
                            System.out.println(param.getName());
                        }
                        //System.out.println(par.getAnnotation(PathVariable.class));
                    }
                    GetMapping getMapping = m.getAnnotation(GetMapping.class);
                    key = RequestMethod.GET + key + getMapping.path()[0];
                    //MessageLogger.info(getMapping.path());
                    methods.put(key, m);
                }
                if(m.isAnnotationPresent(PostMapping.class))
                {
                    PostMapping postMapping = m.getAnnotation(PostMapping.class);
                    key = RequestMethod.POST + key + postMapping.path()[0];
                    methods.put(key, m);
                }
            }
        }
        return methods;
    }
}
