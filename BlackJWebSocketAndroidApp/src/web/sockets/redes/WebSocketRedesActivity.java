package web.sockets.redes;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;

import org.jwebsocket.api.WebSocketClientEvent;
import org.jwebsocket.api.WebSocketClientTokenListener;
import org.jwebsocket.api.WebSocketPacket;
import org.jwebsocket.kit.WebSocketException;
import org.jwebsocket.token.Token;
import org.jwebsocket.token.TokenFactory;

import web.socket.views.ChatView;
import web.socket.views.ImgView;
import web.socket.views.LogView;
import web.socket.views.MusicView;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ViewFlipper;

/**
 * Actividad principal de la aplicación que maneja todas las vistas de la misma
 * así como las conexiones de la app
 * @author juanma
 *
 */
public class WebSocketRedesActivity extends Activity implements OnClickListener,WebSocketClientTokenListener{

	
	/**
	 * @uml.property  name="c"
	 * @uml.associationEnd  readOnly="true"
	 */
	private Constants c; //constantes de la aplicación
	/**
	 * @uml.property  name="intPass"
	 */
	private int intPass; //contraseña interna de la conexión
	/**
	 * @uml.property  name="idList"
	 */
	private int idList;  //id de la lista de la con
	/**
	 * @uml.property  name="state"
	 */
	private int state; //estado de la conexion
	private ViewFlipper flipper; //flipper para manejar las vistas
	/**
	 * @uml.property  name="chatView"
	 * @uml.associationEnd  readOnly="true"
	 */
	private ChatView chatView; //vista del chat
	/**
	 * @uml.property  name="imgView"
	 * @uml.associationEnd  readOnly="true" inverse="activity:web.socket.views.ImgView"
	 */
	private ImgView imgView; //vista de las imágenes
	/**
	 * @uml.property  name="logView"
	 * @uml.associationEnd  readOnly="true"
	 */
	public  LogView logView; //vista del log
	/**
	 * @uml.property  name="musiciew"
	 * @uml.associationEnd  readOnly="true"
	 */
	public MusicView musiciew; //vista de las canciones
	private Button lBtnCloseSession; //botón de cerrar
	private Button lBtnSendChat; //botón de enviar chat
	private Button lBtnTextButton; //botón de texto
	private Button lBtnImgButton; //botón de  imágenes
	private Button lBtnLogButton; //botón de log
	private Button lBtnMusicButton; //botón de música
	private EditText lLog; //edit texxt de log
	private ProgressDialog pd; //diálogo de progreso
	/**
	 * @uml.property  name="activity"
	 * @uml.associationEnd  readOnly="true"
	 */
	private WebSocketRedesActivity activity; //actividad
	Date dat = new Date(); //fecha 
	
	
	 public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		//se obteienen los elementos de las vistas
		activity = this;
		lBtnCloseSession = (Button) findViewById(R.id.buttonLogout);
		lBtnTextButton = (Button) findViewById(R.id.buttonText);
		lBtnImgButton = (Button) findViewById(R.id.buttonImage);
		lBtnLogButton = (Button) findViewById(R.id.buttonLog);
		lBtnMusicButton = (Button) findViewById(R.id.buttonMusic);
		flipper = (ViewFlipper) findViewById(R.id.viewFlipper1);
	 
		
		//se cargan los listView
		chatView= new ChatView(getApplicationContext(),this);
        flipper.addView(chatView,0);
        imgView= new ImgView(getApplicationContext(),this);
        flipper.addView(imgView,1);
        musiciew = new MusicView(getApplicationContext(),this);
        flipper.addView(musiciew, 2);
        logView= new LogView(getApplicationContext(),this);
        flipper.addView(logView,3);

        //se muestra el Log
        flipper.setDisplayedChild(3);
        changeBack(3);
        
        lLog = logView.getEditTextLog(); //se obtiene el EditText para editarlo
        lBtnSendChat = chatView.lBtnSend; //se obtiene el botón del chat
		c = new Constants(); //inicialización de las constantes
	
		
		//se definen los listeners de los botones
		lBtnCloseSession.setOnClickListener(this);	
		lBtnTextButton.setOnClickListener(this);
		lBtnImgButton.setOnClickListener(this);
		lBtnLogButton.setOnClickListener(this);
		lBtnMusicButton.setOnClickListener(this);
		lBtnSendChat.setOnClickListener(this);
		
		//Inicio de la clase encargada de las conexiones y las preferencias de login
		JWC.init();
		
		log("* Abriendo... ");
		
