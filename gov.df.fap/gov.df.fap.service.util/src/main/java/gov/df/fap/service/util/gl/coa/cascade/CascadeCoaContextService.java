package gov.df.fap.service.util.gl.coa.cascade;

import gov.df.fap.api.gl.coa.ICoaService;
import gov.df.fap.bean.gl.coa.FCoaDTO;
import gov.df.fap.bean.gl.coa.FCoaDetail;
import gov.df.fap.service.util.exceptions.gl.CoaCascadeException;
import gov.df.fap.service.util.memcache.MemCacheMap;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 级联修改的相关COA全局 将级联改动的Coa保存在此类
 * 
 * @author LiuYan
 * @version Mar 31, 2011
 */
@Service
public class CascadeCoaContextService {

  // public Map coaContext = new HashMap();
  public Map coaContext = MemCacheMap.getCacheMap(CascadeCoaContextService.class);

  @Autowired
  private ICoaService coaService = null;

  public void modifyCoaElement(FCoaDTO coaDto, CompareItem compareItem) {
    preparedCoa(coaDto);
    FCoaDTO targetCoa = (FCoaDTO) coaContext.get(coaDto.getCoaId());
    if (compareItem.getOriginCoaDetailDTO() == null) // coa增加了要素
      targetCoa.addCoaDetail((FCoaDetail) compareItem.getTargetCoaDetailDTO().clone());
    else if (compareItem.getTargetCoaDetailDTO() == null) // coa删除了要素
      removeCoaDetailByEleCode(targetCoa.getCoaDetail(), compareItem.getOriginCoaDetailDTO().getEleCode());
    else { // 修改了coa某些要素的级次
      final Iterator iterator = targetCoa.getCoaDetail().iterator();
      while (iterator.hasNext()) {
        FCoaDetail coaDetail = (FCoaDetail) iterator.next();
        if (coaDetail.getEleCode().equals(compareItem.getTargetCoaDetailDTO().getEleCode()))
          coaDetail.setLevelNum(compareItem.getTargetCoaDetailDTO().getLevelNum());
      }
    }
  }

  /**
   * 根据要素编码删除coa配置
   * 
   * @param coaDetails
   * @param eleCode
   */
  private void removeCoaDetailByEleCode(List coaDetails, String eleCode) {
    final Iterator iterator = coaDetails.iterator();
    FCoaDetail coaDetail = null;
    FCoaDetail findCoaDetail = null;
    while (iterator.hasNext()) {
      coaDetail = (FCoaDetail) iterator.next();
      if (coaDetail.getEleCode().equals(eleCode)) {
        findCoaDetail = coaDetail;
        break;
      }
    }
    if (findCoaDetail != null)
      coaDetails.remove(findCoaDetail);
  }

  private void preparedCoa(FCoaDTO coaDto) {
    if (coaContext.get(coaDto.getCoaId()) == null)
      coaContext.put(coaDto.getCoaId(), coaService.getCoa(Long.parseLong(coaDto.getCoaId())).clone());
  }

  /**
   * 获取所有级联修改的coa
   * 
   * @return
   */
  public List getCascadeModifyCoa() {
    return new ArrayList(coaContext.values());
  }

  /**
   * 清空级联coa缓存
   */
  public void clear() {
    coaContext.clear();
  }

  public void begin() {
    clear();
  }

  /**
   * 保存级联更新的coa
   * 
   * @throws Exception
   */
  public void commit() throws Exception {
    final Iterator iterator = coaContext.values().iterator();
    FCoaDTO coaDto = null;
    while (iterator.hasNext()) {
      coaDto = (FCoaDTO) iterator.next();
      if (coaService.isGenCcid(coaDto))
        throw new CoaCascadeException("要素级次设置" + coaDto.getCoaCode() + " " + coaDto.getCoaName() + "已经生成ccid不能修改!");
      coaService.updateCoaDto(coaDto, false);
    }
    clear();
  }
}
