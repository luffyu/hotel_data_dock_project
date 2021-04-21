import Cookies from 'js-cookie'

const TokenKey = 'Login-Token';

const User_Info_Key = 'User-Info-Key';

export function getToken() {
  return Cookies.get(TokenKey)
}

export function setToken(token) {
  return Cookies.set(TokenKey, token)
}

export function removeToken() {
  return Cookies.remove(TokenKey)
}

export function loginOut() {
  removeToken();
  removeUserInfo();
}



export function setUserInfo(userInfo) {
  return Cookies.set(User_Info_Key, userInfo)
}

export function getUserInfo() {
  const userInfo = Cookies.get(User_Info_Key)
  return JSON.parse(userInfo);
}
export function removeUserInfo() {
  return Cookies.remove(User_Info_Key)
}
