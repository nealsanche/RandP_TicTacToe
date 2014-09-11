package com.extensionlogic.randp_tictactoe.Shapes;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.PathShape;
import android.view.View;

/**
 * Created by J on 2014-09-05.
 */
public class WinIndicatorLine extends View {
    private ShapeDrawable mDrawable;
    int height;
    int width;
    View line_view;

    public WinIndicatorLine(Context context, View parentView, int winScenarioNum) {
        super(context);
        this.line_view = parentView;
        makeLine(winScenarioNum);
    }

    private void makeLine(int winScenarioNum) {
        int x = 0;
        int y = 0;
        this.height = line_view.getHeight();
        this.width = line_view.getWidth();
        Path p = getPath(winScenarioNum);//path will dictate the correct line placement based on the position of the win array.
        mDrawable = new ShapeDrawable(new PathShape(p, width, height));
        mDrawable.getPaint().setStrokeWidth(32.5f);
        mDrawable.setIntrinsicHeight(height);
        mDrawable.setIntrinsicWidth(width);
        mDrawable.getPaint().setColor(Color.RED);
        mDrawable.getPaint().setStyle(Paint.Style.STROKE);
        mDrawable.setBounds(x, y, x + width, y + height);
    }

    protected void onDraw(Canvas canvas) {
        mDrawable.draw(canvas);
    }

    public Path getPath(int winScenarioNum) {
        Path path = new Path();
        int startingPointVertical = -1;
        int startingPointHorizontal = -1;
        int endPointHorizontal = -1;
        int endPointVertical = -1;
        switch (winScenarioNum) {//direction of line is dictated by the integer passed to the object
            case 0:
                line_view.getHeight();
                startingPointVertical = height / 6;
                startingPointHorizontal = 0;
                endPointHorizontal = width;
                endPointVertical = startingPointVertical;

                break;
            case 1:
                line_view.getHeight();
                startingPointVertical = height / 2;
                startingPointHorizontal = 0;
                endPointHorizontal = width;
                endPointVertical = startingPointVertical;

                break;
            case 2:
                line_view.getHeight();
                startingPointVertical = height - height / 6;
                startingPointHorizontal = 0;
                endPointHorizontal = width;
                endPointVertical = startingPointVertical;

                break;
            case 3:
                line_view.getHeight();
                startingPointVertical = 0;
                startingPointHorizontal = width / 6;
                endPointHorizontal = width / 6;
                endPointVertical = height;

                break;
            case 4:
                line_view.getHeight();
                startingPointVertical = 0;
                startingPointHorizontal = width / 2;
                endPointHorizontal = width / 2;
                endPointVertical = height;

                break;
            case 5:
                line_view.getHeight();
                startingPointVertical = 0;
                startingPointHorizontal = width - width / 6;
                endPointHorizontal = width - width / 6;
                endPointVertical = height;

                break;

            case 6:
                line_view.getHeight();
                startingPointVertical = height;
                startingPointHorizontal = width;
                endPointHorizontal = 0;
                endPointVertical = 0;

                break;
            case 7:
                line_view.getHeight();
                startingPointVertical = 0;
                startingPointHorizontal = width;
                endPointHorizontal = 0;
                endPointVertical = height;

                break;
        }


        path.moveTo(startingPointHorizontal, startingPointVertical);
        path.moveTo(startingPointHorizontal, startingPointVertical);
        path.lineTo(endPointHorizontal, endPointVertical);

        //path.lineTo(10,20);
        path.close();
        return path;
    }
}