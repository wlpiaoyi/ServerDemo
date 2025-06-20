#!/bin/bash
set -e

DATE=$(date +%Y%m%d)

# 脚本路径
BASE_PATH=$(cd $(dirname $0) && pwd)
# 编译包路径
DEPLOY_PATH=${BASE_PATH}
# 部署的包路径
PACKAGE_PATH=${BASE_PATH}/package.tar.gz
JAVA_CMD="java"

while getopts ":d:p:a:n:g:j:" opt
do
    case $opt in
        d)
        DEPLOY_PATH=${OPTARG}
        ;;
        p)
        PACKAGE_PATH=${OPTARG}
        ;;
        j)
        JAVA_CMD=$OPTARG
        ;;
        ?)
#        echo "[cmcc]-$(date "+%Y.%m.%d-%H.%M.%S") unknown shell params $OPTARG"
        ;;
    esac
done

if [ ${JAVA_CMD} = "java" ]; then
    # 检查java是否安装
    if type "${JAVA_CMD}" > /dev/null 2>&1 ; then
      echo "[cmcc]-$(date "+%Y.%m.%d-%H.%M.%S") java is install！"
    else
      echo "[cmcc]-$(date "+%Y.%m.%d-%H.%M.%S") install java please！"
      exit 0;
    fi
fi
echo "[cmcc]-$(date "+%Y.%m.%d-%H.%M.%S") jdk check cmd: ${JAVA_CMD}"

# 检查路径完整性
function check_package() {
    if [ ! -f ${PACKAGE_PATH} ]; then
        echo "[cmcc]-$(date "+%Y.%m.%d-%H.%M.%S") ${PACKAGE_PATH} not exists."
        exit 0;
    fi
    mkdir -p ${DEPLOY_PATH}
    mkdir -p ${DEPLOY_PATH}/server
    mkdir -p ${DEPLOY_PATH}/backup
}

#解压shell tar.gz
function dec_shell() {
    echo "[cmcc]-$(date "+%Y.%m.%d-%H.%M.%S") [copy_jar] start decompression shell:${DEPLOY_PATH}"
#    rm -rf ${DEPLOY_PATH}/tmp
#    mkdir -p ${DEPLOY_PATH}/tmp
    tar -zxvf ${BASE_PATH}/shell.tar.gz -C ${BASE_PATH}/
#    mv deploy_jar.sh ${DEPLOY_PATH}/deploy_jar.sh
#    mv guard.sh ${DEPLOY_PATH}/guard.sh
}

#解压jar tar.gz
function dec_jar() {
    echo "[cmcc]-$(date "+%Y.%m.%d-%H.%M.%S") [copy_jar] start decompression package:${DEPLOY_PATH}"
    rm -rf ${DEPLOY_PATH}/tmp
    mkdir -p ${DEPLOY_PATH}/tmp
    tar -zxvf ${PACKAGE_PATH} -C ${DEPLOY_PATH}/tmp
    cd ${DEPLOY_PATH}/tmp
    JAR_FILES=$(ls *.jar)
    for jar_file in ${JAR_FILES}; do
        jar_name=$(echo ${jar_file} | awk -F ".jar" '{print $1}')
        echo "[cmcc]-$(date "+%Y.%m.%d-%H.%M.%S") decompression jar_name:${jar_name}"
    done
}

# 备份Jar
function backup_jar() {
    cd ${DEPLOY_PATH}
    # 当天备份路径
    backup_path=${DEPLOY_PATH}/backup/$DATE
    mkdir -p ${backup_path}
    for jar_file in ${JAR_FILES}; do
        jar_name=$(echo ${jar_file} | awk -F "/"  '{print $NF}'  | awk -F ".jar" '{print $1}')
        jar_path=${DEPLOY_PATH}/server/${jar_name}/${jar_name}.jar
        # 如果不存在，则无需备份
        if test -e ${jar_path}
        then
            echo "[cmcc]-$(date "+%Y.%m.%d-%H.%M.%S") [backup] start backup:${jar_path}..."
            cp ${jar_path} ${backup_path}/$(date +%H%M).${jar_name}.jar.bak
        else
            echo "[cmcc]-$(date "+%Y.%m.%d-%H.%M.%S") [backup] ${jar_path} is not exit, skip backup"
        fi
#        log_path=${DEPLOY_PATH}/server/${jar_name}/${jar_name}.jar.log
#        # 如果不存在，则无需备份
#        if test -e ${log_path}
#        then
#            echo "[cmcc]-$(date "+%Y.%m.%d-%H.%M.%S") [backup] start backup:${log_path}..."
#            cp ${log_path} ${backup_path}/$(date +%H%M).$jar_name.jar.log.bak
#        else
#            echo "[cmcc]-$(date "+%Y.%m.%d-%H.%M.%S") [backup] ${jar_deploy_path} is not exit, skip backup"
#        fi
#        echo "[cmcc]-$(date "+%Y.%m.%d-%H.%M.%S") [backup] backup [${jar_file}] completed"
    done
}

