package com.veryworks.android.handmemo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    FrameLayout layout;
    RadioGroup color;

    Board board;
    Paint current_brush; // 현재 세팅된 브러쉬

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        layout = (FrameLayout) findViewById(R.id.layout);
        color = (RadioGroup) findViewById(R.id.color);

        color.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId){
                    case R.id.rdBlue:
                        setBrush(Color.BLUE);
                        break;
                    case R.id.rdGreen:
                        setBrush(Color.GREEN);
                        break;
                    case R.id.rdRed:
                        setBrush(Color.RED);
                        break;
                }
            }
        });

        // 1. 보드를 새로 생성한다.
        board = new Board(getBaseContext());

        // 2. 생성된 보드를 화면에 세팅한다.
        layout.addView(board);

        // 3. 기본 브러쉬 세팅
        setBrush(Color.BLUE);
    }

    private void setBrush(int ColorType){
        // 2. 붓을 만들어서 보드에 담는다
        Paint paint = new Paint();
        paint.setColor(ColorType);

        // 선을 채우지 않고 굵기를 지정
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setDither(true);

        // seekbar 에서 처리
        paint.setStrokeWidth(10);
        board.setPaint(paint);
    }

    class Brush {
        Paint paint;
        Path path;
    }

    class Board extends View {
        Paint paint;
        List<Brush> brushes = new ArrayList<>();

        Path current_path;


        public Board(Context context) {
            super(context);
            //path = new Path();
        }

        public void setPaint(Paint paint){
            this.paint = paint;
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            for(Brush brush : brushes) {
                canvas.drawPath(brush.path, brush.paint);
            }
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            // 내가 터치한 좌표를 꺼낸다
            float x = event.getX();
            float y = event.getY();
            switch (event.getAction()){
                // 터치가 시작되면 Path 를 생성하고
                // List 에 담아둔다.
                case MotionEvent.ACTION_DOWN :
                    // 새로운 붓을 생성 - path 와 paint 를 포함한다.
                    Brush brush = new Brush();
                    // 가. 패스 생성
                    current_path = new Path();
                    // 나. 생성한 패스와 현재 페인트를 브러쉬에 담는다.
                    brush.path = current_path;
                    brush.paint = paint;
                    // 다. 완성된 브러쉬를 저장소에 담는다.
                    brushes.add(brush);

                    Log.e("LOG","onTouchEvent=================down");
                    current_path.moveTo(x,y); // 이전점과 현재점 사이를 그리지 않고 이동한다.
                    break;
                case MotionEvent.ACTION_MOVE :
                    Log.e("LOG","onTouchEvent==================move");
                    current_path.lineTo(x,y); // 바로 이전점과 현재점사이에 줄을 그어준다.
                    break;
                case MotionEvent.ACTION_POINTER_UP:
                    Toast.makeText(getContext(), "언제찍히니?", Toast.LENGTH_SHORT).show();
                    break;
                case MotionEvent.ACTION_UP :
                    current_path.lineTo(x,y);
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