		try {
			
			JWC.addListener(this); //se añade un listener
			JWC.open(); // se abre la conexión 
			state = c.DISCONECTED;  //se inicializa el estado de la app
			ConnectBlackServer();//se conecta con el server Black	
			
		} catch (WebSocketException ex) {
			log("* exception: " + ex.getMessage());
		}
	}

	 
	 
	/**
	 * Método para conectar con el Servidor
	 */
	private void ConnectBlackServer() {
		//se lanzan los tokens de logueo en el nuevo plugin
		Token lToken = TokenFactory.createToken(c.NS_BASE, "loginMobile"); 
        lToken.setString("userM", JWC.getmUser()); //user
        lToken.setString("passM", JWC.getmPass()); //pass
        try {
			JWC.sendToken(lToken);
		} catch (WebSocketException e) {
			e.printStackTrace();
		} 
	}

	@Override
	protected void onPause() {
		log("* closing... ");
		try {
			JWC.close(); //se cierra el Websocket
			JWC.removeListener(this); //se elimina el listener
		} catch (WebSocketException ex) {
			log("* exception: " + ex.getMessage());
		}
		super.onPause();
	}

	/**
	 * método para escribir en la consola del la app
	 * @param aString
	 */
	private void log(CharSequence aString) {
		try {
			lLog.append(aString);
		} catch (Exception ex) {
			Toast.makeText(getApplicationContext(), ex.getClass().getSimpleName(),
					Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * método que se ejecuta al abrir el WebSocket
	 */
	public void processOpened(WebSocketClientEvent aEvent) {
		log("opened\n");
	}
	
	
	/**
	 * Método para procesar un paquete que se recibe
	 */
	@Override
	public void processPacket(WebSocketClientEvent aEvent, WebSocketPacket aPacket) {
//		if( aPacket.getUTF8().toString().length() > 200)
//		log("> " + aPacket.getUTF8() + "\n");
	}

	/**
	 * Método para procesar los tokens según el asunto
	 */
	@Override
	public void processToken(WebSocketClientEvent aEvent, Token aToken) {
		if(aToken.toString().length() > 300);
		log("> --" + aToken.toString() + "\n");
		
		System.out.println(aToken.getObject("reqType"));
		
		if(aToken.getObject("reqType") != null) //comprobamos que es un método conocido
		{
			if(aToken.getString("reqType").equals("loginWeb")){
				if(state == c.WAITING){
					System.out.println("Conectamos con la web" );
					intPass = aToken.getInteger("connectionWebPass");
					System.out.println("int Pass " + intPass );
					state = c.CONNECTED;
				}
			}
			if(aToken.getString("reqType").equals("loginMobile")){
				System.out.println("Se envía una petición de log desde el móvil" );
				int id =aToken.getInteger("login_status");
				if(id == -1)
				{
					showDialog(888);
				}
				else
				{
					Toast.makeText(this, "Conectado", 10).show();
					System.out.println(" idList "+ idList);
					state = c.WAITING;
					idList = id;
				}
				
			}
			if(aToken.getString("reqType").equals("logoutMobile")){
				System.out.println("Se intenta cerrar la  conexión");
				idList= -1;
				state = c.DISCONECTED;
			}
			if(aToken.getString("reqType").equals("logoutWeb")){
				System.out.println("La web ha cerrado la conexión");
				state = c.WAITING;
			}
			if(aToken.getString("reqType").equals("sendMsgChatMobile")){
				Date dat = new Date();
				chatView.showMsg(aToken.getString("Msg"),"Movil - " + dat, 0);
				chatView.lMessage.setText("");
			}
			if(aToken.getString("reqType").equals("sendMsgChatWeb")){ 
				Date dat = new Date();
				chatView.showMsg(aToken.getString("Msg"),"Web - " + dat , 1);
				chatView.lMessage.setText("");
			}
		}
		
	}

	@Override
	public void processClosed(WebSocketClientEvent aEvent) {
		log("closed\n");
		showDialog(999);
		
	}

	@Override
	public void processOpening(WebSocketClientEvent aEvent) {
		log("* opening... ");
	}

	@Override
	public void processReconnecting(WebSocketClientEvent aEvent) {
		log("* reconnecting... ");
	}

	//definici�n de un objeto de la clase di�logo para mostrar los cuadros de di�logo que se muestran por pantalla
		protected Dialog onCreateDialog(int id) 
	    { 
	        switch (id) 
	        { 
	         case (999): 
	            return new AlertDialog.Builder(this) 
	                .setTitle(R.string.disconnect) 
	                .setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() { 
	                    public void onClick(DialogInterface dialog, int whichButton) { 
	                    	
	                    	
	                    	finish();
	                    } 
	                })
	       
	                .create(); 
	         
	         	case (888): 
		            return new AlertDialog.Builder(this) 
		                .setTitle(R.string.reconnect) 
		                .setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() { 
		                    public void onClick(DialogInterface dialog, int whichButton) { 
		                    	
		                    	
		                    	finish();
		                    } 
		                })
		       
		                .create(); 
	        } 
	        return null; 
	        } 
	@Override
	public void onClick(View v) {
		
		//Botón de cerrar sesión
		if(v.equals(lBtnCloseSession)){
			try {
			 Token lToken = TokenFactory.createToken(c.NS_BASE, "logoutMobile");
             lToken.setInteger("intPass", intPass);
             lToken.setInteger("idList", idList);
             
				JWC.sendToken(lToken);
			} catch (WebSocketException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			this.finish();
		}
		
		//Botón de enviar mensage
		if(v.equals(lBtnSendChat)){
			if(state == c.CONNECTED){
				Token lToken = TokenFactory.createToken(c.NS_BASE, "sendMsgChatMobile"); 
		        lToken.setString("Msg", chatView.lMessage.getText().toString()); //user
		        lToken.setInteger("intPass", intPass);
	            lToken.setInteger("idList", idList);
		        try {
					JWC.sendToken(lToken);
				} catch (WebSocketException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} //se envía el token
			}
			else
				Toast.makeText(this, "Usted no ha conectado un cliente Web aún", 10).show();
		}
		
		//botón de vista de chat
		if(v.equals(lBtnTextButton)){
			flipper.setDisplayedChild(0);
			chatView.getViewList().setVisibility(View.VISIBLE);
			changeBack(0);
		}
		
		//botón de vista de img
		if(v.equals(lBtnImgButton)){
			flipper.setDisplayedChild(1);
			chatView.getViewList().setVisibility(View.GONE);
			changeBack(1);
		}
		
		//botón de vista de log
		if(v.equals(lBtnLogButton)){
			flipper.setDisplayedChild(3);
			chatView.getViewList().setVisibility(View.GONE);
			changeBack(3);
		}
		//botón de vista de Draw
		if(v.equals(lBtnMusicButton)){
			flipper.setDisplayedChild(2);
			chatView.getViewList().setVisibility(View.GONE);
			changeBack(2);
		}
	}
	
	/**
	 * Método para cambiar el fondo de los iconos de abajo de la app
	 * @param i
	 */
	private void changeBack(int i) {
		
		if(i==0){
			lBtnTextButton.setBackgroundResource(R.drawable.selecticon);
			lBtnImgButton.setBackgroundColor(0x00000000);
			lBtnLogButton.setBackgroundColor(0x00000000);
			lBtnMusicButton.setBackgroundColor(0x00000000);
		}
		else if(i==1){
			lBtnTextButton.setBackgroundColor(0x00000000);
			lBtnImgButton.setBackgroundResource(R.drawable.selecticon);
			lBtnLogButton.setBackgroundColor(0x00000000);
			lBtnMusicButton.setBackgroundColor(0x00000000);
		}
		else if(i==2){
			lBtnTextButton.setBackgroundColor(0x00000000);
			lBtnImgButton.setBackgroundColor(0x00000000);
			lBtnLogButton.setBackgroundColor(0x00000000);
			lBtnMusicButton.setBackgroundResource(R.drawable.selecticon);
		}
		else if(i==3){
			lBtnTextButton.setBackgroundColor(0x00000000);
			lBtnImgButton.setBackgroundColor(0x00000000);
			lBtnLogButton.setBackgroundResource(R.drawable.selecticon);
			lBtnMusicButton.setBackgroundColor(0x00000000);
		}
	}



	/**
	 * Sobrecarga del método de presionar tecla
	 */
	public boolean onKeyDown(int keyCode, KeyEvent event) {
    	
    	if(keyCode == KeyEvent.KEYCODE_BACK) //si la tecla es la de retroceso
    	{
    		if(state == c.CONNECTED || state == c.WAITING){
    			try {
    				 Token lToken = TokenFactory.createToken(c.NS_BASE, "logoutMobile");
    	             lToken.setInteger("intPass", intPass);
    	             lToken.setInteger("idList", idList);
    	             
    					JWC.sendToken(lToken);
    				} catch (WebSocketException e) {
    					// TODO Auto-generated catch block
    					e.printStackTrace();
    				}
    		}
    		this.finish();
    	}
		return false;
	}
	
	/**
	 * Método para enviar música al servidor
	 * @param data dirección de la canción
	 * @param name nombre de la canción
	 */
	public void sendMusic(final String data, final String name){
		
		if(state == c.CONNECTED){
			
		
			//se inicializa el diálogo de espera
			pd= new ProgressDialog(this);
			pd.setTitle(R.string.waiting);
	     	pd.setMessage(""+ getString(R.string.sendmusic) + data);
			pd.show();
			
			//se crea un hilo para enviar el archivo
			new Thread() {
				 
	            public void run() { 
	           	 
	            	try {
	        			
	            		//se abre el archivo 
	        			File file = new File(data);
	        			FileInputStream fis = new FileInputStream(file);
	        			
	        			//se pasa a Bytes el archivo
	        			byte file_data[] = new byte[(int) file.length()];
	        			int data_stream, index=0;
	        			
	        			while((data_stream = fis.read())!= -1){
	        				file_data[index] = (byte) data_stream;
	        				index++;
	        			}
	        			
	        			if(state == c.CONNECTED){ // si el estado es conectado,s e envía
	        				Token lToken = TokenFactory.createToken(c.NS_BASE, "sendMusic");
	        				//el envío ha de hacerse en base 64 para que la web lo pueda tratar
	        		        lToken.setString("data", new String(Base64.encode(file_data,Base64.DEFAULT)) ); //user
	        		        lToken.setInteger("intPass", intPass);
	        	            lToken.setInteger("idList", idList);
	        	            lToken.setString("name",name);
	        	            lToken.setString("MIME","audio/ogg");
	        	            lToken.setString("extension", data.substring(data.length()-4, data.length()));
	        		        try {
	        					JWC.sendToken(lToken);
	        				} catch (WebSocketException e) {
	        					e.printStackTrace();
	        				} 
	        			}
	        			else
	        				Toast.makeText(activity, "Usted no ha conectado un cliente Web aún", 10).show();
	        			
	        			
	        		} catch (FileNotFoundException e) {
	        			e.printStackTrace();
	        		} catch (IOException e) {
	        			e.printStackTrace();
	        		}
	              	 
	            	pd.dismiss(); // se oculta el reloj de progreso
	        
	            }
	            
	        }.start(); //iniciamos el hilo
	        
		}
		else
			Toast.makeText(this, "Usted no ha conectado un cliente Web aún", 10).show();
	}
	
	/**
	 * Método para enviar un mensaje a la dir de token especificada en la clase
	 * @param data dirección de la imagen
	 * @param name nombe de la imagen
	 */
	public void sendImg(final String data, final String name){
		
		if(state == c.CONNECTED){ // si la conxión está creada se envía la imagen
			
		
			//se inicializa el diálogo de espera
			pd= new ProgressDialog(this);
			pd.setTitle(R.string.waiting);
	     	pd.setMessage(""+ getString(R.string.sendImage) + data);
			pd.show();
			
			//se crea un hilo para enviar el archivo
			new Thread() {
				 
	            public void run() { 
	           	 
	            	try {
	        			
	            		//se abre el fichero
	        			File file = new File(data);
	        			FileInputStream fis = new FileInputStream(file);
	        			
	        			// se pasa el contenido a bytes
	        			byte file_data[] = new byte[(int) file.length()];
	        			int data_stream, index=0;
	        			
	        			while((data_stream = fis.read())!= -1){
	        				file_data[index] = (byte) data_stream;
	        				index++;
	        			}
	        			
	        			if(state == c.CONNECTED){ // si el estado es conectado, se crea el token y se envía
	        				Token lToken = TokenFactory.createToken(c.NS_BASE, "sendImage"); 
	        		        lToken.setString("data", new String(Base64.encode(file_data,Base64.DEFAULT)) ); //user
	        		        lToken.setInteger("intPass", intPass);
	        	            lToken.setInteger("idList", idList);
	        	            lToken.setString("name",name);
	        	            lToken.setString("MIME","data:image/png;base64,");
	        	            lToken.setString("extension", data.substring(data.length()-4, data.length()));
	        		        try {
	        					JWC.sendToken(lToken);
	        				} catch (WebSocketException e) {
	        					// TODO Auto-generated catch block
	        					e.printStackTrace();
	        				} //se envía el token
	        			}
	        			else
	        				Toast.makeText(activity, "Usted no ha conectado un cliente Web aún", 10).show();
	        			
	        			
	        		} catch (FileNotFoundException e) {
	        			// TODO Auto-generated catch block
	        			e.printStackTrace();
	        		} catch (IOException e) {
	        			// TODO Auto-generated catch block
	        			e.printStackTrace();
	        		}
	              	
	            	//se oculta el diálogo de progreso
	            	pd.dismiss();
	        
	            }
	            
	        }.start();// searranca el hilo
	        
		}
		else
			Toast.makeText(this, "Usted no ha conectado un cliente Web aún", 10).show();

	}
}