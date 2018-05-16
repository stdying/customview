package stdying.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static android.view.Gravity.BOTTOM;
import static android.view.Gravity.LEFT;
import static android.view.Gravity.RIGHT;
import static android.view.Gravity.TOP;

/**
 * TODO: document your custom view class.
 * <p>
 * 漂浮的小球
 * <p>
 * 通过canvas.drawBitmap，修改rect使小球动起来
 */
public class FloatView extends View {
    private static final String TAG = "FloatView";
    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private static final float[][] STAR_LOCATION = new float[][]{
            {0.5f, 0.2f}, {0.68f, 0.35f}, {0.5f, 0.05f},
            {0.15f, 0.15f}, {0.5f, 0.5f}, {0.15f, 0.8f},
            {0.2f, 0.3f}, {0.77f, 0.4f}, {0.75f, 0.5f},
            {0.8f, 0.55f}, {0.9f, 0.6f}, {0.1f, 0.7f},
            {0.1f, 0.1f}, {0.7f, 0.8f}, {0.5f, 0.6f}
    };

    private String mString; // TODO: use a default from R.string...
    private int mColor = Color.RED; // TODO: use a default from R.color...
    private float mDimension = 0; // TODO: use a default from R.dimen...
    private Drawable mDrawable;

    private TextPaint mTextPaint;
    private float mTextWidth;
    private float mTextHeight;

    private Bitmap mBack;
    private Bitmap mStarOne;
    private Bitmap mStarTwo;
    private Bitmap mStarThree;

    private int mStarCount = 10;

    private int mBackWidth;
    private int mBackHeight;

    private int mStarOneWidth;
    private int mStarOneHeight;

    private int mStarTwoWidth;
    private int mStarTwoHeight;

    private int mStarThreeWidth;
    private int mStarThreeHeight;

    private float mLowSpeed;
    private float mMidSpeed;
    private float mFastSpeed;

    private int mTotalWidth;
    private int mTotalHeight;

    private List<StarInfo> mStarInfos;

    private Rect mBackRect;
    private Rect mStarOneSrcRect;
    private Rect mStarTwoSrcRect;
    private Rect mStarThreeSrcRect;


    public FloatView(Context context) {
        super(context);
        init(null, 0);
    }

