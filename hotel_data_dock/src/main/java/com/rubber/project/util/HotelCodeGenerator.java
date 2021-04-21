package com.rubber.project.util;

import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

/**
 * @author luffyu
 * Created on 2021/4/10
 */
public class HotelCodeGenerator {

    private static final String MODEL_NAME = "hotel_data_dock";
    private static final String AUTHOR = "luffyu-auto";
    private static final String JDBC_URL = "jdbc:mysql://127.0.0.1:3306/hotel_data_db?useUnicode=true&amp;characterEncoding=utf-8&amp;autoReconnect=true";
    private static final String USER_NAME = "root";
    private static final String PASSWORD = "root";
    private static final String PACKAGE_PARENT = "com.rubber.project";
    private static final String BASE_ENTITY_CLASS = "com.rubber.admin.core.base.BaseEntity";
    private static final String BASE_SERVICE_IMPL = "com.rubber.admin.core.base.BaseAdminService";


    public static void main(String[] args) {
        createCode("hotel_room_sync_exec_water","room_contrast_config");
    }

    private static void createCode(String... tableName) {
        AutoGenerator mpg = new AutoGenerator();
        mpg.setTemplateEngine(new FreemarkerTemplateEngine());
        GlobalConfig gc = new GlobalConfig();
        String projectPath = "hotel_data_dock";
        gc.setOutputDir(projectPath + "/src/main/java");
        gc.setAuthor("luffyu-auto");
        gc.setOpen(false);
        gc.setFileOverride(false);
        mpg.setGlobalConfig(gc);
        DataSourceConfig dataSourceConfig = new DataSourceConfig();
        dataSourceConfig.setUrl("jdbc:mysql://127.0.0.1:3306/hotel_data_db?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8");
        dataSourceConfig.setDriverName("com.mysql.jdbc.Driver");
        dataSourceConfig.setUsername("root");
        dataSourceConfig.setPassword("root");
        mpg.setDataSource(dataSourceConfig);
        PackageConfig pc = new PackageConfig();
        pc.setParent("com.rubber.project");
        pc.setXml("mapper");
        mpg.setPackageInfo(pc);
        StrategyConfig strategy = new StrategyConfig();
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        strategy.setSuperEntityClass("com.rubber.admin.core.base.BaseEntity");
        strategy.setSuperServiceImplClass("com.rubber.admin.core.base.BaseAdminService");
        strategy.setEntityLombokModel(true);
        strategy.setRestControllerStyle(true);
        strategy.setInclude(tableName);
        strategy.setControllerMappingHyphenStyle(true);
        strategy.setTablePrefix(new String[]{pc.getModuleName() + "_"});
        mpg.setStrategy(strategy);
        mpg.setTemplateEngine(new FreemarkerTemplateEngine());
        mpg.execute();
    }
}
