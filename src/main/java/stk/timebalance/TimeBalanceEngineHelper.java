package stk.timebalance;

import org.joda.time.DateTime;
import stk.Constans;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import org.apache.log4j.Logger;

/**
 * @author Sebastian Gruszka
 */
public class TimeBalanceEngineHelper {

    private static final Logger logger = Logger.getLogger(TimeBalanceEngineHelper.class);

    public static File createTimeMark(String modulePath) {
        File timeStartMark = new File(modulePath + stk.Constans.TIME_FLAG_START_FILENAME);
        if (!timeStartMark.exists()) {
            try {
                timeStartMark.createNewFile();
            } catch (IOException ex) {
                logger.debug("Can not create timeStartMark");
            }
        }
        return timeStartMark;
    }

    /**
     * Find what is already  processed
     *
     * @param repFile
     * @param reportData
     * @return
     */
    public static void readReport(File repFile, ReportData reportData) {
        String timePeriod;
        String[] array;
        reportData.setLastMonthComputedTimeInMils(Constans.UNSPECIFIED);
        reportData.setLastWeekComputedTimeInMils(Constans.UNSPECIFIED);
        reportData.setLastMonthComputed(Constans.UNSPECIFIED);
        reportData.setLastWeekComputed(Constans.UNSPECIFIED);
        reportData.setLwdSum(Constans.UNSPECIFIED);
        if (repFile.exists()) {
            try {
                Scanner fileScn = new Scanner(repFile).useDelimiter(System.getProperty("line.separator"));
                String name = null;
                while (fileScn.hasNext()) {
                    Scanner lineScn = new Scanner(fileScn.next()).useDelimiter(" ");
                    if (lineScn.hasNext()) {
                        name = lineScn.next();
                        if (name.startsWith("M")) {
                            try {
                                if (lineScn.hasNext()) {
                                    timePeriod = lineScn.next();
                                    array = timePeriod.split("h");
                                    reportData.setLastMonthComputedTimeInMils((Integer.parseInt(array[0]) * 60 + Integer.parseInt(array[1].split("m")[0])) * 60000);
                                    reportData.setLastMonthComputed(Integer.parseInt(name.substring(1, 3)));
                                }
                            } catch (NumberFormatException ex) {
                                reportData.setLastMonthComputed(Constans.UNSPECIFIED);
                            }
                        } else if (name.startsWith("W")) {
                            try {
                                if (lineScn.hasNext()) {
                                    timePeriod = lineScn.next();
                                    array = timePeriod.split("h");
                                    reportData.setLastWeekComputedTimeInMils((Integer.parseInt(array[0]) * 60 + Integer.parseInt(array[1].split("m")[0])) * 60000);
                                    reportData.setLastWeekComputed(Integer.parseInt(name.substring(1, 3)));
                                }
                            } catch (NumberFormatException ex) {
                                reportData.setLastWeekComputed(Constans.UNSPECIFIED);
                            }
                        }
                    }
                }
                fileScn.close();
            } catch (FileNotFoundException ex) {
                logger.debug("Can not find report file", ex);
            }
        }
    }

