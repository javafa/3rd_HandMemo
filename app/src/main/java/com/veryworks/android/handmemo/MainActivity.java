package com.veryworks.android.handmemo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    FrameLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        layout = (FrameLayout) findViewById(R.id.layout);

        // 1. 보드를 새로 생성한다.
        Board board = new Board(getBaseContext());
        // 2. 붓을 만들어서 보드에 담는다
        Paint paint = new Paint();
        paint.setColor(Color.MAGENTA);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(10);
        board.setPaint(paint);

        // 3. 생성된 보드를 화면에 세팅한다.
        layout.addView(board);
    }

    class Board extends View {
        Paint paint;
        Path path;

        public Board(Context context) {
            super(context);
            path = new Path();
        }

        public void setPaint(Paint paint){
            this.paint = paint;
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            canvas.drawPath(path, paint);
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            // 내가 터치한 좌표를 꺼낸다
            float x = event.getX();
            float y = event.getY();

            switch (event.getAction()){
                case MotionEvent.ACTION_DOWN :
                    Log.e("LOG","onTouchEvent=================down");
                    path.moveTo(x,y); // 이전점과 현재점 사이를 그리지 않고 이동한다.
                    break;
                case MotionEvent.ACTION_MOVE :
                    Log.e("LOG","onTouchEvent==================move");
                    path.lineTo(x,y); // 바로 이전점과 현재점사이에 줄을 그어준다.
                    break;
                case MotionEvent.ACTION_POINTER_UP:
                    Toast.makeText(getContext(), "언제찍히니?", Toast.LENGTH_SHORT).show();
                    break;
                case MotionEvent.ACTION_UP :
                    path.lineTo(x,y);
                    break;
            }

            // Path 를 그린후 화면을 갱신해서 반영해 준다.
            invalidate();

            // 리턴 false 일 경우 touch 이벤트를 연속해서 발생시키지 않는다.
            // 즉, 드래그시 onTouchEvent 가 호출되지 않는다
            return true;
        }
    }
}
