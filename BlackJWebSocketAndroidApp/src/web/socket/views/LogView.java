package web.socket.views;



import web.sockets.redes.R;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.view.View.OnClickListener;

/**
 * Clase para mostrar le log de la aplicación
 * @author juanma
 *
 */
public class LogView extends LinearLayout implements OnClickListener{
	

	Button lBtnClearLog;
	private EditText lLog;
	Activity activity;

	/**
	 * Constructor de la clase 
	 * @param context
	 * @param act
	 */
	public LogView(Context context, Activity act) {
		super(context);
		this.activity = act;
		initialize();
	}

	/**
	 * Método para inicializar los elementos de la clase
	 */
	private void initialize() {
		final LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.log, this);
		
		// se obtienen las referencias a los elementos
		lBtnClearLog = (Button) findViewById(R.id.clearLogButton);
		lLog = (EditText) findViewById(R.id.TextViewLog);

		//se define el listener de los botones
		lBtnClearLog.setOnClickListener(this);
		
	}

	public EditText getEditTextLog(){
		return lLog;
	}
	@Override
	public void onClick(View v) {
		if(v.equals(lBtnClearLog)){
			lLog.setText("");
		}
		
	}
}
