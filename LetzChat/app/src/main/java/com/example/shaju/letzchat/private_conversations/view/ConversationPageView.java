package com.example.shaju.letzchat.private_conversations.view;

import de.hdodenhof.circleimageview.CircleImageView;
import github.ankushsachdeva.emojicon.EmojiconEditText;
import github.ankushsachdeva.emojicon.EmojiconGridView;
import github.ankushsachdeva.emojicon.EmojiconsPopup;
import github.ankushsachdeva.emojicon.emoji.Emojicon;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.support.v7.widget.LinearLayoutManager;

import android.util.AttributeSet;

import android.view.View;
import android.view.KeyEvent;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.view.LayoutInflater;
import android.widget.PopupWindow;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.shaju.letzchat.ChatDependencies;
import com.example.shaju.letzchat.R;
import com.example.shaju.letzchat.image_storage.FirebaseImageLoaderService;
import com.example.shaju.letzchat.private_conversations.model.Chat;
import com.example.shaju.letzchat.private_conversations.model.Message;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;


public class ConversationPageView extends LinearLayout implements ConversationPageDisplayer {

    //PHOTO_PICKER
    public static final int RC_PHOTO_PICKER = 2;

    public static final int DEFAULT_MSG_LENGTH_LIMIT = 1000;


    // Firebase instance variables
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mMessagesDatabaseReference;
    private ChildEventListener mChildEventListener;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private FirebaseStorage mFirebaseStorage;
    private static StorageReference mChatPhotosStorageReference;
    private FirebaseRemoteConfig mFirebaseRemoteConfig;

    //Top main_toolbar
    private Toolbar toolbar;
    //Profile image
    private CircleImageView profileImageView;
    //name text view
    private TextView nameTextView;
    //Last seen text view
    private TextView lastSeenTextView;
    //
    private ImageButton imageButton;

    //Message adapter class
    private final ConversationMessageAdapter messageAdapter;
    private EmojiconEditText messageEditText;
    private ImageButton sendButton;
    private RecyclerView messageRecyclerView;

    //Emojicon emojiconsPopup
    private EmojiconsPopup emojiconsPopup;
    //Emojicon button
    private ImageButton emojiconButton;

    private Context context;


    //Typing text view only if end user is typing
    private TextView typingTextView;

    private ConversationListener actionListener;

