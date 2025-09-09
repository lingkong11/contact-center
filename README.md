## Call Center Demo (Spring Boot + FreeSWITCH xml_curl)

### Run

```bash
mvn spring-boot:run
```

### REST: Manage inbound routes

- Create route

```bash
curl -X POST http://localhost:8080/api/inbound-routes \
  -H 'Content-Type: application/json' \
  -d '{"didNumber":"1000","destinationType":"QUEUE","destinationValue":"support_queue"}'
```

- List routes

```bash
curl http://localhost:8080/api/inbound-routes
```

- Update route

```bash
curl -X PUT http://localhost:8080/api/inbound-routes/{id} \
  -H 'Content-Type: application/json' \
  -d '{"didNumber":"1000","destinationType":"USER","destinationValue":"1001"}'
```

- Delete route

```bash
curl -X DELETE http://localhost:8080/api/inbound-routes/{id}
```

### FreeSWITCH xml_curl

In `autoload_configs/xml_curl.conf.xml`:

```xml
<configuration name="xml_curl.conf" description="xml curl config">
  <bindings>
    <binding name="dialplan" value="http://127.0.0.1:8080/freeswitch/xml"/>
  </bindings>
</configuration>
```

Ensure `modules.conf.xml` has:

```xml
<load module="mod_xml_curl"/>
<load module="mod_callcenter"/>
```

Test inbound:

```bash
fs_cli -x 'originate sofia/internal/1000@your_fs_ip &park'
```

The service will answer and route based on the configured route.

[![License](https://img.shields.io/badge/license-Apache%202-4EB1BA.svg)](https://www.apache.org/licenses/LICENSE-2.0.html)

# 云呼叫中心

## 目标：构建一套百万级语音通信平台

## 支持功能点
* 支持sdk接入语音平台
* 支持webrtc
* 支持rest接口接入语音平台
* 支持http ivr接入
* 支持智能对话
* 任务外呼



## 流程图
   ![process](fs-api/src/main/resources/static/images/1.png)
   ![process](fs-api/src/main/resources/static/images/2.png)
   ![process](fs-api/src/main/resources/static/images/3.jpg)

## 前端展示
   ![process](fs-api/src/main/resources/static/images/4.png)
   ![process](fs-api/src/main/resources/static/images/5.png)
   ![process](fs-api/src/main/resources/static/images/6.png)

## 监控
   ![process](fs-api/src/main/resources/static/images/7.png)
   ![process](fs-api/src/main/resources/static/images/8.png)

---

### freeswitch build
```shell
cd freeswitch/debian

docker-compose up -d 

#进入freeswitch控制台
./fs_cli.sh
```
   ![process](fs-api/src/main/resources/static/images/9.png)



## 演示环境
   管理端地址：https://dev.voice9.com/admin  admin 12345678 \
   座席端地址：https://dev.voice9.com/agent  1001@test 12345678 \
   文档地址:  https://voice9.com/docs.html
   
## [申请使用](https://voice9-ai.feishu.cn/share/base/form/shrcnBTPDCb28PW42wVYEHw0Saf)
   ![process](fs-api/src/main/resources/static/images/10.png)
