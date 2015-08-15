#jfinal-rest
jfinal-rest是jfinal的轻量级RESTful扩展，使用非常方便，看了DEMO就可以用了。

2015-8-15: add annotations to indicates that the annotated method responds to requests by HTTP method type. power721

2015-8-15: support API annotation in method level. power721

Config示例代码：

```java
public class Config extends JFinalConfig {
    @Override
    public void configConstant(Constants me) {
        me.setDevMode(true);
        //设置默认渲染json
        me.setMainRenderFactory(new IMainRenderFactory() {
            @Override
            public Render getRender(String view) {
                return new JsonRender();
            }

            @Override
            public String getViewExtension() {
                return null;
            }
        });

        me.setErrorRenderFactory(new IErrorRenderFactory() {
            @Override
            public Render getRender(int errorCode, String view) {
                return new JsonErrorRender(errorCode);
            }
        });
    }

    @Override
    public void configRoute(Routes me) {
        //配置路由，三个参数：访问路径（API版本），jfinal路由对象，要扫描的包(包下加了API注解的controller会被扫描)
        RestKit.buildRoutes("/v1", me, "peak.v1");
    }

    @Override
    public void configPlugin(Plugins me) {
        //TODO 配置数据库等插件

    }

    @Override
    public void configInterceptor(Interceptors me) {

    }

    @Override
    public void configHandler(Handlers me) {
        //配置handler
        RestKit.buildHandler(me);
    }
}
```

Controller示例代码：

```java

@API("/tickets/:ticketId/messages")
public class MessageController extends Controller {
    /**
     * 获取单个数据或者列表
     */
    @GET
    public void index() {
        //路径里定义的参数变量，需要通过getAttr()方法获取
        String ticketId = getAttr("ticketId");
        String messageId = getPara();
        //GET /v1/tickets/xxxx/messages
        if (StrKit.isBlank(messageId)) {
            Page<Message> page = null;
            //TODO 分页查询message
            setAttr("error", 0);
            setAttr("data", page);
            return;
        }
        //GET /v1/tickets/xxxx/messages/xxxx
        Message message = null;
        //TODO 查询单个message数据
        setAttr("error", 0);
        setAttr("data", message);
    }

    /**
     * 添加新数据
     */
    @POST
    public void create() {
        //POST /v1/tickets/xxxx/messages
        String ticketId = getAttr("ticketId");
        Message message = getModel(Message.class);
        message.set("ticketId", ticketId);
        message.save();
        setAttr("error", 0);
        setAttr("id", message.getInt("id"));
    }

    /**
     * 部分更新数据
     */
    public void patch() {
        //PATCH /v1/tickets/xxxx/messages/xxxxx
        String messageId = getPara();
        Message message = Message.dao.findById(messageId);
        //TODO 为message设置各个要更新的属性
        message.update();
        setAttr("error", 0);
    }

    /**
     * 数据整体更新
     */
    @PUT
    public void update() {
        //PUT /v1/tickets/xxxx/messages/xxxxx
        int messageId = getParaToInt();
        Message message = getModel(Message.class);
        message.set("id", messageId);
        message.update();
        setAttr("error", 0);
    }

    /**
     * 删除数据
     */
    @DELETE
    public void remove() {
        //DELETE /v1/tickets/xxxx/messages/xxxxx
        String messageId = getPara();
        Message message = Message.dao.findById(messageId);
        message.delete();
        setAttr("error", 0);
    }

    @API(":messageId/status")
    // equals @API("/tickets/:ticketId/messages/:messageId/status")
    public void status() {
        // GET /v1/tickets/1/messages/5/status
        String ticketId = getAttr("ticketId");  // 1
        String messageId = getAttr("messageId");  // 5

        setAttr("status", true);
    }

    @API(":messageId/status")
    @POST
    public void createStatus() {
        setAttr("created", true);
    }

}

```

感谢各位的关注！在开始实践了restful很久后，jfinal-rest我本人并没有真正用起来，原因是我个人想法的转变。jfinal-rest仅作为jfinal在url上定义变量（/uri/{var}）的一种实现方式供参考和学习吧。

建议不要在url上定义变量，理由如下：1.像/tickets/:ticketId/messages/:messageId这样的路径定义，其实ticketId和messageId是重复的，可以做成/ticket/message/:messageId或者/message/messageId；2.路径上定义变量会进一步降低性能，没有实际测试过。

同时，也建议不要定义error状态码，直接把错误详细信息用返回，有些时候，调试错误还要根据状态码去文档中查询其代表的含义。例如：返回{error:40832}，可以设计成{ok:false,msg:"无效的操作"}。设计一堆状态码，还要写相关的参考文档，麻烦了好多。

推荐各位jfinal用户使用官方自带的restful拦截器，或者自己简单处理下，根据请求方法分配到不同的action。目前我自己的项目里用的是jfinal自带的RestfulInterceptor和自己扩展的CORSHandler。

如上所述，仅仅是建议，实际项目开发中，程序设计请根据实际情况自己斟酌。