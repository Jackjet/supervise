import org.springframework.beans.factory.parsing.ReaderEventListener;

public abstract interface IFaspReaderEventListener extends ReaderEventListener, IFaspAfertReflashEventListener {
  public abstract Object createBean(String paramString, Object paramObject);
}
