#!/bin/bash
set -e

if [ ! -d ${JDX_DIR}/config ]; then
  echo "没有映射config配置目录给本容器，请先映射config配置目录...\n"
  exit 1
fi

if [ ! -s ${JDX_DIR}/config/application.yml ]; then
  echo "检测到config配置目录下不存在application.yml，请参考教程添加...\n"
  exit 1
fi

nohup java -jar ${IBOT_DIR}/app.jar >${IBOT_DIR}/logs/console/all.log 2>&1

exec "$@"
