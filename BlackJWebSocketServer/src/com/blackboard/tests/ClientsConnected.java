package com.blackboard.tests;

import java.util.Random;

/**
 * Clase que contiene una conexión de dos usuarios, uno móbil y otro web además de un estado de 
 * la conexión
 * @author juanma
 *
 */
public class ClientsConnected {

	/**
	 * @uml.property  name="mobileClient"
	 * @uml.associationEnd  
	 */
	UserClient mobileClient; //usuario móbil
	/**
	 * @uml.property  name="webClient"
	 * @uml.associationEnd  
	 */
	UserClient webClient; //usuario web
	/**
	 * @uml.property  name="c"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	Constants c = new Constants(); //constantes
	/**
	 * @uml.property  name="state"
	 */
	int state ; //estado de la conexión 
	/**
	 * @uml.property  name="internalPass"
	 */
	private int internalPass;
	
	/**
	 * get del usuario móvil
	 * @return  usuario móvil
	 * @uml.property  name="mobileClient"
	 */
	public UserClient getMobileClient() {
		return mobileClient;
	}

	/**
	 * set del usuario móvil
	 * @param mobileClient  cliente móbil
	 * @uml.property  name="mobileClient"
	 */
	public void setMobileClient(UserClient mobileClient) {
		this.mobileClient = mobileClient;
		this.state = c.WAITING;
	}

	/**
	 * get del cliente Web 
	 * @return  cliente web
	 * @uml.property  name="webClient"
	 */
	public UserClient getWebClient() {
		return webClient;
	}
	
	/**
	 * set del cliente Web
	 * @param webClient  cliente web
	 * @uml.property  name="webClient"
	 */
	public void setWebClient(UserClient webClient) {
		this.webClient = webClient;
	}

	/**
	 * devuelve el estado de la conexión
	 * @return  estado de la conexión
	 * @uml.property  name="state"
	 */
	public int getState() {
		return state;
	}

	/**
	 * método que define el estado de la conexión
	 * @param state  estado de la conexión
	 * @uml.property  name="state"
	 */
	public void setState(int state) {
		this.state = state;
	}

	
	/**
	 * Constructir de los usuarios de la conexión
	 * @param mobileC
	 * @param webC
	 */
	public ClientsConnected(UserClient mobileC, UserClient webC){
		this.mobileClient = mobileC;
		this.webClient = webC;
		state = c.CONNECTED;
		Random r = new Random();
		internalPass = r.nextInt(10000);
	}
	
	/**
	 * Constructor de la conexión por parte del móbil
	 * @param mobileC
	 */
	public ClientsConnected(UserClient mobileC){
		this.mobileClient = mobileC;
		this.webClient = new UserClient();
		state = c.WAITING;
	}
	
	/**
	 * Constructor vacío de la clase
	 */
	public ClientsConnected(){
		state = c.EMPTY;
	}
	
	/**
	 * Método para conectar a un cliente Web a uno móbil
	 * @param webC
	 * @return estado de la conexión
	 */
	public int connectWebClient(UserClient webC){
		System.out.println("Un clienteWeb se ha conectado con el usuario móvil");
		if(state == c.WAITING){
			this.webClient = webC;
			state = c.CONNECTED;
			Random r = new Random();
			internalPass = r.nextInt(100000); //se genera un número al azar
			
			return internalPass; //se han conectado con éxito
		}
		else
			return -1; //se ha producido un error en la conexión
	}
	
	/**
	 * Método para comprobar la internal pass de la aplicación 
	 * @param internalPass
	 * @return boleano con el resultado de la comprobación
	 */
	public boolean comprobateInternalPass(int internalPass){
		if(internalPass == this.internalPass)
			return true;
		else 
			return false;
	}

	/**
	 * Se cierra la conexión , poniendo el estado a EMPTY, ahora se puede sobreescribir
	 */
	public void closeSession() {
		state = c.EMPTY;
		
	}

	/**
	 * @return
	 * @uml.property  name="internalPass"
	 */
	public int getInternalPass() {
		
		return internalPass;
	}

	public void closeSessionWeb() {
		state = c.WAITING;
		
	}
}
