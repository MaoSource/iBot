```shell
_____________      _____ 
___(_)__  __ )_______  /_
__  /__  __  |  __ \  __/
_  / _  /_/ // /_/ / /_  
/_/  /_____/ \____/\__/  
```

_**iBot只写你想要**_

* 道聚城签到
* 支持QQ扫码登录
* 表情包制作
* 快手短视频解析（开发中）
* 京东查询（开发中）
* 待定...

## 安装说明

1.命令行安装

```shell
java -jar iBot.jar
```

2.本项目已打包成`docker`镜像，拉取配置即可使用
> docker安装方法不再赘述

### 1. 拉取并运行docker

创建配置配置文件目录
创建配置文件application.yml
```yaml
server:
  port: 83
spring:
  application:
    name: iBot
  redis:
    database: 0
    host: redis地址
    port: 6379
    lettuce:
      pool:
        min-idle: 8
        max-idle: 500
        max-active: 2000
        max-wait: 10000
    timeout: 5000
    password: redis密码
app-config:
  # OneBot Mirai地址
  url:
  # 管理员的QQ号
  admin-qq-id:
  # cqhttp token
  admin-qq-token:
  # 日志发送到哪个群 -1:不发送 -2:发送给管理员
  log-group-id:

tao_poly_city:
  headers: "{
      'User_Agent': 'PolyCity/4.4.2 (iPhone; iOS 15.1; Scale/3.00)',
      'Accept': 'application/json, text/plain, */*',
      'Content_Type': 'application/json;charset=UTF-8',
      'Connection': 'keep-alive',
      'Host': 'comm.ams.game.qq.com',
      'Accept-Encoding': 'gzip, deflate, br',
      'Accept-Language': 'zh-Hans-CN;q=1, en-CN;q=0.9',
      'Charset': 'UTF-8',

      'Referer': 'https://daoju.qq.com/index.shtml'
    }"
qq:
  app_id: 716027609
  daid: 5
  u1: https%3A%2F%2Fgraph.qq.com%2Foauth2.0%2Flogin_jump
  pt_3rd_aid: 101487368

```

```dockerfile
docker run -d \
    -v <config dir>:/iBot/config \
    -p <port>:83 \
    --restart=always \
    --name ibot ibot:<版本号>
```
> 这里命令自行替换卷和端口映射
>
> 例如：
> ```dockerfile
> docker run -d \
>   -v /source/iBot/config:/iBot/config \
>   -p 83:83 \
>   --restart=always \
>   --name ibot maosource/ibot:1.0.3
> ```
>
> 
## 使用方法
[![images.jpg](https://files.maosource.com/files/20221124/a8d3b88bc18c42a19aa11b787ce1f012.jpg)](https://files.maosource.com/files/20221124/a8d3b88bc18c42a19aa11b787ce1f012.jpg)

[![images.jpg](https://files.maosource.com/files/20221124/5fb6a7d7486e45f190f7047784695393.jpg)](https://files.maosource.com/files/20221124/5fb6a7d7486e45f190f7047784695393.jpg)

[![images.jpg](https://files.maosource.com/files/20221124/2322e4f647b945118301ad6b464ffcf5.jpg)](https://files.maosource.com/files/20221124/2322e4f647b945118301ad6b464ffcf5.jpg)

## 特别声明:

1. 本仓库涉及的任何解锁和解密分析脚本或代码，仅用于测试和学习研究，禁止用于商业用途，不能保证其合法性，准确性，完整性和有效性，请根据情况自行判断.
2. 请勿将本项目的任何内容用于商业或非法目的，否则后果自负.
3. 如果任何单位或个人认为该项目的脚本可能涉嫌侵犯其权利，则应及时通知并提供身份证明，所有权证明，我们将在收到认证文件后删除相关代码.
4. 任何以任何方式查看此项目的人或直接或间接使用本仓库项目的任何脚本的使用者都应仔细阅读此声明。source 保留随时更改或补充此免责声明的权利。一旦使用并复制了任何本仓库项目的规则，则视为您已接受此免责声明.
5. 您必须在下载后的24小时内从计算机或手机中完全删除以上内容.
6. 您使用或者复制了本仓库且本人制作的任何脚本，则视为已接受此声明，请仔细阅读

