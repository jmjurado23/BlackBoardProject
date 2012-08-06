package com.blackboard.tests;

import java.util.List;

import org.apache.log4j.Logger;
import org.jwebsocket.api.PluginConfiguration;
import org.jwebsocket.api.WebSocketConnector;
import org.jwebsocket.kit.PlugInResponse;
import org.jwebsocket.logging.Logging;
import org.jwebsocket.plugins.TokenPlugIn;
import org.jwebsocket.token.Token;

/**
 * Plugin de jWebSocket que implementa las funciones necesarias para 
 * la gestión de usuarios en el sistema.
 * @author juanma
 *
 */
public class BlackPlugIn extends TokenPlugIn {
	
	//cambiar el looger de apache a BlackPlugin
	private static Logger mLog = Logging.getLogger(BlackPlugIn.class);
	private final static String NS_SAMPLE = "com.blackboard.tests.BlackPlugIn";
	/**
	 * @uml.property  name="con"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private Connections con;
	
	//Constructor
	public BlackPlugIn(PluginConfiguration aConfiguration) {
		super(aConfiguration);
		if (mLog.isDebugEnabled()) {
			mLog.debug("Instalando el plugin BlackBoard ...");
		}
		
		//se inicializan las connexiones
		con = new Connections();
		
		// especificamos el espacio de nombres al ejemplo
		this.setNamespace(NS_SAMPLE);
	}
	
	/**
	 * Método que es llamado cuando se recibe un token en el espacio de nombres especificado
	 */
	@Override
	public void processToken(PlugInResponse aResponse, WebSocketConnector aConnector, Token aToken) {

		
		String lType = aToken.getType();//se mira el tipo del token recibido
		String lNS = aToken.getNS();//se comprueba el espacio de nombre del Token
		
		
		// se comprueba que los datos no sean nulos ni incorrectos
		if (lType != null && lNS != null && lNS.equals(getNamespace())) {
			
			//método para dar el autor del programa (se puede usar como prueba de conexión)
			if (lType.equals("getAuthorName")) { //si es para obtener el nombre del autor
				mLog.debug("Se ha pedido el nombre del autor");
				Token lResponse = createResponse(aToken);//se crea la respuesta
				lResponse.setString("name", " Juanma Jurado ");//se añade el nombre 
				sendToken(aConnector, aConnector, lResponse);//se envía la respuesta
			}
			
			//método para loguear()
			if (lType.equals("loginMobile")) { 
				
				mLog.debug("Un usuario móbil está loguenado");
				
				String user= aToken.getString("userM");
				String pass= aToken.getString("passM");
				
				//comprobamos que nadie se haya logueado con esos datos
				if(!con.testNamePassExist(user,pass)){
					con.createConnectionUserMobile(user, aConnector, pass);
					
					con.showConnections();
					Token lResponse = createResponse(aToken);//se crea la respuesta con el id de la con
					lResponse.setInteger("login_status", 
							con.findUsersConnected(user, pass));//se crea una respuesta con el id de la Lista
					sendToken(aConnector, aConnector, lResponse);//se envía la respuesta
				}
				else{
					Token lResponse = createResponse(aToken);//se crea la respuesta con el id de la con
					lResponse.setInteger("login_status", 
							-1);
					sendToken(aConnector, aConnector, lResponse);
				}
					
			}
			
			//método para loguear la web
			if(lType.equals("loginWeb")){
				
				mLog.debug("Se ha recibido una petición desde la web para emparejar con una web");
				
				String user= aToken.getString("userW"); //se obtiene el user
				String pass= aToken.getString("passW"); //se obtiene el pass
				
				mLog.debug("Intento de conexión del usuario: "+user+ " con la contraseña "+pass);
				mLog.debug("Se han extraido los campos correctamente");
				
				int idList = con.findUsersConnected(user, pass);
				
		
				if(idList != -1) //si el user y la pass existen
				{
					
					System.out.println("El usuario existe en el sistema");
					int internalPass = con.connectUsersWeb(idList,user,aConnector , pass);
					
					//respuesta a la web
					Token lResponse = createResponse(aToken);//se crea la respuesta
					lResponse.setInteger("intPass", internalPass);
					lResponse.setInteger("login_status", idList);//se añade el id
					sendToken(aConnector, aConnector, lResponse);//se envía la respuesta
					
					//respuesta al móvil
					Token lResponse2 = createResponse(aToken);//se crea la respuesta
					lResponse2.setInteger("connectionWebPass",internalPass);//se añade el nombre 
					//se informa al móvil que se ha conectado un usuario web
					sendToken(con.getConList().get(idList).getMobileClient().getCon(),
							con.getConList().get(idList).getMobileClient().getCon(),
							lResponse2);
					
				}
				else //si no existe el user o ya no está esperando
				{
					System.out.println("El intento de conexión ha fallado. No existen users con esos datos");
					Token lResponse = createResponse(aToken);//se crea la respuesta
					
					lResponse.setInteger("login_status", idList);//se añade el id con -1
					sendToken(aConnector, aConnector, lResponse);//se envía la respuesta
					
					
				}
				
			}
			//desconectar el móvil
			if(lType.equals("logoutMobile")){
				
				int idList = aToken.getInteger("idList"); //se obtiene el ID de la lista
				int intPass = aToken.getInteger("intPass"); //se obtiene la Pass interna
				
				System.out.println("Un usuario móvil intenta desconectarse" + intPass);
				
				Token lResponse = createResponse(aToken);//se crea la respuesta
				
				
				System.out.println("La passinterna es "+ intPass);
				System.out.println("el resultado que se ha obtenido es " + con.getConList().get(idList).getInternalPass());
				
				//se comprueba que existe la conexión
				if(con.getConList().get(idList).comprobateInternalPass(intPass)){
				
					con.getConList().get(idList).closeSession(); //se cierra sesión
					lResponse.setString("CloseResult", "OK");  
				}
				else
					lResponse.setString("CloseResult", "KO");
				
				//enviar las respuestas
				sendToken(aConnector, aConnector, lResponse);
				sendToken(con.getConList().get(idList).getWebClient().getCon(),
						con.getConList().get(idList).getWebClient().getCon(),
						lResponse);
				
			}
			
			//desconectar el móvil
			if(lType.equals("logoutWeb")){
				
				int idList = aToken.getInteger("idList"); //se obtiene el ID de la lista
				int intPass = aToken.getInteger("intPass"); //se obtiene la Pass interna
				
				System.out.println("Un usuario web intenta desconectarse" + intPass);
				
				Token lResponse = createResponse(aToken);//se crea la respuesta
				
				System.out.println("La passinterna es "+ intPass);
				System.out.println("el resultado que se ha obtenido es " + con.getConList().get(idList).getInternalPass());
				
				//se comprueba que existe la conexión
				if(con.getConList().get(idList).comprobateInternalPass(intPass)){
				
					con.getConList().get(idList).closeSessionWeb();
					lResponse.setString("CloseResult", "OK"); 
				}
				else
					lResponse.setString("CloseResult", "KO");
				
				//enviar las respuestas
				sendToken(aConnector, aConnector, lResponse);
				sendToken(con.getConList().get(idList).getMobileClient().getCon(),
						con.getConList().get(idList).getMobileClient().getCon(),
						lResponse);
				
			}
			
			//método para enviar un mensage a la web
			if(lType.equals("sendMsgChatMobile")){
				
				mLog.debug("Se ha recibido un mensaje de chat para la web");
				
				int idList = aToken.getInteger("idList"); //se obtiene el ID de la lista
				int intPass = aToken.getInteger("intPass"); //se obtiene la Pass interna
				String msg = aToken.getString("Msg"); //se obtiene el mensaje
				
				Token lResponse = createResponse(aToken);
				
				//se comprueba que existe la conexión
				if(con.getConList().get(idList).comprobateInternalPass(intPass)){
				
					lResponse.setString("Msg", msg);
					
					//enviar las respuestas
					sendToken(aConnector, aConnector, lResponse);
					sendToken(con.getConList().get(idList).getWebClient().getCon(),
							con.getConList().get(idList).getWebClient().getCon(),
							lResponse);
				}
				
			}
			
			//Enviar un mensage al dispositivo móvil
			if(lType.equals("sendMsgChatWeb")){
				
				mLog.debug("Se ha recibido un mensaje de chat para el móvil");
				
				int idList = aToken.getInteger("idList"); //se obtiene el ID de la lista
				int intPass = aToken.getInteger("intPass"); //se obtiene la Pass interna
				String msg = aToken.getString("Msg"); //se obtiene el mensaje
				
				Token lResponse = createResponse(aToken);
				
				//se comprueba que existe la conexión
				if(con.getConList().get(idList).comprobateInternalPass(intPass)){
				
					lResponse.setString("Msg", msg);
					
					//enviar las respuestas
					sendToken(aConnector, aConnector, lResponse);
					sendToken(con.getConList().get(idList).getMobileClient().getCon(),
							con.getConList().get(idList).getMobileClient().getCon(),
							lResponse);
				}
				
			}
			//Enviar canción al cliente Web
			if(lType.equals("sendMusic")){
				
				mLog.debug("Enviar canción al cliente Web");
				
				int idList = aToken.getInteger("idList");
				int intPass = aToken.getInteger("intPass");
				String  music = aToken.getString("data");
				String MIME = aToken.getString("MIME");
				String name = aToken.getString("name");
				String extension = aToken.getString("extension");
				Token lResponse = createResponse(aToken);
				
				//se comprueba que existe la conexión
				if(con.getConList().get(idList).comprobateInternalPass(intPass)){
				
					lResponse.setString("data", music);
					lResponse.setString("MIME",MIME);
					lResponse.setString("extension", extension);
					lResponse.setString("name", name);
					//enviar las respuestas
					sendToken(aConnector, aConnector, lResponse);
					
					//se envía el audio al cliente en forma de lista
					lResponse.setString("Music", music);
					sendToken(con.getConList().get(idList).getWebClient().getCon(),
							con.getConList().get(idList).getWebClient().getCon(),
							lResponse);
				}
				
			}
			
			//se envía una imagen
			if(lType.equals("sendImage")){
				
				mLog.debug("Enviar imagen al cliente Web");
				
				int idList = aToken.getInteger("idList");
				int intPass = aToken.getInteger("intPass");
				String  image = aToken.getString("data");
				String MIME = aToken.getString("MIME");
				String name = aToken.getString("name");
				String extension = aToken.getString("extension");
				Token lResponse = createResponse(aToken);
				
				System.out.println("El nombre de la canción es " + name );
				
				//se comprueba que existe la conexión
				if(con.getConList().get(idList).comprobateInternalPass(intPass)){
				
					lResponse.setString("data", image);
					lResponse.setString("MIME",MIME);
					lResponse.setString("extension", extension);
					lResponse.setString("name", name);
					//enviar las respuestas
					sendToken(aConnector, aConnector, lResponse);
					
					//se envía una imagen al servidor
					lResponse.setString("Image", image);
					sendToken(con.getConList().get(idList).getWebClient().getCon(),
							con.getConList().get(idList).getWebClient().getCon(),
							lResponse);
				}
				
			}
			
			
			
				
		}

	}
}