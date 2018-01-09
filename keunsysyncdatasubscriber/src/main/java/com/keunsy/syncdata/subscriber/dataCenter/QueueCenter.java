package com.keunsy.syncdata.subscriber.dataCenter;

import com.keunsy.syncdata.common.utils.CommonLogFactory;
import com.keunsy.syncdata.subscriber.entity.SignInfo;
import com.keunsy.syncdata.subscriber.entity.TdInfo;
import com.keunsy.syncdata.subscriber.entity.TdSignInfo;
import com.keunsy.syncdata.subscriber.entity.ThreadController;
import com.keunsy.syncdata.subscriber.entity.UserBalanceInfo;
import com.keunsy.syncdata.subscriber.entity.UserCheckType;
import com.keunsy.syncdata.subscriber.entity.UserCountryPhoneCodePrice;
import com.keunsy.syncdata.subscriber.entity.UserInfo;
import com.keunsy.syncdata.subscriber.entity.UserServiceInfo;
import com.keunsy.syncdata.subscriber.entity.UserServiceParam;

import org.apache.commons.logging.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;


/**
 * 
 *     
 * 项目名称：syncPlateDataTool    
 * 类名称：DataCenter    
 * 类描述：    队列数据中心
 * 创建人：chenrong1    
 * 创建时间：2015-9-24 上午10:05:11    
 * 修改人：chenrong1    
 * 修改时间：2015-9-24 上午10:05:11    
 * 修改备注：    
 * @version     
 *
 */
public class QueueCenter {

    private static transient Log log = CommonLogFactory.getLog("monitor");

    /**
     * 队列管理
     */
    private static ArrayBlockingQueue<UserInfo> userInfoQueue = new ArrayBlockingQueue<UserInfo>(5000);//账户队列
    private static ArrayBlockingQueue<SignInfo> signInfoQueue = new ArrayBlockingQueue<SignInfo>(5000);//签名信息队列
    private static ArrayBlockingQueue<TdInfo> tdInfoQueue = new ArrayBlockingQueue<TdInfo>(5000);//通道队列
    private static ArrayBlockingQueue<TdSignInfo> tdSignInfoQueue = new ArrayBlockingQueue<TdSignInfo>(5000);//通道签名队列
    private static ArrayBlockingQueue<ThreadController> threadControllerQueue = new ArrayBlockingQueue<ThreadController>(
            5000);//线程信息队列
    private static ArrayBlockingQueue<UserBalanceInfo> userBalanceInfoQueue = new ArrayBlockingQueue<UserBalanceInfo>(
            5000);//用户余额队列
    private static ArrayBlockingQueue<UserCheckType> userCheckTypeQueue = new ArrayBlockingQueue<UserCheckType>(5000);//用户审核类型队列
    private static ArrayBlockingQueue<UserCountryPhoneCodePrice> userCountryPhoneCodePriceQueue = new ArrayBlockingQueue<UserCountryPhoneCodePrice>(
            5000);//用户国家价格队列
    private static ArrayBlockingQueue<UserServiceInfo> userServiceInfoQueue = new ArrayBlockingQueue<UserServiceInfo>(
            5000);//用户业务队列
    private static ArrayBlockingQueue<UserServiceParam> userServiceParamQueue = new ArrayBlockingQueue<UserServiceParam>(
            5000);//用户参数队列

    public static ArrayBlockingQueue<UserInfo> getUserInfoQueue() {
        return userInfoQueue;
    }

    public static ArrayBlockingQueue<SignInfo> getSignInfoQueue() {
        return signInfoQueue;
    }

    public static ArrayBlockingQueue<TdInfo> getTdInfoQueue() {
        return tdInfoQueue;
    }

    public static ArrayBlockingQueue<TdSignInfo> getTdSignInfoQueue() {
        return tdSignInfoQueue;
    }

    public static ArrayBlockingQueue<ThreadController> getThreadControllerQueue() {
        return threadControllerQueue;
    }

    public static ArrayBlockingQueue<UserBalanceInfo> getUserBalanceInfoQueue() {
        return userBalanceInfoQueue;
    }