    public ConversationPageView(Context contextIN, AttributeSet attrs) {
        super(contextIN, attrs);
        context = contextIN;
        setOrientation(VERTICAL);
        messageAdapter = new ConversationMessageAdapter(LayoutInflater.from(contextIN));

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseStorage = FirebaseStorage.getInstance();
        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        mMessagesDatabaseReference = mFirebaseDatabase.getReference().child("messages");
        mChatPhotosStorageReference = mFirebaseStorage.getReference().child("chat_photos");
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        View.inflate(getContext(), R.layout.conversation_merged, this);

        View rootView = this.getRootView();


        //Initialise with id in xml
        messageEditText = (EmojiconEditText) this.findViewById(R.id.messageEditText);
        sendButton = (ImageButton) this.findViewById(R.id.sendButton);
        emojiconButton = (ImageButton) this.findViewById(R.id.emoticonButton);

        toolbar = (Toolbar) this.findViewById(R.id.toolbar);
        profileImageView = (CircleImageView) toolbar.findViewById(R.id.profileImageView);
        nameTextView = (TextView) toolbar.findViewById(R.id.nameTextView);
        lastSeenTextView = (TextView) toolbar.findViewById(R.id.lastSeenTextView);
        typingTextView = (TextView) this.findViewById(R.id.typingTextView);
        toolbar.setNavigationIcon(R.drawable.arrow_back);
        imageButton = (ImageButton) this.findViewById(R.id.imageButton);

        emojiconsPopup = new EmojiconsPopup(rootView, getContext());
        emojiconsPopup.setSizeForSoftKeyboard();

        messageRecyclerView = (RecyclerView) this.findViewById(R.id.messageRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setStackFromEnd(true);
        messageRecyclerView.setLayoutManager(layoutManager);
        messageRecyclerView.setAdapter(messageAdapter);

    }



    @Override
    public void addToDisplay(Message message, String self) {
        messageAdapter.add(message,self);
        int lastMessagePosition = messageAdapter.getItemCount() == 0 ? 0 : messageAdapter.getItemCount() - 1;
        messageRecyclerView.scrollToPosition(lastMessagePosition);
    }

    @Override
    public void display(Chat chat, String self)
    {
        messageAdapter.update(chat, self);
        int lastMessagePosition = messageAdapter.getItemCount() == 0 ? 0 : messageAdapter.getItemCount() - 1;
        messageRecyclerView.scrollToPosition(lastMessagePosition);
    }

    //To set main_toolbar image, name, last seen
    @Override
    public void setupToolbar(String user, String image, long lastSeen) {

        loadImageElseWhite(image,profileImageView,getContext());

        nameTextView.setText(user);
        if (lastSeen == 0)
        {
            lastSeenTextView.setText("Online");
            lastSeenTextView.setTextColor(this.getResources().getColor(R.color.colorGreen));

        }
        else
        {
            Date lastSeenDate = new Date(lastSeen);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd/HH/mm");
            String timestamp = sdf.format(lastSeenDate);
            String today = getCurrentTimestamp();
            //Times
            String[] time1 = timestamp.split("/");
            String[] time2 = today.split("/");

            if ((time1[0]+time1[1]+time1[2]).equals(time2[0]+time2[1]+time2[2]))
            {
                //String to show user
                String s = "Last seen today at time";
                lastSeenTextView.setTextColor(this.getResources().getColor(R.color.colorText));
                lastSeenTextView.setText(s.replace("time",time1[3] + ":" + time1[4]));
            }
            else
            {
                String s = getResources().getString(R.string.chat_toolbar_lastseen_offline);
                s = s.replace("date",time1[2] + "/" + time1[1]);
                s = s.replace("time",time1[3] + ":" + time1[4]);
                lastSeenTextView.setTextColor(this.getResources().getColor(R.color.colorText));
                lastSeenTextView.setText(s);

            }
        }

        typingTextView.setText(user + " " + getResources().getString(R.string.chat_textview_typing));
    }

    @Override
    public void hideTyping() {
        typingTextView.setVisibility(INVISIBLE);
    }

    @Override
    public void showTyping() {
        typingTextView.setVisibility(VISIBLE);
    }



    //Click listners
    @Override
    public void attach(ConversationListener conversationInteractionListener) {
        this.actionListener = conversationInteractionListener;

        toolbar.setNavigationOnClickListener(navigationClickListener);
        emojiconsPopup.setOnSoftKeyboardOpenCloseListener(softKeyboardOpenCloseListener);
        emojiconsPopup.setOnEmojiconClickedListener(emojiconClickedListener);
        emojiconsPopup.setOnEmojiconBackspaceClickedListener(emojiconBackspaceClickedListener);
        emojiconsPopup.setOnDismissListener(emojiDismissListener);
        emojiconButton.setOnClickListener(emojiClickListener);

        imageButton.setOnClickListener(imageButtonClickListener);

        messageEditText.addTextChangedListener(textWatcher);
        sendButton.setOnClickListener(sendButtonClickListener);
    }

    @Override
    public void detach(ConversationListener conversationInteractionListener) {

        toolbar.setOnMenuItemClickListener(null);
        emojiconsPopup.setOnSoftKeyboardOpenCloseListener(null);
        emojiconsPopup.setOnEmojiconClickedListener(null);
        emojiconsPopup.setOnEmojiconBackspaceClickedListener(null);
        emojiconsPopup.setOnDismissListener(null);
        emojiconButton.setOnClickListener(null);

        sendButton.setOnClickListener(null);
        messageEditText.removeTextChangedListener(textWatcher);

        this.actionListener = null;
    }



    private final TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            actionListener.onMessageLengthChanged(s.toString().trim().length());
        }
    };

    @Override
    public void enableInteraction()
    {
        sendButton.setEnabled(true);
    }

    @Override
    public void disableInteraction()
    {
        sendButton.setEnabled(false);
    }



    private final OnClickListener navigationClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            actionListener.onUpPressed();
        }
    };

    private final OnClickListener sendButtonClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            actionListener.onSubmitMessage(messageEditText.getText().toString().trim());
            messageEditText.setText("");
        }
    };

    //Github from bonfire
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

    private final OnClickListener imageButtonClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            Toast.makeText(v.getContext(), "SELECT IMAGE", Toast.LENGTH_SHORT).show();
            Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
            photoPickerIntent.setType("image/jpeg");
            photoPickerIntent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
            ((Activity) context).startActivityForResult(photoPickerIntent, 2);
            //Intent photoIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            //context.startActivityForResult(photoIntent, 0);
            //startActivityForResult(Intent.createChooser(photoPickerIntent, "Pick a picture using"), RC_PHOTO_PICKER);
        }
    };

    //Github bonfire
    private final EmojiconsPopup.OnEmojiconBackspaceClickedListener emojiconBackspaceClickedListener = new EmojiconsPopup.OnEmojiconBackspaceClickedListener() {

        @Override
        public void onEmojiconBackspaceClicked(View v) {
            KeyEvent event = new KeyEvent(
                    0, 0, 0, KeyEvent.KEYCODE_DEL, 0, 0, 0, 0, KeyEvent.KEYCODE_ENDCALL);
            messageEditText.dispatchKeyEvent(event);
        }
    };
    //Github bonfire

    private final OnClickListener emojiClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {

            if(!emojiconsPopup.isShowing()){
                if(emojiconsPopup.isKeyBoardOpen()){
                    emojiconsPopup.showAtBottom();
                    changeEmojiKeyboardIcon(emojiconButton, R.drawable.type_keyboard_sign);
                }

                else{
                    messageEditText.setFocusableInTouchMode(true);
                    messageEditText.requestFocus();
                    emojiconsPopup.showAtBottomPending();
                    final InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.showSoftInput(messageEditText, InputMethodManager.SHOW_IMPLICIT);
                    changeEmojiKeyboardIcon(emojiconButton, R.drawable.type_keyboard_sign);
                }
            } else{
                emojiconsPopup.dismiss();
            }
        }
    };

    //Github bonfire

    private final PopupWindow.OnDismissListener emojiDismissListener = new PopupWindow.OnDismissListener() {

        @Override
        public void onDismiss() {
            changeEmojiKeyboardIcon(emojiconButton, R.drawable.emoticon_sign);
        }
    };

    //Github bonfire

    private void changeEmojiKeyboardIcon(ImageView iconToBeChanged, int drawableResourceId){
        iconToBeChanged.setImageResource(drawableResourceId);
    }



    //Get current time. Used when sending the messages to upload the time the message was sent
    public static String getCurrentTimestamp() {
        SimpleDateFormat dateFormatGmt = new SimpleDateFormat("yyyy/MM/dd/HH/mm/ss");
        dateFormatGmt.setTimeZone(TimeZone.getTimeZone("GMT"));
        return dateFormatGmt.format(new Date());
    }


