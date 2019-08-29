create table member (
  member_id int(11) auto_increment,
  nickname varchar(45) not null,
--  memberの識別子（外部に漏れてもいい）
  uuid varchar(55) not null,
  auth_user_id int(11) not null,
  created_at datetime not null default current_timestamp,
  updated_at datetime not null default current_timestamp,
  primary key (member_id),
  foreign key (auth_user_id) references auth_user(auth_user_id)
) DEFAULT CHARSET=utf8;
