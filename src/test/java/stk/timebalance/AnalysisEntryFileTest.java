package stk.timebalance;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.File;

import org.joda.time.DateTime;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Sebastian Gruszka
 */
public class AnalysisEntryFileTest {

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void analysisEntriesFile1() {
        //given
        File rapFile = new File("data1.txt");
        File entriesFile = new File ("entries.txt");
        PrintWriter pw;
        try {
            pw = new PrintWriter(entriesFile);
            pw.println("2011-11-16 18:00 18:10");
            pw.close();
        } catch (FileNotFoundException ex) {
            fail(ex.getMessage());
        }
        ReportData reportData = new ReportData();
        DateTime nowForTests = new DateTime("2011-11-16");
        //when
        TimeBalanceEngineHelper.processEntries(entriesFile, reportData, rapFile, nowForTests);
        //then
        assertEquals(600000, reportData.getTodaySum());
        if (!rapFile.delete()) {
            fail("Rap file was not removed");
        }
        if (!entriesFile.delete()) {
            fail("Entries file was not removed");
        }
    }
    
    @Test
    public void analysisEntriesFileTodaySum() {
        //given
        File rapFile = new File("data.txt");
        File entriesFile = new File ("entries.txt");
        PrintWriter pw;
        try {
            pw = new PrintWriter(entriesFile);
            pw.println("2011-11-16 18:00 18:10");
            pw.println("2011-11-16 19:00 19:10");
            pw.close();
        } catch (FileNotFoundException ex) {
            fail(ex.getMessage());
        }
        ReportData reportData = new ReportData();
        DateTime nowForTests = new DateTime("2011-11-16");
        //when
        TimeBalanceEngineHelper.processEntries(entriesFile, reportData, rapFile, nowForTests);
        //then
        assertEquals(1200000, reportData.getTodaySum());
        if (!rapFile.delete()) {
            fail("Rap file was not removed");
        }
        if (!entriesFile.delete()) {
            fail("Entries file was not removed");
        }
    }

    @Test
    public void analysisEntriesFileWeekSum() {
        //given
        File rapFile = new File("data.txt");
        File entriesFile = new File ("entries.txt");
        PrintWriter pw;
        try {
            pw = new PrintWriter(entriesFile);
            pw.println("2011-11-13 08:00 08:10");
            pw.println("2011-11-14 08:00 08:10");
            pw.println("2011-11-15 19:00 19:05");
            pw.println("2011-11-16 19:00 19:05");
            pw.close();
        } catch (FileNotFoundException ex) {
            fail(ex.getMessage());
        }
        ReportData reportData = new ReportData();
        DateTime nowForTests = new DateTime("2011-11-16");
        //when
        TimeBalanceEngineHelper.processEntries(entriesFile, reportData, rapFile, nowForTests);
        //then
        assertEquals(1200000, reportData.getCurrentWeekSum());
        if (!rapFile.delete()) {
            fail("Rap file was not removed");
        }
        if (!entriesFile.delete()) {
            fail("Entries file was not removed");
        }
    }
    
    @Test
    public void analysisEntriesFilePreviousWeekSum() {
        File rapFile = new File("data.txt");
        File entriesFile = new File ("entries.txt");
        PrintWriter pw;
        try {
            pw = new PrintWriter(entriesFile);
            pw.println("2011-11-13 08:00 08:10");
            pw.println("2011-11-14 08:00 08:10");
            pw.println("2011-11-15 19:00 19:05");
            pw.println("2011-11-16 19:00 19:05");
            pw.close();
        } catch (FileNotFoundException ex) {
            fail(ex.getMessage());
        }
        ReportData reportData = new ReportData();
        DateTime nowForTests = new DateTime("2011-11-16");
        TimeBalanceEngineHelper.processEntries(entriesFile, reportData, rapFile, nowForTests);
        assertEquals(600000, reportData.getLastWeekComputedTimeInMils());
        assertEquals(45, reportData.getLastWeekComputed());
        if (!rapFile.delete()) {
            fail("Rap file was not removed");
        }
        if (!entriesFile.delete()) {
            fail("Entries file was not removed");
        }
    }

