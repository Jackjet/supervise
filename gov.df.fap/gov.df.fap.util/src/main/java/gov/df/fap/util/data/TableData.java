package gov.df.fap.util.data;

import gov.df.fap.util.xml.XMLData;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;

/**
 * <p>Title: 网络数据传递对象，普通显示用和传递用</p>
 * <p>Description:可以将要传递的Map和dto进行转换后传递可以直接使用</p>
 * 
 * 
 */
public class TableData implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6328745432907674768L;
	
	/**
	 * 标题数据用来记录，键名 
	 */
	private  Object[] titles = null;
	
	/**
	 * 数据实体存储用来存储数据各行数据是Object[]形式  
	 */
	private ArrayList dataList = new ArrayList();
	
	/**
	 * 存储原有数据对象的类名在传递之后恢复使用
	 */
	private Class clazz = null;
	/**
	 * 构造TableData对象，利于前后台数据传输。
	 * @param titles 每个对象包含的属性
	 */
	public TableData(Object[] titles){
		this(titles, null);
	}
	
	/**
	 * 构造TableData对象，利于前后台数据传输。
	 * @param titles 每个对象包含的属性
	 * @param clz 对象的类型
	 */
	public TableData(Object[] titles, Class clz){
//		if (titles == null){
//			throw new RuntimeException("构造TableData必须传入对象属性数组Object[]与对象类型Class!");
//		}
		
		this.titles = titles;
		this.clazz = clz == null ? XMLData.class : clz;
	}
	
	public void setClazz(Class clz){
		this.clazz = clz;
	}
	
	/**
	 * 列表中对象的属性
	 * @return
	 */
	public Class getClazz() {
		return clazz;
	}
	
	/**
	 * 取得表头字段
	 * @return
	 */
	public Object[] getTitle() {
		return titles;
	}
	
	/**
	 * 将保存的数据转换成XMLData的列表形式
	 * @return
	 */
	private List toMapList(){
		LinkedList link = new LinkedList();
		Iterator iterator = dataList.iterator();
		while(iterator.hasNext())
		{
			Object[] data =(Object[]) iterator.next();
			link.add(this.getDataMap(data));
		}
		return link;
	}
	
	/**
	 * 根据clazz记录的类信息将保存的数据信息
	 * @return
	 */
	public List toDataList(){
		if(Map.class.isAssignableFrom(clazz)){
			return toMapList();
		}
		
		LinkedList link = new LinkedList();
		Iterator iterator = dataList.iterator();
		while(iterator.hasNext())
		{
			Object[] data =(Object[]) iterator.next();
			Object oriData = null;
			try {
				oriData = this.clazz.newInstance();
				BeanUtils.populate(oriData, this.getDataMap(data));
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			link.add(oriData);
		}
		
		return link;
	}
	
	/**
	 * 将一个数组根据头表头信息组装成一个Map对象
	 * @param data
	 * @return
	 */
	private Map getDataMap(Object[] data){
		XMLData xmlData = new XMLData();
		for(int i=0,n=this.titles.length;i<n;i++)
		{
			if(data[i]!=null && !"".equals(data[i]))
			{
				xmlData.put(titles[i], data[i]);
			}
		}
		return xmlData;
	}
	/**
	 * 根据数据自动设置表头适应全字段传递情况
	 * @param map
	 */
		private void setTitleByMap(Map map) {
			this.titles = new String[map.size()];
			Iterator iterator = map.keySet().iterator();
			int i = 0;
			while (iterator.hasNext()) {
				titles[i] = iterator.next();
				i++;
			}
		}
		/**
		 * 根据数据自动设置ResultSet适应全数据集传递
		 * @param map
		 * @throws SQLException 
		 */
		private void setTitleByRs(ResultSet rs) throws SQLException {
			ResultSetMetaData rsmd = rs.getMetaData();
			this.titles = new String[rsmd.getColumnCount()];
			for(int i = 0,n=titles.length;i<n;i++)
			{
				titles[i]=rsmd.getColumnName(i).toLowerCase();
			}
		}
		
	/**
	 * 往列表中加入一个新的Map对象
	 * @param map
	 */
	public void add(Map map){
		if (titles==null)
		{
			this.setTitleByMap(map);
		}
		Object[] dataObj = new Object[titles.length];
		for(int i=0,n=titles.length;i<n;i++)
		{
			dataObj[i] = map.get(titles[i]);
		}
		dataList.add(dataObj);
	}
	
	/**
	 * 往列表中加入一个普通对象
	 * @param dto
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 */
	public void add(Object dto) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException{
		if(dto instanceof Map)
		{
			add((Map)dto);
			return;
		}
		//TODO 会在内存中多增加一个Map对象，是否可以直接进行转换？
		add(BeanUtils.describe(dto));
	}
	/**
	 * 往列表中加入一个查询的数据集
	 * @param dto
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 * @throws SQLException 
	 */
	public void addResult(ResultSet rs) throws SQLException{
		if (titles==null)
		{
			this.setTitleByRs(rs);
		}
		int n = titles.length;
		while(rs.next()){
			Object[] dataObj = new Object[n];
			for(int i = 0;i<n;i++)
			{
				try {
					dataObj[i]=rs.getObject((String) titles[i]);
					//屏蔽部分没有的字段
				} catch (SQLException e) {

				}
			}						
			dataList.add(dataObj);
		}
	}
		
	/**
	 * 往列表中增加Map列表。
	 * @param dataList
	 */
	public void addDataByMapList(List dataList){
		
		Iterator iterator = dataList.iterator();
		while(iterator.hasNext())
		{
			add((Map)(iterator.next()));
		}
	}
	
	/**
	 * 往列表中增加普通对象列表。
	 * @param dataList
	 */
	public void addDataByDataList(List dataList)
	{
		Iterator iterator = dataList.iterator();
		if(dataList.size()>0)
		{
			this.clazz = dataList.get(0).getClass();
		}
		while(iterator.hasNext())
		{
			try {
				add(iterator.next());
			} catch (Exception e) {
				throw new RuntimeException(e);
			} 
		}
		
	}
}
