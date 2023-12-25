package com.example.fitnotes.set;

public interface SetItemChangeListener {
    void onRepValueChanged(int position, int newValue);
    void onWeightValueChanged(int position, double newValue);
}
