import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.support.CglibSubclassingInstantiationStrategy;
import org.springframework.beans.factory.support.RootBeanDefinition;

public class FaspInstantiationStrategy extends CglibSubclassingInstantiationStrategy {
  public Object instantiate(RootBeanDefinition beanDefinition, String beanName, BeanFactory owner) {
    Object o = super.instantiate(beanDefinition, beanName, owner);
    o = DFInitClasspathXmlApplicationContext.getThis().getListener().createBean(beanName, o);
    return o;
  }

  public Object instantiate(RootBeanDefinition beanDefinition, String beanName, BeanFactory owner, Constructor ctor,
    Object[] args) {
    Object o = super.instantiate(beanDefinition, beanName, owner, ctor, args);
    DFInitClasspathXmlApplicationContext.getThis().getListener().createBean(beanName, o);
    return o;
  }

  public Object instantiate(RootBeanDefinition beanDefinition, String beanName, BeanFactory owner, Object factoryBean,
    Method factoryMethod, Object[] args) {
    Object o = super.instantiate(beanDefinition, beanName, owner, factoryBean, factoryMethod, args);
    DFInitClasspathXmlApplicationContext.getThis().getListener().createBean(beanName, o);
    return o;
  }
}