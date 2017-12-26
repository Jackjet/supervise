package gov.df.fap.controller.changergyear;

import gov.df.fap.api.changergyear.ChangeRgcodeSetyearInterface;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/df/changergyear")
public class ChangeRgcodeSetyear {
  @Autowired
  private ChangeRgcodeSetyearInterface ryin;

  @RequestMapping(value = "/changergyear.do", method = RequestMethod.POST)
  public @ResponseBody
  Map<String, Object> changergcode(HttpServletRequest request, HttpServletResponse response) {
    Map<String, Object> map = new HashMap<String, Object>();
    map = ryin.ChangeRgcodeSetyear();
    return map;
  }

  @RequestMapping(value = "/getRgcode.do", method = RequestMethod.POST)
  public @ResponseBody
  Map<String, Object> getRgcode(HttpServletRequest request, HttpServletResponse response) {
    Map<String, Object> map = new HashMap<String, Object>();
    map = ryin.getRgcodeSetyear();
    return map;
  }
}
