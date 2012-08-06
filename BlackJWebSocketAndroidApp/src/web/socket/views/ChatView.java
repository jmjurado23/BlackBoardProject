package web.socket.views;

import java.util.ArrayList;
import java.util.List;
import web.socket.classes.ViewWrapperChatRow;
import web.sockets.redes.R;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Clase que contiene la vista usada para el chat de la aplciación.
 * @author juanma
 *
 */
public class ChatView extends LinearLayout implements OnClickListener{
	
	public Button lBtnSend; //botón de enviar
	public Button lBtnClearLog; //botón de limpiar Log
	public Button lBtnCloseSession; //botón de cerrar sesión 
	public Button lBtnClearChatButton; //botón de limpiar el chat
	public EditText lMessage; //cuadro de texto con los mensajes
	Activity activity; //actividad para lanzar aciones
	ListView list; //lista con los diálogos
	String[] msgs; //vector con los mensajes
	ArrayAdapter<Msg> listAdapter; //adaptador de la lista
	ArrayList<Msg> msgList; //lista con los mensajes

	
	/**
	 * Constructor de la clase
	 * @param context
	 * @param act
	 */
	public ChatView(Context context, Activity act) {
		super(context);
		this.activity = act;
		initialize();
	}

	/**
	 * Método para inicializar los elementos de la vista
	 */
	private void initialize() {
		final LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.chat, this);
		
		//se asocia cada elemento con un botón
		lBtnSend = (Button) findViewById(R.id.sendbutton);
		lBtnClearChatButton = (Button) findViewById(R.id.clearChatButton);
		lBtnCloseSession = (Button)	findViewById(R.id.buttonLogout);
		lMessage = (EditText) findViewById(R.id.editTextMessage);
		list = (ListView) findViewById(R.id.listView1);
		
		
		//se genera una lista con las preferencias
		msgList = new ArrayList<Msg>();

		//se crea un adaptador para listas de los msgs
		listAdapter = new MsgArrayAdapter(this.getContext(),
				 android.R.layout.simple_list_item_1,msgList);
		
		//se define el adaptador del listView
		list.setAdapter(listAdapter);
		
		lBtnClearChatButton.setOnClickListener(this);
	}
	
	/**
	 * Método para devolver el texto 
	 * @return
	 */
	public EditText getEditTextMsg(){
		return lMessage;
	}
	
	/**
	 * función para mostrar un msg en la lista
	 * @param msg
	 * @param origin
	 */
	public void showMsg(String msg,String origin, int source){
		msgList.add(new Msg(msg,origin,source));
		list.setAdapter(listAdapter);
	}
	
	/**
	 * Clase con los datos de un mensaje
	 * @author   juanma
	 */
	private static class Msg{
		public String msg;
		public String origin;
		/**
		 * @uml.property  name="source"
		 */
		public int source = 0;
		
		/*
		 * source 1 = web
		 * source 0 = movil
		 */
		
		public Msg(String msg, String origin, int source){
			this.msg= msg;
			this.origin= origin;
			this.source = source;
		}
		/**
		 * @return
		 * @uml.property  name="source"
		 */
		public int getSource() {
			return source;
		}
		/**
		 * @param source
		 * @uml.property  name="source"
		 */
		public void setSource(int source) {
			this.source = source;
		}
		public String getMsg() {
			return msg;
		}
		public void setMsg(String msg) {
			this.msg = msg;
		}
		public String getOrigin() {
			return origin;
		}
		public void setOrigin(String origin) {
			this.origin = origin;
		}
		
		
	}
	
	
	/**
	 * Adaptador personalizado para los elementos de una lista de mensajes
	 * @author juanma
	 *
	 */
	private static class MsgArrayAdapter extends ArrayAdapter<Msg> {

		private LayoutInflater inflater;

		public MsgArrayAdapter(Context context,int textViewResourceId,
				List<Msg> msgList) {
			
			super(context, textViewResourceId, msgList);
			inflater = LayoutInflater.from(context);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {


			final TextView textviewtitle; //título mensaje
			TextView textviewtitle2; //subtítulo (fecha y origen)
			Msg msg1 = (Msg) this.getItem(position); //mensaje
			
			if (convertView == null) { //si no está definido se define
				if(msg1.getSource() == 1) //según el origen se define el estilo
					convertView = inflater.inflate(R.layout.chatrow, null);
				else
					convertView = inflater.inflate(R.layout.chatrow2, null);
				
				//se crea el wrapper para dar los elementos de la vista
				ViewWrapperChatRow wrapper = new ViewWrapperChatRow(
						convertView);
				textviewtitle = wrapper.getTitle();
				textviewtitle2 = wrapper.getSubTitle();

				//se definene las etiquetas
				convertView.setTag(new ChatViewHolder(textviewtitle,
						textviewtitle2));

			} else {  // sino, se aprovecha para no tener que definir (acelera )

				ChatViewHolder viewHolder = (ChatViewHolder) convertView
						.getTag();
				textviewtitle = viewHolder.getTextTitle();
				textviewtitle2 = viewHolder.getTextSubtitle();
			}

			//se definen los mensajes
			textviewtitle.setText(msg1.getMsg());
			textviewtitle2.setText(msg1.getOrigin());

			//se devuelve la vista convertida al formato
			return convertView;
		}

	}
	
	/**
	 * Clase para menejar los elementos del chat
	 * @author   juanma
	 */
	private static class ChatViewHolder {  
	    private TextView textTitle;  
	    private TextView textSubtitle;
	    
	    public ChatViewHolder(TextView textTitleView, TextView textSubtitle ) {  
	      this.textTitle = textTitleView;
	      this.textSubtitle = textSubtitle;
	    }
		
		public TextView getTextTitle() {
			return textTitle;
		}
		@SuppressWarnings("unused")
		public void setTextTitle(TextView textTitle) {
			this.textTitle = textTitle;
		}
		public TextView getTextSubtitle() {
			return textSubtitle;
		}
		@SuppressWarnings("unused")
		public void setTextSubtitle(TextView textSubtitle) {
			this.textSubtitle = textSubtitle;
		}
	}

	@Override
	public void onClick(View v) {
		if(v.equals(lBtnClearChatButton)){
			lMessage.setText("");
		}
		
	}

	public ListView getViewList() {
			
		return list;
	}

	
		
	
}
