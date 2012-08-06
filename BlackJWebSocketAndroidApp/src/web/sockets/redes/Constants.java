package web.sockets.redes;

/**
 * Clase con constantes definidas para la aplicación
 * @author juanma
 *
 */
public class Constants {
	/**
	 * @uml.property  name="lOGIN"
	 */
	public final int LOGIN = 3;
	/**
	 * @uml.property  name="oK"
	 */
	public final int OK = 4;
	/**
	 * @uml.property  name="kO"
	 */
	public final int KO = 4;
	public final  String url = "ws://192.168.19.12:8787/";
	public String user = "guess";
	public String pass = "guess";
	/**
	 * @uml.property  name="wAITING"
	 */
	public int WAITING = 2;
	/**
	 * @uml.property  name="cONNECTED"
	 */
	public int CONNECTED = 3;
	/**
	 * @uml.property  name="dISCONECTED"
	 */
	public int DISCONECTED = 1;
	public String NS_BASE = "com.blackboard.tests.BlackPlugIn";
}
