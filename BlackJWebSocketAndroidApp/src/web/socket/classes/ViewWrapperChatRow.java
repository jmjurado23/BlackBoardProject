package web.socket.classes;


import web.sockets.redes.R;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Clase que recoge los elemntos de las vistas de las preferencias para crear
 * las listas
 * @version 1.0
 *
 */
public class ViewWrapperChatRow {
	View base;
	TextView label = null;
	TextView label2 = null;
	ImageView icon = null;

	public ViewWrapperChatRow(View base) {
		this.base = base;
	}

	public TextView getTitle() {
		if (label == null) {
			label = (TextView) base.findViewById(R.id.chatrowtitle);
		}
		return (label);
	}

	public TextView getSubTitle() {
		if (label2 == null) {
			label2 = (TextView) base.findViewById(R.id.chatrowsubtitle);
		}
		return (label2);
	}


}
