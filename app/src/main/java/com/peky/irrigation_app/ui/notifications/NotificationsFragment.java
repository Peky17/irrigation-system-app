package com.peky.irrigation_app.ui.notifications;

import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.peky.irrigation_app.R;
import com.peky.irrigation_app.databinding.FragmentNotificationsBinding;
import com.peky.irrigation_app.services.BluetoothService;
import com.peky.irrigation_app.services.DeviceCommandService;

import java.util.ArrayList;

public class NotificationsFragment extends Fragment {

    private BluetoothService bluetoothService;
    private FragmentNotificationsBinding binding;
    private TextView tvTimer1;
    private TextView tvTimer2;
    private PieChart progressBar1;
    private PieChart progressBar2;
    private CountDownTimer countDownTimer1;
    private CountDownTimer countDownTimer2;
    private long timeRemaining1 = 0; // Tiempo restante en milisegundos para Timer 1
    private long totalTime1 = 0; // Tiempo total en milisegundos para Timer 1
    private long timeRemaining2 = 0; // Tiempo restante en milisegundos para Timer 2
    private long totalTime2 = 0; // Tiempo total en milisegundos para Timer 2
    private boolean timerRunning1 = false;
    private boolean timerRunning2 = false;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        bluetoothService = BluetoothService.getInstance(requireContext());

        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        tvTimer1 = root.findViewById(R.id.tv_timer); // Ajusta el ID correspondiente
        tvTimer2 = root.findViewById(R.id.tv_timer2); // Ajusta el ID correspondiente
        progressBar1 = binding.progressBar; // Ajusta el ID correspondiente
        progressBar2 = binding.progressBar2; // Ajusta el ID correspondiente
        setupProgressBar(progressBar1);
        setupProgressBar(progressBar2);

        // Inicializar el tiempo para Timer 1 y Timer 2 (por ejemplo, 1 hora para cada uno)
        setTimeForTimer1(0, 2, 0); // Ajusta el tiempo para Timer 1
        setTimeForTimer2(1, 0, 0); // Ajusta el tiempo para Timer 2

