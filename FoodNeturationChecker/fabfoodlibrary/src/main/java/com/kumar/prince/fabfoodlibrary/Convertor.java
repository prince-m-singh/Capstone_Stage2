package com.kumar.prince.fabfoodlibrary;

import android.arch.persistence.room.TypeConverter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by prince on 14/9/17.
 */

public class Convertor {
    @TypeConverter
    public List<String>  storedStringToLanguages(String value) {
        List<String> langs = Arrays.asList(value.split("\\s*,\\s*"));
        return langs;
    }

    @TypeConverter
    public String languagesToStoredString(List<String> cl) {
        String value = "";

        for (String lang :cl)
            value += lang + ",";

        return value;
    }
}