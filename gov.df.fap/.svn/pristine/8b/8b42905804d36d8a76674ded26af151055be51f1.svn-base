import java.util.ArrayList;
import java.util.List;

public class AfterReflashEeventInit extends FaspDefaultReaderEventListener {
  private static List<IFaspAfertReflashEventListener> afel = new ArrayList();

  public Object createBean(String beanName, Object o) {
    if (o instanceof IFaspAfertReflashEventListener) {
      if (o instanceof FaspDefaultReaderEventListener) {
        return o;
      }
      afel.add((IFaspAfertReflashEventListener) o);
    }
    return o;
  }

  public void afertReflash() {
    for (IFaspAfertReflashEventListener o : afel)
      o.afertReflash();
  }
}