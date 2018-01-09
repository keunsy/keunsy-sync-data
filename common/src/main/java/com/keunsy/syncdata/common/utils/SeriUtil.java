package com.keunsy.syncdata.common.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class SeriUtil {

    /**
     * 序列化对象为byte[]
     * @method toByteArray         
     * @return byte[]
     */
    public static byte[] toByteArray(Object objValue) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        new ObjectOutputStream(bos).writeObject(objValue);
        byte[] t = bos.toByteArray();
        return t;
    }

    /**
     * 反序列化为对象
     * @method toObject         
     * @return Object
     */
    public static Object toObject(byte[] array) {
        Object obj = null;
        ByteArrayInputStream bis = new ByteArrayInputStream(array);
        try {
            obj = new ObjectInputStream(bis).readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return obj;
    }

    /**
     * 将对象序列化为字符串
     * @method toString         
     * @return String
     */
    public static String toString(Object obj, String encoding) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        new ObjectOutputStream(bos).writeObject(obj);
        return bos.toString(encoding);
    }

    /**
     * 将字符串反序列化为对象
     * @method toObject         
     * @return Object
     */
    public static Object toObject(String val, String encoding) throws ClassNotFoundException, IOException {
        ByteArrayInputStream bis = new ByteArrayInputStream(val.getBytes(encoding));
        ObjectInputStream ois = new ObjectInputStream(bis);
        return ois.readObject();
    }

}
