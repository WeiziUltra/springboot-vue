# springboot2-vue
*一款基于springboot2和vue的后台通用模板，没有多余的功能，只有 *权限管理* 和常用功能* 。前后端分离项目，代码通过90% *阿里巴巴编码规约扫描* 、 *findbugs*

## <a href="http://39.96.52.201/vuepress" target="_blank">说明文档</a>
   * 文档更新中。。。

## <a href="https://gitee.com/WeiziPlus/springboot2-vue3-old" target="_blank">原项目地址</a>

## 上手指南
以下指南将帮助你在本地机器上安装和运行该项目，进行开发和测试。关于如何将该项目部署到在线环境，请参考部署小节。
### 安装要求、步骤
   * 安装配置Java环境， *JDK1.8*
   * 安装 *mysql 8* ，创建数据库(*utf8mb4*、*utf8mb4_general_ci*)导入sql(doc目录下)
   * 安装 *redis* ,下载安装即可
   * 开发工具需要安装 *lombok* 插件(开发工具推荐IDEA)
   * ---后端运行(如果出问题一般是yml配置文件中数据源之类的配置出错)
   * 安装配置 *node* 环境
   * 安装 *vue-cli* ,进入vue目录执行`npm install`
   * ---前端运行`npm run serve`( *WebStorm* 或者 *IDEA* 可以直接点击 *package.json* 文件中第6行左边绿三角)

### 演示地址
   * <a href="http://39.96.52.201" target="_blank">在线演示地址</a>
        * 用户名:superadmin  
        * 密码:111111  
        * *tip:多个用户同时登陆可能会被顶掉*
        * *tip:演示环境已经禁止增删改操作,报错忽视即可*
        * *tip:如果有其他异常，请强制刷新页面(有可能是缓存问题)*
   * <a href="http://39.96.52.201/github/doc.html" target="_blank">接口文档</a>
        * *swagger-bootstrap*
        * *tips:请求会提示404，请在请求前面手动添加/github*

## 部署
   * springboot目录下运行`mvn clean package`命令打包,打包后生成文件在/target/build目录下
        * config目录为存放的配置文件
        * lib目录为maven依赖的jar包
        * static目录存放静态文件
        * jar文件为生成的jar包(以后pom依赖不变的话可以只替换该jar包)
        * 将*common*公共模块的*yml*文件手动复制到*config*目录下
   * vue目录下运行`npm run build`命令打包，打包后生成文件在/dist目录下
        * 打包配置在 *.env* 文件和 *vue.config.js* 文件中
   * 部署服务器上面需要配置 *JDK1.8* 、 *mysql 8* 、 *redis* 环境
   * jar包运行`nohup java -jar springboot.jar &`可以在后台运行并且将日志输出到当前目录下 *nohup.out* 文件
   * 部署服务器建议配置 *nginx* ,vue打包后放在nginx下,不配置可以放在 *jar* 包同一目录 *static* 下面
   
## 常见错误
   * `java.lang.RuntimeException: Cannot resolve classpath entry: E:\maven-repository\mysql\mysql-connector-java\8.0.15\mysql-connector-java-8.0.15.jar`
       * 出错: 根据数据库生成实体类
       * 解决: *resources/config/generator-config.xml* 第6行jar包路径改成自己的jar包路径
   * `npm install`
       * 出错: vue安装依赖出错，一般是 *node-sass*
       * 解决: 更换淘宝镜像或者`npm install --save node-sass --registry=https://registry.npm.taobao.org --disturl=https://npm.taobao.org/dist --sass-binary-site=http://npm.taobao.org/mirrors/node-sass`   

## 鸣谢
   首先感谢 *springboot* 、 *vue* 、*element-ui* 等优秀的开源项目  
   其次该项目参考了很多网上的示例，如果看到相似的代码，**那么，答案只有一个了**