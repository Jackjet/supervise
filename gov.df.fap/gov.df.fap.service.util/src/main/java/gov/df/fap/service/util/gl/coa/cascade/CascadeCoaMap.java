package gov.df.fap.service.util.gl.coa.cascade;

import gov.df.fap.api.gl.coa.ICoaService;
import gov.df.fap.bean.gl.coa.FCoaDTO;
import gov.df.fap.bean.gl.coa.FCoaDetail;
import gov.df.fap.service.util.exceptions.gl.CoaCascadeException;
import gov.df.fap.service.util.memcache.MemCacheMap;
import gov.df.fap.service.util.sessionmanager.SessionUtil;
import gov.df.fap.util.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 级联Coa地图
 * 
 * @author 
 * @version Mar 21, 2017
 */
@SuppressWarnings("rawtypes")
@Service
public class CascadeCoaMap {

  private static final String IS_BRANCH = "is_branch";

  private static final String IS_UP_STREAM = "is_up_stream";

  private static final String RELATION_COA_ID = "relation_coa_id";

  private static final String COA_ID = "coa_id";

  private Map coaCascadeMap = null;

  @Autowired
  private ICoaService coaService;
  
  @Autowired
  private CascadeCoaContextService cascadeCoaContext;

  public void setCascadeCoaContext(CascadeCoaContextService cascadeCoaContext) {
    this.cascadeCoaContext = cascadeCoaContext;
  }

  public Map getCoaCascadeMap() {
    if (coaCascadeMap == null) {
      coaCascadeMap = MemCacheMap.getCacheMap(CascadeCoaMap.class);
    }
    //取出当前年度和区划的级联
    if (coaCascadeMap.get(SessionUtil.getLoginYear() + SessionUtil.getRgCode()) == null
      || ((Map) coaCascadeMap.get(SessionUtil.getLoginYear() + SessionUtil.getRgCode())).keySet().size() == 0) {
      initCascadeMap();
    }
    return (Map) coaCascadeMap.get(SessionUtil.getLoginYear() + SessionUtil.getRgCode());
  }

  @SuppressWarnings("unchecked")
  private void initCascadeMap() {
    List coaCascadeList = coaService.loadCoaCascade();
    Map tmpMap = null;
    CoaRelation coaRelation = null;
    CoaObject coaObj = null;
    List relationList = null;
    Map targetCurRegionYearCoaMap = (Map) coaCascadeMap.get(SessionUtil.getLoginYear() + SessionUtil.getRgCode());
    if (targetCurRegionYearCoaMap == null) {
      targetCurRegionYearCoaMap = new HashMap();
      coaCascadeMap.put(SessionUtil.getLoginYear() + SessionUtil.getRgCode(), targetCurRegionYearCoaMap);
    }

    for (int i = 0; i < coaCascadeList.size(); i++) {
      tmpMap = (Map) coaCascadeList.get(i);
      if (!targetCurRegionYearCoaMap.containsKey(tmpMap.get(COA_ID))) {
        coaRelation = new CoaRelation();
        targetCurRegionYearCoaMap.put(tmpMap.get(COA_ID), coaRelation);
      }
      coaRelation = (CoaRelation) targetCurRegionYearCoaMap.get(tmpMap.get(COA_ID));
      if (coaRelation.getRelationCoaList() == null)
        coaRelation.setRelationCoaList(new ArrayList());
      relationList = coaRelation.getRelationCoaList();
      coaObj = new CoaObject(tmpMap.get(RELATION_COA_ID).toString(), tmpMap.get(IS_UP_STREAM).equals(StringUtil.ONE));
      relationList.add(coaObj);
      coaRelation.setIsBranch(tmpMap.get(IS_BRANCH).equals(StringUtil.ONE));
    }
  }

  /**
   * 检验coa级联信息
   * 
   * @param modifiedCoaDto
   */
  public void checkCoaCascadeMessage(FCoaDTO modifiedCoaDto) throws CoaCascadeException {
    FCoaDTO originCoaDto = coaService.getCoa(Long.parseLong(modifiedCoaDto.getCoaId()));
    List compareItems = CoaComparator.compare(originCoaDto, modifiedCoaDto);

    StringBuffer returnMessage = new StringBuffer();
    for (int i = 0; i < compareItems.size(); i++) {
      CompareItem compareItem = (CompareItem) compareItems.get(i);
      returnMessage.append("级联保存+");
      returnMessage.append(new CascadeProcessor(compareItem, modifiedCoaDto, true).processor());
    }
    if (!StringUtil.isEmpty(returnMessage.toString()))
      throw new CoaCascadeException(returnMessage.toString());
  }

