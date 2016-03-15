package com.cs121.project.trollie;

/**
 * Created by ivanalvarado on 3/13/16.
 */
public class ListElement {
    ListElement() {};

    ListElement(String tl, String bl, String gl, String x, String y, String z) {
        message = tl;
        nickname = bl;
        gravity = gl;
        age_18_21 = x;
        age_22_25 = y;
        age_26_30 = z;
    }

    public String message;
    public String nickname;  //Restaurant details button. Currently not implemented
    public String gravity;
    public String age_18_21;
    public String age_22_25;
    public String age_26_30;
}