//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == RC_PHOTO_PICKER) {
//            Uri selectedImageUri = data.getData();
//
//            // Get a reference to store file at chat_photos/<FILENAME>
//            StorageReference photoRef = mChatPhotosStorageReference.child(selectedImageUri.getLastPathSegment());
//
//            // Upload file to Firebase Storage
//            photoRef.putFile(selectedImageUri)
//                    .addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                        @Override
//                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                            // When the image has successfully uploaded, we get its download URL
//                            Uri downloadUrl = taskSnapshot.getDownloadUrl();
//
//                            // Set the download URL to the message box, so that the user can send it to the database
//                            Message friendlyMessage = new Message(null, mUsername, downloadUrl.toString());
//                            mMessagesDatabaseReference.push().setValue(friendlyMessage);
//
//                        }
//                    });
//        }
//    }


    public static void loadImageElseWhite(String imageIN, CircleImageView circleImageViewIN, Context contextIN) {

        try {
            if (imageIN != null && imageIN.length() > 0) {
                StorageReference ref = ChatDependencies.INSTANCE.getStorageService().getProfileImageReferenceFromStorage(imageIN);
                Glide.with(contextIN)
                        .using(new FirebaseImageLoaderService())
                        .load(ref)
                        .into(circleImageViewIN);
            } else
            {
                Glide.with(contextIN)
                        .load("")
                        .placeholder(R.drawable.person_circlewhite)
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .into(circleImageViewIN);
            }
        } catch (IllegalArgumentException e) {

        }

    }

}
