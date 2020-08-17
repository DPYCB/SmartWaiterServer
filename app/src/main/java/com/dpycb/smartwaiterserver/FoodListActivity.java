package com.dpycb.smartwaiterserver;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.dpycb.smartwaiterserver.interfaces.ItemClickListener;
import com.dpycb.smartwaiterserver.model.Category;
import com.dpycb.smartwaiterserver.model.Common;
import com.dpycb.smartwaiterserver.model.Food;
import com.dpycb.smartwaiterserver.viewholder.FoodViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.picasso.Picasso;

import java.util.UUID;

import info.hoang8f.widget.FButton;

public class FoodListActivity extends AppCompatActivity {
    ConstraintLayout rootLayout;

    RecyclerView recyclerFood;
    RecyclerView.LayoutManager layoutManager;
    FloatingActionButton fab;

    MaterialEditText editFoodName;
    MaterialEditText editFoodPrice;
    MaterialEditText editFoodTime;
    MaterialEditText editFoodDescription;
    FButton btnSelect;
    FButton btnUpload;


    FirebaseDatabase db;
    DatabaseReference foods;
    FirebaseStorage storage;
    StorageReference storageReference;

    String categoryId = "";
    FirebaseRecyclerAdapter<Food, FoodViewHolder> adapter;

