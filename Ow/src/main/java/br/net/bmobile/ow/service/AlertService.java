package br.net.bmobile.ow.service;

import android.app.NotificationManager;
import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.Builder;
import br.net.bmobile.ow.R;
import br.net.bmobile.ow.model.Notification;

public class AlertService {

    Context context;
	
	public AlertService(Context context) {

    	this.context = context;
	}	
	
	public void executeAlertForNotification(final Notification notification, boolean withSound) {
		
		Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
		v.vibrate(1000);
		 
		if(withSound) {
			MediaPlayer player = MediaPlayer.create(context, R.raw.ow);
			player.setOnCompletionListener(new OnCompletionListener() {
				
				@Override
				public void onCompletion(MediaPlayer mp) {
					MediaPlayer player = null;
					
					switch(notification.getType()) {
					case 1: 
						player = MediaPlayer.create(context, R.raw.vamo);
						break;
						
					case 2:
						player = MediaPlayer.create(context, R.raw.perai);
						break;
	
					case 3:
						player = MediaPlayer.create(context, R.raw.chegou);
						break;
						
					case 4:
						player = MediaPlayer.create(context, R.raw.maneiro);
						break;
						
					case 5:
						player = MediaPlayer.create(context, R.raw.tocomfome);
						break;
					}

					if(player != null)
						player.start();
					
					postLocalNotification(notification);
				}
			});
			player.start();
		}
		else {

			postLocalNotification(notification);
		}
	}
	
	private void postLocalNotification(Notification notification) {
		
		String message = String.format(
				context.getString(R.string.notification_message), 
				notification.getContact().getFirstName(), 
				notification.typeToString());
		
		NotificationCompat.Builder builder = new Builder(context)
			.setContentTitle("Ow")
			.setContentText(message);
		
		builder.build();
		
		NotificationManager notificationManager =
			    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

		notificationManager.notify(1, builder.build());
	}
}
