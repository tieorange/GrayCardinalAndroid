/*
 * Copyright 2013 Lars Werkman
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

package adapters;

import com.tieorange.graycardinal.app.R;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import tools.ContactsHelper;

public class QuickReturnListView extends ListView {

    private static String LOG_TAG = QuickReturnListView.class.getSimpleName();
    private int mItemCount;
    private List<Integer> mItemOffsetY = new ArrayList<Integer>();
    private boolean scrollIsComputed = false;
    private int mHeight;

    public QuickReturnListView(Context context) {
        super(context);
    }

    public QuickReturnListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public int getListHeight() {
        return mHeight;
    }

    public void computeScrollY() {
        mHeight = 0;
        mItemCount = getAdapter().getCount();
        mItemOffsetY.clear();

        try {
            if (mItemCount > 0) {
                View view = getAdapter().getView(0, null, this);
                view.setLayoutParams(new LayoutParams(
                        LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
                view.measure(
                        MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
                        MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
                int viewMeasuredHeight = view.getMeasuredHeight();
                for (int i = 0; i < mItemCount; i++) {
                    mItemOffsetY.add(i, mHeight);
                    mHeight += viewMeasuredHeight + ContactsHelper
                            .convertToPixels(
                                    (int) getResources().getDimension(R.dimen.divider_info),
                                    getContext());
                }
            }
        } catch (Exception ex) {
            Log.e(LOG_TAG, ex.toString());
        }
        scrollIsComputed = true;
    }

    public boolean scrollYIsComputed() {
        return scrollIsComputed;
    }

    public int getComputedScrollY() {
        int pos, nScrollY, nItemY;
        View view = null;
        pos = getFirstVisiblePosition();
        view = getChildAt(0);
        nItemY = view.getTop();
        nScrollY = 0;
        try {
            nScrollY = mItemOffsetY.get(pos) - nItemY;
        }catch (Exception ex)
        {
            Log.e(LOG_TAG, ex.toString());
        }
        return nScrollY;
    }
}