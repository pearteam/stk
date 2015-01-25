package stk.mp3;

import java.awt.Menu;
import java.awt.MenuItem;
import javax.swing.JPanel;
import stk.Pluginable;
import stk.Tooltipable;

/**
 *
 * @author Sebastian Gruszka
 */
public class Mp3Engine implements Pluginable{
    
    /** instance */
    private static Mp3Engine instance;
    /** plugin menu */
    private Menu mp3Menu;
    /** settings panel */
    private Mp3SettingsJPanel settingsPanel;

    public static Mp3Engine getInstance() {
        if (instance == null){
            instance = new Mp3Engine();
        }
        return instance;
    }
    
    /** constructor */
    Mp3Engine(){
        mp3Menu = new Menu("MP3");
        MenuItem mp3VoteItem = new MenuItem("Vote");
        MenuItem mp3VoteCItem = new MenuItem("Vote & comment");
        MenuItem mp3StatisticsItem = new MenuItem("Statistics");
        MenuItem mp3SettingsItem = new MenuItem("Settings");
        mp3Menu.add(mp3VoteItem);
        mp3Menu.add(mp3VoteCItem);
        mp3Menu.add(mp3StatisticsItem);
        mp3Menu.add(mp3SettingsItem);
        
        settingsPanel = new Mp3SettingsJPanel();
    }
    /**
     * Vote for current song.
     */
    public static void doVote() {
    }

    /**
     * Vote for current song.
     */
    public static void doVoteAndComment() {
    }

     /**
     * Present a recent satistics for user.
     */
    public static void showStatistics(){

    }

    /**
     * Settings for user.
     * Path for folder with current music and path for folder with
     * selected mp3s
     */
    public static void openSettingsForm(){

    }

    public MenuItem getNameOnList() {
        return mp3Menu;
    }

    public JPanel getSettingsPanel() {
        return settingsPanel;
    }

    public String getSettingsPanelTitle() {
        return "MP3-radio";
    }

    public void setToolTipObject(Tooltipable obj) {
        obj.setToolTip("mp3", "mp3", 0);
    }

    public void undoChanges() {
        settingsPanel.undoChanges();
    }

    public void commitChanges() {
        settingsPanel.commitChanges();
    }


}
