package com.example.cmacking.reactnativeandroiddemo.util;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import android.util.Log;


/**
 * @author keane
 */
public class JSONHelper {
    private static String TAG = "JSONHelper";

    /**
     * 将对象转换成Json字符串
     *
     * @param obj
     * @return json类型字符串
     */
    public static String toJSON(Object obj) {
        JSONStringer js = new JSONStringer();
        serialize(js, obj);
        return js.toString();
    }

    /**
     * 序列化为JSON
     *
     * @param js json对象
     * @param o  待需序列化的对象
     */
    private static void serialize(JSONStringer js, Object o) {
        if (isNull(o)) {
            try {
                js.value(null);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return;
        }

        Class<?> clazz = o.getClass();
        if (isObject(clazz)) { // 对象
            serializeObject(js, o);
        } else if (isArray(clazz)) { // 数组
            serializeArray(js, o);
        } else if (isCollection(clazz)) { // 集合
            Collection<?> collection = (Collection<?>) o;
            serializeCollect(js, collection);
        } else if (isMap(clazz)) { // 集合
            HashMap<?, ?> collection = (HashMap<?, ?>) o;
            serializeMap(js, collection);
        } else { // 单个值
            try {
                js.value(o);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 序列化数组
     *
     * @param js    json对象
     * @param array 数组
     */
    private static void serializeArray(JSONStringer js, Object array) {
        try {
            js.array();
            for (int i = 0; i < Array.getLength(array); ++i) {
                Object o = Array.get(array, i);
                serialize(js, o);
            }
            js.endArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 序列化集合
     *
     * @param js         json对象
     * @param collection 集合
     */
    private static void serializeCollect(JSONStringer js, Collection<?> collection) {
        try {
            js.array();
            for (Object o : collection) {
                serialize(js, o);
            }
            js.endArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 序列化Map
     *
     * @param js  json对象
     * @param map map对象
     */
    private static void serializeMap(JSONStringer js, Map<?, ?> map) {
        try {
            js.object();
            @SuppressWarnings("unchecked")
            Map<String, Object> valueMap = (Map<String, Object>) map;
            Iterator<Map.Entry<String, Object>> it = valueMap.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, Object> entry = (Map.Entry<String, Object>) it.next();
                js.key(entry.getKey());
                serialize(js, entry.getValue());
            }
            js.endObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 序列化对象
     *
     * @param js  json对象
     * @param obj 待序列化对象
     */
    private static void serializeObject(JSONStringer js, Object obj) {
        try {
            js.object();
            Class<? extends Object> objClazz = obj.getClass();
            Method[] methods = objClazz.getDeclaredMethods();
            Field[] fields = objClazz.getDeclaredFields();
            for (Field field : fields) {
                try {
                    String fieldType = field.getType().getSimpleName();
                    String fieldGetName = parseMethodName(field.getName(), "get");
                    if (!haveMethod(methods, fieldGetName)) {
                        continue;
                    }
                    Method fieldGetMet = objClazz.getMethod(fieldGetName, new Class[]{});
                    Object fieldVal = fieldGetMet.invoke(obj, new Object[]{});
                    String result = null;
                    if ("Date".equals(fieldType)) {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
                        result = sdf.format((Date) fieldVal);

                    } else {
                        if (null != fieldVal) {
                            result = String.valueOf(fieldVal);
                        }
                    }
                    js.key(field.getName());
                    serialize(js, result);
                } catch (Exception e) {
                    continue;
                }
            }
            js.endObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 判断是否存在某属性的 get方法
     *
     * @param methods     引用方法的数组
     * @param fieldMethod 方法名称
     * @return true或者false
     */
    public static boolean haveMethod(Method[] methods, String fieldMethod) {
        for (Method met : methods) {
            if (fieldMethod.equals(met.getName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 拼接某属性的 get或者set方法
     *
     * @param fieldName  字段名称
     * @param methodType 方法类型
     * @return 方法名称
     */
    public static String parseMethodName(String fieldName, String methodType) {
        if (null == fieldName || "".equals(fieldName)) {
            return null;
        }
        return methodType + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
    }

    /**
     * 给字段赋值
     *
     * @param obj    实例对象
     * @param valMap 值集合
     */
    public static void setFieldValue(Object obj, Map<String, String> valMap) {
        Class<?> cls = obj.getClass();
        // 取出bean里的所有方法
        Method[] methods = cls.getDeclaredMethods();
        Field[] fields = cls.getDeclaredFields();

        for (Field field : fields) {
            try {
                String setMetodName = parseMethodName(field.getName(), "set");
                if (!haveMethod(methods, setMetodName)) {
                    continue;
                }
                Method fieldMethod = cls.getMethod(setMetodName, field
                        .getType());
                String value = valMap.get(field.getName());
                if (null != value && !"".equals(value)) {
                    String fieldType = field.getType().getSimpleName();
                    if ("String".equals(fieldType)) {
                        fieldMethod.invoke(obj, value);
                    } else if ("Date".equals(fieldType)) {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
                        Date temp = sdf.parse(value);
                        fieldMethod.invoke(obj, temp);
                    } else if ("Integer".equals(fieldType)
                            || "int".equals(fieldType)) {
                        Integer intval = Integer.parseInt(value);
                        fieldMethod.invoke(obj, intval);
                    } else if ("Long".equalsIgnoreCase(fieldType)) {
                        Long temp = Long.parseLong(value);
                        fieldMethod.invoke(obj, temp);
                    } else if ("Double".equalsIgnoreCase(fieldType)) {
                        Double temp = Double.parseDouble(value);
                        fieldMethod.invoke(obj, temp);
                    } else if ("Boolean".equalsIgnoreCase(fieldType)) {
                        Boolean temp = Boolean.parseBoolean(value);
                        fieldMethod.invoke(obj, temp);
                    } else {
                        System.out.println("setFieldValue not supper type:" + fieldType);
                    }
                }
            } catch (Exception e) {
                continue;
            }
        }

    }

    /**
     * bean对象转Map
     *
     * @param obj 实例对象
     * @return map集合
     */
    public static Map<String, String> beanToMap(Object obj) {
        Class<?> cls = obj.getClass();
        Map<String, String> valueMap = new HashMap<String, String>();
        // 取出bean里的所有方法
        Method[] methods = cls.getDeclaredMethods();
        Field[] fields = cls.getDeclaredFields();
        for (Field field : fields) {
            try {
                String fieldType = field.getType().getSimpleName();
                String fieldGetName = parseMethodName(field.getName(), "get");
                if (!haveMethod(methods, fieldGetName)) {
                    continue;
                }
                Method fieldGetMet = cls.getMethod(fieldGetName, new Class[]{});
                Object fieldVal = fieldGetMet.invoke(obj, new Object[]{});
                String result = null;
                if ("Date".equals(fieldType)) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
                    result = sdf.format((Date) fieldVal);

                } else {
                    if (null != fieldVal) {
                        result = String.valueOf(fieldVal);
                    }
                }
                valueMap.put(field.getName(), result);
            } catch (Exception e) {
                continue;
            }
        }
        return valueMap;

    }

    /**
     * 给对象的字段赋值
     *
     * @param obj            类实例
     * @param fieldSetMethod 字段方法
     * @param fieldType      字段类型
     * @param value
     */
    public static void setFiedlValue(Object obj, Method fieldSetMethod, String fieldType, Object value) {

        try {
            if (null != value && !"".equals(value)) {
                if ("String".equals(fieldType)) {
                    fieldSetMethod.invoke(obj, value.toString());
                } else if ("Date".equals(fieldType)) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
                    Date temp = sdf.parse(value.toString());
                    fieldSetMethod.invoke(obj, temp);
                } else if ("Integer".equals(fieldType)
                        || "int".equals(fieldType)) {
                    Integer intval = Integer.parseInt(value.toString());
                    fieldSetMethod.invoke(obj, intval);
                } else if ("Long".equalsIgnoreCase(fieldType)) {
                    Long temp = Long.parseLong(value.toString());
                    fieldSetMethod.invoke(obj, temp);
                } else if ("Double".equalsIgnoreCase(fieldType)) {
                    Double temp = Double.parseDouble(value.toString());
                    fieldSetMethod.invoke(obj, temp);
                } else if ("Boolean".equalsIgnoreCase(fieldType)) {
                    Boolean temp = Boolean.parseBoolean(value.toString());
                    fieldSetMethod.invoke(obj, temp);
                } else {
                    fieldSetMethod.invoke(obj, value);
                    Log.e(TAG, TAG + ">>>>setFiedlValue -> not supper type" + fieldType);
                }
            }

        } catch (Exception e) {
//            Log.e(TAG, TAG  + ">>>>>>>>>>set value error.",e);
            e.printStackTrace();
        }

    }

    /**
     * 反序列化简单对象
     *
     * @param jo    json对象
     * @param clazz 实体类类型
     * @return 反序列化后的实例
     * @throws JSONException
     */
    public static <T> T parseObject(JSONObject jo, Class<T> clazz) throws JSONException {
        if (clazz == null || isNull(jo)) {
            return null;
        }

        T obj = newInstance(clazz);
        if (obj == null) {
            return null;
        }
        if (isMap(clazz)) {
            setField(obj, jo);
        } else {
            // 取出bean里的所有方法
            Method[] methods = clazz.getDeclaredMethods();
            Field[] fields = clazz.getDeclaredFields();
            for (Field f : fields) {
                String setMetodName = parseMethodName(f.getName(), "set");
                if (!haveMethod(methods, setMetodName)) {
                    continue;
                }
                try {
                    Method fieldMethod = clazz.getMethod(setMetodName, f.getType());
                    setField(obj, fieldMethod, f, jo);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return obj;
    }

    /**
     * 反序列化简单对象
     *
     * @param jsonStr json字符串
     * @param clazz   实体类类型
     * @return 反序列化后的实例
     * @throws JSONException
     */
    public static <T> T parseObject(String jsonStr, Class<T> clazz) throws JSONException {
        if (clazz == null || jsonStr == null || jsonStr.length() == 0) {
            return null;
        }

        JSONObject jo = null;
        jo = new JSONObject(jsonStr);
        if (isNull(jo)) {
            return null;
        }

        return parseObject(jo, clazz);
    }

    /**
     * 反序列化数组对象
     *
     * @param ja    json数组
     * @param clazz 实体类类型
     * @return 反序列化后的数组
     */
    public static <T> T[] parseArray(JSONArray ja, Class<T> clazz) {
        if (clazz == null || isNull(ja)) {
            return null;
        }

        int len = ja.length();

        @SuppressWarnings("unchecked")
        T[] array = (T[]) Array.newInstance(clazz, len);

        for (int i = 0; i < len; ++i) {
            try {
                JSONObject jo = ja.getJSONObject(i);
                T o = parseObject(jo, clazz);
                array[i] = o;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return array;
    }


    /**
     * 反序列化数组对象
     *
     * @param jsonStr json字符串
     * @param clazz   实体类类型
     * @return 序列化后的数组
     */
    public static <T> T[] parseArray(String jsonStr, Class<T> clazz) {
        if (clazz == null || jsonStr == null || jsonStr.length() == 0) {
            return null;
        }
        JSONArray jo = null;
        try {
            jo = new JSONArray(jsonStr);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (isNull(jo)) {
            return null;
        }

        return parseArray(jo, clazz);
    }

    /**
     * 反序列化泛型集合
     *
     * @param ja              json数组
     * @param collectionClazz 集合类型
     * @param genericType     实体类类型
     * @return
     * @throws JSONException
     */
    @SuppressWarnings("unchecked")
    public static <T> Collection<T> parseCollection(JSONArray ja, Class<?> collectionClazz,
                                                    Class<T> genericType) throws JSONException {

        if (collectionClazz == null || genericType == null || isNull(ja)) {
            return null;
        }

        Collection<T> collection = (Collection<T>) newInstance(collectionClazz);

        for (int i = 0; i < ja.length(); ++i) {
            try {
                JSONObject jo = ja.getJSONObject(i);
                T o = parseObject(jo, genericType);
                collection.add(o);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return collection;
    }

    /**
     * 反序列化泛型集合
     *
     * @param jsonStr         json字符串
     * @param collectionClazz 集合类型
     * @param genericType     实体类类型
     * @return 反序列化后的数组
     * @throws JSONException
     */
    public static <T> Collection<T> parseCollection(String jsonStr, Class<?> collectionClazz,
                                                    Class<T> genericType) throws JSONException {
        if (collectionClazz == null || genericType == null || jsonStr == null
                || jsonStr.length() == 0) {
            return null;
        }
        JSONArray jo = null;
        try {
            //如果为数组，则此处转化时，需要去掉前面的键，直接后面的[]中的值
            int index = jsonStr.indexOf("[");
            String arrayString = null;

            //获取数组的字符串
            if (-1 != index) {
                arrayString = jsonStr.substring(index);
            }

            //如果为数组，使用数组转化
            if (null != arrayString) {
                jo = new JSONArray(arrayString);
            } else {
                jo = new JSONArray(jsonStr);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (isNull(jo)) {
            return null;
        }

        return parseCollection(jo, collectionClazz, genericType);
    }

    /**
     * 根据类型创建对象
     *
     * @param clazz 待创建实例的类型
     * @return 实例对象
     * @throws JSONException
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    private static <T> T newInstance(Class<T> clazz) throws JSONException {
        if (clazz == null)
            return null;
        T obj = null;
        if (clazz.isInterface()) {
            if (clazz.equals(Map.class)) {
                obj = (T) new HashMap();
            } else if (clazz.equals(List.class)) {
                obj = (T) new ArrayList();
            } else if (clazz.equals(Set.class)) {
                obj = (T) new HashSet();
            } else {
                throw new JSONException("unknown interface: " + clazz);
            }
        } else {
            try {
                obj = clazz.newInstance();
            } catch (Exception e) {
                throw new JSONException("unknown class type: " + clazz);
            }
        }
        return obj;
    }

    /**
     * 设定Map的值
     *
     * @param obj 待赋值字段的对象
     * @param jo  json实例
     */
    private static void setField(Object obj, JSONObject jo) {
        try {
            @SuppressWarnings("unchecked")
            Iterator<String> keyIter = jo.keys();
            String key;
            Object value;
            @SuppressWarnings("unchecked")
            Map<String, Object> valueMap = (Map<String, Object>) obj;
            while (keyIter.hasNext()) {
                key = (String) keyIter.next();
                value = jo.get(key);
                valueMap.put(key, value);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 设定字段的值
     *
     * @param obj            待赋值字段的对象
     * @param fieldSetMethod 字段方法名
     * @param field          字段
     * @param jo             json实例
     */
    private static void setField(Object obj, Method fieldSetMethod, Field field, JSONObject jo) {
        String name = field.getName();
        Class<?> clazz = field.getType();
        try {
            if (isArray(clazz)) { // 数组
                Class<?> c = clazz.getComponentType();
                JSONArray ja = jo.optJSONArray(name);
                if (!isNull(ja)) {
                    Object array = parseArray(ja, c);
                    setFiedlValue(obj, fieldSetMethod, clazz.getSimpleName(), array);
                }
            } else if (isCollection(clazz)) { // 泛型集合
                // 获取定义的泛型类型
                Class<?> c = null;
                Type gType = field.getGenericType();
                if (gType instanceof ParameterizedType) {
                    ParameterizedType ptype = (ParameterizedType) gType;
                    Type[] targs = ptype.getActualTypeArguments();
                    if (targs != null && targs.length > 0) {
                        Type t = targs[0];
                        c = (Class<?>) t;
                    }
                }

                JSONArray ja = jo.optJSONArray(name);
                if (!isNull(ja)) {
                    Object o = parseCollection(ja, clazz, c);
                    setFiedlValue(obj, fieldSetMethod, clazz.getSimpleName(), o);
                }
            } else if (isSingle(clazz)) { // 值类型
                Object o = jo.opt(name);
                if (o != null) {
                    setFiedlValue(obj, fieldSetMethod, clazz.getSimpleName(), o);
                }
            } else if (isObject(clazz)) { // 对象
                JSONObject j = jo.optJSONObject(name);
                if (!isNull(j)) {
                    Object o = parseObject(j, clazz);
                    setFiedlValue(obj, fieldSetMethod, clazz.getSimpleName(), o);
                }
            } else if (isList(clazz)) { // 列表
//              JSONObject j = jo.optJSONObject(name);
//              if (!isNull(j)) {
//                  Object o = parseObject(j, clazz);
//                  f.set(obj, o);
//              }
            } else {
                throw new Exception("unknow type!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 设定字段的值
     *
     * @param obj   待赋值字段的对象
     * @param field 字段
     * @param jo    json实例
     */
    @SuppressWarnings("unused")
    private static void setField(Object obj, Field field, JSONObject jo) {
        String name = field.getName();
        Class<?> clazz = field.getType();
        try {
            if (isArray(clazz)) { // 数组
                Class<?> c = clazz.getComponentType();
                JSONArray ja = jo.optJSONArray(name);
                if (!isNull(ja)) {
                    Object array = parseArray(ja, c);
                    field.set(obj, array);
                }
            } else if (isCollection(clazz)) { // 泛型集合
                // 获取定义的泛型类型
                Class<?> c = null;
                Type gType = field.getGenericType();
                if (gType instanceof ParameterizedType) {
                    ParameterizedType ptype = (ParameterizedType) gType;
                    Type[] targs = ptype.getActualTypeArguments();
                    if (targs != null && targs.length > 0) {
                        Type t = targs[0];
                        c = (Class<?>) t;
                    }
                }
                JSONArray ja = jo.optJSONArray(name);
                if (!isNull(ja)) {
                    Object o = parseCollection(ja, clazz, c);
                    field.set(obj, o);
                }
            } else if (isSingle(clazz)) { // 值类型
                Object o = jo.opt(name);
                if (o != null) {
                    field.set(obj, o);
                }
            } else if (isObject(clazz)) { // 对象
                JSONObject j = jo.optJSONObject(name);
                if (!isNull(j)) {
                    Object o = parseObject(j, clazz);
                    field.set(obj, o);
                }
            } else if (isList(clazz)) { // 列表
                JSONObject j = jo.optJSONObject(name);
                if (!isNull(j)) {
                    Object o = parseObject(j, clazz);
                    field.set(obj, o);
                }
            } else {
                throw new Exception("unknow type!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 判断对象是否为空
     *
     * @param obj 实例
     * @return
     */
    private static boolean isNull(Object obj) {
        if (obj instanceof JSONObject) {
            return JSONObject.NULL.equals(obj);
        }
        return obj == null;
    }

    /**
     * 判断是否是值类型
     *
     * @param clazz
     * @return
     */
    private static boolean isSingle(Class<?> clazz) {
        return isBoolean(clazz) || isNumber(clazz) || isString(clazz);
    }

    /**
     * 是否布尔值
     *
     * @param clazz
     * @return
     */
    public static boolean isBoolean(Class<?> clazz) {
        return (clazz != null)
                && ((Boolean.TYPE.isAssignableFrom(clazz)) || (Boolean.class
                .isAssignableFrom(clazz)));
    }

    /**
     * 是否数值
     *
     * @param clazz
     * @return
     */
    public static boolean isNumber(Class<?> clazz) {
        return (clazz != null)
                && ((Byte.TYPE.isAssignableFrom(clazz)) || (Short.TYPE.isAssignableFrom(clazz))
                || (Integer.TYPE.isAssignableFrom(clazz))
                || (Long.TYPE.isAssignableFrom(clazz))
                || (Float.TYPE.isAssignableFrom(clazz))
                || (Double.TYPE.isAssignableFrom(clazz)) || (Number.class
                .isAssignableFrom(clazz)));
    }

    /**
     * 判断是否是字符串
     *
     * @param clazz
     * @return
     */
    public static boolean isString(Class<?> clazz) {
        return (clazz != null)
                && ((String.class.isAssignableFrom(clazz))
                || (Character.TYPE.isAssignableFrom(clazz)) || (Character.class
                .isAssignableFrom(clazz)));
    }

    /**
     * 判断是否是对象
     *
     * @param clazz
     * @return
     */
    private static boolean isObject(Class<?> clazz) {
        return clazz != null && !isSingle(clazz) && !isArray(clazz) && !isCollection(clazz) && !isMap(clazz);
    }

    /**
     * 判断是否是数组
     *
     * @param clazz
     * @return
     */
    public static boolean isArray(Class<?> clazz) {
        return clazz != null && clazz.isArray();
    }

    /**
     * 判断是否是集合
     *
     * @param clazz
     * @return
     */
    public static boolean isCollection(Class<?> clazz) {
        return clazz != null && Collection.class.isAssignableFrom(clazz);
    }

    /**
     * 判断是否是Map
     *
     * @param clazz
     * @return
     */
    public static boolean isMap(Class<?> clazz) {
        return clazz != null && Map.class.isAssignableFrom(clazz);
    }

    /**
     * 判断是否是列表
     *
     * @param clazz
     * @return
     */
    public static boolean isList(Class<?> clazz) {
        return clazz != null && List.class.isAssignableFrom(clazz);
    }

}