/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package stk;

import java.util.Date;

/**
 *
 * @author user
 */
public class Constans {
    /** Branch */
    public static final String BRANCH = "/.stk";
    /** Time balance dir */
    public static final String TIME_BALANCE_DIR = "/.timebalance";
    /** Time flag for recent time*/
    public static final String TIME_FLAG_RECENT_FILENAME = "/.timeflag";
    /** Time flag for start time*/
    public static final String TIME_FLAG_START_FILENAME = "/.timestartflag";
    /** Time in ms to wait to consider new time interval*/
    public static final long TIME_BALANCE_CHECK_BUFFER = 300000;
    /** Time balance report file name*/
    public static final String TIME_BALANCE_REPORT_FILENAME = "/rap";
    /** Properties storage file*/
    public static final String PROPERTIES_STORAGE_FILE = "/.propstorage";
    /** File name with time entries */
    public static final String TIME_BALANCE_TIME_ENTRIES_FILENAME= "/timeent";
    /** Report extension */
    public static final String REP_EXT = ".txt";
    /** Unspecified */
    public static final int UNSPECIFIED = -1;
    
}
