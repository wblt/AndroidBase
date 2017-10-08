package cn.tthud.taitian.utils;

import com.google.gson.Gson;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by TianHongChun on 2016/6/1.
 */
public class GsonUtils {

    private volatile static Gson gson;
    private GsonUtils(){

    }
    private  static void initGson(){
            if(gson==null){
                synchronized (GsonUtils.class){
                    if(gson==null){
                        gson=new Gson();
                    }
                }
            }
    }

    public static Gson getInstance(){
        initGson();
        return gson;
    }

    /**
     * 对象转json字符串
     */
    public static <T> String beanToJson(Object object){
        initGson();
        return gson.toJson(object);
    }
    /**
     * list对象装json
     */
    public static <T> String listToJson(List<T> list){
        initGson();
        return gson.toJson(list);
    }
    /**
     * json 字符串转对象
     */
    public static   <T> T jsonToBean(String json, Class<T> classOft){
        initGson();
        return gson.fromJson(json,classOft);
    }
    /**
     * json list 转list对象
     */
    public static <T> List<T> jsonToList(String jsonList, Type type){
            initGson();
        return gson.fromJson(jsonList,type);
    }

    /**
     * 转换一个xml格式的字符串到json格式
     *
     * @param xml
     *            xml格式的字符串
     * @return 成功返回json 格式的字符串;失败反回null
     */
    @SuppressWarnings("unchecked")
    public static JSONObject xml2JSON(String xml) {
        JSONObject obj = new JSONObject();
        try {
            InputStream is = new ByteArrayInputStream(xml.getBytes("utf-8"));
            SAXBuilder sb = new SAXBuilder();
            Document doc = sb.build(is);
            Element root = doc.getRootElement();
            obj.put(root.getName(), iterateElement(root));
            return obj;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 一个迭代方法
     *
     * @param element
     *            : org.jdom.Element
     * @return java.util.Map 实例
     */
    @SuppressWarnings("unchecked")
    private static Map iterateElement(Element element) {
        List jiedian = element.getChildren();
        Element et = null;
        Map obj = new HashMap();
        List list = null;
        for (int i = 0; i < jiedian.size(); i++) {
            list = new LinkedList();
            et = (Element) jiedian.get(i);
            if (et.getTextTrim().equals("")) {
                if (et.getChildren().size() == 0)
                    continue;
                if (obj.containsKey(et.getName())) {
                    list = (List) obj.get(et.getName());
                }
                list.add(iterateElement(et));
                obj.put(et.getName(), list);
            } else {
                if (obj.containsKey(et.getName())) {
                    list = (List) obj.get(et.getName());
                }
                list.add(et.getTextTrim());
                obj.put(et.getName(), list);
            }
        }
        return obj;
    }
}