    public static void processEntries(File entriesFile, ReportData reportData, File rapFile, DateTime now) {
        DateTime lwdTemp = null;
        long lwdTempSum = 0;
        if (entriesFile.exists()) {
            try {
                Scanner entriesScn = new Scanner(entriesFile).useDelimiter(System.getProperty("line.separator"));
                String dayDate = null;
                Scanner lineScanner;
                DateTime start = null;
                DateTime end = null;
                long monthSum = 0;
                long weekSum = 0;
                int processedMonth = 0;
                int processedWeek = 0;
                int thisMonth = 0;
                int countDaysInWeek = 0;
                int countDaysInMonth = 0;
                DateTime calTemp;
                FileWriter fw = new FileWriter(rapFile, true);
                BufferedWriter reportWriter = new BufferedWriter(fw);
                while (entriesScn.hasNext()) {
                    lineScanner = new Scanner(entriesScn.next()).useDelimiter(" ");
                    if (lineScanner.hasNext()) {
                        dayDate = lineScanner.next();
                        if (lineScanner.hasNext()) {
                            start = new DateTime(dayDate + "T" + lineScanner.next());
                            if (lineScanner.hasNext()) {
                                end = new DateTime(dayDate + "T" + lineScanner.next());
                            } else {
                                start = null;
                            }
                        }
                    }
                    long timeGap = end.getMillis() - start.getMillis();
                    if ((end.getYear() == now.getYear())
                            && (end.getDayOfYear() == now.getDayOfYear())) {
                        reportData.setTodaySum(reportData.getTodaySum() + timeGap);
                    }
                    //remember last working day
                    if (reportData.getLwdSum() == Constans.UNSPECIFIED) {
                        lwdTemp = new DateTime(end.getYear(), end.getMonthOfYear(), end.getDayOfMonth(), 0, 0);
                        reportData.setLwdSum(timeGap);
                    } else {
                        calTemp = new DateTime(end.getYear(), end.getMonthOfYear(), end.getDayOfMonth(), 0, 0);
                        //this same day
                        if (calTemp.compareTo(lwdTemp) == 0) {
                            reportData.setLwdSum(reportData.getLwdSum() + timeGap);
                        } else { //day changed
                            lwdTemp = calTemp;
                            lwdTempSum = reportData.getLwdSum();
                            reportData.setLwdSum(timeGap);
                            countDaysInWeek++;
                            countDaysInMonth++;
                        }
                    }
                    //write week
                    if (processedWeek != 0) {
                        int pointWeek = start.getWeekOfWeekyear();
                        if (processedWeek == pointWeek) {
                            weekSum += (end.getMillis() - start.getMillis());
                        } else {
                            //week changed, save required
                            if (processedWeek > reportData.getLastWeekComputed()) {
                                reportWriter.write("W" + String.format("%02d", processedWeek) + " " + TimeBalanceEngine.milsToHuman(weekSum) + " " + countDaysInWeek);
                                reportWriter.write(System.getProperty("line.separator"));
                                reportData.setLastWeekComputed(processedWeek);
                                reportData.setLastWeekComputedTimeInMils(weekSum);
                            }
                            processedWeek = pointWeek;
                            weekSum = (end.getMillis() - start.getMillis());
                        }

                    } else {
                        weekSum += (end.getMillis() - start.getMillis());
                        processedWeek = start.getWeekOfWeekyear();
                    }
                    //write month
                    if (processedMonth != 0) {
                        int pointMonth = start.getMonthOfYear();
                        if (processedMonth == pointMonth) {
                            monthSum += (end.getMillis() - start.getMillis());
                        } else {
                            //month changed, save required
                            if (processedMonth > reportData.getLastMonthComputed()) {
                                reportWriter.write("M" + String.format("%02d", processedMonth) + " " + TimeBalanceEngine.milsToHuman(monthSum) + " " + countDaysInMonth);
                                reportWriter.write(System.getProperty("line.separator"));
                                reportData.setLastMonthComputed(processedMonth);
                                reportData.setLastMonthComputedTimeInMils(monthSum);
                            }
                            processedMonth = pointMonth;
                            monthSum = (end.getMillis() - start.getMillis());
                        }

                    } else {
                        monthSum += (end.getMillis() - start.getMillis());
                        processedMonth = start.getMonthOfYear();
                    }
                }
                entriesScn.close();
                if (weekSum > 0) {
                    int currentWeek = now.getWeekOfWeekyear();
                    if (processedWeek == currentWeek) {
                        reportData.setCurrentWeekSum(weekSum);
                    } else {
                        //TODO: when year changes check this condition
                        if (processedWeek == currentWeek - 1) {
                            reportData.setPreviousWeekSum(weekSum);
                        }
                        if (processedWeek > reportData.getLastWeekComputed()) {
                            reportWriter.write("W" + String.format("%02d", processedWeek) + " " + TimeBalanceEngine.milsToHuman(weekSum) + " " + countDaysInWeek);
                            reportWriter.write(System.getProperty("line.separator"));
                            reportData.setLastWeekComputed(processedWeek);
                            reportData.setLastWeekComputedTimeInMils(weekSum);
                        }
                    }
                    weekSum = 0;
                    countDaysInWeek = 0;
                }
                if (monthSum > 0) {
                    int currentMonth = now.getMonthOfYear();
                    if (processedMonth == currentMonth) {
                        reportData.setCurrentMonthSum(monthSum);
                    } else {
                        //TODO: when year changes check this condition
                        if (processedMonth == currentMonth - 1) {
                            reportData.setPreviousMonthSum(monthSum);
                        }
                        if (processedMonth > reportData.getLastMonthComputed()) {
                            reportWriter.write("M" + String.format("%02d", processedMonth) + " " + TimeBalanceEngine.milsToHuman(monthSum) + " " + countDaysInMonth);
                            reportWriter.write(System.getProperty("line.separator"));
                            reportData.setLastMonthComputed(processedMonth);
                            reportData.setLastMonthComputedTimeInMils(monthSum);
                        }
                    }
                    monthSum = 0;
                    countDaysInMonth = 0;
                }
                reportWriter.close();
            } catch (FileNotFoundException ex) {
                logger.debug("File not found", ex);
            } catch (IOException ex) {
                logger.debug("Input/output exception", ex);
            }
        }
        //TODO: year problem
        if (reportData.getLastWeekComputed() == (now.getWeekOfWeekyear() - 1)) {
            reportData.setPreviousWeekSum(reportData.getLastWeekComputedTimeInMils());
        }
        if (reportData.getLastMonthComputed() == (now.getMonthOfYear() - 1)) {
            reportData.setPreviousMonthSum(reportData.getLastMonthComputedTimeInMils());
        }

        //clear last_working_day if it is today
        if ((lwdTemp != null) && (lwdTemp.getYear() == now.getYear())
                && (lwdTemp.getDayOfYear() == now.getDayOfYear())) {
            reportData.setLwdSum(lwdTempSum);
        }
    }
}
