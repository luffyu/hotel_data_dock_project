/**
 * %s 替换符
 * @type {{sysRole: {add: string, edit: string, del: string, pageList: string}, sysPermissionDict: {add: string, edit: string, del: string, pageList: string}, sysUser: {add: string, edit: string, del: string, pageList: string}, sysMenu: {add: string, edit: string, del: string, pageList: string}, sysDept: {add: string, edit: string, del: string, pageList: string}}}
 */
const allUrl = {
  sysRole:{
    pageList:"/sys/role/list",
    add:"/sys/role/add",
    edit:"/sys/role/update/%s",
    del:"/sys/role/del/%s",
    info:"/sys/role/info/%s",
  },
  sysUser:{
    pageList:"/sys/user/list",
    add:"/sys/user/add",
    edit:"/sys/user/update/%s",
    del:"/sys/user/del/%s",
  },
  sysMenu:{
    pageList:"/sys/menu/tree",
    add:"/sys/menu/add",
    edit:"/sys/menu/update/%s",
    del:"/sys/menu/del/%s",
    info:"/sys/menu/info/%s",
    optionTree:"/sys/menu/option-tree",
  },
  sysDept:{
    pageList:"/sys/dept/tree",
    add:"/sys/dept/add",
    edit:"/sys/dept/update/%s",
    del:"/sys/dept/del/%s",
  },
  sysAuthorize:{
    pageList:"/auth/group/apply-tree",
    optionTree:"/auth/group/option-tree",
  },
  sysAuthorizeConfig:{
    viewInfo:"/auth/config/view-info",
    pageList:"/auth/config/list",
    add:"/auth/config/add",
    edit:"/auth/config/%s/update",
    del:"/auth/config/%s/del",
  }
};


export default {
  allUrl
}
