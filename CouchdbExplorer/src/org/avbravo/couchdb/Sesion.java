/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.avbravo.couchdb;

/**
 *
 * @author avbravo
 */
import com.fourspaces.couchdb.Database;
import com.fourspaces.couchdb.Document;
import com.fourspaces.couchdb.Session;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.swing.JOptionPane;
import org.openide.awt.StatusDisplayer;

/**
 *
 * @author avbravo
 */
public class Sesion {

    private static String ipCouchdb = "localhost";
    private static int puertoCouchdb = 5984;
    private static String userCouchdb;
    private static String passwordCouchdb;
    private static String prefijo;
    private static String formatoFecha;
    public static Session dbSession;
    public static Database db;
    public static Document doc;
    static Connection conn;
    static Statement stmt;
    static ResultSet rs;
    static ResultSetMetaData mdata;
    static DatabaseMetaData dmd;
    int num_columnas;
    static List<String> ListaTablas = new ArrayList<String>();
    static List<String> ListaMensajes = new ArrayList<String>();

    public static List<String> getListaMensajes() {
        return ListaMensajes;
    }

    public static void setListaMensajes(List<String> ListaMensajes) {
        Sesion.ListaMensajes = ListaMensajes;
    }

    public static String getFormatoFecha() {
        return formatoFecha;
    }

    public static void setFormatoFecha(String formatoFecha) {
        Sesion.formatoFecha = formatoFecha;
    }

    public static String getPrefijo() {
        return prefijo;
    }

    public static void setPrefijo(String prefijo) {
        Sesion.prefijo = prefijo;
    }

    public static List<String> getListaTablas() {
        return ListaTablas;
    }

    public static void setListaTablas(List<String> ListaTablas) {
        Sesion.ListaTablas = ListaTablas;
    }

    public String getIpCouchdb() {
        return ipCouchdb;
    }

    public void setIpCouchdb(String ipCouchdb) {
        this.ipCouchdb = ipCouchdb;
    }

    public String getPasswordCouchdb() {
        return passwordCouchdb;
    }

    public void setPasswordCouchdb(String passwordCouchdb) {
        this.passwordCouchdb = passwordCouchdb;
    }

    public int getPuertoCouchdb() {
        return puertoCouchdb;
    }

    public void setPuertoCouchdb(int puertoCouchdb) {
        this.puertoCouchdb = puertoCouchdb;
    }

    public String getUserCouchdb() {
        return userCouchdb;
    }

    public void setUserCouchdb(String userCouchdb) {
        this.userCouchdb = userCouchdb;
    }

    public static DatabaseMetaData getDmd() {
        return dmd;
    }

    public static void setDmd(DatabaseMetaData dmd) {
        Sesion.dmd = dmd;
    }
    /*
     * Agrega una tabla a la lista
     */

