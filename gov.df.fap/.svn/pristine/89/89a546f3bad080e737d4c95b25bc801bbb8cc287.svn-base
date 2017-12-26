package gov.df.fap.controller.coa;

import gov.df.fap.api.dictionary.interfaces.IDictionary;
import gov.df.fap.bean.gl.coa.FCoaDTO;
import gov.df.fap.bean.gl.coa.FCoaDetail;
import gov.df.fap.util.xml.XMLData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 要素层级controller
 */

@Controller
@RequestMapping(value = "/df/coa")
public class CoaController {
  /**
   * 要素
   */
  private static List elementSeq = new ArrayList();

  /**
   * 要素列表
   */
  private Set elementSet = new LinkedHashSet();

  /**
   * 按照使用的子系统，将各个coa放到相应子系统的属性中
   */
  private Map coaCacheMap = new LinkedHashMap();

  /**
   * 此处coaData是界面传入参数 存储格式为 key-子系统类型 value-各种子系统类型的coa集合
   * */
  private Map coaData = null;

  /**
   * 此处coaCache是本界面中coa缓存
   * 用处是为将界面传入的coaData转换成 key-coaId value-map
   * 其中value中的map key-eleCode,value-FCoaDetail
   * 为了优化后面的排序，将格式转换存储
   * */
  private Map coaCache = new HashMap();

  private static List sysSeq = new ArrayList();

  @Autowired
  private gov.df.fap.api.gl.coa.IConfigCoa icoa; //ICoa coa配置管理服务接口

  @Autowired
  private gov.df.fap.api.gl.coa.ibs.ICoa coaService;

  @Autowired
  private IDictionary dictionaryService; //数据字典服务端组件DTO接口（数据元、要素关联）

  /**
   * coa级联设置
   */
  private Map coaCascadeMap = null;

  //	private List coaLists=null;

  /*	*//**
        * 获取所有COA要素信息（针对表sys_element）
        * 查找选要素配置信息(表sys_element)，“要素级次”正下方列表用到
        * 
        * @return Map
        * @throws Exception
        * */
  /*
  @SuppressWarnings("unchecked")
  @RequestMapping(value = "/getEleSetByCondition.do", method = RequestMethod.GET)
  @ResponseBody
  public Map<String,Object> getEleSetByCondition() {
  Map<String, Object> result = new HashMap<String, Object>();
  List data = null;
  if (sysSeq.isEmpty()) {
  sysSeq.add(FCoaDTO.BUSINESS_STATUS[0]);
  sysSeq.add(FCoaDTO.BUSINESS_STATUS[1]);
  sysSeq.add(FCoaDTO.BUSINESS_STATUS[2]);
  sysSeq.add(FCoaDTO.BUSINESS_STATUS[3]);
  sysSeq.add(FCoaDTO.BUSINESS_STATUS[4]);
  }
  try {
  if (elementSeq.isEmpty()) {
  // 根据传入的过滤条件筛选要素配置信息
  List elementSet = dictionaryService
  		.getElementSet(" and enabled=1 and is_deleted=0 order by disp_order,ele_code");
  for (int i = 0; i < elementSet.size(); i++){
  	elementSeq.add(((Map) elementSet.get(i)).get("ele_code"));
  data.add(elementSeq);
  }
  }
  result.put("data", data);
  result.put("errorCode", 0); // 0代表操作成功
  } catch (Exception e) {
  result.put("errorCode", -1); // -1代表操作失败
  e.printStackTrace();
  }
  return result;
  }
  */

