package gov.df.fap.controller.portal;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 门户与大数据对接
 */
@Controller
@RequestMapping(value = "/df/portal/dubbo")
public class PortalDubboController {

  //  @Autowired
  //  IPayProgressService iPayProgressService;
  //  
  //  /**
  //   * 支付进度
  //   */
  //  @RequestMapping(value="/payProgress.do", method=RequestMethod.GET)
  //  @ResponseBody
  //  public Map<String, Object> payProgress(HttpServletRequest req, HttpServletResponse resp) {
  //    Map<String, Object> map = new HashMap<String, Object>();
  //    
  //    String fundtypeCode = req.getParameter("fundtypeCode");
  //    String expfuncCode = req.getParameter("expfuncCode");
  //    // 指标来源
  //    String bgtsourceCode = req.getParameter("bgtsourceCode");
  //    String selecttime = req.getParameter("selecttime");
  //    
  //    PayProgressData payProgressData = new PayProgressData();
  //    payProgressData.setMofdepCode("10");
  //    payProgressData.setAgencyCode("413001");
  //    payProgressData.setFundtypeCode(fundtypeCode);
  //    payProgressData.setExpfuncCode(expfuncCode);
  //    payProgressData.setBgtsourceCode(bgtsourceCode);
  //    payProgressData.setQueryDate(selecttime.replace("-", ""));
  //    List<PayProgressData> list = iPayProgressService.getPayProgressdata(payProgressData);
  //    map.put("data", list);
  //    resp.setHeader("Cache-Control", "no-cache");
  //    return map;
  //  }

}