    Food foodToAdd;
    Uri saveUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);

        //Init Firebase
        db = FirebaseDatabase.getInstance();
        foods = db.getReference("Food");
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        recyclerFood = findViewById(R.id.recyclerFood);
        recyclerFood.setHasFixedSize(true);
        recyclerFood.setItemViewCacheSize(5);
        layoutManager = new LinearLayoutManager(this);
        recyclerFood.setLayoutManager(layoutManager);

        rootLayout = findViewById(R.id.rootLayout);

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAddDialog();
            }
        });

        if (getIntent().getExtras() != null) {
            categoryId = getIntent().getStringExtra("CategoryID");
        }
        if (!categoryId.isEmpty()) {
            loadFoodList(categoryId);
        }
    }

    private void openAddDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(FoodListActivity.this);
        alertDialog.setTitle("Новое блюдо");
        alertDialog.setMessage("заполните данные о новом блюде в Вашем меню");

        LayoutInflater inflater = this.getLayoutInflater();
        View addFoodItem = inflater.inflate(R.layout.add_food_item, null);

        editFoodName = addFoodItem.findViewById(R.id.editFoodName);
        editFoodPrice = addFoodItem.findViewById(R.id.editFoodPrice);
        editFoodTime = addFoodItem.findViewById(R.id.editFoodTime);
        editFoodDescription = addFoodItem.findViewById(R.id.editFoodDescription);

        btnSelect = addFoodItem.findViewById(R.id.btnSelect);
        btnUpload = addFoodItem.findViewById(R.id.btnUpload);

        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImage();
            }
        });

        alertDialog.setView(addFoodItem);
        alertDialog.setIcon(R.drawable.ic_shopping_cart_black_24dp);

        alertDialog.setPositiveButton("Да", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (foodToAdd != null) {
                    foods.push().setValue(foodToAdd);
                    Snackbar.make(rootLayout, "Новая категория " + foodToAdd.getName() + " успешнно добавлена!", Snackbar.LENGTH_SHORT).show();
                }
            }
        }).setNegativeButton("Нет", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        }).show();
    }

    private void uploadImage() {
        if (saveUri != null) {
            final ProgressDialog mDialog = new ProgressDialog(this);
            mDialog.setMessage("Загрузка...");
            mDialog.show();

            String imageName = UUID.randomUUID().toString();
            final StorageReference imageFolder = storageReference.child("images/" + imageName);
            imageFolder.putFile(saveUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    mDialog.dismiss();
                    Toast.makeText(FoodListActivity.this, "Изображение успешно загружено!", Toast.LENGTH_SHORT).show();

                    imageFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            foodToAdd = new Food(editFoodName.getText().toString(),
                                    saveUri.toString(),
                                    editFoodPrice.getText().toString(),
                                    editFoodTime.getText().toString(),
                                    editFoodDescription.getText().toString(), categoryId);

                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    mDialog.dismiss();
                    Toast.makeText(FoodListActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = 100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount();
                    mDialog.setMessage("Загружено " + progress + "%");
                }
            });
        }
    }

    private void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Выберите изображение"), Common.PICK_IMAGE_REQUEST);
    }

    private void loadFoodList(String categoryId) {
        adapter = new FirebaseRecyclerAdapter<Food, FoodViewHolder>(Food.class, R.layout.food_item,
                FoodViewHolder.class, foods.orderByChild("menuId").equalTo(categoryId)) {
            @Override
            protected void populateViewHolder(FoodViewHolder foodViewHolder, Food food, int i) {
                foodViewHolder.foodText.setText(food.getName());
                Picasso.with(getBaseContext()).load(food.getImage()).into(foodViewHolder.foodImage);

                foodViewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onclick(View view, int position, boolean isLongClock) {

                    }
                });
            }
        };
        adapter.notifyDataSetChanged();
        recyclerFood.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Common.PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            saveUri = data.getData();
            btnSelect.setText("Изображение выбрано!");
        }
    }

    //Edit/Remove functionality

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if (item.getTitle().equals("Обновить")) {
            openEditDialog(adapter.getRef(item.getOrder()).getKey(), adapter.getItem(item.getOrder()));
        }
        else if (item.getTitle().equals("Удалить")) {
            deleteFood(adapter.getRef(item.getOrder()).getKey());
        }
        return super.onContextItemSelected(item);
    }

    private void deleteFood(String key) {
        foods.child(key).removeValue();
        Toast.makeText(this, "Блюдо удалено!", Toast.LENGTH_SHORT).show();
    }

    private void openEditDialog(final String key, final Food item) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(FoodListActivity.this);
        alertDialog.setTitle("Изменить блюдо");
        alertDialog.setMessage("Введите информацию о блюде");

        LayoutInflater inflater = this.getLayoutInflater();
        View addFoodItem = inflater.inflate(R.layout.add_food_item, null);

        editFoodName = addFoodItem.findViewById(R.id.editFoodName);
        editFoodPrice = addFoodItem.findViewById(R.id.editFoodPrice);
        editFoodTime = addFoodItem.findViewById(R.id.editFoodTime);
        editFoodDescription = addFoodItem.findViewById(R.id.editFoodDescription);

        btnSelect = addFoodItem.findViewById(R.id.btnSelect);
        btnUpload = addFoodItem.findViewById(R.id.btnUpload);

        editFoodName.setText(item.getName());
        editFoodPrice.setText(item.getPrice());
        editFoodTime.setText(item.getTime());
        editFoodDescription.setText(item.getDescription());

        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadChangedImage(item);
            }
        });

        alertDialog.setView(addFoodItem);
        alertDialog.setIcon(R.drawable.ic_shopping_cart_black_24dp);

        alertDialog.setPositiveButton("Да", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //Update info
                item.setName(editFoodName.getText().toString());
                item.setPrice(editFoodPrice.getText().toString());
                item.setTime(editFoodTime.getText().toString());
                item.setDescription(editFoodDescription.getText().toString());

                foods.child(key).setValue(item);
            }
        }).setNegativeButton("Нет", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        }).show();
    }

    private void uploadChangedImage(final Food item) {
        if (saveUri != null) {
            final ProgressDialog mDialog = new ProgressDialog(this);
            mDialog.setMessage("Загрузка...");
            mDialog.show();

            String imageName = UUID.randomUUID().toString();
            final StorageReference imageFolder = storageReference.child("images/" + imageName);
            imageFolder.putFile(saveUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    mDialog.dismiss();
                    Toast.makeText(FoodListActivity.this, "Изображение успешно загружено!", Toast.LENGTH_SHORT).show();

                    imageFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            item.setImage(uri.toString());
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    mDialog.dismiss();
                    Toast.makeText(FoodListActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = 100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount();
                    mDialog.setMessage("Загружено " + progress + "%");
                }
            });
        }
    }
}
