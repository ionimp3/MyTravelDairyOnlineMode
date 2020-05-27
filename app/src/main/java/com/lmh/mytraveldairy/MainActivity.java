package com.lmh.mytraveldairy;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import CommonFolder.BackPressHandler;
import DataView.InfoTBmodel;
import DataView.InfoTBviewHolder;
import MyProgressDialog.MyProgressDialogActivity;
import OnBoarding.OnBoardingActivity;
import PlanFolder.ClickLongDetailPlanActivity;
import PlanFolder.ClickPlanActivity;
import PlanFolder.NewPlanActivity;
import ProfileFolder.CoverImageActivity;
import ProfileFolder.NicNameActivity;
import ProfileFolder.ProfileActivity;
import ProfileFolder.ProfileImageActivity;
import SignFolder.SignInActivity;
import de.hdodenhof.circleimageview.CircleImageView;


public class MainActivity extends AppCompatActivity {

    private BackPressHandler backPressHandler = new BackPressHandler(this);
    private long backKeyPressedTime = 0;

    MyProgressDialogActivity myProgressDialogActivity;

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference, referenceroot;
    private FirebaseUser currentUser;

    //초기프로파일헤더 업데이트
    private DatabaseReference UserRef;
    private String UserTB_nicName_fromdb, UserTB_coverImage_fromdb, UserTB_profileImage_fromdb, UserTB_coverImageKey_fromdb, UserTB_profileImageKey_fromdb;
    private int i = 0;

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    //네비게이션헤더
    private CircleImageView headerImage;
    private ImageView headerCoverImage;
    private TextView headerNicName, headerEmailId;

    //리사이클러뷰
    DatabaseReference ref;
    private FirebaseRecyclerOptions<InfoTBmodel> options;
    private FirebaseRecyclerAdapter<InfoTBmodel, InfoTBviewHolder> adapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //FIREBASE 초기화
        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();

        drawerLayout = findViewById(R.id.drawer_main);
        navigationView = findViewById(R.id.nav_view);
        View nevView = navigationView.inflateHeaderView(R.layout.menu_header);

