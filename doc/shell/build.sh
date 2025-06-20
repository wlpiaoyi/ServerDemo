#!/bin/bash
#set -e
# maven命令
#m_cmd="/c/Home/Documents/Develop/Tools/apache-maven/3.6.1/bin/mvn"
#m_cmd="/home/maven/bin/mvn"
m_cmd="mvn"
JAVA_CMD="java"


#./build.sh -e 1 -m /d/wlpia/.m2/repository -j java


# 源码路径
BASE_PATH=$(cd $(dirname $0) && pwd)
cd ${BASE_PATH}
cd ../
BASE_PATH=$(pwd)


#maven库路径
MAVEN_LIB=$($m_cmd help:evaluate -Dexpression=settings.localRepository -q -DforceStdout)
while getopts ":b:e:p:m:j:" opt
do
    case $opt in
        e)
        EXEC_TAG=${OPTARG}
        ;;
        b)
        BASE_PATH=${OPTARG}
        ;;
        m)
        MAVEN_LIB=${OPTARG}
        ;;
        j)
        JAVA_CMD=$OPTARG
        ;;
        ?)

        ;;
    esac
done


# 0:执行所有方法 1:maven安装jar包 2:编译项目 3:打包项目 4:打包脚本 5:打包资源
EXEC_TAG="0"
#maven库路径
#MAVEN_LIB=$($m_cmd help:evaluate -Dexpression=settings.localRepository -q -DforceStdout)

#MAVEN_JAVA_HOME=$($m_cmd -v | grep -o 'Java version:.*')
#MAVEN_JAVA_HOME=$($m_cmd -v | grep -o 'runtime:.*' | cut -d' ' -f2-)
#MAVEN_JAVA_HOME=$MAVEN_JAVA_HOME/bin/java

echo "[cmcc]-$(date "+%Y.%m.%d-%H.%M.%S") -----------------------------------------------------------------------------"
echo "[cmcc]-$(date "+%Y.%m.%d-%H.%M.%S") EXEC_TAG:${EXEC_TAG}"
echo "[cmcc]-$(date "+%Y.%m.%d-%H.%M.%S") BASE_PATH:${BASE_PATH}"
#echo "[cmcc]-$(date "+%Y.%m.%d-%H.%M.%S") MAVEN_LIB:${MAVEN_LIB}"
echo "[cmcc]-$(date "+%Y.%m.%d-%H.%M.%S") -----------------------------------------------------------------------------"

# 检查环境
function check_env() {
    sleep 1

    # 检查maven是否安装
    if type "$m_cmd" > /dev/null 2>&1 ; then
      echo "[cmcc]-$(date "+%Y.%m.%d-%H.%M.%S") maven已经安装！"
    else
      echo "[cmcc]-$(date "+%Y.%m.%d-%H.%M.%S") 请先安装maven！"
      exit
    fi

    # 检查java是否安装
    if type "java" > /dev/null 2>&1 ; then
      echo "[cmcc]-$(date "+%Y.%m.%d-%H.%M.%S") java已经安装！"
    else
      echo "[cmcc]-$(date "+%Y.%m.%d-%H.%M.%S") 请先安装java！"
      exit 1;
    fi
#
#    echo "[cmcc]-$(date "+%Y.%m.%d-%H.%M.%S") -----------------------------------------------------------------------------"
#    echo "[cmcc]-$(date "+%Y.%m.%d-%H.%M.%S") 尝试安装jar到maven库"
#    sleep 1
#
#    echo "[cmcc]-$(date "+%Y.%m.%d-%H.%M.%S") MAVEN_LIB ${MAVEN_LIB}"
}

#编译项目
function build_project() {

    #清空缓存
    echo "[cmcc]-$(date "+%Y.%m.%d-%H.%M.%S") cd ${BASE_PATH}"
    echo "[cmcc]-$(date "+%Y.%m.%d-%H.%M.%S") $m_cmd clean"
    sleep 1

    # 清空maven打包脏数据
    cd ${BASE_PATH}
    $m_cmd clean

    # 开始编译文件
    echo "[cmcc]-$(date "+%Y.%m.%d-%H.%M.%S") mvn package -P prod"
    sleep 1
    $m_cmd package -P prod -Dmaven.test.skip=true
}

# 打包项目
function package_shell() {
    rm -rf ${BASE_PATH}/shell.tar.gz

    cd ${BASE_PATH}/shell

    echo "[cmcc]-$(date "+%Y.%m.%d-%H.%M.%S") -zcvf shell.tar.gz *.sh"
    sleep 1

    cd ${BASE_PATH}/target/
    tar -zcvf shell.tar.gz *.sh
    chmod +x shell.tar.gz
    mv ${BASE_PATH}/shell/shell.tar.gz ${BASE_PATH}/shell.tar.gz

    echo "[cmcc]-$(date "+%Y.%m.%d-%H.%M.%S") 打包完成, 包名:shell.tar.gz"

}

# 打包项目
function package_project() {
    # 清理打包痕迹
    rm -rf ${BASE_PATH}/target/
    rm -rf ${BASE_PATH}/package.tar.gz

    mkdir ${BASE_PATH}/target/
    cd ${BASE_PATH}/

    mv filling-gateway/target/*jar target/
    mv filling-module-bpm/filling-module-bpm-biz/target/*jar target/
    mv filling-module-infra/filling-module-infra-biz/target/*jar target/
    mv filling-module-poi/filling-module-poi-biz/target/*jar target/
    mv filling-module-report/filling-module-report-biz/target/*jar target/
    mv filling-module-system/filling-module-system-biz/target/*jar target/

    echo "[cmcc]-$(date "+%Y.%m.%d-%H.%M.%S") -zcvf package.tar.gz *.jar"
    sleep 1

    cd ${BASE_PATH}/target/
    tar -zcvf package.tar.gz *.jar
    chmod +x package.tar.gz
    mv ${BASE_PATH}/target/package.tar.gz ${BASE_PATH}/package.tar.gz

    echo "[cmcc]-$(date "+%Y.%m.%d-%H.%M.%S") 打包完成, 包名:package.tar.gz"

}

# 检查环境
check_env
# 打包脚本
package_shell
# 打包项目
package_project