    public static ArrayBlockingQueue<UserCheckType> getUserCheckTypeQueue() {
        return userCheckTypeQueue;
    }

    public static ArrayBlockingQueue<UserCountryPhoneCodePrice> getUserCountryPhoneCodePriceQueue() {
        return userCountryPhoneCodePriceQueue;
    }

    public static ArrayBlockingQueue<UserServiceInfo> getUserServiceInfoQueue() {
        return userServiceInfoQueue;
    }

    public static ArrayBlockingQueue<UserServiceParam> getUserServiceParamQueue() {
        return userServiceParamQueue;
    }

    /**
     * 初始化缓存文件
     */
    static {
        File file = new File("../shutDownCache");
        if (file.exists()) {
            for (File f : file.listFiles()) {
                try {
                    ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f));
                    Object value = ois.readObject();
                    ois.close();

                    String fileName = f.getName();
                    String fieldName = fileName.substring(0, fileName.indexOf("."));
                    Field field = QueueCenter.class.getDeclaredField(fieldName);
                    field.set(null, value);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println("---------< QueueCenter Init completed >------------");
    }

    /**
     * 关闭数据中心
     * @method doStop         
     * @return boolean
     */
    public static boolean doStop() {

        boolean result = false;
        File file = new File("../shutDownCache");
        if (!file.exists()) {
            file.mkdirs();
        }
        try {
            for (Field field : QueueCenter.class.getDeclaredFields()) {
                if (!Modifier.isTransient(field.getModifiers())) {
                    File cacheFile = new File("../shutDownCache/" + field.getName() + ".dat");
                    ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(cacheFile));
                    oos.writeObject(field.get(null));
                    oos.flush();
                    oos.close();
                }

            }
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("----------> QueueCenter closed <----------");
        return result;
    }

    /**
     * 添加list到队列中
     * @method addListToQueue         
     * @return boolean
     */
    public static <T> boolean addListToQueue(List<T> list, ArrayBlockingQueue<T> queue) {
        boolean result = false;
        try {
            if (list != null && list.size() <= queue.remainingCapacity()) {
                result = queue.addAll(list);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 从队列中获取信息
     * @method getInfoFromQueue         
     * @return T
     */
    public static <T> T getInfoFromQueue(ArrayBlockingQueue<T> queue) {
        T result = null;
        try {
            if (!queue.isEmpty()) {
                result = queue.poll();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 添加单信息到队列
     * @method addInfoToQueue         
     * @return boolean
     */
    public static <T> boolean addInfoToQueue(T t, ArrayBlockingQueue<T> queue) {
        boolean result = false;
        try {
            if (t != null) {
                // 队列已满：add方法抛异常；offer方法返回false；put方法继续阻塞，直到空间空余
                result = queue.offer(t);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static void printSize() {

        StringBuffer sb = new StringBuffer();
        sb.append("\n\r---------<QueueCenter - print ↓ size>------------\n\r")
                .append("userInfoQueue size: " + userInfoQueue.size()).append(";  \n\r")
                .append("signInfoQueue size: " + signInfoQueue.size()).append(";  \n\r")
                .append("tdInfoQueue size: " + tdInfoQueue.size()).append(";  \n\r")
                .append("tdSignInfoQueue size: " + tdSignInfoQueue.size()).append(";  \n\r")
                .append("threadControllerQueue size: " + threadControllerQueue.size()).append(";  \n\r")
                .append("userBalanceInfoQueue size: " + userBalanceInfoQueue.size()).append(";  \n\r")
                .append("userCheckTypeQueue size: " + userCheckTypeQueue.size()).append(";  \n\r")
                .append("userCountryPhoneCodePriceQueue size: " + userCountryPhoneCodePriceQueue.size())
                .append(";  \n\r").append("userServiceInfoQueue size: " + userServiceInfoQueue.size())
                .append(";  \n\r").append("userServiceParamQueue size: " + userServiceParamQueue.size())
                .append(";  \n\r").append("---------<print ↑ size>------------").append("\n\r");
        log.info(sb.toString());
    }

}
