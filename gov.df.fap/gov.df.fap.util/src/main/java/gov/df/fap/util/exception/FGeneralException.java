package gov.df.fap.util.exception;

import java.io.Serializable;

public class FGeneralException extends Exception implements Serializable {

  private static final long serialVersionUID = 1L;

  private String code;

  private String message;

  private IFException IException;

  public String getCode() {

    return code;

  }

  public void setCode(String code) {

    this.code = code;

  }

  public String getMessage() {

    if (this.message == null) {

      this.message = super.getMessage();

    }

    return message;

  }

  public void setMessage(String message) {

    this.message = message;

  }

  public FGeneralException() {
  }

  public FGeneralException(String code) {

    if (code == null || code.equals("")) {

      this.message = "异常code不能为空";

    } else {

      setCode(code);

    }

  }

  public FGeneralException(String code, String message) {

    if (code != null) {

      this.code = code;

    }

    if (message != null) {

      this.message = message;

    }

  }

  public FGeneralException(Exception e) {
    super(e);
  }

  public FGeneralException(String message, Exception e) {
    super(message, e);
  }

  public String toString() {
    if (code == null || "".equals(code))
      return "FAppException [message: " + this.message + " ]";
    else
      return "FAppException [code: " + this.code + " message: " + this.message + " ]";

  }

}
