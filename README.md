# config
分布式配置中心


* 启动时获取应用配置，可以替换 Spring 的 PlaceholderConfigurerSupport。
* 和 Spring 的 PlaceholderConfigurerSupport 做 merge。
* 应用运行时，动态修改配置可被应用监听，获取最新配置。

初心是写个玩的，算是造个轮子，目前是通过 zk 管理应用配置。
