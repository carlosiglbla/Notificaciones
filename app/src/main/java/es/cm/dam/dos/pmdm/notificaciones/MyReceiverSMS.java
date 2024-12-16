package es.cm.dam.dos.pmdm.notificaciones;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

public class MyReceiverSMS extends BroadcastReceiver {
    /** Podemos probar esta sección enviándonos un mensaje en la configuración
     * del móvil. En Phone, se puede enviar un mensaje. Escribimos y pulsamos
     * enviar.
     * @param context Contexto
     * @param intent Intent
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        SmsMessage[] msgs = null;
        String origen = null;
        String msg = null;

        if (bundle != null) {
            //obtenemos el mensaje original SMS:
            Object[] pdus = (Object[]) bundle.get("pdus");
            msgs = new SmsMessage[pdus.length];
            for (int i = 0; i < msgs.length; i++) {
                String format = bundle.getString("format");
                msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i], format);
                origen = msgs[i].getOriginatingAddress();
                msg = msgs[i].getMessageBody().toString();
            }

            Toast.makeText(context, "SMS Recibido de " + origen + ":" + msg, Toast.LENGTH_LONG).show();
            Log.d("NOTIFICACIONES.MYRECEIVERSMS","Escribiendo en el log por si me pierto el Toast.");
            //continua el proceso normal de broadcast
            // es decir, llega el sms y se almacena en la bandeja de entrada
            this.clearAbortBroadcast();

        }
    }
}