package stk.timebalance;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

import stk.Constans;

/**
 *
 * @author Sebastian Gruszka
 */
public class AnalysisReportFileTest {

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
    public void emptyReportFileTest() {
        //given
        File rapFile =  new File(System.getProperty("user.home") + "/.tmp-empty.txt");
        //when
        try {
            rapFile.createNewFile();
        } catch (IOException ex) {
            fail(ex.getMessage());
        }
        ReportData reportData = new ReportData();
        TimeBalanceEngineHelper.readReport(rapFile, reportData);
        //then
        assertEquals(0, reportData.getCurrentWeekSum());
        if (!rapFile.delete()) {
            fail("Rap file was not removed");
        }
    }

    @Test
    public void missingReportFile() {
        //given
        File rapFile = new File("xxx.txt");
        ReportData reportData = new ReportData();
        //when
        TimeBalanceEngineHelper.readReport(rapFile, reportData);
        //then
        assertEquals(0, reportData.getCurrentWeekSum());
    }

    @Test
    public void analysisReportFileForMonth() {
        //given
        File rapFile = new File("data.txt");
        PrintWriter pw;
        try {
            pw = new PrintWriter(rapFile);
            pw.println("M01 0h1m 1\n");
            pw.close();
        } catch (FileNotFoundException ex) {
            fail(ex.getMessage());
        }
        ReportData reportData = new ReportData();
        //when
        TimeBalanceEngineHelper.readReport(rapFile, reportData);
        //then
        assertEquals(1, reportData.getLastMonthComputed());
        assertEquals(60000, reportData.getLastMonthComputedTimeInMils());
        if (!rapFile.delete()) {
            fail("Rap file was not removed");
        }
    }

    @Test
    public void analysisReportFileForManyMonths() {
        //given
        File rapFile = new File("data.txt");
        PrintWriter pw;
        try {
            pw = new PrintWriter(rapFile);
            pw.println("M01 0h1m 1\n");
            pw.println("M02 0h1m 1\n");
            pw.close();
        } catch (FileNotFoundException ex) {
            fail(ex.getMessage());
        }
        ReportData reportData = new ReportData();
        //when
        TimeBalanceEngineHelper.readReport(rapFile, reportData);
        //then
        assertEquals(2, reportData.getLastMonthComputed());
        assertEquals(60000, reportData.getLastMonthComputedTimeInMils());
        if (!rapFile.delete()) {
            fail("Rap file was not removed");
        }
    }

    @Test
    public void analysisReportFileForManyMonthsV2() {
        //given
        File rapFile = new File("data.txt");
        PrintWriter pw;
        try {
            pw = new PrintWriter(rapFile);
            pw.println("M01 0h01m 1\n");
            pw.println("M02 5h02m 1\n");
            pw.close();
        } catch (FileNotFoundException ex) {
            fail(ex.getMessage());
        }
        ReportData reportData = new ReportData();
        //when
        TimeBalanceEngineHelper.readReport(rapFile, reportData);
        //then
        assertEquals(2, reportData.getLastMonthComputed());
        assertEquals((5 * 60 + 2)*60000, reportData.getLastMonthComputedTimeInMils());
        if (!rapFile.delete()) {
            fail("Rap file was not removed");
        }
    }

    @Test
    public void analysisReportFileForManyMonthsV3() {
        //given
        File rapFile = new File("data.txt");
        PrintWriter pw;
        try {
            pw = new PrintWriter(rapFile);
            pw.println("M01 0h01m 1\n");
            pw.println("M02 5h01m 1\n");
            pw.println("M03 5h02m 1\n");
            pw.close();
        } catch (FileNotFoundException ex) {
            fail(ex.getMessage());
        }
        ReportData reportData = new ReportData();
        //when
        TimeBalanceEngineHelper.readReport(rapFile, reportData);
        //then
        assertEquals(3, reportData.getLastMonthComputed());
        assertEquals((5 * 60 + 2)*60000, reportData.getLastMonthComputedTimeInMils());
        assertEquals(Constans.UNSPECIFIED, reportData.getLastWeekComputed());
        assertEquals(Constans.UNSPECIFIED, reportData.getLastWeekComputedTimeInMils());
        if (!rapFile.delete()) {
            fail("Rap file was not removed");
        }
    }

    @Test
    public void analysisReportFileForWeek() {
        //given
        File rapFile = new File("data.txt");
        PrintWriter pw;
        try {
            pw = new PrintWriter(rapFile);
            pw.println("W01 0h01m 1\n");
            pw.println("W02 5h01m 1\n");
            pw.println("W03 5h02m 1\n");
            pw.close();
        } catch (FileNotFoundException ex) {
            fail(ex.getMessage());
        }
        ReportData reportData = new ReportData();
        //when
        TimeBalanceEngineHelper.readReport(rapFile, reportData);
        //then
        assertEquals(3, reportData.getLastWeekComputed());
        assertEquals((5 * 60 + 2)*60000, reportData.getLastWeekComputedTimeInMils());
        assertEquals(Constans.UNSPECIFIED, reportData.getLastMonthComputed());
        assertEquals(Constans.UNSPECIFIED, reportData.getLastMonthComputedTimeInMils());
        if (!rapFile.delete()) {
            fail("Rap file was not removed");
        }
    }

    @Test
    public void analysisReportFileForWeekAndMonth() {
        //given
        File rapFile = new File("data.txt");
        PrintWriter pw;
        try {
            pw = new PrintWriter(rapFile);
            pw.println("W01 0h01m 1\n");
            pw.println("W02 5h01m 1\n");
            pw.println("W03 5h02m 1\n");
            pw.println("M01 0h01m 1\n");
            pw.println("M02 5h01m 1\n");
            pw.println("M05 15h15m 1\n");
            pw.close();
        } catch (FileNotFoundException ex) {
            fail(ex.getMessage());
        }
        ReportData reportData = new ReportData();
        //when
        TimeBalanceEngineHelper.readReport(rapFile, reportData);
        //then
        assertEquals(3, reportData.getLastWeekComputed());
        assertEquals((5 * 60 + 2)*60000, reportData.getLastWeekComputedTimeInMils());
        assertEquals(5, reportData.getLastMonthComputed());
        assertEquals((15 * 60 + 15)*60000, reportData.getLastMonthComputedTimeInMils());
        if (!rapFile.delete()) {
            fail("Rap file was not removed");
        }
    }
    
}
