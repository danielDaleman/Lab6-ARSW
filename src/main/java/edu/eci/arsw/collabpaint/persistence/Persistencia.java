/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.collabpaint.persistence;

import edu.eci.arsw.collabpaint.model.Point;
import org.springframework.messaging.handler.annotation.DestinationVariable;

/**
 *
 * @author danie
 */
public interface Persistencia {
    public void handlePointEvent(Point pt, String numdibujo);
}
