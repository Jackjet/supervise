package gov.df.fap.service.gl.balance;

import gov.df.fap.bean.gl.dto.CtrlRecordDTO;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.IdentityMap;

/**
 * 
 * @author
 * @version 2017-04-12
 */
public class BalanceGenResult {

  /**额度对象列表*/
  private List balanceList;

  /**追溯对象列表*/
  private List balanceTracerList;

  /**是否已汇总*/
  boolean isMerge = false;

  /**
   * 初始化时不对额度进行汇总.
   *
   */
  public BalanceGenResult() {
    //this.balanceList=new BalanceList(new ArrayList());
    this.balanceTracerList = new ArrayList();
    this.balanceList = new ArrayList();
  }

  public BalanceGenResult(BalanceList balanceList, LinkedList balanceTracerList) {
    this.balanceList = balanceList;
    this.balanceTracerList = balanceTracerList;
  }

  public CtrlRecordDTO addBalance(CtrlRecordDTO balance) {
    if (balance != null) {
      if (balanceList instanceof BalanceList)
        return ((BalanceList) balanceList).addBalance(balance);
      else
        balanceList.add(balance);
    }
    return balance;
  }

  public void addTracer(BalanceTracer tracer) {
    if (tracer != null)
      this.balanceTracerList.add(tracer);
  }

  public List getBalanceTracerList() {
    return balanceTracerList;
  }

  public void setBalanceTracerList(List balanceTracerList) {
    this.balanceTracerList = balanceTracerList;
  }

  public BalanceList getBalanceList() {
    if (!isMerge) {
      this.balanceList = mergeBalance();
      isMerge = true;
    }
    return (BalanceList) balanceList;
  }

  public void setBalanceList(BalanceList balanceList) {
    this.balanceList = balanceList;
  }

  /**
   * 对临时额度进行汇总.
   *
   */
  public BalanceList mergeBalance() {
    /*
     * 为重新指向追溯对象的临时额度对象作准备.
     */
    Map tracerMap = new IdentityMap();
    for (int i = 0; i < balanceTracerList.size(); i++) {
      final BalanceTracer t = (BalanceTracer) balanceTracerList.get(i);
      if (t.getBalance() == null || tracerMap.containsKey(t.getBalance()))
        throw new RuntimeException("临时额度数据生成异常,额度汇总失败!");
      tracerMap.put(t.getBalance(), t);
    }

    /*
     * 因为生成临时额度时,临时额度对象与额度追溯对象的关系是1:1,所以可以通过这个方式去重指向
     * 追溯对象中对临时额度的引用为汇总额度.
     */
    BalanceList sumBalanceList = new BalanceList(new ArrayList());
    for (int i = 0; i < balanceList.size(); i++) {
      final CtrlRecordDTO balance = (CtrlRecordDTO) balanceList.get(i);
      final CtrlRecordDTO sumBalance = sumBalanceList.addBalance(balance);
      if (tracerMap.containsKey(balance)) {
        final BalanceTracer t = (BalanceTracer) tracerMap.get(balance);
        t.setBalance(sumBalance);
      }
    }

    return sumBalanceList;
  }
}
