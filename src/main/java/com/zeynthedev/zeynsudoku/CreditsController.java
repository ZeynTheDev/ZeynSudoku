/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.zeynthedev.zeynsudoku;

import java.io.IOException;
import java.net.URI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;

/**
 * FXML Controller class
 *
 * @author Zeyn
 */
public class CreditsController{

    /**
     * Initializes the controller class.
     */ 
    
    @FXML
    private void actBack() throws IOException {
        App.setRoot("secondary");
    }
    
    @FXML
    private void actOpenLink(ActionEvent event) {
        Node clickedNode = (Node) event.getSource();
        
        String url = clickedNode.getUserData().toString();
        
        try {
            java.awt.Desktop.getDesktop().browse(new URI(url));
//            System.out.println("Opening link: " + url);
        } catch  (Exception e) {
            
        }
    }
}
