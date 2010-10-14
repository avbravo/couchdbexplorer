/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.avbravo.couchdb;

/**
 *
 * @author avbravo
 */
import javax.swing.JOptionPane;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.awt.StatusDisplayer;

/**
 *
 * @author avbravo
 */
public class NBRunnable implements Runnable {

    Sesion sesion = new Sesion();
    int msgTypeError = NotifyDescriptor.ERROR_MESSAGE;
    int msgType = NotifyDescriptor.INFORMATION_MESSAGE;

    @Override
    public void run() {

        try {

            for (int i = 0; i < sesion.getListaTablas().size(); i++) {
                ProgressHandle p = ProgressHandleFactory.createHandle(
                        "Procesando tabla: " + sesion.getListaTablas().get(i)
                        + "     espere....");
                p.start();
                sesion.procesarTabla(sesion.getListaTablas().get(i));
                // break;
                p.finish();
            }
            StatusDisplayer.getDefault().setStatusText("Migración terminada...");
            NotifyDescriptor d = new NotifyDescriptor.Message("Proceso de migración terminada...", msgType);
            DialogDisplayer.getDefault().notify(d);
            // p.finish();
        } catch (Exception ex) {
            StatusDisplayer.getDefault().setStatusText("Error " + ex.getMessage().toString());
        }

    }
}
