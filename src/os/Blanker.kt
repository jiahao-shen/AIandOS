package os

import java.util.*

fun main(args: Array<String>) {

    val scan = Scanner(System.`in`)

    val numOfProcess: Int      //进程个数
    val numOfResource: Int      //资源个数

    lateinit var available: Array<Int>      //可利用资源
    lateinit var allocation: Array<Array<Int>>      //已经分配的资源矩阵
    lateinit var need: Array<Array<Int>>        //需求矩阵

    println("请输入进程个数n和资源个数m:")     //输入进程和资源个数
    numOfProcess = scan.nextInt()
    numOfResource = scan.nextInt()

    //矩阵初始化
    available = Array(numOfResource, { _ -> -1 })
    allocation = Array(numOfProcess, { _ -> Array(numOfResource, { _ -> -1 }) })
    need = Array(numOfProcess, { _ -> Array(numOfResource, { _ -> -1 }) })

    //判断向量a是否小于等于向量b
    fun isLessOrEqual(a: Array<Int>, b: Array<Int>): Boolean {
        for (index in a.indices) {
            if (a[index] > b[index])
                return false
        }
        return true
    }

    //安全性检查
    fun checkSafety(): Boolean {
        val work = Array(numOfResource, { _ -> -1 })        //work向量
        val finish = Array(numOfProcess, { _ -> false })       //finish标志
        for (j in 0 until numOfResource) {      //复制available
            work[j] = available[j]
        }
        val safetyCollection = ArrayList<Int>()     //安全序列
        while (true) {
            var flag = false        //标志位
            for (i in 0 until numOfProcess) {
                if (!finish[i] && isLessOrEqual(need[i], work)) {       //找到对应的资源
                    for (j in 0 until numOfResource) {
                        work[j] += allocation[i][j]     //将资源i的资源归还
                    }
                    finish[i] = true    //资源i已经归还
                    safetyCollection.add(i)     //往安全序列中添加资源i
                    flag = true     //找到了这样一个资源
                    break       //跳出循环
                }
            }
            if (!flag) {        //没有找到这样一个资源
                for (i in 0 until numOfProcess) {
                    if (!finish[i]) {       //如果所有的finish都是false
                        println("不安全!")
                        return false        //返回false
                    }
                }
                print("安全序列:")     //否则打印安全序列
                for (item in safetyCollection) {        //打印
                    print("$item ")
                }
                println()
                return true
            }
        }
    }

    println("输入可利用资源向量:")      //输入可利用资源向量
    for (i in 0 until numOfResource)
        available[i] = scan.nextInt()
    println("输入已分配资源矩阵:")     //输入已分配向量
    for (i in 0 until numOfProcess)
        for (j in 0 until numOfResource)
            allocation[i][j] = scan.nextInt()
    println("输入需求矩阵:")       //输入需求矩阵
    for (i in 0 until numOfProcess)
        for (j in 0 until numOfResource)
            need[i][j] = scan.nextInt()

    checkSafety()       //安全性检查

    while (true) {      //开启循环不断请求资源
        println("输入请求资源的进程下标,输入-1退出:")       //输入请求的进程下标
        val i = scan.nextInt()
        if (i == -1) {
            break       //输入-1则程序结束
        } else {
            if (i >= numOfProcess) {
                println("请输入正确的进程序号")
                continue
            }
            println("输入请求的资源数量:")        //输入请求向量request
            val request = Array(numOfResource, { _ -> -1 })
            for (j in 0 until numOfResource)
                request[j] = scan.nextInt()
            if (isLessOrEqual(request, need[i])) {      //如果request小约等于need[i]
                if (isLessOrEqual(request, available)) {        //如果request小于等于available
                    for (j in 0 until numOfResource) {      //先分配资源
                        available[j] -= request[j]      //可利用资源减少
                        allocation[i][j] += request[j]      //已分配资源增加
                        need[i][j] -= request[j]        //需求资源减少
                    }
                    if (!checkSafety()) {       //如果不安全
                        println("请求失败")       //请求资源失败
                        for (j in 0 until numOfResource) {      //归还资源
                            available[j] += request[j]
                            allocation[i][j] -= request[j]
                            need[i][j] += request[j]
                        }
                    } else {        //申请成功
                        println("请求成功")
                    }
                } else {        //资源不够,需要等待
                    println("资源不够,请等待")
                }
            } else {        //所需资源已超过需求,返回错误
                println("请求错误")
            }
        }
    }
}
