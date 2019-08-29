create table post (
  post_id bigint(20) auto_increment,
  content varchar(1024) not null,
  created_at datetime not null default current_timestamp,
  member_id int(11) not null,
  primary key (post_id),
  foreign key (member_id) references member(member_id)
) DEFAULT CHARSET=utf8;
