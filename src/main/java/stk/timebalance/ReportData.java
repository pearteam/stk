package stk.timebalance;

import stk.Constans;

/**
 *  This class is responsible for storing data from file
 * @author Sebastian Gruszka
 */
public class ReportData {

    public ReportData() {
        this.lastWeekComputed = Constans.UNSPECIFIED;
        this.lastMonthComputed = Constans.UNSPECIFIED;
        this.lwdSum = Constans.UNSPECIFIED;
        this.lastMonthComputedTimeInMils = Constans.UNSPECIFIED;
        this.lastWeekComputedTimeInMils = Constans.UNSPECIFIED;
    }
    
    private long todaySum;
    
    private int lastWeekComputed;
    
    private int lastMonthComputed;
    
    private long currentWeekSum;
    
    private long previousWeekSum;
    
    private long currentMonthSum;
    
    private long previousMonthSum;
    
    private long lwdSum;
    
    private long lastMonthComputedTimeInMils;
    
    private long lastWeekComputedTimeInMils;

    /**
     * @return the todaySum
     */
    public long getTodaySum() {
        return todaySum;
    }

    /**
     * @param todaySum the todaySum to set
     */
    public void setTodaySum(long todaySum) {
        this.todaySum = todaySum;
    }

    /**
     * @return the lastWeekComputed
     */
    public int getLastWeekComputed() {
        return lastWeekComputed;
    }

    /**
     * @param lastWeekComputed the lastWeekComputed to set
     */
    public void setLastWeekComputed(int lastWeekComputed) {
        this.lastWeekComputed = lastWeekComputed;
    }

    /**
     * @return the lastMonthComputed
     */
    public int getLastMonthComputed() {
        return lastMonthComputed;
    }

    /**
     * @param lastMonthComputed the lastMonthComputed to set
     */
    public void setLastMonthComputed(int lastMonthComputed) {
        this.lastMonthComputed = lastMonthComputed;
    }

    /**
     * @return the currentWeekSum
     */
    public long getCurrentWeekSum() {
        return currentWeekSum;
    }

    /**
     * @param currentWeekSum the currentWeekSum to set
     */
    public void setCurrentWeekSum(long currentWeekSum) {
        this.currentWeekSum = currentWeekSum;
    }

    /**
     * @return the previousWeekSum
     */
    public long getPreviousWeekSum() {
        return previousWeekSum;
    }

    /**
     * @param previousWeekSum the previousWeekSum to set
     */
    public void setPreviousWeekSum(long previousWeekSum) {
        this.previousWeekSum = previousWeekSum;
    }

    /**
     * @return the lwdSum
     */
    public long getLwdSum() {
        return lwdSum;
    }

    /**
     * @param lwdSum the lwdSum to set
     */
    public void setLwdSum(long lwdSum) {
        this.lwdSum = lwdSum;
    }

    /**
     * @return the lastMonthComputedTimeInMils
     */
    public long getLastMonthComputedTimeInMils() {
        return lastMonthComputedTimeInMils;
    }

    /**
     * @param lastMonthComputedTimeInMils the lastMonthComputedTimeInMils to set
     */
    public void setLastMonthComputedTimeInMils(long lastMonthComputedTimeInMils) {
        this.lastMonthComputedTimeInMils = lastMonthComputedTimeInMils;
    }

    /**
     * @return the lastWeekComputedTimeInMils
     */
    public long getLastWeekComputedTimeInMils() {
        return lastWeekComputedTimeInMils;
    }

    /**
     * @param lastWeekComputedTimeInMils the lastWeekComputedTimeInMils to set
     */
    public void setLastWeekComputedTimeInMils(long lastWeekComputedTimeInMils) {
        this.lastWeekComputedTimeInMils = lastWeekComputedTimeInMils;
    }

    /**
     * @return the currentMonthSum
     */
    public long getCurrentMonthSum() {
        return currentMonthSum;
    }

    /**
     * @param currentMonthSum the currentMonthSum to set
     */
    public void setCurrentMonthSum(long currentMonthSum) {
        this.currentMonthSum = currentMonthSum;
    }

    /**
     * @return the previousMonthSum
     */
    public long getPreviousMonthSum() {
        return previousMonthSum;
    }

    /**
     * @param previousMonthSum the previousMonthSum to set
     */
    public void setPreviousMonthSum(long previousMonthSum) {
        this.previousMonthSum = previousMonthSum;
    }
}
