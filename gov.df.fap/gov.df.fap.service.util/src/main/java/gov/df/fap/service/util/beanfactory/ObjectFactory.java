package gov.df.fap.service.util.beanfactory;

import gov.df.fap.service.util.signer.ICertVerify;
import gov.df.fap.service.util.signer.ILocalSigner;
import gov.df.fap.service.util.signer.IRemoteSigner;
import gov.df.fap.util.PropertiesFile;

public class ObjectFactory {

  private ILocalSigner localSigner = null;

  private ICertVerify certVerify = null;

  private IRemoteSigner remoteSigner = null;

  PropertiesFile propertiesFile = null;

  public ObjectFactory() {
    propertiesFile = new PropertiesFile("securityconf.properties", true);
  }

  public ObjectFactory(String xmlName) {
    propertiesFile = new PropertiesFile(xmlName, true);
  }

  public ILocalSigner getLoclSignerObj() {
    try {
      localSigner = (ILocalSigner) Class.forName(propertiesFile.getProperty("ILocalSigner")).newInstance();
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    } catch (InstantiationException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    }
    return localSigner;
  }

  public ICertVerify getCertVerifyObj() {
    try {
      certVerify = (ICertVerify) Class.forName(propertiesFile.getProperty("ICertVerify")).newInstance();
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    } catch (InstantiationException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    }
    return certVerify;
  }

  public IRemoteSigner getRemoteSigner() {
    try {
      remoteSigner = (IRemoteSigner) Class.forName(propertiesFile.getProperty("IRemoteSigner")).newInstance();
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    } catch (InstantiationException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    }
    return remoteSigner;
  }

  public static void main(String[] args) {
    ObjectFactory obj = new ObjectFactory();
    obj.getCertVerifyObj();
  }

}