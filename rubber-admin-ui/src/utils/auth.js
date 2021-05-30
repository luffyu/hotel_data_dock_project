import Cookies from 'js-cookie'

const TokenKey = 'Login-Token';

const User_Info_Key = 'User-Info-Key';

const User_Data_Key = 'User-Data-Key';

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

export function setUserData(userData) {
  return Cookies.set(User_Data_Key, userData)
}

export function getUserData() {
  const userData = Cookies.get(User_Data_Key)
  return JSON.parse(userData);
}

export function removeUserInfo() {
  return Cookies.remove(User_Data_Key)
}
