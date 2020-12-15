# common-query
通用查询组件是一个针对SQL语句的轻量级封装，他的主要特性是提供了完整的可配置的数据权限的数据查询接口。
这个包里面有四个目录。
commonquery-tokenuser-core		
	把tokenid翻译成userid的组件，运行在web controller层
commonquery-designer-core		
	这个项目本来设计是为设计器的后台，但后来使用webdw来做界面，目前为空
commonquery-api-core		
	通用查询组件的API定义和具体实现，包括
	1.不进行用户鉴权的通用查询组件
	2.进行用户鉴权和数据权限控制的通用查询组件
commonquery-web
	web层的测试程序和测试代码
  
# 安装方法
1。通过git下载源代码以后，打开commonquery-web项目，并把其他三个项目设置为依赖项目。等待其mvn更新下载jar包
2。安装mysql,创建一个空白的数据库，叫commondataquerydb
3. 执行commondataquerydb.sql脚本，创建并初始化数据库的数据表
4. 把commonqquery-web项目打包成war包，发布到tomcat上，这里需要注意修改spring配置文件里面的数据库配置
5. 打开地址 http://localhost/commonquery-web/ 可以看到有若干个连接，其中包括数据库设计，项目的设计文档，后台数据表的展示，以及数据访问的demo入口
6. 点击demo链接，并修改后台数据库的sql配置，来看具体的数据接口是如何通过配置的方式来提供访问支持的。

有任何问题，请联系：email:liujunsong@aliyun.com
