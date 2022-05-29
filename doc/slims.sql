create database slimsdb default character set utf8;
use slimsdb;
create table department
(
    id          varchar(3) primary key comment '编号  部门类型 X：行政 J：教学 S：系统管理',
    pid         varchar(3)  not null default 0 comment '上级编号',
    pids        varchar(30) not null default 0 comment '所有上级编号',
    name        varchar(30) not null comment '部门名称',
    sort        int unsigned comment '排序',
    create_date datetime default current_timestamp COMMENT '创建时间',
    update_date datetime COMMENT '更新时间',
    key idx_pid (pid),
    key idx_sort (sort)
) comment '部门';
insert into department(id, name, sort)
 VALUES ("S00","系统管理部","0"),
        ("X00","学生工作处","1"),
        ("J00","二级学院","2");
insert into department(ID, PID, PIDS, NAME, SORT)
VALUES ("J01","J00","J00","大数据与软件工程学院","0"),
       ("J02","J00","J00","商学院","1"),
       ("J03","J00","J00","教师教育学院","2"),
       ("J04","J00","J00","文化与传媒学院","3");
create table user
(
    id            bigint primary key auto_increment COMMENT 'id',
    name          varchar(20)        not null comment '姓名',
    account          char(12)      UNIQUE not null comment '账户',
    password      varchar(20)        not null comment '密码',
    department_id varchar(3)         not null comment '所属部门',
    phone         varchar(11)        not null comment '手机号',
    gender        tinyint unsigned default 2 COMMENT '性别   0：男   1：女    2：保密',
    super_admin   tinyint unsigned default 0 COMMENT '超级管理员   0：否   1：是',
    status        tinyint default 1 COMMENT '状态  0：停用   1：正常',
    create_date   datetime default current_timestamp COMMENT '创建时间',
    update_date   datetime COMMENT '更新时间',
    KEY status_key(status),
    key idx_create_date (create_date)
) comment '用户';
insert into user(id,name,account, password, department_id, phone, gender, super_admin, status)
VALUES ("1","张钧","202009512221","123456","S00","18977491704","0","1","1"),
       ("2","张三","101009512221","123456","X00","18977491704","0","0","1"),
       ("3","李四","101009512222","123456","J01","18977491704","0","0","1"),
       ("4","王五","101009512223","123456","J02","18977491704","0","0","1"),
       ("5","赵六","101009512224","123456","J03","18977491704","0","0","1"),
       ("6","钱七","101009512225","123456","J04","18977491704","1","0","1"),
       ("7","吕八","101009512226","123456","J01","18977491704","0","0","1"),
       ("8","柯旭","101009512227","123456","J01","18977491704","1","0","1"),
       ("9","萨达","101009512228","123456","J01","18977491704","0","0","1"),
       ("10","千欧","101009512229","123456","J02","18977491704","0","0","1"),
       ("11","希斯","101009512230","123456","J02","18977491704","1","0","1"),
       ("12","萨瓦","101009512231","123456","J02","18977491704","0","0","0"),
       ("13","宋线","101009512232","123456","J03","18977491704","0","0","1"),
       ("14","斯基","101009512233","123456","J03","18977491704","1","0","1"),
       ("15","吴静","101009512234","123456","J03","18977491704","0","0","0"),
       ("16","王晓","101009512235","123456","J04","18977491704","0","0","1"),
       ("17","贺知","101009512236","123456","J04","18977491704","1","0","1"),
       ("18","薛者","101009512237","123456","J04","18977491704","0","0","0"),
       ("19","王晓","101009512238","123456","X00","18977491704","0","0","1"),
       ("20","贺知","101009512239","123456","X00","18977491704","1","0","1"),
       ("21","薛者","101009512240","123456","X00","18977491704","0","0","0");
create table role
(
    id            varchar(3) primary key COMMENT 'id',
    name          varchar(50) COMMENT '角色名称',
    remark        varchar(100) COMMENT '备注',
    create_date   datetime default current_timestamp COMMENT '创建时间',
    update_date   datetime default current_timestamp COMMENT '更新时间'
) COMMENT '角色';
insert into role(id, name, remark)
VALUES ("0","系统管理处","系统管理处只有一个管理员"),
       ("1_1","二级学院","二级学院"),
       ("1_2","学生工作处","学生工作处"),
       ("2_1","学院管理员","每个学院只有一个管理员"),
       ("2_2","学工处管理员","学工处只有一个管理员"),
       ("3_1","学院辅导员","学院辅导员"),
       ("3_2","学工处工作人员","学工处工作人员"),
       ("4_1","学生","学生");
