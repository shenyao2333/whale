package com.whale.provider.common.utils;

/**
 * @Author: shenyao
 * @Date: Created by 2020/12/28 22:01
 * @description: 雪花生成id
 */

/*****
 * 雪花算法根据时间戳生成有序的 64 bit 的 Long 类型的唯一 ID
 *
 * 各 bit 含义：
 *   1 bit: 符号位，0 是正数 1 是负数， ID 为正数，所以恒取 0
 *   41 bit: 时间差，我们可以选择一个参考点，用它来计算与当前时间的时间差 (毫秒数)，41 bit 存储时间差，足够使用 69 年
 *   10 bit: 机器码，能编码 1024 台机器；可以手动指定含义，比如前5 bit 作为机器编号、后 5 bit 作为进程编号
 *   12 bit: 序列号，同一机器同一毫秒内产生不同的序列号，12 bit 可以支持 4096 个序列号
 * 优点：
 * 灵活配置：机器码可以根据需求灵活配置含义
 * 无需持久化：如果序号自增往往需要持久化，本算法不需要持久化
 *  ID 有含义/可逆性：ID 可以反解出来，对 ID 进行统计分析，可以很简单的分析出整个系统的繁忙曲线，还可以定位到每个机器，在某段时间承担了多少工作，分析出负载均衡情况
 * 高性能：生成速度很快
 *
 */
public class SnowflakeId {

    /** 开始时间截 (2019-08-21) */
    private final long twepoch = 1566316801000L;


    /** 机器id所占的位数 */
    private final long workerIdBits = 5L;

    /** 数据标识id所占的位数 */
    private final long datacenterIdBits = 5L;

    /** 支持的最大机器id，结果是31 (这个移位算法可以很快的计算出几位二进制数所能表示的最大十进制数) */
    private final long maxWorkerId = -1L ^ (-1L << workerIdBits);

    /** 支持的最大数据标识id，结果是31 */
    private final long maxDatacenterId = -1L ^ (-1L << datacenterIdBits);

    /** 序列在id中占的位数 */
    private final long sequenceBits = 12L;

    /** 机器ID向左移12位 */
    private final long workerIdShift = sequenceBits;

    /** 数据标识id向左移17位(12+5) */
    private final long datacenterIdShift = sequenceBits + workerIdBits;

    /** 时间截向左移22位(5+5+12) */
    private final long timestampLeftShift = sequenceBits + workerIdBits + datacenterIdBits;

    /** 生成序列的掩码，这里为4095 (0b111111111111=0xfff=4095) */
    private final long sequenceMask = -1L ^ (-1L << sequenceBits);

    /** 工作机器ID(0~31) */
    private long workerId;

    /** 数据中心ID(0~31) */
    private long datacenterId;

    /** 毫秒内序列(0~4095) */
    private long sequence = 0L;

    /** 上次生成ID的时间截 */
    private long lastTimestamp = -1L;

    /**
     * 构造函数
     * @param workerId 工作ID (0~31)
     * @param datacenterId 数据中心ID (0~31)
     */
    public SnowflakeId(long workerId, long datacenterId) {
        if (workerId > maxWorkerId || workerId < 0) {
            throw new IllegalArgumentException(String.format("worker Id can't be greater than %d or less than 0", maxWorkerId));
        }
        if (datacenterId > maxDatacenterId || datacenterId < 0) {
            throw new IllegalArgumentException(String.format("datacenter Id can't be greater than %d or less than 0", maxDatacenterId));
        }
        this.workerId = workerId;
        this.datacenterId = datacenterId;
    }

    /**
     * 获得下一个ID (该方法是线程安全的)
     * @return SnowflakeId
     */
    public synchronized long nextId() {
        long timestamp = timeGen();

        //如果当前时间小于上一次ID生成的时间戳，说明系统时钟回退过这个时候应当抛出异常
        if (timestamp < lastTimestamp) {
            throw new RuntimeException(
                    String.format("Clock moved backwards.  Refusing to generate id for %d milliseconds", lastTimestamp - timestamp));
        }

        //如果是同一时间生成的，则进行毫秒内序列
        if (lastTimestamp == timestamp) {
            sequence = (sequence + 1) & sequenceMask;
            //毫秒内序列溢出
            if (sequence == 0) {
                //阻塞到下一个毫秒,获得新的时间戳
                timestamp = tilNextMillis(lastTimestamp);
            }
        }
        //时间戳改变，毫秒内序列重置
        else {
            sequence = 0L;
        }

        //上次生成ID的时间截
        lastTimestamp = timestamp;

        //移位并通过或运算拼到一起组成64位的ID
        return ((timestamp - twepoch) << timestampLeftShift) //
                | (datacenterId << datacenterIdShift) //
                | (workerId << workerIdShift) //
                | sequence;
    }

    /**
     * 阻塞到下一个毫秒，直到获得新的时间戳
     * @param lastTimestamp 上次生成ID的时间截
     * @return 当前时间戳
     */
    protected long tilNextMillis(long lastTimestamp) {
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen();
        }
        return timestamp;
    }

    /**
     * 返回以毫秒为单位的当前时间
     * @return 当前时间(毫秒)
     */
    protected long timeGen() {
        return System.currentTimeMillis();
    }


    public static Long  getId(){
        return new SnowflakeId(12, 0).nextId();
    }

    /** 测试 */
    public static void main(String[] args) {
        SnowflakeId idWorker = new SnowflakeId(12, 0);
        for (int i = 0; i < 1000; i++) {
            long id = idWorker.nextId();
            //System.out.println(Long.toBinaryString(id));
            System.out.println(id);
        }
    }




}
