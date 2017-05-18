package com.example.shaju.letzchat.global_chat.view;

import github.ankushsachdeva.emojicon.EmojiconEditText;
import github.ankushsachdeva.emojicon.EmojiconGridView;
import github.ankushsachdeva.emojicon.EmojiconsPopup;
import github.ankushsachdeva.emojicon.emoji.Emojicon;

import com.example.shaju.letzchat.R;
import com.example.shaju.letzchat.global_chat.model.Chat;
import com.example.shaju.letzchat.global_chat.model.Message;
import com.example.shaju.letzchat.users_page.model.User;
import com.example.shaju.letzchat.users_page.model.Users;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;


public class GlobalChatPageView extends LinearLayout implements GlobalChatPageDisplayer {


    //Emojicons
    private EmojiconsPopup emojiconsPopup;
    private ImageButton emojiconButton;
    //message edit text
    private EmojiconEditText messageEditText;

    //Gallery button - not working
    private ImageButton galleryButton;
    //Message adapter
    private final GlobalChatMessageAdapter globalChatMessageAdapter;

    //send button
    private ImageButton sendButton;

    //Context
    private final Context context;
    private Activity activity;
    //recycler view to show messages list
    private RecyclerView messageRecyclerView;

    //listners for action
    private GlobalChatPageActionListener actionListener;

    public GlobalChatPageView(Context contextIN, AttributeSet attributeSetIN) {
        super(contextIN, attributeSetIN);
        this.context = contextIN;
        setOrientation(VERTICAL);
        globalChatMessageAdapter = new GlobalChatMessageAdapter(LayoutInflater.from(contextIN));
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        //set layout
        View.inflate(getContext(), R.layout.merge_global_view, this);

        View rootView = this.getRootView();
        //emojicons
        emojiconsPopup = new EmojiconsPopup(rootView, getContext());
        emojiconsPopup.setSizeForSoftKeyboard();
        emojiconButton = (ImageButton) this.findViewById(R.id.emoticonButton);
        messageEditText = (EmojiconEditText) this.findViewById(R.id.messageEditText);

        //Buttons
        sendButton = (ImageButton) this.findViewById(R.id.sendButton);
        galleryButton = (ImageButton) findViewById(R.id.imageButton);

        //recycler view for new messages
        messageRecyclerView = (RecyclerView) this.findViewById(R.id.messageRecyclerView);

        //layout
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setStackFromEnd(true);
        messageRecyclerView.setLayoutManager(linearLayoutManager);
        messageRecyclerView.setAdapter(globalChatMessageAdapter);

    }

    //Attach listeners
    @Override
    public void attach(GlobalChatPageActionListener globalChatPageActionListenerIN) {
        this.actionListener = globalChatPageActionListenerIN;
        emojiconsPopup.setOnSoftKeyboardOpenCloseListener(softKeyboardOpenCloseListener);
        emojiconsPopup.setOnEmojiconClickedListener(emojiconClickedListener);
        emojiconsPopup.setOnEmojiconBackspaceClickedListener(emojiconBackspaceClickedListener);
        emojiconsPopup.setOnDismissListener(emojiDismissListener);
        emojiconButton.setOnClickListener(emojiClickListener);
        messageEditText.addTextChangedListener(textWatcher);
        sendButton.setOnClickListener(submitClickListener);
        galleryButton.setOnClickListener(galleryButtonClickListener);

    }

    //remove listeners
    @Override
    public void detach(GlobalChatPageActionListener globalChatPageActionListenerIN) {
        sendButton.setOnClickListener(null);
        emojiconsPopup.setOnSoftKeyboardOpenCloseListener(null);
        emojiconsPopup.setOnEmojiconClickedListener(null);
        emojiconsPopup.setOnEmojiconBackspaceClickedListener(null);
        emojiconsPopup.setOnDismissListener(null);
        emojiconButton.setOnClickListener(null);
        galleryButton.setOnClickListener(null);
        messageEditText.removeTextChangedListener(textWatcher);
        this.actionListener = null;
    }

    private final OnClickListener galleryButtonClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            Toast.makeText(v.getContext(), "SELECT IMAGE", Toast.LENGTH_SHORT).show();
            Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
            photoPickerIntent.setType("image/jpeg");
            photoPickerIntent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
            ((Activity) context).startActivityForResult(photoPickerIntent, 2);
        }
    };

    //On submit button action listener
    private final OnClickListener submitClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
