package stk;

import java.awt.MenuItem;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;
import stk.fun.Fun;
import stk.timebalance.TimeBalanceEngine;

/**
 * Loads settings from xml file
 * @author Sebastian Gruszka
 */
public class SettingsLoader {
    /** instance */
    private static SettingsLoader instance;
    
    /** Variables: */
    /** Look and feel */
    private String lnf = "javax.swing.plaf.metal.MetalLookAndFeel";
    //private String lnf = com.sun.java.swing.plaf.windows.WindowsLookAndFeel;
    /** Tray icon*/
    private String grayIcon = "/images/hum.gif";
    /** Module list */
    private List<Pluginable> moduleList;
    
    /** singleton */
    public static SettingsLoader getInstance() {
        if (instance == null){
            instance = new SettingsLoader();
        }
        return instance;
    }
    
    SettingsLoader(){
        
    }

    /**
     * Look and feel
     * @return the lnf
     */
    public String getLnf() {
        return lnf;
    }

    /**
     * Tray icon
     * @return the grayIcon
     */
    public String getGrayIcon() {
        return grayIcon;
    }


    /**
     * Module list
     * @return the moduleList
     */
    public List<Pluginable> getModuleList() {
        if (moduleList == null){
            moduleList = new ArrayList<Pluginable>();
            createModuleList();
        }
        
        return moduleList;
    }

    private void loadTestPluginsStubs() {
        moduleList.add(new Pluginable() {

            public MenuItem getNameOnList() {
                return new MenuItem("plugin 1");
            }

            public String getSettingsPanelTitle() {
                return null;
            }

            public JPanel getSettingsPanel() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            public void setToolTipObject(Tooltipable obj) {
                obj.setToolTip("pl1", "TXT", 0);
            }

            public void undoChanges() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            public void commitChanges() {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        });
        moduleList.add(new Pluginable() {

            public MenuItem getNameOnList() {
                return new MenuItem("plugin 2");
            }

            public String getSettingsPanelTitle() {
                return null;
            }

            public JPanel getSettingsPanel() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            public void setToolTipObject(Tooltipable obj) {
                obj.setToolTip("pl2", null, 0);
            }

            public void undoChanges() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            public void commitChanges() {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        });
    }

    private void createModuleList() {
        //loadTestPluginsStubs();
        //moduleList.add(Mp3Engine.getInstance());
        moduleList.add(TimeBalanceEngine.getInstance());
        moduleList.add(Fun.getInstance());
        //moduleList.add(ClockEngine.getInstance());
    }
}
