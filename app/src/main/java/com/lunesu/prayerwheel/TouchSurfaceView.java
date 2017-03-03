package com.lunesu.prayerwheel;

import java.io.IOException;
import java.io.InputStream;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.SoundPool;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.opengl.Matrix;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.widget.Toast;

/**
 * Implement a simple rotation control.
 *
 */
class TouchSurfaceView extends GLSurfaceView {

    private long mPreviousTime;
    private float mPreviousX;

    private volatile int streamId;

	private final Toast warningToast;
	private final SoundPool sp;
    private final int omSoundId;
    //private final int bellSoundId;
    
	private static final int DefaultTextureResource = R.drawable.tibetan;
    
    private static final float WARNING_SPEED = 1f;
    private static final float MINIMUM_SPEED = 1f;
    private static final float VOLUME_MAX = 1f;
    private static final float VOLUME_MULTIPLIER = -0.001f;
    private static final float FRICTION = 0.4f;
    private static final float TOUCH_SCALE_FACTOR = 180.0f / 500;
    private static final float TRACKBALL_SCALE_FACTOR = 200.0f;
    
    private final CylinderRenderer mRenderer;
	
    public TouchSurfaceView(Context context) {
        super(context);

        //mp = MediaPlayer.create(context, R.raw.om22k);
        //mp.setOnPreparedListener(this);
        
        sp = new SoundPool(2, AudioManager.STREAM_MUSIC, 0);
        omSoundId = sp.load(TouchSurfaceView.this.getContext(), R.raw.om11k, 1);
        //bellSoundId = sp.load(TouchSurfaceView.this.getContext(), R.raw.bell22k, 1);

		warningToast = Toast.makeText(context, R.string.wrong_way, Toast.LENGTH_SHORT);

        mRenderer = new CylinderRenderer();
        setEGLConfigChooser(false);  
        setRenderer(mRenderer);
        // NO: start in CONTINUOUS mode so we can fade in nicely
        //setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

    @Override 
    public boolean onTrackballEvent(MotionEvent e) {
        setSpeed(e.getX() * TRACKBALL_SCALE_FACTOR);
        return true;
    }

    @Override 
    public boolean onTouchEvent(MotionEvent e) {
        float x = e.getX();
        //float y = e.getY();
        switch (e.getAction()) {
        case MotionEvent.ACTION_MOVE:
        	float dt = 0.001f * (e.getEventTime() - mPreviousTime);
            float dx = x - mPreviousX;
            setSpeed(dx * TOUCH_SCALE_FACTOR / dt);
        }
        mPreviousX = x;
        mPreviousTime = e.getEventTime() - 1;	// prevent div by 0
        return true;
    }

    @Override
    public void onPause() {
    	// workaround for pre-2.0
    	mRenderer.resetFade();
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
    	super.onPause();
    	//mp.stop();
    	mRenderer.stopSound();
    }

    @Override 
    public void onResume() 
    {
    	super.onResume();
    	// refresh
        if (!isPlaying())
        	requestRender();
    }
    
	private void setSpeed(float speed) {
		mRenderer.setSpeed(speed);
		
        if (speed > WARNING_SPEED)
        	warningToast.show();
        else
        	warningToast.cancel();
        
		if (Math.abs(speed) > MINIMUM_SPEED)
		{
	        //mp.setLooping(true);
	        //mp.start();
	        //if (playing)
	        //sp.setLoop(streamId, -1);
	        //sp.setVolume(streamId, 1, 1);
	        //int sid = sp.play(soundId, 1, 1, 1, -1, 1);
	        //if (sid != 0)
	        //	streamId = sid;
	        if (!isPlaying())
		        mRenderer.startRendering();
		}
    }
    
	private boolean isPlaying() {
		return getRenderMode() == GLSurfaceView.RENDERMODE_CONTINUOUSLY;
	}

	private void onPrayer() {
		//sp.play(bellSoundId, 1f, 1f, 0, 0, 1f);
		++mPrayerCount;
	}
	
	static void normalizeV(float[] v) {
		float oolen = 1f / Matrix.length(v[0], v[1], v[2]);
		v[0] *= oolen;
		v[1] *= oolen;
		v[2] *= oolen;
	}
	
    /**
     * Render a cylinder.
     */
    private class CylinderRenderer implements GLSurfaceView.Renderer {
    	
		private final Gradient mGradient;
        private final Cylinder mCylinder;

        private volatile int mNewTexture;
        private volatile float mNewSpeed;

        private int mTextureID;
		private long mTime = SystemClock.uptimeMillis();
		private float mSpeed;
        private float mAngle;
        private volatile float mFade;

        public void resetFade() {
        	synchronized(this) {
        		mFade = 0.0f;
        	}
        }
        
		public CylinderRenderer() {
            mCylinder = new Cylinder(16);
            mGradient = new Gradient();
 		}

		private float mVolume = 0f;

		public void setSpeed(float speed) {
    		synchronized(this) {
    			mNewSpeed = speed;
    		}
		}
		
		public void setTexture(int texture) {
    		synchronized(this) {
    			mNewTexture = texture;
    		}
		}
		
		private void stopSound() {
	    	if (streamId != 0) {
	    		sp.stop(streamId);
	    		streamId = 0;
	    	}
		}
		
		private void setVolume(float dt) {
        	float targetvolume = mSpeed * VOLUME_MULTIPLIER;
        	if (targetvolume <= 0.0f)
        		targetvolume = 0.0f;
        	else if (targetvolume > VOLUME_MAX)
        		targetvolume = VOLUME_MAX;
        	
        	float dv = targetvolume - mVolume;
        	float newVolume = mVolume + dv * dt;
        	
        	if (newVolume <= 0.0f || mSilent) {
        		stopSound();
        		//newVolume = 0.0f;
        	}
        	else {
        		if (streamId == 0)
        			streamId = sp.play(omSoundId, newVolume, newVolume, 1, -1, 1);
        		else if (Math.abs(dv) > 0.02f)
                	sp.setVolume(streamId, newVolume, newVolume);
        		else
        			return;	// do not update volume too often
        	}
        	mVolume = newVolume;
		}
		
	    public void startRendering()
	    {
	        mTime = SystemClock.uptimeMillis();
        	setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
	    }
	    
		private void stopRendering() {
		    setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
		    //mp.setLooping(false);
		    stopSound();
		}

		private float getFrameTime() {
            long time = SystemClock.uptimeMillis();
            float dtime = 0.001f * (time - mTime);
            mTime = time;
            if (dtime > 1.0f) 
            	dtime = 1.0f;
            return dtime;
		}
		
		@Override
	    public void onDrawFrame(GL10 gl) {
			
			float dtime = getFrameTime();
			
            synchronized(this) {
		    	if (mNewSpeed != 0f) {
	    			mSpeed = mNewSpeed;
	    			mNewSpeed = 0f;
		    		//setVolume();
	    		}
		    	if (mNewTexture != 0) {
	    			loadTexture(mNewTexture);
	    			mNewTexture = 0;
	    		}
	    	
	            mAngle += mSpeed * dtime;
	            
	            if (mFade < 1.0f)
	            {
	            	// Fade in
	            	mFade += dtime;
	            	if (mFade > 1.0f)
	            		mFade = 1.0f;
	            }
	            else
	            {
		            // Stop continous rendering if the speed is too slow (<1 deg/sec)
		            if (Math.abs(mSpeed) < 1f) {
		            	stopRendering();
		            }
	            }
            }

            // Apply friction
            mSpeed *= (float)Math.pow(FRICTION, dtime);

            //if (mSpeed > -100.0f)// && prepared && mp.isPlaying())
            	setVolume(dtime);

            // Keep track of the number of prayers
            if (mAngle <= -360f) {
            	mAngle += 360f;
            	onPrayer();
            } else if (mAngle > 360f) {
            	mAngle -= 360f;
            }
            
            /*
             * Usually, the first thing one might want to do is to clear
             * the screen. The most efficient way of doing this is to use
             * glClear().
             */

            gl.glClear(GL10.GL_COLOR_BUFFER_BIT);// | GL10.GL_DEPTH_BUFFER_BIT);

            /*
             * Now we're ready to draw some 3D objects
             */

            float World[] = new float[16];
            Matrix.setRotateM(World, 0, mAngle - 200f, 0f, 1f, 0f);
            Matrix.rotateM(World, 0, -90f, 1f, 0f, 0f);
            World[14] = -3f;//translate

            float WorldT[] = new float[16];
            Matrix.transposeM(WorldT, 0, World, 0);
            //Matrix.invertM(WorldT, 0, World, 0);
            
            gl.glMatrixMode(GL10.GL_MODELVIEW);
            gl.glLoadMatrixf(World, 0);
            
            /*gl.glLoadIdentity();
            gl.glTranslatex(0, 0, -0x30000);
            gl.glRotatef(mAngle, 0f, 1f, 0f);
            gl.glRotatex(-0x5A0000, 0x10000, 0, 0);*/

            gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
            gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);

            gl.glActiveTexture(GL10.GL_TEXTURE0);
            gl.glBindTexture(GL10.GL_TEXTURE_2D, mTextureID);          

            gl.glColor4f(mR * mFade, mG * mFade, mB * mFade, 1f);
/*
            float LigthDirWorld[] = new float[] { 0f, 1f, -2f, 0f };
            normalizeV(LigthDirWorld);
            
            float LightDirObj[] = new float[4];
            Matrix.multiplyMV(LightDirObj, 0, WorldT, 0, LigthDirWorld, 0);
            
            final float H = 0.5f;
            gl.glColor4f(LightDirObj[0]*H+H, LightDirObj[1]*H+H, LightDirObj[2]*H+H, 0f);            
            
            // Configure the texture combiner to do the DOT3 bumpmapping calculation
            gl.glTexEnvf( GL10.GL_TEXTURE_ENV, GL10.GL_TEXTURE_ENV_MODE, GL11.GL_COMBINE );
            gl.glTexEnvf( GL10.GL_TEXTURE_ENV, GL11.GL_COMBINE_RGB,  GL11.GL_DOT3_RGB );
            //gl.glTexEnvf( GL10.GL_TEXTURE_ENV, GL11.GL_COMBINE_ALPHA,  GL11.GL_REPLACE );
            
            // The first colour for each fragment operation is gotten from the bumpmap texture
            gl.glTexEnvf( GL10.GL_TEXTURE_ENV, GL11.GL_SRC0_RGB,  GL10.GL_TEXTURE );
            gl.glTexEnvf( GL10.GL_TEXTURE_ENV, GL11.GL_OPERAND0_RGB, GL10.GL_SRC_COLOR );
            
            //gl.glTexEnvf( GL10.GL_TEXTURE_ENV, GL11.GL_SRC0_ALPHA,  GL11.GL_PRIMARY_COLOR);
            //gl.glTexEnvf( GL10.GL_TEXTURE_ENV, GL11.GL_OPERAND0_ALPHA, GL10.GL_SRC_ALPHA );
            
            // The second colour for each fragment operation is gotten from the primary colour
            gl.glTexEnvf( GL10.GL_TEXTURE_ENV, GL11.GL_SRC1_RGB,  GL11.GL_PRIMARY_COLOR );
            gl.glTexEnvf( GL10.GL_TEXTURE_ENV, GL11.GL_OPERAND1_RGB, GL10.GL_SRC_COLOR );            
            //gl.glTexEnvf( GL10.GL_TEXTURE_ENV, GL11.GL_OPERAND1_ALPHA, GL10.GL_SRC_ALPHA);            
            //gl.glTexEnvf( GL10.GL_TEXTURE_ENV, GL11.GL_SRC1_ALPHA,  GL11.GL_PRIMARY_COLOR );

            //gl.glDisable(GL10.GL_BLEND);
            //gl.glBlendFunc(GL10.GL_ALPHA, GL10.GL_SRC_COLOR);
*/            
            mCylinder.draw(gl);

            gl.glLoadIdentity();
            gl.glTranslatex(0, 0, -0x1f000);
            
            //gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
            gl.glDisable(GL10.GL_TEXTURE_2D);
            gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
            gl.glEnable(GL10.GL_BLEND);

            mGradient.draw(gl);
            
            gl.glDisableClientState(GL10.GL_COLOR_ARRAY);
            gl.glEnable(GL10.GL_TEXTURE_2D);
            gl.glDisable(GL10.GL_BLEND);
        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {
            gl.glViewport(0, 0, width, height);

            /*
             * Set our projection matrix. This doesn't have to be done
             * each time we draw, but usually a new projection needs to
             * be set when the viewport is resized.
             */

            //float ratio = (float) width / height;
            gl.glMatrixMode(GL10.GL_PROJECTION);
            gl.glLoadIdentity();
            //gl.glFrustumf(-ratio, ratio, -1, 1, 1, 10);
            gl.glOrthof(-0.98f, 0.98f, -1f, 1f, 1f, 3.1f);
        }

        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        	mFade = 0.0f;
       	
            /*
             * By default, OpenGL enables features that improve quality
             * but reduce performance. One might want to tweak that
             * especially on software renderer.
             */
            gl.glDisable(GL10.GL_DITHER);

            /*
             * Some one-time OpenGL initialization can be made here
             * probably based on features of this particular context
             */
            gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT,
            	GL10.GL_FASTEST);

            gl.glBlendFunc(GL10.GL_DST_COLOR, GL10.GL_ONE_MINUS_SRC_ALPHA);   //define blending factors
            gl.glFrontFace(GL10.GL_CW);

            gl.glEnable(GL10.GL_CULL_FACE);
            gl.glShadeModel(GL10.GL_SMOOTH);
            gl.glDisable(GL10.GL_DEPTH_TEST);

            gl.glClearColor(mR, mG, mB, 1f);
            gl.glEnable(GL10.GL_TEXTURE_2D);
             
            //gl.glFogf(GL10.GL_FOG_MODE, GL10.GL_LINEAR);
            //gl.glFogf(GL10.GL_FOG_START, 2.6f);
            //gl.glFogf(GL10.GL_FOG_END, 2.9f);
            //gl.glFogf(GL10.GL_FOG_DENSITY, 0.2f);
            //gl.glEnable(GL10.GL_FOG);
             
            /*
             * Create our texture. This has to be done each time the
             * surface is created.
             */

            int[] textures = new int[1];
            gl.glGenTextures(1, textures, 0);

            mTextureID = textures[0];
            gl.glBindTexture(GL10.GL_TEXTURE_2D, mTextureID);

            gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER,
                    GL10.GL_LINEAR);
            gl.glTexParameterf(GL10.GL_TEXTURE_2D,
                    GL10.GL_TEXTURE_MAG_FILTER,
                    GL10.GL_LINEAR);

            gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S,
                    GL10.GL_CLAMP_TO_EDGE);
            gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T,
                    GL10.GL_CLAMP_TO_EDGE);

            gl.glTexEnvf(GL10.GL_TEXTURE_ENV, GL10.GL_TEXTURE_ENV_MODE,
                    GL10.GL_MODULATE);
             
            loadTexture(DefaultTextureResource);
        }
        
        private void loadTexture(int resid) {
			InputStream is = TouchSurfaceView.this.getResources().openRawResource(resid);
        	try {
			    Bitmap bitmap = BitmapFactory.decodeStream(is);
				GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
				bitmap.recycle();
			} 
        	finally {
			    try {
			        is.close();
			    } catch(IOException e) {
			        // Ignore.
			    }
			}
        }
    }
    
	private volatile int mPrayerCount;
    private volatile boolean mSilent;
    private int mColor = -1;
    
    private static final float oo255 = 1f/255f;
    private volatile float mR = 1f;
    private volatile float mG = 1f;
    private volatile float mB = 1f;

    public void setPrayers(int p) {
    	mPrayerCount = p;
    }
    
    public int getPrayers() {
    	return mPrayerCount;
    }
    
	public void setSilent(boolean s) {
		mSilent = s;
	}

	public boolean getSilent() {
		return mSilent; 
	}
	
	public int getColor() {
		return mColor;
	}

	public void setTexture(String nt) {
    	int resid = TouchSurfaceView.this.getResources().getIdentifier(nt, "drawable", "com.lunesu.prayerwheel");
    	if (resid < 0)
    		resid = DefaultTextureResource;

    	mRenderer.setTexture(resid);
	}
	
	public void setColor(int c) {
		mColor = c;
		mR = Color.red(c) * oo255;
		mG = Color.green(c) * oo255;
		mB = Color.blue(c) * oo255;
    	// refresh
        if (!isPlaying())
        	requestRender();
	}
}


