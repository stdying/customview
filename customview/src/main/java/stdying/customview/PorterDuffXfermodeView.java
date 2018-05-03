package stdying.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

/**
 * TODO: document your custom view class.
 * <p>
 * 参考
 * <p>
 * http://www.gcssloop.com/customview/Color
 * <p>
 * http://www.runoob.com/w3cnote/android-tutorial-xfermode-porterduff.html
 */
public class PorterDuffXfermodeView extends View {
    private String mporterDuffString; // TODO: use a default from R.string...string
    private int mporterDuffColor = Color.RED; // TODO: use a default from R.color...
    private float mporterDuffDimension = 0; // TODO: use a default from R.dimen...
    private Drawable mporterDuffDrawable;

    private TextPaint mTextPaint;
    private float mTextWidth;
    private float mTextHeight;

    private Paint mPaint;

    private Bitmap mSrcBRectBlue;
    private Bitmap mDstBOvalYellow;

    private int width = 600;
    private int height = 600;

    static Bitmap makeDst(int w, int h) {
        Bitmap bm = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bm);
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);

        p.setColor(0xFFFFCC44);
        c.drawOval(new RectF(0, 0, w * 3 / 4, h * 3 / 4), p);
        return bm;
    }

    // create a bitmap with a rect, used for the "src" image
    static Bitmap makeSrc(int w, int h) {
        Bitmap bm = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bm);
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);

        p.setColor(0xFF66AAFF);
        c.drawRect(w / 3, h / 3, w * 19 / 20, h * 19 / 20, p);
        return bm;
    }

    public PorterDuffXfermodeView(Context context) {
        super(context);
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL);//设置为画边框
        //init(null, 0);
        mSrcBRectBlue = makeSrc(width, height);
        mDstBOvalYellow = makeDst(width, height);
    }

    public PorterDuffXfermodeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL);//设置为画边框
        //init(attrs, 0);
        mSrcBRectBlue = makeSrc(width, height);
        mDstBOvalYellow = makeDst(width, height);
    }

    public PorterDuffXfermodeView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        ///init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.PorterDuffXfermodeView, defStyle, 0);

        mporterDuffString = a.getString(
                R.styleable.PorterDuffXfermodeView_porterDuffString);
        mporterDuffColor = a.getColor(
                R.styleable.PorterDuffXfermodeView_porterDuffColor,
                mporterDuffColor);
        // Use getDimensionPixelSize or getDimensionPixelOffset when dealing with
        // values that should fall on pixel boundaries.
        mporterDuffDimension = a.getDimension(
                R.styleable.PorterDuffXfermodeView_porterDuffDimension,
                mporterDuffDimension);

        if (a.hasValue(R.styleable.PorterDuffXfermodeView_porterDuffDrawable)) {
            mporterDuffDrawable = a.getDrawable(
                    R.styleable.PorterDuffXfermodeView_porterDuffDrawable);
            mporterDuffDrawable.setCallback(this);
        }

        a.recycle();

        // Set up a default TextPaint object
        mTextPaint = new TextPaint();
        mTextPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextAlign(Paint.Align.LEFT);

        // Update TextPaint and text measurements from attributes
        invalidateTextPaintAndMeasurements();
    }

    private void invalidateTextPaintAndMeasurements() {
        mTextPaint.setTextSize(mporterDuffDimension);
        mTextPaint.setColor(mporterDuffColor);
        mTextWidth = mTextPaint.measureText(mporterDuffString);

        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        mTextHeight = fontMetrics.bottom;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //setLayerType(LAYER_TYPE_HARDWARE, null);


        //drawNormal(canvas);
        //drawClear(canvas);
        drawLayoutId(canvas);
    }

    //普通绘制
    private void drawNormal(Canvas canvas) {
        //设置背景色
        canvas.drawARGB(255, 139, 197, 186);

        int canvasWidth = canvas.getWidth();
        int r = canvasWidth / 3;
        //绘制黄色的圆形
        mPaint.setColor(0xFFFFCC44);
        canvas.drawCircle(r, r, r, mPaint);
        //绘制蓝色的矩形
        mPaint.setColor(0xFF66AAFF);
        canvas.drawRect(r, r, r * 2.7f, r * 2.7f, mPaint);
    }

    //Model clear
    private void drawClear(Canvas canvas) {
        //设置背景色
        canvas.drawColor(Color.GREEN);

        int canvasWidth = canvas.getWidth();
        int r = canvasWidth / 3;
        //绘制黄色的圆形
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.YELLOW);
        canvas.drawCircle(r, r, r, mPaint);

        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        //绘制蓝色的矩形
        mPaint.setColor(Color.RED);
        canvas.drawRect(r, r, r * 2.7f, r * 2.7f, mPaint);
        mPaint.setXfermode(null);
    }

    //使用新Layer
    private void drawLayoutId(Canvas canvas) {
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL);

        //设置背景色
        canvas.drawColor(Color.GREEN);

        int canvasWidth = canvas.getWidth();
        int canvasHeight = canvas.getHeight();


        int layerId = canvas.saveLayer(0, 0, canvasWidth, canvasHeight, null, Canvas.ALL_SAVE_FLAG);

        int r = canvasWidth / 3;
        canvas.drawBitmap(mDstBOvalYellow, 0, 0, mPaint);
        //正常绘制黄色的圆形
//        mPaint.setColor(Color.YELLOW);
//        canvas.drawCircle(r, r, r, mPaint);
//        //使用CLEAR作为PorterDuffXfermode绘制蓝色的矩形
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.MULTIPLY));
//        mPaint.setColor(Color.RED);
//        canvas.drawRect(r, r, r * 2.7f, r * 2.7f, mPaint);
//        //最后将画笔去除Xfermode
//        mPaint.setXfermode(null);
        canvas.drawBitmap(mSrcBRectBlue, 0, 0, mPaint);
        canvas.restoreToCount(layerId);
    }

}
