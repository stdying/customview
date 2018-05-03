package stdying.customview.path;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * TODO: document your custom view class.
 */
public class PathView extends View {
    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Path mPath;


    public PathView(Context context) {
        super(context, null);
    }

    public PathView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setFocusable(true);
        setFocusableInTouchMode(true);

        mPath = new Path();
        mPath.addCircle(40, 40, 45, Path.Direction.CCW);
        mPath.addCircle(80, 80, 45, Path.Direction.CCW);
        mPath.addCircle(120, 120, 45, Path.Direction.CCW);
    }

    private void showPath(Canvas canvas, int x, int y, Path.FillType ft,
                          Paint paint) {
        canvas.save();
        canvas.translate(x, y);
        canvas.clipRect(0, 0, 160, 160);
        canvas.drawColor(Color.WHITE);
        mPath.setFillType(ft);
        canvas.drawPath(mPath, paint);
        canvas.restore();
    }


    private void drawLine(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);
        int width = getWidth();
        int height = getHeight();
        canvas.drawLine(0, height / 2, width, height / 2, paint);
        canvas.drawLine(width / 2, 0, width / 2, height, paint);
    }

    private void showFillPath(Canvas canvas) {
        Paint paint = mPaint;
        paint.setColor(Color.RED);
        canvas.drawColor(0xFFCCCCCC);
        canvas.translate(20, 20);
        paint.setAntiAlias(true);
        showPath(canvas, 0, 0, Path.FillType.WINDING, paint);
        showPath(canvas, 160 * 2, 0, Path.FillType.EVEN_ODD, paint);
        showPath(canvas, 0, 160 * 2, Path.FillType.INVERSE_WINDING, paint);
        showPath(canvas, 160 * 2, 160 * 2, Path.FillType.INVERSE_EVEN_ODD, paint);
    }

    private void showAddPath(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);
        int width = getWidth();
        int height = getHeight();
        //移到屏幕中央
        canvas.translate(width / 2, height / 2);
        //y坐标反转
        canvas.scale(1, -1);

        Path path = new Path();
        Path src = new Path();
        path.addRect(-200, -200, 200, 200, Path.Direction.CW);
        src.addCircle(0, 0, 100, Path.Direction.CW);
        path.addPath(src, 0, 200);

        canvas.drawPath(path, paint);
    }

    private void showAddArch(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);
        int width = getWidth();
        int height = getHeight();
        //移到屏幕中央
        canvas.translate(width / 2, height / 2);
        //y坐标反转
        canvas.scale(1, -1);

        Path path = new Path();
        path.lineTo(100, 100);

        RectF oval = new RectF(0, 0, 300, 300);
        path.addArc(oval, 0, 270);

        canvas.drawPath(path, paint);
    }

    //绘制椭圆
    private void makeBound(Canvas canvas) {
        int width = getWidth();
        int height = getHeight();
//        Bitmap boundBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
//        Canvas c = new Canvas(boundBitmap);
        canvas.translate(50, 50);
        canvas.drawColor(Color.WHITE);
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);

        p.setColor(0xFF66AAFF);
        p.setStyle(Paint.Style.STROKE);//设置为画边框
        Path path = new Path();

        RectF rectF = new RectF(0, 0, 100, 100);//圆弧的圆心在对角线中间，即：50,50
        path.addArc(rectF, 90, 180);
        path.lineTo(200, 0);
        RectF rectF2 = new RectF(150, 0, 250, 100);//圆弧的圆心在对角线中间，即：200,50
        path.addArc(rectF2, 270, 180);
        path.moveTo(200, 100);
        path.lineTo(50, 100);
        path.close();

        canvas.drawPath(path, p);

    }

    private void showDrawArch(Canvas canvas) {
        int width = getWidth();
        int height = getHeight();
        mPaint.setStyle(Paint.Style.STROKE);//设置为画边框
        canvas.translate(width / 2, height / 2);  // 移动坐标系到屏幕中心
        //canvas.scale(1, -1);                         // <-- 注意 翻转y坐标轴

        Path path = new Path();
        //path.lineTo(100, 100);

        RectF oval = new RectF(0, 0, 300, 300);

        path.arcTo(oval, 0, 270);
        // path.arcTo(oval,0,270,false);             // <-- 和上面一句作用等价

        canvas.drawPath(path, mPaint);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //drawLine(canvas);
        //showAddPath(canvas);
        //showAddArch(canvas);
        //makeBound(canvas);
        showDrawArch(canvas);
    }

}
