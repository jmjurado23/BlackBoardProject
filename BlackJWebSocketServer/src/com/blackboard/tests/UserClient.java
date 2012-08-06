package com.blackboard.tests;

import org.jwebsocket.api.WebSocketConnector;

/**
 * Clase que representa a un usuario de la aplicación tanto móvil como web
 * @author juanma
 *
 */
public class UserClient {

	/**
	 * @uml.property  name="name"
	 */
	public String name =""; //nombre del cliente
	/**
	 * @uml.property  name="con"
	 * @uml.associationEnd  
	 */
	public WebSocketConnector con; //socket con la conexión
	/**
	 * @uml.property  name="pass"
	 */
	public String pass =""; //pass de la conexión
	
	/**
	 * obtiene la pass
	 * @return  pass
	 * @uml.property  name="pass"
	 */
	public String getPass() {
		return pass;
	}

	/**
	 * define la pass de la conexión
	 * @param  pass
	 * @uml.property  name="pass"
	 */
	public void setPass(String pass) {
		this.pass = pass;
	}

	/**
	 * constructor de una conexión con el nombre y el conctor
	 * @param name
	 * @param con
	 */
	public UserClient(String name, WebSocketConnector con){
		this.name= name;
		this.con= con;
	}

	/**
	 * Constructor de la clase con el nombre, la conexión y la pass
	 * @param name
	 * @param con
	 * @param pass
	 */
	public UserClient(String name, WebSocketConnector con, String pass) {
		this.name= name;
		this.con= con;
		this.pass=pass;
	}

	/**
	 * Constructor vacío
	 */
	public UserClient() {
		
	}

	/**
	 * Obtiene el nombre del usuario
	 * @return
	 * @uml.property  name="name"
	 */
	public String getName() {
		return name;
	}

	/**
	 * define el nombre de la conexión
	 * @param  name
	 * @uml.property  name="name"
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * obtiene el socket con la conexión del cliente
	 * @return
	 * @uml.property  name="con"
	 */
	public WebSocketConnector getCon() {
		return con;
	}

	/**
	 * Define el socket con la conexión del cliente
	 * @param  con
	 * @uml.property  name="con"
	 */
	public void setCon(WebSocketConnector con) {
		this.con = con;
	}

	
	
}