create table role_user_group
(
    id          bigint primary key auto_increment COMMENT 'id',
    role_id     varchar(3) COMMENT '角色ID',
    user_id     bigint COMMENT '用户ID',
    create_date datetime default current_timestamp COMMENT '创建时间',
    key idx_role_id (role_id),
    key idx_user_id (user_id)
) COMMENT '角色用户关系';
insert into role_user_group( role_id, user_id)
value("0","1"),("2_2","2"),("3_2","20"),("3_2","21"),
    ("2_1","3"),("2_1","4"),("2_1","5"),("2_1","6"),
    ("3_2","19"),("3_1","7"),("4_1","8"),("4_1","9"),
    ("3_1","10"),("4_1","11"),("4_1","12"),("3_1","13"),
    ("4_1","14"),("4_1","15"),("3_1","16"),("4_1","17"),
    ("4_1","18");
create table menu
(
    id          bigint primary key auto_increment COMMENT 'id',
    name        varchar(200) COMMENT '名称',
    type        tinyint unsigned COMMENT '类型   0：菜单   1：按钮',
    sort        int COMMENT '排序',
    create_date datetime default current_timestamp COMMENT '创建时间',
    update_date datetime default current_timestamp COMMENT '更新时间',
    key idx_sort (sort)
) COMMENT '菜单';
insert into menu(name, type, sort)
VALUES ("部门管理","0","0"),("班级管理","0","1"),("菜单管理","0","2"),("请假管理","0","3"),
       ("角色菜单关系","0","4"),("角色管理","0","5"),("用户角色管理","0","6"),("用户班级管理","0","7"),
       ("用户管理","0","8");
create table role_menu_group
(
    id          bigint primary key auto_increment COMMENT 'id',
    role_id     varchar(3) COMMENT '角色ID',
    menu_id     varchar(3) COMMENT '菜单ID',
    permissions varchar(1) comment '权限 4修改 2删除 1增加',
    create_date datetime default current_timestamp COMMENT '创建时间',
    key idx_role_id (role_id)
) ENGINE = InnoDB DEFAULT CHARACTER SET utf8mb4 COMMENT ='角色菜单关系';
# insert into role_menu_group(role_id, menu_id,permissions)
# value ("","",""),("","",""),("","",""),("","",""),("","",""),
#     ("","",""),("","",""),("","",""),("","",""),("","",""),
#     ("","",""),("","",""),("","",""),("","",""),("","",""),
#     ("","",""),("","",""),("","",""),("","",""),("","",""),
#     ("","",""),("","",""),("","",""),("","",""),("","",""),
#     ("","",""),("","",""),("","",""),("","",""),("","","");
create table clazz
(
    id            char(12) primary key COMMENT '编号',
    name          varchar(20) not null comment '班级名称',
    major         varchar(5)  not null comment '所属专业编号',
    grade         varchar(4)  not null comment '年级',
    department_id varchar(3)  not null comment '所属部门',
    key grade_key (grade),
    key major_key (major)
) comment '班级信息';
create table user_clazz_group
(
    id         bigint primary key auto_increment comment '编号',
    user_id    bigint   not null comment '用户编号',
    clazz_id   char(12) not null comment '班级编号',
    start_time datetime not null comment '开始时间',
    end_time   datetime not null comment '结束时间',
    key instructor_key (user_id),
    key clazz_key (clazz_id)
) comment '用户班级关系';
create table `leave`
(
    id                 bigint primary key auto_increment comment '单号',
    student_user_id    bigint        not null COMMENT '学生编号',
    instructor_user_id bigint        not null COMMENT '辅导员编号',
    type               char(1)       not null comment '请假类型  0：事假，1：病假',
    reason             varchar(255)  not null comment '请假原因',
    start_time         datetime      not null comment '起始时间',
    end_time           datetime      not null comment '借书时间',
    days               decimal(4, 2) not null comment '请假时长',
    create_time        datetime   default current_timestamp   not null comment '申请时间',
    key create_key (create_time)
) comment '请假条';