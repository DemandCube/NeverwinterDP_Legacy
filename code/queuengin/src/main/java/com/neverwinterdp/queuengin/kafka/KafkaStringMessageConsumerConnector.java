package com.neverwinterdp.queuengin.kafka ;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.neverwinterdp.queuengin.StringMessageConsumerHandler;

import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import kafka.message.MessageAndMetadata;

public class KafkaStringMessageConsumerConnector {
  private  ConsumerConnector consumer;

  public KafkaStringMessageConsumerConnector(String group, String zkConnectUrls) {
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

  public void consume(String topic, StringMessageConsumerHandler handler, int numOfThreads) {
    Map<String, Integer> topicCountMap = new HashMap<String, Integer>();
    topicCountMap.put(topic, numOfThreads);
    Map<String, List<KafkaStream<byte[], byte[]>>> consumerMap = consumer.createMessageStreams(topicCountMap);
    List<KafkaStream<byte[], byte[]>> streams = consumerMap.get(topic);
    ExecutorService executor = Executors.newFixedThreadPool(numOfThreads);
    int threadNumber = 0;
    for (final KafkaStream<byte[], byte[]> stream : streams) {
      executor.submit(new MessageConsumer(handler, stream, threadNumber));
      threadNumber++;
    }
  }
  
  static public class MessageConsumer implements Runnable {
    private StringMessageConsumerHandler handler ;
    private KafkaStream<byte[], byte[]> stream;
    private int threadId;

    public MessageConsumer(StringMessageConsumerHandler handler, KafkaStream<byte[], byte[]> stream, int threadId) {
      this.handler = handler ;
      this.threadId = threadId;
      this.stream = stream;
    }

    public void run() {
      ConsumerIterator<byte[], byte[]> it = stream.iterator();
      while (it.hasNext()) {
        MessageAndMetadata<byte[], byte[]> data = it.next() ;
        String key = new String(data.key()) ;
        String message = new String(data.message()) ;
        handler.onMessage(key, message) ;
      }
    }
  }
}