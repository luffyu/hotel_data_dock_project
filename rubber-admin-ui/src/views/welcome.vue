<template>
  <div>
    <H5>系统首页</H5>

<!--    <el-row :gutter="20">-->
<!--      <el-col :span="8">-->

<!--        <el-card shadow="hover" class="mgb20" style="height:300px;">-->
<!--          <div class="user-info">-->
<!--            <img src="../assets/img/img.jpg" class="user-avator" alt />-->
<!--            <div class="user-info-cont">-->
<!--              <div class="user-info-name">{{userInfo.userName}}</div>-->
<!--              <div>-->
<!--                <span v-for="item in userRoles"> {{item.roleName}} </span>-->
<!--              </div>-->
<!--            </div>-->
<!--          </div>-->
<!--          <div>-->
<!--            <el-form ref="userInfo" :model="userInfo" label-width="100px">-->
<!--                <el-form-item label="上次登录时间">-->
<!--                  <span>{{userInfo.loginTime}}</span>-->
<!--                </el-form-item>-->
<!--                <el-form-item label="总的登陆次数">-->
<!--                  <span>{{userInfo.loginCount}}</span>次-->
<!--                </el-form-item>-->
<!--            </el-form>-->
<!--          </div>-->
<!--        </el-card>-->

<!--        <el-card shadow="hover" style="height:260px;">-->
<!--          <div slot="header" class="clearfix">-->
<!--            <span>执行属性相关信息</span>-->
<!--          </div>-->
<!--          成功-->
<!--          <el-progress :percentage="71.3" color="#42b983"></el-progress>-->
<!--          异常-->
<!--          <el-progress :percentage="24.1" color="#f1e05a"></el-progress>-->
<!--          失败-->
<!--          <el-progress :percentage="13.7"></el-progress>-->
<!--          未知-->
<!--          <el-progress :percentage="5.9" color="#f56c6c"></el-progress>-->
<!--        </el-card>-->

<!--      </el-col>-->

<!--      <el-col :span="16">-->

<!--        <el-row :gutter="20" class="mgb20">-->
<!--          <el-col :span="8">-->
<!--            <el-card shadow="hover" :body-style="{padding: '0px'}">-->
<!--              <div class="grid-content grid-con-1">-->
<!--                <i class="el-icon-lx-people grid-con-icon"></i>-->
<!--                <div class="grid-cont-right">-->
<!--                  <div class="grid-num">{{configNumInfo.hotelConfigNum}}</div>-->
<!--                  <div>总配置酒店数</div>-->
<!--                </div>-->
<!--              </div>-->
<!--            </el-card>-->
<!--          </el-col>-->
<!--          <el-col :span="8">-->
<!--            <el-card shadow="hover" :body-style="{padding: '0px'}">-->
<!--              <div class="grid-content grid-con-2">-->
<!--                <i class="el-icon-lx-notice grid-con-icon"></i>-->
<!--                <div class="grid-cont-right">-->
<!--                  <div class="grid-num">{{configNumInfo.syncExecNum}}</div>-->
<!--                  <div>同步执行总次数</div>-->
<!--                </div>-->
<!--              </div>-->
<!--            </el-card>-->
<!--          </el-col>-->
<!--          <el-col :span="8">-->
<!--            <el-card shadow="hover" :body-style="{padding: '0px'}">-->
<!--              <div class="grid-content grid-con-3">-->
<!--                <i class="el-icon-lx-goods grid-con-icon"></i>-->
<!--                <div class="grid-cont-right">-->
<!--                  <div class="grid-num">{{configNumInfo.userAccessNum}}</div>-->
<!--                  <div>用户总访问次数</div>-->
<!--                </div>-->
<!--              </div>-->
<!--            </el-card>-->
<!--          </el-col>-->
<!--        </el-row>-->

