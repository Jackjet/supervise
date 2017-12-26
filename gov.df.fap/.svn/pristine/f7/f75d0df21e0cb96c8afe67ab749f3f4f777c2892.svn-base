import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.InstantiationStrategy;

public class FaspDefaultListableBeanFactory extends DefaultListableBeanFactory {
  private final InstantiationStrategy instantiationStrategy = new FaspInstantiationStrategy();

  protected InstantiationStrategy getInstantiationStrategy() {
    return this.instantiationStrategy;
  }

  public FaspDefaultListableBeanFactory(BeanFactory parentBeanFactory) {
    super(parentBeanFactory);
  }
}
