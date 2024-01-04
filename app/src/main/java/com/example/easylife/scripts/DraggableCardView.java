package com.example.easylife.scripts;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.cardview.widget.CardView;

public class DraggableCardView extends CardView {

    private float lastTouchX, lastTouchY;
    private boolean snapToGridEnabled = true;

    public DraggableCardView(Context context) {
        super(context);
        init();
    }

    public DraggableCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DraggableCardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setOnTouchListener((v, event) -> {
            float x = event.getRawX();
            float y = event.getRawY();

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    lastTouchX = x;
                    lastTouchY = y;
                    break;

                case MotionEvent.ACTION_MOVE:
                    float deltaX = x - lastTouchX;
                    float deltaY = y - lastTouchY;

                    setX(getX() + deltaX);
                    setY(getY() + deltaY);

                    lastTouchX = x;
                    lastTouchY = y;
                    break;

                case MotionEvent.ACTION_UP:
                    if (snapToGridEnabled) {
                        snapToGrid();
                    }
                    break;

                default:
                    return false;
            }

            return true;
        });
    }

    private void snapToGrid() {
        if (getParent() instanceof FrameLayout) {
            FrameLayout parentLayout = (FrameLayout) getParent();

            // Find the nearest grid cell
            Rect cardBounds = new Rect((int) getX(), (int) getY(), (int) (getX() + getWidth()), (int) (getY() + getHeight()));
            Rect gridBounds = new Rect();

            for (int i = 0; i < parentLayout.getChildCount(); i++) {
                View gridCell = parentLayout.getChildAt(i);
                gridBounds.set(gridCell.getLeft(), gridCell.getTop(), gridCell.getRight(), gridCell.getBottom());

                if (Rect.intersects(cardBounds, gridBounds)) {
                    // Snap the card to the center of the grid cell
                    float centerX = gridCell.getX() + gridCell.getWidth() / 2f - getWidth() / 2f;
                    float centerY = gridCell.getY() + gridCell.getHeight() / 2f - getHeight() / 2f;

                    animate().x(centerX).y(centerY).setDuration(200).start();
                    break;
                }
            }
        }
    }

    public void setSnapToGridEnabled(boolean snapToGridEnabled) {
        this.snapToGridEnabled = snapToGridEnabled;
    }
}