  /**
   * 获取所有coa（针对表gl_coa）
   * 
   * @method：	GET 
   * @return Map(String , List)
   * @throws Exception异常	
     * */
  @SuppressWarnings("unchecked")
  @RequestMapping(value = "/getAllCoa.do", method = RequestMethod.GET)
  @ResponseBody
  public Map<String, Object> getAllCoa() {
    //this.setUserInfo();
    Map<String, Object> result = new HashMap<String, Object>();
    List coaLists = new ArrayList();
    List coaName = new ArrayList();
    List coaDetail = new ArrayList();
    List sysAppName = new ArrayList();
    try {
      List coaList = coaService.getAllCoa(); //得到所有的FcoaDto
      coaList = removeGlCoa(coaList);//移除不符合要求的FcoaDto
      Iterator i = coaList.iterator();

      while (i.hasNext()) {
        List list1 = new ArrayList();
        FCoaDTO coaDto = (FCoaDTO) i.next();
        //判断一条FcoaDto是否有业务阶段，没有就归类到其他
        if (StringUtils.isEmpty(coaDto.getSysAppName())) {
          coaDto.setSysAppName("其他");
        }

        List coaDetailList = new ArrayList();
        for (int j = 0; j < coaDto.size(); j++) {
          FCoaDetail f = coaDto.get(j);
          f.setCoaDTO(null);
          coaDetailList.add(f);
        }

        // coaDto.setCoaDetailList(coaDetailList);
        list1.add(coaDto.getCoaName());
        list1.add(coaDto.getCoaId());
        list1.add(coaDto.getSysAppName());
        list1.add(coaDetailList);
        // list1.add(coaDto);
        coaLists.add(list1);
        // coaLists.add(coaDto.getCoaDesc());
      }

      result.put("coaLists", coaLists);
      result.put("errorCode", 0); // 0代表操作成功
    } catch (Exception e) {
      result.put("errorCode", -1); // -1代表操作失败
      e.printStackTrace();
    }
    return result;
  }

  /**
   * 获取COA级联信息（表gl_coa_cascade）
   * 
   * @method：	GET 
   * @return Map
   * @throws Exception
   * */
  /*
  @RequestMapping(value = "/getCoaCascade.do", method = RequestMethod.GET)
  @ResponseBody
  public Map<String,Object> getCoaCascade() {
  Map<String, Object> result = new HashMap<String, Object>();
  this.setUserInfo();
  try {
  coaCascadeMap = icoa.getCoaCascade(); // 取出COA当前年度和区划的级联信息
  result.put("coaCascadeMap", coaCascadeMap);
  result.put("errorCode", 0); // 0代表操作成功
  } catch (Exception e) {
  result.put("errorCode", -1); // -1代表操作失败
  e.printStackTrace();
  }
  return result;
  }*/

  /**
   * 查询COA的级联设置（数据是指界面表头，针对表gl_coa_cascade）
   * 
   * @method：	GET 
   * @return Map
   * @throws Exception
   * */
  /*
  @RequestMapping(value = "/loadCoaCascade.do", method = RequestMethod.GET)
  @ResponseBody
  public Map<String,Object> loadCoaCascade() {
  Map<String, Object> result = new HashMap<String, Object>();
  try {
  List coaCascadeMap = icoa.loadCoaCascade(); 
  result.put("coaCascadeMap", coaCascadeMap);
  result.put("errorCode", 0); // 0代表操作成功
  } catch (Exception e) {
  result.put("errorCode", -1); // -1代表操作失败
  e.printStackTrace();
  }
  return result;
  }*/

  /**
   * 保存COA配置（针对表gl_coa）
   * 
   * @method	POST 
   * @param  FCoaDTO（COA的model数据对象）
   * @return Map（String，FCoaDTO）
   * @throws Exception
   * */
  @RequestMapping(value = "/saveCoaDto.do", method = RequestMethod.POST)
  @ResponseBody
  public Map<String, Object> saveCoaDto(@RequestBody
  FCoaDTO coaDataform) {
    List list = coaDataform.getCoaDetailList();
    List<FCoaDetail> coaDetailList = new ArrayList();
    if (list != null) {
      Iterator i = list.iterator();
      while (i.hasNext()) {
        LinkedHashMap f = (LinkedHashMap) i.next();
        FCoaDetail fcd = new FCoaDetail();
        fcd.setDefaultValue((String) f.get("defaultValue"));
        fcd.setEleCode((String) f.get("eleCode"));
        fcd.setEleName((String) f.get("eleName"));
        fcd.setLevelNum((Integer) f.get("levelNum"));
        coaDetailList.add(fcd);
        /* fcd.setCoaDetailCode((List)f.get("coaDetailCode"));
         fcd.setCoaDetailId((String)f.get("coaDetailId"));
         fcd.setCoaDTO(coaDto)*/
        /*System.out.print(f.get("defaultValue"));
        System.out.print(f);*/
      }
    }
    coaDataform.setCoaDetailList(coaDetailList);
    Map<String, Object> result = new HashMap<String, Object>();

    try {
      FCoaDTO coaDtoBack = icoa.saveCoaDto(coaDataform); // 保存COA配置
      result.put("coaDtoBack", coaDtoBack);
      result.put("errorCode", 0); // 0代表操作成功

    } catch (Exception e) {

      result.put("errorMessage", e.getMessage());
      result.put("errorCode", -1); // -1代表操作失败
      e.printStackTrace();
    }
    return result;
  }

