package com.spoilerlog.zr_ootmm_log_searcher.helper;

import com.spoilerlog.zr_ootmm_log_searcher.dto.ItemList;

import java.util.ArrayList;

public class HelperUtility {

    public void handleAllItems(String item, String check, ItemList itemList){
        if (itemList.allItems.containsKey(item)){
            itemList.allItems.get(item).add(check);
        } else {
            ArrayList<String> checkList = new ArrayList<>();
            checkList.add(check);
            itemList.allItems.put(item, checkList);
        }
    }
}
