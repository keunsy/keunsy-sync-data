package com.keunsy.syncdata.common.utils;


import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * 项目名称：financeSystem 类名称：ReflectionUtils 类描述： 创建人：chenrong1 创建时间：2014-5-20
 * 下午1:40:36 修改人：chenrong1 修改时间：2014-5-20 下午1:40:36 修改备注：
 * 
 * @version
 * 
 */
public class ReflectionUtils {

    /**
     * 根据对象获得所有字段的值
     * 
     */
    @SuppressWarnings("rawtypes")
    public static List<Object> getPropertyValue(Object model) {

        List<Object> list = new ArrayList<Object>();
        if (model == null) {
            return list;
        }
        try {
            Class clazz = model.getClass();
            Field[] fields = model.getClass().getDeclaredFields();// 获得所有属性

            for (Field field : fields) {

                PropertyDescriptor pd = new PropertyDescriptor(field.getName(), clazz);
                Method getMethod = pd.getReadMethod();// 获得get方法
                Object object = getMethod.invoke(model);// 执行get方法返回一个object
                // System.out.println(line + ":" + field.getName() + "=" +
                // object);
                // System.out.println(object);
                list.add(object);
            }
        } catch (IntrospectionException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 通过对象和具体的字段名字获得字段的值
     * 
     * @param model
     * @param filed
     */
    public static Object getPropertyValueByField(Object model, String filed) {

        Object object = null;
        try {
            Class<?> clazz = model.getClass();
            PropertyDescriptor pd = new PropertyDescriptor(filed, clazz);
            Method getMethod = pd.getReadMethod();// 获取get方法
            if (pd != null) {
                object = getMethod.invoke(model);// 执行get方法返回一个object
            }
        } catch (IntrospectionException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        return object;
    }

    /**
     * 根据对象获得所有属性
     * 
     */
    public static List<Field> getProperty(Object model) {

        List<Field> list = new ArrayList<Field>();
        try {
            // Class clazz = model.getClass();
            Field[] fields = model.getClass().getDeclaredFields();// 获得所有属性

            for (Field field : fields) {

                list.add(field);
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 直接设置对象属性值, 无视private/protected修饰符, 不经过setter函数.
     */
    public static void setFieldValue(final Object object, final String fieldName, final Object value) {

        try {

            // Field field = getDeclaredField(object, fieldName);向上获取
            Field field = object.getClass().getDeclaredField(fieldName);
            if (field == null) {
                throw new IllegalArgumentException("Could not find field [" + fieldName + "] on target [" + object
                        + "]");
            }
            makeAccessible(field);
            field.set(object, value);
        } catch (IllegalAccessException e) {
        } catch (SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 循环向上转型, 获取对象的DeclaredField.
     * 
     * 如向上转型到Object仍无法找到, 返回null.
     */
    protected static Field getDeclaredField(final Object object, final String fieldName) {

        for (Class<?> superClass = object.getClass(); superClass != Object.class; superClass = superClass
                .getSuperclass()) {
            try {
                return superClass.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {// NOSONAR
                // Field不在当前类定义,继续向上转型
            }
        }
        return null;
    }

    /**
     * 强行设置Field可访问.
     */
    protected static void makeAccessible(final Field field) {
        if (!Modifier.isPublic(field.getModifiers()) || !Modifier.isPublic(field.getDeclaringClass().getModifiers())) {
            field.setAccessible(true);
        }
    }

}