        //네비 드로어 헤더는 navigationView.getHeaderView(0) 를 써야함.
        headerImage = (CircleImageView) navigationView.getHeaderView(0).findViewById(R.id.circleimage_header_profile);
        headerNicName = (TextView) navigationView.getHeaderView(0).findViewById(R.id.text_header_nicname);
        headerEmailId = (TextView) navigationView.getHeaderView(0).findViewById(R.id.text_header_emailid);
        headerCoverImage = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.image_header_cover_bg);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Home");

        String currentUserId = firebaseAuth.getCurrentUser().getUid();
        ref = FirebaseDatabase.getInstance().getReference().child(currentUserId).child("InfoTB");
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                UserMenuSelector(menuItem);
                return false;
            }
        });

        options = new FirebaseRecyclerOptions.Builder<InfoTBmodel>().setQuery(ref, InfoTBmodel.class).build();
        adapter = new FirebaseRecyclerAdapter<InfoTBmodel, InfoTBviewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull InfoTBviewHolder holder, int position, @NonNull InfoTBmodel model) {
                final String planKey = getRef(position).getKey();
                holder.a_rv_tag.setText(model.getInfoTB_planTags());
                holder.a_rv_title_name.setText(model.getInfoTB_planName());
                holder.a_rv_depart_date.setText(model.getInfoTB_planDepartDate());
                holder.a_rv_end_date.setText(model.getInfoTB_planEndDate());
                Picasso.get().load(model.getInfoTB_titleImage()).into(holder.a_image_rv_bg);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent clickPlanIntent = new Intent(MainActivity.this, ClickPlanActivity.class);
                        clickPlanIntent.putExtra("planKeyFromMain", planKey);
                        startActivity(clickPlanIntent);
                    }
                });
                // 롱클릭시 세부내용으로 이동 하는 리스너 추가
                holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {

                        Intent clickLongDetailPlanIntent = new Intent(MainActivity.this, ClickLongDetailPlanActivity.class);
                        clickLongDetailPlanIntent.putExtra("planKeyFromMain", planKey);
                        startActivity(clickLongDetailPlanIntent);
                        return false;
                    }
                });
                //adapter.notifyDataSetChanged();
            }

            @NonNull
            @Override
            public InfoTBviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_plan_list_layout, parent, false);
                return new InfoTBviewHolder(view);
            }
        };

        //adapter.startListening();
        recyclerView.setAdapter(adapter);
    }

    protected void onStart() {
        super.onStart();
        adapter.startListening();
        currentUser = firebaseAuth.getCurrentUser();
        if (currentUser == null) {
            Intent profileIntent = new Intent(MainActivity.this, OnBoardingActivity.class);
            profileIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(profileIntent);
            finish();
        } else {
            CheckUserExistance();
        }
    }
    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    private void CheckUserExistance() {

        final String current_User_Id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference RealUserCheck = FirebaseDatabase.getInstance().getReference();
        myProgressDialogActivity = new MyProgressDialogActivity(this);
        myProgressDialogActivity.show();
        RealUserCheck.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //실제 데이터베이스를 조회해서 이메일 id 가 있는지 확인
                if (!dataSnapshot.hasChild(current_User_Id)) {
                    myProgressDialogActivity.dismiss();
                    Intent profileIntent = new Intent(MainActivity.this, OnBoardingActivity.class);
                    profileIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(profileIntent);
                    finish();
                } else {
                    //MessageToast.message(UpdateNicName.this,"실제접속했어요");
                    isUserCallData();
                    myProgressDialogActivity.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void isUserCallData() {
        final String current_User_Id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(current_User_Id + "/UserTB");
        Query checkSeleMailPk = reference.orderByChild("UserTB_Id");
        checkSeleMailPk.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    //userDashboard 코드보고, 컬럼이 미존재시 오류 처리 할것,,안하고 컬럼 미존재시 앱크래시 남
                    UserTB_nicName_fromdb = dataSnapshot.child("UserTB_nicName").getValue(String.class);
                    UserTB_coverImage_fromdb = dataSnapshot.child("UserTB_coverImage").getValue(String.class);
                    UserTB_profileImage_fromdb = dataSnapshot.child("UserTB_profileImage").getValue(String.class);
                    UserTB_coverImageKey_fromdb = dataSnapshot.child("UserTB_coverImageKey").getValue(String.class);
                    UserTB_profileImageKey_fromdb = dataSnapshot.child("UserTB_profileImageKey").getValue(String.class);

                    String user = FirebaseAuth.getInstance().getCurrentUser().getEmail();
                    headerNicName.setText(UserTB_nicName_fromdb);
                    headerEmailId.setText(user);

                    profileImageShow();
                    coverImageShow();

                } else {
                    //
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void profileImageShow() {
        if (UserTB_profileImage_fromdb.trim().equals("NA")) {
            headerImage.setImageResource(R.drawable.ic_account_circle_black);
            i = 1;
        } else if (UserTB_profileImage_fromdb.trim().length() > 20 && i == 0) {
            Picasso.get().load(UserTB_profileImage_fromdb).into(headerImage);
            i = 5;
            //Toast.makeText(this,"정보있음",Toast.LENGTH_SHORT).show();
        } else {
            String currentUser = firebaseAuth.getCurrentUser().getUid();
            UserRef = FirebaseDatabase.getInstance().getReference().child(currentUser).child("UserTB");
            UserRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String image = dataSnapshot.child("UserTB_profileImage").getValue().toString();
                        //Picasso.get().load("http://i.imgur.com/DvpvklR.png").into(imageView);
                        Picasso.get().load(image).into(headerImage);
                        i = 5;
                    } else {
                        //
                        //Log.d("로그유저2", "불러오기 실패 ");
                        i = 0;
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
        }
    }

    private void coverImageShow() {
        String currentUser = firebaseAuth.getCurrentUser().getUid();
        UserRef = FirebaseDatabase.getInstance().getReference().child(currentUser).child("UserTB");
        if (UserTB_coverImage_fromdb.trim().equals("NA")) {
            headerCoverImage.setImageResource(R.drawable.bg_drawer_header);
            i = 1;
        } else if (UserTB_coverImage_fromdb.trim().length() > 20 && i == 0) {
            Picasso.get().load(UserTB_coverImage_fromdb).into(headerCoverImage);
            i = 5;
            //Toast.makeText(this,"정보있음",Toast.LENGTH_SHORT).show();
        } else {
            UserRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String image = dataSnapshot.child("UserTB_coverImage").getValue().toString();
                        //Picasso.get().load("http://i.imgur.com/DvpvklR.png").into(imageView);
                        Picasso.get().load(image).into(headerCoverImage);
                        i = 5;

                    } else {
                        // Log.d("로그유저4", "불러오기 실패 ");
                        i = 0;
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
        }
    }

    private void UserMenuSelector(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_btn_home:
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.nav_setting:
                Intent ProfileIntent = new Intent(this, ProfileActivity.class);
                ProfileIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(ProfileIntent);
                finish();
                break;
            case R.id.nav_add:
                Intent NewPlanIntent = new Intent(this, NewPlanActivity.class);
                NewPlanIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(NewPlanIntent);
                break;
            case R.id.nav_sign_out:
                firebaseAuth.signOut();
                Toast.makeText(MainActivity.this
                        , "로그아웃 하였읍니다.", Toast.LENGTH_SHORT).show();
                Intent logoutIntent = new Intent(MainActivity.this, SignInActivity.class);
                logoutIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(logoutIntent);
                finish();
                break;
            case R.id.nav_delete_user:
                delUser();
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void btn_action_push_alarm_select(View view) {
        Toast.makeText(MainActivity.this, "개발 예정입니다.", Toast.LENGTH_LONG).show();
    }

    public void btn_action_change_profile_pic(View view) {
        Intent intent = new Intent(MainActivity.this, ProfileImageActivity.class);
        intent.putExtra("UserTB_nicName_Send", UserTB_nicName_fromdb);
        intent.putExtra("UserTB_profileImage_Send", UserTB_profileImage_fromdb);
        intent.putExtra("UserTB_coverImage_Send", UserTB_coverImage_fromdb);
        intent.putExtra("UserTB_coverImageKey_Send", UserTB_coverImageKey_fromdb);
        intent.putExtra("UserTB_profileImageKey_Send", UserTB_profileImageKey_fromdb);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    public void btn_action_change_nic_name(View view) {
        Intent intent = new Intent(MainActivity.this, NicNameActivity.class);
        intent.putExtra("UserTB_nicName_Send", UserTB_nicName_fromdb);
        intent.putExtra("UserTB_profileImage_Send", UserTB_profileImage_fromdb);
        intent.putExtra("UserTB_coverImage_Send", UserTB_coverImage_fromdb);
        intent.putExtra("UserTB_coverImageKey_Send", UserTB_coverImageKey_fromdb);
        intent.putExtra("UserTB_profileImageKey_Send", UserTB_profileImageKey_fromdb);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    public void btn_action_change_cover_pic(View view) {
        Intent intent = new Intent(MainActivity.this, CoverImageActivity.class);
        intent.putExtra("UserTB_nicName_Send", UserTB_nicName_fromdb);
        intent.putExtra("UserTB_profileImage_Send", UserTB_profileImage_fromdb);
        intent.putExtra("UserTB_coverImage_Send", UserTB_coverImage_fromdb);
        intent.putExtra("UserTB_coverImageKey_Send", UserTB_coverImageKey_fromdb);
        intent.putExtra("UserTB_profileImageKey_Send", UserTB_profileImageKey_fromdb);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void delUser() {
        if (firebaseAuth.getCurrentUser() != null) {
            final String currentUserId = firebaseAuth.getCurrentUser().getUid();
            AlertDialog.Builder alert_confirm = new AlertDialog.Builder(MainActivity.this);
            alert_confirm.setMessage("정말 계정을 삭제 할까요? 탈퇴시 DB에 저장된 데이터도 삭제됩니다.").setCancelable(false).setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //삭제는 되나 파일한개씩만 전체삭제 할 방법 찾을것 없다면, 회원탈퇴시 하나씩 찾아 지워야함..(파일삭제화면 띄우고 대기하도록, 그리고 업로드한 파일명,파일path를 기록한 테이블 추가
/*                            StorageReference storageReference = firebaseStorage.getReference().child("ionimp3navercom/userProfileImages/Profileimage.jpg");
                            storageReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(MainActivity.this,"삭제성공",Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(MainActivity.this,"삭제실패",Toast.LENGTH_SHORT).show();
                                }
                            });*/
                            referenceroot = FirebaseDatabase.getInstance().getReference(currentUserId);
                            firebaseAuth.getCurrentUser().delete()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            //db는 선택된 사용자노드 기준 하위글 전체삭제 구문추가
                                            if (task.isSuccessful()) {
                                                //DatabaseReference referenceroot = FirebaseDatabase.getInstance().getReference(currentUserId);
                                                referenceroot.setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            Toast.makeText(MainActivity.this, "DB,Storage 데이터 와 계정을 삭제하였읍니다.", Toast.LENGTH_LONG).show();
                                                            //firebaseAuth.signOut();
                                                            Intent goToOnBardingIntent = new Intent(MainActivity.this, OnBoardingActivity.class);
                                                            //goToOnBardingIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                            startActivity(goToOnBardingIntent);
                                                        } else {
                                                            Toast.makeText(MainActivity.this, "계정삭제를 실패하였읍니다1. 다시 진행해주세요.", Toast.LENGTH_LONG).show();
                                                        }
                                                    }
                                                });
                                            } else {
                                                Toast.makeText(MainActivity.this, "계정삭제를 실패하였읍니다2. 다시 진행해주세요.", Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });
                        }
                    }
            );
            alert_confirm.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Toast.makeText(MainActivity.this, "취소", Toast.LENGTH_LONG).show();
                }
            });
            alert_confirm.show();
        } else {
            Toast.makeText(MainActivity.this, "로그인 후에 삭제가 가능합니다..", Toast.LENGTH_LONG).show();
            Intent goToOnBardingIntent = new Intent(MainActivity.this, OnBoardingActivity.class);
            goToOnBardingIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(goToOnBardingIntent);
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerVisible(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            //backPressHandler.onBackPressed("뒤로가기 버튼 한번 더 누르면 앱을 종료");
            if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
                backKeyPressedTime = System.currentTimeMillis();
                Toast.makeText(this, "뒤로가기 버튼 한번 더 누르면 앱을 종료", Toast.LENGTH_SHORT).show();
                return;
            }
            if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
                moveTaskToBack(true);                        // 태스크를 백그라운드로 이동
                finishAndRemoveTask();                        // 액티비티 종료 + 태스크 리스트에서 지우기
                android.os.Process.killProcess(android.os.Process.myPid());    // 앱 프로세스 종료
            }
        }
    }

    public void btn_action_none(View view) {
        //바탕화면 터치시 제외하기 위해
    }
}
