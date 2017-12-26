package gov.df.fap.service.util.signer;

import javax.security.cert.X509Certificate;

public interface IFX509Certificate {

  public void setCertByString(String cert);

  public X509Certificate getX509Obj();

}
