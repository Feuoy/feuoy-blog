package com.feuoy.blog.vo;

/**
 * 关于实体类，分为两种PO、VO。
 * PO主要是用于实现持久层的数据存储。
 * VO则主要用于逻辑层和表示层之间数据处理封装。
 *
 * PO字段跟数据库的表的列之间一一对应，而VO则根据业务需求，封装特定字段信息。
 * PO对象跟VO对象可以是多对多关系。
 *
 * PO对象跟VO对象之间的数据传递主要在逻辑层进行。
 * */

/**
 * View Object（VO）
 * Business Object（BO）
 * Persistence Object（PO）
 * 他们分别是三层结构的显示层、业务逻辑层和存储层内部使用的数据结构，它们还有一个统称，叫做数据对象Data Object（DO）。
 * 我们也可以把VO，BO和PO看成是DO在不同阶段的不同表示形态。
 * 当一个DO从显示层开始穿越整个系统的时候，它的形态和结构就开始变化，从VO转变到BO，最终到PO。
 * 但是这个过程不一定是可逆的，这个过程如果反向，从PO->BO->VO，很可能就对应不同的对象了。
 * 比如当输入错误的时候，回馈页面可能就需要增加一个错误信息提示。
 * 虽然实际使用的时候，我们经常会忽略这种细微的差异性，实际上这个错误信息，只对显示层有意义。
 * */

/**
 * VO：VO（View Object） 通常是请求处理层传输的对象，它通过 Spring 框架的转换后，往往是一个 JSON 对象。
 * BO：BO（Business Object），它是业务逻辑层封装业务逻辑的对象，一般情况下，它是聚合了多个数据源的复合对象。
 * DO：DO（Data Object）与数据库表结构一一对应，通过 DAO 层向上传输数据源对象。
 * DTO：DTO（Data Transfer Object）是远程调用对象，它是 RPC 服务提供的领域模型。
 * */

/**
 * vo层，value object值对象，不是实体也不是视图，它是中间转换的一个层
 * */

/*后台管理的博客管理，分类查询vo*/
public class BlogQuery {

    private String title;   //查询标题
    private Long typeId;    //查询分类id
    private boolean recommend;  //查询推荐布尔

    public BlogQuery() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

    public boolean isRecommend() {
        return recommend;
    }

    public void setRecommend(boolean recommend) {
        this.recommend = recommend;
    }

}
