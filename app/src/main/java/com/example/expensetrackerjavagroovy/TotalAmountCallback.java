package com.example.expensetrackerjavagroovy;

public interface TotalAmountCallback {
    void onSuccess();
    void onFailure(String errorMessage);
}
