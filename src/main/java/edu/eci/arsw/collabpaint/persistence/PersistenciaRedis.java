/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.collabpaint.persistence;

import edu.eci.arsw.collabpaint.model.Point;
import edu.eci.arsw.collabpaint.util.JedisUtil;
import java.util.ArrayList;
import java.util.List;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Response;
import redis.clients.jedis.Transaction;

/**
 *
 * @author danie
 */
public class PersistenciaRedis implements Persistencia {
    
     private static final String lua = "local xVal,yVal; \n if (redis.call('LLEN','X')==4) then \n"
                + "	xVal=redis.call('LRANGE','X',0,-1);\n yVal=redis.call('LRANGE','Y',0,-1);\n"
                + "	redis.call('DEL','X');\n redis.call('DEL','Y');\n return {xVal,yVal};\n"
                + "     else\n return {};\n end";
    
    
    @Override
    public void handlePointEvent(Point pt, String numdibujo) {
        Jedis jedis = JedisUtil.getPool().getResource();
        
        jedis.getClient().setTimeoutInfinite();
        jedis.watch("X", "Y");
        
        Transaction tx = jedis.multi();
        tx.rpush("X", String.valueOf(pt.getX()));
        tx.rpush("Y", String.valueOf(pt.getY()));
        
        Response<Object> luares = tx.eval(lua.getBytes(), 0, "0".getBytes());

        List<Object> resp = tx.exec();
        
        if (((ArrayList)luares.get()).size()==2){
            System.out.println(new String((byte[])((ArrayList)(((ArrayList)luares.get()).get(0))).get(0)));
}
        
        
        
        
        
        
        
        jedis.close();
    }
    
}
