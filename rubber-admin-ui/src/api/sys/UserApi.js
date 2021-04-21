import request from '@/utils/request';
import global from '@/utils/Global';


/**
 * 获取全部的角色信息
 *
 */
export  function getRoleList(params) {
  return request({
    url: global.rubberBasePath + '/sys/role/list',
    method: 'get',
    params: {
      'json':encodeURI(JSON.stringify(params))
    }
  })
}


/**
 * 获取部门的树形结构
 */
export  function getDeptTree() {
  return request({
    url: global.rubberBasePath + '/sys/dept/tree',
    method: 'get',
    params: {
      status:0
    }
  })
}

export  function getUserInfo(userId) {
  return request({
    url: global.rubberBasePath + '/sys/user/'+userId+'/info',
    method: 'get',
  })
}


export default {
  getRoleList,
  getDeptTree,
  getUserInfo
}
