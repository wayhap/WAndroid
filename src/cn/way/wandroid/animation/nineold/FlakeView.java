/*
 * Copyright (C) 2011 The Android Open Source Project
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
package cn.way.wandroid.animation.nineold;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.view.View;

import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.animation.ValueAnimator.AnimatorUpdateListener;

/**
 * This class is the custom view where all of the Droidflakes are drawn. This class has
 * all of the logic for adding, subtracting, and rendering Droidflakes.
 */
public class FlakeView extends View {

    ArrayList<Flake> flakes = new ArrayList<Flake>(); // List of current flakes

    // Animator used to drive all separate flake animations. Rather than have potentially
    // hundreds of separate animators, we just use one and then update all flakes for each
    // frame of that single animation.
    ValueAnimator animator = ValueAnimator.ofFloat(0, 1);
    long startTime, prevTime; // Used to track elapsed time for animations and fps
    int frames = 0;     // Used to track frames per second
    Paint textPaint;    // Used for rendering fps text
    float fps = 0;      // frames per second
    Matrix m = new Matrix(); // Matrix used to translate/rotate each flake during rendering
    String fpsString = "";
    private boolean showFps = false;
    boolean paused;
    /**
     * Constructor. Create objects used throughout the life of the View: the Paint and
     * the animator
     */
    public FlakeView(Context context) {
        super(context);
//        image = BitmapFactory.decodeResource(getResources(), resId);
        
        // This listener is where the action is for the flak animations. Every frame of the
        // animation, we calculate the elapsed time and update every flake's position and rotation
        // according to its speed.
        animator.addUpdateListener(new AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator arg0) {
            	if (paused) {           		
					return;
				}else{
					
				}
                long nowTime = System.currentTimeMillis();
                float secs = (float)(nowTime - prevTime) / 300f;
                prevTime = nowTime;
                for (int i = 0; i < flakes.size(); ++i) {
                    Flake flake = flakes.get(i);
                    if (flake.delay>0) {
                    	flake.delay -= 1;
					}else{
						flake.y = flake.y;
						flake.y += (flake.speed * secs);
						if (flake.y > getHeight()) {
							// If a flake falls off the bottom, send it back to the top
							flake.y = 0 - flake.height;
						}
						flake.rotation = flake.rotation + (flake.rotationSpeed * secs);
					}
                }
                // Force a redraw to see the flakes in their new positions and orientations
                invalidate();
            }
        });
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setDuration(3000);
    }

    int getNumFlakes() {
        return flakes.size();
    }



    /**
     * Add the specified number of droidflakes.
     */
    public void addFlakes(int quantity,int resId) {
        for (int i = 0; i < quantity; ++i) {
            flakes.add(Flake.createFlake(getWidth(), BitmapFactory.decodeResource(getResources(), resId)));
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // For each flake: back-translate by half its size (this allows it to rotate around its center),
        // rotate by its current rotation, translate by its location, then draw its bitmap
        for (int i = 0; i < flakes.size(); ++i) {
            Flake flake = flakes.get(i);
            m.setTranslate(-flake.width/2, -flake.height/2);
            m.postRotate(flake.rotation);
            m.postTranslate(flake.width/2 + flake.x, flake.height/2 + flake.y);
            canvas.drawBitmap(flake.bitmap, m, null);
        }
        // fps counter: count how many frames we draw and once a second calculate the
        // frames per second
        ++frames;
        if (showFps) {
        	long nowTime = System.currentTimeMillis();
        	long deltaTime = nowTime - startTime;
        	if (deltaTime > 1000) {
        		float secs = (float) deltaTime / 1000f;
        		fps = (float) frames / secs;
        		fpsString = "fps: " + fps;
        		startTime = nowTime;
        		frames = 0;
        	}
        	canvas.drawText("numFlakes: " +flakes.size(), getWidth() - 200, getHeight() - 50, textPaint);
        	canvas.drawText(fpsString, getWidth() - 200, getHeight() - 80, textPaint);
		}
    }

    public void cancel() {
    	Flake.clear();
    	flakes.clear();
        animator.cancel();
        // Make sure the animator's not spinning in the background when the activity is paused.
    }

    public void start() {
    	paused = false;
    	startTime = System.currentTimeMillis();
        prevTime = startTime;
        frames = 0;
        animator.start();
    }

    public void pause(){
    	paused = true;
    	for (Flake flake : flakes) {
			flake.reset();
		}
    	animator.end();
    }
    public void resume(){
    	paused = false;
    	animator.start();
    }
    
	public boolean isShowFps() {
		return showFps;
	}

	public void setShowFps(boolean showFps) {
		this.showFps = showFps;
		if (showFps) {
			if (textPaint==null) {
				textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
				textPaint.setColor(Color.WHITE);
				textPaint.setTextSize(24);
			}
		}
	}

}
