#V1.0   Dockerfile文件编写
# 基础镜像
FROM openjdk:8-jdk-alpine
# 维护者信息
MAINTAINER 1040676712@qq.com
#数据卷
VOLUME /whale-business-system
#解决时间
ENV TZ=Asia/Shanghai

#创建启动脚本
RUN touch /etc/init.d/start.sh \
&& chmod +x /etc/init.d/start.sh \
&& echo "#!/bin/sh  " >> /etc/init.d/start.sh\
&& echo "java -jar /whale-business-system/whale-business-system-0.0.1.jar" >> /etc/init.d/start.sh
#添加应用
ADD  target/whale-business-system-0.0.1.jar  /whale-business-system/whale-business-system-0.0.1.jar
#开放端口
EXPOSE  9200
#启动
ENTRYPOINT /bin/sh -c  /etc/init.d/start.sh