  /**
   * 保存coa级联信息
   * 
   * @param modifiedCoaDto
   * @throws CoaCascadeException
   */
  public void checkCoaCascade(FCoaDTO modifiedCoaDto) throws Exception {
    FCoaDTO originCoaDto = coaService.getCoa(Long.parseLong(modifiedCoaDto.getCoaId()));
    List compareItems = CoaComparator.compare(originCoaDto, modifiedCoaDto);

    cascadeCoaContext.begin();
    for (int i = 0; i < compareItems.size(); i++) {
      CompareItem compareItem = (CompareItem) compareItems.get(i);
      new CascadeProcessor(compareItem, modifiedCoaDto).processor();
      cascadeCoaContext.modifyCoaElement(modifiedCoaDto, compareItem);
    }
    cascadeCoaContext.commit();
  }

  /**
   * 校验coa是否是需要级联更新的coa
   * 
   * @param targetCoa
   * @return
   */
  public boolean isNeedCascade(FCoaDTO targetCoa) {
    final Iterator iterator = getCoaCascadeMap().keySet().iterator();
    while (iterator.hasNext())
      if (iterator.next().toString().equals(targetCoa.getCoaId()))
        return true;
    return false;
  }

  /**
   * 级联校验处理类
   * 
   * @author LiuYan
   * @version Mar 22, 2011
   */
  class CascadeProcessor {

    private CompareItem compareItem = null;

    private FCoaDTO coaDto = null;

    private boolean isCheck = false;

    private StringBuffer returnMessage = new StringBuffer();

    public CascadeProcessor(CompareItem compareItem, FCoaDTO coaDto) {
      this.compareItem = compareItem;
      this.coaDto = coaDto;
    }

    public CascadeProcessor(CompareItem compareItem, FCoaDTO coaDto, boolean isCheck) {
      this.compareItem = compareItem;
      this.coaDto = coaDto;
      this.isCheck = isCheck;
    }

    public String processor() {
      if (compareItem.getOperateType() == CompareItem.ADD_ELEMENT)
        addElementCheck();
      else if (compareItem.getOperateType() == CompareItem.DEL_ELEMENT)
        delElementCheck();
      else
        modifyElementCheck();
      return returnMessage.toString();
    }

    /**
     * 修改要素检查
     */
    private void modifyElementCheck() {
      checkCoaByRecursion(null, coaDto, true, 0);
      checkCoaByRecursion(null, coaDto, false, 0);
    }

    /**
     * 删除要素检查
     */
    private void delElementCheck() {
      checkCoaByRecursion(null, coaDto, true, 0);
    }

    /**
     * 新增要素检查
     */
    private void addElementCheck() {
      checkCoaByRecursion(null, coaDto, false, 0);
    }