  /**
   * 更新coa配置（表gl_coa）
   * 
   * @param coaDto
   * 
   * @method	POST 
   * @return void
   * @throws Exception
   */
  @RequestMapping(value = "/updateCoaDto.do", method = RequestMethod.POST)
  @ResponseBody
  public Map<String, Object> updateCoaDto(@RequestBody
  FCoaDTO coaDataform) {

    Map<String, Object> result = new HashMap<String, Object>();
    List list = coaDataform.getCoaDetailList();
    List<FCoaDetail> coaDetailList = new ArrayList();
    if (list != null) {
      Iterator i = list.iterator();
      while (i.hasNext()) {
        LinkedHashMap f = (LinkedHashMap) i.next();
        FCoaDetail fcd = new FCoaDetail();
        fcd.setDefaultValue((String) f.get("defaultValue"));
        fcd.setEleCode((String) f.get("eleCode"));
        fcd.setEleName((String) f.get("eleName"));
        fcd.setLevelNum(f.get("levelNum") == null ? 0 : (Integer) f.get("levelNum"));
        coaDetailList.add(fcd);
        /* fcd.setCoaDetailCode((List)f.get("coaDetailCode"));
         fcd.setCoaDetailId((String)f.get("coaDetailId"));
         fcd.setCoaDTO(coaDto)*/
        /*System.out.print(f.get("defaultValue"));
        System.out.print(f);*/
      }
    }
    coaDataform.setCoaDetailList(coaDetailList);
    List data = null;
    try {

      icoa.checkCascadeCoa(coaDataform);// 校验coa级联关系
      icoa.updateCoaDto(coaDataform, true);
      result.put("errorCode", 0); // 0代表操作成功

    } catch (Exception e) {
      //icoa.updateCoaDto(coaDataform,true);
      result.put("errorMessage", e.getMessage());
      result.put("errorCode", -1); // -1代表操作失败
      e.printStackTrace();
    }
    return result;
  }

  /**
   * 更新coa配置(级联更新)（表gl_coa）
   * 
   * @param coaDto
   * @param isCascadeSave 是否级联保存
   * @method	POST 
   * @return void
   * @throws Exception
   */
  @RequestMapping(value = "/updateCascadeCoaDto.do", method = RequestMethod.POST)
  @ResponseBody
  public Map<String, Object> updateCascadeCoaDto(@RequestBody
  FCoaDTO coaDataform) {
    Map<String, Object> result = new HashMap<String, Object>();
    List list = coaDataform.getCoaDetailList();
    List<FCoaDetail> coaDetailList = new ArrayList();
    if (list != null) {
      Iterator i = list.iterator();
      while (i.hasNext()) {
        LinkedHashMap f = (LinkedHashMap) i.next();
        FCoaDetail fcd = new FCoaDetail();
        fcd.setDefaultValue((String) f.get("defaultValue"));
        fcd.setEleCode((String) f.get("eleCode"));
        fcd.setEleName((String) f.get("eleName"));
        fcd.setLevelNum(f.get("levelNum") == null ? 0 : (Integer) f.get("levelNum"));
        coaDetailList.add(fcd);
        /* fcd.setCoaDetailCode((List)f.get("coaDetailCode"));
         fcd.setCoaDetailId((String)f.get("coaDetailId"));
         fcd.setCoaDTO(coaDto)*/
        /*System.out.print(f.get("defaultValue"));
        System.out.print(f);*/
      }
    }
    coaDataform.setCoaDetailList(coaDetailList);
    List data = null;

    try {

      //icoa.checkCascadeCoa(coaDataform);// 校验coa级联关系
      icoa.updateCoaDto(coaDataform, true);
      result.put("errorCode", 0); // 0代表操作成功

    } catch (Exception e) {

      result.put("errorMessage", e.getMessage());
      result.put("errorCode", -1); // -1代表操作失败
      e.printStackTrace();
    }
    return result;
  }

