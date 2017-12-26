import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.parsing.AliasDefinition;
import org.springframework.beans.factory.parsing.ComponentDefinition;
import org.springframework.beans.factory.parsing.DefaultsDefinition;
import org.springframework.beans.factory.parsing.ImportDefinition;

public class FaspReaderEventListener implements IFaspReaderEventListener {
  private final ArrayList<IFaspReaderEventListener> listener = new ArrayList();

  private static final Logger logger = Logger.getLogger(FaspReaderEventListener.class);

  public void addListener(IFaspReaderEventListener l) {
    this.listener.add(l);
  }

  public void aliasRegistered(AliasDefinition aliasDefinition) {
    for (IFaspReaderEventListener l : this.listener)
      l.aliasRegistered(aliasDefinition);
  }

  public void componentRegistered(ComponentDefinition componentDefinition) {
    for (IFaspReaderEventListener l : this.listener)
      l.componentRegistered(componentDefinition);
  }

  public void defaultsRegistered(DefaultsDefinition defaultsDefinition) {
    for (IFaspReaderEventListener l : this.listener)
      l.defaultsRegistered(defaultsDefinition);
  }

  public void importProcessed(ImportDefinition importDefinition) {
    for (IFaspReaderEventListener l : this.listener)
      l.importProcessed(importDefinition);
  }

  public Object createBean(String beanName, Object o) {
    for (IFaspReaderEventListener l : this.listener) {
      o = l.createBean(beanName, o);
    }
    return o;
  }

  public void clear() {
    this.listener.clear();
  }

  public void afertReflash() {
    for (IFaspReaderEventListener l : this.listener)
      l.afertReflash();
  }
}
