# 基于WEB的车辆违章管理系统

参考项目 [teclan-SpringBoot](https://github.com/teclan/teclan-SpringBoot)



# 环境搭建

- Myeclispe|Eclipse|Idea 

  三者选择自己熟悉的工具即可，要求必须支持Maven
  
- ActiveMQ（可后续搭建，没有也可以启动项目）
  
  [官网地址](http://activemq.apache.org/),下载最新的压缩包（例如：apache-activemq-5.15.9-bin.zip），
  
  解压后，运行 `apache-activemq-5.9.1\bin\activemq.bat`，启动完成后，在浏览器中输入`http://localhost:8161/admin`,
  
  输入账号密码（账号密码默认均是：admin），ActiveMQ具体的使用方式请参阅网上资料。
  
  
# 项目运行

项目采用Maven管理，将项目以Maven形式导入IDE，等待IDE自动下载相关依赖即可，程序入口在`teclan.springboot.Main`文件中。

- 配置文件：application.yml
 
 ``` 
 # 数据库配置
 datasource:
   driver-class-name: com.mysql.cj.jdbc.Driver
   url: jdbc:mysql://rm-bp1k0ef85lmpf2821zo.mysql.rds.aliyuncs.com/cheliangweizhang?useUnicode=true&characterEncoding=UTF-8&useSSL=false&serverTimezone=UTC
   username: root
   password: Ali123!@#
 
 # mybatis 配置，暂时用不到
 mybatis:
   mapper-locations: classpath:mapper/*.xml
 
 # activemq 配置
 activemq:
   broker-url: tcp://localhost:61616
   user: admin
   password: admin
   pool:
     enabled: true
     max-connections: 5
 
 # 服务启动监听的端口，根目录是：http://localhost:8080
 server:
   port: 8080

 ```
   

  
  
  
  
  
