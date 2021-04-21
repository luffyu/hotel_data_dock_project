import request from '../../utils/request';

import { getToken } from '../../utils/auth'

import global from '@/utils/Global';

/**
 * 获取用户的基本信息和菜单信息
 */
export function getUserInfoAndMenus() {
  return request({
    url: global.rubberBasePath+'/u/info',
    method: 'get'
  })
}