    public FloatView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public FloatView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.FloatView, defStyle, 0);

        mString = a.getString(
                R.styleable.FloatView_floatviewString);
        mColor = a.getColor(
                R.styleable.FloatView_floatviewColor,
                mColor);
        // Use getDimensionPixelSize or getDimensionPixelOffset when dealing with
        // values that should fall on pixel boundaries.
        mDimension = a.getDimension(
                R.styleable.FloatView_floatviewDimension,
                mDimension);

        if (a.hasValue(R.styleable.FloatView_floatviewDrawable)) {
            mDrawable = a.getDrawable(
                    R.styleable.FloatView_floatviewDrawable);
            mDrawable.setCallback(this);
        }

        a.recycle();

        // Set up a default TextPaint object
        mTextPaint = new TextPaint();
        mTextPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextAlign(Paint.Align.LEFT);

        // Update TextPaint and text measurements from attributes
        //invalidateTextPaintAndMeasurements();

    }


    private void invalidateTextPaintAndMeasurements() {
        mTextPaint.setTextSize(mDimension);
        mTextPaint.setColor(mColor);
        mTextWidth = mTextPaint.measureText(mString);

        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        mTextHeight = fontMetrics.bottom;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Log.i(TAG, "onSizeChanged: ");
        mTotalHeight = getHeight();
        mTotalWidth = getWidth();

        mStarInfos = new ArrayList<>();
        intBitmap();
        initStarInfo();
    }

    private void initStarInfo() {
        StarInfo starInfo;
        Random random = new Random();
        for (int i = 0; i < STAR_LOCATION.length; i++) {
            //星球大小
            float startSize = getStarSize(0.4f, 0.9f);
            float[] startLocation = STAR_LOCATION[i];

            starInfo = new StarInfo();
            starInfo.sizePercent = startSize;
            //初始化速度
            int randomSpeed = random.nextInt(3);
            switch (randomSpeed) {
                case 0:
                    starInfo.speed = mLowSpeed;
                    break;
                case 1:
                    starInfo.speed = mMidSpeed;
                    break;
                case 2:
                    starInfo.speed = mFastSpeed;
                    break;

                default:
                    starInfo.speed = mMidSpeed;
                    break;
            }

            //初始化透明度
            starInfo.alpha = getStarSize(0.3f, 0.8f);
            //初始位置
            starInfo.x = (int) (startLocation[0] * mTotalWidth);
            starInfo.y = (int) (startLocation[1] * mTotalHeight);

            starInfo.direction = getStarDirct();
            mStarInfos.add(starInfo);

        }

        new Thread(new Runnable() {
            @Override
            public void run() {

                while (true) {
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    for (int i = 0; i < STAR_LOCATION.length; i++) {
                        //drawStarDynamic(i, mStarInfos.get(i), canvas, mPaint);
                        resetStarFloat(mStarInfos.get(i));
                        postInvalidate();

                    }
                }

            }
        }).start();
    }

    private void intBitmap() {
        mBack = ((BitmapDrawable) getResources().getDrawable(R.drawable.back)).getBitmap();
        mBackHeight = mBack.getHeight();
        mBackWidth = mBack.getWidth();
        mBackRect = new Rect(0, 0, mBack.getWidth(), mBack.getHeight());

        mStarOne = ((BitmapDrawable) getResources().getDrawable(R.drawable.circle1)).getBitmap();
        mStarOneHeight = mStarOne.getHeight();
        mStarOneWidth = mStarOne.getWidth();
        mStarOneSrcRect = new Rect(0, 0, mStarOneWidth, mStarOneHeight);

        mStarTwo = ((BitmapDrawable) getResources().getDrawable(R.drawable.circle2)).getBitmap();
        mStarTwoWidth = mStarTwo.getWidth();
        mStarTwoHeight = mStarTwo.getHeight();
        mStarTwoSrcRect = new Rect(0, 0, mStarTwoWidth, mStarTwoHeight);

        mStarThree = ((BitmapDrawable) getResources().getDrawable(R.drawable.circle3)).getBitmap();
        mStarThreeWidth = mStarThree.getWidth();
        mStarThreeHeight = mStarThree.getHeight();
        mStarThreeSrcRect = new Rect(0, 0, mStarThreeWidth, mStarThreeHeight);

        mLowSpeed = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 0.5f, getResources().getDisplayMetrics());
        mMidSpeed = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 0.7f, getResources().getDisplayMetrics());
        mFastSpeed = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1f, getResources().getDisplayMetrics());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawBitmap(mBack, null, mBackRect, mPaint);

        for (int i = 0; i < STAR_LOCATION.length; i++) {
            drawStarDynamic(i, mStarInfos.get(i), canvas, mPaint);
        }

    }

    /**
     * Gets the example string attribute value.
     *
     * @return The example string attribute value.
     */
    public String getString() {
        return mString;
    }

    /**
     * Sets the view's example string attribute value. In the example view, this string
     * is the text to draw.
     *
     * @param exampleString The example string attribute value to use.
     */
    public void setString(String exampleString) {
        mString = exampleString;
        invalidateTextPaintAndMeasurements();
    }

    /**
     * Gets the example color attribute value.
     *
     * @return The example color attribute value.
     */
    public int getColor() {
        return mColor;
    }

    /**
     * Sets the view's example color attribute value. In the example view, this color
     * is the font color.
     *
     * @param exampleColor The example color attribute value to use.
     */
    public void setColor(int exampleColor) {
        mColor = exampleColor;
        invalidateTextPaintAndMeasurements();
    }

    /**
     * Gets the example dimension attribute value.
     *
     * @return The example dimension attribute value.
     */
    public float getDimension() {
        return mDimension;
    }

    /**
     * Sets the view's example dimension attribute value. In the example view, this dimension
     * is the font size.
     *
     * @param exampleDimension The example dimension attribute value to use.
     */
    public void setDimension(float exampleDimension) {
        mDimension = exampleDimension;
        invalidateTextPaintAndMeasurements();
    }

    /**
     * Gets the example drawable attribute value.
     *
     * @return The example drawable attribute value.
     */
    public Drawable getDrawable() {
        return mDrawable;
    }

    /**
     * Sets the view's example drawable attribute value. In the example view, this drawable is
     * drawn above the text.
     *
     * @param exampleDrawable The example drawable attribute value to use.
     */
    public void setDrawable(Drawable exampleDrawable) {
        mDrawable = exampleDrawable;
    }

    private float getStarSize(float start, float end) {
        float nextfloat = (float) Math.random();
        if (start < nextfloat
                && nextfloat < end) {
            return nextfloat;
        } else {
            return (float) Math.random();
        }
    }

    private int getStarDirct() {
        Random random = new Random();
        int radomInt = random.nextInt(4);
        int direct = 0;
        switch (radomInt) {
            case 0:
                direct = LEFT;
                break;
            case 1:
                direct = RIGHT;
                break;
            case 2:
                direct = TOP;
                break;
            case 3:
                direct = BOTTOM;
                break;
            default:
                break;

        }
        return direct;
    }

    private void drawStarDynamic(int count, StarInfo starInfo,
                                 Canvas canvas, Paint paint) {
        Log.i(TAG, "drawStarDynamic: ");
        float startAlpha = starInfo.alpha;
        int x = starInfo.x;
        int y = starInfo.y;
        Log.i(TAG, "drawStarDynamic1: x:" + x + " y:" + y);
        float sizePrecent = starInfo.sizePercent;

        x = (int) (x / sizePrecent);
        y = (int) (y / sizePrecent);
        Log.i(TAG, "drawStarDynamic2: x:" + x + " y:" + y);
        Bitmap bitmap;
        Rect srcRect;
        Rect destRect = new Rect();

        if (count % 3 == 0) {
            bitmap = mStarOne;
            srcRect = mStarOneSrcRect;
            destRect.set(x, y, x + mStarOneWidth, y + mStarOneHeight);
        } else if (count % 2 == 0) {
            srcRect = mStarThreeSrcRect;
            bitmap = mStarThree;
            destRect.set(x, y, x + mStarThreeWidth, y + mStarThreeHeight);
        } else {
            srcRect = mStarTwoSrcRect;
            bitmap = mStarTwo;
            destRect.set(x, y, x + mStarTwoWidth, y + mStarTwoHeight);
        }

        paint.setAlpha((int) (startAlpha * 255));
        canvas.save();
        canvas.scale(sizePrecent, sizePrecent);
        canvas.drawBitmap(bitmap, srcRect, destRect, paint);
        canvas.restore();
    }

    private void resetStarFloat(StarInfo starInfo) {
        switch (starInfo.direction) {
            case LEFT:
                starInfo.x -= starInfo.speed;
                break;
            case RIGHT:
                starInfo.x += starInfo.speed;
                break;
            case TOP:
                starInfo.y -= starInfo.speed;
                break;
            case BOTTOM:
                starInfo.y += starInfo.speed;
                break;
            default:
                break;
        }
    }

    class StarInfo {
        float sizePercent;
        int x;
        int y;
        float alpha;
        int direction;
        float speed;
    }
}
