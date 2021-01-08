package org.suai.network;

import javax.swing.*;
import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * List of commands:
 *      canICreate - Client's request
 *      yesYouCanCreate - Server's response
 *      noYouCannotCreate - Server's response
 *
 *      canIRegistered - Account's request
 *      yesYouCanRegistered
 *      noYouCannotRegistered
 *
 *      sendToAllUpdateImage - Canvas's request
 *      updateImage - Server's response to all
 *      yourImage - Server's response to Account
 *
 *      newImage - Client's or Server's message
 *
 *      sendMeImagesArray - Client's request
 *      imagesArray - Server's response
 *
 *      sendMeMyAcc - Client's request
 *      yourAcc - Server's response
 *
 *      iAddAccess - Account's message
 *      iRemoveAccess - Account's message
 *
 *      canIPainting - Client's request
 *      yesYouCanPainting - Server's response
 *      noYouCannotPainting - Server's response
 */

public class Message implements Serializable {

    private String command;
    private String userName;
    private String password;
    private String imageName;
    private ImageIcon image;
    private HashMap<String, ImageIcon> imagesArray = new HashMap<>();
    private ArrayList<String> accesses = new ArrayList<>();
    private String access;

    public Message(String command) {
        this.command = command;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setAccess(String access) {
        this.access = access;
    }

    public String getAccess() {
        return access;
    }

    public void setAccesses(ArrayList<String> accesses) {
        this.accesses = accesses;
    }

    public ArrayList<String> getAccesses() {
        return accesses;
    }

    public void setImage(String imageName, Image image) {
        this.imageName = imageName;
        if (image == null) {
            this.image = null;
        } else {
            this.image = new ImageIcon(image);
        }
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public void putInImagesArray(String imageName, Image image) {

        if (image == null) {
            imagesArray.put(imageName, null);
        } else {
            imagesArray.put(imageName, new ImageIcon(image));
        }

    }

    public HashMap<String, ImageIcon> getImagesArray() {
        return imagesArray;
    }

    public String getCommand() {
        return command;
    }

    public Image getImage() {

        if (image == null) {
            return null;
        } else {
            return image.getImage();
        }
    }

    public String getImageName() {
        return imageName;
    }
}
