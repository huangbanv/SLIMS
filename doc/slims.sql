DROP DATABASE IF EXISTS slimsdb;
create database slimsdb default character set utf8;
use slimsdb;
create table department
(
    id          varchar(3) primary key comment '编号  部门类型 X：行政 J：教学 S：系统管理',
    pid         varchar(3)  not null default 0 comment '上级编号',
    name        varchar(30) not null comment '部门名称',
    create_date datetime default current_timestamp COMMENT '创建时间',
    update_date datetime default current_timestamp COMMENT '更新时间',
    key idx_pid (pid)
) comment '部门';
insert into department(id, name)
 VALUES ("S00","系统管理部"),
        ("X00","学生工作处"),
        ("J00","二级学院");
insert into department(ID, PID, NAME)
VALUES ("J01","J00","大数据与软件工程学院"),
       ("J02","J00","商学院"),
       ("J03","J00","教师教育学院"),
       ("J04","J00","文化与传媒学院");
create table user
(
    id            bigint primary key auto_increment COMMENT 'id',
    name          varchar(20)        not null comment '姓名',
    account          char(12)      UNIQUE not null comment '账户',
    password      varchar(20)        not null comment '密码',
    department_id varchar(3)         not null comment '所属部门',
    phone         varchar(11)        not null comment '手机号',
    gender        tinyint unsigned default 2 COMMENT '性别   0：男   1：女    2：保密',
    status        tinyint default 1 COMMENT '状态  0：停用   1：正常',
    create_date   datetime default current_timestamp COMMENT '创建时间',
    update_date   datetime default current_timestamp COMMENT '更新时间',
    KEY status_key(status),
    key idx_create_date (create_date)
) comment '用户';
insert into user(id,name,account, password, department_id, phone, gender, status)
VALUES ("1","张钧","202009512221","123456","S00","18977491704","0","1"),
       ("2","张三","101009512221","123456","X00","18977491704","0","1"),
       ("3","李四","101009512222","123456","J01","18977491704","0","1"),
       ("4","王五","101009512223","123456","J02","18977491704","0","1"),
       ("5","赵六","101009512224","123456","J03","18977491704","0","1"),
       ("6","钱七","101009512225","123456","J04","18977491704","1","1"),
       ("7","吕八","101009512226","123456","J01","18977491704","0","1"),
       ("8","柯旭","101009512227","123456","J01","18977491704","1","1"),
       ("9","萨达","101009512228","123456","J01","18977491704","0","1"),
       ("10","千欧","101009512229","123456","J02","18977491704","0","1"),
       ("11","希斯","101009512230","123456","J02","18977491704","1","1"),
       ("12","萨瓦","101009512231","123456","J02","18977491704","0","1"),
       ("13","宋线","101009512232","123456","J03","18977491704","0","1"),
       ("14","斯基","101009512233","123456","J03","18977491704","1","1"),
       ("15","吴静","101009512234","123456","J03","18977491704","0","1"),
       ("16","王晓","101009512235","123456","J04","18977491704","0","1"),
       ("17","贺知","101009512236","123456","J04","18977491704","1","1"),
       ("18","薛者","101009512237","123456","J04","18977491704","0","1"),
       ("19","宇文","101009512238","123456","X00","18977491704","0","1"),
       ("20","王珊","101009512239","123456","X00","18977491704","1","1"),
       ("21","李梅","101009512240","123456","X00","18977491704","0","1"),
       ("22","许褚","101009512241","123456","J01","18977491704","0","1"),
       ("23","曹操","101009512242","123456","J01","18977491704","0","1"),
       ("24","刘备","101009512243","123456","J01","18977491704","1","1"),
       ("25","张宇","101009512244","123456","J01","18977491704","0","1"),
       ("26","张飞","101009512245","123456","J01","18977491704","0","1"),
       ("27","关羽","101009512246","123456","J02","18977491704","1","1"),
       ("28","孙权","101009512247","123456","J02","18977491704","0","1"),
       ("29","刘禅","101009512248","123456","J02","18977491704","0","1"),
       ("30","吴瑜","101009512249","123456","J02","18977491704","1","1"),
       ("31","孙策","101009512250","123456","J02","18977491704","0","1"),
       ("32","玄奘","101009512251","123456","J03","18977491704","0","1"),
       ("33","悟空","101009512252","123456","J03","18977491704","1","1"),
       ("34","八戒","101009512253","123456","J03","18977491704","0","1"),
       ("35","何须","101009512254","123456","J03","18977491704","0","1"),
       ("36","吴倩","101009512255","123456","J03","18977491704","0","1"),
       ("37","刘芒","101009512256","123456","J04","18977491704","1","1"),
       ("38","可汗","101009512257","123456","J04","18977491704","0","1"),
       ("39","徐成","101009512258","123456","J04","18977491704","0","1"),
       ("40","刘振","101009512259","123456","J04","18977491704","1","1"),
       ("41","岐山","101009512260","123456","J04","18977491704","0","1");
