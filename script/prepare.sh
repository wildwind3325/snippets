#!/bin/bash

# 关闭防火墙
systemctl disable firewalld
systemctl stop firewalld

# 关闭selinux
sed -i 's#enforcing#disabled#g' /etc/selinux/config
setenforce 0

# 关闭调优服务
systemctl start tuned
tuned-adm off
systemctl stop tuned
systemctl disable tuned

# 关闭透明页
echo never > /sys/kernel/mm/transparent_hugepage/enabled
echo never > /sys/kernel/mm/transparent_hugepage/defrag
echo "echo never > /sys/kernel/mm/transparent_hugepage/enabled" >> /etc/rc.d/rc.local
echo "echo never > /sys/kernel/mm/transparent_hugepage/defrag" >> /etc/rc.d/rc.local
chmod +x /etc/rc.d/rc.local

# 设置交换页
sysctl -w vm.swappiness=1
echo "vm.swappiness=1" >> /etc/sysctl.conf

echo "设置主机名"
read hostname
hostnamectl set-hostname $hostname
echo "HOSTNAME=$hostname" >> /etc/sysconfig/network

# 设置hosts
echo "172.17.114.190 util.hv.com util" >> /etc/hosts
echo "172.17.114.235 master01.hv.com master01" >> /etc/hosts
echo "172.17.114.236 master02.hv.com master02" >> /etc/hosts
echo "172.17.114.237 master03.hv.com master03" >> /etc/hosts
echo "172.17.114.193 data01.hv.com data01" >> /etc/hosts
echo "172.17.114.201 data02.hv.com data02" >> /etc/hosts
echo "172.17.114.202 data03.hv.com data03" >> /etc/hosts
echo "172.17.114.35 data04.hv.com data04" >> /etc/hosts
echo "172.17.114.36 data05.hv.com data05" >> /etc/hosts
echo "172.17.114.37 data06.hv.com data06" >> /etc/hosts
echo "172.17.114.160 rt01.hv.com rt01" >> /etc/hosts
echo "172.17.114.161 rt02.hv.com rt02" >> /etc/hosts
echo "172.17.114.162 rt03.hv.com rt03" >> /etc/hosts
echo "172.17.114.163 data04.hv.com data04" >> /etc/hosts
echo "172.17.114.164 data05.hv.com data05" >> /etc/hosts
echo "172.17.114.165 data06.hv.com data06" >> /etc/hosts
echo "172.17.114.166 tb01.hv.com tb01" >> /etc/hosts
echo "172.17.114.167 tb02.hv.com tb02" >> /etc/hosts
echo "172.17.114.168 tb03.hv.com tb03" >> /etc/hosts

# 配置yum源
mkdir /etc/yum.repos.d/backup
mv /etc/yum.repos.d/*.repo /etc/yum.repos.d/backup
curl -o /etc/yum.repos.d/hv.repo http://172.17.114.175/www/hv.repo
curl -o /etc/yum.repos.d/cm.repo http://172.17.114.175/www/cm.repo
yum clean all
yum makecache

# 禁用chrony服务
systemctl stop chronyd
systemctl disable chronyd

# 安装ntp服务
yum install -y ntp
#echo "输入ntp服务器ip"
#read ntpserver
#ntpdate -u $ntpserver
ntpdate -u 172.17.114.175
hwclock --systohc
#echo "server $ntpserver" >> /etc/ntp.conf
echo "server 172.17.114.175" >> /etc/ntp.conf
systemctl start ntpd
systemctl enable ntpd

# 安装jdk
yum install -y jdk1.8

# 下载mysql-connector-java
mkdir /usr/share/java
curl -o /usr/share/java/mysql-connector-java.jar http://172.17.114.175/www/CDH/mysql-connector-java-5.1.48.jar

# ssh开放端口18022
# echo "Port 22" >> /etc/ssh/sshd_config
# echo "Port 18022" >> /etc/ssh/sshd_config
# systemctl restart sshd
# firewall-cmd --zone=public --add-port=18022/tcp --permanent

echo -e "\033[32m准备工作已完成，建议使用前先手动重启服务器。\033[0m"