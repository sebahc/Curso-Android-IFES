package com.example.myservice;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class HelloIntentService extends IntentService {

	  /**
	   * A constructor is required, and must call the super IntentService(String)
	   * constructor with a name for the worker thread.
	   */
	  public HelloIntentService() {
	      super("HelloIntentService");
	  }

	  @Override
	  public int onStartCommand(Intent intent, int flags, int startId) {
	      Toast.makeText(this, "iniciando servicio", Toast.LENGTH_SHORT).show();
	      return super.onStartCommand(intent,flags,startId);
	  }
	  
	  /**
	   * The IntentService calls this method from the default worker thread with
	   * the intent that started the service. When this method returns, IntentService
	   * stops the service, as appropriate.
	   */
	  @Override
	  protected void onHandleIntent(Intent intent) {
	      // Normally we would do some work here, like download a file.
	      // For our sample, we just sleep for 20 seconds.
		  String mensaje = intent.getStringExtra("mensaje");
	      long endTime = System.currentTimeMillis() + 20*1000;
	      while (System.currentTimeMillis() < endTime) {
	          synchronized (this) {
	              try {
	                  wait(endTime - System.currentTimeMillis());
	                  
	              } catch (Exception e) {
	              }
	          }
	          
	      }
	      Log.e("Servicio", mensaje);
	      
	      
	  }
	}