import request from '@/utils/request';
import global from '@/utils/Global';


export  function queryList(query) {
  return request({
    url: global.rubberBasePath + '/sys/permission/list',
    method: 'get',
  })
}


export default {
  queryList
}
