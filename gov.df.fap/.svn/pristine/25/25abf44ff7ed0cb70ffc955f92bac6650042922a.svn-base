package gov.df.fap.bean.workflow;

import gov.df.fap.util.date.DateHandler;
import gov.df.fap.util.xml.XMLData;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class FAttachmentDTO implements java.io.Serializable{

	public static final long serialVersionUID = -1L;
	
	public FAttachmentDTO() {
		super();
	}
	
	/**
	 * 附件Id
	 */
	private String attm_id ;
	private String file_name;
	private String time_path; 

	/**
	 * 文件名称与字节实体对应
	 */
	private Map map = new HashMap();
	
	/**
	 * 文件名称分隔符
	 */
	public static final String SEPARATOR = ";" ;
	/**
	 * XMLData中存放文件名称的Key
	 */
	public static final String FILE_NAME_KEY = "ob_name";
	/**
	 * XMLData中存放文件类型的Key
	 */
	public static final String FILE_TYPE_KEY = "ob_type";
	/**
	 * XMLData中存放文件的Key
	 */
	public static final String FILE_BYTES_KEY = "ob";
	/**
	 * XMLData中存放文件路径的Key（不包括根路径部分）
	 */
	public static final String FILE_PATH_KEY = "ob_path";

	/**
	 * 消息附件的根目录
	 */
	public static final String MESSAGE_ROOT_PATH = "UFMSGATTACHMENT";
	
	/**
	 * 获取文件名称的组合字符串，会以分号进行分割
	 * @return
	 */
	public String getFileNameCombination(){
		//一次上传的附件应该不会太多，先这样实时获取吧
		StringBuffer sb = new StringBuffer();
		String[] fns = keys();
		for (int i = 0; i < fns.length; i++) {
			sb.append(fns[i]).append(SEPARATOR);
		}
		return sb.toString() ;
	}

	/**
	 * 获取上传文件的形式
	 * 根据存放实体实时获得文件名称
	 * List里面存XMLData
	 * @return
	 */
	public List getUploadList(){
		List list = new ArrayList();
		String[] ks = keys();
		XMLData data = null;
		for (int i = 0; i < ks.length; i++) {
			data = new XMLData();
			data.put(FILE_NAME_KEY , ks[i]);//文件名
			data.put(FILE_BYTES_KEY , get(ks[i]));//文件实体
//			文件路径,形式：消息根目录/时间/附件ID/
			data.put(FILE_PATH_KEY , MESSAGE_ROOT_PATH + File.separator + time_path + 
					File.separator + attm_id + File.separator );
			list.add(data);
		}
		return list ;
	}
	
	/**
	 * 获取上传文件的形式
	 * 分解file_name获得文件名称
	 * List里面存XMLData
	 * @return
	 */
	public List getDownloadList(){
		List list = new ArrayList();
		XMLData data = null;
		if( file_name!= null && !"".equals(file_name)){
			String[] ks = file_name.split(SEPARATOR);
			if( ks != null && ks.length > 0){
				for (int i = 0; i < ks.length; i++) {
					data = new XMLData();
					data.put(FILE_NAME_KEY , ks[i]);//文件名
	//				文件路径,形式：消息根目录/时间/附件ID/
					data.put(FILE_PATH_KEY , MESSAGE_ROOT_PATH + File.separator + time_path + 
							File.separator + attm_id + File.separator );
					list.add(data);
				}
			}
		}
		return list ;
	}
	
	/**
	 * 获取值的集合
	 * @return
	 */
	public List valueList(){
		List list = new ArrayList();
		Iterator it = map.values().iterator();
		while( it.hasNext() ){
			list.add(it.next());
		}
		return list;
	}
	
	/**
	 * 获取值的二维数组
	 * @return
	 */
	public byte[][] values(){
		List list = valueList();
		byte[][] bs = new byte[list.size()][];
		for (int i = 0; i < bs.length; i++) {
			bs[i] = (byte[])list.get(i);
		}
		return bs;
	}
	
	/**
	 * 用文件名作为Key，存放实体
	 * 注： 暂时不支持一次上传两个同名文件，即使是在不同的路径下
	 * 就是为了这里限制住，才不使用继承hashmap，导致要写N多代码，大悲剧啊
	 * @param fileName 文件名
	 * @param content 文件实体字节组
	 */
	public void put(String fileName , byte[] content){
		map.put( fileName , content);
	}
	
	/**
	 * 没啥可说的，就是获取呗
	 * @param key
	 */
	public byte[] get(String key){
		return ( byte[] )map.get(key);
	}
	public void remove(String key){
		map.remove(key);
	}
	
	public boolean containsKey(String key){
		return map.containsKey(key);
	}
	public boolean containsValue(byte[] value){
		return map.containsValue(value);
	}
	/**
	 * 判断是否有文件
	 * @return
	 */
	public boolean isEmpty(){
		return map.isEmpty();
	}
	/**
	 * 傻眼了吧，名字不一样了
	 * @return
	 */
	public String[] keys(){
		Set set = map.keySet();
		String[] ks = new String[set.size()];
		//set.toArray(ks);允许放，但是给的是Object，还是自己写吧
		Iterator it=set.iterator();
		for (int i = 0; i < ks.length; i++) {
			ks[i] = (String) it.next();
		}
		return ks ;
	}
	/**
	 * 清空所有文件名称和文件字节的全部对应
	 *
	 */
	public void clear(){
		map.clear();
	}
	
	public String getAttm_id() {
		return attm_id;
	}

	public void setAttm_id(String attm_id) {
		this.attm_id = attm_id;
	}
	public Map getMap() {
		return map;
	}

	public String getTime_path() {
		if( time_path == null ){
			time_path = DateHandler.getDateFromNow( 0 , "yyyyMMdd");
		}
		return time_path;
	}

	public void setTime_path(String time_path) {
		this.time_path = time_path;
	}

	/**
	 * 注：该字段记录的文件名称未必是和map里的实时对应
	 * 正因为有不对应的需要，才有了该字段，
	 * 该方法仅仅是取出放进去的值
	 * @return
	 */
	public String getFile_name() {
		return file_name;
	}

	public void setFile_name(String file_name) {
		this.file_name = file_name;
	}

}
