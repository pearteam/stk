package stk;

/**
 *
 * @author Sebastian Gruszka
 */
public interface Tooltipable {
    /**used to set informations displayed above try icon*/
    public void setToolTip(String name, String txt, int pos);
    /**used to store varius objects */
    public void setProperty(String key, Object obj);
    /**used to get stored objects*/
    public Object getProperty(String key);
}
