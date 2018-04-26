/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.collabpaint.persistence;

import edu.eci.arsw.collabpaint.model.Point;
import edu.eci.arsw.collabpaint.util.JedisUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

/**
 *
 * @author danie
 */
public class PersistenciaRedis implements Persistencia {

    @Override
    public void handlePointEvent(Point pt, String numdibujo) {
        Jedis jedis = JedisUtil.getPool().getResource();
        
        jedis.getClient().setTimeoutInfinite();
        jedis.watch("X", "Y");
        
        Transaction tx = jedis.multi();
        tx.rpush("X", String.valueOf(pt.getX()));
        tx.rpush("Y", String.valueOf(pt.getY()));
        
        jedis.close();
    }
    
}
