<template>
    <div >
        <div class="container handlerDiv" style="padding: 20px;" >
            <el-form :inline="true" :model="hotelRoomSearchParam" class="demo-form-inline">
                <el-form-item label="携程的酒店id">
                    <el-input v-model="hotelRoomSearchParam.xcHotelId" :disabled="hotelContrastId != undefined" placeholder="携程的酒店id" ></el-input>
                </el-form-item>
                <el-form-item label="龙腾的酒店id">
                    <el-input v-model="hotelRoomSearchParam.ltHotelId" :disabled="hotelContrastId != undefined" placeholder="龙腾的酒店id" ></el-input>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" @click="searchHotelRoomInfo">点击查找房型数据</el-button>
                </el-form-item>
            </el-form>
        </div>

        <div class="container handlerDiv" v-loading="loading">
            <el-row>
                <h4 class="titleH">同步数据的酒店对应基础信息
                    <span v-if="hotelContrastId != undefined">, 同步的配置Id:{{hotelContrastId}}</span>
                </h4>
            </el-row>
            <el-row style="padding: 10px 20px;" >
                <el-col :span="12">
                    <div class="grid-content bg-purple">
                        <el-form :model="xcHotelInfo" class="demo-form-inline el_form_grid">
                            <el-form-item label="携程酒店Id">
                                <span>{{xcHotelInfo.hotelId}}</span>
                            </el-form-item>
                            <el-form-item label="携程酒店名称">
                                <span>{{xcHotelInfo.hotelName}}</span>
                            </el-form-item>
                            <el-form-item label="携程酒店接口状态">
                                <el-tag :type="xcHotelInfo.interfaceStatus === 0 ? 'success' : 'danger'" >{{xcHotelInfo.msg}}</el-tag>
                            </el-form-item>
                        </el-form>
                    </div>
                </el-col>
                <el-col :span="12">
                    <div class="grid-content bg-purple-light">
                        <el-form :model="ltHotelInfo" class="demo-form-inline el_form_grid">
                            <el-form-item label="龙腾酒店Id">
                                {{ltHotelInfo.hotelId}}
                            </el-form-item>
                            <el-form-item label="龙腾酒店名称">
                                <span>{{ltHotelInfo.hotelName}}</span>
                            </el-form-item>
                            <el-form-item label="龙腾酒店接口状态">
                                <el-tag :type="ltHotelInfo.interfaceStatus === 0 ? 'success' : 'danger'">{{ltHotelInfo.msg}}</el-tag>
                            </el-form-item>
                        </el-form>
                    </div>
                </el-col>
            </el-row>

            <el-row style="padding: 20px;">
                <el-form>
                    <el-form-item label="设置当前同步类型" label-width="130px">
                        <el-select v-model="configHotelInfo.execType" placeholder="请选择">
                            <el-option
                                    v-for="item in syncDict"
                                    :key="item.value"
                                    :label="item.label"
                                    :value="item.value">
                            </el-option>
                        </el-select>
                    </el-form-item>

                </el-form>
            </el-row>
        </div>

        <div class="container handlerDiv">
            <el-row>
                <h4 class="titleH">同步数据的酒店对应房间信息</h4>
            </el-row>

            <el-row>
                <el-table
                        :data="configRoomList"
                        v-loading="loading"
                        style="width: 100%">
                    <el-table-column
                            fixed
                            prop="roomContrastId"
                            label="同步Id"
                            width="80">
                    </el-table-column>

                    <el-table-column
                            fixed
                            prop="xcRoomName"
                            label="携程房间名称"
                            min-width="250" max-width="350">
                        <template slot-scope="scope">
                            {{scope.row.xcRoomName}}({{scope.row.xcRoomId}})
                        </template>
                    </el-table-column>

                    <el-table-column
                            prop="ltRoomId"
                            label="设置绑定龙腾房间"
                            width="300">
                        <template slot-scope="scope">
                            <el-select clearable v-model="scope.row.ltRoomId" filterable placeholder="请选择" @change="handlerChangeRoom(scope)">
                                <el-option
                                        v-for="item in ltHotelRoomList"
                                        :key="item.roomId"
                                        :label="item.roomName+'('+item.roomId+')'"
                                        :value="item.roomId">
                                </el-option>
                            </el-select>
                        </template>
                    </el-table-column>

                    <el-table-column
                            prop="ltRoomPlanKey"
                            label="设置绑定的龙腾房间计划"
                            width="350">
                        <template slot-scope="scope">
                            <el-select clearable v-model="scope.row.ltRoomPlanKey" filterable placeholder="请选择" style="width: 330px" @change="getSelectLtRoomPlans(scope.row,scope.row.ltRoomId,scope.row.ltRoomPlanKey)">
                                <el-option
                                        v-for="item in getLtRoomPlans(scope.row.ltRoomId)"
                                        :key="item.ratePlanId"
                                        :label="item.ratePlanName+'('+item.ratePlanId+')'"
                                        :value="item.ratePlanId">
                                </el-option>
                            </el-select>
                        </template>
                    </el-table-column>

                    <el-table-column
                            label="当前绑定计划的今日价格示例"
                            min-width="200"
                            >
                        <template slot-scope="scope">
                            <div v-if="scope.row.ltRoomPlanInfo != undefined">
                                <span class="showPlanSpan">日期：{{scope.row.ltRoomPlanInfo.data}} </span>
                                <span class="showPlanSpan">价格：{{scope.row.ltRoomPlanInfo.price}}元</span>
                                <span class="showPlanSpan">早餐数量：{{showBackFastLabel(scope.row.ltRoomPlanInfo.breakfast)}}</span>
                                <span class="showPlanSpan">房态：{{showRoomStatus(scope.row.ltRoomPlanInfo.statu)}}</span>
                            </div>
                        </template>
                    </el-table-column>
                </el-table>
            </el-row>

            <el-row style="text-align: center;margin-top: 20px">
                <el-button
                        :disabled="xcHotelInfo.interfaceStatus != 0 || ltHotelInfo.interfaceStatus != 0"
                        type="primary"
                        icon="el-icon-s-order"
                        @click="handlerSaveSyncConfig" :loading="saveSyncConfigLoad"
                >保存配置</el-button>
            </el-row>

            <el-row style="text-align: center;">
                <span v-if="xcHotelInfo.interfaceStatus != 0 || ltHotelInfo.interfaceStatus != 0" style="font-size: 12px;color: orange">Tip:酒店id不合法</span>
            </el-row>
        </div>
    </div>

