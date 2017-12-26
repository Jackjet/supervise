package gov.df.fap.util.where;

import java.io.Serializable;

/**
* <p>Title:where子句值对象，内部类，用于WhereBean</p>
* <p>Description: </p>
* <p>Copyright: Copyright (c) 2006</p>
* <p>Company: 北京用友政务软件有限公司</p>
* <p>CreateData 2006-6-10</p>
* @author Tim
* @see gov.df.fap.util.where.WhereBean
* @version 1.0
*/
public class WhereBean implements Serializable {
  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  private boolean bracketFront = false;

  private String relation;

  private String key;

  private String operation;

  private String value;

  private boolean bracketBack = false;

  private boolean onlyBracket;

  public WhereBean() {

  }

  /**
   * @param key
   *            The key to set.
   */
  public void setKey(String key) {
    this.key = key;
  }

  /**
   * @return Returns the key.
   */
  public String getKey() {
    return key;
  }

  /**
   * @param operation
   *            The operation to set.
   */
  public void setOperation(String operation) {
    this.operation = operation;
  }

  /**
   * @return Returns the operation.
   */
  public String getOperation() {
    return operation;
  }

  /**
   * @param value
   *            The value to set.
   */
  public void setValue(String value) {
    this.value = value;
  }

  /**
   * @return Returns the value.
   */
  public String getValue() {
    return value;
  }

  /**
   * @param bracketFront
   *            The bracketFront to set.
   */
  public void setBracketFront(boolean bracketFront) {
    this.bracketFront = bracketFront;
  }

  /**
   * @return Returns the bracketFront.
   */
  public boolean isBracketFront() {
    return bracketFront;
  }

  /**
   * @param relation
   *            The relation to set.
   */
  public void setRelation(String relation) {
    this.relation = relation;
  }

  /**
   * @return Returns the relation.
   */
  public String getRelation() {
    return relation;
  }

  /**
   * @param bracketBack
   *            The bracketBack to set.
   */
  public void setBracketBack(boolean bracketBack) {
    this.bracketBack = bracketBack;
  }

  /**
   * @return Returns the bracketBack.
   */
  public boolean isBracketBack() {
    return bracketBack;
  }

  /**
   * @param onlyBracket
   *            The onlyBracket to set.
   */
  public void setOnlyBracket(boolean onlyBracket) {
    this.onlyBracket = onlyBracket;
  }

  /**
   * @return Returns the onlyBracket.
   */
  public boolean isOnlyBracket() {
    return onlyBracket;
  }
}
