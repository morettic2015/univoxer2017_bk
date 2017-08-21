package com.univoxer.android.login.fragments;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.univoxer.android.R;
import com.univoxer.android.commons.BaseFragment;
import com.univoxer.android.user.User;
import com.univoxer.android.network.RequestManager;

import java.io.File;
import java.io.IOException;

/**
 * Created by Gustavo on 01/06/2016.
 * SignUpFirstPageFragment
 */
public class SignUpFirstPageFragment extends BaseFragment {

    private Button mButton;
    private EditText mName;
    private EditText mEmail;
    private EditText mPassword;
    private EditText mConfirmPassword;

    private Uri mImageCaptureUri;
    private ImageView mImageView;

    private AlertDialog mDialog;

    private static final int PICK_FROM_CAMERA = 1;
    private static final int PICK_FROM_FILE = 2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
//
//        builder.setTitle("Select Image");
//        builder.setPositiveButton("From Camera", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int i) {
//                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                File file = new File(Environment.getExternalStorageDirectory(),
//                        "tmp_avatar_" + String.valueOf(System.currentTimeMillis()) + ".jpg");
//                mImageCaptureUri = Uri.fromFile(file);
//                try {
//                    intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
//                    intent.putExtra("return-data", true);
//                    startActivityForResult(intent, PICK_FROM_CAMERA);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//                dialog.dismiss();
//            }
//        });
//
//        builder.setNegativeButton("From Album", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                Intent intent = new Intent();
//                intent.setType("image/*");
//                intent.setAction(Intent.ACTION_GET_CONTENT);
//                startActivityForResult(Intent.createChooser(intent, "Complete action using"), PICK_FROM_FILE);
//            }
//        });
//
//        mDialog = builder.create();
    }

    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.signup_first_page_fragment, container, false);

        mName = (EditText) view.findViewById(R.id.txt_name);
        mEmail = (EditText) view.findViewById(R.id.txt_email);
        mPassword = (EditText) view.findViewById(R.id.txt_password);
        mConfirmPassword = (EditText) view.findViewById(R.id.txt_confirm_password);

        mImageView = (ImageView) view.findViewById(R.id.img);
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                mDialog.show();
            }
        });

        mButton = (Button) view.findViewById(R.id.btn_next);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(mEmail.getText().toString()).matches()) {
                    Toast.makeText(getActivity(), getResources().getText(R.string.incorrect_email), Toast.LENGTH_LONG).show();
                    return;
                }

                if (mName.getText().toString().equals("")) {
                    Toast.makeText(getActivity(), getResources().getText(R.string.invalid_name), Toast.LENGTH_LONG).show();
                    return;
                }

                if (mPassword.getText().toString().equals(mConfirmPassword.getText().toString()) && !mPassword.getText().toString().isEmpty()) {
                    User.getInstance().setUserData(RequestManager.NAME, mName.getText().toString());
                    User.getInstance().setUserData(RequestManager.EMAIL, mEmail.getText().toString());
                    User.getInstance().setUserData(RequestManager.PASSWORD, mPassword.getText().toString());

                    switchFragment(new SignUpSecondPageFragment(), null, true);
                } else {
                    Toast.makeText(getActivity(), getResources().getText(R.string.password_not_equal), Toast.LENGTH_LONG).show();
                }
            }
        });

        return view;
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (resultCode != Activity.RESULT_OK) return;
//
//        Bitmap bitmap = null;
//        String path = "";
//
//        if (requestCode == PICK_FROM_FILE) {
//            mImageCaptureUri = data.getData();
//            try {
//                bitmap = MediaStore.Images.Media.getBitmap(mContext.getContentResolver(), mImageCaptureUri);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        } else {
//            path = mImageCaptureUri.getPath();
//            bitmap = BitmapFactory.decodeFile(path);
//        }
//
//        mImageView.setImageBitmap(bitmap);
//    }
}
