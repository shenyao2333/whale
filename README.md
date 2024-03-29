# whale 
### 介绍
SpringCloudAlibaba的脚手架，想法是快速搭建一套完整可用的微服务器架构，所用技术点选用比较流行的可靠的方案。我将不断地完善中，这也是今年的目标。

### 服务说明    


|     模块 |    说明  |   端口号   |
| ---- | ---- | ---- |
|   whale-gateway   | 网关     |  8000   |
|   whale-oauth2   | 登录     | 8100 |
|   whale-business-system   | 业务模块-系统     | 9100 |
| whale-business-order | 业务模块-订单 | 9000 |
|   whale-generator-netty-server   |  netty服务端（暂时不用）  |        |
| whale-generator-es | 搜索系统 | 10060 |
| whale-business-workflow | 工作流 | 9030 |



### 使用技术

- [x] nacos注册中心

  [安装教程](https://blog.csdn.net/qq_39381892/article/details/113715094)     [官方文档](https://nacos.io/zh-cn/docs/quick-start.html)

- [x] oAuth2单点登录

- [x] oAuth2多种登录方式

  文档完善中....

- [x] SpringSecurity子模块配置
  [博客案例](https://blog.csdn.net/qq_39381892/article/details/108438599)

- [x] Gateway网关
  [介绍说明](https://blog.csdn.net/qq_39381892/article/details/108438599)

- [x] Gateway集成swagger

- [x] OpenFeign通信

- [x] Dubbo的RPC调用（还需完善）
    [官方文档](https://github.com/alibaba/spring-cloud-alibaba/blob/master/spring-cloud-alibaba-examples/spring-cloud-alibaba-com.whale.api.dubbo-examples/README_CN.md)

- [ ] Camunda工作流

- [ ] RocketMQ消息中间键

- [ ] xxl任务调度
    [官方文档](https://www.xuxueli.com/xxl-job)

- [ ] Sentiel限流

- [x] Ribbon负载均衡

- [x] Netty聊天

- [x] es做搜索引擎

- [x] Redis缓存

- [x] Redisson分布式锁

- [x] mybatis-plus

- [ ] 分布式事务Seata

- [x] 注解记录日志集成ELK

    注解记录日志，推送到Kafka，Logstash 存储到es里

- [x] 集成docker部署

- [ ] 设计模式

- [ ] vue
