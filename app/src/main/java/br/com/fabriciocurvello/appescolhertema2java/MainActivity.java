package br.com.fabriciocurvello.appescolhertema2java;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private static final String THEME_FILE_NAME = "theme_preference.txt";

    private TextView tvTema;
    private Button btTemaClaro;
    private Button btTemaEscuro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);

        // Aplicar o tema antes da tela ser criada
        applySavedTheme();

        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tvTema = findViewById(R.id.tv_tema);
        btTemaClaro = findViewById(R.id.bt_tema_claro);
        btTemaEscuro = findViewById(R.id.bt_tema_escuro);

        updateThemeStatusText();

        btTemaClaro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setAppTheme(AppCompatDelegate.MODE_NIGHT_NO);
                saveThemePreference("LIGHT");
                updateThemeStatusText();
            }
        });

        btTemaEscuro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setAppTheme(AppCompatDelegate.MODE_NIGHT_YES);
                saveThemePreference("DARK");
                updateThemeStatusText();
            }
        });

    } // fim do onCreate()

    private void applySavedTheme() {
        String theme = loadThemePreference();
        if ("DARK".equals(theme)) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else if ("LIGHT".equals(theme)) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        }
    }

    private void setAppTheme(int mode) {
        AppCompatDelegate.setDefaultNightMode(mode);
    }

    private void saveThemePreference(String theme) {
        File file = new File(getFilesDir(), THEME_FILE_NAME);
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(theme);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateThemeStatusText() {
        int currentMode = AppCompatDelegate.getDefaultNightMode();
        String currentTheme;
        switch (currentMode) {
            case AppCompatDelegate.MODE_NIGHT_YES:
                currentTheme = "Escuro";
                break;
            case AppCompatDelegate.MODE_NIGHT_NO:
                currentTheme = "Claro";
                break;
            default:
                currentTheme = "Padrão do Sistema";
                break;
        }
        tvTema.setText("Tema Atual: " + currentTheme);
    }

    private String loadThemePreference() {
        File file = new File(getFilesDir(), THEME_FILE_NAME);
        if (file.exists()) {
            try (FileReader reader = new FileReader(file)) {
                char[] buffer = new char[(int) file.length()];
                reader.read(buffer);
                return new String(buffer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }



}