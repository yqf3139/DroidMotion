package com.example.droidmotion;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.Rect;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.R.integer;
import android.R.string;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.Menu;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity{

	static  int point = 2;
	
	private DroidServer mServer = null;
	private SensorManager mSensorManager = null;
	
	private Sensor mSensorAcceleration = null;
	private SensorEventListener mAccelerationListener = new AccelerationListener();
	private TextView mAccelerationTextView = null;
	float[] accelerometerValues = new float[3];  
	
	private Sensor mSensorGravity = null;
	private SensorEventListener mGravityListener = new GravityListener();
	private TextView mGravityTextView = null;
	
	private Sensor mSensorGyroscope = null;
	private SensorEventListener mGyroscopeListener = new GyroscopeListener();
	private TextView mGyroscopeTextView = null;
	
	private Sensor mSensorLinearAcceleration = null;
	private SensorEventListener mLinearAccelerationListener = new LinearAccelerationListener();
	private TextView mLinearAccelerationTextView = null;

	private Sensor mSensorRotationVector = null;
	private SensorEventListener mRotationVectorListener = new RotationVectorListener();
	private TextView mRotationVectorTextView = null;
	
	private Sensor mSensorProximity = null;
	private SensorEventListener mProximityListener = new ProximityListener();
	private TextView mProximityTextView = null;
	
	private Sensor mSensorMagneticField = null;
	private SensorEventListener mMagneticFieldListener = new MagneticFieldListener();
	private TextView mMagneticFieldTextView = null;
	float[] magneticFieldValues = new float[3];
	
	private TextView mOrientationTextView = null;
    float[] orientationValues = new float[3];
    float[] orientationDegrees = new float[3];
    float[] orientationR = new float[9];

	private SurfaceView sfvSurfaceView;
	private Paint mXPaint;
	private Paint mYPaint;
	private Paint mZPaint;

	private SurfaceHolder surfaceHolder;

//	private Canvas canvas;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.i("DroidMotion", "on activity create");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		
		mSensorAcceleration = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		mSensorGravity = mSensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
		mSensorGyroscope = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
		mSensorLinearAcceleration = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
		mSensorRotationVector = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
		mSensorProximity = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
		mSensorMagneticField = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
		
		mAccelerationTextView = (TextView) findViewById(R.id.Acceleration);
		mGravityTextView = (TextView) findViewById(R.id.Gravity);
		mGyroscopeTextView = (TextView) findViewById(R.id.Gyroscope);
		mLinearAccelerationTextView = (TextView) findViewById(R.id.LinearAcceleration);
		mRotationVectorTextView = (TextView) findViewById(R.id.RotationVector);
		mProximityTextView = (TextView) findViewById(R.id.Proximity);
		mMagneticFieldTextView = (TextView) findViewById(R.id.MagneticField);
		
		mOrientationTextView = (TextView) findViewById(R.id.Orientation);
		
//		sfvSurfaceView = (SurfaceView) findViewById(R.id.SurfaceView01);
		Button firebButton = (Button) findViewById(R.id.fireBtn);
		
		mXPaint = new Paint();
		mXPaint.setColor(Color.WHITE);
		mXPaint.setStrokeWidth(1);
		mXPaint.setAntiAlias(true);
		
		mYPaint = new Paint();
		mYPaint.setColor(Color.GREEN);
		mYPaint.setStrokeWidth(1);
		mYPaint.setAntiAlias(true);
		
		mZPaint = new Paint();
		mZPaint.setColor(Color.RED);
		mZPaint.setStrokeWidth(1);
		mZPaint.setAntiAlias(true);
		
		firebButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				WebSocket ws = mServer.getChromeClientSocket();
				if(ws == null){
					Log.i("DroidMotion", "no client");
					return;
				}
				
				ws.send("{\"type\":\"event\",\"name\":\"click\",\"data\":{}}");
				Log.i("DroidMotion", "sent message" + "{\"type\":\"text\",\"msg\":\"a new motion fired\"}");
			}
		});
		
		mServer = DroidServer.getDroidServer();
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	protected void onStart() {
		Log.i("DroidMotion", "on activity start");
		mServer.start();
		super.onStart();
	}
	
	@Override
	protected void onResume() {
		Log.i("DroidMotion", "on activity resume");
		
//		if(mSensorAcceleration != null)
//			mSensorManager.registerListener(mAccelerationListener, mSensorAcceleration, SensorManager.SENSOR_DELAY_UI);
//		else
//			mAccelerationTextView.setText("acceleration : no sensor");
		
//		if(mSensorGravity != null)
//			mSensorManager.registerListener(mGravityListener, mSensorGravity, SensorManager.SENSOR_DELAY_NORMAL);
//		else
//			mAccelerationTextView.setText("gravity : no sensor");
		
//		if(mSensorGyroscope != null)
//			mSensorManager.registerListener(mGyroscopeListener, mSensorGyroscope, SensorManager.SENSOR_DELAY_NORMAL);
//		else
//			mGyroscopeTextView.setText("gyroscope : no sensor");
		
		if(mSensorLinearAcceleration != null)
			mSensorManager.registerListener(mLinearAccelerationListener, mSensorLinearAcceleration, SensorManager.SENSOR_DELAY_FASTEST);
		else
			mLinearAccelerationTextView.setText("linear acceleration : no sensor");
		
//		if(mSensorRotationVector != null)
//			mSensorManager.registerListener(mRotationVectorListener, mSensorRotationVector, SensorManager.SENSOR_DELAY_NORMAL);
//		else
//			mRotationVectorTextView.setText("rotation vector : no sensor");
		
//		if(mSensorProximity != null)
//			mSensorManager.registerListener(mProximityListener, mSensorProximity, SensorManager.SENSOR_DELAY_NORMAL);
//		else
//			mProximityTextView.setText("proximity : no sensor");
		
//		if(mSensorMagneticField != null)
//			mSensorManager.registerListener(mMagneticFieldListener, mSensorMagneticField, SensorManager.SENSOR_DELAY_NORMAL);
//		else
//			mMagneticFieldTextView.setText("magnetic field : no sensor");
		
//		surfaceHolder = sfvSurfaceView.getHolder();
//		surfaceHolder.addCallback((Callback) this);
		
		super.onResume();
	}
	
	@Override
	protected void onPause() {
		Log.i("DroidMotion", "on activity pause");
		try {
			
			if(mSensorAcceleration != null)
				mSensorManager.unregisterListener(mAccelerationListener);
			if(mSensorGravity != null)
				mSensorManager.unregisterListener(mGravityListener);
			if(mSensorGyroscope != null)
				mSensorManager.unregisterListener(mGyroscopeListener);
			if(mSensorLinearAcceleration != null)
				mSensorManager.unregisterListener(mLinearAccelerationListener);
			if(mSensorRotationVector != null)
				mSensorManager.unregisterListener(mRotationVectorListener);
			if(mSensorProximity != null)
				mSensorManager.unregisterListener(mProximityListener);
			if(mSensorMagneticField != null)
				mSensorManager.unregisterListener(mMagneticFieldListener);
			
			mServer.stop();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		super.onPause();
	}
	
	@Override
	protected void onStop() {
		Log.i("DroidMotion", "on activity stop");
		super.onStop();
	}
	
	
	class GravityListener implements SensorEventListener {
		
		public long lastTime = 0;
		public float xtmp = 0;
		public float ytmp = 0;
		public float ztmp = 0;
		public float x = 0;
		public float y = 0;
		public float z = 0;
		
		@Override
		public void onAccuracyChanged(Sensor arg0, int arg1) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onSensorChanged(SensorEvent event) {
			if( (System.currentTimeMillis() - lastTime) < 100){
				return;
			}
				
			
			xtmp = round(event.values[0], point);
			ytmp = round(event.values[1], point);
			ztmp = round(event.values[2], point);
			
			if( xtmp == x && ytmp == y && ztmp == z){//if(eq(xtmp, x) && eq(ytmp, y) && eq(ztmp, z)){
				mGravityTextView.setText("gravity : still");
				return;
			}
			
			x = xtmp;
			y = ytmp;
			z = ztmp;
			
			accelerometerValues = event.values.clone();
			
			mGravityTextView.setText("gravity (m/s^2) : x = " + (x) + " y = " + (y) + " z = " + (z));
			
			//sendMsg("{\"type\":\"event\",\"name\":\"gravity\",\"data\":{\"x\":"+x+",\"y\":"+y+",\"z\":"+z+"}}");
			
			
			lastTime = System.currentTimeMillis();
		}
		
	}
	
	class AccelerationListener implements SensorEventListener {
		
		long lastTime = 0;
		float xtmp = 0;
		float ytmp = 0;
		float ztmp = 0;
		float x = 0;
		float y = 0;
		float z = 0;
		
		@Override
		public void onAccuracyChanged(Sensor arg0, int arg1) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onSensorChanged(SensorEvent event) {
			if( (System.currentTimeMillis() - lastTime) < 100){
				return;
			}
				
			
			xtmp = round(event.values[0], point);
			ytmp = round(event.values[1], point);
			ztmp = round(event.values[2], point);
			
			if( xtmp == x && ytmp == y && ztmp == z){//if(eq(xtmp, x) && eq(ytmp, y) && eq(ztmp, z)){
				mAccelerationTextView.setText("acceleration : still");
				return;
			}
			
			x = xtmp;
			y = ytmp;
			z = ztmp;
			
			mAccelerationTextView.setText("acceleration (m/s^2) : x = " + (x) + " y = " + (y) + " z = " + (z));
			
			//sendMsg("{\"type\":\"event\",\"name\":\"acceleration\",\"data\":{\"x\":"+x+",\"y\":"+y+",\"z\":"+z+"}}");
			
			lastTime = System.currentTimeMillis();
			
			sendOrientation();
		}
		
	}

	class GyroscopeListener implements SensorEventListener {
		
		long lastTime = 0;
		float xtmp = 0;
		float ytmp = 0;
		float ztmp = 0;
		float x = 0;
		float y = 0;
		float z = 0;
		
		@Override
		public void onAccuracyChanged(Sensor arg0, int arg1) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onSensorChanged(SensorEvent event) {
			/*if( (System.currentTimeMillis() - lastTime) < 50){
				return;
			}
				
			
			xtmp = round(event.values[0]);
			ytmp = round(event.values[1]);
			ztmp = round(event.values[2]);
			
			if( xtmp == x && ytmp == y && ztmp == z){//if(eq(xtmp, x) && eq(ytmp, y) && eq(ztmp, z)){
				mAccelerationTextView.setText("acceleration still");
				return;
			}*/
			
			x = event.values[0];//xtmp;
			y = event.values[1];//ytmp;
			z = event.values[2];//ztmp;
			
			mGyroscopeTextView.setText("gyroscope (rad/s) : x = " + (x) + " y = " + (y) + " z = " + (z));
			Log.i("gyroscope", "fire");
			//sendMsg("{\"type\":\"event\",\"name\":\"acceleration\",\"data\":{\"x\":"+x+",\"y\":"+y+",\"z\":"+z+"}}");
			
			//lastTime = System.currentTimeMillis();
			
		}
		
	}

	class LinearAccelerationListener implements SensorEventListener {
		
		long lastTime = 0;
		float xtmp = 0;
		float ytmp = 0;
		float ztmp = 0;
		float x = 0;
		float y = 0;
		float z = 0;
		
		@Override
		public void onAccuracyChanged(Sensor arg0, int arg1) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onSensorChanged(SensorEvent event) {
			if( (System.currentTimeMillis() - lastTime) < 15){
				return;
			}

			xtmp = round(event.values[0], point);
			ytmp = round(event.values[1], point);
			ztmp = round(event.values[2], point);
			
			
			if( xtmp == x && ytmp == y && ztmp == z){//if(eq(xtmp, x) && eq(ytmp, y) && eq(ztmp, z)){
				mLinearAccelerationTextView.setText("linear acceleration : still");
				return;
			}
			
			x = xtmp;
			y = ytmp;
			z = ztmp;
			
			mLinearAccelerationTextView.setText("linear acceleration (m/s^2) : x = " + (x) + " y = " + (y) + " z = " + (z));
			
			sendMsg("{\"type\":\"event\",\"name\":\"acceleration\",\"data\":{\"x\":"+x+",\"y\":"+y+",\"z\":"+z+"}}");
			
			lastTime = System.currentTimeMillis();
			
//			SimpleDraw(event.values[0],event.values[1],event.values[2]);
		}
		
	}
	
	class RotationVectorListener implements SensorEventListener {
		
		long lastTime = 0;
		float xtmp = 0;
		float ytmp = 0;
		float ztmp = 0;
		float x = 0;
		float y = 0;
		float z = 0;
		
		@Override
		public void onAccuracyChanged(Sensor arg0, int arg1) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onSensorChanged(SensorEvent event) {
			if( (System.currentTimeMillis() - lastTime) < 100){
				return;
			}
			
			xtmp = round(event.values[0], 2);
			ytmp = round(event.values[1], 2);
			ztmp = round(event.values[2], 2);
			
			if( xtmp == x && ytmp == y && ztmp == z){//if(eq(xtmp, x) && eq(ytmp, y) && eq(ztmp, z)){
				mRotationVectorTextView.setText("rotation vector : still");
				return;
			}
			
			x = xtmp;
			y = ytmp;
			z = ztmp;
			
			mRotationVectorTextView.setText("rotation vector : x = " + (x) + " y = " + (y) + " z = " + (z));
			
			//sendMsg("{\"type\":\"event\",\"name\":\"rotation_vector\",\"data\":{\"x\":"+x+",\"y\":"+y+",\"z\":"+z+"}}");
			
			lastTime = System.currentTimeMillis();
		}
		
	}
	
	class ProximityListener implements SensorEventListener {
		
		long lastTime = 0;
		float dtmp = 0;
		float d = 0;
		
		@Override
		public void onAccuracyChanged(Sensor arg0, int arg1) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onSensorChanged(SensorEvent event) {
			if( (System.currentTimeMillis() - lastTime) < 1000){
				return;
			}
			
			dtmp = round(event.values[0], 1);
			
			if( dtmp == d ){
				mProximityTextView.setText("proximity : still");
				return;
			}
			
			d = dtmp;
			
			mProximityTextView.setText("proximity (cm) : d = " + (d) );
			
			//sendMsg("{\"type\":\"event\",\"name\":\"acceleration\",\"data\":{\"x\":"+x+",\"y\":"+y+",\"z\":"+z+"}}");
			
			lastTime = System.currentTimeMillis();
		}
		
	}
	
	class MagneticFieldListener implements SensorEventListener {
		
		long lastTime = 0;
		float xtmp = 0;
		float ytmp = 0;
		float ztmp = 0;
		float x = 0;
		float y = 0;
		float z = 0;
		
		@Override
		public void onAccuracyChanged(Sensor arg0, int arg1) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onSensorChanged(SensorEvent event) {
			if( (System.currentTimeMillis() - lastTime) < 100){
				return;
			}
			
			xtmp = round(event.values[0], point);
			ytmp = round(event.values[1], point);
			ztmp = round(event.values[2], point);
			
			if( eq(xtmp,x) && eq(ytmp,y) && eq(ztmp,z) ){//if(eq(xtmp, x) && eq(ytmp, y) && eq(ztmp, z)){
				mMagneticFieldTextView.setText("magnetic field : still");
				return;
			}
			
			x = xtmp;
			y = ytmp;
			z = ztmp;
			
			magneticFieldValues = event.values.clone();
			
			mMagneticFieldTextView.setText("magnetic field (μT) : x = " + (x) + " y = " + (y) + " z = " + (z));
			
			//sendMsg("{\"type\":\"event\",\"name\":\"acceleration\",\"data\":{\"x\":"+x+",\"y\":"+y+",\"z\":"+z+"}}");
			
			//lastTime = System.currentTimeMillis();
			
			//calculateOrientation();
		}
	}

	private  void calculateOrientation() {

	      SensorManager.getRotationMatrix(orientationR, null, accelerometerValues, magneticFieldValues);	      
	      
	      SensorManager.getOrientation(orientationR, orientationValues);
	      
	      for(int i = 0; i < orientationValues.length; i++ )
	    	  orientationDegrees[i] = round((float) Math.toDegrees( orientationValues[i] ), 2);

	      //sendMsg("{\"type\":\"event\",\"name\":\"orientation\",\"data\":{\"a\":"+orientationDegrees[0]+",\"p\":"+orientationDegrees[1]+",\"r\":"+orientationDegrees[2]+"}}");
	      
	      mOrientationTextView.setText("orientation (Degrees) : a = " + orientationDegrees[0] + " p = " + orientationDegrees[1] + " r = " + orientationDegrees[2]);
	}
	
	
	private void sendMsg(String msg) {
		WebSocket ws = mServer.getChromeClientSocket();
		if(ws == null){
			//Log.i("DroidMotion", "no client");
			return;
		}
		
		ws.send(msg);
	}
	
	private void sendOrientation() {
		float x, y, z;
		
		x = ((AccelerationListener)mAccelerationListener).x - ((LinearAccelerationListener)mLinearAccelerationListener).x;
		y = ((AccelerationListener)mAccelerationListener).y - ((LinearAccelerationListener)mLinearAccelerationListener).y;
		z = ((AccelerationListener)mAccelerationListener).z - ((LinearAccelerationListener)mLinearAccelerationListener).z;
		
		
		sendMsg("{\"type\":\"event\",\"name\":\"orientation\",\"data\":{\"x\":"+x/10+",\"y\":"+y/10+",\"z\":"+z/10+"}}");
	}
	
	public static float round(float a, int n) {
		double i = Math.pow(10, n);
		return (float)((Math.round(a*i))/i);
	}
	
	public static boolean eq(float a, float b) {
		return false;//(a - b) < 0.2 || (b - a) > -0.2 ;
	}
	
	List<Float> zFloats = new LinkedList<Float>();
//	private synchronized void SimpleDraw(float x,float y,float z){
//		synchronized (zFloats) {
////			canvas = surfaceHolder.lockCanvas(new Rect(0, 0, sfvSurfaceView.getWidth(),sfvSurfaceView.getHeight()));
////			if(canvas == null)return;
//			//canvas.drawColor(Color.BLACK);
//			//canvas.drawl
//			
//			float middle = 300;
//			zFloats.add(z);
////			canvas.drawColor(Color.BLACK);
//			if(zFloats.size() == 500) {
//				zFloats.remove(0);
//			}
//			
//			//canvas.drawCircle(500-counter, 100*z+middle, 1, mZPaint);
//			
////			for (int i = 0; i < zFloats.size(); i++) {
////	
////				canvas.drawCircle(500-i, 50*zFloats.get(i)+middle, 1, mZPaint);
////			}
////
////			surfaceHolder.unlockCanvasAndPost(canvas);
//		}
//
//	}
//
//	@Override
//	public void surfaceChanged(SurfaceHolder holder, int format, int width,
//			int height) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void surfaceCreated(SurfaceHolder holder) {
//		surfaceHolder = holder;
//		canvas = surfaceHolder.lockCanvas(new Rect(0, 0, sfvSurfaceView.getWidth(),sfvSurfaceView.getHeight()));
//		canvas.drawColor(Color.BLACK);
//		surfaceHolder.unlockCanvasAndPost(canvas);
//	}
//
//	@Override
//	public void surfaceDestroyed(SurfaceHolder holder) {
//		// TODO Auto-generated method stub
//		
//	}
	
}
