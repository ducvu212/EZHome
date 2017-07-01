package com.example.minhd.ezhome.interact;

/**
 * Created by ducnd on 5/21/17.
 */

public class AccountInteractor {
    private static AccountInteractor instance = new AccountInteractor();

    private AccountInteractor() {
        
    }

    public static AccountInteractor getInstance() {
        return instance;
    }


}