# 停止所有服务
function stop_jar() {
    cd ${DEPLOY_PATH}/server
    for jar_file in ${JAR_FILES}; do
        jar_name=$(echo ${jar_file} | awk -F "/"  '{print $NF}'  | awk -F ".jar" '{print $1}')
        echo "[cmcc]-$(date "+%Y.%m.%d-%H.%M.%S") stop ${jar_name}/${jar_file}"
        stop_server ${jar_name}
    done
}


#拷贝jar包到部署目录
function copy_jar() {
    echo "[cmcc]-$(date "+%Y.%m.%d-%H.%M.%S") [copy_jar] start copy jar to deploy path: ${DEPLOY_PATH}"
    cd ${DEPLOY_PATH}/tmp
    for jar_file in ${JAR_FILES}; do
        jar_name=$(echo ${jar_file} | awk -F ".jar" '{print $1}')
        jar_dir=${DEPLOY_PATH}/server/${jar_name}
        mkdir -p ${jar_dir}
        mv -f ${jar_file} ${jar_dir}
        echo "[cmcc]-$(date "+%Y.%m.%d-%H.%M.%S") [copy_jar] copy jar [${jar_file}] completed"
    done
}

# 启动所有服务
function start_jar() {
    for jar_file in ${JAR_FILES}; do
        jar_name=$(echo ${jar_file} | awk -F ".jar" '{print $1}')
        echo "[cmcc]-$(date "+%Y.%m.%d-%H.%M.%S") cd ${BASE_PATH}"
        cd ${BASE_PATH}
        echo "[cmcc]-$(date "+%Y.%m.%d-%H.%M.%S") ./guard.sh -s ${jar_name} -d ${DEPLOY_PATH} -t 1 -j ${JAVA_CMD}"
        ./guard.sh -s ${jar_name} -d ${DEPLOY_PATH} -t 1 -j ${JAVA_CMD}
    done

    echo "[cmcc]-$(date "+%Y.%m.%d-%H.%M.%S") 开始检查服务启动状态..."
    for jar_file in ${JAR_FILES}; do
        sleep 1
        jar_name=$(echo ${jar_file} | awk -F ".jar" '{print $1}')
        echo "[cmcc]-$(date "+%Y.%m.%d-%H.%M.%S") cd ${BASE_PATH}"
        cd ${BASE_PATH}
        echo "[cmcc]-$(date "+%Y.%m.%d-%H.%M.%S") ./guard.sh -s ${jar_name} -d ${DEPLOY_PATH} -t 0"
        ./guard.sh -s ${jar_name} -d ${DEPLOY_PATH} -t 0
    done

    echo "[cmcc]-$(date "+%Y.%m.%d-%H.%M.%S") 输入以下命令查看启动日志【"
    for jar_file in ${JAR_FILES}; do
        sleep 1
        jar_name=$(echo ${jar_file} | awk -F ".jar" '{print $1}')
        cd ${BASE_PATH}
        echo "./guard.sh -s ${jar_name} -d ${DEPLOY_PATH} -t 2"
    done
    echo "】"
}


#停止单个服务
function stop_server() {
    server_name=$1
    echo "[cmcc]-$(date "+%Y.%m.%d-%H.%M.%S") cd ${BASE_PATH}"
    cd ${BASE_PATH}
    pwd
    ls
    echo "[cmcc]-$(date "+%Y.%m.%d-%H.%M.%S") ./guard.sh -s ${server_name} -d ${DEPLOY_PATH} -t 10"
    ./guard.sh -s ${server_name} -d ${DEPLOY_PATH} -t 10
}


function deploy_jar() {
#    # 解压shell tar.gz
#    dec_shell
    # 检查部署包是否存在
    check_package
    # 解压 tar
    dec_jar
    # 备份原 jar
    backup_jar
    # 停止java服务
    stop_jar
    # 部署新jar
    copy_jar
    # 启动新jar
    start_jar
}


deploy_jar

# 使用示例
#chmod 777 deploy_jar.sh
#./deploy_jar.sh -d /icss/project/fill/ -p package.tar.gz -j /usr/local/jdk1.8.0_341/bin/java