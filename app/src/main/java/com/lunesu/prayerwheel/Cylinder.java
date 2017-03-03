/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.lunesu.prayerwheel;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

import javax.microedition.khronos.opengles.GL10;

/**
 * A vertex shaded cube.
 */
class Cylinder {
	public Cylinder(int segments) {
		final int ONE = 0x10000;

		int vertices[] = new int[(segments + 1) * 6];
		int uvs[] = new int[(segments + 1) * 4];

		final double da = 2 * Math.PI / segments;
		final int du = 65536 / segments;

		for (int t = 0; t < segments + 1; t++) {
			int s = (int) (Math.sin(t * da) * 65536.0);
			int c = (int) (Math.cos(t * da) * 65536.0);

			vertices[t * 6 + 0] = vertices[t * 6 + 3] = s;
			vertices[t * 6 + 1] = vertices[t * 6 + 4] = c;
			vertices[t * 6 + 2] = ONE;
			vertices[t * 6 + 5] = -ONE;

			uvs[t * 4 + 0] = uvs[t * 4 + 2] = ONE - t * du;
			uvs[t * 4 + 1] = 0;
			uvs[t * 4 + 3] = ONE;
		}

		// create strip
		byte indices[] = new byte[(segments + 1) * 2];
		for (byte t = 0; t < indices.length; ++t)
			indices[t] = t;

		// Buffers to be passed to gl*Pointer() functions
		// must be direct, i.e., they must be placed on the
		// native heap where the garbage collector cannot
		// move them.
		//
		// Buffers with multi-byte datatypes (e.g., short, int, float)
		// must have their byte order set to native order

		ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
		vbb.order(ByteOrder.nativeOrder());
		mVertexBuffer = vbb.asIntBuffer();
		mVertexBuffer.put(vertices);
		mVertexBuffer.position(0);

		ByteBuffer cbb = ByteBuffer.allocateDirect(uvs.length * 4);
		cbb.order(ByteOrder.nativeOrder());
		mTexCoordBuffer = cbb.asIntBuffer();
		mTexCoordBuffer.put(uvs);
		mTexCoordBuffer.position(0);

		mIndexBuffer = ByteBuffer.allocateDirect(indices.length);
		mIndexBuffer.put(indices);
		mIndexBuffer.position(0);
	}

	public void draw(GL10 gl) {
		gl.glVertexPointer(3, GL10.GL_FIXED, 0, mVertexBuffer);
		gl.glTexCoordPointer(2, GL10.GL_FIXED, 0, mTexCoordBuffer);
		gl.glDrawElements(GL10.GL_TRIANGLE_STRIP, mIndexBuffer.remaining(),
				GL10.GL_UNSIGNED_BYTE, mIndexBuffer);
	}

	private IntBuffer mVertexBuffer;
	private IntBuffer mTexCoordBuffer;
	private ByteBuffer mIndexBuffer;
}
