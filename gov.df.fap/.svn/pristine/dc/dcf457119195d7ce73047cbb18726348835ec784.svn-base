package gov.df.fap.controller.role;

import gov.df.fap.api.role.IRoleDfService;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/df/role")
public class RoleController {

  @Autowired
  private IRoleDfService iRoleService;

  @RequestMapping(value = "/getAllRole.do", method = RequestMethod.GET)
  public @ResponseBody
  Map<String, Object> getAllRole(HttpServletRequest request, HttpServletResponse response) {
    Map<String, Object> map = null;
    map = iRoleService.getAllRole(request, response);
    return map;
  }

}
