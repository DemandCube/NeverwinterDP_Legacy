
package com.neverwinterdp.queue.kafka;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

import com.neverwinterdp.queue.StringMessageConsumerHandler;

import kafka.api.FetchRequest;
import kafka.api.FetchRequestBuilder;
import kafka.javaapi.FetchResponse;
import kafka.javaapi.consumer.SimpleConsumer;
import kafka.javaapi.message.ByteBufferMessageSet;
import kafka.message.Message;
import kafka.message.MessageAndOffset;

public class KafkaMessageConsumer {
  final static Charset UTF8 = Charset.forName("UTF-8") ;
  final static int    BUFFER_SIZE = 64 * 1024;
  final static int    CONNECTION_TIMEOUT = 100000;

  private String clientId ;
  private SimpleConsumer consumer ;
  
  public KafkaMessageConsumer(String clientId, String hostname, int port) {
    this.clientId = clientId ;
    consumer = new SimpleConsumer(hostname, port, CONNECTION_TIMEOUT, BUFFER_SIZE, clientId);
  }

  public void consume(String topic, StringMessageConsumerHandler handler) {
    FetchRequest req = 
      new FetchRequestBuilder().
        clientId(clientId).
        addFetch(topic, 0, 0L, 100).
        build();
    
    FetchResponse fetchResponse = consumer.fetch(req);
    ByteBufferMessageSet messageSet = fetchResponse.messageSet(topic, 0) ;
    
    for(MessageAndOffset messageAndOffset: messageSet) {
      Message message = messageAndOffset.message() ;
      ByteBuffer keyBuffer = message.key() ;
      byte[] keyBytes = new byte[keyBuffer.limit()] ;
      keyBuffer.get(keyBytes);
      String key = new String(keyBytes, UTF8) ;
      
      ByteBuffer payload = message.payload();
      byte[] bytes = new byte[payload.limit()];
      payload.get(bytes);
      String messageStr = new String(bytes, UTF8) ;
      handler.onMessage(key, messageStr) ;
    }
  }
}
