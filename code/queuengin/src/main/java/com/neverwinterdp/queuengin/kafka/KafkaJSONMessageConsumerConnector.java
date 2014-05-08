package com.neverwinterdp.queue.kafka ;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import kafka.message.MessageAndMetadata;

import com.neverwinterdp.queue.JSONMessage;
import com.neverwinterdp.queue.JSONMessageConsumerConnector;
import com.neverwinterdp.queue.JSONMessageConsumerHandler;
import com.neverwinterdp.util.JSONSerializer;

public class KafkaJSONMessageConsumerConnector<T> implements JSONMessageConsumerConnector<T>{
  private  ConsumerConnector consumer;

  public KafkaJSONMessageConsumerConnector(String group, String zkConnectUrls) {
    Properties props = new Properties();
    props.put("group.id", group);
    props.put("zookeeper.connect", zkConnectUrls);
    props.put("zookeeper.session.timeout.ms", "400");
    props.put("zookeeper.sync.time.ms", "200");
    props.put("auto.commit.interval.ms", "1000");
    props.put("auto.offset.reset", "smallest");

    ConsumerConfig config = new ConsumerConfig(props);
    consumer = kafka.consumer.Consumer.createJavaConsumerConnector(config);
  }

  public void consume(String topic, JSONMessageConsumerHandler<T> handler, int numOfThreads) throws IOException {
    Map<String, Integer> topicCountMap = new HashMap<String, Integer>();
    topicCountMap.put(topic, numOfThreads);
    Map<String, List<KafkaStream<byte[], byte[]>>> consumerMap = consumer.createMessageStreams(topicCountMap);
    List<KafkaStream<byte[], byte[]>> streams = consumerMap.get(topic);
    ExecutorService executor = Executors.newFixedThreadPool(numOfThreads);
    int threadNumber = 0;
    for (final KafkaStream<byte[], byte[]> stream : streams) {
      executor.submit(new MessageConsumer<T>(handler, stream, threadNumber));
      threadNumber++;
    }
  }
  
  static public class MessageConsumer<T> implements Runnable {
    private JSONMessageConsumerHandler<T> handler ;
    private KafkaStream<byte[], byte[]> stream;
    private int threadId;

    public MessageConsumer(JSONMessageConsumerHandler<T> handler, KafkaStream<byte[], byte[]> stream, int threadId) {
      this.handler = handler ;
      this.stream = stream;
      this.threadId = threadId;
    }

    public void run() {
      ConsumerIterator<byte[], byte[]> it = stream.iterator();
      while (it.hasNext()) {
        try {
          MessageAndMetadata<byte[], byte[]> data = it.next() ;
          byte[] mBytes = data.message() ;
          JSONMessage<T> jsonMessage = 
            (JSONMessage<T>)JSONSerializer.INSTANCE.fromBytes(mBytes, JSONMessage.class);
          handler.onJSONMessage(jsonMessage) ;
        } catch (IOException e) {
          //TODO: use log4j or any log wrapper
          e.printStackTrace();
        }
      }
    }
  }
}