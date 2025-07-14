/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package npm.inf.thread;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import npm.inf.init.NodeInfMonitoring;
import npm.inf.pojo.InterfaceMonInfo;

/**
 *
 * @author NPM
 */
public class InterfaceMonRequester implements Runnable {

   // public static Logger logger = Logger.getLogger(InterfaceMonRequester.class.getName());
    private ExecutorService executorService = null;

    @Override
    public void run() {

        ArrayList<InterfaceMonInfo> internal_list = null;
        internal_list = new ArrayList<>();
        ArrayList<InterfaceMonInfo> external_list = null;
        external_list = new ArrayList<>();
        
        System.out.println("Interfcae Monitoring SIze:"+NodeInfMonitoring.interface_mon_list.size());
        Iterator<InterfaceMonInfo> main_itr = NodeInfMonitoring.interface_mon_list.iterator();
        int m = 0;
        while (main_itr.hasNext()) {
            m = m + 1;

            InterfaceMonInfo content_val = main_itr.next();
            internal_list.add(content_val);
            if (m % 1 == 0) {

                external_list.addAll(internal_list);
                internal_list.clear();
            }
        }

        if (internal_list.size() != 0) {
            external_list.addAll(internal_list);
        }

        int thread_pool_size = external_list.size();
        System.out.println("Thread size:"+thread_pool_size);
//        System.out.println("Internal List values : "+internal_list);
//        System.out.println("External List size : "+external_list.size());
//        System.out.println("External List values : "+external_list);
//        
        Runnable runnable = null;
        executorService = null;
        executorService = Executors.newFixedThreadPool(thread_pool_size);
        Iterator out_itr = external_list.iterator();//1236
        while (out_itr.hasNext()) {

            ArrayList<InterfaceMonInfo> list = null;
            list = new ArrayList<>();

            InterfaceMonInfo info = (InterfaceMonInfo) out_itr.next();
            list.add(info);
           // System.out.println("Thread list size : "+list.size());
          //  System.out.println("Interface list : "+list);
            try {
                runnable = null;
                runnable = new InterfaceSensorMonitoring(list);
                executorService.execute(runnable);
                
                Thread.sleep(3000);
            } catch (Exception e) {

            }

        }

    }

}
