package com.example.cbarcode;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cbarcode.adapters.BarcodeAdapter;
import com.example.cbarcode.models.Barcode;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import java.lang.Object;
import android.os.FileUtils;
import java.io.Reader;
import java.io.InputStreamReader;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import static java.nio.file.Paths.*;


public class MainActivity extends AppCompatActivity {



    int sira = 1;
    EditText txtBarkod;
    EditText txtMiktar;
    EditText txtSearch;
    TextView infoMiktar, infoBarkod;
    Button btnEkle;
    Button btnSil;
    Button btnKaydet;
    String INO = null;
    RecyclerView recListe;
    String mText;
    private String resultValue;
    int np = 0;
    private static final int WRITE_EXTERNAL_STORAGE_CODE = 1;
    private BarcodeAdapter mBarcodeAdapter;
    private List<Barcode> BarcodeList = new ArrayList<>();
    private List<Barcode> BarcodeListSearch = new ArrayList<>();
    private String barcode = "";
    private IntentIntegrator scan;
    private String m_Text = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtBarkod = findViewById(R.id.txtBarkod);
        txtMiktar = findViewById(R.id.txtMiktar);
        btnEkle = findViewById(R.id.btnEkle);
        btnKaydet = findViewById(R.id.btnKaydet);
        btnSil = findViewById(R.id.btnSil);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        Runnable r = new Runnable() {
            @Override
            public void run(){
                mBarcodeAdapter.notifyDataSetChanged();
            }
        };




