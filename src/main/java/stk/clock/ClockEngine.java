package stk.clock;

import java.awt.CheckboxMenuItem;
import java.awt.Menu;
import java.awt.MenuItem;
import javax.swing.JPanel;
import stk.Pluginable;
import stk.Tooltipable;

/**
 *
 * @author Sebastian Gruszka
 */
public class ClockEngine implements Pluginable{
    /** instance */
    private static ClockEngine instance;
    /** clock menu */
    private Menu clockMenu;

    /**
     * instance
     * @return the instance
     */
    public static ClockEngine getInstance() {
        if (instance == null){
            instance = new ClockEngine();
        }
        return instance;
    }
    
    
    ClockEngine(){
    //clock
        clockMenu = new Menu("Clock");
        CheckboxMenuItem showClockItem = new CheckboxMenuItem("Show clock");
        CheckboxMenuItem transparentClockItem = new CheckboxMenuItem("Transparent");
        CheckboxMenuItem alwaysOnTopClockItem = new CheckboxMenuItem("Always on top");
        clockMenu.add(showClockItem);
        clockMenu.add(transparentClockItem);
        clockMenu.add(alwaysOnTopClockItem);
    }
    
    public MenuItem getNameOnList() {
        return clockMenu;
    }

    public String getSettingsPanelTitle() {
        return null;
    }

    public JPanel getSettingsPanel() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setToolTipObject(Tooltipable obj) {
        obj.setToolTip("clock", "CLK", 0);
    }

    public void undoChanges() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void commitChanges() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
