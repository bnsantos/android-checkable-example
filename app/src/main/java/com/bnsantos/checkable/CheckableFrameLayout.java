/*
 * Copyright 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.bnsantos.checkable;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Checkable;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

/**
 * This is a simple wrapper for {@link FrameLayout} that implements the {@link Checkable}
 * interface by keeping an internal 'checked' state flag.
 * <p>
 * This can be used as the root view for a custom list item layout for
 * {@link android.widget.AbsListView} elements with a
 * {@link android.widget.AbsListView#setChoiceMode(int) choiceMode} set.
 */
public class CheckableFrameLayout extends FrameLayout implements Checkable {
  private static final int[] CHECKED_STATE_SET = {android.R.attr.state_checked};
  private static final int[] SELECTABLE_STATE_SET = {R.attr.selectable};

  private boolean mChecked = false;
  private boolean mSelectable;

  public CheckableFrameLayout(Context context, AttributeSet attrs) {
    super(context, attrs);
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
    }
  }

  @Override
  public int[] onCreateDrawableState(int extraSpace) {
    final int[] drawableState = super.onCreateDrawableState(extraSpace + 2);
    if(isSelectable()){
      mergeDrawableStates(drawableState, SELECTABLE_STATE_SET);
    }
    if (isChecked()) {
      mergeDrawableStates(drawableState, CHECKED_STATE_SET);
    }
    return drawableState;
  }
}