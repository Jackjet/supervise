package gov.df.fap.service.webservicetest.service;

import gov.df.fap.service.webservicetest.inter.IWebserviceInter;

import javax.jws.WebService;

@WebService(endpointInterface = "gov.df.fap.service.webservicetest.inter.IWebserviceInter ", serviceName = "YyWS")
public class WebServiceImpl implements IWebserviceInter {

  @Override
  public String test(String param) {
    return "param";

  }
}
