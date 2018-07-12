DELETE FROM role_info;
DELETE FROM user_info;
DELETE FROM group_tree;
DELETE FROM menu_info;
commit;
select setval('seq_role_info', 2, false);
select setval('seq_user_info', 2, false);
select setval('seq_group_tree', 2, false);
INSERT into group_tree(id,name,parent_id,type,status) VALUES(1,'医院',0,0,0);
INSERT INTO "public"."role_info" VALUES ('1', '管理员', '2016-01-01 00:00:00', null, '1,11,1101,1102,1103,12,1201,1202,1203,1204,13,1301,1302,1303,14,1401,1402,15,1501,1502,1503,1504,16,', '0', null, null, '2017-06-07 15:08:46.134');
INSERT INTO "public"."role_info" VALUES ('11', 'sys Tester', '2015-05-01 12:00:00', '', '1,12,1201,1202,1203,1204,', '0', null, '2017-04-06 16:31:18.95', '2017-06-07 14:57:06.795');
INSERT INTO "public"."role_info" VALUES ('21', '普通用户', '2015-05-01 12:00:00', '', '1,11,1101,1102,1103,12,1201,1202,1203,1204,16,', '0', null, '2017-06-07 15:15:43.357', '2017-06-08 10:11:04.029');
INSERT INTO "public"."role_info" VALUES ('31', '院感负责人', '2015-05-01 12:00:00', '', '1,11,1101,1102,1103,12,1201,1202,1203,1204,13,1301,1302,1303,14,1401,1402,15,1501,1504,16,', '0', null, '2017-06-07 15:16:55.616', '2017-06-07 15:38:59.077');
INSERT INTO "public"."role_info" VALUES ('41', '科室院感负责人', '2015-05-01 12:00:00', '', '1,11,1101,1102,1103,12,1201,1202,1203,1204,13,1301,1302,', '0', null, '2017-06-08 10:13:18.47', '2017-06-08 10:13:51.792');
INSERT into user_info(id,name,PASSWORD,user_status,status,login_counts,role_id,depart_Ids,group_id) VALUES(1,'admin','827ccb0eea8a706c4c34a16891f84e7b',0,0,0,1,1,1);

-- ----------------------------
-- Records of menu_info
-- ----------------------------
INSERT INTO "public"."menu_info" VALUES ('1', '进入系统', '0', 'NULL', null, '1', null, null, 'images/calendar.png', '1', '0', null, '2017-02-06 15:51:07', null);
INSERT INTO "public"."menu_info" VALUES ('11', '事件查询', '1', 'NULL', null, '0', null, null, 'images/icon_menu_event.png', '0', '0', null, '2017-02-28 14:56:33', null);
INSERT INTO "public"."menu_info" VALUES ('12', '统计分析', '1', 'NULL', null, '0', null, null, 'images/icon_menu_statistic.png', '0', '0', null, '2017-04-01 13:56:40', null);
INSERT INTO "public"."menu_info" VALUES ('13', '基本信息', '1', 'NULL', null, '0', null, null, 'images/icon_menu_info_base.png', '0', '0', null, '2017-04-02 13:56:44', null);
INSERT INTO "public"."menu_info" VALUES ('14', '实时状态', '1', 'NULL', null, '0', null, null, 'images/icon_menu_state_real_time.png', '0', '0', null, '2017-04-04 13:56:49', null);
INSERT INTO "public"."menu_info" VALUES ('15', '系统管理', '1', 'NULL', null, '0', null, null, 'images/icon_menu_sys_manage.png', '0', '0', null, '2017-03-05 13:56:53', null);
INSERT INTO "public"."menu_info" VALUES ('16', '帮助中心', '1', 'NULL', null, '0', null, null, 'images/icon_menu_help.png', '0', '0', null, '2017-04-04 13:56:59', null);
INSERT INTO "public"."menu_info" VALUES ('1101', '今日事件', '11', 'module/washHand/todayEventList.action', null, '0', null, null, null, '0', '1', null, '2017-05-11 13:01:21', null);
INSERT INTO "public"."menu_info" VALUES ('1102', '历史事件', '11', 'module/washHand/eventList.action', null, '0', null, null, '', '0', '1', null, '2017-05-11 13:02:33', null);
INSERT INTO "public"."menu_info" VALUES ('1103', '日志查询', '11', 'module/signalLog/list.action', null, '0', null, null, '', '0', '1', null, '2017-05-11 13:03:40', null);
INSERT INTO "public"."menu_info" VALUES ('1201', '人员依从率', '12', 'module/washHand/rateByStaffList.action', null, '0', null, null, '', '0', '0', null, '2017-05-12 13:01:51', null);
INSERT INTO "public"."menu_info" VALUES ('1202', '人员类别依从率', '12', 'module/washHand/rateByStaffTypeList.action', null, '0', null, null, '', '0', '0', null, '2017-05-12 13:02:00', null);
INSERT INTO "public"."menu_info" VALUES ('1203', '科室依从率', '12', 'module/washHand/rateByDepartList.action', null, '0', null, null, '', '0', '0', null, '2017-05-12 13:03:16', null);
INSERT INTO "public"."menu_info" VALUES ('1204', '手卫生时机依从率', '12', 'module/washHand/eventCompareList.action', null, '0', null, null, '', '0', '0', null, '2017-05-12 13:04:25', null);
INSERT INTO "public"."menu_info" VALUES ('1301', '职工信息', '13', 'module/staff/list.action', null, '0', null, null, '', '0', '0', null, '2017-05-13 13:03:05', null);
INSERT INTO "public"."menu_info" VALUES ('1302', '设备信息', '13', 'module/device/list.action', null, '0', null, null, '', '0', '0', null, '2017-05-13 13:02:58', null);
INSERT INTO "public"."menu_info" VALUES ('1303', '网关配置', '13', 'module/apSet/list.action', null, '0', null, null, '', '0', '0', null, '2017-05-13 13:01:51', null);
INSERT INTO "public"."menu_info" VALUES ('1401', '设备状态', '14', 'module/device/queryAlarmList.action', null, '0', null, null, '', '1', '0', null, '2017-05-15 14:01:25', null);
INSERT INTO "public"."menu_info" VALUES ('1402', '人员位置', '14', 'module/location/allStaffLocation.action', null, '0', null, null, '', '1', '0', null, '2017-05-15 14:01:25', null);
INSERT INTO "public"."menu_info" VALUES ('1501', '用户管理', '15', 'module/user/pageUser.action', null, '0', null, null, '', '0', '0', null, '2017-05-15 15:01:25', null);
INSERT INTO "public"."menu_info" VALUES ('1502', '角色管理', '15', 'module/role/list.action', null, '0', null, null, '', '0', '0', null, '2017-05-15 15:02:49', null);
INSERT INTO "public"."menu_info" VALUES ('1503', '登录日志', '15', 'module/log/logInfoList.action', null, '0', null, null, '', '0', '0', null, '2017-05-15 15:03:11', null);
INSERT INTO "public"."menu_info" VALUES ('1504', '感控沙龙', '15', 'module/notice/list.action', null, '0', null, null, '', '0', '0', null, '2017-05-15 15:04:19', null);