//            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            actionListener.onMessageSend(messageEditText.getText().toString().trim());
            //set text to " "
            messageEditText.setText("");
        }
    };
    //Display
    @Override
    public void display(Chat chatIN, Users usersIN, User userIN) {
        globalChatMessageAdapter.update(chatIN, usersIN, userIN);
        int lastMessagePosition = globalChatMessageAdapter.getItemCount() == 0 ? 0 : globalChatMessageAdapter.getItemCount() - 1;
        messageRecyclerView.smoothScrollToPosition(lastMessagePosition);
    }

    //Add messages to the displayUserDetails
    @Override
    public void addToDisplay(Message messageIN, User senderIN, User userIN) {
        globalChatMessageAdapter.add(messageIN, senderIN, userIN);
        int lastMessagePosition = globalChatMessageAdapter.getItemCount() == 0 ? 0 : globalChatMessageAdapter.getItemCount() - 1;
        messageRecyclerView.smoothScrollToPosition(lastMessagePosition);
    }

    //If message length is > 0 then enable send button interaction
    @Override
    public void enableInteraction()
    {
        sendButton.setEnabled(true);
    }

    //If message length is <= 0 then disable send button interaction
    @Override
    public void disableInteraction()
    {
        sendButton.setEnabled(false);
    }

    private final TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int startIN, int countIN, int afterIN) {
        }

        @Override
        public void onTextChanged(CharSequence s, int startIN, int beforeIN, int countIN) {
        }

        @Override
        public void afterTextChanged(Editable editableString) {
            actionListener.onMessageLengthChanged(editableString.toString().trim().length());
        }
    };


    /**
     * Based on https://github.com/ankushsachdeva/emojicon/blob/master/example/src/com/example/emojiconsample/MainActivity.java
     * Emojicon popup
     */
    private final EmojiconsPopup.OnSoftKeyboardOpenCloseListener softKeyboardOpenCloseListener = new EmojiconsPopup.OnSoftKeyboardOpenCloseListener() {

        @Override
        public void onKeyboardOpen(int keyBoardHeight) {

        }

        @Override
        public void onKeyboardClose() {
            if(emojiconsPopup.isShowing())
                emojiconsPopup.dismiss();
        }
    };

    /**
     * Based on https://github.com/ankushsachdeva/emojicon/blob/master/example/src/com/example/emojiconsample/MainActivity.java
     * Emojicon popup
     */
    private final EmojiconGridView.OnEmojiconClickedListener emojiconClickedListener = new EmojiconGridView.OnEmojiconClickedListener() {

        @Override
        public void onEmojiconClicked(Emojicon emojicon) {
            if (messageEditText == null || emojicon == null) {
                return;
            }

            int start = messageEditText.getSelectionStart();
            int end = messageEditText.getSelectionEnd();
            if (start < 0) {
                messageEditText.append(emojicon.getEmoji());
            } else {
                messageEditText.getText().replace(Math.min(start, end),
                        Math.max(start, end), emojicon.getEmoji(), 0,
                        emojicon.getEmoji().length());
            }
        }
    };

    /**
     * Based on https://github.com/ankushsachdeva/emojicon/blob/master/example/src/com/example/emojiconsample/MainActivity.java
     * Emojicon popup
     */
    private final EmojiconsPopup.OnEmojiconBackspaceClickedListener emojiconBackspaceClickedListener = new EmojiconsPopup.OnEmojiconBackspaceClickedListener() {

        @Override
        public void onEmojiconBackspaceClicked(View v) {
            KeyEvent event = new KeyEvent(
                    0, 0, 0, KeyEvent.KEYCODE_DEL, 0, 0, 0, 0, KeyEvent.KEYCODE_ENDCALL);
            messageEditText.dispatchKeyEvent(event);
        }
    };

    /**
     * Based on https://github.com/ankushsachdeva/emojicon/blob/master/example/src/com/example/emojiconsample/MainActivity.java
     * Emojicon popup
     */
    private final OnClickListener emojiClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {

            if(!emojiconsPopup.isShowing()){

                if(emojiconsPopup.isKeyBoardOpen())
                {
                    emojiconsPopup.showAtBottom();
                    changeEmojiKeyboardIcon(emojiconButton, R.drawable.type_keyboard_sign);
                }

                else
                {
                    messageEditText.setFocusableInTouchMode(true);
                    messageEditText.requestFocus();
                    emojiconsPopup.showAtBottomPending();
                    final InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.showSoftInput(messageEditText, InputMethodManager.SHOW_IMPLICIT);
                    changeEmojiKeyboardIcon(emojiconButton, R.drawable.type_keyboard_sign);
                }
            }

            else
            {
                emojiconsPopup.dismiss();
            }
        }
    };

    /**
     * Based on https://github.com/ankushsachdeva/emojicon/blob/master/example/src/com/example/emojiconsample/MainActivity.java
     * Emojicon popup
     */
    private final PopupWindow.OnDismissListener emojiDismissListener = new PopupWindow.OnDismissListener()
    {

        @Override
        public void onDismiss()
        {
            changeEmojiKeyboardIcon(emojiconButton, R.drawable.emoticon_sign);
        }
    };

    private void changeEmojiKeyboardIcon(ImageView iconToBeChangedIN, int drawableResourceIdIN){
        iconToBeChangedIN.setImageResource(drawableResourceIdIN);
    }

}


