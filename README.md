easypre-client
=======
**download**
本客户端可在maven库[maven center](https://search.maven.org/search?q=a:easypre-client)直接下载使用。
```
<dependency>
    <groupId>com.easypre</groupId>
    <artifactId>easypre-client</artifactId>
    <version>1.0.2</version>
</dependency>
```

请移步 https://easypre.com/help/easypre/start 查看

**Features**
- 应用监控
 - 秒级推送消息，及时感知
 - 应用消息推送
 - 灵活配置推送方式
 - 支持钉钉机器人、企业微信机器人、邮件方式推送
 - 秒级推送消息，及时感知
 
**Usage Examples**
**construct**

无代理模式
```Java
// appKey和appSecret需登录EasyPre.com后，在【控制台】-【我的应用】- 【设置】-【应用信息】查看获取。
EasyPre.init(new EasyPreConfig("pro","appKey","appSecret"));
```

代理模式
'''Java
// appKey和appSecret需登录EasyPre.com后，在【控制台】-【我的应用】- 【设置】-【应用信息】查看获取。
EasyPreConfig easyPreConfig=new EasyPreConfig("pro","appKey","appSecret");
// 代理ip和端口全部配置才生效
easyPreConfig.setProxyIp("**.**.**.**");
easyPreConfig.setProxyPort(8888);
EasyPre.init(easyPreConfig);
'''

**useage**

tag template
```Java
Map<String,Object> params=Maps.newHashMap();
params.put("userName","张三");
params.put("code","10086");
params.put("expireMiniutes",10);
EasyPre.eventTemplate("register","xxxx@xx.com",params);
```

event
```Java
/**
* 普通事件
*
* @param content 内容
* @param params  参数
*/
EasyPre.event(String content, final Object... params)
```