    public void AgregarTablaLista(String tabla) {
        try {
            this.ListaTablas.add(tabla);

        } catch (Exception ex) {

            JOptionPane.showMessageDialog(null, "AgregarTablaLista()\nMensaje: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

    }

    /*
     *inicio
     */
    public boolean ConectarCouchDB() {
        try {
            dbSession = new Session(ipCouchdb, puertoCouchdb);
            String host = dbSession.getHost();
            //
            //  dbSession
            List<String> databasesCouchdb = dbSession.getDatabaseNames();

            StatusDisplayer.getDefault().setStatusText("host..." + dbSession.getHost());
            if (dbSession == null) {
                return false;
            }
            return true;
        } catch (Exception ex) {

            JOptionPane.showMessageDialog(null, "ConectarCouchDB()\nMensaje: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            this.ListaMensajes.add("ConectarCouchDB) " + ex.getLocalizedMessage().toString());
            StatusDisplayer.getDefault().setStatusText("ConectarCouchDB. Error " + ex.getMessage().toString());
        }
        return false;
    }

    public boolean CrearBaseDatos(String dbname) {
        try {
            db = dbSession.createDatabase(dbname);
            if (db == null) {
                return false;

            }
            return true;
        } catch (Exception ex) {
            //  JOptionPane.showMessageDialog(null, "CrearBaseDatos()\nMensaje: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            this.ListaMensajes.add("CrearBaseDatos(String dbname) " + ex.getLocalizedMessage().toString());
            StatusDisplayer.getDefault().setStatusText("CrearBaseDatos(String dbname). Error " + ex.getMessage().toString());
        }
        return false;
    }

    public boolean AbrirBaseDatos(String dbname) {
        try {
            db = dbSession.getDatabase(dbname);
            if (db == null) {
                return false;
            }
            return true;
        } catch (Exception ex) {

            //  JOptionPane.showMessageDialog(null, "AbrirBaseDatos()\nMensaje: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            this.ListaMensajes.add("AbrirBaseDatos(String dbname) " + ex.getLocalizedMessage().toString());
            StatusDisplayer.getDefault().setStatusText("AbrirBaseDatos(String dbname). Error " + ex.getMessage().toString());
        }
        return false;
    }

    public void AgregarDocumentos() {
        try {

            doc = new Document();
            doc.setId("myid");
            doc.put("nombre", "valor");

            db.saveDocument(doc);
        } catch (Exception ex) {
            this.ListaMensajes.add("AgregarDocumentos " + ex.getLocalizedMessage().toString());
            StatusDisplayer.getDefault().setStatusText("AgregarDocumentos(). Error " + ex.getMessage().toString());
        }

    }

    public String FechaToString(Date date) {
        try {
            if (date == null) {
                return "";
            }
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            int day = cal.get(Calendar.DATE);
            int month = cal.get(Calendar.MONTH) + 1;
            int year = cal.get(Calendar.YEAR);
            String sfecha;
            String sdia, smonth, syear;

            if (day < 10) {
                sdia = "0" + String.valueOf(day);
            } else {
                sdia = String.valueOf(day);
            }
            if (month < 10) {
                smonth = String.valueOf(month);
            } else {

                smonth = String.valueOf(month);
            }
            syear = String.valueOf(year);

            formatoFecha = formatoFecha.trim();
            if (formatoFecha.equals("dd/MM/aaaa")) {
                return sdia + "/" + month + "/" + syear;
            }

            if (formatoFecha.equals("MM/dd/aaaa")) {
                return smonth + "/" + sdia + "/" + syear;
            }

            if (formatoFecha.equals("aaaa/MM/dd")) {
                return syear + "/" + smonth + "/" + sdia;
            }
            if (formatoFecha.equals("dd/aaaa/MM")) {
                return sdia + "/" + syear + "/" + month;
            }
            if (formatoFecha.equals("MM/aaaa/dd")) {
                return smonth + "/" + syear + "/" + sdia;
            }
            if (formatoFecha.equals("aaaa/dd/MM")) {
                return syear + "/" + sdia + "/" + smonth;
            }


        } catch (Exception ex) {
            // JOptionPane.showMessageDialog(null, "FechaToString()\nMensaje: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            StatusDisplayer.getDefault().setStatusText("FechaToString(). Error " + ex.getMessage().toString());
            this.ListaMensajes.add("FechaToString() " + ex.getLocalizedMessage().toString());
        }
        return "";
    }

    public void procesarTabla(String tabla) {
        try {

        
            String name_couchdb = prefijo + tabla;
   
            if (ConectarCouchDB() == true) {

                if (CrearBaseDatos(name_couchdb) == true) {

                    if (AbrirBaseDatos(name_couchdb) == true) {
                         this.ListaMensajes.add("Migrando  : " + tabla +  " a " + name_couchdb);
                        stmt = conn.createStatement();
                        rs = stmt.executeQuery("select * from " + tabla);

                        mdata = rs.getMetaData(); //metadata
                        num_columnas = mdata.getColumnCount(); //numero columnas
                        migrar();
                    }

                    stmt.close();
                    rs.close();
                    //  conn.close();
                }
            }
        } catch (Exception ex) {

            StatusDisplayer.getDefault().setStatusText("procesarTabla(). Error " + ex.getMessage().toString());
            this.ListaMensajes.add("procesarTabla() " + ex.getLocalizedMessage().toString());
        }
    }

    private void migrar() {

        try {

            while (rs.next()) {
                Document doc = new Document();
                for (int i = 1; i <= num_columnas; i++) {   // Recorro las columnas


                    if (mdata.getColumnTypeName(i).equals("date")) {
                        String fecha = FechaToString(rs.getDate(i));
                        doc.put(mdata.getColumnLabel(i), fecha);
                    } else {
                        doc.put(mdata.getColumnLabel(i), rs.getString(i));
                    }
                }
                db.saveDocument(doc);
            }

        } catch (Exception ex) {

            StatusDisplayer.getDefault().setStatusText("migrar(). Error " + ex.getMessage().toString());
            this.ListaMensajes.add("migrar() " + ex.getLocalizedMessage().toString());
        }
    }
}
