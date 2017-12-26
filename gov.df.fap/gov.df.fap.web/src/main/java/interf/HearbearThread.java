package interf;

import com.longtu.framework.heartbeat.HearbeatFactory;
import com.longtu.framework.springexp.LoadAppidFactory;
import com.longtu.framework.util.ServiceFactory;

public class HearbearThread extends Thread {
  public void run() {
    while (true) {
      try {
        Thread.sleep(1000L);
      } catch (InterruptedException localInterruptedException) {
      }
      HearbeatManager m = (HearbeatManager) HearbeatFactory.getManager();
      Object[] list = m.getServiceAll().toArray();
      for (Object keyy : list) {
        String key = keyy.toString();
        if (LoadAppidFactory.newInstance().getServerGuid().equals(key))
          continue;
        try {
          IHearbeatService hrs = (IHearbeatService) ServiceFactory.getBean("remote.fasp2.hearbeat.service", key);
          hrs.hearbeat(key);
        } catch (Throwable localThrowable) {
          m.destroy(key);
        }
      }
    }
  }
}