    @Test
    public void analysisEntriesFilePreviousWeekSum1() {
        //given
        File rapFile = new File("data.txt");
        File entriesFile = new File ("entries.txt");
        PrintWriter pw;
        try {
            pw = new PrintWriter(entriesFile);
            pw.println("2011-11-13 08:00 08:10");
            pw.println("2011-11-14 08:00 08:10");
            pw.println("2011-11-15 19:00 19:05");
            pw.println("2011-11-16 19:00 19:05");
            pw.close();
        } catch (FileNotFoundException ex) {
            fail(ex.getMessage());
        }
        ReportData reportData = new ReportData();
        DateTime nowForTests = new DateTime("2011-11-21");//week 47
        //when
        TimeBalanceEngineHelper.processEntries(entriesFile, reportData, rapFile, nowForTests);
        //then
        assertEquals(46, reportData.getLastWeekComputed());
        assertEquals(1200*1000, reportData.getLastWeekComputedTimeInMils());
        if (!rapFile.delete()) {
            fail("Rap file was not removed");
        }
        if (!entriesFile.delete()) {
            fail("Entries file was not removed");
        }
    }

    @Test
    public void analysisEntriesFilePreviousWeekSumAndDataFromReport() {
        //given
        File rapFile = new File("data.txt");
        File entriesFile = new File ("entries.txt");
        PrintWriter pw;
        try {
            pw = new PrintWriter(entriesFile);
            pw.println("2011-11-13 08:00 08:10");
            pw.println("2011-11-14 08:00 08:10"); //45
            pw.println("2011-11-15 19:00 19:05"); //46
            pw.println("2011-11-16 19:00 19:05");
            pw.close();
        } catch (FileNotFoundException ex) {
            fail(ex.getMessage());
        }
        ReportData reportData = new ReportData();
        reportData.setLastWeekComputed(45);
        reportData.setLastWeekComputedTimeInMils(123456);
        DateTime nowForTests = new DateTime("2011-11-20");//week 46
        //when
        TimeBalanceEngineHelper.processEntries(entriesFile, reportData, rapFile, nowForTests);
        //then
        assertEquals(45, reportData.getLastWeekComputed());
        assertEquals(123456, reportData.getLastWeekComputedTimeInMils());
        if (!rapFile.delete()) {
            fail("Rap file was not removed");
        }
        if (!entriesFile.delete()) {
            fail("Entries file was not removed");
        }
    }

    @Test
    public void analysisBothFiles() {
        //given
        ReportData reportData = new ReportData();
        File rapFile = new File("data.txt");
        PrintWriter pw;
        try {
            pw = new PrintWriter(rapFile);
            pw.println("W44 1h0m X");
            pw.println("W45 10h0m X");
            pw.close();
        } catch (FileNotFoundException ex) {
            fail(ex.getMessage());
        }
        TimeBalanceEngineHelper.readReport(rapFile, reportData);
        File entriesFile = new File ("entries.txt");
        try {
            pw = new PrintWriter(entriesFile);
            pw.println("2011-11-13 08:00 08:10");
            pw.println("2011-11-14 08:00 08:10"); //45
            pw.println("2011-11-15 19:00 19:05"); //46
            pw.println("2011-11-16 19:00 19:05");
            pw.close();
        } catch (FileNotFoundException ex) {
            fail(ex.getMessage());
        }
        DateTime nowForTests = new DateTime("2011-11-20");//week 46
        //when
        TimeBalanceEngineHelper.processEntries(entriesFile, reportData, rapFile, nowForTests);
        //then
        assertEquals(45, reportData.getLastWeekComputed());
        assertEquals(10*60*60000, reportData.getLastWeekComputedTimeInMils());
        if (!rapFile.delete()) {
            fail("Rap file was not removed");
        }
        if (!entriesFile.delete()) {
            fail("Entries file was not removed");
        }
    }