  /**
   * 校验COA级联关系（修改更新时）
   * 
   * @param FCoaDTO（COA的model数据对象）
   * @method	POST 
   * @return void
   * @throws Exception
   */
  /*
  @RequestMapping(value = "/checkCascadeCoa.do", method = RequestMethod.POST)
  @ResponseBody
  public Map<String,Object> checkCascadeCoa(FCoaDTO coaData) {
  Map<String, Object> result = new HashMap<String, Object>();
  List data = null;
  try {
  icoa.checkCascadeCoa(coaData);
  result.put("errorCode", 0); // 0代表操作成功
  } catch (Exception e) {
  result.put("errorCode", -1); // -1代表操作失败
  e.printStackTrace();
  }
  return result;
  }
  */

  /**
   * 通过COA_ID读取COA对象
   * 
   * @param coaid
   * @method	POST 
   * @return Map
   * @throws Exception
   */
  @RequestMapping(value = "/getCoa.do", method = RequestMethod.GET)
  @ResponseBody
  public Map<String, Object> getCoa(Long coaid) {
    Map<String, Object> result = new HashMap<String, Object>();
    //this.setUserInfo();
    try {
      FCoaDTO coaDto = icoa.getCoa(coaid); //通过COA_ID读取COA对象
      List coaDetailList = new ArrayList();
      for (int j = 0; j < coaDto.size(); j++) {
        FCoaDetail f = coaDto.get(j);
        f.setCoaDTO(null);
        coaDetailList.add(f);
      }

      coaDto.setCoaDetailList(coaDetailList);
      result.put("coaDto", coaDto);
      result.put("errorCode", 0); // 0代表操作成功
    } catch (Exception e) {
      result.put("errorCode", -1); // -1代表操作失败
      e.printStackTrace();
    }
    return result;
  }

  /**
   * 根据coa_id删除coa要素配置（表gl_coa）
   * 
   * @param coa_id     要素配置信息
   * @method	POST
   * @return Map
   * @throws Exception
   *             异常
   */
  @RequestMapping(value = "/deleteCoa.do", method = RequestMethod.GET)
  @ResponseBody
  public Map<String, Object> deleteCoa(String coa_id) {
    Map<String, Object> result = new HashMap<String, Object>();
    //this.setUserInfo();
    try {
      icoa.deleteCoa(coa_id);
      result.put("errorCode", 0); // 0代表操作成功
    } catch (Exception e) {
      result.put("errorMessage", e.getMessage());
      result.put("errorCode", -1); // -1代表操作失败
      e.printStackTrace();
    }
    return result;
  }

  /*	*//**
        * 查询要素级次标题
        * 
        * @param
        * @return
        */
  /*
  @RequestMapping(value = "/listTitle.do", method = RequestMethod.GET)
  @ResponseBody
  public Map<String, Object> listTitle() {
  Map<String, Object> result = new HashMap<String, Object>();
  List<String> list = new ArrayList<String>();
  List tmpList = null;
  try {

  final Iterator iterator = coaData.keySet().iterator();
  while (iterator.hasNext()) {
  	String appName = iterator.next().toString();
  	tmpList = (List) coaData.get(appName);
  	String sysAppName = ((FCoaDTO) tmpList.get(0)).getSysAppName();
  	list.add(sysAppName);
  }
  result.put("data", list);
  result.put("errorCode", 0);
  } catch (Exception e) {
  result.put("errorCode", -1);
  e.printStackTrace();
  }
  return result;
  }
  */

  /**
   * 获取当前所有启用要素信息（表sys_element）
   * 
   * @param 
   * @method	
   * @return Map
   * @throws Exception
   *             异常
   */
  @RequestMapping(value = "/getAllElement.do")
  @ResponseBody
  public Map<String, Object> getAllElement() {
    Map<String, Object> result = new HashMap<String, Object>();
    List allElement = new ArrayList();
    List allEleDTOList = new ArrayList();
    //  this.setUserInfo();

    try {
      allElement = coaService.getAllElement();
      /* ElementSet eleSet = null;
       Iterator eleIt = allElement.iterator();
       while (eleIt.hasNext()) {
           eleSet = (ElementSet) eleIt.next();
           FCoaDetail coaDetail = new FCoaDetail();
           coaDetail.setEleCode(eleSet.getEleCode());
           coaDetail.setEleName(eleSet.getEleName());
           coaDetail.setSetYear(Integer.parseInt(eleSet.getSetYear()));
           coaDetail.setDispOrder(Integer.parseInt(eleSet.getDispOrder()));
           allEleDTOList.add(coaDetail);
         }		*/

      result.put("data", allElement);
      result.put("errorCode", 0); // 0代表操作成功
    } catch (Exception e) {
      result.put("errorCode", -1); // -1代表操作失败
      e.printStackTrace();
    }
    return result;
  }

