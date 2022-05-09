package com.akash.noteoncloud;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;


import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.common.base.internal.Finalizer;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class notesActivity extends AppCompatActivity {

    FloatingActionButton mcreatenotesfab;
    private FirebaseAuth firebaseAuth;


    RecyclerView mrecyclerview;
    StaggeredGridLayoutManager staggeredGridLayoutManager;

    FirebaseUser firebaseUser;
    FirebaseFirestore firebaseFirestore;

    FirestoreRecyclerAdapter<firebasemodel, NoteViewHolder> noteAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //for vibration
        final Vibrator vibe = (Vibrator) notesActivity.this.getSystemService(Context.VIBRATOR_SERVICE);
        // vibe.vibrate(80);

        //
       // overridePendingTransition(R.anim.fadein, R.anim.fadeout);


        setContentView(R.layout.activity_notes);
        firebaseAuth = FirebaseAuth.getInstance();
        mcreatenotesfab = findViewById(R.id.createnotefab);
        getSupportActionBar().setTitle("All Notes");

        //status bar color code
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.parseColor("#111B34"));

    //#0D1528

        //to remove status bar
      //  getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#111B34")));
        getSupportActionBar().setTitle(Html.fromHtml("<font color=\"#FCA311\">" + getString(R.string.All_Notes) + "</font>"));

        //changing the color of navigation bar
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.darknavybackground));
        }



        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();

        mcreatenotesfab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vibe.vibrate(10);
                startActivity(new Intent(notesActivity.this, createnote.class));


            }
        });



    Query query = firebaseFirestore.collection("Users").document(firebaseUser.getUid()).collection("AllNotes").orderBy("title", Query.Direction.ASCENDING);
    FirestoreRecyclerOptions<firebasemodel> allusernotes = new FirestoreRecyclerOptions.Builder<firebasemodel>().setQuery(query, firebasemodel.class).build();
    noteAdapter=new FirestoreRecyclerAdapter<firebasemodel, NoteViewHolder>(allusernotes)

    {
        @Override
        protected void onBindViewHolder (@NonNull NoteViewHolder noteViewHolder,int i, @NonNull firebasemodel firebasemodel)
        {

            ImageView popupbutton =noteViewHolder.itemView.findViewById(R.id.menupopbutton);

            noteViewHolder.notetitle.setText(firebasemodel.getTitle());
            noteViewHolder.notecontent.setText(firebasemodel.getContent());

            String docId=noteAdapter.getSnapshots().getSnapshot(i).getId();

            noteViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent=new Intent(view.getContext(), notedetails.class);

                    intent.putExtra("title",firebasemodel.getTitle());
                    intent.putExtra("content",firebasemodel.getContent());
                    intent.putExtra("noteId",docId);

                    view.getContext().startActivity(intent);
                }
            });

            popupbutton.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void onClick(View view) {
                    PopupMenu popupMenu=new PopupMenu(view.getContext(),view);
                   // popupMenu.setGravity(Gravity,END);
                    popupMenu.getMenu().add("Edit").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            Intent intent=new Intent(view.getContext(), editnoteactivity.class);

                            intent.putExtra("title",firebasemodel.getTitle());
                            intent.putExtra("content",firebasemodel.getContent());
                            intent.putExtra("noteId",docId);

                            view.getContext().startActivity(intent);
                            return false;
                        }
                    });

                    popupMenu.getMenu().add("Delete").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {

                            DocumentReference documentReference=firebaseFirestore.collection("Users").document(firebaseUser.getUid()).collection("AllNotes").document(docId);
                            documentReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(getApplicationContext(), "Deleted Successfully", Toast.LENGTH_SHORT).show();

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                    Toast.makeText(getApplicationContext(), "Failed to Delete", Toast.LENGTH_SHORT).show();


                                }
                            });

                            return false;
                        }
                    });

                    popupMenu.show();

                }
            });


        }



        @NonNull
        @Override
        public NoteViewHolder onCreateViewHolder (@NonNull ViewGroup parent, int viewType)
        {
          View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notes_layout,parent,false);
          return new NoteViewHolder(view);
       }
    };

    mrecyclerview=findViewById(R.id.recyclerview);
    mrecyclerview.setHasFixedSize(true);
    staggeredGridLayoutManager=new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
    mrecyclerview.setLayoutManager(staggeredGridLayoutManager);
    mrecyclerview.setAdapter(noteAdapter);

}
    public class NoteViewHolder extends  RecyclerView.ViewHolder
    { private TextView notetitle;
        private TextView notecontent;
        LinearLayout mnote;
        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            notetitle=itemView.findViewById(R.id.notetitle);
            notecontent=itemView.findViewById(R.id.notecontent);
            mnote=itemView.findViewById(R.id.note);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);

        return true;
    }




    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {


        switch(item.getItemId())
        {
            case R.id.logout:
            firebaseAuth.signOut();
            finish();
            startActivity(new Intent(notesActivity.this, MainActivity.class));

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected  void onStart(){
        super.onStart();
        noteAdapter.startListening();
    }

    @Override
    protected void onStop(){
        super.onStop();
        if(noteAdapter!=null)
        {
            noteAdapter.stopListening();
        }
    }


//this code is used to exit the app
    public void onBackPressed() {
        // TODO Auto-generated method stub
        moveTaskToBack(true);


        super.onBackPressed();
    }



}