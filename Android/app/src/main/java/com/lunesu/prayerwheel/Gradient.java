package com.lunesu.prayerwheel;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

import javax.microedition.khronos.opengles.GL10;

public class Gradient {
	private IntBuffer mVertexBuffer;
	private IntBuffer mColorBuffer;
	private ByteBuffer mIndexBuffer;

	public Gradient() {
		
    	int ONE = 0x10000;
    	int FRAC = 0x0B000;
    	int Z = 0;//0x18000;
    	
        int vertices[] = {
        		-ONE, -ONE, Z,
        		-ONE, ONE, Z,
        		-FRAC, -ONE, Z,
        		-FRAC, ONE, Z,
        		FRAC, -ONE, Z,
        		FRAC, ONE, Z,
        		ONE, -ONE, Z,
        		ONE, ONE, Z,
        };

        int colors[] = {
        		0, 0, 0, ONE,
        		0, 0, 0, ONE,
        		0, 0, 0, 0,
        		0, 0, 0, 0,
        		0, 0, 0, 0,
        		0, 0, 0, 0,
        		0, 0, 0, ONE,
        		0, 0, 0, ONE,
        };
        		
        byte indices[] = {
        		0, 1, 2,  1, 3, 2, 
        		4, 5, 6,  5, 7, 6,
        };

        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length*4);
        vbb.order(ByteOrder.nativeOrder());
        mVertexBuffer = vbb.asIntBuffer();
        mVertexBuffer.put(vertices);
        mVertexBuffer.position(0);

        ByteBuffer cbb = ByteBuffer.allocateDirect(colors.length*4);
        cbb.order(ByteOrder.nativeOrder());
        mColorBuffer = cbb.asIntBuffer();
        mColorBuffer.put(colors);
        mColorBuffer.position(0);

        mIndexBuffer = ByteBuffer.allocateDirect(indices.length);
        mIndexBuffer.put(indices);
        mIndexBuffer.position(0);
	}
	
    public void draw(GL10 gl)
    {
        //gl.glFrontFace(GL10.GL_CW);
        gl.glVertexPointer(3, GL10.GL_FIXED, 0, mVertexBuffer);
        gl.glColorPointer(4, GL10.GL_FIXED, 0, mColorBuffer);
        gl.glDrawElements(GL10.GL_TRIANGLES, mIndexBuffer.remaining(), GL10.GL_UNSIGNED_BYTE, mIndexBuffer);
    }

}
