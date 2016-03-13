package com.cs121.project.trollie;

/**
 * Created by ivanalvarado on 3/13/16.
 */
public class ListElement {
    ListElement() {};

    ListElement(String tl, String bl, String gl) {
        message = tl;
        nickname = bl;
        gravity = gl;
    }

    public String message;
    public String nickname;  //Restaurant details button. Currently not implemented
    public String gravity;
}