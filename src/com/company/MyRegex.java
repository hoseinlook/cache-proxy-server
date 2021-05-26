package com.company;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MyRegex {

    public String find(String str_pattern , String text){
        final Pattern pattern = Pattern.compile(str_pattern, Pattern.MULTILINE);
        final Matcher matcher = pattern.matcher(text);

        while (matcher.find()) {

            for (int i = 1; i <= matcher.groupCount(); i++) {
                return matcher.group(i).toString();
            }
        }
        return null;
    }
}