<!--        <el-card shadow="hover" style="height:460px;">-->
<!--          <div slot="header" class="clearfix">-->
<!--            <span>最新执行公告</span>-->
<!--          </div>-->
<!--          <el-table :show-header="false" :data="todoList" style="width:100%;">-->
<!--            <el-table-column>-->
<!--              <template slot-scope="scope">-->
<!--                <div-->
<!--                    class="todo-item"-->
<!--                    :class="{'todo-item-del': scope.row.status}"-->
<!--                >{{scope.row.title}}</div>-->
<!--              </template>-->
<!--            </el-table-column>-->
<!--            <el-table-column width="60">-->

<!--            </el-table-column>-->
<!--          </el-table>-->
<!--        </el-card>-->

<!--      </el-col>-->

<!--    </el-row>-->
  </div>
</template>


<script>
  import { getUserInfoAndMenus } from '@/api/home/home';
  import global from '@/utils/Global';

  export default {
        data(){
          return {
              userInfo:{},
              userRoles:[],
              configNumInfo:{
                  hotelConfigNum:0,
                  syncExecNum:0,
                  userAccessNum:0,
              },
              todoList: [
                {
                  title: '今天要修复100个bug',
                  status: false
                },
                {
                  title: '今天要修复100个bug',
                  status: false
                },
                {
                  title: '今天要写100行代码加几个bug吧',
                  status: false
                },
                {
                  title: '今天要修复100个bug',
                  status: false
                },
                {
                  title: '今天要修复100个bug',
                  status: true
                },
                {
                  title: '今天要写100行代码加几个bug吧',
                  status: true
                }
              ],
          }
        },
        created(){
          // 获取用户的菜单信息
          getUserInfoAndMenus().then(result => {
            if(result.code === global.SUCCESS){
              //设置用户的基本信息到cookie中
              //获取用户的菜单信息
              this.userInfo = result.data.sysUser;
              this.userRoles = result.data.sysRoles;
            }else {
              this.$message.error(result.msg);
            }
          });
        }
    }
</script>


<style scoped>
  .el-row {
    margin-bottom: 20px;
  }

  .grid-content {
    display: flex;
    align-items: center;
    height: 100px;
  }

  .grid-cont-right {
    flex: 1;
    text-align: center;
    font-size: 14px;
    color: #999;
  }

  .grid-num {
    font-size: 30px;
    font-weight: bold;
  }

  .grid-con-icon {
    font-size: 50px;
    width: 100px;
    height: 100px;
    text-align: center;
    line-height: 100px;
    color: #fff;
  }

  .grid-con-1 .grid-con-icon {
    background: rgb(45, 140, 240);
  }

  .grid-con-1 .grid-num {
    color: rgb(45, 140, 240);
  }

  .grid-con-2 .grid-con-icon {
    background: rgb(100, 213, 114);
  }

  .grid-con-2 .grid-num {
    color: rgb(45, 140, 240);
  }

  .grid-con-3 .grid-con-icon {
    background: rgb(242, 94, 67);
  }

  .grid-con-3 .grid-num {
    color: rgb(242, 94, 67);
  }

  .user-info {
    display: flex;
    align-items: center;
    padding-bottom: 20px;
    border-bottom: 2px solid #ccc;
    margin-bottom: 20px;
  }

  .user-avator {
    width: 120px;
    height: 120px;
    border-radius: 50%;
  }

  .user-info-cont {
    padding-left: 50px;
    flex: 1;
    font-size: 14px;
    color: #999;
  }

  .user-info-cont div:first-child {
    font-size: 30px;
    color: #222;
  }

  .user-info-list {
    font-size: 14px;
    color: #999;
    line-height: 25px;
  }

  .user-info-list span {
    margin-left: 70px;
  }

  .mgb20 {
    margin-bottom: 20px;
  }

  .todo-item {
    font-size: 14px;
  }

  .todo-item-del {
    text-decoration: line-through;
    color: #999;
  }

  .schart {
    width: 100%;
    height: 300px;
  }
</style>
