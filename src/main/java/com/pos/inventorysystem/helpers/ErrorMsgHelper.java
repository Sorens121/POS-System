package com.pos.inventorysystem.helpers;

import java.util.Objects;

public class ErrorMsgHelper {
    public static void catchError(String error) {
        DialogMsgHelper helper = new DialogMsgHelper();
        System.out.println("Error from MYSql server: "+ error);
        if(error.equals("1062")){
            helper.displayDialogMsg(8);
        }
    }

}
