package com.example.firebasechat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {
    private ListView listView;
    private AdapterMessage adapterMessage;
    private ProgressBar bar;
    private ImageButton imageButton;
    private Button sendBtn;
    private EditText editText;
    private List<Message> list;
    private static final int RQ_CODE = 111;
    private String recipientUserId;
    private FirebaseStorage storage;
    private StorageReference imageStore;

    private String recipientUserName;

    private String userName;

    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    //слежка за изменениями в узле "message"
    private ChildEventListener messageChildEventListener;

    private DatabaseReference userReference;
    private ChildEventListener usersChildEventListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        auth = FirebaseAuth.getInstance();

        Intent intent = getIntent();
        if (intent != null) {
            userName = intent.getStringExtra("userName");
            recipientUserId = intent.getStringExtra("recipientUserId");
            recipientUserName = intent.getStringExtra("recipientUserName");
        }

        setTitle("Chat with " + recipientUserName);

//        } else {
//            userName = "Default User";
//        }


        // доступ к базе данных
        database = FirebaseDatabase.getInstance();
        //узел в котором хранятся все сообщения
        myRef = database.getReference().child("message");
        //узел в котором хранятся все юзеры
        userReference = database.getReference().child("Users");
        // для хранения картинок в firebase
        storage = FirebaseStorage.getInstance();
        imageStore = storage.getReference().child("images");


        list = new ArrayList<>();
        listView = findViewById(R.id.messageListView);
        adapterMessage = new AdapterMessage(this, R.layout.message_item, list);
        listView.setAdapter(adapterMessage);
        bar = findViewById(R.id.progressBar);
        imageButton = findViewById(R.id.sendImageButton);
        sendBtn = findViewById(R.id.sendMessageButton);
        editText = findViewById(R.id.messageEditText);

        bar.setVisibility(View.INVISIBLE);
        // нельзя отправить пустое сообщение
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (s.toString().trim().length() > 0) {
                    sendBtn.setEnabled(true);
                } else {
                    sendBtn.setEnabled(false);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Получение контента
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                //тип получаемого контента
                intent.setType("image/*");
                //брать изображения только с локального хранилища
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(Intent.createChooser(intent, "Choose the image"), RQ_CODE);
            }
        });
        // установка максимального количества символов в сообщении
        editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(500)});

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message message = new Message();
                message.setText(editText.getText().toString());
                message.setName(userName);
                message.setSender(auth.getCurrentUser().getUid());
                message.setRecipient(recipientUserId);
                message.setIamgeUrl(null);
                //запись id в firebase
                myRef.push().setValue(message);
                editText.setText("");

            }
        });

        usersChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                User user = snapshot.getValue(User.class);
                if (user.getId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                    userName = user.getName();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        userReference.addChildEventListener(usersChildEventListener);


        messageChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Message message = snapshot.getValue(Message.class);

                if (message.getSender().equals(auth.getCurrentUser().getUid()) &&
                        message.getRecipient().equals(recipientUserId)) {
                    message.setMine(true);
                    adapterMessage.add(message);
                } else if (message.getRecipient().equals(auth.getCurrentUser().getUid())
                        && message.getSender().equals(recipientUserId)) {
                    message.setMine(false);
                    adapterMessage.add(message);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        myRef.addChildEventListener(messageChildEventListener);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sign_out:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(ChatActivity.this, SignInActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RQ_CODE && resultCode == RESULT_OK) {
            Uri selectedImage = data.getData();
            // установка картинки в Storage
            final StorageReference ImgREf = imageStore.child(selectedImage.getLastPathSegment());
            //content://images/some_folder/3<-

            UploadTask uploadTask = ImgREf.putFile(selectedImage);


            uploadTask = ImgREf.putFile(selectedImage);

            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    // Continue with the task to get the download URL
                    return ImgREf.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        Message message = new Message();
                        message.setIamgeUrl(downloadUri.toString());
                        message.setSender(auth.getCurrentUser().getUid());
                        message.setRecipient(recipientUserId);
                        message.setName(userName);
                        myRef.push().setValue(message);
                    } else {
                        // Handle failures
                        // ...
                    }
                }
            });
        }
    }
}