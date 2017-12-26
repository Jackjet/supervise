import org.springframework.beans.factory.parsing.AliasDefinition;
import org.springframework.beans.factory.parsing.ComponentDefinition;
import org.springframework.beans.factory.parsing.DefaultsDefinition;
import org.springframework.beans.factory.parsing.ImportDefinition;

public class FaspDefaultReaderEventListener implements IFaspReaderEventListener {
  public Object createBean(String beanName, Object o) {
    return o;
  }

  public void aliasRegistered(AliasDefinition aliasDefinition) {
  }

  public void componentRegistered(ComponentDefinition componentDefinition) {
  }

  public void defaultsRegistered(DefaultsDefinition defaultsDefinition) {
  }

  public void importProcessed(ImportDefinition importDefinition) {
  }

  public void afertReflash() {
  }
}