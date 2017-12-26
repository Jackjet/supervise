/*
 * <p>Copyright 2000 by Spring 1st Software corporation,
 * <p>All rights reserved.
 * <p>版权所有：用友政务软件有限公司
 * <p>未经本公司许可，不得以任何方式复制或使用本程序任何部分，
 * <p>侵权者将受到法律追究。
 */
package gov.df.fap.service.gl.balance;

import gov.df.fap.bean.gl.dto.CtrlRecordDTO;
import gov.df.fap.service.util.gl.balance.BalanceUtil;
import gov.df.fap.service.util.memcache.MemCacheMap;
import gov.df.fap.util.StringUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 一个额度列表,封装了一些额度的特性.
 * 
 * @author 
 * @version 
 */

public class BalanceList implements List {

	protected static final Log logger = LogFactory.getLog(BalanceList.class);

	private List delegate = null;
	private Map containerMap = MemCacheMap.getCacheMap(BalanceList.class);
	private List accountIdList = null;

	public BalanceList(List delegate) {
		this.delegate = delegate;
		accountIdList = new ArrayList();
	}

	public List getAccountIdList() {
		return accountIdList;
	}

	public void add(int index, Object element) {
		addBalance(index, element);
	}

	public CtrlRecordDTO addBalance(Object element) {
		return addBalance(delegate.size(), element);
	}

	/**
	 * 如果是新加入,则返回加入的对象,如果额度已存在,返回已存在额度
	 * 
	 * @param index
	 * @param element
	 * @return
	 */
	public CtrlRecordDTO addBalance(int index, Object element) {
		if (element == null)
			throw new NullPointerException(
					"can not put Null CtrlRecordDTO into BalanceList!");
		if (!(element instanceof CtrlRecordDTO))
			throw new IllegalArgumentException(
					"Only Supports setting CtrlRecordDTO into BalanceList only!");

		CtrlRecordDTO inputOne = (CtrlRecordDTO) element;
		Object key = new CtrlRecordKey(inputOne);
		CtrlRecordDTO existsOne = (CtrlRecordDTO) containerMap.get(key);
		if (existsOne == null) {
			containerMap.put(key, inputOne);
			delegate.add(index, inputOne);
			inputOne.setIndex(index);
			if (!accountIdList.contains(inputOne.getAccount_id()))
				accountIdList.add(inputOne.getAccount_id());
			return inputOne;
		} else {
			if (logger.isDebugEnabled()) {
				logger.debug("汇总临时额度" + inputOne + " 到" + existsOne);
			}
			BalanceUtil.plus(existsOne, inputOne);
			return existsOne;
		}
	}

	/**
	 * 如果新建返回ture, 如果直接累加,返回false
	 */
	public boolean add(Object element) {
		addBalance(delegate.size(), element);
		return true;
	}

	public boolean addAll(Collection c) {
		final Iterator iterator = c.iterator();
		while (iterator.hasNext())
			this.add(iterator.next());
		return true;
	}

	public boolean addAll(int index, Collection c) {
		throw new UnsupportedOperationException();
	}

	public void clear() {
		delegate.clear();
		containerMap.clear();
	}

	public boolean contains(Object o) {
		return containerMap.containsValue(o) || delegate.contains(o);
	}

	public boolean containsAll(Collection c) {
		throw new UnsupportedOperationException();
	}

	public boolean equals(Object o) {
		throw new UnsupportedOperationException();
	}

	public Object get(int index) {
		return delegate.get(index);
	}

	public CtrlRecordDTO getBalance(int index) {
		return (CtrlRecordDTO) get(index);
	}

	public int hashCode() {
		return delegate.hashCode();
	}

	public int indexOf(Object o) {
		return delegate.indexOf(o);
	}

	public boolean isEmpty() {
		return delegate.isEmpty();
	}

	public Iterator iterator() {
		return delegate.iterator();
	}

	public int lastIndexOf(Object o) {
		return delegate.lastIndexOf(o);
	}

	public ListIterator listIterator() {
		return delegate.listIterator();
	}

	public ListIterator listIterator(int index) {
		return delegate.listIterator(index);
	}

	public Object remove(int index) {
		throw new UnsupportedOperationException();
	}

	public boolean remove(Object o) {
		throw new UnsupportedOperationException();
	}

	public boolean removeAll(Collection c) {
		throw new UnsupportedOperationException();
	}

	public boolean retainAll(Collection c) {
		return delegate.retainAll(c);
	}

	public Object set(int index, Object element) {
		return delegate.set(index, element);
	}

	public int size() {
		return delegate.size();
	}

	public List subList(int fromIndex, int toIndex) {
		return delegate.subList(fromIndex, toIndex);
	}

	public Object[] toArray() {
		return delegate.toArray();
	}

	public Object[] toArray(Object[] a) {
		return delegate.toArray(a);
	}

	class CtrlRecordKey {
		private String accountId = null;
		private long ccid = 0;
		private int setMonth = 0;
		private int setYear = 0;
		private String rgCode = StringUtil.EMPTY;

		CtrlRecordKey(CtrlRecordDTO ctrl) {
			this(ctrl.getAccount_id(), ctrl.getCcid(), ctrl.getSet_month(),
					ctrl.getSet_year(), ctrl.getRg_code());
		}

		CtrlRecordKey(String accountId, long ccid, int setMonth, int setYear,
				String rgCode) {
			this.accountId = accountId;
			this.ccid = ccid;
			this.setMonth = setMonth;
			this.setYear = setYear;
			this.rgCode = rgCode;
		}

		public boolean equals(Object o) {
			if (o == null)
				return false;

			if (o instanceof CtrlRecordKey) {
				CtrlRecordKey t = (CtrlRecordKey) o;
				return t.accountId.equals(accountId) && t.ccid == ccid
						&& t.setMonth == setMonth && t.setYear == setYear
						&& t.rgCode.equals(rgCode);
			}
			return false;
		}

		public int hashCode() {
			return (accountId + ccid + setMonth + setYear + rgCode).hashCode();
		}
	}
}