  /**
   * 得到自定义级次的数据（刷新树时）（表gl_coa_detail和表gl_coa_detail_code）
   * 
   */
  @RequestMapping(value = "/getCOADetailCode.do", method = RequestMethod.GET)
  @ResponseBody
  public Map<String, Object> getCOADetailCode(String coa_id, String element) {
    Map<String, Object> result = new HashMap<String, Object>();

    List coadetailList = new ArrayList();
    List allEleDTOList = new ArrayList();
    try {
      coadetailList = coaService.getCOADetailCode(coa_id, element);
      allEleDTOList.add(coadetailList);
      result.put("data", allEleDTOList);
      result.put("errorCode", 0); // 0代表操作成功
    } catch (Exception e) {
      result.put("errorCode", -1); // -1代表操作失败
      e.printStackTrace();
    }
    return result;
  }

  /**
   *  根据要素简称获取要素列表（刷新树时）（表gl_coa_detail和表gl_coa_detail_code）
   * 
   */
  @RequestMapping(value = "/getEle.do", method = RequestMethod.GET)
  @ResponseBody
  public Map<String, Object> getEle(String element) {
    Map<String, Object> result = new HashMap<String, Object>();
    //this.setUserInfo();
    XMLData ele = new XMLData();
    List<XMLData> allEleDTOList = new ArrayList<XMLData>();
    try {
      ele = coaService.getEle(element);
      allEleDTOList.add(ele);
      result.put("data", allEleDTOList);
      result.put("errorCode", 0); // 0代表操作成功
    } catch (Exception e) {
      result.put("errorCode", -1); // -1代表操作失败
      e.printStackTrace();
    }
    return result;
  }

  /*	
  	*//**
     * 得到要素的最大级次
     * 
     * @param ele
     * @param eleCode
     * @return
     */
  /*
  private int getEleMaxLevel(List ele, String eleCode) {
  int maxLevel = 5;
  ElementSetXMLData eleData = null;
  Object level = null;
  for (int i = 0; i < ele.size(); i++) {
  eleData = (ElementSetXMLData) ele.get(i);
  if (eleData.getEleCode().equalsIgnoreCase(eleCode)) {
  	level = eleData.get("max_level");
  	if (level != null)
  		maxLevel = Integer.valueOf(level.toString()).intValue();
  	break;
  }
  }
  return maxLevel;
  }
  */
  /**
   * 将级次转换成中文字
   * 
   * @param levelNum
   * @return
   */
  /*
  public String transferLevelNum2String(int levelNum) {
  if (levelNum == -1)
  return FCoaDetail.LEVEL_NAMES[1];
  else if (levelNum == 1)
  return FCoaDetail.LEVEL_NAMES[3];
  else if (levelNum == 2)
  return FCoaDetail.LEVEL_NAMES[4];
  else if (levelNum == 3)
  return FCoaDetail.LEVEL_NAMES[5];
  else if (levelNum == 4)
  return FCoaDetail.LEVEL_NAMES[6];
  else if (levelNum == 5)
  return FCoaDetail.LEVEL_NAMES[7];
  else if (levelNum == -2)
  return FCoaDetail.LEVEL_NAMES[0];
  else if (levelNum == 0)
  return FCoaDetail.LEVEL_NAMES[2];
  return StringUtils.EMPTY;
  }
  */
  /*public static void setUserInfo(){
  	HashMap user_context = new HashMap();
  	user_context.put("user_id", "F868E54D0381D530645B182A3C53926E");
  	user_context.put("user_name", "中央系统管理员");
  	user_context.put("user_code", "87_sys_admin");
  	user_context.put("rg_code", "110000");
  	user_context.put("sys_id", "df");
  	user_context.put("set_year", "2016");
  	SessionUtil.getUserInfoContext().setContext(user_context);
  }*/

  /**
   * 移除账务的COA
   */
  @SuppressWarnings({ "unchecked" })
  private static List removeGlCoa(List coaList) {
    if (coaList == null || coaList.isEmpty())
      return coaList;

    List retList = new ArrayList();
    for (int i = 0, len = coaList.size(); i < len; i++) {
      final FCoaDTO coa = (FCoaDTO) coaList.get(i);
      if (!coa.getCoaCode().startsWith("900"))
        retList.add(coa);
    }
    return retList;
  }

}
