package gov.df.fap.service.util.signer;

import javax.security.cert.X509Certificate;

/**
 * 本地数据签名相关操作接口类
 * <p>
 * <p>
 * All rights reserved.
 * <p>
 * <p>
 * 未经本公司许可，不得以任何方式复制或使用本程序任何部分，
 * <p>
 * 侵权者将受到法律追究。
 * <p>
 * DERIVED FROM: NONE
 * <p>
 * PURPOSE:
 * <p>
 * DESCRIPTION:
 * <p>
 * UPDATE: 
 * <p>
 * DATE:  2017-04-01
 * <p>
 * HISTORY: 1.0
 * 
 * @version 1.0
 * @author lindx
 * @since java 1.6.0.34
 */
public interface ILocalSigner {

  /**
   * 弹出对话框，从证书列表中选择当前证书
   * 
   * @param selectModel
   *            0:表示不管系统中有几个证书都需要弹出窗口，1：表示如果系统只有一个证书，不需要弹出窗口，自动选中该证书。
   * @return 操作结果：0：操作失败 1：操作成功
   */
  public int selectCertificate(int selectModel);

  /**
   * 根据SN指定当前证书
   * 
   * @param sn
   *            证书SN序列号
   * @return 操作结果：0：操作失败 1：操作成功
   */
  public int setCertificateBySn(String sn);

  /**
   * 根据DN指定当前证书
   * 
   * @param dn
   *            证书DN值
   * @return 操作结果：0：操作失败 1：操作成功
   */
  public int setCertificateByDN(String dn);

  /**
   * 取得当前用户选择的证书
   * 
   * @return 当前用户选择的证书对象
   */
  public X509Certificate getCurrentCert();

  /**
   * 使用当前选择证书，对数据进行签名，返回签名结果
   * 
   * @param data
   *            待签名数据
   * @param iAlgorithm
   *            签名算法 0表示MD5, 1 表示SHA1
   * @return 签名结果 throws 异常错误 ：如证书不存在等异常
   */
  public byte[] signMessage(byte[] data, int iAlgorithm) throws Exception;

  /**
   * 使用当前选择证书，对文件进行签名，返回签名结果
   * 
   * @param signFile
   *            待签名的文件路径
   * @param iAlgorithm
   *            签名算法 0表示MD5, 1 表示SHA1
   * @return 签名结果
   * @throws 异常错误
   *             ：如文件不存在，证书不存在等异常
   */
  public byte[] signFile(String signFile, int iAlgorithm) throws Exception;

  /**
   * 对数据进行验签，返回验证结果
   * 
   * @param data
   *            待验证数据
   * @param signData
   *            签名信息
   * @param cert
   *            公钥证书
   * @return 0:验证结果错误 1：验证结果正确
   * @throws Exception
   */
  public int verifyMessage(byte[] data, byte[] signData, X509Certificate cert) throws Exception;

  /**
   * 对文件进行验签，返回验证结果
   * 
   * @param signFile
   *            待验证原文件
   * @param signData
   *            签名信息
   * @param cert
   *            公钥证书
   * @return 0:验证结果错误 1：验证结果正确
   * @throws Exception
   */
  public int verifyMessage(String signFile, byte[] signData, X509Certificate cert) throws Exception;

  /**
   * 使用当前选择证书，对数据进行PKCS7签名，返回签名结果
   * 
   * @param data
   *            待签名数据
   * @param iAlgorithm
   *            签名算法 0表示MD5, 1 表示SHA1
   * @return P7签名数据包
   */
  public byte[] signPKCS7(byte[] data, int iAlgorithm) throws Exception;

  /**
   * 对数据进行验证，返回验签结果
   * 
   * @param p7data
   *            PKCS7数据
   * @return 验签结果 0:数据错误 1：数据正确
   */
  public int verifyPKCS7(byte[] p7data) throws Exception;

  /**
   * 设置是否进行了unicode的转换
   * @param type 1 表示在签名前已经进行unicode的转换
   * @throws Exception
   */
  public void setcodetype(int type) throws Exception;

}
