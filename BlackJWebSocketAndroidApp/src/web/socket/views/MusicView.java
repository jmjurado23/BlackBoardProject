package web.socket.views;


import java.util.ArrayList;
import java.util.List;
import web.socket.classes.ViewWrapperChatRow;
import web.sockets.redes.R;
import web.sockets.redes.WebSocketRedesActivity;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Clas eque muestra los elementos de audio que tiene el dispositivo
 * @author juanma
 *
 */
public class MusicView extends LinearLayout implements OnClickListener{
	
	
	static WebSocketRedesActivity activity; //actividad
	ListView list; //lista donde se colocarán las vistas de las canciones
	String[] music; //cadena con las músicas
	ArrayAdapter<Music> listAdapter; //adaptador de canciones a la lista
	static ArrayList<Music> musicList; //array con las músicas

	/**
	 * Constructor de la clase
	 * @param context
	 * @param act
	 */
	public MusicView(Context context, WebSocketRedesActivity act) {
		super(context);
		this.activity = act;
		initialize();
	}

	/**
	 * Método para inicializar los elementos de la clase
	 */
	private void initialize() {
		final LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.music, this);
		
		
		//se obtiene el elemento de la lista
		list = (ListView) findViewById(R.id.listViewMusic);
		
		//se rellenen las listas con la música
		music2List();
		
		//se crea un adaptador para listas de los msgs
		listAdapter = new MusicArrayAdapter(this.getContext(),
				musicList);

		list.setAdapter(listAdapter);
	}
	
	
	
	/**
	 * Clase con los datos de las canciones del sistema
	 * @author   juanma
	 */
	private static class Music {
		public String music; //cadena con el título
		public String album; //cadena con el álbum
		public String dir; //cadena con la dirección
		
		public String getDir() {
			return dir;
		}
		public void setDir(String dir) {
			this.dir = dir;
		}
		public Music(String music, String album, String dir){
			this.music= music;
			this.album = album;
			this.dir = dir;
		}
		public String getMusic() {
			return music;
		}
		public void setMusic(String music) {
			this.music = music;
		}
		public String getAlbum() {
			return album;
		}
		public void setAlbum(String album) {
			this.album = album;
		}
		
		
	}
	
	/**
	 * Método para rellenar el array de Music con las canciones
	 */
	public void music2List(){
		
		//se genera una lista con las preferencias
		musicList = new ArrayList<Music>();
		
		//se crea una proyección para la consulta en la BBDD
		String[] proyection={
				MediaStore.Audio.Media._ID,
				MediaStore.Audio.Media.DATA,
				MediaStore.Audio.Media.TITLE,
				MediaStore.Audio.Media.ARTIST,
				MediaStore.Audio.Media.DURATION
			};
		String sel = MediaStore.Audio.Media.IS_MUSIC + " != 0";
		
		//se crea el cursor con la consulta
	     Cursor cursor=null;
	     cursor = activity.managedQuery(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, proyection, sel,null,null);
	     cursor.moveToFirst();
	   
	     //se recorre el cursor para obtener los datos
	     if(cursor != null &&   cursor.getCount() != 0){
	    	 //System.out.println(cursor.getString(1));
			 for(int i=0;i<cursor.getCount();i++)
	         {
				 musicList.add(new Music(cursor.getString(2)+ " - "+ cursor.getString(4) +" bytes",
						 cursor.getString(3),cursor.getString(1)));
				 cursor.moveToNext();
	         }
		 }
	}
	
	/**
	 * Adaptador personalizado para los elementos de una lista de mensajes
	 * @author juanma
	 *
	 */
	private static class MusicArrayAdapter extends ArrayAdapter<Music> {

		private LayoutInflater inflater;

		public MusicArrayAdapter(Context context,
				List<Music> musicList) {
			super(context, R.layout.chatrow3, musicList);
			inflater = LayoutInflater.from(context);
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {


			final TextView textviewtitle;
			TextView textviewtitle2;

			if (convertView == null) {
				convertView = inflater.inflate(R.layout.chatrow3, null);

				ViewWrapperChatRow wrapper = new ViewWrapperChatRow(
						convertView);
				textviewtitle = wrapper.getTitle();
				textviewtitle2 = wrapper.getSubTitle();

				convertView.setTag(new ChatViewHolder(textviewtitle,
						textviewtitle2));

			} else {

				ChatViewHolder viewHolder = (ChatViewHolder) convertView
						.getTag();
				textviewtitle = viewHolder.getTextTitle();
				textviewtitle2 = viewHolder.getTextSubtitle();
			}

			final Music msg1 = (Music) this.getItem(position);
			textviewtitle.setText(msg1.getMusic());
			textviewtitle2.setText(msg1.getAlbum());

			convertView.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View arg0) {
					activity.sendMusic(msg1.getDir(),musicList.get(position).getMusic() + " - " + musicList.get(position).getAlbum());
					
				}
				
			});
			return convertView;
		}

	}
	
	/**
	 * Clase para manejar los nombres de las caciones 
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



	public ListView getViewList() {
			
		return list;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}

	
		
	
}
