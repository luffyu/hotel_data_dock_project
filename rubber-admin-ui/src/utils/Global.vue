<script>
  //rubber后台的全局地址
  const rubberBasePath = '';


  //请求成功的code
  const SUCCESS = '10100';
  //用户没有登陆
  const USER_NOT_LOGIN = '20100';
  //登陆的token过期
  const TOKEN_IS_EXPIRED = '20105';
  //账户登陆状态异常
  const LOGIN_VERSION_ILLEGAL = '20109';

  export default
  {
    rubberBasePath,
    SUCCESS,
    checkLoginFlag,
    handelRequestError
  }

  /**
   * 检测登陆状态
   * @param code 当前的接口请求code
   * @returns {boolean} 返回true表示登陆状态正常 返回false表示不正常
   */
  export function checkLoginFlag(code) {
    if(code === USER_NOT_LOGIN || code === TOKEN_IS_EXPIRED || code === LOGIN_VERSION_ILLEGAL){
      return false;
    }
    return true;
  }

  export function handelRequestError(result) {
    this.$message.error(result.msg);
  }
</script>