</template>

<script>
    import request from '@/utils/request';
    import global from '@/utils/Global';

    export default {
        name: "",
        created() {
            this.hotelContrastId = this.$route.query.hotelContrastId;
            if (this.hotelContrastId != undefined){
                this.getHotelConfigInfo(this.hotelContrastId);
            }
        },
        data(){
            return {
                hotelContrastId:0,
                hotelRoomSearchParam:{

                },
                hotelRoomInfo:{

                },
                ltHotelInfo:{

                },
                xcHotelInfo:{

                },
                xcHotelRoomList:[],
                ltHotelRoomList:[],
                loading: false,
                configHotelInfo:{},
                hotelConfigRoomDict:{},
                syncDict:[{
                    value: 0,
                    label: '自动执行'
                }, {
                    value: 1,
                    label: '仅手动执行'
                }, {
                    value: 2,
                    label: '暂停执行'
                }],
                saveSyncConfigLoad: false,
                configRoomList:[]
            }
        },
        methods:{
            handlerChangeRoom(scope){
                this.$delete(scope.row,'ltRoomPlanKey')
                this.$delete(scope.row,'ltRoomPlanInfo')

            },
            getHotelConfigInfo(hotelContrastId){
                request({
                    url: global.rubberBasePath + "/hotel-config/infoAndRoom?hotelContrastId="+hotelContrastId,
                    method: 'get',
                }).then(result => {
                    if (global.SUCCESS === result.code){
                        this.configHotelInfo = result.data;
                        this.hotelRoomSearchParam.xcHotelId = this.configHotelInfo.xcHotelId;
                        this.hotelRoomSearchParam.ltHotelId = this.configHotelInfo.ltHotelId;
                        let configRoomList = this.configHotelInfo.roomContrastList;
                        let hotelConfigRoomMap = {};
                        for (let i in configRoomList){
                            let roomConfig = configRoomList[i];
                            hotelConfigRoomMap[roomConfig.xcRoomId] = roomConfig;
                        }
                        this.configRoomList = configRoomList;
                        this.hotelConfigRoomDict = hotelConfigRoomMap;
                        this.searchHotelRoomInfo();
                    }else {
                        this.$message.error(result.msg);
                    }
                })
            },
            searchHotelRoomInfo(){
                this.loading = true;
                this.hotelRoomSearchParam.afterDay = 1
                request({
                    url: global.rubberBasePath + "/hotels/info",
                    method: 'get',
                    params: this.hotelRoomSearchParam
                }).then(result => {
                    if (global.SUCCESS === result.code){
                        const data = result.data;
                        this.hotelRoomInfo = data
                        this.xcHotelInfo = this.hotelRoomInfo.xcHotel;
                        this.xcHotelRoomList = this.xcHotelInfo.roomInfoList;

                        this.ltHotelInfo = this.hotelRoomInfo.ltHotel;
                        this.ltHotelRoomList = this.ltHotelInfo.roomList;

                        this.addNotConfigToRoomList(this.xcHotelRoomList);
                    }else if("30100" === result.code){
                        const data = result.data;
                        this.hotelRoomInfo = data;
                        this.xcHotelInfo = this.hotelRoomInfo.xcHotel;
                        if (this.xcHotelInfo.interfaceStatus === 0){
                            this.xcHotelRoomList = this.xcHotelInfo.roomInfoList;
                        }
                        this.ltHotelInfo = this.hotelRoomInfo.ltHotel;
                        if (this.ltHotelInfo.interfaceStatus === 0){
                            this.ltHotelRoomList = this.ltHotelInfo.roomList;

                        }
                    } else {
                        this.$message.error(result.msg);
                    }
                    this.loading = false;
                })
            },
            addNotConfigToRoomList(newConfigList){
                let oldConfigRoomList = this.configRoomList;
                if (oldConfigRoomList === undefined){
                    oldConfigRoomList = []
                }
                let oldXcHotelIds=[];
                for (let i in oldConfigRoomList){
                    oldXcHotelIds.push(oldConfigRoomList[i].xcRoomId);
                }
                for (let index in newConfigList){
                    let xcRoomId = newConfigList[index].roomId;
                    if(oldXcHotelIds.indexOf(xcRoomId) == -1){
                        let roomConfigInfo = {};
                        roomConfigInfo.xcRoomId = xcRoomId;
                        roomConfigInfo.xcRoomName = newConfigList[index].roomName;
                        this.configRoomList.push(roomConfigInfo);
                    }
                }
            },
            getLtRoomPlans(ltRoomId){
                let ltRoomPlans = [];
                if (ltRoomId == undefined){
                    return ltRoomPlans;
                }
                for (let i in this.ltHotelRoomList){
                    let hotelRoomInfo = this.ltHotelRoomList[i];
                    if (hotelRoomInfo.roomId == ltRoomId){
                        let ltRoomPlansArray = hotelRoomInfo.roomPlans;
                        for (let index in ltRoomPlansArray){
                            if (ltRoomPlansArray[index].price != undefined){
                                ltRoomPlans.push(ltRoomPlansArray[index]);
                            }
                        }
                    }
                }
                return ltRoomPlans;
            },
            getSelectLtRoomPlans(rom ,ltRoomId,selectLtRoomPlatId){
                rom.ltRoomPlanInfo = {};
                if (ltRoomId == undefined){
                    return ltRoomPlanInfo;
                }
                for (let i in this.ltHotelRoomList){
                    let hotelRoomInfo = this.ltHotelRoomList[i];
                    if (hotelRoomInfo.roomId == ltRoomId){
                        rom.ltHotelRoomInfo = hotelRoomInfo;
                        let roomPlansArray  = hotelRoomInfo.roomPlans;
                        for (let index in roomPlansArray){
                            let platInfo = roomPlansArray[index];
                            if (platInfo.ratePlanId == selectLtRoomPlatId){
                                rom.ltRoomPlanInfo = platInfo;
                                return rom.ltRoomPlanInfo;
                            }
                        }
                    }
                }
            },
            handlerSaveSyncConfig(){
                this.saveSyncConfigLoad = true;
                let hotelConfig = {};
                hotelConfig.hotelContrastId = this.hotelContrastId;
                hotelConfig.xcHotelId = this.xcHotelInfo.hotelId;
                hotelConfig.xcHotelName = this.xcHotelInfo.hotelName;
                hotelConfig.xcRequestStatus = 0;

                hotelConfig.ltHotelId = this.ltHotelInfo.hotelId;
                hotelConfig.ltHotelName = this.ltHotelInfo.hotelName;
                hotelConfig.ltRequestStatus = 0;
                hotelConfig.execType = this.configHotelInfo.execType;
                if (hotelConfig.execType == undefined){
                    this.$message.error("请设置当前同步类型");
                    return false;
                }
                let configArray = [];

                for(let i in this.configRoomList){
                    let hotelConfigInfo = this.configRoomList[i];

                    hotelConfigInfo.xcHotelId = this.xcHotelInfo.hotelId;
                    hotelConfigInfo.xcRoomStatus = 0;
                    hotelConfigInfo.xcRoomInfo = "";

                    hotelConfigInfo.ltHotelId = this.xcHotelInfo.hotelId;
                    // if (hotelConfigInfo.ltRoomId == undefined){
                    //     this.$message.error("请选中 "+hotelConfigInfo.xcRoomName+"（"+hotelConfigInfo.xcRoomId+"）的对应的龙腾房间");
                    //     this.saveSyncConfigLoad = false;
                    //     return false;
                    // }
                    if (hotelConfigInfo.ltHotelRoomInfo != undefined){
                        hotelConfigInfo.ltRoomName = hotelConfigInfo.ltHotelRoomInfo.roomName;
                    }
                    // if (hotelConfigInfo.ltRoomPlanKey == undefined){
                    //     this.$message.error("请选中 "+hotelConfigInfo.xcRoomName+"（"+hotelConfigInfo.xcRoomId+"）的对应的龙腾房间价格计划");
                    //     this.saveSyncConfigLoad = false;
                    //     return false;
                    // }
                    if (hotelConfigInfo.ltRoomPlanInfo != undefined){
                        hotelConfigInfo.ltRoomPlanName = hotelConfigInfo.ltRoomPlanInfo.ratePlanName;
                    }
                    hotelConfigInfo.ltRoomStatus = 0;
                    hotelConfigInfo.execType = 0;
                    hotelConfigInfo.ltRoomInfo = "";
                    configArray.push(hotelConfigInfo);
                }
                hotelConfig.roomContrastListStr = JSON.stringify(configArray);
                request({
                    url: global.rubberBasePath + "/hotel-config/saveAdnEdit",
                    method: 'post',
                    data: hotelConfig
                }).then(result => {
                    if (global.SUCCESS === result.code){
                        this.$message.success("保存成功");
                        this.$router.push({path: "/hotel/data-sync", query: {}})
                    }else {
                        this.$message.error(result.msg);
                    }
                    this.loading = false;
                    this.saveSyncConfigLoad = false;
                })

            },
            showBackFastLabel(backfastCode){
                let json = {
                    "011001":"不含早餐",
                    "011002":"含单早",
                    "011003":"含双早",
                    "011004":"含单双早",
                    "011007":"含床位早",
                    "011008":"含三早",
                    "011011":"含四早",
                    "011014":"含五早",
                    "011010":"含六早",
                    "011012":"含八早",
                    "011015":"双早+午餐/晚餐"
                };
                return json[backfastCode] + "("+backfastCode+")"
            },
            showRoomStatus(status){
                let json = {
                    "026001":"开启",
                    "026002":"冻结",
                    "015004":"上线",
                    "015005":"下线",
                }
                return json[status] + "("+status+")"
            }
        }
    }
</script>

<style scoped>
    .handlerDiv{
        margin-top: 10px;
        padding: 20px;
    }
    .showPlanSpan{
        padding: 5px;
    }

</style>