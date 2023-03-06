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
    public static final Map<String, String> pathVar = new HashMap<>();

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

    /**
     iterate over the file system
     for each .class file
     pass the filename to class loader
     after loading the class we use reflection on it
     -Controller and Service Annotations
     Using reflection we check for any of these two annotations
     and make a decision
     Map< Class, Object>
     */

    public static void scan(String[] packages) throws IOException {
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
            for (File address: fileAddress) {
                classes.addAll(findFiles(address, pack));
            }
            try {
                init(classes);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     *         If it's RestController, checks the annotations over the fields.
     *         If it is Autowired checks the beans for existence and if not, if not, creates it and injects it.
     *        Checks for Service annotation and injects it
    */
    public static void init(List<Class<?>> classes) throws ClassNotFoundException {
        for (Class<?> cls : classes) {
            if (cls.isAnnotationPresent(RestController.class) || cls.isAnnotationPresent(Service.class)) {
                Object bean = getBean(cls);
                // Gets field and looks for the ones with Autowired annotation
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

    public static void injector(Object controller, Object dependency, String fieldName) {
        try {
            Field field = controller.getClass().getField(fieldName);
            try {
                field.set(controller, dependency);
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
                            //System.out.println(m.getName());
                            PathVariable var = param.getAnnotation(PathVariable.class);
                            GetMapping getMapping = m.getAnnotation(GetMapping.class);
                            //System.out.println("Value = " + var.value() + ", name = " + var.name());
                            pathVar.put(var.name(),getMapping.path()[0]);
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
