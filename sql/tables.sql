-- auto-generated definition
create table user
(
    id           bigint auto_increment comment '主键'
        primary key,
    username     varchar(256)                       null comment '昵称',
    userAccount  varchar(256)                       null comment '登录账号',
    avatarUrl    varchar(1024)                      null comment '头像',
    gender       tinyint                            null comment '性别',
    userPassword varchar(512)                       not null comment '密码',
    phone        varchar(128)                       null comment '电话号码',
    email        varchar(512)                       null comment '邮箱',
    userStatus   int      default 0                 null comment '用户状态0-正常',
    createTime   datetime default CURRENT_TIMESTAMP null comment '创建时间',
    updateTime   datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP,
    isDelete     tinyint  default 0                 null comment '是否删除',
    userRole     int      default 0                 null comment '用户角色 0-普通用户，1-管理员',
    planetCode   varchar(512)                       null comment '星球编号'
)
    comment '用户表';

-- auto-generated definition
create table tag
(
    id         bigint auto_increment comment '主键'
        primary key,
    tagName    varchar(256)                       null comment '标签名称',
    userId     bigint                             null comment '用户id',
    parentId   bigint                             null comment '父标签id ',
    isParent   tinyint                            null comment '是否为父标签 0-不是，1-父标签',
    createTime datetime default CURRENT_TIMESTAMP null comment '创建时间',
    updateTime datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP,
    isDelete   tinyint  default 0                 null comment '是否删除',
    constraint unique_tagName
        unique (tagName)
)
    comment '标签表';

create index idx__userId
    on tag (userId);

