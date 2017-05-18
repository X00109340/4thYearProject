package com.example.shaju.letzchat.navigations;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.example.shaju.letzchat.R;
import com.example.shaju.letzchat.users_page.model.User;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.widget.TextView;
import android.widget.Toast;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;

import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Created by Shajun on 17/03/2017.
 */

public class AndroidProfileNavigator implements ProfileNavigator {

    //Profile listener
    private ProfileDialogListener dialogListener;

    //App compact appCompatActivity
    private final AppCompatActivity appCompatActivity;

    //COnstructor
    public AndroidProfileNavigator(AppCompatActivity appCompatActivityIN) {
        appCompatActivity = appCompatActivityIN;
    }

    //photo picker
    @Override
    public void showImagePicker() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        appCompatActivity.startActivityForResult(photoPickerIntent, 1);
    }


    //Dialog for showing user to enter new online ID (Full Name)
    @Override
    public void showInputTextDialog(String hintIN, final TextView textViewIN, final User userIN)
    {
        //Dialog
        final MaterialDialog dialog = new MaterialDialog.Builder(appCompatActivity)
                .title("Type Your New Online ID")
                .inputType(InputType.TYPE_CLASS_TEXT)
                .input(appCompatActivity.getString(R.string.profile_hint_name), textViewIN.getText().toString(), new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(MaterialDialog dialog, CharSequence input) {
                        // Do something
                    }
                })
                .negativeText("Close")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        String inputText = dialog.getInputEditText().getText().toString();
                        if (inputText.length() == 0) {
                            Toast.makeText(appCompatActivity,"Name cannot be left empty",Toast.LENGTH_LONG).show();
                            return;
                        }
                        dialogListener.onNameSelected(inputText, userIN);
                        textViewIN.setText(inputText);
                        dialog.dismiss();
                    }
                })
                .show();
        //showing dialog now
    }

    //Dialog for new password
    @Override
    public void showInputPasswordDialog(String hintIN, User userIN) {
        MaterialDialog dialog = new MaterialDialog.Builder(appCompatActivity)
                .title("Type Your New Password")
                .inputType(InputType.TYPE_TEXT_VARIATION_PASSWORD)
                .input(null, null, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(MaterialDialog dialog, CharSequence input) {
                    }
                })
                .negativeText("Close")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        String password = dialog.getInputEditText().getText().toString();
                        if (password.length() < 8) {
                            Toast.makeText(appCompatActivity,"Password cannot be empty",Toast.LENGTH_LONG).show();
                            return;
                        }
                        dialogListener.onPasswordSelected(password);
                        dialog.dismiss();
                    }
                })
                .show();
    }



    @Override
    public void attach(ProfileDialogListener profileDialogListenerIN) {
        this.dialogListener = profileDialogListenerIN;
    }

    //Profile dialog listener
    @Override
    public void detach(ProfileDialogListener profileDialogListenerIN) {
        this.dialogListener = null;
    }

    //Go to parent activity
    @Override
    public void toParent() {
        appCompatActivity.finish();
    }

    public boolean onActivityResult(int requestCodeIN, int resultCodeIN, Intent intentIN) {
        //If choose picture = 1
        if (requestCodeIN != 1) {
            return false;
        }

        if (intentIN == null) {
            return false;
        }

        try {
            final Uri imageUri = intentIN.getData();
            final InputStream imageImageStream = appCompatActivity.getContentResolver().openInputStream(imageUri);
            final Bitmap selectedImageBitmap = BitmapFactory.decodeStream(imageImageStream);
            dialogListener.onImageSelected(selectedImageBitmap);
        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

        return true;
    }

    @Override
    public void toLogin() {
        //Not used

    }

    @Override
    public void toMainActivity() {
        //Not Used
    }

}
