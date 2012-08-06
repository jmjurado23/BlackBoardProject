package com.blackboard.tests;

import java.util.ArrayList;
import java.util.List;

import org.jwebsocket.api.WebSocketConnector;

/**
 * Clase que gestiona la conexiones creadas en el servidor
 * @author juanma
 *
 */
public class Connections {

	/**
	 * @uml.property  name="conList"
	 * @uml.associationEnd  multiplicity="(0 -1)" elementType="com.blackboard.tests.ClientsConnected"
	 */
	List <ClientsConnected> conList; //lista con los clientes conectados
	/**
	 * @uml.property  name="c"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	Constants c = new Constants();
	
	public Connections() {
		// TODO Auto-generated constructor stub
		conList = new ArrayList<ClientsConnected>();
	}

	/**
	 * obtiene la lista de conexiones
	 * @return Lista con las conexiones
	 */
	public List<ClientsConnected> getConList() {
		return conList;
	}

	/**
	 * define la lista con las conexiones
	 * @param conList 
	 */
	public void setConList(List<ClientsConnected> conList) {
		this.conList = conList;
	}
	
	/**
	 * Encuentra la posición en la lista de un cliente en espera y devuelve la pos 
	 * @param user 
	 * @param pass
	 * @return entero con la posición en la lista del usuario
	 */
	public int findUsersConnected(String user, String pass){
		for(int i=0; i< conList.size();i++){
			System.out.println(conList.size() + "-- " +conList.get(i).getState() + " " + conList.get(i).getMobileClient().getName()
					+ " " +  conList.get(i).getMobileClient().getPass() + " " + user + " " + pass);
			if(conList.get(i).getMobileClient().getName().equals(user) &&
					conList.get(i).getMobileClient().getPass().equals(pass) &&
					conList.get(i).getState()==c.WAITING) {
				System.out.println("Logueadoooo web");
				return i;
			}
		}
		
		return -1;
	}
	
	/**
	 * Encuentra un lugar en la lista que no tenga conexiones activas y lo devuelve
	 * @param user
	 * @param pass
	 * @return posición libre en la lista
	 */
	public int findUsers(String user, String pass){
		for(int i=0; i< conList.size();i++){
			if(conList.get(i).getMobileClient().getName().equals(user) &&
					conList.get(i).getMobileClient().getPass().equals(pass) &&
					conList.get(i).getState()==c.EMPTY) {
				return i;
			}
		}
		
		return -1;
	}
	
	/**
	 * Conecta un usuario Web con un usuario móbil que ya está conectado
	 * @param idList posición en la que se encuentra el usuario
	 * @param name nombre del usuario
	 * @param con conector del usuario web
	 * @param pass contraseña del usuario web
	 * @return
	 */
	public int connectUsersWeb(int idList, String name, WebSocketConnector con, String pass){
		try{
			return conList.get(idList).connectWebClient(new UserClient(name, con, pass));
			
		}catch(Exception e){
			System.out.println(e);
			return -1;
		}
		
	}
	
	/**
	 * Se crea una conexión en espera para el usuario móbil
	 * @param name
	 * @param con
	 * @param pass
	 * @return entero con la posición de la lista
	 */
	public int createConnectionUserMobile(String name, WebSocketConnector con, String pass){
		
		for(int i=0; i < conList.size();i++) //buscamos un elemento libre en la lista
		{
			if(conList.get(i).getState() == c.EMPTY ){
				
				conList.get(i).setMobileClient(new UserClient(name, con, pass));
				
				return i;
			}
		}
		//si no hay un hueco, creamos un nuevo elemento en la lista
		conList.add(new ClientsConnected( new UserClient(name, con,pass)));
		System.out.println("Creado un cliente móvil en " + conList.size());
		return conList.size()-1;
	}
	
	/**
	 * Muestra los estados de las conexiones activas
	 */
	public void showConnections(){
		
		System.out.println("Los uusarios que hay conectadors son:");

		for(int i=0 ; i< conList.size();i++){
			System.out.println("i= "+i+" Status :"+conList.get(i).getState()+ 
					" Mobile => user: "+ conList.get(i).getMobileClient().getName()+
					" pass: "+ conList.get(i).getMobileClient().pass+
					" || Web => user : "+ conList.get(i).getWebClient().getName()+
					" pass: "+conList.get(i).getWebClient());
		}
	}
	
	/**
	 * Comprueba que el usuario y la pass no se están utilizando ya
	 * @param user
	 * @param pass
	 * @return
	 */
	public boolean testNamePassExist(String user, String pass) {
		for(int i = 0 ;i < conList.size();i++ ){//se recorre la lista
			if(conList.get(i).getMobileClient().getName().equals(user) && 
					conList.get(i).getMobileClient().getPass().equals(pass) &&
					(conList.get(i).getState() == c.WAITING ||
					conList.get(i).getState() == c.CONNECTED))
				return true;
		}
		return false;
	}
	
}
