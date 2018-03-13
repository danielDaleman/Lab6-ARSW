/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.collabpaint.controller;

import edu.eci.arsw.collabpaint.model.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;

/**
 *
 * @author danie
 */
public class STOMPMessagesHandler {
    @Autowired
    SimpMessagingTemplate msgt;
    
    @MessageMapping("/app/newpoint.{numdibujo}")    
    public void handlePointEvent(Point pt,@DestinationVariable String numdibujo) throws Exception {
    	System.out.println("Nuevo punto recibido en el servidor!:"+pt);
	msgt.convertAndSend("/app/newpoint."+numdibujo, pt);
    }
}
