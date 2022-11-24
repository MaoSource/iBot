FROM registry.cn-hangzhou.aliyuncs.com/yiidii-hub/nginx-openjdk:v1
MAINTAINER Source <1134496928@qq.com>
# envs
ENV IBOT_DIR=/iBot

# 工作目录
WORKDIR ${IBOT_DIR}

# 后端
ADD ./target/iBot.jar ${IBOT_DIR}/app.jar
ADD ./docker/docker-entrypoint.sh ${IBOT_DIR}/
ADD ./src/main/resources/crt/public_key.der ${IBOT_DIR}/public_key.der

# 其他操作
RUN mkdir -p ${IBOT_DIR}/logs/console

# 入口文件
ENTRYPOINT ["sh", "docker-entrypoint.sh"]
	