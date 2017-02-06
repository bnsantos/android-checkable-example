package com.bnsantos.checkable;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.Checkable;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.DraweeHolder;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.common.RotationOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

public class DraweeCheckable extends FrameLayout implements Checkable {
  private static final int[] CHECKED_STATE_SET = {android.R.attr.state_checked};
  private static final int[] SELECTABLE_STATE_SET = {R.attr.selectable};
  private static final int[] ENABLED_SET = { android.R.attr.state_enabled };

  private boolean mChecked = false;
  private boolean mSelectable;

  private DraweeHolder mDraweeHolder;
  private Drawable mForegroundSelector;
  private StateListDrawable mStateListDrawable;

  public DraweeCheckable(Context context) {
    super(context);
    init();
  }

  public DraweeCheckable(Context context, AttributeSet attrs) {
    super(context, attrs);
    init();
  }

  public DraweeCheckable(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init();
  }

  @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
  public DraweeCheckable(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
    init();
  }

  private void init(){
    GenericDraweeHierarchy hierarchy = new GenericDraweeHierarchyBuilder(getResources())
        .build();
    mDraweeHolder = DraweeHolder.create(hierarchy, getContext());
    mDraweeHolder.getTopLevelDrawable().setCallback(this);

    mStateListDrawable = new StateListDrawable();

    mForegroundSelector = ContextCompat.getDrawable(getContext(), R.drawable.checkable_bg);
    mForegroundSelector.setCallback(this);
  }

  @Override
  public boolean isChecked() {
    return mChecked;
  }

  @Override
  public void setChecked(boolean checked) {
    if (checked != mChecked) {
      mChecked = checked;
      refreshDrawableState();
      invalidate();
    }

    if(checked){
      setPadding(20, 20, 20, 20);
    }else {
      setPadding(0, 0, 0, 0);
    }
  }

  @Override
  public void toggle() {
    setChecked(!mChecked);
  }

  public boolean isSelectable(){
    return mSelectable;
  }

  public void setSelectable(boolean selectable){
    if (selectable != mSelectable) {
      mSelectable = selectable;
      refreshDrawableState();
      invalidate();
    }
  }

  @Override
  public int[] onCreateDrawableState(int extraSpace) {
    Log.i("BRUNO", "onCreateDrawableState");
    final int[] drawableState = super.onCreateDrawableState(extraSpace + 2);
    if(isSelectable()){
      mergeDrawableStates(drawableState, SELECTABLE_STATE_SET);
    }
    if (isChecked()) {
      mergeDrawableStates(drawableState, CHECKED_STATE_SET);
    }
    return drawableState;
  }

  @Override
  protected void drawableStateChanged() {
    super.drawableStateChanged();
    mForegroundSelector.setState(getDrawableState());
    invalidate();
  }

  @Override
  protected void onSizeChanged(int w, int h, int oldw, int oldh) {
    super.onSizeChanged(w, h, oldw, oldh);
    mForegroundSelector.setBounds(0, 0, w, h);
  }

  @Override
  public void jumpDrawablesToCurrentState() {
    super.jumpDrawablesToCurrentState();
    mForegroundSelector.jumpToCurrentState();
  }

  /*
    Fresco
   */

  @Override
  protected void dispatchDraw(Canvas canvas) {
    super.dispatchDraw(canvas);

    Drawable drawable =  mDraweeHolder.getTopLevelDrawable();
    drawable.setState(getDrawableState());
    drawable.setBounds(getPaddingLeft(), getPaddingTop(), getWidth()-getPaddingRight(), getHeight()-getPaddingBottom());
    drawable.draw(canvas);

    mForegroundSelector.draw(canvas);
  }

  @Override
  protected void onDraw(Canvas canvas) {
    Log.i("BRUNO", "onDraw");
    Drawable drawable =  mDraweeHolder.getTopLevelDrawable();
    drawable.setState(getDrawableState());
    drawable.setBounds(getPaddingLeft(), getPaddingTop(), getWidth()-getPaddingRight(), getHeight()-getPaddingBottom());
    drawable.draw(canvas);

    mForegroundSelector.draw(canvas);

    /*mStateListDrawable.addState(ENABLED_STATE_SET, drawable);
    mStateListDrawable.addState(CHECKED_STATE_SET, new ColorDrawable(Color.BLUE));*/
//    mStateListDrawable.addState(SELECTABLE_STATE_SET, mDrawable);

/*    mStateListDrawable.draw(canvas);
    setImageDrawable(mStateListDrawable);*/

    /*Paint paint = new Paint();
    paint.setColor(Color.BLACK);
    paint.setStyle(Paint.Style.FILL);
    paint.setAntiAlias(true);
    paint.setStyle(Paint.Style.STROKE);
    paint.setStrokeJoin(Paint.Join.ROUND);
    paint.setStrokeCap(Paint.Cap.ROUND);*/
    //canvas.drawRect(getPaddingLeft(), getPaddingTop(), getWidth()-getPaddingRight(), getHeight()-getPaddingBottom(), paint);
//    super.onDraw(canvas);
  }

  @Override
  protected void onDetachedFromWindow() {
    super.onDetachedFromWindow();
    mDraweeHolder.onDetach();
    mDraweeHolder.getTopLevelDrawable().setCallback(null);
  }

  @Override
  public void onStartTemporaryDetach() {
    super.onStartTemporaryDetach();
    mDraweeHolder.onDetach();
    mDraweeHolder.getTopLevelDrawable().setCallback(null);
  }

  @Override
  protected void onAttachedToWindow() {
    super.onAttachedToWindow();
    mDraweeHolder.onAttach();
    mDraweeHolder.getTopLevelDrawable().setCallback(this);
  }

  @Override
  public void onFinishTemporaryDetach() {
    super.onFinishTemporaryDetach();
    mDraweeHolder.onAttach();
    mDraweeHolder.getTopLevelDrawable().setCallback(this);
  }

  @Override
  protected boolean verifyDrawable(Drawable who) {
    return who == mDraweeHolder.getTopLevelDrawable() || who == mForegroundSelector || super.verifyDrawable(who);
  }

  public void setImage(Uri uri, ResizeOptions options){
    ImageRequest imageRequest = ImageRequestBuilder.newBuilderWithSource(uri)
        .setRotationOptions(RotationOptions.autoRotate())
        .setResizeOptions(options).build();
    DraweeController controller = Fresco.newDraweeControllerBuilder()
        .setImageRequest(imageRequest)
        .setOldController(mDraweeHolder.getController())
        .build();
    mDraweeHolder.setController(controller);
  }
}
