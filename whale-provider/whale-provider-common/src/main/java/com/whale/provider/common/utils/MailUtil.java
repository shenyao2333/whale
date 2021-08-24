package com.whale.provider.common.utils;

import com.whale.provider.basices.utils.StrUtil;
import lombok.SneakyThrows;
import javax.activation.DataHandler;
import javax.mail.*;
import javax.mail.internet.*;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.Properties;
/**
 * @Author: sy
 * @Date: Created by 2019.7.13 20:57
 * @description: 邮箱发送工具类
 */
public class MailUtil {

    /**
     * 发件人邮箱
     */
    private static final String SENDER_ADDRESS = "sy91aa@163.com";
    /**
     * 发件人账户名
     */
    private static final String SENDER_ACCOUNT = "sy91aa";
    /**
     * 发件人账户密码
     */
    private static final String SENDER_PASSWORD = "shenyao1995";

    public static void main(String[] args) throws Exception {
        send("1040676712@qq.com","今天邮箱提示","这个是内容的啊", (String) null);
        sendText("1040676712@qq.com","今天邮箱提示","这个是内容的啊");
    }

    /**
     *
     * @param recipient 收件人邮箱
     * @param subject 主题
     * @param content 内容
     * @param url 附件URL列表
     */
    @SneakyThrows
    public static void  send(String recipient, String subject, String content, String ... url) {
        Session session = getTransport();
        session.setDebug(true);
        MimeMessage msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(SENDER_ADDRESS));
        msg.setSubject(subject,"UTF-8");

        /**
         * （可以增加多个收件人、抄送、密送），即下面这一行代码书写多行
         * MimeMessage.RecipientType.TO:发送
         * MimeMessage.RecipientType.CC：抄送
         * MimeMessage.RecipientType.BCC：密送
         */
        msg.setRecipient(MimeMessage.RecipientType.TO,new InternetAddress(recipient));
        MimeMultipart multipart = getBody(content, url);
        msg.setContent(multipart);
        msg.setSentDate(new Date());
        performSend(session ,  msg);
    }

    /**
     *  发送普通文本消息
     * @param recipient 接收人
     * @param subject 邮箱主题
     * @param content 邮箱内容
     * @throws Exception
     */
    @SneakyThrows
    public static void sendText(String recipient, String subject , String content){
        Session session = getTransport();
        session.setDebug(true);
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(SENDER_ADDRESS));
        message.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(recipient));
        message.setSubject(subject);
        message.setContent(content, "text/html;charset=UTF-8");
        performSend(session ,  message);
    }

    private static void performSend(Session session , MimeMessage msg) throws Exception {
        Transport transport = session.getTransport();
        transport.connect(SENDER_ACCOUNT, SENDER_PASSWORD);
        transport.sendMessage(msg,msg.getAllRecipients());
        transport.close();
    }


    private static Session getTransport() {
        Properties props = new Properties();
        props.setProperty("mail.smtp.auth", "true");
        props.setProperty("mail.transport.protocol", "smtp");
        props.setProperty("mail.smtp.host", "smtp.163.com");
        return Session.getInstance(props);
    }

    private static MimeMultipart getBody(String content,String ... urls) {
        //创建附件"节点"
        MimeBodyPart attachment = new MimeBodyPart();
        MimeMultipart mm = new MimeMultipart();
        for (String s : urls) {
            try {
                DataHandler dh  = new DataHandler(new URL(s));
                attachment.setDataHandler(dh);
                // 设置附件的文件名（需要编码）
                attachment.setFileName(MimeUtility.encodeText(dh.getName()));
                mm.addBodyPart(attachment);
            } catch (MalformedURLException | MessagingException | UnsupportedEncodingException e) {
                System.out.println("资源路径错误！！！");
                e.printStackTrace();
            }
        }
        if (StrUtil.isNotBlank(content)){
            //文本内容
            try {
                MimeBodyPart text = new MimeBodyPart();
                text.setContent(content, "text/html;charset=UTF-8");
                mm.addBodyPart(text);
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        }
        return  mm;
    }

}
