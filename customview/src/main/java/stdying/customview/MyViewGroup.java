package stdying.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * TODO: document your custom view class.
 */
public class MyViewGroup extends ViewGroup {


    public MyViewGroup(Context context) {
        super(context);
    }

    public MyViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyViewGroup(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

//    /**
//     * 遍历所有ziview，测量自己
//     *
//     * @param widthMeasureSpec
//     * @param heightMeasureSpec
//     */
//    protected void measureChildren(int widthMeasureSpec, int heightMeasureSpec) {
//        final int size = getChildCount();
//        for (int i = 0; i < size; i++) {
//            View childView = getChildAt(i);
//            if (childView.getVisibility() != GONE) {
//                measureChild(childView, widthMeasureSpec, heightMeasureSpec);
//            }
//        }
//    }
//
//    /**
//     * 测量单个视图，将宽高和padding加在一起后交给getChildMeasureSpec去获得最终的测量值
//     *
//     * @param child
//     * @param parentWidthMeasureSpec
//     * @param parentHeightMeasureSpec
//     */
//    @Override
//    protected void measureChild(View child, int parentWidthMeasureSpec, int parentHeightMeasureSpec) {
//        //super.measureChild(child, parentWidthMeasureSpec, parentHeightMeasureSpec);
//        final LayoutParams lp = child.getLayoutParams();
//
//        //通过getChildMeasureSpec获取最终的宽高详细测量值
//        final int childWidthMeasureSpec = getChildMeasureSpec(parentWidthMeasureSpec, 10, lp.width);
//        final int childHeightMeaureSped = getChildMeasureSpec(parentHeightMeasureSpec, 10, lp.height);
//        child.measure(childWidthMeasureSpec, childHeightMeaureSped);
//    }


}