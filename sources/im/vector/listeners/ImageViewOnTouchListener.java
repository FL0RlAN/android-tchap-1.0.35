package im.vector.listeners;

import android.graphics.Matrix;
import android.graphics.PointF;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;

public abstract class ImageViewOnTouchListener implements OnTouchListener {
    private static final int DRAG = 1;
    private static final int NONE = 0;
    private static final int ZOOM = 2;
    private float d = 0.0f;
    private float[] lastEvent = null;
    private Matrix matrix = new Matrix();
    private PointF mid = new PointF();
    private int mode = 0;
    private float newRot = 0.0f;
    private float oldDist = 1.0f;
    private Matrix savedMatrix = new Matrix();
    private PointF start = new PointF();

    public void setStartMatrix(Matrix matrix2) {
        this.matrix.set(matrix2);
    }

    private float spacing(MotionEvent motionEvent) {
        float x = motionEvent.getX(0) - motionEvent.getX(1);
        float y = motionEvent.getY(0) - motionEvent.getY(1);
        return (float) Math.sqrt((double) ((x * x) + (y * y)));
    }

    private void midPoint(PointF pointF, MotionEvent motionEvent) {
        pointF.set((motionEvent.getX(0) + motionEvent.getX(1)) / 2.0f, (motionEvent.getY(0) + motionEvent.getY(1)) / 2.0f);
    }

    private float rotation(MotionEvent motionEvent) {
        return (float) Math.toDegrees(Math.atan2((double) (motionEvent.getY(0) - motionEvent.getY(1)), (double) (motionEvent.getX(0) - motionEvent.getX(1))));
    }

    /* JADX WARNING: Code restructure failed: missing block: B:8:0x0019, code lost:
        if (r0 != 6) goto L_0x0102;
     */
    public boolean onTouch(View view, MotionEvent motionEvent) {
        ImageView imageView = (ImageView) view;
        int action = motionEvent.getAction() & 255;
        if (action != 0) {
            if (action != 1) {
                if (action == 2) {
                    int i = this.mode;
                    if (i == 1) {
                        this.matrix.set(this.savedMatrix);
                        this.matrix.postTranslate(motionEvent.getX() - this.start.x, motionEvent.getY() - this.start.y);
                    } else if (i == 2) {
                        float spacing = spacing(motionEvent);
                        if (spacing > 10.0f) {
                            this.matrix.set(this.savedMatrix);
                            float f = spacing / this.oldDist;
                            this.matrix.postScale(f, f, this.mid.x, this.mid.y);
                        }
                        if (this.lastEvent != null && motionEvent.getPointerCount() == 3) {
                            this.newRot = rotation(motionEvent);
                            float f2 = this.newRot - this.d;
                            float[] fArr = new float[9];
                            this.matrix.getValues(fArr);
                            float f3 = fArr[2];
                            float f4 = fArr[5];
                            float f5 = fArr[0];
                            this.matrix.postRotate(f2, f3 + (((float) (imageView.getWidth() / 2)) * f5), f4 + (((float) (imageView.getHeight() / 2)) * f5));
                        }
                    }
                } else if (action == 5) {
                    this.oldDist = spacing(motionEvent);
                    if (this.oldDist > 10.0f) {
                        this.savedMatrix.set(this.matrix);
                        midPoint(this.mid, motionEvent);
                        this.mode = 2;
                    }
                    this.lastEvent = new float[4];
                    this.lastEvent[0] = motionEvent.getX(0);
                    this.lastEvent[1] = motionEvent.getX(1);
                    this.lastEvent[2] = motionEvent.getY(0);
                    this.lastEvent[3] = motionEvent.getY(1);
                    this.d = rotation(motionEvent);
                }
            }
            this.mode = 0;
            this.lastEvent = null;
        } else {
            this.savedMatrix.set(this.matrix);
            this.start.set(motionEvent.getX(), motionEvent.getY());
            this.mode = 1;
            this.lastEvent = null;
        }
        imageView.setImageMatrix(this.matrix);
        return true;
    }
}
