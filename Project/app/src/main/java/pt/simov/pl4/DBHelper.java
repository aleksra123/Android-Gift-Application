package pt.simov.pl4;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class DBHelper extends SQLiteOpenHelper {
    private static final String TAG = "DBHelper";
    private static final int DATABASE_VERSION = 1;
    private String createStatement = "";
    private String dropStatement = "";
    private String tableName;
    private Context context;

    public DBHelper(Context context, String tableName, String fields) {
        super(context, tableName, null, DATABASE_VERSION);
        this.createStatement  = "CREATE TABLE IF NOT EXISTS ";
        this.createStatement += tableName + " (";
        this.createStatement += fields + ");";
        this.tableName = tableName;
        dropStatement= "DROP TABLE IF EXISTS " + this.tableName;
        this.context = context;
    }
    @Override
    public void onCreate(SQLiteDatabase arg0) {
        Log.i(TAG, "onCreate: " + this.createStatement);
        arg0.execSQL(this.createStatement);
        insertData(arg0);
    }

    @Override
    public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
        Log.i(TAG, "onUpgrade: " + this.dropStatement);
        arg0.execSQL(this.dropStatement);
        onCreate(arg0);
    }
    /*!
	 * This method uses a structured xml file with data to fill database tables
	 */
    public void insertData(SQLiteDatabase db) {
        String sql;
        try {

            InputStream in = this.context.getResources().openRawResource(R.raw.data);
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = builder.parse(in, null);
            NodeList statements = doc.getElementsByTagName("statement");
            for (int i = 0; i < statements.getLength(); i++) {
                sql = "INSERT INTO " + this.tableName + " " + statements.item(i).getChildNodes().item(0).getNodeValue();
                db.execSQL(sql);
                sql = "";
            }
        } catch (Throwable t) {
            Toast.makeText(context, t.toString(), Toast.LENGTH_LONG).show();
        }
    }
}
