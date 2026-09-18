package com.example.studyspotucsm;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

public class TaskEditor extends AppCompatActivity {
    private AppTask t;
    private EditText taskInput, professorName, Ubicacion;
    private TextView from, to;
    private Spinner color, spinnerBuilding, spinnerRoom, spinnerTipoClase;
    private Button submit;
    private TextView delete;
    private Database database;

    // Mapa que asocia cada sala con su ubicación
    private Map<String, String> roomLocationMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_editor);

        // Obtener el usuario actual
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            database = new Database(user.getUid()); // Inicializa Database con el UID del usuario
        } else {
            Toast.makeText(this, "No authenticated user found", Toast.LENGTH_LONG).show();
            finish(); // Cerrar la actividad si no hay usuario
            return;
        }

        // Inicialización de los controles UI
        taskInput = findViewById(R.id.task);
        from = findViewById(R.id.from);
        to = findViewById(R.id.to);
        color = findViewById(R.id.color);
        spinnerBuilding = findViewById(R.id.spinnerBuilding);
        spinnerRoom = findViewById(R.id.spinnerRoom);
        professorName = findViewById(R.id.professorName);
        submit = findViewById(R.id.submit);
        delete = findViewById(R.id.delete);
        Ubicacion = findViewById(R.id.Ubicacion);
        spinnerTipoClase = findViewById(R.id.spinnerTipoClase);

        String date = getIntent().getStringExtra("Date");

        // Configuración del spinner para los colores
        String[] colors = {"Rose", "Blue", "Green", "Red", "Yellow", "Orange", "Purple"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, colors);
        color.setAdapter(adapter);

        ArrayAdapter<CharSequence> tipoClaseAdapter = ArrayAdapter.createFromResource(this,
                R.array.tipo_clase, android.R.layout.simple_spinner_item);
        tipoClaseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTipoClase.setAdapter(tipoClaseAdapter);

        ArrayAdapter<CharSequence> adapterBuilding = ArrayAdapter.createFromResource(this,
                R.array.buildings_array, android.R.layout.simple_spinner_item);
        adapterBuilding.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerBuilding.setAdapter(adapterBuilding);

        spinnerBuilding.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                updateRoomsBasedOnBuilding(position);
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spinnerRoom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedRoom = parent.getItemAtPosition(position).toString();
                String location = roomLocationMap.get(selectedRoom);
                if (location != null) {
                    // Muestra la ubicación asociada a la sala seleccionada
                    Ubicacion.setText(location);
                }
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        if (getIntent().hasExtra("ID")) {
            t = new AppTask();
            t.setId(getIntent().getIntExtra("ID", 0));
            t.setTask(getIntent().getStringExtra("Task"));
            t.setFrom(getIntent().getStringExtra("From"));
            t.setTo(getIntent().getStringExtra("To"));
            t.setColor(getIntent().getStringExtra("Color"));
            t.setProfessor(getIntent().getStringExtra("ProfessorName"));
            t.setBuilding(getIntent().getStringExtra("Building"));
            t.setRoom(getIntent().getStringExtra("Room"));
            t.setUbicacion(getIntent().getStringExtra("Ubicacion"));
            t.setTipoClase(getIntent().getStringExtra("TipoClase"));

            taskInput.setText(t.getTask());
            from.setText(t.getFromToString());
            to.setText(t.getToToString());
            color.setSelection(adapter.getPosition(t.getColor()));
            spinnerBuilding.setSelection(adapterBuilding.getPosition(t.getBuilding()));
            professorName.setText(t.getProfessor());
            Ubicacion.setText(t.getUbicacion());
        } else {
            t = new AppTask(); // Nueva instancia si es una nueva tarea
        }

        setupTimePicker(); // Configurar los selectores de tiempo

        submit.setOnClickListener(v -> handleTaskSubmission(date, adapter));
        delete.setOnClickListener(v -> handleTaskDeletion(date));
    }

    private void updateRoomsBasedOnBuilding(int buildingIndex) {
        int arrayId;
        switch (buildingIndex) {
            case 0: // Pabellón A
                arrayId = R.array.pabellon_a_rooms;
                break;
            case 1: // Pabellón B
                arrayId = R.array.pabellon_b_rooms;
                break;
            case 2: // Pabellón C
                arrayId = R.array.pabellon_c_rooms;
                break;
            case 3: // Pabellón D
                arrayId = R.array.pabellon_d_rooms;
                break;
            case 4: // Pabellón E
                arrayId = R.array.pabellon_e_rooms;
                break;
            case 5: // Pabellón CH
                arrayId = R.array.pabellon_ch_rooms;
                break;
            case 6: // Pabellón O
                arrayId = R.array.pabellon_o_rooms;
                break;
            case 7: // Pabellón G
                arrayId = R.array.pabellon_g_rooms;
                break;
            case 8: // Pabellón H
                arrayId = R.array.pabellon_h_rooms;
                break;
            case 9: // Pabellón I
                arrayId = R.array.pabellon_i_rooms;
                break;
            case 10: // Pabellón F
                arrayId = R.array.pabellon_f_rooms;
                break;
            case 11: // Pabellón L
                arrayId = R.array.pabellon_l_rooms;
                break;
            case 12: // Pabellón R
                arrayId = R.array.pabellon_r_rooms;
                break;
            case 13: // Pabellón S
                arrayId = R.array.pabellon_s_rooms;
                break;
            default:
                arrayId = R.array.default_rooms;
                break;
        }
        ArrayAdapter<CharSequence> adapterRoom = ArrayAdapter.createFromResource(this,
                arrayId, android.R.layout.simple_spinner_item);
        adapterRoom.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRoom.setAdapter(adapterRoom);

        // Actualizar el mapa de ubicaciones de las salas
        String[] rooms = getResources().getStringArray(arrayId);
        roomLocationMap.clear(); // Limpiamos el mapa antes de actualizarlo

        if (buildingIndex == 0) { // Si se selecciona el Pabellón A
            for (String room : rooms) {
                switch (room) {
                    case "101":
                        roomLocationMap.put(room, "-16.405755, -71.549604");
                        break;
                    case "102":
                        roomLocationMap.put(room, "-16.405755, -71.549604");
                        break;
                    case "103":
                        roomLocationMap.put(room, "-16.405755, -71.549604");
                        break;
                    case "104":
                        roomLocationMap.put(room, "-16.405755, -71.549604");
                        break;
                    case "105":
                        roomLocationMap.put(room, "-16.405755, -71.549604");
                        break;
                    case "106":
                        roomLocationMap.put(room, "-16.405755, -71.549604");
                        break;
                    case "107":
                        roomLocationMap.put(room, "-16.405755, -71.549604");
                        break;
                    case "108":
                        roomLocationMap.put(room, "-16.405755, -71.549604");
                        break;
                    case "201":
                        roomLocationMap.put(room, "-16.405755, -71.549604");
                        break;
                    case "202":
                        roomLocationMap.put(room, "-16.405755, -71.549604");
                        break;
                    case "203":
                        roomLocationMap.put(room, "-16.405755, -71.549604");
                        break;
                    case "204":
                        roomLocationMap.put(room, "-16.405755, -71.549604");
                        break;
                    case "205":
                        roomLocationMap.put(room, "-16.405755, -71.549604");
                        break;
                    case "206":
                        roomLocationMap.put(room, "-16.405755, -71.549604");
                        break;
                    case "207":
                        roomLocationMap.put(room, "-16.405755, -71.549604");
                        break;
                    case "208":
                        roomLocationMap.put(room, "-16.405755, -71.549604");
                        break;
                    case "301":
                        roomLocationMap.put(room, "-16.405755, -71.549604");
                        break;
                    case "302":
                        roomLocationMap.put(room, "-16.405755, -71.549604");
                        break;
                    case "303":
                        roomLocationMap.put(room, "-16.405755, -71.549604");
                        break;
                    case "304":
                        roomLocationMap.put(room, "-16.405755, -71.549604");
                        break;
                    case "305":
                        roomLocationMap.put(room, "-16.405755, -71.549604");
                        break;
                    case "306":
                        roomLocationMap.put(room, "-16.405755, -71.549604");
                        break;
                    case "307":
                        roomLocationMap.put(room, "-16.405755, -71.549604");
                        break;
                    case "308":
                        roomLocationMap.put(room, "-16.405755, -71.549604");
                        break;
                    case "401":
                        roomLocationMap.put(room, "-16.405755, -71.549604");
                        break;
                    case "402":
                        roomLocationMap.put(room, "-16.405755, -71.549604");
                        break;
                    case "403":
                        roomLocationMap.put(room, "-16.405755, -71.549604");
                        break;
                    case "404":
                        roomLocationMap.put(room, "-16.405755, -71.549604");
                        break;
                    case "405":
                        roomLocationMap.put(room, "-16.405755, -71.549604");
                        break;
                    case "406":
                        roomLocationMap.put(room, "-16.405755, -71.549604");
                        break;
                    case "407":
                        roomLocationMap.put(room, "-16.405755, -71.549604");
                        break;
                    case "408":
                        roomLocationMap.put(room, "-16.405755, -71.549604");
                        break;
                    default:
                        roomLocationMap.put(room, "-16.405755, -71.549604");
                        break;
                }
            }
        } else if (buildingIndex == 1) {
            for (String room : rooms) {
                switch (room) {
                    case "101":
                        roomLocationMap.put(room, "-16.405937, -71.549941");
                        break;
                    case "102":
                        roomLocationMap.put(room, "-16.405937, -71.549941");
                        break;
                    case "103":
                        roomLocationMap.put(room, "-16.405937, -71.549941");
                        break;
                    case "104":
                        roomLocationMap.put(room, "-16.405937, -71.549941");
                        break;
                    case "105":
                        roomLocationMap.put(room, "-16.405937, -71.549941");
                        break;
                    case "106":
                        roomLocationMap.put(room, "-16.405937, -71.549941");
                        break;
                    case "107":
                        roomLocationMap.put(room, "-16.405937, -71.549941");
                        break;
                    case "108":
                        roomLocationMap.put(room, "-16.405937, -71.549941");
                        break;
                    case "201":
                        roomLocationMap.put(room, "-16.405937, -71.549941");
                        break;
                    case "202":
                        roomLocationMap.put(room, "-16.405937, -71.549941");
                        break;
                    case "203":
                        roomLocationMap.put(room, "-16.405937, -71.549941");
                        break;
                    case "204":
                        roomLocationMap.put(room, "-16.405937, -71.549941");
                        break;
                    case "205":
                        roomLocationMap.put(room, "-16.405937, -71.549941");
                        break;
                    case "206":
                        roomLocationMap.put(room, "-16.405937, -71.549941");
                        break;
                    case "207":
                        roomLocationMap.put(room, "-16.405937, -71.549941");
                        break;
                    case "208":
                        roomLocationMap.put(room, "-16.405937, -71.549941");
                        break;
                    case "301":
                        roomLocationMap.put(room, "-16.405937, -71.549941");
                        break;
                    case "302":
                        roomLocationMap.put(room, "-16.405937, -71.549941");
                        break;
                    case "303":
                        roomLocationMap.put(room, "-16.405937, -71.549941");
                        break;
                    case "304":
                        roomLocationMap.put(room, "-16.405937, -71.549941");
                        break;
                    case "305":
                        roomLocationMap.put(room, "-16.405937, -71.549941");
                        break;
                    case "306":
                        roomLocationMap.put(room, "-16.405937, -71.549941");
                        break;
                    case "307":
                        roomLocationMap.put(room, "-16.405937, -71.549941");
                        break;
                    case "308":
                        roomLocationMap.put(room, "-16.405937, -71.549941");
                        break;
                    case "401":
                        roomLocationMap.put(room, "-16.405937, -71.549941");
                        break;
                    case "402":
                        roomLocationMap.put(room, "-16.405937, -71.549941");
                        break;
                    case "403":
                        roomLocationMap.put(room, "-16.405937, -71.549941");
                        break;
                    case "404":
                        roomLocationMap.put(room, "-16.405937, -71.549941");
                        break;
                    case "405":
                        roomLocationMap.put(room, "-16.405937, -71.549941");
                        break;
                    case "406":
                        roomLocationMap.put(room, "-16.405937, -71.549941");
                        break;
                    case "407":
                        roomLocationMap.put(room, "-16.405937, -71.549941");
                        break;
                    case "408":
                        roomLocationMap.put(room, "-16.405937, -71.549941");
                        break;
                    default:
                        roomLocationMap.put(room, "-16.405937, -71.549941");
                        break;
                }
            }// Si se selecciona el Pabellón B
        } else if (buildingIndex == 2) { // Si se selecciona el Pabellón C
            for (String room : rooms) {
                switch (room) {
                    case "101":
                        roomLocationMap.put(room, "-16.406001538121053, -71.55000086890158");
                        break;
                    case "102":
                        roomLocationMap.put(room, "-16.406001538121053, -71.55000086890158");
                        break;
                    case "103":
                        roomLocationMap.put(room, "-16.406001538121053, -71.55000086890158");
                        break;
                    case "104":
                        roomLocationMap.put(room, "-16.406001538121053, -71.55000086890158");
                        break;
                    case "105":
                        roomLocationMap.put(room, "-16.406001538121053, -71.55000086890158");
                        break;
                    case "106":
                        roomLocationMap.put(room, "-16.406001538121053, -71.55000086890158");
                        break;
                    case "107":
                        roomLocationMap.put(room, "-16.406001538121053, -71.55000086890158");
                        break;
                    case "108":
                        roomLocationMap.put(room, "-16.406001538121053, -71.55000086890158");
                        break;
                    case "201":
                        roomLocationMap.put(room, "-16.406001538121053, -71.55000086890158");
                        break;
                    case "202":
                        roomLocationMap.put(room, "-16.406001538121053, -71.55000086890158");
                        break;
                    case "203":
                        roomLocationMap.put(room, "-16.406001538121053, -71.55000086890158");
                        break;
                    case "204":
                        roomLocationMap.put(room, "-16.406001538121053, -71.55000086890158");
                        break;
                    case "205":
                        roomLocationMap.put(room, "-16.406001538121053, -71.55000086890158");
                        break;
                    case "206":
                        roomLocationMap.put(room, "-16.406001538121053, -71.55000086890158");
                        break;
                    case "207":
                        roomLocationMap.put(room, "-16.406001538121053, -71.55000086890158");
                        break;
                    case "208":
                        roomLocationMap.put(room, "-16.406001538121053, -71.55000086890158");
                        break;
                    case "301":
                        roomLocationMap.put(room, "-16.406001538121053, -71.55000086890158");
                        break;
                    case "302":
                        roomLocationMap.put(room, "-16.406001538121053, -71.55000086890158");
                        break;
                    case "303":
                        roomLocationMap.put(room, "-16.406001538121053, -71.55000086890158");
                        break;
                    case "304":
                        roomLocationMap.put(room, "-16.406001538121053, -71.55000086890158");
                        break;
                    case "305":
                        roomLocationMap.put(room, "-16.406001538121053, -71.55000086890158");
                        break;
                    case "306":
                        roomLocationMap.put(room, "-16.406001538121053, -71.55000086890158");
                        break;
                    case "307":
                        roomLocationMap.put(room, "-16.406001538121053, -71.55000086890158");
                        break;
                    case "308":
                        roomLocationMap.put(room, "-16.406001538121053, -71.55000086890158");
                        break;
                    case "401":
                        roomLocationMap.put(room, "-16.406001538121053, -71.55000086890158");
                        break;
                    case "402":
                        roomLocationMap.put(room, "-16.406001538121053, -71.55000086890158");
                        break;
                    case "403":
                        roomLocationMap.put(room, "-16.406001538121053, -71.55000086890158");
                        break;
                    case "404":
                        roomLocationMap.put(room, "-16.406001538121053, -71.55000086890158");
                        break;
                    case "405":
                        roomLocationMap.put(room, "-16.406001538121053, -71.55000086890158");
                        break;
                    case "406":
                        roomLocationMap.put(room, "-16.406001538121053, -71.55000086890158");
                        break;
                    case "407":
                        roomLocationMap.put(room, "-16.406001538121053, -71.55000086890158");
                        break;
                    case "408":
                        roomLocationMap.put(room, "-16.406001538121053, -71.55000086890158");
                        break;
                    default:
                        roomLocationMap.put(room, "-16.406001538121053, -71.55000086890158");
                        break;
                }
            }
        } else if (buildingIndex == 3) { // Si se selecciona el Pabellón D
            for (String room : rooms) {
                switch (room) {
                    case "101":
                        roomLocationMap.put(room, "-16.406300, -71.550095");
                        break;
                    case "102":
                        roomLocationMap.put(room, "-16.406300, -71.550095");
                        break;
                    case "103":
                        roomLocationMap.put(room, "-16.406300, -71.550095");
                        break;
                    case "104":
                        roomLocationMap.put(room, "-16.406300, -71.550095");
                        break;
                    case "105":
                        roomLocationMap.put(room, "-16.406300, -71.550095");
                        break;
                    case "106":
                        roomLocationMap.put(room, "-16.406300, -71.550095");
                        break;
                    case "107":
                        roomLocationMap.put(room, "-16.406300, -71.550095");
                        break;
                    case "108":
                        roomLocationMap.put(room, "-16.406300, -71.550095");
                        break;
                    case "201":
                        roomLocationMap.put(room, "-16.406300, -71.550095");
                        break;
                    case "202":
                        roomLocationMap.put(room, "-16.406300, -71.550095");
                        break;
                    case "203":
                        roomLocationMap.put(room, "-16.406300, -71.550095");
                        break;
                    case "204":
                        roomLocationMap.put(room, "-16.406300, -71.550095");
                        break;
                    case "205":
                        roomLocationMap.put(room, "-16.406300, -71.550095");
                        break;
                    case "206":
                        roomLocationMap.put(room, "-16.406300, -71.550095");
                        break;
                    case "207":
                        roomLocationMap.put(room, "-16.406300, -71.550095");
                        break;
                    case "208":
                        roomLocationMap.put(room, "-16.406300, -71.550095");
                        break;
                    case "301":
                        roomLocationMap.put(room, "-16.406300, -71.550095");
                        break;
                    case "302":
                        roomLocationMap.put(room, "-16.406300, -71.550095");
                        break;
                    case "303":
                        roomLocationMap.put(room, "-16.406300, -71.550095");
                        break;
                    case "304":
                        roomLocationMap.put(room, "-16.406300, -71.550095");
                        break;
                    case "305":
                        roomLocationMap.put(room, "-16.406300, -71.550095");
                        break;
                    case "306":
                        roomLocationMap.put(room, "-16.406300, -71.550095");
                        break;
                    case "307":
                        roomLocationMap.put(room, "-16.406300, -71.550095");
                        break;
                    case "308":
                        roomLocationMap.put(room, "-16.406300, -71.550095");
                        break;
                    case "401":
                        roomLocationMap.put(room, "-16.406300, -71.550095");
                        break;
                    case "402":
                        roomLocationMap.put(room, "-16.406300, -71.550095");
                        break;
                    case "403":
                        roomLocationMap.put(room, "-16.406300, -71.550095");
                        break;
                    case "404":
                        roomLocationMap.put(room, "-16.406300, -71.550095");
                        break;
                    case "405":
                        roomLocationMap.put(room, "-16.406300, -71.550095");
                        break;
                    case "406":
                        roomLocationMap.put(room, "-16.406300, -71.550095");
                        break;
                    case "407":
                        roomLocationMap.put(room, "-16.406300, -71.550095");
                        break;
                    case "408":
                        roomLocationMap.put(room, "-16.406300, -71.550095");
                        break;
                    default:
                        roomLocationMap.put(room, "Ubicación desconocida");
                        break;
                }
            }
        } else if (buildingIndex == 4) { // Si se selecciona el Pabellón E
            for (String room : rooms) {
                switch (room) {
                    case "101":
                        roomLocationMap.put(room, "-16.405429, -71.548777");
                        break;
                    case "102":
                        roomLocationMap.put(room, "-16.405429, -71.548777");
                        break;
                    case "103":
                        roomLocationMap.put(room, "-16.405429, -71.548777");
                        break;
                    case "104":
                        roomLocationMap.put(room, "-16.405429, -71.548777");
                        break;
                    case "105":
                        roomLocationMap.put(room, "-16.405429, -71.548777");
                        break;
                    case "106":
                        roomLocationMap.put(room, "-16.405429, -71.548777");
                        break;
                    case "107":
                        roomLocationMap.put(room, "-16.405429, -71.548777");
                        break;
                    case "108":
                        roomLocationMap.put(room, "-16.405429, -71.548777");
                        break;
                    case "201":
                        roomLocationMap.put(room, "-16.405429, -71.548777");
                        break;
                    case "202":
                        roomLocationMap.put(room, "-16.405429, -71.548777");
                        break;
                    case "203":
                        roomLocationMap.put(room, "-16.405429, -71.548777");
                        break;
                    case "204":
                        roomLocationMap.put(room, "-16.405429, -71.548777");
                        break;
                    case "205":
                        roomLocationMap.put(room, "-16.405429, -71.548777");
                        break;
                    case "206":
                        roomLocationMap.put(room, "-16.405429, -71.548777");
                        break;
                    case "207":
                        roomLocationMap.put(room, "-16.405429, -71.548777");
                        break;
                    case "208":
                        roomLocationMap.put(room, "-16.405429, -71.548777");
                        break;
                    case "301":
                        roomLocationMap.put(room, "-16.405429, -71.548777");
                        break;
                    case "302":
                        roomLocationMap.put(room, "-16.405429, -71.548777");
                        break;
                    case "303":
                        roomLocationMap.put(room, "-16.405429, -71.548777");
                        break;
                    case "304":
                        roomLocationMap.put(room, "-16.405429, -71.548777");
                        break;
                    case "305":
                        roomLocationMap.put(room, "-16.405429, -71.548777");
                        break;
                    case "306":
                        roomLocationMap.put(room, "-16.405429, -71.548777");
                        break;
                    case "307":
                        roomLocationMap.put(room, "-16.405429, -71.548777");
                        break;
                    case "308":
                        roomLocationMap.put(room, "-16.405429, -71.548777");
                        break;
                    case "401":
                        roomLocationMap.put(room, "-16.405429, -71.548777");
                        break;
                    case "402":
                        roomLocationMap.put(room, "-16.405429, -71.548777");
                        break;
                    case "403":
                        roomLocationMap.put(room, "-16.405429, -71.548777");
                        break;
                    case "404":
                        roomLocationMap.put(room, "-16.405429, -71.548777");
                        break;
                    case "405":
                        roomLocationMap.put(room, "-16.405429, -71.548777");
                        break;
                    case "406":
                        roomLocationMap.put(room, "-16.405429, -71.548777");
                        break;
                    case "407":
                        roomLocationMap.put(room, "-16.405429, -71.548777");
                        break;
                    case "408":
                        roomLocationMap.put(room, "-16.405429, -71.548777");
                        break;
                    default:
                        roomLocationMap.put(room, "Ubicación desconocida");
                        break;
                }
            }
        } else if (buildingIndex == 5) { // Si se selecciona el Pabellón CH
            for (String room : rooms) {
                switch (room) {
                    case "101":
                        roomLocationMap.put(room, "-16.406348, -71.550042");
                        break;
                    case "102":
                        roomLocationMap.put(room, "-16.406348, -71.550042");
                        break;
                    case "103":
                        roomLocationMap.put(room, "-16.406348, -71.550042");
                        break;
                    case "104":
                        roomLocationMap.put(room, "-16.406348, -71.550042");
                        break;
                    case "105":
                        roomLocationMap.put(room, "-16.406348, -71.550042");
                        break;
                    case "106":
                        roomLocationMap.put(room, "-16.406348, -71.550042");
                        break;
                    case "107":
                        roomLocationMap.put(room, "-16.406348, -71.550042");
                        break;
                    case "108":
                        roomLocationMap.put(room, "-16.406348, -71.550042");
                        break;
                    case "201":
                        roomLocationMap.put(room, "-16.406348, -71.550042");
                        break;
                    case "202":
                        roomLocationMap.put(room, "-16.406348, -71.550042");
                        break;
                    case "203":
                        roomLocationMap.put(room, "-16.406348, -71.550042");
                        break;
                    case "204":
                        roomLocationMap.put(room, "-16.406348, -71.550042");
                        break;
                    case "205":
                        roomLocationMap.put(room, "-16.406348, -71.550042");
                        break;
                    case "206":
                        roomLocationMap.put(room, "-16.406348, -71.550042");
                        break;
                    case "207":
                        roomLocationMap.put(room, "-16.406348, -71.550042");
                        break;
                    case "208":
                        roomLocationMap.put(room, "-16.406348, -71.550042");
                        break;
                    case "301":
                        roomLocationMap.put(room, "-16.406348, -71.550042");
                        break;
                    case "302":
                        roomLocationMap.put(room, "-16.406348, -71.550042");
                        break;
                    case "303":
                        roomLocationMap.put(room, "-16.406348, -71.550042");
                        break;
                    case "304":
                        roomLocationMap.put(room, "-16.406348, -71.550042");
                        break;
                    case "305":
                        roomLocationMap.put(room, "-16.406348, -71.550042");
                        break;
                    case "306":
                        roomLocationMap.put(room, "-16.406348, -71.550042");
                        break;
                    case "307":
                        roomLocationMap.put(room, "-16.406348, -71.550042");
                        break;
                    case "308":
                        roomLocationMap.put(room, "-16.406348, -71.550042");
                        break;
                    case "401":
                        roomLocationMap.put(room, "-16.406348, -71.550042");
                        break;
                    case "402":
                        roomLocationMap.put(room, "-16.406348, -71.550042");
                        break;
                    case "403":
                        roomLocationMap.put(room, "-16.406348, -71.550042");
                        break;
                    case "404":
                        roomLocationMap.put(room, "-16.406348, -71.550042");
                        break;
                    case "405":
                        roomLocationMap.put(room, "-16.406348, -71.550042");
                        break;
                    case "406":
                        roomLocationMap.put(room, "-16.406348, -71.550042");
                        break;
                    case "407":
                        roomLocationMap.put(room, "-16.406348, -71.550042");
                        break;
                    case "408":
                        roomLocationMap.put(room, "-16.406348, -71.550042");
                        break;
                    default:
                        roomLocationMap.put(room, "Ubicación desconocida");
                        break;
                }
            }
        } else if (buildingIndex == 6) { // Si se selecciona el Pabellón O
            for (String room : rooms) {
                switch (room) {
                    case "101":
                        roomLocationMap.put(room, "-16.405902, -71.549193");
                        break;
                    case "102":
                        roomLocationMap.put(room, "-16.405902, -71.549193");
                        break;
                    case "103":
                        roomLocationMap.put(room, "-16.405902, -71.549193");
                        break;
                    case "104":
                        roomLocationMap.put(room, "-16.405902, -71.549193");
                        break;
                    case "105":
                        roomLocationMap.put(room, "-16.405902, -71.549193");
                        break;
                    case "106":
                        roomLocationMap.put(room, "-16.405902, -71.549193");
                        break;
                    case "107":
                        roomLocationMap.put(room, "-16.405902, -71.549193");
                        break;
                    case "108":
                        roomLocationMap.put(room, "-16.405902, -71.549193");
                        break;
                    case "201":
                        roomLocationMap.put(room, "-16.405902, -71.549193");
                        break;
                    case "202":
                        roomLocationMap.put(room, "-16.405902, -71.549193");
                        break;
                    case "203":
                        roomLocationMap.put(room, "-16.405902, -71.549193");
                        break;
                    case "204":
                        roomLocationMap.put(room, "-16.405902, -71.549193");
                        break;
                    case "205":
                        roomLocationMap.put(room, "-16.405902, -71.549193");
                        break;
                    case "206":
                        roomLocationMap.put(room, "-16.405902, -71.549193");
                        break;
                    case "207":
                        roomLocationMap.put(room, "-16.405902, -71.549193");
                        break;
                    case "208":
                        roomLocationMap.put(room, "-16.405902, -71.549193");
                        break;
                    case "301":
                        roomLocationMap.put(room, "-16.405902, -71.549193");
                        break;
                    case "302":
                        roomLocationMap.put(room, "-16.405902, -71.549193");
                        break;
                    case "303":
                        roomLocationMap.put(room, "-16.405902, -71.549193");
                        break;
                    case "304":
                        roomLocationMap.put(room, "-16.405902, -71.549193");
                        break;
                    case "305":
                        roomLocationMap.put(room, "-16.405902, -71.549193");
                        break;
                    case "306":
                        roomLocationMap.put(room, "-16.405902, -71.549193");
                        break;
                    case "307":
                        roomLocationMap.put(room, "-16.405902, -71.549193");
                        break;
                    case "308":
                        roomLocationMap.put(room, "-16.405902, -71.549193");
                        break;
                    case "401":
                        roomLocationMap.put(room, "-16.405902, -71.549193");
                        break;
                    case "402":
                        roomLocationMap.put(room, "-16.405902, -71.549193");
                        break;
                    case "403":
                        roomLocationMap.put(room, "-16.405902, -71.549193");
                        break;
                    case "404":
                        roomLocationMap.put(room, "-16.405902, -71.549193");
                        break;
                    case "405":
                        roomLocationMap.put(room, "-16.405902, -71.549193");
                        break;
                    case "406":
                        roomLocationMap.put(room, "-16.405902, -71.549193");
                        break;
                    case "407":
                        roomLocationMap.put(room, "-16.405902, -71.549193");
                        break;
                    case "408":
                        roomLocationMap.put(room, "-16.405902, -71.549193");
                        break;
                    default:
                        roomLocationMap.put(room, "Ubicación desconocida");
                        break;
                }
            }
        } else if (buildingIndex == 7) { // Si se selecciona el Pabellón G
            for (String room : rooms) {
                switch (room) {
                    case "101":
                        roomLocationMap.put(room, "-16.405898332782996, -71.54933117642936");
                        break;
                    case "102":
                        roomLocationMap.put(room, "-16.405898332782996, -71.54933117642936");
                        break;
                    case "103":
                        roomLocationMap.put(room, "-16.405898332782996, -71.54933117642936");
                        break;
                    case "104":
                        roomLocationMap.put(room, "-16.405898332782996, -71.54933117642936");
                        break;
                    case "105":
                        roomLocationMap.put(room, "-16.405898332782996, -71.54933117642936");
                        break;
                    case "106":
                        roomLocationMap.put(room, "-16.405898332782996, -71.54933117642936");
                        break;
                    case "107":
                        roomLocationMap.put(room, "-16.405898332782996, -71.54933117642936");
                        break;
                    case "108":
                        roomLocationMap.put(room, "-16.405898332782996, -71.54933117642936");
                        break;
                    case "201":
                        roomLocationMap.put(room, "-16.405898332782996, -71.54933117642936");
                        break;
                    case "202":
                        roomLocationMap.put(room, "-16.405898332782996, -71.54933117642936");
                        break;
                    case "203":
                        roomLocationMap.put(room, "-16.405898332782996, -71.54933117642936");
                        break;
                    case "204":
                        roomLocationMap.put(room, "-16.405898332782996, -71.54933117642936");
                        break;
                    case "205":
                        roomLocationMap.put(room, "-16.405898332782996, -71.54933117642936");
                        break;
                    case "206":
                        roomLocationMap.put(room, "-16.405898332782996, -71.54933117642936");
                        break;
                    case "207":
                        roomLocationMap.put(room, "-16.405898332782996, -71.54933117642936");
                        break;
                    case "208":
                        roomLocationMap.put(room, "-16.405898332782996, -71.54933117642936");
                        break;
                    case "301":
                        roomLocationMap.put(room, "-16.405898332782996, -71.54933117642936");
                        break;
                    case "302":
                        roomLocationMap.put(room, "-16.405898332782996, -71.54933117642936");
                        break;
                    case "303":
                        roomLocationMap.put(room, "-16.405898332782996, -71.54933117642936");
                        break;
                    case "304":
                        roomLocationMap.put(room, "-16.405898332782996, -71.54933117642936");
                        break;
                    case "305":
                        roomLocationMap.put(room, "-16.405898332782996, -71.54933117642936");
                        break;
                    case "306":
                        roomLocationMap.put(room, "-16.405898332782996, -71.54933117642936");
                        break;
                    case "307":
                        roomLocationMap.put(room, "-16.405898332782996, -71.54933117642936");
                        break;
                    case "308":
                        roomLocationMap.put(room, "-16.405898332782996, -71.54933117642936");
                        break;
                    case "401":
                        roomLocationMap.put(room, "-16.405898332782996, -71.54933117642936");
                        break;
                    case "402":
                        roomLocationMap.put(room, "-16.405898332782996, -71.54933117642936");
                        break;
                    case "403":
                        roomLocationMap.put(room, "-16.405898332782996, -71.54933117642936");
                        break;
                    case "404":
                        roomLocationMap.put(room, "-16.405898332782996, -71.54933117642936");
                        break;
                    case "405":
                        roomLocationMap.put(room, "-16.405898332782996, -71.54933117642936");
                        break;
                    case "406":
                        roomLocationMap.put(room, "-16.405898332782996, -71.54933117642936");
                        break;
                    case "407":
                        roomLocationMap.put(room, "-16.405898332782996, -71.54933117642936");
                        break;
                    case "408":
                        roomLocationMap.put(room, "-16.405898332782996, -71.54933117642936");
                        break;
                    default:
                        roomLocationMap.put(room, "Ubicación desconocida");
                        break;
                }
            }
        } else if (buildingIndex == 8) { // Si se selecciona el Pabellón H
            for (String room : rooms) {
                switch (room) {
                    case "101":
                        roomLocationMap.put(room, "-16.406089, -71.549248");
                        break;
                    case "102":
                        roomLocationMap.put(room, "-16.406089, -71.549248");
                        break;
                    case "103":
                        roomLocationMap.put(room, "-16.406089, -71.549248");
                        break;
                    case "104":
                        roomLocationMap.put(room, "-16.406089, -71.549248");
                        break;
                    case "105":
                        roomLocationMap.put(room, "-16.406089, -71.549248");
                        break;
                    case "106":
                        roomLocationMap.put(room, "-16.406089, -71.549248");
                        break;
                    case "107":
                        roomLocationMap.put(room, "-16.406089, -71.549248");
                        break;
                    case "108":
                        roomLocationMap.put(room, "-16.406089, -71.549248");
                        break;
                    case "201":
                        roomLocationMap.put(room, "-16.406089, -71.549248");
                        break;
                    case "202":
                        roomLocationMap.put(room, "-16.406089, -71.549248");
                        break;
                    case "203":
                        roomLocationMap.put(room, "-16.406089, -71.549248");
                        break;
                    case "204":
                        roomLocationMap.put(room, "-16.406089, -71.549248");
                        break;
                    case "205":
                        roomLocationMap.put(room, "-16.406089, -71.549248");
                        break;
                    case "206":
                        roomLocationMap.put(room, "-16.406089, -71.549248");
                        break;
                    case "207":
                        roomLocationMap.put(room, "-16.406089, -71.549248");
                        break;
                    case "208":
                        roomLocationMap.put(room, "-16.406089, -71.549248");
                        break;
                    case "301":
                        roomLocationMap.put(room, "-16.406089, -71.549248");
                        break;
                    case "302":
                        roomLocationMap.put(room, "-16.406089, -71.549248");
                        break;
                    case "303":
                        roomLocationMap.put(room, "-16.406089, -71.549248");
                        break;
                    case "304":
                        roomLocationMap.put(room, "-16.406089, -71.549248");
                        break;
                    case "305":
                        roomLocationMap.put(room, "-16.406089, -71.549248");
                        break;
                    case "306":
                        roomLocationMap.put(room, "-16.406089, -71.549248");
                        break;
                    case "307":
                        roomLocationMap.put(room, "-16.406089, -71.549248");
                        break;
                    case "308":
                        roomLocationMap.put(room, "-16.406089, -71.549248");
                        break;
                    case "401":
                        roomLocationMap.put(room, "-16.406089, -71.549248");
                        break;
                    case "402":
                        roomLocationMap.put(room, "-16.406089, -71.549248");
                        break;
                    case "403":
                        roomLocationMap.put(room, "-16.406089, -71.549248");
                        break;
                    case "404":
                        roomLocationMap.put(room, "-16.406089, -71.549248");
                        break;
                    case "405":
                        roomLocationMap.put(room, "-16.406089, -71.549248");
                        break;
                    case "406":
                        roomLocationMap.put(room, "-16.406089, -71.549248");
                        break;
                    case "407":
                        roomLocationMap.put(room, "-16.406089, -71.549248");
                        break;
                    case "408":
                        roomLocationMap.put(room, "-16.406089, -71.549248");
                        break;
                    default:
                        roomLocationMap.put(room, "Ubicación desconocida");
                        break;
                }
            }
        } else if (buildingIndex == 9) { // Si se selecciona el Pabellón I
            for (String room : rooms) {
                switch (room) {
                    case "101":
                        roomLocationMap.put(room, "-16.406079, -71.549090");
                        break;
                    case "102":
                        roomLocationMap.put(room, "-16.406079, -71.549090");
                        break;
                    case "103":
                        roomLocationMap.put(room, "-16.406079, -71.549090");
                        break;
                    case "104":
                        roomLocationMap.put(room, "-16.406079, -71.549090");
                        break;
                    case "105":
                        roomLocationMap.put(room, "-16.406079, -71.549090");
                        break;
                    case "106":
                        roomLocationMap.put(room, "-16.406079, -71.549090");
                        break;
                    case "107":
                        roomLocationMap.put(room, "-16.406079, -71.549090");
                        break;
                    case "108":
                        roomLocationMap.put(room, "-16.406079, -71.549090");
                        break;
                    case "201":
                        roomLocationMap.put(room, "-16.406079, -71.549090");
                        break;
                    case "202":
                        roomLocationMap.put(room, "-16.406079, -71.549090");
                        break;
                    case "203":
                        roomLocationMap.put(room, "-16.406079, -71.549090");
                        break;
                    case "204":
                        roomLocationMap.put(room, "-16.406079, -71.549090");
                        break;
                    case "205":
                        roomLocationMap.put(room, "-16.406079, -71.549090");
                        break;
                    case "206":
                        roomLocationMap.put(room, "-16.406079, -71.549090");
                        break;
                    case "207":
                        roomLocationMap.put(room, "-16.406079, -71.549090");
                        break;
                    case "208":
                        roomLocationMap.put(room, "-16.406079, -71.549090");
                        break;
                    case "301":
                        roomLocationMap.put(room, "-16.406079, -71.549090");
                        break;
                    case "302":
                        roomLocationMap.put(room, "-16.406079, -71.549090");
                        break;
                    case "303":
                        roomLocationMap.put(room, "-16.406079, -71.549090");
                        break;
                    case "304":
                        roomLocationMap.put(room, "-16.406079, -71.549090");
                        break;
                    case "305":
                        roomLocationMap.put(room, "-16.406079, -71.549090");
                        break;
                    case "306":
                        roomLocationMap.put(room, "-16.406079, -71.549090");
                        break;
                    case "307":
                        roomLocationMap.put(room, "-16.406079, -71.549090");
                        break;
                    case "308":
                        roomLocationMap.put(room, "-16.406079, -71.549090");
                        break;
                    case "401":
                        roomLocationMap.put(room, "-16.406079, -71.549090");
                        break;
                    case "402":
                        roomLocationMap.put(room, "-16.406079, -71.549090");
                        break;
                    case "403":
                        roomLocationMap.put(room, "-16.406079, -71.549090");
                        break;
                    case "404":
                        roomLocationMap.put(room, "-16.406079, -71.549090");
                        break;
                    case "405":
                        roomLocationMap.put(room, "-16.406079, -71.549090");
                        break;
                    case "406":
                        roomLocationMap.put(room, "-16.406079, -71.549090");
                        break;
                    case "407":
                        roomLocationMap.put(room, "-16.406079, -71.549090");
                        break;
                    case "408":
                        roomLocationMap.put(room, "-16.406079, -71.549090");
                        break;
                    default:
                        roomLocationMap.put(room, "Ubicación desconocida");
                        break;
                }
            }
        } else if (buildingIndex == 10) { // Si se selecciona el Pabellón F
            for (String room : rooms) {
                switch (room) {
                    case "101":
                        roomLocationMap.put(room, "-16.405639, -71.548689");
                        break;
                    case "102":
                        roomLocationMap.put(room, "-16.405639, -71.548689");
                        break;
                    case "103":
                        roomLocationMap.put(room, "-16.405639, -71.548689");
                        break;
                    case "104":
                        roomLocationMap.put(room, "-16.405639, -71.548689");
                        break;
                    case "105":
                        roomLocationMap.put(room, "-16.405639, -71.548689");
                        break;
                    case "106":
                        roomLocationMap.put(room, "-16.405639, -71.548689");
                        break;
                    case "107":
                        roomLocationMap.put(room, "-16.405639, -71.548689");
                        break;
                    case "108":
                        roomLocationMap.put(room, "-16.405639, -71.548689");
                        break;
                    case "201":
                        roomLocationMap.put(room, "-16.405639, -71.548689");
                        break;
                    case "202":
                        roomLocationMap.put(room, "-16.405639, -71.548689");
                        break;
                    case "203":
                        roomLocationMap.put(room, "-16.405639, -71.548689");
                        break;
                    case "204":
                        roomLocationMap.put(room, "-16.405639, -71.548689");
                        break;
                    case "205":
                        roomLocationMap.put(room, "-16.405639, -71.548689");
                        break;
                    case "206":
                        roomLocationMap.put(room, "-16.405639, -71.548689");
                        break;
                    case "207":
                        roomLocationMap.put(room, "-16.405639, -71.548689");
                        break;
                    case "208":
                        roomLocationMap.put(room, "-16.405639, -71.548689");
                        break;
                    case "301":
                        roomLocationMap.put(room, "-16.405639, -71.548689");
                        break;
                    case "302":
                        roomLocationMap.put(room, "-16.405639, -71.548689");
                        break;
                    case "303":
                        roomLocationMap.put(room, "-16.405639, -71.548689");
                        break;
                    case "304":
                        roomLocationMap.put(room, "-16.405639, -71.548689");
                        break;
                    case "305":
                        roomLocationMap.put(room, "-16.405639, -71.548689");
                        break;
                    case "306":
                        roomLocationMap.put(room, "-16.405639, -71.548689");
                        break;
                    case "307":
                        roomLocationMap.put(room, "-16.405639, -71.548689");
                        break;
                    case "308":
                        roomLocationMap.put(room, "-16.405639, -71.548689");
                        break;
                    case "401":
                        roomLocationMap.put(room, "-16.405639, -71.548689");
                        break;
                    case "402":
                        roomLocationMap.put(room, "-16.405639, -71.548689");
                        break;
                    case "403":
                        roomLocationMap.put(room, "-16.405639, -71.548689");
                        break;
                    case "404":
                        roomLocationMap.put(room, "-16.405639, -71.548689");
                        break;
                    case "405":
                        roomLocationMap.put(room, "-16.405639, -71.548689");
                        break;
                    case "406":
                        roomLocationMap.put(room, "-16.405639, -71.548689");
                        break;
                    case "407":
                        roomLocationMap.put(room, "-16.405639, -71.548689");
                        break;
                    case "408":
                        roomLocationMap.put(room, "-16.405639, -71.548689");
                        break;
                    default:
                        roomLocationMap.put(room, "Ubicación desconocida");
                        break;
                }
            }
        } else if (buildingIndex == 11) { // Si se selecciona el Pabellón L
            for (String room : rooms) {
                switch (room) {
                    case "101":
                        roomLocationMap.put(room, "Salon No existente");
                        break;
                    case "102":
                        roomLocationMap.put(room, "Salon No existente");
                        break;
                    case "103":
                        roomLocationMap.put(room, "Salon No existente");
                        break;
                    case "104":
                        roomLocationMap.put(room, "Salon No existente");
                        break;
                    case "105":
                        roomLocationMap.put(room, "Salon No existente");
                        break;
                    case "106":
                        roomLocationMap.put(room, "Salon No existente");
                        break;
                    case "107":
                        roomLocationMap.put(room, "Salon No existente");
                        break;
                    case "108":
                        roomLocationMap.put(room, "Salon No existente");
                        break;
                    case "201":
                        roomLocationMap.put(room, "-16.405548, -71.547966");
                        break;
                    case "202":
                        roomLocationMap.put(room, "-16.405548, -71.547966");
                        break;
                    case "203":
                        roomLocationMap.put(room, "-16.405548, -71.547966");
                        break;
                    case "204":
                        roomLocationMap.put(room, "-16.405548, -71.547966");
                        break;
                    case "205":
                        roomLocationMap.put(room, "-16.405548, -71.547966");
                        break;
                    case "206":
                        roomLocationMap.put(room, "-16.405548, -71.547966");
                        break;
                    case "207":
                        roomLocationMap.put(room, "-16.405548, -71.547966");
                        break;
                    case "208":
                        roomLocationMap.put(room, "Salon No existente");
                        break;
                    case "301":
                        roomLocationMap.put(room, "-16.405548, -71.547966");
                        break;
                    case "302":
                        roomLocationMap.put(room, "-16.405548, -71.547966");
                        break;
                    case "303":
                        roomLocationMap.put(room, "-16.405548, -71.547966");
                        break;
                    case "304":
                        roomLocationMap.put(room, "-16.405548, -71.547966");
                        break;
                    case "305":
                        roomLocationMap.put(room, "-16.405548, -71.547966");
                        break;
                    case "306":
                        roomLocationMap.put(room, "-16.405548, -71.547966");
                        break;
                    case "307":
                        roomLocationMap.put(room, "-16.405548, -71.547966");
                        break;
                    case "308":
                        roomLocationMap.put(room, "Salon No existente");
                        break;
                    case "401":
                        roomLocationMap.put(room, "Salon No existente");
                        break;
                    case "402":
                        roomLocationMap.put(room, "Salon No existente");
                        break;
                    case "403":
                        roomLocationMap.put(room, "Salon No existente");
                        break;
                    case "404":
                        roomLocationMap.put(room, "Salon No existente");
                        break;
                    case "405":
                        roomLocationMap.put(room, "Salon No existente");
                        break;
                    case "406":
                        roomLocationMap.put(room, "Salon No existente");
                        break;
                    case "407":
                        roomLocationMap.put(room, "Salon No existente");
                        break;
                    case "408":
                        roomLocationMap.put(room, "Salon No existente");
                        break;
                    default:
                        roomLocationMap.put(room, "Ubicación desconocida");
                        break;
                }
            }
        } else if (buildingIndex == 12) { // Si se selecciona el Pabellón R
            for (String room : rooms) {
                switch (room) {
                    case "101":
                        roomLocationMap.put(room, "-16.405740572057827, -71.54762020445175");
                        break;
                    case "102":
                        roomLocationMap.put(room, "-16.405740572057827, -71.54762020445175");
                        break;
                    case "103":
                        roomLocationMap.put(room, "-16.405740572057827, -71.54762020445175");
                        break;
                    case "104":
                        roomLocationMap.put(room, "-16.405740572057827, -71.54762020445175");
                        break;
                    case "105":
                        roomLocationMap.put(room, "-16.405740572057827, -71.54762020445175");
                        break;
                    case "106":
                        roomLocationMap.put(room, "-16.405740572057827, -71.54762020445175");
                        break;
                    case "107":
                        roomLocationMap.put(room, "-16.405740572057827, -71.54762020445175");
                        break;
                    case "108":
                        roomLocationMap.put(room, "-16.405740572057827, -71.54762020445175");
                        break;
                    case "201":
                        roomLocationMap.put(room, "-16.405740572057827, -71.54762020445175");
                        break;
                    case "202":
                        roomLocationMap.put(room, "-16.405740572057827, -71.54762020445175");
                        break;
                    case "203":
                        roomLocationMap.put(room, "-16.405740572057827, -71.54762020445175");
                        break;
                    case "204":
                        roomLocationMap.put(room, "-16.405740572057827, -71.54762020445175");
                        break;
                    case "205":
                        roomLocationMap.put(room, "-16.405740572057827, -71.54762020445175");
                        break;
                    case "206":
                        roomLocationMap.put(room, "-16.405740572057827, -71.54762020445175");
                        break;
                    case "207":
                        roomLocationMap.put(room, "-16.405740572057827, -71.54762020445175");
                        break;
                    case "208":
                        roomLocationMap.put(room, "-16.405740572057827, -71.54762020445175");
                        break;
                    case "301":
                        roomLocationMap.put(room, "-16.405740572057827, -71.54762020445175");
                        break;
                    case "302":
                        roomLocationMap.put(room, "-16.405740572057827, -71.54762020445175");
                        break;
                    case "303":
                        roomLocationMap.put(room, "-16.405740572057827, -71.54762020445175");
                        break;
                    case "304":
                        roomLocationMap.put(room, "-16.405740572057827, -71.54762020445175");
                        break;
                    case "305":
                        roomLocationMap.put(room, "-16.405740572057827, -71.54762020445175");
                        break;
                    case "306":
                        roomLocationMap.put(room, "-16.405740572057827, -71.54762020445175");
                        break;
                    case "307":
                        roomLocationMap.put(room, "-16.405740572057827, -71.54762020445175");
                        break;
                    case "308":
                        roomLocationMap.put(room, "-16.405740572057827, -71.54762020445175");
                        break;
                    case "401":
                        roomLocationMap.put(room, "-16.405740572057827, -71.54762020445175");
                        break;
                    case "402":
                        roomLocationMap.put(room, "-16.405740572057827, -71.54762020445175");
                        break;
                    case "403":
                        roomLocationMap.put(room, "-16.405740572057827, -71.54762020445175");
                        break;
                    case "404":
                        roomLocationMap.put(room, "-16.405740572057827, -71.54762020445175");
                        break;
                    case "405":
                        roomLocationMap.put(room, "-16.405740572057827, -71.54762020445175");
                        break;
                    case "406":
                        roomLocationMap.put(room, "-16.405740572057827, -71.54762020445175");
                        break;
                    case "407":
                        roomLocationMap.put(room, "-16.405740572057827, -71.54762020445175");
                        break;
                    case "408":
                        roomLocationMap.put(room, "-16.405740572057827, -71.54762020445175");
                        break;
                    default:
                        roomLocationMap.put(room, "Ubicación desconocida");
                        break;
                }
            }
        } else if (buildingIndex == 13) { // Si se selecciona el Pabellón S
            for (String room : rooms) {
                switch (room) {
                    case "101":
                        roomLocationMap.put(room, "-16.405986066913268, -71.54800968428434");
                        break;
                    case "102":
                        roomLocationMap.put(room, "-16.405986066913268, -71.54800968428434");
                        break;
                    case "103":
                        roomLocationMap.put(room, "-16.405986066913268, -71.54800968428434");
                        break;
                    case "104":
                        roomLocationMap.put(room, "-16.405986066913268, -71.54800968428434");
                        break;
                    case "105":
                        roomLocationMap.put(room, "-16.405986066913268, -71.54800968428434");
                        break;
                    case "106":
                        roomLocationMap.put(room, "-16.405986066913268, -71.54800968428434");
                        break;
                    case "107":
                        roomLocationMap.put(room, "-16.405986066913268, -71.54800968428434");
                        break;
                    case "108":
                        roomLocationMap.put(room, "-16.405986066913268, -71.54800968428434");
                        break;
                    case "201":
                        roomLocationMap.put(room, "-16.405986066913268, -71.54800968428434");
                        break;
                    case "202":
                        roomLocationMap.put(room, "-16.405986066913268, -71.54800968428434");
                        break;
                    case "203":
                        roomLocationMap.put(room, "-16.405986066913268, -71.54800968428434");
                        break;
                    case "204":
                        roomLocationMap.put(room, "-16.405986066913268, -71.54800968428434");
                        break;
                    case "205":
                        roomLocationMap.put(room, "-16.405986066913268, -71.54800968428434");
                        break;
                    case "206":
                        roomLocationMap.put(room, "-16.405986066913268, -71.54800968428434");
                        break;
                    case "207":
                        roomLocationMap.put(room, "-16.405986066913268, -71.54800968428434");
                        break;
                    case "208":
                        roomLocationMap.put(room, "-16.405986066913268, -71.54800968428434");
                        break;
                    case "301":
                        roomLocationMap.put(room, "-16.405986066913268, -71.54800968428434");
                        break;
                    case "302":
                        roomLocationMap.put(room, "-16.405986066913268, -71.54800968428434");
                        break;
                    case "303":
                        roomLocationMap.put(room, "-16.405986066913268, -71.54800968428434");
                        break;
                    case "304":
                        roomLocationMap.put(room, "-16.405986066913268, -71.54800968428434");
                        break;
                    case "305":
                        roomLocationMap.put(room, "-16.405986066913268, -71.54800968428434");
                        break;
                    case "306":
                        roomLocationMap.put(room, "-16.405986066913268, -71.54800968428434");
                        break;
                    case "307":
                        roomLocationMap.put(room, "-16.405986066913268, -71.54800968428434");
                        break;
                    case "308":
                        roomLocationMap.put(room, "-16.405986066913268, -71.54800968428434");
                        break;
                    case "401":
                        roomLocationMap.put(room, "-16.405986066913268, -71.54800968428434");
                        break;
                    case "402":
                        roomLocationMap.put(room, "-16.405986066913268, -71.54800968428434");
                        break;
                    case "403":
                        roomLocationMap.put(room, "-16.405986066913268, -71.54800968428434");
                        break;
                    case "404":
                        roomLocationMap.put(room, "-16.405986066913268, -71.54800968428434");
                        break;
                    case "405":
                        roomLocationMap.put(room, "-16.405986066913268, -71.54800968428434");
                        break;
                    case "406":
                        roomLocationMap.put(room, "-16.405986066913268, -71.54800968428434");
                        break;
                    case "407":
                        roomLocationMap.put(room, "-16.405986066913268, -71.54800968428434");
                        break;
                    case "408":
                        roomLocationMap.put(room, "-16.405986066913268, -71.54800968428434");
                        break;
                    default:
                        roomLocationMap.put(room, "Ubicación desconocida");
                        break;
                }
            }
        } else {

        }
    }
    private void handleTaskSubmission(String date, ArrayAdapter<String> adapter) {
        if (taskInput.getText().toString().isEmpty()) {
            taskInput.setError("Task cannot be empty");
            return;
        }

        // Actualiza la tarea con los nuevos valores ingresados
        updateTaskDetails();

        // Guarda la tarea inicial y programa las tareas repetitivas
        if (t.getId() != 0) {
            database.updateTask(t, date);
            Toast.makeText(this, "Task updated successfully", Toast.LENGTH_SHORT).show();
        } else {
            database.addTask(t, date);  // Guarda la tarea inicial
            scheduleRecurringTasks(t, date);  // Programa las repeticiones semanales
            Toast.makeText(this, "Task added and scheduled successfully", Toast.LENGTH_SHORT).show();
        }
        finish();
    }

    private void updateTaskDetails() {
        t.setTask(taskInput.getText().toString());
        t.setColor(color.getSelectedItem().toString());
        t.setProfessor(professorName.getText().toString());
        t.setBuilding(spinnerBuilding.getSelectedItem().toString());
        t.setRoom(spinnerRoom.getSelectedItem().toString());
        t.setUbicacion(Ubicacion.getText().toString());
        t.setTipoClase(spinnerTipoClase.getSelectedItem().toString());
        // No necesitas configurar 'from' y 'to' aquí ya que se configuran en el TimePicker
    }

    private void scheduleRecurringTasks(AppTask task, String initialDate) {
        LocalDate startDate = LocalDate.parse(initialDate);
        for (int i = 1; i <= 52; i++) {  // Repite por un año
            LocalDate nextDate = startDate.plusWeeks(i);
            AppTask newTask = new AppTask(task);  // Usando el constructor de copia
            database.addTask(newTask, nextDate.toString());
        }
    }
    private void handleTaskDeletion(String date) {
        if (t.getId() != 0) {
            database.deleteTask(String.valueOf(t.getId()), date);
            Toast.makeText(this, "Task deleted successfully", Toast.LENGTH_SHORT).show();
        }
        finish();
    }
    private void setupTimePicker() {
        TimePickerDialog.OnTimeSetListener fromTimeListener = (view, hourOfDay, minute) -> {
            String formattedTime = String.format("%02d:%02d", hourOfDay, minute);
            from.setText(formattedTime);
            t.setFrom(formattedTime);
        };

        TimePickerDialog.OnTimeSetListener toTimeListener = (view, hourOfDay, minute) -> {
            String formattedTime = String.format("%02d:%02d", hourOfDay, minute);
            to.setText(formattedTime);
            t.setTo(formattedTime);
        };

        from.setOnClickListener(v -> {
            LocalTime currentTime = LocalTime.now();
            new TimePickerDialog(this, fromTimeListener, currentTime.getHour(), currentTime.getMinute(), true).show();
        });

        to.setOnClickListener(v -> {
            LocalTime currentTime = LocalTime.now();
            new TimePickerDialog(this, toTimeListener, currentTime.getHour(), currentTime.getMinute(), true).show();
        });
    }
}