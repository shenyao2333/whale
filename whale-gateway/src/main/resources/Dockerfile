#V1.0   Dockerfile文件编写
# 基础镜像
FROM openjdk:8-jdk-alpine
# 维护者信息
MAINTAINER 1040676712@qq.com
#数据卷
VOLUME /whale-gateway
#解决时间
ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime \
&&  echo $TZ > /etc/timezone\

#创建启动脚本
&& touch /etc/init.d/start.sh \
&& chmod +x /etc/init.d/start.sh \
&& echo "#!/bin/sh  " >> /etc/init.d/start.sh\
&& echo "java -jar /entfrm-web.jar" >> /etc/init.d/start.sh
#添加应用
ADD entfrm-web/target/whale-gateway-0.0.1.jar whale-gateway.jar
#开放端口
EXPOSE  8000
#启动
ENTRYPOINT /bin/sh -c   /etc/init.d/start.sh