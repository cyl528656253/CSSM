# CSSM
源于牛客网一个项目
# toutiao 中级项目
这是某著名的It求职网站的中级项目课，做的的是咨询头条的的功能。后端使用SpringBoot，MyBatis，前端使用Thymeleaf，中间使用了异步框架，数据库使用的MySQL与Redis。

该项目比较适合新手学习。

这个项目最后也集成了高级项目的一些优势，搜索方面使用了solr，外加使用邮件注册功能。
最后整个项目的功能是
  + 登陆
  + 分享新闻
  + 点赞  （使用redis）
  + 站内信
  + 评论功能
  + 搜索新闻功能
  
  
  
  
  效果图：
  ![ABC](https://i.loli.net/2019/03/09/5c838a6de414b.png)
  
  
  # 架构
  使用了SpringBoot，分了Controller、Service和Dao层。这部分业务主要是一些请求的接收，在service层为controller成提供数据的调用的接口，Dao层最后提供数据如何与数据库打交道。
  
  # 异步框架
  
  一般异步框架就是一个生产者消费者的模式。如下图：
  
  ![ABC](https://images2015.cnblogs.com/blog/993374/201705/993374-20170521092045963-1110739068.png) 
  大概意思就是：生产者负责数据的产生，它将数据放到内存中去（一般是一个队列），而消费者则负责处理内存中的数据，处理完成后，可以通过回调的方式进行响应。上面的图比较粗略，下面是具体的实现示意图：
  ![ABC](https://images2015.cnblogs.com/blog/993374/201705/993374-20170521092441588-1428652553.png) 
  
  
  
  ### 为什么要使用异步

　显然，在上面描述的思路中，我们大概可以知道什么时候应该使用异步框架：对相应速度要求比较高请求，但是该请求的相关业务操作允许一定的延迟。

　举个具体的例子：在一个社交网站中，很多时候会有点赞的操作，A给B点赞，一般来说会包含两个操作，第一个操作是告诉A点赞成功了，第二个操作是告诉B他被A点赞了；如果不采用异步的方式，那就需要在在这两个操作都完成后，才响应A说点赞成功，但是第二个操作显然会耗时很长（例如需要发邮件通知），所以不采用异步方式时A就会有这样一种感觉：怎么点个赞要等半天才响应的，什么垃圾系统！所以，这时候为了提高对A的相应速度，我们可以采用异步的方式：A点赞请求发出之后，程序不需要等到B收到A的点赞通知了，才告诉A说你点赞成功了，因为B收到A的点赞通知相对于A知道自己点赞成功来说，是允许延迟的。
    
  具体的理解可以根据代码和下这篇博文  (https://www.cnblogs.com/lcplcpjava/p/6884420.html)  
    
  总而言之，就是把要做的时间的信息存储起来，交给其他线程做。一般用于非主业务，这样非主业挂了，主业务还能继续运行。而把信息存储下来就涉及到序列化与反序列化。项目中是使用redis的list实现消费队列，序列化的信息也存在redis中。我们的消费队列可以使用RabbitMQ、Jafka/Kafka等等。
  
  
  
  ### 序列与反序列
  一、什么是序列化和反序列化

1、序列化：把对象转换为字节序列的过程称为对象的序列化。

2、反序列化：把字节序列恢复为对象的过程称为对象的反序列化。



二、什么时候会用到序列化和反序列化

1、把的内存中的对象状态保存到一个文件中或者数据库中时候；
2、用套接字在网络上传送对象的时候；

3、通过RMI传输对象



三、如何实现序列化

Java语言通过实现Serializable接口的方式实现序列化和反序列化




项目中的是使用JSONObject.toJSONString();  JSON.parseObject(); 使用JSON串形式进行的序列化与反序列化的。



 ### 写在最后
 
 以上是项目的难点，其他的代码主要是写业务逻辑，可以熟能生巧，希望喜欢。
    
  
  