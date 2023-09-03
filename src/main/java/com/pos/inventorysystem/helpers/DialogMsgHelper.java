package com.pos.inventorysystem.helpers;

public class DialogMsgHelper {
    private static final String SUCCESS_MSG = "Data saved successfully!";
    private static final String UPDATE_MSG = "Update was successful";
    private static final String ERROR_MSG = "Error while saving please try again!";
    private static final String DEFAULT_MSG = "Try again!";
    private static final String NO_SEARCH = "No results";
    private static final String SEARCH_MSG = "Results found";
    private static final String DELETE_MSG = "Item deleted successfully";
    private static final String DELETE_ERROR_MSG = "No items found";
    private static final String EMPTY_FIELDS = "Fields cannot be empty";
    private static final String DUPLICATE_ENTRY = "Duplicate Entry";

    public String displayDialogMsg(int state) {
        String message = "";
        switch (state) {
            case 0:
                message = ERROR_MSG;
                break;
            case 1:
                message = SUCCESS_MSG;
                break;
            case 2:
                message = UPDATE_MSG;
                break;
            case 3:
                message = NO_SEARCH;
                break;
            case 4:
                message = DELETE_MSG;
                break;
            case 5:
                message = DELETE_ERROR_MSG;
                break;
            case 6:
                message = SEARCH_MSG;
                break;
            case 7:
                message = EMPTY_FIELDS;
                break;
            case 8:
                message = DUPLICATE_ENTRY;
                break;
            default:
                message = DEFAULT_MSG;
        }

        return message;
    }
}
