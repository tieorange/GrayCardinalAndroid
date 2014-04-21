/*
 * Copyright 2012 Google Inc.
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
package fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.ListView;

import com.nhaarman.listviewanimations.swinginadapters.prepared.ScaleInAnimationAdapter;
import com.tieorange.graycardinal.app.R;

import activities.AddInfoActivity;
import activities.InfoActivity;
import adapters.InfoAdapter;
import adapters.QuickReturnListView;
import application.Constants;
import models.ContactInfo;
import tools.popupmenu.MenuItem;
import tools.popupmenu.PopupMenu;

public class BottomFragment extends ListFragment implements PopupMenu.OnItemSelectedListener{

	private QuickReturnListView mListView;
	private Button mQuickReturnView;
	private int mQuickReturnHeight;

    private final static int PLAY_SELECTION = 0;
    private final static int ADD_TO_PLAYLIST = 1;
    private final static int SEARCH = 2;

	private static final int STATE_ONSCREEN = 0;
	private static final int STATE_OFFSCREEN = 1;
	private static final int STATE_RETURNING = 2;
	private int mState = STATE_ONSCREEN;
	private int mScrollY;
	private int mMinRawY = 0;

	private TranslateAnimation anim;
    public static InfoAdapter mInfoAdapter;

    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.footer_fragment, null);
        initViews(view);

		return view;
	}

    private void initViews(View view) {
        mQuickReturnView = (Button) view.findViewById(R.id.footer);

        mQuickReturnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddInfoActivity.class);
                startActivityForResult(intent, Constants.REQUEST_CODE_ADD_INFO);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_CODE_ADD_INFO && resultCode == Activity.RESULT_OK && data != null) {
            String infoName = data.getStringExtra(Constants.EXTRAS_INFO_NAME);
            String infoValue = data.getStringExtra(Constants.EXTRAS_INFO_VALUE);

            AddInfo(infoName, infoValue);


        }
    }

    private void AddInfo(String infoName, String infoValue) {
        ContactInfo info = new ContactInfo(infoName, infoValue, InfoActivity.mContact);
        InfoActivity.mContact.save();
        info.save();


        InfoActivity.mInfoList.add(0, info);
        BottomFragment.mInfoAdapter.notifyDataSetChanged();
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        ContactInfo selectedItem = (ContactInfo) l.getItemAtPosition(position);

        // Create Instance
        PopupMenu menu = new PopupMenu(getActivity());
        menu.setHeaderTitle(selectedItem.getName());
        // Set Listener
        menu.setOnItemSelectedListener(this);
        // Add Menu (Android menu like style)
        menu.add(PLAY_SELECTION, R.string.edit_info).setIcon(
                getResources().getDrawable(R.drawable.ic_action_add_contact));
        menu.add(ADD_TO_PLAYLIST, R.string.remove_info).setIcon(
                getResources().getDrawable(R.drawable.ic_save_info_button));
        menu.show(v);

    }
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

        setListApapter();

                mListView.getViewTreeObserver().addOnGlobalLayoutListener(
                        new ViewTreeObserver.OnGlobalLayoutListener() {
                            @Override
                            public void onGlobalLayout() {
                                mQuickReturnHeight = mQuickReturnView.getHeight();
                                mListView.computeScrollY();
                            }
                        }
                );

		mListView.setOnScrollListener(new OnScrollListener() {
			@SuppressLint("NewApi")
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {

				mScrollY = 0;
				int translationY = 0;

				if (mListView.scrollYIsComputed()) {
					mScrollY = mListView.getComputedScrollY();
				}

				int rawY = mScrollY;

				switch (mState) {
				case STATE_OFFSCREEN:
					if (rawY >= mMinRawY) {
						mMinRawY = rawY;
					} else {
						mState = STATE_RETURNING;
					}
					translationY = rawY;
					break;

				case STATE_ONSCREEN:
					if (rawY > mQuickReturnHeight) {
						mState = STATE_OFFSCREEN;
						mMinRawY = rawY;
					}
					translationY = rawY;
					break;

				case STATE_RETURNING:

					translationY = (rawY - mMinRawY) + mQuickReturnHeight;

					System.out.println(translationY);
					if (translationY < 0) {
						translationY = 0;
						mMinRawY = rawY + mQuickReturnHeight;
					}

					if (rawY == 0) {
						mState = STATE_ONSCREEN;
						translationY = 0;
					}

					if (translationY > mQuickReturnHeight) {
						mState = STATE_OFFSCREEN;
						mMinRawY = rawY;
					}
					break;
				}

				/** this can be used if the build is below honeycomb **/
				if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.HONEYCOMB) {
					anim = new TranslateAnimation(0, 0, translationY,
							translationY);
					anim.setFillAfter(true);
					anim.setDuration(0);
					mQuickReturnView.startAnimation(anim);
				} else {
					mQuickReturnView.setTranslationY(translationY);
				}

			}

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
			}
		});
	}

    private void setListApapter() {
        mListView = (QuickReturnListView) getListView();
        mInfoAdapter = new InfoAdapter(getActivity(), InfoActivity.mInfoList);

        ScaleInAnimationAdapter scaleInAnimationAdapter = new ScaleInAnimationAdapter(mInfoAdapter);
        scaleInAnimationAdapter.setAbsListView(mListView);
        mListView.setAdapter(scaleInAnimationAdapter);
    }



    @Override
    public void onItemSelected(MenuItem item) {

    }
}