    @Test
    public void analysisBothFiles2() {
        ReportData reportData = new ReportData();
        File rapFile = new File("data.txt");
        PrintWriter pw;
        try {
            pw = new PrintWriter(rapFile);
            pw.println("W44 1h0m X");
            pw.close();
        } catch (FileNotFoundException ex) {
            fail(ex.getMessage());
        }
        TimeBalanceEngineHelper.readReport(rapFile, reportData);
        File entriesFile = new File ("entries.txt");
        try {
            pw = new PrintWriter(entriesFile);
            pw.println("2011-11-13 08:00 08:10"); //45
            pw.println("2011-11-14 08:00 08:10"); //46
            pw.println("2011-11-15 19:00 19:05");
            pw.println("2011-11-16 19:00 19:05");
            pw.close();
        } catch (FileNotFoundException ex) {
            fail(ex.getMessage());
        }
        DateTime nowForTests = new DateTime("2011-11-20");//week 46
        TimeBalanceEngineHelper.processEntries(entriesFile, reportData, rapFile, nowForTests);
        assertEquals(45, reportData.getLastWeekComputed());
        assertEquals(10*60000, reportData.getLastWeekComputedTimeInMils());
        if (!rapFile.delete()) {
            fail("Rap file was not removed");
        }
        if (!entriesFile.delete()) {
            fail("Entries file was not removed");
        }
    }

    @Test
    public void analysisBothFiles3() {
        ReportData reportData = new ReportData();
        File rapFile = new File("data.txt");
        PrintWriter pw;
        try {
            pw = new PrintWriter(rapFile);
            pw.println("W45 1h0m X");
            pw.close();
        } catch (FileNotFoundException ex) {
            fail(ex.getMessage());
        }
        TimeBalanceEngineHelper.readReport(rapFile, reportData);
        File entriesFile = new File ("entries.txt");
        try {
            pw = new PrintWriter(entriesFile);
            pw.println("2011-11-13 08:00 08:10"); //45
            pw.println("2011-11-14 08:00 08:10"); //46
            pw.println("2011-11-15 19:00 19:05");
            pw.println("2011-11-16 19:00 19:05");
            pw.close();
        } catch (FileNotFoundException ex) {
            fail(ex.getMessage());
        }
        DateTime nowForTests = new DateTime("2011-11-20");//week 46
        TimeBalanceEngineHelper.processEntries(entriesFile, reportData, rapFile, nowForTests);
        assertEquals(45, reportData.getLastWeekComputed());
        assertEquals(1*60*60000, reportData.getLastWeekComputedTimeInMils());
        assertEquals(20*60000, reportData.getCurrentWeekSum());
        assertEquals(1*60*60000, reportData.getPreviousWeekSum());
        if (!rapFile.delete()) {
            fail("Rap file was not removed");
        }
        if (!entriesFile.delete()) {
            fail("Entries file was not removed");
        }
    }

    @Test
    public void analysisBothFiles4() {
        ReportData reportData = new ReportData();
        File rapFile = new File("data.txt");
        PrintWriter pw;
        try {
            pw = new PrintWriter(rapFile);
            pw.println("W44 1h0m X");
            pw.close();
        } catch (FileNotFoundException ex) {
            fail(ex.getMessage());
        }
        TimeBalanceEngineHelper.readReport(rapFile, reportData);
        File entriesFile = new File ("entries.txt");
        try {
            pw = new PrintWriter(entriesFile);
            pw.println("2011-11-13 08:00 08:10"); //45
            pw.println("2011-11-14 08:00 08:10"); //46
            pw.println("2011-11-15 19:00 19:05");
            pw.println("2011-11-16 19:00 19:05");
            pw.close();
        } catch (FileNotFoundException ex) {
            fail(ex.getMessage());
        }
        DateTime nowForTests = new DateTime("2011-11-20");//week 46
        TimeBalanceEngineHelper.processEntries(entriesFile, reportData, rapFile, nowForTests);
        assertEquals(45, reportData.getLastWeekComputed());
        assertEquals(10*60000, reportData.getLastWeekComputedTimeInMils());
        assertEquals(20*60000, reportData.getCurrentWeekSum());
        assertEquals(10*60000, reportData.getPreviousWeekSum());
        if (!rapFile.delete()) {
            fail("Rap file was not removed");
        }
        if (!entriesFile.delete()) {
            fail("Entries file was not removed");
        }
    }

