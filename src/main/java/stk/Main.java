package stk;

import java.awt.AWTException;
import java.awt.Font;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedHashMap;
import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author Sebastian Gruszka
 */
public class Main implements Tooltipable {

    /** an instance to main*/
    private static Main instance;
        
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        SettingsLoader settingsLoader = SettingsLoader.getInstance();
        /* Use an appropriate Look and Feel */
        try {
            UIManager.setLookAndFeel(settingsLoader.getLnf());
        } catch (UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
        } catch (InstantiationException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        /* Turn off metal's use of bold fonts */
        UIManager.put("swing.boldMetal", Boolean.FALSE);
        // Schedule a job for the event-dispatching thread:
        // adding TrayIcon.
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {

                getInstance().createAndShowGUI();
            }
        });
        //TimeBalanceEngine.runIt();
    }
    private static TrayIcon trayIcon0;
    /** Tool tim list*/
    private static LinkedHashMap<String, String> toolTipList;
    /** For storing properties from evry module */
    private HashMap<String, Object> propertiesStorage;

    Main() {
        loadProperties();
        //add shoutdown hook - prevent ghost process
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {

            @Override
            public void run() {
                saveProperties();
            }
        }));
    }

    /**
     * inner class to make sure about correct behavior when is no storage file
     */
    class NoStorageException extends Exception {
    }

    /**
     * Loads properties of modules
     */
    private void loadProperties() {
        try {
            try {
                File propertiesStorageFile = new File(System.getProperty("user.home") + stk.Constans.BRANCH + Constans.PROPERTIES_STORAGE_FILE);
                if (propertiesStorageFile.exists()) {
                    FileInputStream fis = new FileInputStream(propertiesStorageFile);
                    ObjectInputStream ois = new ObjectInputStream(fis);
                    Object obj = ois.readObject();
                    if (obj instanceof HashMap) {
                        propertiesStorage = (HashMap) obj;
                    } else {
                        throw new NoStorageException();
                    }
                    ois.close();
                } else {
                    throw new NoStorageException();
                }
            } catch (FileNotFoundException e) {
                throw new NoStorageException();
            } catch (IOException e) {
                throw new NoStorageException();
            } catch (ClassNotFoundException e) {
                throw new NoStorageException();
            }
        } catch (NoStorageException ex) {
            propertiesStorage = new HashMap<String, Object>();
        }
    }

    /**
     * Saves Properties of modules
     */
    private void saveProperties() {
        try {
            FileOutputStream fs = new FileOutputStream(System.getProperty("user.home") + stk.Constans.BRANCH + Constans.PROPERTIES_STORAGE_FILE);
            ObjectOutputStream os = new ObjectOutputStream(fs);
            os.writeObject(propertiesStorage);
            os.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Module name, if not exists it is adding it, otherwise is updating,
     * 'pos' suppose to be used to choose position on list: 0, 1, 2, ect.
     * at the end it aggregates strings and put it to tooltiplist.settooltip(str)
     * 
     * @param name
     * @param txt if null entry will be removed
     * @param pos don't work now
     */
    public void setToolTip(String name, String txt, int pos) {
        if (trayIcon0 != null) {
            String output = "";
            if (toolTipList == null) {
                toolTipList = new LinkedHashMap<String, String>();
                toolTipList.put(name, txt);
            } else {
                if (txt == null) {
                    toolTipList.remove(name);
                } else {
                    toolTipList.put(name, txt);
                }

            }
            boolean first = true;
            for (String str : toolTipList.values()) {
                output += (first ? "" : "\n") + str;
                first = false;
            }
            trayIcon0.setToolTip(output);
        }
    }

    private void createAndShowGUI() {
        // Check the SystemTray support
        if (!SystemTray.isSupported()) {
            System.out.println("SystemTray is not supported");
            JOptionPane.showMessageDialog(null, "SystemTray is not supported");
            return;
        }
        SettingsLoader settingsLoader = SettingsLoader.getInstance();
        final PopupMenu popup = new PopupMenu();
        trayIcon0 = new TrayIcon(createImage(settingsLoader.getGrayIcon(), "hum icon"));
        final TrayIcon trayIcon = trayIcon0;
        final SystemTray tray = SystemTray.getSystemTray();

        //Main settings panel
        final StkSettingsJDialog stkSettingsJDialog = new StkSettingsJDialog(new javax.swing.JFrame(), true);
        stkSettingsJDialog.setLocationRelativeTo(null);

        // Create a popup menu components
        for (Pluginable plu : settingsLoader.getModuleList()) {
            popup.add(plu.getNameOnList());
            if (plu.getSettingsPanelTitle() != null) {
                stkSettingsJDialog.getjTabbedPane1().addTab(plu.getSettingsPanelTitle(), plu.getSettingsPanel());
            }
            plu.setToolTipObject(this);
        }
        if (popup.getItemCount() > 0) {
            popup.addSeparator();
        }
        MenuItem settingsItem = new MenuItem("Settings");
        popup.add(settingsItem);
        popup.addSeparator();
        MenuItem aboutItem = new MenuItem("About");
        popup.add(aboutItem);
        popup.addSeparator();
        MenuItem exitItem = new MenuItem("Exit");
        popup.add(exitItem);



        final JPopupMenu jPopupMenu = new JPopupMenu();
        jPopupMenu.add(new JMenuItem("test1"));
        jPopupMenu.add(new JMenuItem("test2"));
        popup.setFont(Font.getFont("SansSerif-Plain-10"));
        trayIcon.setPopupMenu(popup);
        trayIcon.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                System.out.println("action performed");
            }
        });
        trayIcon.addMouseListener(new MouseListener() {

            public void mouseClicked(MouseEvent e) {
                System.out.println("MM mouse clicked" + e.getButton());
            }

            public void mousePressed(MouseEvent e) {
                System.out.println("mouse pressed");
            }

            public void mouseReleased(MouseEvent e) {
                System.out.println("mouse released");
            }

            public void mouseEntered(MouseEvent e) {
                System.out.println("mouse entered");
            }

            public void mouseExited(MouseEvent e) {
                System.out.println("mouse exited");
            }
        });

        try {
            tray.add(trayIcon);
        } catch (AWTException e) {
            System.out.println("TrayIcon could not be added.");
            return;
        }

        trayIcon.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                //double click on system tray
                JOptionPane.showMessageDialog(null,
                        "This dialog box is run from System Tray");
            }
        });

        settingsItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                stkSettingsJDialog.setVisible(true);
            }
        });

        aboutItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                final AboutBox dialog = new AboutBox(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {

                    public void windowClosing(java.awt.event.WindowEvent e) {
                        dialog.dispose();
                    }
                });
                dialog.setLocationRelativeTo(null);
                dialog.setVisible(true);
            }
        });

        exitItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                tray.remove(trayIcon);
                System.exit(0);
            }
        });
    }

    // Obtain the image URL
    protected static Image createImage(String path, String description) {
        URL imageURL = Main.class.getResource(path);

        if (imageURL == null) {
            System.err.println("Resource not found: " + path);
            return null;
        } else {
            return (new ImageIcon(imageURL, description)).getImage();
        }
    }

    /**
     * Sets moduls properties.
     * @param key for property
     * @param obj object
     */
    public void setProperty(String key, Object obj) {
        propertiesStorage.put(key, obj);
    }

    /**
     * Gets module properties
     * @param key
     * @return property object
     */
    public Object getProperty(String key) {
        return propertiesStorage.get(key);
    }
    
    /**
     * Gets module properties
     * @param key
     * @param _default default object, when missing setting
     * @return property object
     */
    public Object getProperty(String key, Object _default) {
        Object ret = propertiesStorage.get(key);
        if (ret == null){
            ret = _default;
        }
        return ret;
    }
    
    public static Main getInstance(){
        if (instance == null){
            instance = new Main();
        }
        return instance;
    }
}
