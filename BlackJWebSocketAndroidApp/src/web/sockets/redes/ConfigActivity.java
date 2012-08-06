package web.sockets.redes;



import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Actividad de Login de la aplicación.
 * @author juanma
 *
 */
public class ConfigActivity extends Activity {

    private Button lBtnCancel; //botón de cancelar
    private Button lBtnSave; //botón de guardar
    private EditText lTxfURL; //texto de la URL
    private EditText lTxfUser; //texto con el nombre de usuario
    private EditText lTxfPass; //texto con la pass
    private Activity lInstance; //instancia de la actividad
    /**
	 * @uml.property  name="c"
	 * @uml.associationEnd  readOnly="true"
	 */
    private Constants c= new Constants(); //constantes para la con
    Intent  newActivity; //intent para lanzar la actividad
    
   
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.config);

        //se obtienen las referencias a la app
        lBtnCancel = (Button) findViewById(R.id.cfgBtnCancel);
        lBtnSave = (Button) findViewById(R.id.cfgBtnSave);
        lTxfURL = (EditText) findViewById(R.id.cfgTxfURL);
        lTxfUser = (EditText) findViewById(R.id.cfgTxfUsername);
        lTxfPass =  (EditText) findViewById(R.id.cfgTxfPassword);
        lInstance = this;

        //funcionalidad de cancelar
        lBtnCancel.setOnClickListener(new OnClickListener() {

			@Override
            public void onClick(View v) {

                Toast.makeText(getApplicationContext(), "DISCARDING...",
                        Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        //funcionalidad del botón de guardar
        lBtnSave.setOnClickListener(new OnClickListener() {

			@Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "SAVING...",
                        Toast.LENGTH_SHORT).show();

                JWC.setURL(lTxfURL.getText().toString());
                JWC.setUser(lTxfUser.getText().toString());
                JWC.setPass(lTxfPass.getText().toString());
                JWC.saveSettings(lInstance);
                Intent configIntent= new Intent(lInstance, WebSocketRedesActivity.class);
    			startActivityForResult(configIntent, c.LOGIN);
                finish();
            }
        });
        JWC.loadSettings(this);
        lTxfURL.setText(JWC.getURL());

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
