package com.example.easylife.scripts;

import android.content.Context;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

public class DraggableFrameLayout extends FrameLayout {

    private static final long LONG_PRESS_DURATION = 500;
    private static final int MOVE_THRESHOLD = 10;

    private GestureDetector gestureDetector;
    private Handler longPressHandler;
    private boolean isLongPressing = false;
    private boolean isMoving = false;
    private float startX, startY;

    public DraggableFrameLayout(Context context) {
        super(context);
        init(context);
    }

    public DraggableFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public DraggableFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        gestureDetector = new GestureDetector(context, new GestureListener());
        longPressHandler = new Handler(Looper.getMainLooper());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = event.getRawX();
                startY = event.getRawY();
                isMoving = false;
                longPressHandler.postDelayed(longPressRunnable, LONG_PRESS_DURATION);
                break;

            case MotionEvent.ACTION_MOVE:
                if (isLongPressing && !isMoving) {
                    float dx = Math.abs(event.getRawX() - startX);
                    float dy = Math.abs(event.getRawY() - startY);
                    if (dx > MOVE_THRESHOLD || dy > MOVE_THRESHOLD) {
                        isMoving = true;
                        longPressHandler.removeCallbacks(longPressRunnable);
                    }
                }

                if (isMoving) {
                    moveView(event.getRawX() - startX, event.getRawY() - startY);
                    startX = event.getRawX();
                    startY = event.getRawY();
                }
                break;

            case MotionEvent.ACTION_UP:
                longPressHandler.removeCallbacks(longPressRunnable);
                isLongPressing = false;
                isMoving = false;
                break;

            default:
                return super.onTouchEvent(event);
        }

        return true;
    }

    private void moveView(float dx, float dy) {
        // Mova a view
        float newX = getX() + dx;
        float newY = getY() + dy;

        // Certifique-se de que a view não ultrapasse os limites do layout
        Rect parentBounds = new Rect(0, 0, getWidth() + ((View) getParent()).getWidth(), getHeight() + ((View) getParent()).getHeight());
        if (parentBounds.contains((int) newX, (int) newY)) {
            setX(newX);
            setY(newY);
        }
    }

    private Runnable longPressRunnable = new Runnable() {
        @Override
        public void run() {
            isLongPressing = true;
            performLongClick();
        }
    };

    private class GestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public void onLongPress(MotionEvent e) {
            isLongPressing = true;
            performLongClick();
        }
    }
}
