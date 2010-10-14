/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.avbravo.couchdb;

import java.awt.Image;
import java.awt.Point;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import org.netbeans.api.visual.vmd.VMDGraphScene;
import org.netbeans.api.visual.vmd.VMDNodeWidget;
import org.netbeans.api.visual.vmd.VMDPinWidget;
import org.openide.util.ImageUtilities;

/**
 *
 * @author avbravo
 */
public class DBGraphScene extends VMDGraphScene {

    private static final Image IMAGE_LIST = ImageUtilities.loadImage("org/avbravo/couchdb/resources/list_16.png"); // NOI18N
    private static final Image IMAGE_ITEM = ImageUtilities.loadImage("org/avbravo/couchdb/resources/item_16.png"); // NOI18N
    private static int edgeID = 1;
Sesion sesion = new Sesion();
    public DBGraphScene(Connection connection) {
        try {
            createSceneFromConnection(connection);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "DBGraphScene()" + e.getLocalizedMessage().toString());
        }
    }

    private void createSceneFromConnection(Connection jdbcConnection) throws SQLException {
        try {
            ArrayList<String> tables = new ArrayList<String>();
            DatabaseMetaData databaseMetaData = jdbcConnection.getMetaData();
            String[] names = {"TABLE"};

            ResultSet resultSet = databaseMetaData.getTables(null, "%", "%", names);
            while (resultSet.next()) {

                String table = resultSet.getString("TABLE_NAME");
sesion.AgregarTablaLista(table);
                tables.add(table);
                createNode(this, (int) (Math.random() * 800), (int) (Math.random() * 800), IMAGE_LIST, table, "Table", null);
               // ResultSet columns = jdbcConnection.getMetaData().getColumns(null, null, table.toUpperCase(), "%");
                //Quitar el metodo toUpper() para evitar que no reconozca tablas con
                //nombres en minuscula
                ResultSet columns = jdbcConnection.getMetaData().getColumns(null, null, table, "%");


               while (columns.next()) {
                    String columnName = columns.getString("COLUMN_NAME");

                    createPin(this, table, table + ":" + columnName, IMAGE_ITEM, columnName, columnName);
                }




            }
            for (String string : tables) {

                ResultSet resultSet1 = databaseMetaData.getExportedKeys(null, null, string);

                while (resultSet1.next()) {
                    String pkTable = resultSet1.getString("PKTABLE_NAME");

                    String pkColumn = resultSet1.getString("PKCOLUMN_NAME");
                    String fkTable = resultSet1.getString("FKTABLE_NAME");
                    String fkColumn = resultSet1.getString("FKCOLUMN_NAME");

                    /*
                     *Para mostrar las relaciones de las tablas
                     * quitar el comentario de esta linea
                     * pendiente verificar el comportamiento con postgresql
                     */

                   createEdge(this, fkTable + ":" + fkColumn, pkTable + ":" + pkColumn);
                }

            }
            this.moveTo(null);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "createSceneFromConnection<()" + ex.getLocalizedMessage().toString());

        }
    }

    private static String createNode(VMDGraphScene scene, int x, int y, Image image, String name, String type, java.util.List<Image> glyphs) {

        String node = name;
        try {
            VMDNodeWidget widget = (VMDNodeWidget) scene.addNode(node);
            widget.setPreferredLocation(new Point(x, y));
            widget.setNodeProperties(image, name, type, glyphs);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "createNode()" + ex.getLocalizedMessage().toString());

        }
        return node;
    }

    private static void createPin(VMDGraphScene scene, String nodeID, String pinID, Image image, String name, String type) {
        ((VMDPinWidget) scene.addPin(nodeID, pinID)).setProperties(name, null);
    }

    private static void createEdge(VMDGraphScene scene, String sourcePinID, String targetPinID) {
        try {

            String edge = "edge" + DBGraphScene.edgeID++;
            scene.addEdge(edge);
            scene.setEdgeSource(edge, sourcePinID);
            scene.setEdgeTarget(edge, targetPinID);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "createEdge()" + ex.getLocalizedMessage().toString());

        }

    }

    private void moveTo(Point point) {
        try {
            int index = 0;
            for (String node : getNodes()) {
                getSceneAnimator().animatePreferredLocation(findWidget(node), point != null ? point : new Point(++index * 100, index * 100));
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "moveTo()" + ex.getLocalizedMessage().toString());

        }
    }
}
