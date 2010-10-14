/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.avbravo.couchdb;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.openide.windows.TopComponent;
/**
*
* @author avbravo
*/
public class Explorar implements ActionListener {
public void actionPerformed(ActionEvent e) {
// TODO implement action body
    TopComponent tc = new DemoDBTopComponent();
tc.open();
tc.requestActive();

}
}
