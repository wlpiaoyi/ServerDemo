CREATE TABLE `sys_access` (
  `id` bigint NOT NULL,
  `value` varchar(64) COLLATE utf8mb4_bin NOT NULL,
  `path` varchar(255) COLLATE utf8mb4_bin NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='数据权限';

CREATE TABLE `sys_access_role` (
  `role_id` bigint NOT NULL,
  `access_id` bigint NOT NULL,
  PRIMARY KEY (`role_id`,`access_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

CREATE TABLE `sys_dept` (
  `id` bigint unsigned NOT NULL,
  `parent_id` bigint DEFAULT NULL,
  `name` varchar(32) COLLATE utf8mb4_bin NOT NULL,
  `code` varchar(32) COLLATE utf8mb4_bin NOT NULL COMMENT '部门编码',
  `status` int DEFAULT '1' COMMENT '状态',
  `is_deleted` int DEFAULT '0' COMMENT '是否删除',
  `create_dept` bigint DEFAULT NULL COMMENT '创建部门',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` bigint DEFAULT NULL COMMENT '修改人',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='部门';

CREATE TABLE `sys_dict` (
  `id` bigint unsigned NOT NULL,
  `parent_id` bigint DEFAULT NULL,
  `name` varchar(32) COLLATE utf8mb4_bin NOT NULL,
  `code` varchar(16) COLLATE utf8mb4_bin NOT NULL,
  `value` varchar(64) COLLATE utf8mb4_bin NOT NULL,
  `is_public` tinyint NOT NULL DEFAULT '1',
  `sort` int NOT NULL DEFAULT '0',
  `deep` int NOT NULL DEFAULT '0',
  `is_leaf` tinyint NOT NULL DEFAULT '1',
  `status` int DEFAULT '1' COMMENT '状态',
  `is_deleted` int DEFAULT '0' COMMENT '是否删除',
  `create_user` bigint DEFAULT NULL COMMENT '创建人',
  `create_dept` bigint DEFAULT NULL COMMENT '创建部门',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` bigint DEFAULT NULL COMMENT '修改人',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='数据字典';

CREATE TABLE `sys_dict_role` (
  `dict_id` bigint NOT NULL,
  `role_id` bigint NOT NULL,
  PRIMARY KEY (`dict_id`,`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

CREATE TABLE `sys_menu` (
  `id` int unsigned NOT NULL,
  `parent_id` bigint DEFAULT NULL,
  `name` varchar(32) COLLATE utf8mb4_bin NOT NULL COMMENT '菜单名称',
  `code` varchar(45) COLLATE utf8mb4_bin NOT NULL COMMENT '菜单编码',
  `action` varchar(256) COLLATE utf8mb4_bin DEFAULT NULL,
  `icon` varchar(32) COLLATE utf8mb4_bin DEFAULT NULL,
  `type` int NOT NULL COMMENT '菜单类型=(0:未知类型, 1:菜单, 2:按钮)',
  `status` int DEFAULT '1' COMMENT '状态',
  `is_deleted` int DEFAULT '0' COMMENT '是否删除',
  `create_user` bigint DEFAULT NULL COMMENT '创建人',
  `create_dept` bigint DEFAULT NULL COMMENT '创建部门',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` bigint DEFAULT NULL COMMENT '修改人',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='菜单';

CREATE TABLE `sys_menu_role` (
  `menu_id` bigint NOT NULL,
  `role_id` bigint NOT NULL,
  PRIMARY KEY (`menu_id`,`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

CREATE TABLE `sys_platform` (
  `id` bigint unsigned NOT NULL,
  `name` varchar(32) COLLATE utf8mb4_bin NOT NULL COMMENT '名称',
  `code` varchar(32) COLLATE utf8mb4_bin NOT NULL COMMENT '编码',
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='平台';

CREATE TABLE `sys_role` (
  `id` int unsigned NOT NULL,
  `name` varchar(32) COLLATE utf8mb4_bin DEFAULT NULL,
  `code` varchar(32) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '角色编码=(amdin:管理员)',
  `status` int DEFAULT '1' COMMENT '状态',
  `is_deleted` int DEFAULT '0' COMMENT '是否删除',
  `create_user` bigint DEFAULT NULL COMMENT '创建人',
  `create_dept` bigint DEFAULT NULL COMMENT '创建部门',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` bigint DEFAULT NULL COMMENT '修改人',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  UNIQUE KEY `name_UNIQUE` (`name`),
  UNIQUE KEY `code_UNIQUE` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='角色';

CREATE TABLE `sys_user` (
  `id` bigint unsigned NOT NULL,
  `account` varchar(32) COLLATE utf8mb4_bin NOT NULL,
  `password` varchar(32) COLLATE utf8mb4_bin NOT NULL,
  `dept_id` bigint NOT NULL,
  `status` int DEFAULT '1' COMMENT '状态',
  `is_deleted` int DEFAULT '0' COMMENT '是否删除',
  `create_user` bigint DEFAULT NULL COMMENT '创建人',
  `create_dept` bigint DEFAULT NULL COMMENT '创建部门',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` bigint DEFAULT NULL COMMENT '修改人',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='用户表';

CREATE TABLE `sys_user_role` (
  `role_id` bigint NOT NULL,
  `user_id` bigint NOT NULL,
  PRIMARY KEY (`role_id`,`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='用户角色';