    @Test
    public void analysisBothFiles5() {
        ReportData reportData = new ReportData();
        File rapFile = new File("data.txt");
        PrintWriter pw;
        try {
            pw = new PrintWriter(rapFile);
            pw.println("W44 1h0m X");
            pw.close();
        } catch (FileNotFoundException ex) {
            fail(ex.getMessage());
        }
        TimeBalanceEngineHelper.readReport(rapFile, reportData);
        File entriesFile = new File ("entries.txt");
        try {
            pw = new PrintWriter(entriesFile);
            pw.println("2011-11-13 08:00 08:10"); //45
            pw.println("2011-11-14 08:00 08:10"); //46
            pw.println("2011-11-15 19:00 19:05");
            pw.println("2011-11-16 18:00 18:02");
            pw.println("2011-11-16 19:00 19:03");
            pw.println("2011-11-20 19:00 19:05");
            pw.println("2011-11-20 20:05 20:11");
            pw.close();
        } catch (FileNotFoundException ex) {
            fail(ex.getMessage());
        }
        DateTime nowForTests = new DateTime("2011-11-20");//week 46
        TimeBalanceEngineHelper.processEntries(entriesFile, reportData, rapFile, nowForTests);
        assertEquals(45, reportData.getLastWeekComputed());
        assertEquals(10*60000, reportData.getLastWeekComputedTimeInMils());
        assertEquals(31*60000, reportData.getCurrentWeekSum());
        assertEquals(10*60000, reportData.getPreviousWeekSum());
        assertEquals(11*60000, reportData.getTodaySum());
        assertEquals(5*60000, reportData.getLwdSum());
        assertEquals(41*60000, reportData.getCurrentMonthSum());
        if (!rapFile.delete()) {
            fail("Rap file was not removed");
        }
        if (!entriesFile.delete()) {
            fail("Entries file was not removed");
        }
    }

    @Test
    public void analysisBothFiles6() {
         ReportData reportData = new ReportData();
        File rapFile = new File("data.txt");
        PrintWriter pw;
        try {
            pw = new PrintWriter(rapFile);
            pw.println("W44 1h0m X");
            pw.close();
        } catch (FileNotFoundException ex) {
            fail(ex.getMessage());
        }
        TimeBalanceEngineHelper.readReport(rapFile, reportData);
        File entriesFile = new File ("entries.txt");
        try {
            pw = new PrintWriter(entriesFile);
            pw.println("2011-11-13 08:00 08:10"); //45
            pw.println("2011-11-14 08:00 08:10"); //46
            pw.println("2011-11-15 19:00 19:05");
            pw.println("2011-11-16 18:00 18:02");
            pw.println("2011-11-16 19:00 19:03");
            pw.println("2011-11-20 19:00 19:05");
            pw.println("2011-11-20 20:05 20:11");
            pw.close();
        } catch (FileNotFoundException ex) {
            fail(ex.getMessage());
        }
        DateTime nowForTests = new DateTime("2011-11-20");//week 46
        TimeBalanceEngineHelper.processEntries(entriesFile, reportData, rapFile, nowForTests);
        assertEquals(45, reportData.getLastWeekComputed());
        assertEquals(10*60000, reportData.getLastWeekComputedTimeInMils());
        assertEquals(31*60000, reportData.getCurrentWeekSum());
        assertEquals(10*60000, reportData.getPreviousWeekSum());
        assertEquals(11*60000, reportData.getTodaySum());
        assertEquals(5*60000, reportData.getLwdSum());
        assertEquals(41*60000, reportData.getCurrentMonthSum());
        if (!rapFile.delete()) {
            fail("Rap file was not removed");
        }
        if (!entriesFile.delete()) {
            fail("Entries file was not removed");
        }
    }

}
