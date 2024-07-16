package com.peky.irrigation_app.ui.dashboard;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.card.MaterialCardView;
import com.peky.irrigation_app.databinding.FragmentDashboardBinding;

import java.util.ArrayList;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;
    private PieChart pieChart1;
    private PieChart pieChart2;
    private LineChart lineChart;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Inicialización de los gráficos de torta
        pieChart1 = binding.piechart1;
        pieChart2 = binding.piechart2;

        // Inicialización del gráfico de línea
        lineChart = binding.lineChart;

        // Configurar gráfico 1
        setupPieChart1();

        // Configurar gráfico 2
        setupPieChart2();

        // Configurar gráfico de línea
        setupLineChart();

        return root;
    }

    private void setupPieChart1() {
        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(30f, "Red"));
        entries.add(new PieEntry(20f, "Green"));
        entries.add(new PieEntry(50f, "Blue"));

        PieDataSet dataSet = new PieDataSet(entries, "Pie Chart 1");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        dataSet.setValueTextSize(12f);

        PieData data = new PieData(dataSet);
        pieChart1.setData(data);
        pieChart1.getDescription().setEnabled(false); // Deshabilita la descripción
        pieChart1.invalidate(); // Actualiza el gráfico
    }

    private void setupPieChart2() {
        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(40f, "A"));
        entries.add(new PieEntry(60f, "B"));

        PieDataSet dataSet = new PieDataSet(entries, "Pie Chart 2");
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        dataSet.setValueTextSize(12f);

        PieData data = new PieData(dataSet);
        pieChart2.setData(data);
        pieChart2.getDescription().setEnabled(false); // Deshabilita la descripción
        pieChart2.invalidate(); // Actualiza el gráfico
    }

    private void setupLineChart() {
        // Crear datos de ejemplo para el gráfico de línea
        ArrayList<Entry> entries = new ArrayList<>();
        entries.add(new Entry(0, 60));
        entries.add(new Entry(1, 50));
        entries.add(new Entry(2, 70));
        entries.add(new Entry(3, 30));
        entries.add(new Entry(4, 80));

        // Configurar dataset y línea
        LineDataSet dataSet = new LineDataSet(entries, "Line Chart Dataset");
        dataSet.setColor(Color.BLUE);
        dataSet.setValueTextSize(10f);

        // Configurar el gráfico de línea
        LineData lineData = new LineData(dataSet);
        lineChart.setData(lineData);
        lineChart.getDescription().setEnabled(false); // Deshabilita la descripción
        lineChart.invalidate(); // Actualiza el gráfico
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}