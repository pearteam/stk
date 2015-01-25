package stk.timebalance;

import org.joda.time.DateTime;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import static stk.timebalance.TimeBalanceEngine.milsToHuman;

/**
 * @author Sebastian Gruszka
 */
public class BackgroundUpdater implements Runnable {

    TimeBalanceEngine engine;
    File timeMark;
    long lastRun = 0;
    long lastRunPlus = 0;
    ReportData reportData;
    String modulePath;
    File timeStartMark;
    TBSettingsJPanel settingsPanel;

    BackgroundUpdater(TimeBalanceEngine engine) {
        this.engine = engine;
        modulePath = System.getProperty("user.home") + stk.Constans.BRANCH + stk.Constans.TIME_BALANCE_DIR;
        new File(modulePath).mkdirs();
        timeStartMark = engine.getTimeBalanceEngineHelper().createTimeMark(modulePath);
        //Make report start
        int currentYear = (new DateTime()).getYear();
        String rapFileName = modulePath + stk.Constans.TIME_BALANCE_REPORT_FILENAME + currentYear + stk.Constans.REP_EXT;
        File rapFile = new File(rapFileName);
        reportData = new ReportData();
        engine.getTimeBalanceEngineHelper().readReport(rapFile, reportData);
        //process entries
        File entriesFile = new File(modulePath + stk.Constans.TIME_BALANCE_TIME_ENTRIES_FILENAME + currentYear + stk.Constans.REP_EXT);
        engine.getTimeBalanceEngineHelper().processEntries(entriesFile, reportData, rapFile, new DateTime());

        //Make report end
        timeMark = new File(modulePath + stk.Constans.TIME_FLAG_RECENT_FILENAME);
        settingsPanel = (TBSettingsJPanel) engine.getSettingsPanel();

    }

    /**
     * Mark working time
     */
    public void run() {
        DateTime nowDate = new DateTime();
        if (timeMark.exists()) {
            //There was run before.
            DateTime recentDate = new DateTime(timeMark.lastModified());
            if ((recentDate.getMillis() + stk.Constans.TIME_BALANCE_CHECK_BUFFER) < nowDate.getMillis()
                    || recentDate.getDayOfYear() != nowDate.getDayOfYear()) {
                //day changed
                if (recentDate.getDayOfYear() != nowDate.getDayOfYear()) {
                    lastRunPlus += lastRun;
                    reportData.setLwdSum(reportData.getTodaySum());
                    reportData.setTodaySum(0);
                }
                try {
                    //add to report
                    FileWriter out = new FileWriter(modulePath + stk.Constans.TIME_BALANCE_TIME_ENTRIES_FILENAME + nowDate.getYear() + stk.Constans.REP_EXT, true);
                    BufferedWriter writer = new BufferedWriter(out);
                    DateTime startDate = new DateTime(timeStartMark.lastModified());
                    writer.write(startDate.toLocalDate() + " " + startDate.toLocalTime().toString().substring(0,5) + " " + recentDate.toLocalTime().toString().substring(0,5));
                    writer.write(System.getProperty("line.separator"));
                    writer.close();
                    timeStartMark.setLastModified(nowDate.getMillis());

                } catch (Exception ex) {
                    Logger.getLogger(TimeBalanceEngine.class.getName()).log(Level.SEVERE, "Couldn't write report");
                }

                //set start for new interval
                //..
            }
            lastRun = nowDate.getMillis() - timeStartMark.lastModified();
            //check that last month is previous month
            int previousMonth = (new DateTime()).getMonthOfYear() - 1;
            if (previousMonth == 0)
                previousMonth = 12;
            if (reportData.getLastMonthComputed() != (previousMonth)) {
                reportData.setLastMonthComputedTimeInMils(0);
            }

            engine.publish((settingsPanel.getCB_TW().isSelected() ? "TW:" + milsToHuman(reportData.getCurrentWeekSum() + reportData.getTodaySum() + lastRun) + " " : "")
                    + (settingsPanel.getCB_LW().isSelected() ? "LW:" + milsToHuman(reportData.getLastWeekComputedTimeInMils()) + " " : "")
                    + (settingsPanel.getCB_TM().isSelected() ? "TM:" + milsToHuman(reportData.getCurrentMonthSum() + lastRun) + " " : "")
                    + (settingsPanel.getCB_LM().isSelected() ? "LM:" + milsToHuman(reportData.getLastMonthComputedTimeInMils()) + " " : "")
                    + (settingsPanel.getCB_T().isSelected() ? "T:" + milsToHuman(lastRun + reportData.getTodaySum()) + " " : "")
                    + (settingsPanel.getCB_LWD().isSelected() ? "LWD:" + milsToHuman(reportData.getLwdSum()) + " " : "")
                    + (settingsPanel.getCB_LR().isSelected() ? "LR:" + milsToHuman(lastRun + lastRunPlus) : ""));

        } else {
            //First time run :-)
            try {
                timeMark.createNewFile();
            } catch (IOException ex) {
                Logger.getLogger(TimeBalanceEngine.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        timeMark.setLastModified(nowDate.getMillis());
    }
}
