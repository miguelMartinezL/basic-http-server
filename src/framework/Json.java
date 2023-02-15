package framework;


import application.Product;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;


public class Json {

//    public static String ioJson(InputStream obj) throws IOException
//    {
//        StringBuilder jsonBuff = new StringBuilder();
//        try
//        {
//            InputStreamReader isr = new InputStreamReader(obj, "utf-8");
//            BufferedReader br = new BufferedReader(isr);
//            String line;
//            while ((line = br.readLine()) != null)
//            {
//                jsonBuff.append(line);
//            }
//            return String.valueOf(jsonBuff);
//        } catch (Exception e)
//        {
//            return e.toString();
//        }
//    }
    public static String toJson(Object obj) throws Exception{
        Class<?> cls = obj.getClass();
        Field[] flds =  cls.getDeclaredFields();
        String str = "{";
        String val;
        for (int i = 0; i <= flds.length - 1; i++){
            flds[i].setAccessible(true);
            val = flds[i].get(obj).toString();
            if(flds[i].getType().getSimpleName().equals("String")){
                val = "\"" + val + "\"";
            }
            str += "\"" + flds[i].getName()+ "\":" + val + (i < flds.length - 1 ? ",": "");
        }
        str += "}";
        return str;
    }

    public static <T> T  fromJson(String str, Class<T> clase) throws Exception {
        String subStr = str.substring(1, str.length() - 1);
        String[] values = subStr.split(",");

        Constructor<?> objectConstructor = Class.forName(clase.getName()).getConstructor(null);
        Object object = objectConstructor.newInstance(null);
        Field[] fields = clase.getDeclaredFields();

        for(int i = 0; i <= fields.length - 1; i++){
            fields[i].setAccessible(true);
            String[] data = values[i].split(":");

            switch (fields[i].getType().getSimpleName()){
                case "int":
                    int val = Integer.valueOf(data[1]);
                    fields[i].set(object, val);
                    break;
                case "String":
                    String strVal = data[1].substring(1, data[1].length() - 1);
                    fields[i].set(object, strVal);
                    break;
                case "double":
                    double dobVal = Double.valueOf(data[1]);
                    fields[i].set(object, dobVal);
                    break;
            }
        }
        T objAsType = clase.cast(object);
        return objAsType;
    }

    public static void main(String[] args) throws Exception {
        String json = "{\"id\":106,\"pname\":\"macbook\",\"batchno\":\"LKEWR67\",\"price\":80000,\"noofproduct\":8}";
        //String json = "{\"pname\" : \"macbook\", \"batchno\" : \"LKEWR67\", \"price\" : 80000, \"noofproduct\" : 8}";
        Product prod = new Product(100,"Mobile","CLK98123",9000.00,6);

        String jsonStr = Json.toJson(prod);
        System.out.println(jsonStr);
//
//        Product obj = Json.parseString(jsonStr, Product.class);
//
//        Map<String, Object> mapa = Json.mapJson(json);
//        Field field;
//        for (String key : mapa.keySet()){
//           //Stream<Field> field =  Arrays.stream(fields).filter(name -> name.equals(key));
//            try {
//                field = obj.getBean().getDeclaredField(key);
//                field.setAccessible(true);
//                field.set(obj, mapa.get(key));
//            } catch (Exception e) {
//                System.out.println(e);
//            }
//
//        }
//
//
//        Logger logger = Logger.getLogger(Json.class.getName());
//
//        logger.info(jsonStr);
//
//        logger.info("application.Product: " + obj.getId() + " "
//                + obj.getPname() + " "
//                + obj.getBatchno() + " "
//                + obj.getPrice() + " "
//                + obj.getNoofproduct());
   }
}
