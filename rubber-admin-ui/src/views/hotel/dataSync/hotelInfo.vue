<template>
    <div >
        <div class="container">
            <el-row>
                <el-col :span="21">
                    <h4 class="titleH">同步数据的酒店对应基础信息</h4>
                </el-col>
                <el-col :span="3" >
                    <el-button class="titleH" icon="el-icon-video-play" type="success" @click="startForManual()" >手动执行一次</el-button>
                    <el-button style="margin-left: 10px" class="titleH" icon="el-icon-arrow-left" type="primary" @click="handlerReturn()" >返回</el-button>
                </el-col>
            </el-row>
            <el-row>
                <el-col :span="12">
                    <div class="grid-content bg-purple">
                        <el-form :model="hotelInfo" class="demo-form-inline el_form_grid">
                            <el-form-item label="携程酒店Id">
                                <span>{{hotelInfo.xcHotelId}}</span>
                            </el-form-item>
                            <el-form-item label="携程酒店名称">
                                <span>{{hotelInfo.xcHotelName}}</span>
                            </el-form-item>
                            <el-form-item label="携程酒店接口状态">
                                <el-tag :type="hotelInfo.xcRequestStatus === 0 ? 'success' : 'danger'">
                                    {{hotelInfo.xcRequestStatus === 0 ? '正常':'异常'}}
                                </el-tag>
                            </el-form-item>
<!--                            <el-form-item label="携程酒店详情">-->
<!--                                <span>{{hotelInfo.xcHotelInfo}}</span>-->
<!--                            </el-form-item>-->
                        </el-form>
                    </div>
                </el-col>
                <el-col :span="12">
                    <div class="grid-content bg-purple-light">
                        <el-form :model="hotelInfo" class="demo-form-inline el_form_grid">
                            <el-form-item label="龙腾酒店Id">
                                {{hotelInfo.ltHotelId}}
                            </el-form-item>
                            <el-form-item label="龙腾酒店名称">
                                <span>{{hotelInfo.ltHotelName}}</span>
                            </el-form-item>
                            <el-form-item label="龙腾酒店接口状态">
                                <el-tag :type="hotelInfo.ltRequestStatus === 0 ? 'success' : 'danger'">
                                    {{hotelInfo.ltRequestStatus === 0 ? '正常':'异常'}}
                                </el-tag>
                            </el-form-item>
<!--                            <el-form-item label="龙腾酒店详情">-->
<!--                                <span>{{hotelInfo.ltHotelInfo}}</span>-->
<!--                            </el-form-item>-->
                        </el-form>
                    </div>
                </el-col>
            </el-row>

        </div>

        <div class="container" style="margin-top: 10px;">
            <h4 class="titleH">同步数据的酒店房型相关信息</h4>
            <v-room-list :hotel-contrast-id="hotelContrastId"/>
        </div>

        <el-dialog title="正在同步中" :visible.sync="dialogTableVisible">
            <div style="width: 80%;margin-left: 10%">
                <el-progress :text-inside="true" :stroke-width="26" :percentage="percentage"></el-progress>
            </div>
        </el-dialog>
    </div>
</template>

<script>
    import request from '@/utils/request';
    import global from '@/utils/Global';
    import roomList from "./roomList";

    export default {
        name: "",
        components:{
            'v-room-list':roomList
        },
        created() {
            this.hotelContrastId = this.$route.query.hotelContrastId;
            this.getHotelInfo(this.hotelContrastId);
        },
        methods:{
            getHotelInfo(hotelContrastId){
                request({
                    url: global.rubberBasePath + "/hotel-config/info?hotelContrastId="+hotelContrastId,
                    method: 'get',
                }).then(result => {
                    if (global.SUCCESS === result.code){
                        this.hotelInfo = result.data;
                    }else {
                        this.$message.error(result.msg);
                    }
                })
            },
            handlerReturn(){
                this.$router.push({path: "/hotel/data-sync", query: {}})
            },
            startForManual(){
                this.$confirm('确认之后当前酒店所有房间信息将同步一次', '确定手动执行一次？', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    this.dialogTableVisible = true;
                    this.percentage = 80;
                    request({
                        url: global.rubberBasePath + "/room-sync/start-manual/"+this.hotelContrastId,
                        method: 'post',
                    }).then(result => {
                        if (global.SUCCESS === result.code){
                            this.percentage = 100;
                            location.reload()
                        }else {
                            this.$message.error(result.msg);
                        }
                    })

                }).catch(() => {
                    this.$message({
                        type: 'info',
                        message: '已取消执行'
                    });
                });
            },
        },
        data(){
            return {
                hotelInfo:{},
                hotelContrastId:0,
                dialogTableVisible:false,
                percentage:15,
            }
        }
    }
</script>

<style scoped>
    .titleH{
        margin: 20px 0;
    }
    .el_form_grid{
        padding: 20px;
    }


</style>