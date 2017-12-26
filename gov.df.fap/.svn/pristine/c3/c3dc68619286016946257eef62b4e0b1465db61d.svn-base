package gov.df.fap.bean.gl.coa;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.StringUtils;

/**
 * 一个COA明细
 * @author lwq
 * @version 2007-5-14
 */
public class FCoaDetail implements Cloneable, Serializable {

  private static final long serialVersionUID = 1L;

  public static final String[] LEVEL_NAMES = new String[] { "任意级次", "底级", "自定义级次", "一级", "二级", "三级", "四级", "五级", "六级",
    "七级", "八级", "九级" };

  private int levelNum = 0;

  private FCoaDTO coaDto = null;

  private String eleCode = null;

  private String eleName = null;

  private String coaId = null;

  private String coaDetailId = null;

  private List coaDetailCode = null;

  private String defaultValue = null;

  private int setYear = 0;

  private int dispOrder = 0;

  private int isMustInput = 1;

  private String rgCode = StringUtils.EMPTY;

  public String getCoaDetailId() {
    return coaDetailId;
  }

  public String getRgCode() {
    return rgCode;
  }

  public void setRgCode(String rgCode) {
    this.rgCode = rgCode;
  }

  public void setCoaDetailId(String coaDetailId) {
    this.coaDetailId = coaDetailId;
  }

  public List getCoaDetailCode() {
    return coaDetailCode;
  }

  public void setCoaDetailCode(List coaDetailCode) {
    this.coaDetailCode = coaDetailCode;
  }

  public String getCoaId() {
    return coaId;
  }

  public void setCoaId(String coaId) {
    this.coaId = coaId;
  }

  public int getLevelNum() {
    return levelNum;
  }

  public void setLevelNum(int levelNum) {
    this.levelNum = levelNum;
  }

  public String getEleName() {
    return eleName;
  }

  public void setEleName(String ele_name) {
    this.eleName = ele_name;
  }

  public String getDefaultValue() {
    return defaultValue;
  }

  public void setDefaultValue(String defaultValue) {
    this.defaultValue = defaultValue;
  }

  public FCoaDTO getCoaDto() {
    return coaDto;
  }

  public void setCoaDTO(FCoaDTO coaDto) {
    if (coaDto != null) {
      this.coaId = coaDto.getCoaId();
      this.coaDto = coaDto;
    }
  }

  public String getEleCode() {
    return eleCode;
  }

  public void setEleCode(String eleCode) {
    this.eleCode = eleCode;
  }

  public String getLevelName() {
    int index = levelNum + 2;
    if (index > LEVEL_NAMES.length - 1 || index < 0) {
      //throw GlException.glExceptionFactory(GlConstant.EXCEP_ILLEGAL_KEY, new Object[]{"级次"});
      try {
        throw new Exception("级次错误");
      } catch (Exception e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
    return LEVEL_NAMES[index];
  }

  public int getDispOrder() {
    return dispOrder;
  }

  public void setDispOrder(int dispOrder) {
    this.dispOrder = dispOrder;
  }

  public int getSetYear() {
    return setYear;
  }

  public void setSetYear(int setYear) {
    this.setYear = setYear;
  }

  public int getIsMustInput() {
    return isMustInput;
  }

  public void setIsMustInput(int isMustInput) {
    this.isMustInput = isMustInput;
  }

   public Object clone() {
     FCoaDetail coaDetail = null;
     try {
       coaDetail = (FCoaDetail) super.clone();
     } catch (CloneNotSupportedException e) {
       e.printStackTrace();
     }
     return coaDetail;
   }
}
