package com.example.easylife.scripts.mainvieweditlayout_things;

import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

public class DraggableCardView extends FrameLayout {
    private static final float MIN_DISTANCE_THRESHOLD = 10;
    private static final float DRAGGING_ALPHA = 0.5f;
    private float initialX, initialY;
    private List<DraggableCardView> draggableCardViews;
    private List<Point> predefinedPositions;
    private OnCardViewDropListener dropListener;
    private Point lastPosition;
    private int ID;
    public interface OnCardViewDropListener {
        void onCardViewDrop(DraggableCardView cardView, Point point, Point lastPosition);
    }
    public void setOnCardViewDragListener(OnCardViewDropListener listener) {
        this.dropListener = listener;
    }
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
        draggableCardViews = new ArrayList<>();
        predefinedPositions = new ArrayList<>();;
    }
    public void setID(int id){
        this.ID = id;
    }
    public int getID(){
        return ID;
    }
    public void setDraggableCardViews(List<DraggableCardView> draggableCardViews) {
        this.draggableCardViews = draggableCardViews;
    }
    public void setLastPosition(Point lastPosition){
        this.lastPosition = lastPosition;
    }
    public void setInitialPosition(float x, float y) {
        setX(x);
        setY(y);
    }
    public void setPredefinedPositions(int width, int height){
        int column1X = 0;
        int column2X = width;

        int RowY = 0;
        int RowY2 = 0;

        for(int i = 0; i < 10; i++){
            Point point = new Point();
            if(i < 5){
                point.x = column1X;
                point.y = RowY;

                RowY +=height;
            }else{
                point.x = column2X;
                point.y = RowY2;

                RowY2+=height;
            }

            predefinedPositions.add(point);
        }
    }
    public List<Point> getPredefinedPositions(){
        return predefinedPositions;
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                setAlpha(DRAGGING_ALPHA);
                //setEnabled(false);
                initialX = event.getRawX();
                initialY = event.getRawY();

                float currentX = getX();
                float currentY = getY();
                Point closestPosition = null;
                float closestDistance = Float.MAX_VALUE;

                // Compare to predefined positions
                for (Point position : predefinedPositions) {
                    float distance = calculateDistance(currentX, currentY, position.x, position.y);

                    // Check if the current position is closer to the target position
                    if (distance < closestDistance && !isPositionOccupied(position)) {
                        closestDistance = distance;
                        closestPosition = position;
                    }
                }
                lastPosition = closestPosition;
                break;

            case MotionEvent.ACTION_MOVE:
                float dx = event.getRawX() - initialX;
                float dy = event.getRawY() - initialY;

                float newX = getX() + dx;
                float newY = getY() + dy;

                if (newX >= 0 && newX + getWidth() <= ((View) getParent()).getWidth() &&
                        newY >= 0 && newY + getHeight() <= ((View) getParent()).getHeight()) {
                    if (!isOverlapping(newX, newY)) {
                        setX(newX);
                        setY(newY);
                    }
                }

                initialX = event.getRawX();
                initialY = event.getRawY();
                break;

            case MotionEvent.ACTION_UP:
                onDrop();
                setAlpha(1.0f);
                setEnabled(true);
                break;

            default:
                return super.onTouchEvent(event);
        }

        setDesiredSize(); // Set the desired size after each movement
        return true;
    }
    private boolean isOverlapping(float x, float y) {
        Rect currentBounds = new Rect((int) x, (int) y, (int) (x + getWidth()), (int) (y + getHeight()));
        for (DraggableCardView otherView : draggableCardViews) {
            if (otherView != this) {
                Rect otherBounds = new Rect((int) otherView.getX(), (int) otherView.getY(),
                        (int) (otherView.getX() + otherView.getWidth()), (int) (otherView.getY() + otherView.getHeight()));
                if (currentBounds.intersect(otherBounds)) {
                    return true; // Overlapping with another DraggableCardView
                }
            }
        }
        return false; // No overlap
    }
    private void setDesiredSize() {
        float desiredWidthPercentage = 0.1f;
        float desiredHeightPercentage = 0.1f;

        Object tag = getTag();
        if (tag.equals("1")) {
            desiredWidthPercentage = 0.495f;
            desiredHeightPercentage = 0.195f;
        } else if (tag.equals("2")) {
            desiredWidthPercentage = 0.98f;
            desiredHeightPercentage = 0.195f;
        } else if (tag.equals("3")) {
            desiredWidthPercentage = 0.98f;
            desiredHeightPercentage = 0.395f;
        }
        // Get the parent layout
        View parentLayout = (View) getParent();

        // Calculate desired width and height based on parent dimensions
        int desiredWidth = (int) (parentLayout.getWidth() * desiredWidthPercentage);
        int desiredHeight = (int) (parentLayout.getHeight() * desiredHeightPercentage);

        // Set the size of the DraggableCardView
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        layoutParams.width = desiredWidth;
        layoutParams.height = desiredHeight;
        setLayoutParams(layoutParams);
    }
    private void onDrop() {
        // Get the current position of the DraggableCardView
        float currentX = getX();
        float currentY = getY();

        // Initialize variables to store the closest position and its distance
        Point closestPosition = null;
        float closestDistance = Float.MAX_VALUE;

        // Compare to predefined positions
        for (Point position : predefinedPositions) {
            float distance = calculateDistance(currentX, currentY, position.x, position.y);

            // Check if the current position is closer to the target position
            if (distance < closestDistance && !isPositionOccupied(position)) {
                closestDistance = distance;
                closestPosition = position;
            }
        }

        // Handle the drop based on the closest position
        if (closestPosition != null) {
            handleDropAtClosestPosition(closestPosition);
        } else {
            // Handle the drop when there are no available predefined positions
            handleDropAtNonPredefinedPosition();
        }
    }
    public boolean isPositionOccupied(Point position) {
        for (DraggableCardView otherView : draggableCardViews) {
            if (otherView != this) {
                float distance = calculateDistance(position.x, position.y, otherView.getX(), otherView.getY());
                if (distance < MIN_DISTANCE_THRESHOLD) {
                    return true; // Position is occupied
                }
            }
        }
        return false; // Position is not occupied
    }
    private float calculateDistance(float x1, float y1, float x2, float y2) {
        // Calculate the Euclidean distance between two points
        return (float) Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
    }
    private void handleDropAtClosestPosition(Point closestPosition) {
        // Handle the drop when the current position is closest to a predefined position
        // You can add your custom logic here
        float targetX = closestPosition.x;
        float targetY = closestPosition.y;

        // Move the DraggableCardView to the closest position
        animate().x(targetX).y(targetY).setDuration(200).start();
        dropListener.onCardViewDrop(this, closestPosition, lastPosition);
    }
    private void handleDropAtNonPredefinedPosition() {
        // Handle the drop when the current position is not close to any predefined position
        // You can add your custom logic here
    }
}
