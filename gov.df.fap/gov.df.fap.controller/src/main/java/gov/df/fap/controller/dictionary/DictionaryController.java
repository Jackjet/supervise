package gov.df.fap.controller.dictionary;

import gov.df.fap.api.dictionary.IDictionaryService;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/df/dic")
public class DictionaryController {

  @Autowired
  private IDictionaryService iDictionaryService;

  @RequestMapping(value = "/dictree.do", method = RequestMethod.GET)
  public @ResponseBody
  Map<String, Object> test(HttpServletRequest request, HttpServletResponse response) {
    Map<String, Object> map = null;
    map = iDictionaryService.getdictree(request, response);
    return map;
  }
}
