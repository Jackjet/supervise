import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.parsing.BeanComponentDefinition;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.xml.BeanDefinitionParserDelegate;
import org.springframework.beans.factory.xml.DefaultBeanDefinitionDocumentReader;
import org.w3c.dom.Element;

public class FaspBeanDefinitionDocumentReader extends DefaultBeanDefinitionDocumentReader {
  protected void processBeanDefinition(Element ele, BeanDefinitionParserDelegate delegate) {
    BeanDefinitionHolder bdHolder = delegate.parseBeanDefinitionElement(ele);
    if (bdHolder != null) {
      bdHolder = delegate.decorateBeanDefinitionIfRequired(ele, bdHolder);
      try {
        BeanDefinitionReaderUtils.registerBeanDefinition(bdHolder, getReaderContext().getRegistry());
      } catch (BeanDefinitionStoreException ex) {
        getReaderContext().error("Failed to register bean definition with name '" + bdHolder.getBeanName() + "'", ele,
          ex);
      }

      getReaderContext().fireComponentRegistered(new BeanComponentDefinition(bdHolder));
    }
  }
}
