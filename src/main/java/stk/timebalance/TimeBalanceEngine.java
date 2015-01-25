package stk.timebalance;

import java.awt.MenuItem;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.swing.JPanel;
import stk.Pluginable;
import stk.Tooltipable;


/**
 *
 * @author user
 */
public class TimeBalanceEngine implements Pluginable {

    private static TimeBalanceEngine instance;
    private TBSettingsJPanel settingsPanel;
        /**String that will be published in tray icon*/
        private String publish = "--NO-DATA-YET--";


    public static Pluginable getInstance() {
        if (instance == null) {
            instance = new TimeBalanceEngine();
        }
        return instance;
    }
    /** object with publish info */
    private Tooltipable toolTipObj;
    /** main computing here */
    private TimeBalanceEngineHelper timeBalanceEngineHelper;
    
    Runnable backgroundUpdater;

    TimeBalanceEngine() {
        settingsPanel = new TBSettingsJPanel();
        timeBalanceEngineHelper = new TimeBalanceEngineHelper();
        backgroundUpdater = new BackgroundUpdater(this);
        //run once to update data
        backgroundUpdater.run();
        ScheduledExecutorService service = Executors.newScheduledThreadPool(1);
        service.scheduleAtFixedRate(backgroundUpdater, 0, 60, TimeUnit.SECONDS);
    }



    public MenuItem getNameOnList() {
        return new MenuItem("TimeBalance");
    }

    public String getSettingsPanelTitle() {
        return "Time balance";
    }

    public JPanel getSettingsPanel() {
        return settingsPanel;
    }

    public void setToolTipObject(Tooltipable obj) {
        toolTipObj = obj;
        toolTipObj.setToolTip("timebalance", publish, 0);
    }

    public void undoChanges() {
        settingsPanel.undoChanges();
    }

    public void commitChanges() {
        settingsPanel.commitChanges();
        //run once to reload data
        backgroundUpdater.run();
    }

    public void publish(String publishText) {
        if (toolTipObj != null) {
            publish = publishText;
            toolTipObj.setToolTip("timebalance", publishText, 0);
        }
    }

    public static String milsToHuman(long mils) {
        //seconds = (A / 1000) % 60
        long minutes = (mils / (1000 * 60)) % 60;
        long hours = (mils / (1000 * 60)) / 60;
        //long hours = (mils / (1000 * 60 * 60)) % 24;
        //long days = (mils / (1000 * 60 * 60)) / 24;
        //return ((days > 0) ? days + "d" : "") + String.format("%02dh%02dm", hours, minutes);
        return String.format("%dh%02dm", hours, minutes);
    }

    /**
     * @return the timeBalanceEngineHelper
     */
    public TimeBalanceEngineHelper getTimeBalanceEngineHelper() {
        return timeBalanceEngineHelper;
    }
}
