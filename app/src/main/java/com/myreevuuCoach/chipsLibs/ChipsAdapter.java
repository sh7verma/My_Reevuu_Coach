package com.myreevuuCoach.chipsLibs;

import android.content.Context;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.myreevuuCoach.R;
import com.myreevuuCoach.utils.Constants;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class ChipsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = ChipsAdapter.class.toString();
    private static final int TYPE_EDIT_TEXT = 0;
    private static final int TYPE_ITEM = 1;
    private Context mContext;
    private ChipsInput mChipsInput;
    private List<ChipInterface> mChipList = new ArrayList<>();
    private String mHintLabel;
    private ChipsInputEditText mEditText;
    private RecyclerView mRecycler;

    public ChipsAdapter(Context context, ChipsInput chipsInput, RecyclerView recycler) {
        mContext = context;
        mChipsInput = chipsInput;
        mRecycler = recycler;
        mHintLabel = mChipsInput.getHint();
        mEditText = mChipsInput.getEditText();
        initEditText();
    }

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_EDIT_TEXT)
            return new EditTextViewHolder(mEditText);
        else
            return new ItemViewHolder(mChipsInput.getChipView());

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        // edit text
        if (position == mChipList.size()) {
            if (mChipList.size() == 0)
                mEditText.setHint(mHintLabel);

            // auto fit edit text
            autofitEditText();
        }
        // chip
        else if (getItemCount() > 1) {
            ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
            itemViewHolder.chipView.inflate(getItem(position));
            // handle click
            handleClickOnEditText(itemViewHolder.chipView, position);
        }
    }

    @Override
    public int getItemCount() {
        return mChipList.size() + 1;
    }

    private ChipInterface getItem(int position) {
        return mChipList.get(position);
    }

    @Override
    public int getItemViewType(int position) {
        if (position == mChipList.size())
            return TYPE_EDIT_TEXT;

        return TYPE_ITEM;
    }

    @Override
    public long getItemId(int position) {
        return mChipList.get(position).hashCode();
    }

    private void initEditText() {
        mEditText.setLayoutParams(new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        mEditText.setHint(mHintLabel);
        mEditText.setBackgroundResource(android.R.color.transparent);
        // prevent fullscreen on landscape
        mEditText.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);
        mEditText.setPrivateImeOptions("nm");
        mEditText.setMaxLines(2);

        // no suggestion
        mEditText.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);

        // handle back space
        mEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // backspace
                if (event.getAction() == KeyEvent.ACTION_DOWN
                        && event.getKeyCode() == KeyEvent.KEYCODE_DEL) {

                    // remove last chip
                    if (mChipList.size() > 0 && mEditText.getText().toString().length() == 0)
                        removeChip(mChipList.size() - 1);
                }

                return false;
            }
        });

        mEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if (!mEditText.getText().toString().isEmpty()) {
                        if (isValidEmail(mEditText.getText().toString().trim())) {
                            if (mChipList.size() < 5)
                                mChipsInput.addChip(mEditText.getText().toString().trim());
                            else
                                Toast.makeText(mContext, R.string.error_max_email, Toast.LENGTH_SHORT).show();
                        } else
                            Toast.makeText(mContext, R.string.enter_valid_email, Toast.LENGTH_SHORT).show();
                    } else {
                        Constants.Companion.closeKeyboard(mContext, mEditText);
                    }
                    return true;
                }

                return false;
            }
        });


        // text changed
        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().startsWith(" ")) {
                    mEditText.setText("");
                    return;
                }
                if (s.toString().contains(" ")) {
                    if (!mEditText.getText().toString().isEmpty()) {
                        if (isValidEmail(mEditText.getText().toString().trim())) {
                            if (mChipList.size() < 5)
                                mChipsInput.addChip(mEditText.getText().toString().trim());
                            else
                                Toast.makeText(mContext, R.string.error_max_email, Toast.LENGTH_SHORT).show();
                        } else
                            Toast.makeText(mContext, R.string.enter_valid_email, Toast.LENGTH_SHORT).show();
                    } else {
                        Constants.Companion.closeKeyboard(mContext, mEditText);
                    }
                } else {
                    mChipsInput.onTextChanged(s);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void autofitEditText() {
        // min width of edit text = 50 dp
        ViewGroup.LayoutParams params = mEditText.getLayoutParams();
        params.width = ViewUtil.dpToPx(50);
        mEditText.setLayoutParams(params);

        // listen to change in the tree
        mEditText.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                // get right of recycler and left of edit text
                int right = mRecycler.getRight();
                int left = mEditText.getLeft();

                // edit text will fill the space
                ViewGroup.LayoutParams params = mEditText.getLayoutParams();
                params.width = right - left - ViewUtil.dpToPx(8);
                mEditText.setLayoutParams(params);

                if (mChipList.size() > 0)
                    // request focus
                    mEditText.requestFocus();

                // remove the listener:
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                    mEditText.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                } else {
                    mEditText.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            }

        });
    }

    private void handleClickOnEditText(ChipView chipView, final int position) {
        // delete chip
        chipView.setOnDeleteClicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeChip(position);
            }
        });

        // show detailed chip
        if (mChipsInput.isShowChipDetailed()) {
            chipView.setOnChipClicked(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // get chip position
                    int[] coord = new int[2];
                    v.getLocationInWindow(coord);

                }
            });
        }
    }

    public void addChip(ChipInterface chip) {
        if (!listContains(mChipList, chip)) {
            mChipList.add(chip);
            // notify listener
            mChipsInput.onChipAdded(chip, mChipList.size());
            // hide hint
            mEditText.setHint(null);
            // reset text
            mEditText.setText(null);
            // refresh data
            notifyItemInserted(mChipList.size());
        } else {
            Toast.makeText(mContext, "Can't enter the same email id twice", Toast.LENGTH_SHORT).show();
        }
    }

    public void removeChip(ChipInterface chip) {
        int position = mChipList.indexOf(chip);
        mChipList.remove(position);
        // notify listener
        notifyItemRangeChanged(position, getItemCount());
        // if 0 chip
        if (mChipList.size() == 0)
            mEditText.setHint(mHintLabel);
        // refresh data
        notifyDataSetChanged();
    }

    public void removeChip(int position) {
        ChipInterface chip = mChipList.get(position);
        // remove contact
        mChipList.remove(position);
        // notify listener
        mChipsInput.onChipRemoved(chip, mChipList.size());
        // if 0 chip
        if (mChipList.size() == 0)
            mEditText.setHint(mHintLabel);
        // refresh data
        notifyDataSetChanged();
    }

    public void removeChipById(Object id) {
        for (Iterator<ChipInterface> iter = mChipList.listIterator(); iter.hasNext(); ) {
            ChipInterface chip = iter.next();
            if (chip.getId() != null && chip.getId().equals(id)) {
                // remove chip
                iter.remove();
                // notify listener
                mChipsInput.onChipRemoved(chip, mChipList.size());
            }
        }
        // if 0 chip
        if (mChipList.size() == 0)
            mEditText.setHint(mHintLabel);
        // refresh data
        notifyDataSetChanged();
    }

    public void removeChipByLabel(String label) {
        for (Iterator<ChipInterface> iter = mChipList.listIterator(); iter.hasNext(); ) {
            ChipInterface chip = iter.next();
            if (chip.getLabel().equals(label)) {
                // remove chip
                iter.remove();
                // notify listener
                mChipsInput.onChipRemoved(chip, mChipList.size());
            }
        }
        // if 0 chip
        if (mChipList.size() == 0)
            mEditText.setHint(mHintLabel);
        // refresh data
        notifyDataSetChanged();
    }

    public void removeChipByInfo(String info) {
        for (Iterator<ChipInterface> iter = mChipList.listIterator(); iter.hasNext(); ) {
            ChipInterface chip = iter.next();
            if (chip.getInfo() != null && chip.getInfo().equals(info)) {
                // remove chip
                iter.remove();
                // notify listener
                mChipsInput.onChipRemoved(chip, mChipList.size());
            }
        }
        // if 0 chip
        if (mChipList.size() == 0)
            mEditText.setHint(mHintLabel);
        // refresh data
        notifyDataSetChanged();
    }

    public List<ChipInterface> getChipList() {
        return mChipList;
    }

    private boolean listContains(List<ChipInterface> contactList, ChipInterface chip) {

        if (mChipsInput.getChipValidator() != null) {
            for (ChipInterface item : contactList) {
                if (mChipsInput.getChipValidator().areEquals(item, chip))
                    return true;
            }
        } else {
            for (ChipInterface item : contactList) {
                if (chip.getId() != null && chip.getId().equals(item.getId()))
                    return true;
                if (chip.getLabel().equals(item.getLabel()))
                    return true;
            }
        }

        return false;
    }

    private class ItemViewHolder extends RecyclerView.ViewHolder {

        private final ChipView chipView;

        ItemViewHolder(View view) {
            super(view);
            chipView = (ChipView) view;
        }
    }

    private class EditTextViewHolder extends RecyclerView.ViewHolder {

        private final EditText editText;

        EditTextViewHolder(View view) {
            super(view);
            editText = (EditText) view;
        }
    }
}
