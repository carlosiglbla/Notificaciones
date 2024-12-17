package es.cm.dam.dos.pmdm.notificaciones;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import android.widget.Toast;

import androidx.core.app.NotificationManagerCompat;
import androidx.core.app.TaskStackBuilder;


public class MainActivity extends AppCompatActivity {

    private static final String ID_CANAL = "mi_canal_id";
    public static final int NOTIF_ID = 13;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        crearCanalDeNoficiacion();
        permisos();
        findViewById(R.id.btnNotificacion).setOnClickListener(view -> mostrarNotificaciones());
    }

    private void permisos() {
        if(checkSelfPermission("android.permission.RECEIVE_SMS")!= PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{"android.permission.RECEIVE_SMS"},1);
        }
    }

    private void crearCanalDeNoficiacion() {
        String nombreCanal = "CanalOne";
        String description = "Canal sobre las notificaciones en Android";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel notificationChannel = new NotificationChannel(ID_CANAL, nombreCanal, importance);
        notificationChannel.setDescription(description);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(notificationChannel);
    }

    private void mostrarNotificaciones() {

        // Intent para abrir una actividad al tocar la notificación
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                this,
                0,
                intent,
                PendingIntent.FLAG_IMMUTABLE
        );

        // Acción rápida: Botón que realiza algo sin abrir la app
        Intent accionIntent = new Intent(this, ActividadDeDestino.class);
        PendingIntent accionPendingIntent = PendingIntent.getActivity(
                this,
                0,
                accionIntent,
                PendingIntent.FLAG_IMMUTABLE
        );

        //El objeto stackBuilder crea un back stack que nos asegura que el botón de
        // "Atrás" del dispositivo nos lleva desde la Actividad a la pantalla principal
        TaskStackBuilder pila = TaskStackBuilder.create(this);
        // Añade el Intent que comienza la Actividad al inicio de la pila
        pila.addNextIntentWithParentStack(intent);

        // Crear la notificación
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, ID_CANAL)
                .setSmallIcon(R.drawable.baseline_notifications_active_24) // Ícono pequeño
                .setContentTitle("Notificación Completa")
                .setContentText("Esta notificación incluye múltiples funcionalidades.")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent) // Acción al tocar la notificación
                .setAutoCancel(true) // Se elimina al tocarla
                .addAction(R.drawable.sharp_accessibility_24, "Acción Rápida", accionPendingIntent); // Botón de acción

        // Añadir una imagen grande (BigPictureStyle)
//        builder.setStyle(new NotificationCompat.BigPictureStyle()
//                .bigPicture(getDrawable(R.drawable.elementos)) // Cambia a tu imagen
//                .bigLargeIcon(null)); // Ícono grande se oculta al expandir

        // Añadir texto largo (BigTextStyle)
        builder.setStyle(new NotificationCompat.BigTextStyle()
                .bigText("Este es un texto más largo que aparecerá cuando la notificación esté expandida."));

        // Añadir progreso (ejemplo de tarea en progreso)
        builder.setProgress(100, 50, false); // Progreso al 50% (no indeterminado)

        //Avanzadas
        /*NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
        String[] eventos = new String[5];
        // Título del expanded layout
        inboxStyle.setBigContentTitle("Notificación expandible:");
        eventos[0] = "Esto es la primera línea";
        eventos[1] = "Esto es la segunda línea";
        eventos[2] = "Esto es la tercera línea";
        eventos[3] = "Esto es la cuarta línea";
        eventos[4] = "Esto es la quinta línea";

        // Mueve eventos dentro del expanded layout
        for (int i = 0; i < eventos.length; i++) {
            inboxStyle.addLine(eventos[i]);
        }
        builder.setWhen(0)
            .setPriority(NotificationManager.IMPORTANCE_HIGH)
            .setStyle(inboxStyle);  // Mueve el expanded layout a la notificación.
        */

        // Mostrar la notificación
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // Si el permiso no está concedido, solicitarlo
            requestPermissions(new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, 101);
            return;
        }
        notificationManager.notify(NOTIF_ID, builder.build());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 101) { // Código que usaste al solicitar el permiso
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permiso concedido, puedes mostrar la notificación
                mostrarNotificaciones();
            } else {
                // Permiso denegado, muestra un mensaje al usuario
                Toast.makeText(this, "El permiso para mostrar notificaciones fue denegado.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}