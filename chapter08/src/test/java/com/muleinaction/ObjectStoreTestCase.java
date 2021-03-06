
package com.muleinaction;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assume.assumeNotNull;
import static org.junit.Assume.assumeTrue;

import org.apache.commons.lang.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.mule.api.MuleMessage;
import org.mule.module.client.MuleClient;
import org.mule.tck.junit4.FunctionalTestCase;

import redis.clients.jedis.Jedis;

public class ObjectStoreTestCase extends FunctionalTestCase
{
	private static final String REDIS_HOST = "localhost";
	
	private Jedis jedis;
	
	public ObjectStoreTestCase()
    {
        setStartContext(false);
    }
	
    @Override
    protected String getConfigResources()
    {
        return "object-store.xml";
    }
    
    @Before
    public void doSetup() throws Exception 
    {
    	jedis = new Jedis(REDIS_HOST);
    	
    	try {
    		jedis.connect();
    	} catch (Exception e){
    	}

    	// Tests should not fail if Redis is not reachable
    	assumeNotNull(jedis);
    	assumeTrue(jedis.isConnected());
    	
    	setStartContext(true);
    	setUpMuleContext();
    }

    @Test
    public void testObjectStore() throws Exception
    {
        MuleClient muleClient = new MuleClient(muleContext);
        MuleMessage result = muleClient.send("vm://objectStoreBasedRequest", StringUtils.EMPTY, null);
        assertThat(result.getPayload(), is(instanceOf(String.class)));
    }
    
}

