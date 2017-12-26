package gov.df.fap.api.dictionary.interfaces;

import java.util.List;

/**
 * 基础数据变化联动更新的监听接口。
 * 通过实现此接口的修改、删除基础数据两个方法并调用IDictionary的addEleChangeListener方法添加到类中，
 * 达到基础数据删除或者删除时进行的联动更新。
 * @author justin
 *
 */
public interface EleChangeListener {
	
	/**
	 * 基础数据在修改后需要进行的联动修改
	 * @param element 要素别名，如BS，EN
	 * @param id 修改要素id 
	 * @param oldCode 要素修改前的code
	 * @param newCode 修改后的编码
	 * @param newName 修改后的名称
	 * @throws Exception 更新失败后异常提示
	 */
	public void updateEleAction(String element,String id, String oldCode,String oldName, String newCode,String newName) throws Exception;
	
	/**
	 * 基础数据在删除后需要进行的联动删除
	 * @param delEle 删除的要素集合，对象是FElementDTO
	 * @throws Exception 删除失败后异常提示
	 */
	public void deleteEleAction(List delEle) throws Exception;
	
	/**
	 * 校验基础数据是否可以删除
	 * @param element 要素别名，如BS，EN
	 * @param id 删除要素id
	 * @param code 删除要素编码
	 * @throws Exception 不允许删除的信息提示
	 */
	public boolean isEleCanDelete(String element,String id,String code) throws Exception;
	
	/**
	 * 获取修改时需要联动的配置信息
	 * @return 字符串列表，list中是String类型的对象，统一通过调用Object.toString()获取信息。
	 */
	public List getChangeSet();
	
	/**
	 * 获取修改时需要联动的配置信息
	 * @return 字符串列表，字符串列表，list中是String类型的对象，统一通过调用Object.toString()获取信息。
	 */
	public List getDelSet();
	
}