        tvTimer1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimerInputDialog(1);
            }
        });

        tvTimer2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimerInputDialog(2);
            }
        });

        root.findViewById(R.id.btn_start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startStopTimer1();
            }
        });

        root.findViewById(R.id.btn_start2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startStopTimer2();
            }
        });

        root.findViewById(R.id.btn_pause).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pauseTimer1();
            }
        });

        root.findViewById(R.id.btn_pause2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pauseTimer2();
            }
        });

        root.findViewById(R.id.btn_reset).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetTimer1();
            }
        });

        root.findViewById(R.id.btn_reset2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetTimer2();
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        if (countDownTimer1 != null) {
            countDownTimer1.cancel();
        }
        if (countDownTimer2 != null) {
            countDownTimer2.cancel();
        }
    }

    private void setTimeForTimer1(int hours, int minutes, int seconds) {
        totalTime1 = (hours * 60 * 60 + minutes * 60 + seconds) * 1000;
        timeRemaining1 = totalTime1;
        updateTimerText1();
        updateProgressBar1();
    }

    private void setTimeForTimer2(int hours, int minutes, int seconds) {
        totalTime2 = (hours * 60 * 60 + minutes * 60 + seconds) * 1000;
        timeRemaining2 = totalTime2;
        updateTimerText2();
        updateProgressBar2();
    }

    private void startStopTimer1() {
        if (timerRunning1) {
            pauseTimer1();
            // Stop main actuator
            DeviceCommandService command = DeviceCommandService.MAIN_ACTUATOR_OFF;
            sendBluetoothCommand(command.getCommand(), "Main actuator turned off");
        } else {
            startTimer1();
            // Start main actuator
            DeviceCommandService command = DeviceCommandService.MAIN_ACTUATOR_ON;
            sendBluetoothCommand(command.getCommand(), "Main actuator turned on");
        }
    }

    private void startStopTimer2() {
        if (timerRunning2) {
            pauseTimer2();
            // Stop secondary actuator
            DeviceCommandService command = DeviceCommandService.SECONDARY_ACTUATOR_OFF;
            sendBluetoothCommand(command.getCommand(), "Secondary actuator turned off");
        } else {
            startTimer2();
            // Start secondary actuator
            DeviceCommandService command = DeviceCommandService.SECONDARY_ACTUATOR_ON;
            sendBluetoothCommand(command.getCommand(), "Secondary actuator turned on");
        }
    }

    private void startTimer1() {
        // Start main actuator
        DeviceCommandService command = DeviceCommandService.MAIN_ACTUATOR_ON;
        sendBluetoothCommand(command.getCommand(), "Main actuator turned on");
        countDownTimer1 = new CountDownTimer(timeRemaining1, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeRemaining1 = millisUntilFinished;
                updateTimerText1();
                updateProgressBar1();
            }

            @Override
            public void onFinish() {
                timerRunning1 = false;
                // Stop main actuator when timer1 is finished
                DeviceCommandService command = DeviceCommandService.MAIN_ACTUATOR_OFF;
                sendBluetoothCommand(command.getCommand(), "Main actuator turned off");
                // Reset timer1 when timer1 is finished
                resetTimer1();
            }
        }.start();
        timerRunning1 = true;
    }

    private void startTimer2() {
        // Start secondary actuator
        countDownTimer2 = new CountDownTimer(timeRemaining2, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeRemaining2 = millisUntilFinished;
                updateTimerText2();
                updateProgressBar2();
            }

            @Override
            public void onFinish() {
                timerRunning2 = false;
                // Stop secondary actuator when timer2 is finished
                DeviceCommandService command = DeviceCommandService.SECONDARY_ACTUATOR_OFF;
                sendBluetoothCommand(command.getCommand(), "Secondary actuator turned off");
                // Reset timer1 when timer1 is finished
                resetTimer2();
            }
        }.start();
        timerRunning2 = true;
    }

    private void pauseTimer1() {
        if (countDownTimer1 != null) {
            countDownTimer1.cancel();
            timerRunning1 = false;
            // Stop main actuator
            DeviceCommandService command = DeviceCommandService.MAIN_ACTUATOR_OFF;
            sendBluetoothCommand(command.getCommand(), "Main actuator turned off");
        }
    }

    private void pauseTimer2() {
        if (countDownTimer2 != null) {
            countDownTimer2.cancel();
            timerRunning2 = false;
            // Stop secondary actuator
            DeviceCommandService command = DeviceCommandService.SECONDARY_ACTUATOR_OFF;
            sendBluetoothCommand(command.getCommand(), "Secondary actuator turned off");
        }
    }

    private void resetTimer1() {
        if (countDownTimer1 != null) {
            countDownTimer1.cancel();
        }
        timeRemaining1 = totalTime1;
        updateTimerText1();
        updateProgressBar1();
        timerRunning1 = false;
        // Stop main actuator
        DeviceCommandService command = DeviceCommandService.MAIN_ACTUATOR_OFF;
        sendBluetoothCommand(command.getCommand(), "Main actuator turned off");
    }

    private void resetTimer2() {
        if (countDownTimer2 != null) {
            countDownTimer2.cancel();
        }
        timeRemaining2 = totalTime2;
        updateTimerText2();
        updateProgressBar2();
        timerRunning2 = false;
        // Stop secondary actuator
        DeviceCommandService command = DeviceCommandService.SECONDARY_ACTUATOR_OFF;
        sendBluetoothCommand(command.getCommand(), "Secondary actuator turned off");
    }

    private void updateTimerText1() {
        int hours = (int) (timeRemaining1 / (1000 * 60 * 60));
        int minutes = (int) (timeRemaining1 / (1000 * 60)) % 60;
        int seconds = (int) (timeRemaining1 / 1000) % 60;

        String timeLeftFormatted = String.format("%02d:%02d:%02d", hours, minutes, seconds);
        tvTimer1.setText(timeLeftFormatted);
    }

    private void updateTimerText2() {
        int hours = (int) (timeRemaining2 / (1000 * 60 * 60));
        int minutes = (int) (timeRemaining2 / (1000 * 60)) % 60;
        int seconds = (int) (timeRemaining2 / 1000) % 60;

        String timeLeftFormatted = String.format("%02d:%02d:%02d", hours, minutes, seconds);
        tvTimer2.setText(timeLeftFormatted);
    }

    private void updateProgressBar1() {
        float progress = (float) timeRemaining1 / totalTime1 * 100;

        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(progress, ""));
        entries.add(new PieEntry(100 - progress, ""));

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        dataSet.setDrawValues(false);

        PieData data = new PieData(dataSet);
        progressBar1.setData(data);
        progressBar1.invalidate();
    }

    private void updateProgressBar2() {
        float progress = (float) timeRemaining2 / totalTime2 * 100;

        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(progress, ""));
        entries.add(new PieEntry(100 - progress, ""));

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        dataSet.setDrawValues(false);

        PieData data = new PieData(dataSet);
        progressBar2.setData(data);
        progressBar2.invalidate();
    }

    private void setupProgressBar(PieChart progressBar) {
        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(100f, ""));
        entries.add(new PieEntry(0f, ""));

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        dataSet.setDrawValues(false);

        PieData data = new PieData(dataSet);
        progressBar.setData(data);
        progressBar.setHoleColor(Color.TRANSPARENT);
        progressBar.setHoleRadius(75f);
        progressBar.setTransparentCircleRadius(45f);
        progressBar.getDescription().setEnabled(false);
        progressBar.getLegend().setEnabled(false);
        progressBar.animateY(1000);
        progressBar.setTouchEnabled(false);
        progressBar.invalidate();
    }

    private void showTimerInputDialog(final int timerNumber) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_set_time, null);
        builder.setView(dialogView);

        TextView dialogTitle = dialogView.findViewById(R.id.tv_dialog_title);
        EditText etHours = dialogView.findViewById(R.id.et_hours);
        EditText etMinutes = dialogView.findViewById(R.id.et_minutes);
        EditText etSeconds = dialogView.findViewById(R.id.et_seconds);
        Button btnSetTime = dialogView.findViewById(R.id.btn_set_time);

        dialogTitle.setText("Set Timer " + timerNumber + " Time");

        AlertDialog alertDialog = builder.create();

        btnSetTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String hoursStr = etHours.getText().toString().trim();
                String minutesStr = etMinutes.getText().toString().trim();
                String secondsStr = etSeconds.getText().toString().trim();

                if (hoursStr.isEmpty() || minutesStr.isEmpty() || secondsStr.isEmpty()) {
                    Toast.makeText(getContext(),
                            "Please enter hours, minutes, and seconds",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                int hours = Integer.parseInt(hoursStr);
                int minutes = Integer.parseInt(minutesStr);
                int seconds = Integer.parseInt(secondsStr);

                if (timerNumber == 1) {
                    setTimeForTimer1(hours, minutes, seconds);
                    updateTimerText1();
                } else {
                    setTimeForTimer2(hours, minutes, seconds);
                    updateTimerText2();
                }

                alertDialog.dismiss();
            }
        });

        alertDialog.show();
    }

    private void sendBluetoothCommand(String btCommand, String message) {
        if (bluetoothService.isBluetoothEnabled()) {
            if (bluetoothService.isDeviceConnected()) {
                bluetoothService.sendData(btCommand, message);
            } else {
                Toast.makeText(requireContext(), "bluetooth is not connected", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(requireContext(), "Bluetooth is not enabled", Toast.LENGTH_SHORT).show();
        }
    }

}