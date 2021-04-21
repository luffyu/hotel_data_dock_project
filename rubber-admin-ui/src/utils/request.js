import axios from 'axios';
import { getToken } from '../utils/auth'
import global from "../utils/Global";
import { Notification, MessageBox } from 'element-ui'
import router from '@/router';

axios.defaults.headers['Content-Type'] = 'application/json;charset=utf-8';


const service = axios.create({
    timeout: 10000,
});

service.interceptors.request.use(
    config => {
      if(getToken()){
        config.headers["Authorization"] = getToken();
      }
      return config;
    },
    error => {
        console.log(error);
        return Promise.reject();
    }
);

service.interceptors.response.use(
    response => {
        if (response.status === 200) {
            const result = response.data;
            if(!global.checkLoginFlag(result.code)){
              MessageBox.confirm(
                  '登录状态异常'+result.msg,
                  '系统提示',
                  {
                    confirmButtonText: '重新登录',
                    cancelButtonText: '取消',
                    type: 'warning'
                  }
              ).then(() => {
                // store.dispatch('LogOut').then(() => {
                //   location.reload() // 为了重新实例化vue-router对象 避免bug
                // })
                router.push('/login');
              })

            }else {
              return response.data;
            }
        } else {
            Promise.reject();
        }
    },
    error => {
        console.log(error);
        return Promise.reject();
    }
);

export default service;
