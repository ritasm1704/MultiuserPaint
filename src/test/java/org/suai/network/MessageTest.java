package org.suai.network;

import org.junit.Test;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.Assert.*;

public class MessageTest {

    Message message = new Message("@command");

    @Test
    public void getAccess() {
        String access = "rita";
        message.setAccess(access);
        assertEquals(access, message.getAccess());
    }

    @Test
    public void getAccesses() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("rita");
        arrayList.add("mark");
        message.setAccesses(arrayList);
        assertEquals(arrayList, message.getAccesses());
    }

    @Test
    public void getUserName() {
        String username = "Rita";
        message.setUserName(username);
        assertEquals(username, message.getUserName());
    }

    @Test
    public void getPassword() {
        String password = "123";
        message.setPassword(password);
        assertEquals(password, message.getPassword());
    }

    @Test
    public void getImagesArray() {
        HashMap<String, ImageIcon> imagesArray = new HashMap<>();
        imagesArray.put("holst1", null);
        imagesArray.put("holst2", null);
        message.putInImagesArray("holst1", null);
        message.putInImagesArray("holst2", null);
        assertEquals(imagesArray, message.getImagesArray());
    }

    @Test
    public void getCommand() {
        assertEquals("@command", message.getCommand());
    }

    @Test
    public void getImage() {
        message.setImage("holst", null);
        assertEquals(null, message.getImage());
    }

    @Test
    public void getImageName() {
        message.setImageName("holst");
        assertEquals("holst", message.getImageName());
    }
}