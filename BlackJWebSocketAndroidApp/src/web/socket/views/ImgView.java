package web.socket.views;


import web.sockets.redes.R;
import web.sockets.redes.WebSocketRedesActivity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.BaseColumns;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * Clase usada para alojar una vista encargada de mostrar una galería de imágenes
 * Contiene botones para enviar las imágenes
 * @author juanma
 *
 */
public class ImgView extends LinearLayout implements OnClickListener{
	
	/**
	 * @uml.property  name="activity"
	 * @uml.associationEnd  readOnly="true" inverse="imgView:web.sockets.redes.WebSocketRedesActivity"
	 */
	private WebSocketRedesActivity activity; //actividad
	/**
	 * @uml.property  name="mImageIds" multiplicity="(0 -1)" dimension="1"
	 */
	private int[] mImageIds ; //enteros con los ids de las imágenes
    private String[] mImageData; //vectores con la dirección de las imágenes
    private String[] mName; //array con los nombres de las imágenes
	private ImageView imgBig; //vista de la imagen ampliada
	private Button sendButton; //botón de enviar
	private Gallery gallery; //galería para mostrar todas las imágenes
	
	/**
	 * Constructor de la clase ImgView
	 * @param context
	 * @param act
	 */
	public ImgView(Context context, WebSocketRedesActivity act) {
		super(context);
		
		this.activity = act;
		initialize();
	}

	/**
	 * Método que inicializa la vista con los elementos
	 */
	private void initialize() {
		// TODO Auto-generated method stub
		final LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.img, this);
		
		//se obtienen los elementos de las vistas
		imgBig = (ImageView) findViewById(R.id.imageViewBig);
		sendButton = (Button) findViewById(R.id.sendButton);
		
		//se inicializan los cursores
        makeCursorThumb();  //cursor de las miniaturas
        makeCursorData(); //cursor para las imágenes
       
        //se crea la galería
		gallery = (Gallery) findViewById(R.id.gallery);
	    gallery.setAdapter(new ImageAdapter(activity));

	    //se define el método al clickar en la galería
	    gallery.setOnItemClickListener(new OnItemClickListener() {
	        public void onItemClick(AdapterView parent, View v, int position, long id) {
	        	Bitmap myBitmap = BitmapFactory.decodeFile(mImageData[position]);
	        	imgBig.setImageBitmap(myBitmap);
	        	
	        }
	    });
	    
	    sendButton.setOnClickListener(this);
	}

	
	/**
	 * Método para crear el cursor de las imágenes 
	 */
	private void makeCursorData() {
		
		// elementos que se quieren recuperar de la base de datos
        String[] projection = {MediaStore.Images.Media.DATA, MediaStore.Images.Media.DISPLAY_NAME}; 
		// se crea el cursor
        Cursor cursor = activity.managedQuery(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                      projection, null, null, null);
        
        if(cursor == null) // si no se puede crear, se avisa al usuario
        {
            //se crea un cuadro de diálogo para informar al usuario
            AlertDialog alert = new AlertDialog.Builder(activity).create();

            alert.setMessage("No hay imagenes");
            alert.show();
            alert.setCanceledOnTouchOutside(true);
        }
        else // si ha imágenes
        {
            //Se obtiene el número de miniaturas de la imagen
            int size = cursor.getCount();
            if(size == 0) // si el tamaño es 0
            {
                Log.i("blackboard", "No hay imágenes");
            }
            else
            {
               //se ibtiene  el identificador de la columna
                String imageData = "";
                String name = "";
                mImageData = new String[size]; //localización de la imagen
                mName = new String[size]; //nombre de la imagen
                
                

                for (int i = 0; i < size; i++)
                {
                   	// se mueve el cursor a la posición
                    cursor.moveToPosition(i);

                    //se obtienen el id y el nombre y se almacenan en los arrays destinados a ello
                    imageData = cursor.getString(0);
                    name = cursor.getString(1);
                    mImageData[i] = imageData;
                    mName[i] = name;
                }
                //se cierra el cursor
                cursor.close();
            }
        }
		
	}

	/**
	 * método para rellenar le cursor con las miniaturas de las imágenes
	 */
	private void makeCursorThumb() {

		// elementos que se quieren recuperar de la base de datos
        String[] projection = {BaseColumns._ID};
		//se crea el cursor con la consulta a la BBDD
        Cursor cursor = activity.managedQuery(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                      projection, null, null, null);
        
        if(cursor == null) //si no hay imágenes
        {
            //se crea un cuadro de diálogo
            AlertDialog alert = new AlertDialog.Builder(activity).create();

            alert.setMessage("No hay imagenes");
            alert.show();
            alert.setCanceledOnTouchOutside(true);
        }
        else
        {
            //se obtienen el número de imágenes de la aplicación
            int size = cursor.getCount();
            if(size == 0)
            {
                Log.i("BlackBoardWeb", "No hay imágenes");
            }
            else
            {
                
                int imageID = 0;
                mImageIds = new int[size];

             
                for (int i = 0; i < size; i++)
                {
                    //se mueve el cursor a la posición dada
                    cursor.moveToPosition(i);

                    /* Obtain the _ID of the current image */
                    imageID = cursor.getInt(0);
                    mImageIds[i] = imageID;

                }
                //se cierra el cursor
                cursor.close();
            }
        }
		
	}
	
	/**
	 * Se crea el adaptador de imágenes para la aplicación
	 * @author juanma
	 *
	 */
	public class ImageAdapter extends BaseAdapter {
	    int mGalleryItemBackground;
	    private Context mContext;

 	    
	    public ImageAdapter(Context c) {
	        mContext = c;
	        TypedArray attr = mContext.obtainStyledAttributes(R.styleable.Gallery);
	        mGalleryItemBackground = attr.getResourceId(
	                R.styleable.Gallery_android_galleryItemBackground, 0);
	        attr.recycle();
	    }

	    public int getCount() {
	    	if(mImageIds == null)
	    		return 0;
	        return mImageIds.length;
	    }

	    public Object getItem(int position) {
	        return position;
	    }

	    public long getItemId(int position) {
	        return position;
	    } 

	    //se define el tipo de galería para las imágenes
	    public View getView(int position, View convertView, ViewGroup parent) {
	        ImageView imageView = new ImageView(mContext);
	        imageView.setImageBitmap(MediaStore.Images.Thumbnails.getThumbnail(
                    activity.getContentResolver(), mImageIds[position], MediaStore.Images.Thumbnails.MINI_KIND, null));
	        
	        imageView.setLayoutParams(new Gallery.LayoutParams(150, 100));
	        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
	        imageView.setBackgroundResource(mGalleryItemBackground);

	        
	        return imageView; //devolvemos la vista de la miniatura
	    }
	}
	
	@Override
	public void onClick(View v) {
		if(v.equals(sendButton)){
			activity.sendImg(mImageData[gallery.getSelectedItemPosition()],mName[gallery.getSelectedItemPosition()]);
		}
		
	}
	
}