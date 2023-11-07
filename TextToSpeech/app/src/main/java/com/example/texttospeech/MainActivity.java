package com.example.texttospeech;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    Button btnGenrateSpeech, btnGenerateText ,btnSave;
    protected static final int RESULT_SPEECH = 1;
    EditText etText;
    public List<String> items = new ArrayList<>();
    public List<Tuple> Tuplas = new ArrayList<>();
    ListView listView;
    FirebaseFirestore firestore;
    DocumentSnapshot documentSnapshot;
    Tree tree = new Tree();
    int contador = 0;

    DocumentReference documentReference;

    TextToSpeech t1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //getSupportActionBar().hide();
        t1 = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @SuppressLint("SuspiciousIndentation")
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR)
                    t1.setLanguage(Locale.ENGLISH);
            }
        });
        firestore = FirebaseFirestore.getInstance();
        listView = findViewById(R.id.listView);
        btnGenrateSpeech = findViewById(R.id.btn);
        btnGenerateText = findViewById(R.id.btnLis);
        btnSave = findViewById(R.id.btnSave);
        etText = findViewById(R.id.et);
        listView = findViewById(R.id.listView);

        CollectionReference collectionReference = firestore.collection("textos");
        Task<QuerySnapshot> query = collectionReference.get();
        query.addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();

                for (DocumentSnapshot document : querySnapshot)
                    if (document.exists()) {
                        String date = document.getString("date");
                        String text = document.getString("text");

                        // Combine os valores em uma única string com uma vírgula entre eles
                        String combinedValue = text + ", " + date;
                        items.add(combinedValue);
                        contador++;

                    }
            } else {
                Exception e = task.getException();
                e.printStackTrace();
            }
        });
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, items);
        listView.setAdapter(adapter);
        btnGenrateSpeech.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = etText.getText().toString();
                t1.speak(text, TextToSpeech.QUEUE_FLUSH, null);
            }
        });

        btnGenerateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US");
                try {
                    startActivityForResult(intent, RESULT_SPEECH);
                    etText.setText("");
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Incompativel com o seu dispositivo", Toast.LENGTH_SHORT);
                    e.printStackTrace();
                }
            }
        });



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String stringSelecionada = (String) listView.getItemAtPosition(position);
                String[] palavrasSplit = stringSelecionada.split(",");
                String Frase = palavrasSplit[0].toString();
                etText.setText(Frase);

            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                // Verifique se o item está sendo segurado
                if (view.isPressed()) {
                    // Remova o item do listview
                    String value = (String) adapter.getItem(position);
                    items.remove(value);
                    final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, items);
                    listView.setAdapter(adapter);
                    firestore.collection("textos").document("textdata " + position).delete();
                    contador--;
                    return true;
                }
                return false;
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Date date = new Date();
                String text = etText.getText().toString();
                SimpleDateFormat formatador = new SimpleDateFormat("dd/MM/yyyy");
                String dataFormatada = formatador.format(date);
                Tuple item = new Tuple(text, dataFormatada);
                Tuplas.add(item);
                String sep = ",";
                String item_new = text + sep + dataFormatada;
                items.add(item_new);
                final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, items);
                listView.setAdapter(adapter);
                Map<String, Object> map = item.toMap();
                Item item1 = new Item("item" + contador, item);
                tree.addItem(item1);
                documentReference = firestore.collection("textos").document("textdata "+contador);


                contador++;
                documentReference.set(item).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(), "Mensagem enviada", Toast.LENGTH_SHORT);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Mensagem não enviada, error: " + e, Toast.LENGTH_SHORT);
                    }
                });

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case RESULT_SPEECH:
                if (resultCode == RESULT_OK && data != null){
                    ArrayList<String> text =data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    etText.setText(text.get(0));
                }
                break;
        }
    }

}
class Tuple {
    private String text;
    private String date;

    public Tuple(String text, String date) {
        this.text = text;
        this.date = date;
    }

    public String getText() {
        return text;
    }

    public String getDate() {
        return date;
    }
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("text", this.text);
        map.put("date", this.date);
        return map;
    }
}

class Item {
    private String name;
    private Tuple value;

    public Item(String name, Tuple value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public Tuple getValue() {
        return value;
    }
}

class Tree {
    private List<Item> items;

    public Tree() {
        this.items = new ArrayList<>();
    }

    public void addItem(Item item) {
        this.items.add(item);
    }

    public List<Item> getItems() {
        return items;
    }
}




