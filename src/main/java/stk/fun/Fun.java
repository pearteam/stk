package stk.fun;

import java.awt.Menu;
import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JPanel;
import stk.Pluginable;
import stk.Tooltipable;

/**
 *
 * @author Sebastian Gruszka
 */
public class Fun implements Pluginable {

    private static Fun instance;
    Menu list;

    public static Pluginable getInstance() {
        if (instance == null) {
            instance = new Fun();
        }
        return instance;
    }

    Fun() {
        list = new Menu("Fun");
        MenuItem wheel = new MenuItem("Progress wheel");
        list.add(wheel);
        wheel.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                /* Create and display the dialog */
                java.awt.EventQueue.invokeLater(new Runnable() {

                    public void run() {
                        final ProgressWheel dialog = new ProgressWheel(new javax.swing.JFrame(), true);
                        dialog.addWindowListener(new java.awt.event.WindowAdapter() {

                            @Override
                            public void windowClosing(java.awt.event.WindowEvent e) {
                                dialog.dispose();
                            }
                        });
                        dialog.setVisible(true);
                    }
                });
            }
        });
                
    }

    public MenuItem getNameOnList() {
        return list;
    }

    public String getSettingsPanelTitle() {
        return null;
    }

    public JPanel getSettingsPanel() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setToolTipObject(Tooltipable obj) {
        obj.setToolTip("xx", null, 0);
    }

    public void undoChanges() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void commitChanges() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
