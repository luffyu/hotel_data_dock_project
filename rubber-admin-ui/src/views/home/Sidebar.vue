<template>
    <div class="sidebar">
        <el-menu
            class="sidebar-el-menu"
            :default-active="onRoutes"
            background-color="#324157"
            active-text-color="#20a0ff"
            text-color="#fff"
            unique-opened
            router

            @open="handleOpen"
            @close="handleClose"
            :collapse="collapse">

            <template v-for="(menu,index) in sysMenu">
                <el-submenu v-if="menu.children && menu.children.length > 0"
                            :index="menu.url+ menu.menuId">
                    <template slot="title">
                        <i :class="menu.icon"></i>
                        <span>{{ menu.menuName }}</span>
                    </template>
                    <template v-for="(cMenu,cIndex) in menu.children">

                        <el-submenu v-if="cMenu.children && cMenu.children.length > 0" :index="cMenu.url + cMenu.menuId">
                            <template slot="title">
                                <i :class="cMenu.icon"></i>
                                <span>{{ cMenu.menuName }}</span>
                            </template>
                            <el-menu-item  v-for="(ccMenu,ccIndex) in cMenu.children"
                                           :index="ccMenu.url"
                                           :key="ccMenu.seq">
                                <i :class="ccMenu.icon"></i>
                                <span slot="title">{{ ccMenu.menuName }}</span>
                            </el-menu-item>
                        </el-submenu>
                        <el-menu-item  v-else
                                      :index="cMenu.url"
                                      :key="cMenu.seq">
                            <i :class="cMenu.icon"></i>
                            <span slot="title">{{ cMenu.menuName }}</span>
                        </el-menu-item>
                    </template>
                </el-submenu>
                <el-menu-item   v-else :index="menu.url" :key="menu.seq">
                    <i :class="menu.icon"></i>
                    <span slot="title">{{ menu.menuName }}</span>
                </el-menu-item>
            </template>

        </el-menu>

    </div>
</template>

<script>
import bus from '../../api/home/bus';
import { getUserInfoAndMenus } from '@/api/home/home';
import global from '@/utils/Global';
import {setUserData} from '@/utils/auth';

let index = 0;
export default {
    data() {
       return {
           //测边栏是否展开
           collapse: false,
           //菜单信息
           sysMenu: [{
               icon:'el-icon-lx-home',
               menuName:'我的主页',
               url: '/welcome',
           }],
       };
    },
    computed: {
        onRoutes() {
            return this.$route.path;
        }
    },

    created() {
        // 通过 Event Bus 进行组件间通信，来折叠侧边栏
        bus.$on('collapse', msg => {
            this.collapse = msg;
            bus.$emit('collapse-content', msg);
        });

        // 获取用户的菜单信息
        getUserInfoAndMenus().then(result => {
            if(result.code === global.SUCCESS){
                //设置用户的基本信息到cookie中
                setUserData(result.data);
                //获取用户的菜单信息
                const menuInfo = result.data.sysMenus;
                //获取根目录的菜单信息
                if(menuInfo && menuInfo.length > 0){
                    menuInfo.forEach((item) => {
                        this.sysMenu.push(item);
                    });
                }
            }else {
                this.$message.error(result.msg);
            }
        });
    },
    methods:{
        handleOpen(key, keyPath) {
            console.log(key, keyPath);
        },
        handleClose(key, keyPath) {
            console.log(key, keyPath);
        }
    }

};
</script>

<style scoped>
.sidebar {
    display: block;
    position: absolute;
    left: 0;
    top: 60px;
    bottom: 0;
    overflow-y: scroll;
}
.sidebar::-webkit-scrollbar {
    width: 0;
}
.sidebar-el-menu:not(.el-menu--collapse) {
    width: 200px;
}
.sidebar > ul {
    height: 100%;
}
</style>
