alter table room_contrast_config add origin_price  double not null default 0 COMMENT '数据源的价格';
alter table room_contrast_config add float_price double not null default 0 COMMENT '浮动的价格';
alter table room_contrast_config add push_price double not null default 0 COMMENT '推送的价格';
alter table room_contrast_config add float_type varchar(50) not null default 'add' COMMENT 'add表示新增 reduce表示减少';





alter table hotel_room_sync_exec_water add origin_price  double not null default 0 COMMENT '数据源的价格';
alter table hotel_room_sync_exec_water add float_price double not null default 0 COMMENT '浮动的价格';
alter table hotel_room_sync_exec_water add float_type varchar(50) not null default 'add' COMMENT 'add表示新增 reduce表示减少';
