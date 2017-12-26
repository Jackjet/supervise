package gov.df.fap.api.bill;

import java.rmi.RemoteException;

/**
 * 服务扩展接口，各应用系统可以从此类派生各自对外系统的服务。
 * 并且将此服务通过Spring发布到Web上。
 * @author Administrator
 *
 */
public interface IService {
  public Object doTask(Object param) throws RemoteException;
}
