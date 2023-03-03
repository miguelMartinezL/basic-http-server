package framework.context;

import framework.MessageLogger;
import framework.RequestMethod;
import framework.annotation.*;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.net.URL;
import java.util.*;

public class ComponentScanner {
    private static final Map<Class<?>, Object> context = new HashMap<>();
    public static final Map<String, String> pathvar = new HashMap<>();

    public static List<Class<?>> findFiles(File directory, String pack) {
        List<Class<?>> classes = new ArrayList<>();
        File[] files = directory.listFiles();
        if(files != null){
            for (File file : files) {
                if (file.isDirectory()) {
                    classes.addAll(findFiles(file, pack + '.' + file.getName()));
                } else if (file.getName().endsWith(".class")) {
                    try {
                        classes.add(Class.forName(pack + '.' + file.getName().replaceAll(".class", "")));
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        return classes;
    }

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
        for (String pack : packages) {
            ClassLoader cLoader = ClassLoader.getSystemClassLoader();
            assert cLoader != null;
            String path = pack.replace('.', '/');
            List<Class<?>> classes = new ArrayList<>();
            Enumeration<URL> resources = cLoader.getResources(path);
            List<File> fileAddress = new ArrayList<>();

            while (resources.hasMoreElements()) {
                URL url = resources.nextElement();
                fileAddress.add(new File(url.getFile()));
                System.out.println(url);
            }
            for (File addr : fileAddress) {
                classes.addAll(findFiles(addr, pack));
            }
            try {
                init(classes);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     *         Si es RestController reviso las anotaciones sobro los fields
     *         si la annotación es Autowired reviso los beans para ver si ya tengo la instancia, se saca del contexto
     *         y se inyecta
     *         reviso que esté anotada con service
     *         se instancia y se inyecta
     *         se guarda el restcontroller en el contexto.
     *         Service
     *         Solamente se instancía
     *         se guarda en el contexto
     *
    */
    public static void init(List<Class<?>> classes) throws ClassNotFoundException {
        for (Class<?> cls : classes) {
            if (cls.isAnnotationPresent(RestController.class) || cls.isAnnotationPresent(Service.class)) {
                Object bean = getBean(cls);
                // Obtener campos y checar los que esten anotados con autowired
                Field[] fields = bean.getClass().getDeclaredFields();
                for (Field fld : fields) {
                    fld.setAccessible(true);
                    if (fld.isAnnotationPresent(Autowired.class)) {
                        try {
                            Class<?> clz = Class.forName(fld.getType().getName());
                            Object objClz = getBean(clz);
                            injector(bean, objClz, fld.getName());
                        } catch (ClassNotFoundException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        }
    }

    public static void injector(Object ctrlr, Object dpndncy, String fieldName) {
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


    @SuppressWarnings("unchecked")
    public static <T> T getBean(Class<?> cls) {
        if (!context.containsKey(cls)) {
            Object bean = null;
            try {
                Constructor<?> objectConstructor = cls.getConstructor();
                bean = objectConstructor.newInstance();

            } catch (Exception e) {
                MessageLogger.error(e.getMessage());
            }
            context.put(cls, bean);
        }
        return (T) context.get(cls);
    }

    public static String getVar(String key){
        return pathvar.get(key);
    }

    public static Map<String, Class<?>> getRestControllers() {
        Map<String, Class<?>> restControllers = new HashMap<>();
        for (Map.Entry<Class<?>, Object> entry : context.entrySet()) {
            if (entry.getKey().isAnnotationPresent(RestController.class)) {
                restControllers.put(entry.getKey().getSimpleName(), entry.getValue().getClass());
            }
        }
        return restControllers;
    }

    public static Map<String, Method> getRestControllerMethods() {
        Map<String, Class<?>> restControllers = getRestControllers();
        Map<String, Method> methods = new HashMap<>();
        for (Map.Entry<String, Class<?>> entry : restControllers.entrySet()) {
            RequestMapping reqMapping = entry.getValue().getAnnotation(RequestMapping.class);
            Method[] methList = entry.getValue().getMethods();
            for (Method m : methList) {
                String key = reqMapping.path();
                if (m.isAnnotationPresent(GetMapping.class)) {
                    for (Parameter param : m.getParameters()) {
                        if (param.isAnnotationPresent(PathVariable.class)) {
                            System.out.println(m.getName());
                            PathVariable var = param.getAnnotation(PathVariable.class);
                            GetMapping getMapping = m.getAnnotation(GetMapping.class);
                            System.out.println("Value = " + var.value() + ", name = " + var.name());
                            pathvar.put(var.name(),getMapping.path()[0]);
                        }
                    }
                    GetMapping getMapping = m.getAnnotation(GetMapping.class);
                    key = RequestMethod.GET + key + getMapping.path()[0];
                    //MessageLogger.info(getMapping.path());
                    methods.put(key, m);
                }
                if (m.isAnnotationPresent(PostMapping.class)) {
                    PostMapping postMapping = m.getAnnotation(PostMapping.class);
                    key = RequestMethod.POST + key + postMapping.path()[0];
                    methods.put(key, m);
                }
            }
        }
        return methods;
    }
}
