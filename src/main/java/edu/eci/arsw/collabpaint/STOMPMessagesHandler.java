/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.collabpaint;

import edu.eci.arsw.collabpaint.model.Point;
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
    SimpMessagingTemplate msgt;
    ConcurrentHashMap<String, ArrayList<Point>> polygons = new ConcurrentHashMap<>();

    @MessageMapping("/newpoint.{numdibujo}")    
    public void handlePointEvent(Point pt, @DestinationVariable String numdibujo) throws Exception {        
        //Operaciones
        try (Jedis jedis = JedisUtil.getPool().getResource()) {
            //Operaciones
            Transaction tx = jedis.multi();
            tx.watch("X", "Y");
            tx.rpush("X", String.valueOf(pt.getX()));
            tx.rpush("Y", String.valueOf(pt.getY()));
            List<Object> res = tx.exec();
            System.out.println(res.size() + "  TAMAÑO RES");
            /**if(polygons.containsKey(numdibujo)){
            polygons.get(numdibujo).add(pt);
            if(polygons.get(numdibujo).size()>=3){
            msgt.convertAndSend("/topic/newpolygon."+numdibujo, polygons.get(numdibujo));
            }
            }else{
            ArrayList list = new ArrayList<>();
            list.add(pt);
            polygons.put(numdibujo, list);
            }**/                        
        }
                        
    }
}
