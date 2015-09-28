package timecontroler.com.virtual_joystick;

import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;

import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    // These TextViews are for debugging purposes.
    private TextView textView1, textView2, textView3, textView4, textView5;

    private RelativeLayout joystickLayout;
    private FrameLayout frameLayout;

    private float xRef, yRef;
    private JoyStick joystick;

    private int stickSize = 150, layoutSize = 500;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView1 = (TextView) findViewById(R.id.textView1);
        textView2 = (TextView) findViewById(R.id.textView2);
        textView3 = (TextView) findViewById(R.id.textView3);
        textView4 = (TextView) findViewById(R.id.textView4);
        textView5 = (TextView) findViewById(R.id.textView5);

        frameLayout = (FrameLayout) findViewById(R.id.frame_layout);
        joystickLayout = (RelativeLayout) findViewById(R.id.layout_joystick);

        joystick = new JoyStick(getApplicationContext(), joystickLayout, R.drawable.joystick_button);
        joystick.setStickSize(stickSize, stickSize);
        joystick.setLayoutSize(layoutSize, layoutSize);
        joystick.setLayoutAlpha(150);
        joystick.setStickAlpha(100);
        joystick.setOffset(90);
        joystick.setMinimumDistance(50);

        frameLayout.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) {

                    float xPos = event.getX();
                    float yPos = event.getY();

                    xRef = xPos;
                    yRef = yPos;

                    // Remove joystick from root view
                    frameLayout.removeView(joystickLayout);

                    // Create layout parameters for joystickLayout
                    FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT
                    );
                    params.height = layoutSize;
                    params.width = layoutSize;
                    params.gravity = Gravity.TOP;
                    params.topMargin = (int) yPos - (params.height / 2);
                    params.leftMargin = (int) xPos - (params.width / 2);

                    // Add joystick back with created layout parameters
                    frameLayout.addView(joystickLayout, params);

                }

                // Obtain MotionEvent object
                MotionEvent motionEvent = MotionEvent.obtain(
                        SystemClock.uptimeMillis(),
                        SystemClock.uptimeMillis() + 100,
                        event.getAction(),
                        (layoutSize / 2) + (event.getX() - xRef),
                        (layoutSize / 2) + (event.getY() - yRef),
                        0
                );
                // Dispatch this event to joystickLayout as well
                // The reason is to make sure that joystickLayout get event
                joystickLayout.dispatchTouchEvent(motionEvent);

                return true;
            }
        });

        joystickLayout.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View arg0, MotionEvent arg1) {

                joystick.drawStick(arg1);

                if (arg1.getAction() == MotionEvent.ACTION_DOWN
                        || arg1.getAction() == MotionEvent.ACTION_MOVE) {

                    textView1.setText("X : " + String.valueOf(joystick.getX()));
                    textView2.setText("Y : " + String.valueOf(joystick.getY()));
                    textView3.setText("Angle : " + String.valueOf(joystick.getAngle()));
                    textView4.setText("Distance : " + String.valueOf(joystick.getDistance()));

                    int direction = joystick.get8Direction();
                    if (direction == JoyStick.STICK_UP) {
                        textView5.setText("Direction : Up");
                    } else if (direction == JoyStick.STICK_UPRIGHT) {
                        textView5.setText("Direction : Up Right");
                    } else if (direction == JoyStick.STICK_RIGHT) {
                        textView5.setText("Direction : Right");
                    } else if (direction == JoyStick.STICK_DOWNRIGHT) {
                        textView5.setText("Direction : Down Right");
                    } else if (direction == JoyStick.STICK_DOWN) {
                        textView5.setText("Direction : Down");
                    } else if (direction == JoyStick.STICK_DOWNLEFT) {
                        textView5.setText("Direction : Down Left");
                    } else if (direction == JoyStick.STICK_LEFT) {
                        textView5.setText("Direction : Left");
                    } else if (direction == JoyStick.STICK_UPLEFT) {
                        textView5.setText("Direction : Up Left");
                    } else if (direction == JoyStick.STICK_NONE) {
                        textView5.setText("Direction : Center");
                    }
                } else if (arg1.getAction() == MotionEvent.ACTION_UP) {
                    textView1.setText("X :");
                    textView2.setText("Y :");
                    textView3.setText("Angle :");
                    textView4.setText("Distance :");
                    textView5.setText("Direction :");
                }

                return true;
            }
        });

    }
}
