<template>
  <div>
    <H5>系统首页</H5>
<!--    <el-row :gutter="20">-->

<!--      <el-col :span="8">-->

<!--        <el-card shadow="hover" class="mgb20" style="height:252px;">-->
<!--          <div class="user-info">-->
<!--            <img src="../assets/img/img.jpg" class="user-avator" alt />-->
<!--            <div class="user-info-cont">-->
<!--              <div class="user-info-name">{{userName}}</div>-->
<!--              <div>{{roleName}}</div>-->
<!--            </div>-->
<!--          </div>-->
<!--          <div class="user-info-list">-->
<!--            上次登录时间：<span>{{loginTime}}</span>-->
<!--          </div>-->
<!--          <div class="user-info-list">-->
<!--            上次登录地点：<span>{{loginAddress}}</span>-->
<!--          </div>-->
<!--          <div class="user-info-list">-->
<!--            总的登陆次数：<span>{{loginCount}}</span>-->
<!--          </div>-->
<!--        </el-card>-->

<!--        <el-card shadow="hover" style="height:252px;">-->
<!--          <div slot="header" class="clearfix">-->
<!--            <span>语言详情</span>-->
<!--          </div>Vue-->
<!--          <el-progress :percentage="71.3" color="#42b983"></el-progress>JavaScript-->
<!--          <el-progress :percentage="24.1" color="#f1e05a"></el-progress>CSS-->
<!--          <el-progress :percentage="13.7"></el-progress>HTML-->
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
<!--                  <div class="grid-num">1234</div>-->
<!--                  <div>用户访问量</div>-->
<!--                </div>-->
<!--              </div>-->
<!--            </el-card>-->
<!--          </el-col>-->
<!--          <el-col :span="8">-->
<!--            <el-card shadow="hover" :body-style="{padding: '0px'}">-->
<!--              <div class="grid-content grid-con-2">-->
<!--                <i class="el-icon-lx-notice grid-con-icon"></i>-->
<!--                <div class="grid-cont-right">-->
<!--                  <div class="grid-num">321</div>-->
<!--                  <div>系统消息</div>-->
<!--                </div>-->
<!--              </div>-->
<!--            </el-card>-->
<!--          </el-col>-->
<!--          <el-col :span="8">-->
<!--            <el-card shadow="hover" :body-style="{padding: '0px'}">-->
<!--              <div class="grid-content grid-con-3">-->
<!--                <i class="el-icon-lx-goods grid-con-icon"></i>-->
<!--                <div class="grid-cont-right">-->
<!--                  <div class="grid-num">5000</div>-->
<!--                  <div>数量</div>-->
<!--                </div>-->
<!--              </div>-->
<!--            </el-card>-->
<!--          </el-col>-->
<!--        </el-row>-->

<!--        <el-card shadow="hover" style="height:403px;">-->
<!--          <div slot="header" class="clearfix">-->
<!--            <span>待办事项</span>-->
<!--            <el-button style="float: right; padding: 3px 0" type="text">添加</el-button>-->
<!--          </div>-->
<!--          <el-table :show-header="false" :data="todoList" style="width:100%;">-->
<!--            <el-table-column width="40">-->
<!--              <template slot-scope="scope">-->
<!--                <el-checkbox v-model="scope.row.status"></el-checkbox>-->
<!--              </template>-->
<!--            </el-table-column>-->
<!--            <el-table-column>-->
<!--              <template slot-scope="scope">-->
<!--                <div-->
<!--                    class="todo-item"-->
<!--                    :class="{'todo-item-del': scope.row.status}"-->
<!--                >{{scope.row.title}}</div>-->
<!--              </template>-->
<!--            </el-table-column>-->
<!--            <el-table-column width="60">-->
<!--              <template>-->
<!--                <i class="el-icon-edit"></i>-->
<!--                <i class="el-icon-delete"></i>-->
<!--              </template>-->
<!--            </el-table-column>-->
<!--          </el-table>-->
<!--        </el-card>-->

<!--      </el-col>-->

<!--    </el-row>-->
  </div>
</template>


<script>
  import { getUserInfo } from "../utils/auth";

    export default {
        data(){
          return {
              userName : "余哈",
              roleName : "",
              loginTime : "",
              loginIp:"",
              loginAddress:"",
              loginCount: 0,
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
          const userInfo = getUserInfo();
          this.userName = userInfo.userName;
          this.roleName = userInfo.loginAccount;
          this.loginTime = userInfo.loginTime;
          this.loginIp = userInfo.loginIp;
          this.loginAddress = '深圳';
          this.loginCount = userInfo.loginCount;

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
