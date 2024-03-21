package com.spoilerlog.zr_ootmm_log_searcher.service;

import com.spoilerlog.zr_ootmm_log_searcher.dto.ItemList;

import java.io.BufferedReader;
import java.io.File;

public interface SpoilerLogReader {
    ItemList processFile(BufferedReader reader);
}
