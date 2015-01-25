package stk;

import java.awt.MenuItem;
import javax.swing.JPanel;

/**
 *
 * @author Sebastian Gruszka
 */
public interface Pluginable {
    /**Name that is displayed on list */
    public MenuItem getNameOnList();
    /** Panel settings title, if you set this value equal null there is no 
     * settings panel, and getSettingsPanel(), undoChanges(), commitChanges()*/
    public String getSettingsPanelTitle();
    /** Panel with settings */
    public JPanel getSettingsPanel();
    /** Tool tip */
    public void setToolTipObject(Tooltipable obj);
    /**Undo changes on setting panel */
    public void undoChanges();
    /**Commit changes for panel with settings*/
    public void commitChanges();
        
}
