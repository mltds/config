# config
分布式配置中心


* 启动时获取应用配置，可以替换 Spring 的 PlaceholderConfigurerSupport。
* 和 Spring 的 PlaceholderConfigurerSupport 做 merge。
* 应用运行时，动态修改配置可被应用监听，获取最新配置。

初心是写个玩的，算是造个轮子，目前是通过 zk 管理应用配置。



配置中心需求
 1. 能够托管所有配置项或配置文件
 2. 支持 KV配置、文件配置（例如公私钥）
 3. 支持 KV配置、文件配置 修改后回调
 4. 支持高可用（本地缓存、内存缓存、服务端发布不影响客户端，客户端接入监控、强行pull/push 等）
 5. 支持 KV配置 properties 格式注释
 6. 依赖轻量，与其他框架解耦（如 Spring 等）
 7. 支持Spring 接入，但要在 Spring bean init 前能够获取配置
 8. 支持多环境配置
 9. 支持修改版本记录，并记录修改人
 10. 支持权限，防止其他人修改 (-)
