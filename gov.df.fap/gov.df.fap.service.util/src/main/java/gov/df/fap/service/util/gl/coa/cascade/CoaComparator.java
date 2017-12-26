package gov.df.fap.service.util.gl.coa.cascade;

import gov.df.fap.bean.gl.coa.FCoaDTO;
import gov.df.fap.bean.gl.coa.FCoaDetail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Coa配置对比器
 * 提供coa设置的比较方法，比较两个coa的区别
 * @author 
 * @version
 */
public class CoaComparator {
	
	/**
	 * 对比两个coa设置
	 * @param originCoaDto 修改前coa
	 * @param targetCoaDto 修改后coa
	 * @return CompareItem对象集合
	 */
	public static List compare(FCoaDTO originCoaDto, FCoaDTO targetCoaDto) {
		List compareItems = new ArrayList();
		
		Map originCoaMap = new HashMap();
		FCoaDetail coaDetail = null;
		for (int i=0;i<originCoaDto.getCoaDetailList().size();i++) {
			coaDetail = (FCoaDetail) originCoaDto.getCoaDetailList().get(i);
			originCoaMap.put(coaDetail.getEleCode(), coaDetail);
		}
		
		Map targetCoaMap = new HashMap();
		for (int i=0;i<targetCoaDto.getCoaDetailList().size();i++) {
			coaDetail = (FCoaDetail) targetCoaDto.getCoaDetailList().get(i);
			targetCoaMap.put(coaDetail.getEleCode(), coaDetail);
		}
		
		Iterator iterator = null;
		CompareItem compareItem = null;
		//原始coaDto和修改后的coaDto 要素个数不一样的情况
		if (originCoaMap.keySet().size() != targetCoaMap.keySet().size()) {
			//先以原始coaDto循环 遍历 查询是否有勾选掉的要素
			iterator = originCoaMap.keySet().iterator();
			while (iterator.hasNext()) {
				String eleSource = iterator.next().toString();
				if (!targetCoaMap.keySet().contains(eleSource)) {
					compareItem = new CompareItem(CompareItem.DEL_ELEMENT, eleSource, 
							(FCoaDetail) originCoaMap.get(eleSource), null);
					compareItems.add(compareItem);
				}
			}
			//如果删除的元素个数 等于 原始coaDto和修改后coaDto要素数量的查，则表示修改后的coa只是删除了要素
			//如果不一样 需要循环修改后的coaDto 判断是否有新增的要素
			if (originCoaMap.keySet().size() - targetCoaMap.keySet().size() != compareItems.size()) {
				iterator = targetCoaMap.keySet().iterator();
				while (iterator.hasNext()) {
					String eleSource = iterator.next().toString();
					if (!originCoaMap.keySet().contains(eleSource)) {
						compareItem = new CompareItem(CompareItem.ADD_ELEMENT, eleSource, 
								null, (FCoaDetail) targetCoaMap.get(eleSource));
						compareItems.add(compareItem);
					}
				}
			}
		}
		//后面还要判断是否修改了coa的级次
		iterator = originCoaMap.keySet().iterator();
		while (iterator.hasNext()) {
			String eleSource = iterator.next().toString();
			if (targetCoaMap.get(eleSource) == null)
				continue ;
			if (((FCoaDetail) originCoaMap.get(eleSource)).getLevelNum() 
					!= ((FCoaDetail) targetCoaMap.get(eleSource)).getLevelNum()) {
				compareItem = new CompareItem(CompareItem.MODIFY_ELEMENT, eleSource, 
						(FCoaDetail) originCoaMap.get(eleSource), (FCoaDetail) targetCoaMap.get(eleSource));
				compareItems.add(compareItem);
			}
		}
		
		return compareItems;
	}
	
}