    /**
     * 递归校验coa级联异常
     * 
     * @param isUpTrace
     *            true-向上追溯 false-向下追溯
     */
    private void checkCoaByRecursion(FCoaDTO sourceCoa, FCoaDTO targetCoa, boolean isUpTrace, int relationNum) {
      final CoaRelation coaRelation = (CoaRelation) getCoaCascadeMap().get(targetCoa.getCoaId());
      if (coaRelation == null)
        return;

      final List relationCoa = coaRelation.getRelationCoaList();
      CoaObject coaObj = null;
      for (int i = relationNum; i < relationCoa.size(); i++) {
        coaObj = (CoaObject) relationCoa.get(i);
        if (coaObj.isUpStream() == isUpTrace) {
          if (compareItem.getOperateType() == CompareItem.ADD_ELEMENT) {// 新增要素
            FCoaDetail coaDetail = compareItem.getTargetCoaDetailDTO();
            FCoaDetail originDetail = getCoaDetailByEleSource(coaObj.getCoa(), coaDetail.getEleCode());
            if (!isUpTrace
              && (originDetail == null || (originDetail != null && isElementSlim(originDetail, coaDetail)))) {// 下游添加要素并且不存在添加要素
              returnMessage.append(coaObj.getCoa().getCoaCode()).append(" ").append(coaObj.getCoa().getCoaName());
              if (originDetail == null)
                returnMessage.append("需要添加要素");
              else
                returnMessage.append("需要修改要素");
              returnMessage.append(coaDetail.getEleCode()).append(" ").append(coaDetail.getEleName()).append("使用的级次是")
                .append(coaDetail.getLevelName()).append("\n");
              if (!isCheck)
                cascadeCoaContext.modifyCoaElement(coaObj.getCoa(), compareItem);
            }
          } else if (compareItem.getOperateType() == CompareItem.DEL_ELEMENT) {// 删除要素
            FCoaDetail coaDetail = compareItem.getOriginCoaDetailDTO();
            FCoaDetail originDetail = getCoaDetailByEleSource(coaObj.getCoa(), coaDetail.getEleCode());
            if (isUpTrace && originDetail != null) {// 上游减少要素并且存在删除要素
              returnMessage.append(coaObj.getCoa().getCoaCode()).append(" ").append(coaObj.getCoa().getCoaName())
                .append("需要减少要素").append(coaDetail.getEleCode()).append(" ").append(coaDetail.getEleName())
                .append("\n");
              if (!isCheck)
                cascadeCoaContext.modifyCoaElement(coaObj.getCoa(), compareItem);
            }
          } else {// 修改要素级次
            FCoaDetail coaDetail = getCoaDetailByEleSource(coaObj.getCoa(), compareItem.getEleSource());
            FCoaDetail targetCoaDetail = compareItem.getTargetCoaDetailDTO();
            if (coaDetail == null)
              continue;
            if (isUpTrace) {
              if (isElementWide(coaDetail, targetCoaDetail)) {
                returnMessage.append(coaObj.getCoa().getCoaCode()).append(" ").append(coaObj.getCoa().getCoaName())
                  .append("需要修改要素").append(coaDetail.getEleCode()).append(" ").append(targetCoaDetail.getEleName())
                  .append("的级次为").append(targetCoaDetail.getLevelName()).append("\n");
                if (!isCheck)
                  cascadeCoaContext.modifyCoaElement(coaObj.getCoa(), compareItem);
                // 下面是为了处理 分支的。 可执行指标和 计划额度/拨款单 的分支
                // 修改计划下游Coa变粗时，会向上产生影响，同时对这个上游的coa其所有下游coa都会产生影响
                if (coaRelation.isBranch() && sourceCoa != null) {
                  for (int j = 0; j < coaRelation.getRelationCoaList().size(); j++) {
                    CoaObject tmpObj = (CoaObject) coaRelation.getRelationCoaList().get(j);
                    if (!tmpObj.isUpStream() && !sourceCoa.getCoaId().equals(tmpObj.getCoaId()))
                      checkCoaByRecursion(sourceCoa, targetCoa, tmpObj.isUpStream(), j);
                  }
                }
              }
            } else {
              if (isElementSlim(coaDetail, targetCoaDetail)) {
                returnMessage.append(coaObj.getCoa().getCoaCode()).append(" ").append(coaObj.getCoa().getCoaName())
                  .append("需要修改要素").append(coaDetail.getEleCode()).append(" ").append(targetCoaDetail.getEleName())
                  .append("的级次为").append(targetCoaDetail.getLevelName()).append("\n");
                if (!isCheck)
                  cascadeCoaContext.modifyCoaElement(coaObj.getCoa(), compareItem);
              }
            }
          }
          checkCoaByRecursion(targetCoa, coaObj.getCoa(), isUpTrace, 0);
        }
      }
    }

    /**
     * 校验两个级次 是否coaDetail 比 targetCoaDetail 粗
     * 
     * @param coaDetail
     * @param targetCoaDetail
     * @return
     */
    private boolean isElementWide(FCoaDetail coaDetail, FCoaDetail targetCoaDetail) {
      return (targetCoaDetail.getLevelNum() < coaDetail.getLevelNum() || coaDetail.getLevelNum() == -1)
        && targetCoaDetail.getLevelNum() != -1;
    }

    /**
     * 校验两个级次 是否coaDetail 比 targetCoaDetail 细
     * 
     * @param coaDetail
     * @param targetCoaDetail
     * @return
     */
    private boolean isElementSlim(FCoaDetail coaDetail, FCoaDetail targetCoaDetail) {
      return (targetCoaDetail.getLevelNum() > 0 && coaDetail.getLevelNum() > 0 && targetCoaDetail.getLevelNum() > coaDetail
        .getLevelNum()) || (targetCoaDetail.getLevelNum() == -1 && coaDetail.getLevelNum() != -1);
    }

    private FCoaDetail getCoaDetailByEleSource(FCoaDTO coaDto, String eleSource) {
      final Iterator iterator = coaDto.getCoaDetailList().iterator();
      FCoaDetail coaDetail = null;
      while (iterator.hasNext()) {
        coaDetail = (FCoaDetail) iterator.next();
        if (coaDetail.getEleCode().equals(eleSource))
          return coaDetail;
      }
      return null;
    }
  }

}
