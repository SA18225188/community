# community

设计描述：
1. 使用Redis缓存热点数据，优化系统性能和实现Session共享
2. 使用Kafka实现异步发送消息，解耦生产者和消费者、缓存消息
3. 使用Elasticsearch实现全文搜索功能，提供实时的搜索服务
4. 使用ThreadLocal存储当前登陆用户，使用前缀树算法实现敏感词过滤功能

项目收获：通过该项目的开发，使我熟悉了相关技术栈的使用，学会了用⼀些开源技术进⼀步优化项目，⽐如使用Caffeine实
现本地缓存热⻔帖子，优化前后均使用JMeter模拟100个线程对50万条帖子进行压力测试，测试结果表明没加本地缓存之前TPS
为7.1/sec，加了本地缓存之后TPS为191.2/sec，TPS提高了接近27倍

测试结果：
http://lilong.cool/2020/04/13/%e9%a1%b9%e7%9b%ae%e6%9c%ac%e5%9c%b0%e7%bc%93%e5%ad%98%e5%89%8d%e5%90%8e%e5%8e%8b%e6%b5%8b%e7%bb%93%e6%9e%9c/

ps：之前压测redis也就是把TPS提高3到4倍，结果加了本地缓存TPS提高那么多，本地缓存真香！！！