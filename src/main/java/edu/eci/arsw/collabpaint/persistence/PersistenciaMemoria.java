/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.collabpaint.persistence;

import edu.eci.arsw.collabpaint.model.Point;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

/**
 *
 * @author danie
 */

@Service
public class PersistenciaMemoria implements Persistencia{
    
    @Autowired
    SimpMessagingTemplate msgt;
    
    ConcurrentHashMap<String, ArrayList<Point>> polygons = new ConcurrentHashMap<>();
    
    @Override
    public void handlePointEvent(Point pt, String numdibujo) {
        if(polygons.containsKey(numdibujo)){
            polygons.get(numdibujo).add(pt);
            if(polygons.get(numdibujo).size()>=3){
                msgt.convertAndSend("/topic/newpolygon."+numdibujo, polygons.get(numdibujo));
            }
        }else{
            ArrayList list = new ArrayList<>();
            list.add(pt);
            polygons.put(numdibujo, list);                        
        }
    }
    
}