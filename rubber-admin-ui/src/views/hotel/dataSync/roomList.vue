<template>
    <div>
        <el-table
                :data="tableData"
                v-loading="loading"
                style="width: 100%">

            <el-table-column
                    prop="date"
                    label="携程房间信息"
                    width="300">
                <template slot-scope="scope">
                    {{ scope.row.xcRoomName}}（{{ scope.row.xcRoomId }}）
                </template>
            </el-table-column>

            <el-table-column
                    prop="date"
                    label="绑定的龙腾房间"
                    min-width="200">
                <template slot-scope="scope">
                    {{ scope.row.ltRoomName}}（{{ scope.row.ltRoomId }}）
                </template>
            </el-table-column>

            <el-table-column
                    prop="ltRoomPlanKey"
                    label="绑定的龙腾房间计划"
                    min-width="200">
                <template slot-scope="scope">
                    {{ scope.row.ltRoomPlanName}}（{{ scope.row.ltRoomPlanKey }}）
                </template>
            </el-table-column>

            <el-table-column
                    prop="floatPrice"
                    label="当时设置浮动价格"
                    width="80"
            >
                <template slot-scope="scope" >
                    <span>
                        {{scope.row.floatType === 'add' ? '+':'-'}}
                        {{scope.row.floatPrice}}元
                    </span>
                </template>
            </el-table-column>


            <el-table-column
                    prop="lastSyncTime"
                    label="最后一次同步时间"
                    min-width="100" >
            </el-table-column>

            <el-table-column
                    prop="lastSyncInfo"
                    min-width="150"
                    label="最后一次同步信息">
                <template slot-scope="scope" >
                    <span>{{showSyncInfo(scope.row.lastSyncInfo)}}</span>

                </template>
            </el-table-column>


            <el-table-column
                    prop="lastSyncStatus"
                    label="最后一次同步状态"
                    width="160">
                <template slot-scope="scope">
                    <el-tag :type="scope.row.lastSyncStatus === 1 ? 'success' : scope.row.lastSyncStatus === 0 ? '' :'danger'  ">
                        {{showSyncStatue(scope.row.lastSyncStatus)}}
                    </el-tag>
                </template>
            </el-table-column>

            <el-table-column
                    prop="remark"
                    label="备注">
            </el-table-column>

            <el-table-column
                    label="操作"
                    width="100"
                    fixed="right"
                    >
                <template slot-scope="scope">
                    <el-button
                            @click.native.prevent="openWater(scope.row, tableData)"
                            type="text"
                            size="small">
                        查看流水
                    </el-button>

                </template>
            </el-table-column>
        </el-table>

        <el-dialog :title="'房间'+ openWaterRoomInfo.xcRoomId+'的执行流水'" :visible.sync="showRoomWater"  width="70%">
            <div class="container">
                <v-room-water-list :openWaterRoomInfo="openWaterRoomInfo"/>
            </div>
        </el-dialog>
    </div>


</template>

<script>
    import request from '@/utils/request';
    import global from '@/utils/Global';
    import roomWaterList from "./roomWaterList";

    export default {
        name: "",
        props:{
            hotelContrastId:{
                required:true
            }
        },
        components:{
            'v-room-water-list':roomWaterList
        },
        data() {
            return {
                query:{
                    page:1,
                    size:10,
                    selectModels:[]
                },
                tableData: [],
                loading: true,
                showRoomWater:false,
                openWaterRoomInfo:{},
            }
        },
        created() {
          this.queryList()
        },
        methods: {
            queryList(){
                console.info("hotelContrastId="+this.hotelContrastId)
                const models = {};
                models.field = "hotelContrastId";
                models.type= "eq";
                models.data = this.hotelContrastId;
                this.query.selectModels.push(models);
                request({
                    url: global.rubberBasePath + "/room-config/list",
                    method: 'get',
                    params: {
                        'json':encodeURI(JSON.stringify(this.query))
                    }
                }).then(result => {
                    if (global.SUCCESS === result.code){
                        const data = result.data;
                        this.query.page = data.current;
                        this.query.pageTotal = data.total;
                        this.tableData = data.records;
                    }else {
                        this.$message.error(result.msg);
                    }
                    this.loading = false;
                })
            },

            openWater(roomInfo){
                this.showRoomWater = true;
                this.openWaterRoomInfo = roomInfo;
            },
            showExecType(type){
                let typeInfo = {
                    "0": "自动执行",
                    "1": "仅手动执行",
                    "2": "停止自动执行",
                };
                let typeStr = type.toString();
                return typeInfo[typeStr];
            },
            showSyncStatue(syncStatue){
                let typeInfo = {
                    "0": "初始化",
                    "1": "成功",
                    "2": "失败",
                    "3": "异常",
                    "4": "携程接口同步异常",
                    "5": "龙腾接口拉取异常",
                };
                let syncStatueStr = syncStatue.toString();
                return typeInfo[syncStatueStr];
            },
            showSyncInfo(data){
                let msg = "";
                let jsonInfo = JSON.parse(data);
                if (jsonInfo.roomPriceModel != undefined){
                    msg += " 价格:" + jsonInfo.roomPriceModel.roomPrice+"元，";
                    if (jsonInfo.floatPrice >= 0){
                        msg += " 其中浮动: +" + jsonInfo.floatPrice+"元，";
                    }
                    msg += " 早餐数:" + jsonInfo.roomPriceModel.breakfast+"份，";
                }
                if (jsonInfo.roomStatusModel != undefined){
                    let code = jsonInfo.roomStatusModel.saleStatus;
                    let name = "";
                    if (code == 0){
                        name = "满房";
                    }else if (code == 1){
                        name = "销售";
                    }else if (code == 2){
                        name = "限量";
                    }
                    msg += " 房态:" + name+"("+code+")";
                }
                return msg;
            }

        },
    }
</script>

<style scoped>

</style>