create table role
(
    id            varchar(3) primary key COMMENT 'id',
    name          varchar(50) COMMENT '角色名称',
    remark        varchar(100) COMMENT '备注',
    create_date   datetime default current_timestamp COMMENT '创建时间',
    update_date   datetime default current_timestamp COMMENT '更新时间'
) COMMENT '角色';
insert into role(id, name, remark)
VALUES ("0","系统管理员","系统管理处只有一个管理员"),
       ("1_1","二级学院管理员","每个学院只有一个管理员"),
       ("1_2","学工处管理员","学工处只有一个管理员"),
       ("2_1","学院辅导员","学院辅导员"),
       ("2_2","学工处工作人员","学工处工作人员"),
       ("3_1","学生","学生");
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
value
    ("0","1"),
    ("1_1","3"),("1_1","4"),("1_1","5"),("1_1","6"),
    ("1_2","2"),
    ("2_1","7"),("2_1","10"),("2_1","13"),("2_1","16"),
    ("2_2","20"),("2_2","21"),("2_2","19"),
    ("3_1","8"),("3_1","9"),("3_1","11"),("3_1","12"),("3_1","14"),
    ("3_1","15"),("3_1","17"),("3_1","18"),("3_1","22"),("3_1","23"),
    ("3_1","24"),("3_1","25"),("3_1","26"),("3_1","27"),("3_1","28"),
    ("3_1","29"),("3_1","30"),("3_1","31"),("3_1","32"),("3_1","33"),
    ("3_1","34"),("3_1","35"),("3_1","36"),("3_1","37"),("3_1","38"),
    ("3_1","39"),("3_1","40"),("3_1","41");
create table menu
(
    id          bigint primary key auto_increment COMMENT 'id',
    name        varchar(200) COMMENT '名称',
    menu_code   varchar(20) comment '菜单代码',
    create_date datetime default current_timestamp COMMENT '创建时间',
    update_date datetime default current_timestamp COMMENT '更新时间'
) COMMENT '菜单';
insert into menu(name,menu_code)
VALUES ("部门管理","department"),("班级管理","clazz"),("菜单管理","menu"),
       ("请假管理","leave"),("角色管理","role"),("用户管理","user"),
       ("数据分析","analysis"),("辅导员管理","instructor");
create table role_menu_group
(
    id          bigint primary key auto_increment COMMENT 'id',
    role_id     varchar(3) COMMENT '角色ID',
    menu_id     varchar(3) COMMENT '菜单ID',
    create_date datetime default current_timestamp COMMENT '创建时间',
    key idx_role_id (role_id)
) ENGINE = InnoDB DEFAULT CHARACTER SET utf8mb4 COMMENT ='角色菜单关系';
insert into role_menu_group(role_id, menu_id)
value
    ("0","1"),("0","2"),("0","3"),("0","4"),("0","5"),("0","6"),("0","7"),("0","8"),
    ("1_1","2"),("1_1","4"),("1_1","6"),("1_1","7"),("1_1","8"),
    ("1_2","6"),("1_2","7"),
    ("2_1","2"),("2_1","4"),("2_1","6"),("2_1","7"),
    ("2_2","7"),
    ("3_1","4");
create table clazz
(
    id            bigint primary key auto_increment COMMENT '编号',
    name          varchar(20) not null comment '班级名称',
    instructor_id bigint        not null COMMENT '辅导员编号',
    grade         varchar(4)  not null comment '年级',
    start_time char(7) not null comment '开始时间',
    end_time   char(7) not null comment '结束时间',
    key grade_key (grade)
) comment '班级信息';
insert into clazz(name, instructor_id, grade,start_time,end_time)
VALUES ("19软件工程1班","7","2019","2019-09","2023-06"),("20软件工程2班","7","2020","2020-09","2024-06"),("20数字与计算机科学1班","7","2020","2020-09","2024-06"),
       ("21电子商务1班","10","2021","2021-09","2025-06"),("18电子商务2班","10","2018","2018-09","2022-06"),("19电子商务1班","10","2019","2019-09","2023-06"),
       ("18小学教育1班","13","2018","2018-09","2022-06"),("20小学教育2班","13","2020","2020-09","2024-06"),("19小学教育1班","13","2019","2019-09","2023-06"),
       ("19新闻学1班","16","2019","2019-09","2023-06"),("20新闻学2班","16","2020","2020-09","2024-06"),("21新闻学1班","16","2021","2021-09","2025-06");
create table user_clazz_group
(
    id         bigint primary key auto_increment comment '编号',
    student_id    bigint        not null COMMENT '学生编号',
    clazz_id   char(12) not null comment '班级编号',
    key student_key (student_id ),
    key clazz_key (clazz_id)
) comment '用户班级关系';
insert into user_clazz_group(student_id, clazz_id)
VALUES ("8","1"),("9","1"),
       ("22","2"),("23","2"),
       ("24","3"),("25","3"),("26","3"),
       ("11","4"),("12","4"),
       ("27","5"),("28","5"),
       ("29","6"),("30","6"),("31","6"),
       ("14","7"),("15","7"),
       ("32","8"),("33","8"),
       ("34","9"),("35","9"),("36","9"),
       ("17","10"),("18","10"),
       ("37","11"),("38","11"),
       ("39","12"),("40","12"),("41","12");
create table `leave`
(
    id                 bigint primary key auto_increment comment '单号',
    student_id    bigint        not null COMMENT '学生编号',
    instructor_id bigint        not null COMMENT '辅导员编号',
    type               char(1)       not null comment '请假类型  0：事假，1：病假',
    reason             varchar(255)  not null comment '请假原因',
    status             char(1)       not null default 0 comment '状态 0：未批准 1：已批准 2：已拒绝 3：已取消 4：已销假',
    start_time         timestamp      not null comment '起始时间',
    end_time           timestamp      not null comment '结束时间',
    days               decimal(5, 3) not null comment '请假时长',
    create_time        datetime   default current_timestamp   not null comment '申请时间',
    key create_key (create_time)
) comment '请假条';