        txtSearch = findViewById(R.id.txtSearch);
        txtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });



        recListe = findViewById(R.id.recListe);
        mBarcodeAdapter = new BarcodeAdapter(BarcodeList, this);
        GridLayoutManager manager = new GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false);
        recListe.setLayoutManager(manager);
        recListe.setItemAnimator(new DefaultItemAnimator());
        recListe.setAdapter(mBarcodeAdapter);

        RecyclerView.ItemDecoration divider = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recListe.addItemDecoration(divider);

        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.START | ItemTouchHelper.END, 0) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder dragged, @NonNull RecyclerView.ViewHolder target) {

                int position_dragged = dragged.getAdapterPosition();
                int position_target = target.getAdapterPosition();

                Collections.swap(BarcodeList,position_dragged,position_target);
                //mBarcodeAdapter.notifyItemMoved(position_dragged,position_target);

                recListe.getAdapter().notifyItemMoved(position_dragged,position_target);

                Handler h = new Handler();
                h.postDelayed(r, 3000);

                return false;


            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            }
        });

        helper.attachToRecyclerView(recListe);

        scan = new IntentIntegrator(this);






        //recListe ekleme
        btnEkle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtBarkod.getText().toString().isEmpty() == false && txtMiktar.getText().toString().isEmpty() == false)
                {

                    mText = txtBarkod.getText().toString().trim() + ";" + txtMiktar.getText().toString().trim();
                    Iterator<Barcode> iterator = BarcodeList.iterator();
                    if (mText.isEmpty()) {
                        Toast.makeText(MainActivity.this, "Lütfen alanları doldurun..", Toast.LENGTH_SHORT).show();
                    }
                    else {

                        if (mText.isEmpty() == false && BarcodeList.size() > 0)
                        {
                            np = 0;
                            while (iterator.hasNext()) {
                                Barcode barcode = iterator.next();
                                long barkod = Long.parseLong(barcode.getBarcode().toString().trim());
                                long barkod2 = Long.parseLong(txtBarkod.getText().toString().trim());
                                if (barkod == barkod2) {
                                    Toast.makeText(MainActivity.this, "Eklenen barkod listede mevcut", Toast.LENGTH_SHORT).show();
                                    np = 1;
                                }
                            }
                            if (np == 0) {
                                String barcode = txtBarkod.getText().toString().trim();
                                int quantity = Integer.parseInt(txtMiktar.getText().toString().trim());
                                int order = sira;
                                Barcode barcode1 = new Barcode(order, quantity, barcode);
                                BarcodeList.add(barcode1);
                                mBarcodeAdapter.notifyDataSetChanged();
                                txtBarkod.setText("");
                                txtMiktar.setText("");
                                sira++;
                            }
                        }
                        else {
                            String barcode = txtBarkod.getText().toString().trim();
                            int quantity = Integer.parseInt(txtMiktar.getText().toString().trim());
                            int order = sira;
                            Barcode barcode1 = new Barcode(order, quantity, barcode);
                            BarcodeList.add(barcode1);
                            mBarcodeAdapter.notifyDataSetChanged();
                            txtBarkod.setText("");
                            txtMiktar.setText("");
                            sira++;
                        }
                    }
                }
                else{
                    Toast.makeText(MainActivity.this, "Barkod veya Miktar bilgisi eksik.", Toast.LENGTH_SHORT).show();
                }
            }
        });


        //text kaydetme tetikleme
        btnKaydet.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                int bCount = BarcodeList.size();
                List<String> listBM = new ArrayList<>();
                if (bCount == 0) {
                    Toast.makeText(MainActivity.this, "Listede barkod mevcut değil.", Toast.LENGTH_SHORT).show();
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                            String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                            requestPermissions(permissions, WRITE_EXTERNAL_STORAGE_CODE);
                        } else {
                            /*listBM = BarcodeList.toArray();*/
                            saveToTxtFile(BarcodeList);
                        }
                    } else {
                        saveToTxtFile(BarcodeList);
                    }
                }
            }
        });

        btnSil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BarcodeList.clear();
                mBarcodeAdapter.notifyDataSetChanged();
            }
        });
    }

    private void filter(String yazi){

        for(Barcode item : BarcodeList){
            if(item.getBarcode().toLowerCase().contains(yazi.toLowerCase())){
                BarcodeListSearch.add(item);
            }
        }
        filterList(BarcodeListSearch);
    }

    //yazma izni almak için
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case WRITE_EXTERNAL_STORAGE_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    saveToTxtFile(BarcodeList);
                } else {
                    Toast.makeText(this, "Lütfen dosya erişim izni veriniz..", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
    //dosyaya kaydetme

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void saveToTxtFile(List mText) {

        getDialogValueBack(MainActivity.this);
        if (resultValue == "cancel") {
            Toast.makeText(this, "Hatalı İrsaliye No girişi.", Toast.LENGTH_SHORT).show();
        }
        else
        {
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(System.currentTimeMillis());
            try {
                File path = Environment.getExternalStorageDirectory();
                File dir = new File(path + "/Irsaliyeler/");
                boolean mkdir = dir.mkdirs();
                if (!mkdir) {
                    String fileName = resultValue +"_" + timeStamp + ".txt";
                    File file = new File(dir, fileName);

                    FileWriter fw = new FileWriter(file.getAbsoluteFile());
                    BufferedWriter bw = new BufferedWriter(fw);
                    //object iterator ile çevirip text dosyasına kaydetme.
                    Iterator<Barcode> iterator = mText.iterator();
                    while (iterator.hasNext())
                    {
                        Barcode barcode = iterator.next();
                        String barkod = barcode.getBarcode().trim();
                        String miktar = String.valueOf(barcode.getQuantity()).trim();
                        bw.write(barkod +";;;"+ miktar);
                        bw.newLine();
                    }
                    bw.flush();
                    bw.close();
                    BarcodeList.clear();
                    mBarcodeAdapter.notifyDataSetChanged();
                    Toast.makeText(this, fileName + " kaydedildi " + dir, Toast.LENGTH_SHORT).show();
                    sira = 1;
                }
            } catch (Exception e) {
                Toast.makeText(this, "HATA", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void scanCode (View view){
        scan.initiateScan();
    }

    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        IntentResult result= IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if (result !=null){
            if (result.getContents() == null) {
                Toast.makeText(this, "bulunand", Toast.LENGTH_SHORT).show();
            }
            else{
                try {
                    JSONObject obj = new JSONObject(result.getContents());
                    txtBarkod.setText(obj.getString("barcode_string"));
                } catch (JSONException e){
                    e.printStackTrace();;
                    Toast.makeText(this, result.getContents(), Toast.LENGTH_SHORT).show();

                }

            }

        } else {
            super.onActivityResult( requestCode,  resultCode,  data);
        }

    }
    //Barkod yakalama ve txtBarkoda yazma.
    public boolean dispatchKeyEvent(KeyEvent e) {
        if (e.getAction()==KeyEvent.ACTION_DOWN && e.getKeyCode() == 520) {
            txtBarkod.requestFocus();
        }
        return super.dispatchKeyEvent(e);
    }



    public String getDialogValueBack(Context context) {
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message mesg) {
                throw new RuntimeException();
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("İrsaliye No giriniz.");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_TEXT_VARIATION_NORMAL);
        builder.setView(input);

        builder.setPositiveButton("Tamam", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (input.getText().toString().isEmpty() == false) {
                    resultValue = input.getText().toString();
                    handler.sendMessage(handler.obtainMessage());
                }

                else
                {
                    resultValue = "cancel";
                    handler.sendMessage(handler.obtainMessage());
                }
            }
        });
        builder.setNegativeButton("İptal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                resultValue = "cancel";
                handler.sendMessage(handler.obtainMessage());
            }
        });

        builder.show();
        try{ Looper.loop(); }
        catch(RuntimeException e){}

        return resultValue;
    }

    public void filterList(List<Barcode> BarcodeListSearch){
        BarcodeList = BarcodeListSearch;
        mBarcodeAdapter.notifyDataSetChanged();

    }
}







