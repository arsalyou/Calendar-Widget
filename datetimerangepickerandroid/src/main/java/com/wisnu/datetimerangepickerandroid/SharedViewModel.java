package com.wisnu.datetimerangepickerandroid;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SharedViewModel extends ViewModel {
    private final MutableLiveData<String> selected = new MutableLiveData<String>();
    private final MutableLiveData<Boolean> dropDownState = new MutableLiveData<Boolean>();
    private final MutableLiveData<Integer> firstVisiblePosition = new MutableLiveData<Integer>();
    private final MutableLiveData<String> dropDownyear = new MutableLiveData<String>();

    public void select(String item) {
        selected.setValue(item);
    }

    public LiveData<String> getSelected() {
        return selected;
    }

    public void setDropDownState(Boolean state) {
        dropDownState.setValue(state);
    }

    public LiveData<Boolean> getState() {
        return dropDownState;
    }

    public void setFirstVisiblePosition(Integer position) {
        firstVisiblePosition.setValue(position);
    }

    public LiveData<Integer> getFirstVisiblePosition() {
        return firstVisiblePosition;
    }

    public void setDropDownyear(String year) {
        dropDownyear.setValue(year);
    }

    public LiveData<String> getDropDownyear() {
        return dropDownyear;
    }


}
