/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.collabpaint;

import edu.eci.arsw.collabpaint.model.Point;
import edu.eci.arsw.collabpaint.persistence.Persistencia;
import edu.eci.arsw.collabpaint.util.JedisUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;


/**
 *
 * @author danie
 */
@Controller
public class STOMPMessagesHandler {
    
    @Autowired
    Persistencia msgt;
    

    @MessageMapping("/newpoint.{numdibujo}")    
    public void handlePointEvent(Point pt, @DestinationVariable String numdibujo) throws Exception {                
        System.out.println("SI ENTRO");
        msgt.handlePointEvent(pt, numdibujo);
    }
}
