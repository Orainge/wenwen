package com.orainge.wenwen.util;

import com.orainge.wenwen.exception.EmailException;
import com.orainge.wenwen.exception.type.EmailError;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Component
public class EmailUtil {
    private final String topicPrefix = WebsiteSettings.WEBSITE_NAME + " | ";
    private final String activateUrlPrefix = WebsiteSettings.WEBSITE_DOMAIN + "/auth/activate/";
    private final String resetPasswordUrlPrefix = WebsiteSettings.WEBSITE_DOMAIN + "/auth/resetPassword/";

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer; //发送邮件的模板引擎

    @Async
    public void sendActivateEmail(String emailAddress, String activeToken) {
        String topic = topicPrefix + "请激活您的账户";
        String url = activateUrlPrefix + activeToken;
        sendEmail(emailAddress, topic, url, 0);
    }

    @Async
    public void sendResetPasswordEmail(String emailAddress, String resetPasswordToken) {
        String topic = topicPrefix + "重置您的账户密码";
        String url = resetPasswordUrlPrefix + resetPasswordToken;
        sendEmail(emailAddress, topic, url, 1);
    }

    private void sendEmail(String emailAddress, String topic, String url, int emailType) {
        System.out.println("邮件中的链接:" + url);
    }

    // 已经测试通过的，正式使用时再开启
    private void sendEmailTrue(String emailAddress, String topic, String url, int emailType) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(new String((WebsiteSettings.EMAIL_NICKNAME + " <" + WebsiteSettings.EMAIL_ADDRESS + ">").getBytes(StandardCharsets.UTF_8)));
            helper.setTo(emailAddress);
            helper.setSubject(topic);
            helper.setText(getContent(emailAddress, url, emailType), true);//重点，默认为false，显示原始html代码，无效果
            mailSender.send(message);
            // 邮件发送完成
        } catch (Exception ex) {
            throw new EmailException(ex, EmailError.SEND);
        }
    }

    private String getContent(String email, String url, int emailType) throws EmailException {
        String content = "";
        String title = "";
        String message = "";

        if (emailType == 0) {
            title = "欢迎加入" + WebsiteSettings.WEBSITE_NAME;
            message = "请点击下面的链接激活您的账户";
        } else if (emailType == 1) {
            title = "重置您的账户密码";
            message = "请点击下面的链接重置您的账户密码";
        } else {
            return content;
        }

        try {
            Map<String, Object> data = new HashMap<>();
            data.put("title", title);
            data.put("email", email);
            data.put("message", message);
            data.put("url", url);
            data.put("indexUrl", WebsiteSettings.WEBSITE_DOMAIN);
            String templateName = "emailTemplate.html";
            Template template = freeMarkerConfigurer.getConfiguration().getTemplate(templateName);
            content = FreeMarkerTemplateUtils.processTemplateIntoString(template, data);
        } catch (Exception e) {
            throw new EmailException(e, "获取模板内容出错");
        }
        return content;
    }
}
