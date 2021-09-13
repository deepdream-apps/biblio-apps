package fr.cmci.biblio.security.util;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import freemarker.template.Template;
import freemarker.template.TemplateException;
@Component
public class EmailSender {
	private Logger logger = Logger.getLogger(EmailSender.class.getName()) ;
	@Autowired
	private FreeMarkerConfigurer freemarkerConfigurer ;
	@Autowired
    private JavaMailSender sender ;
	private ScheduledExecutorService quickService = Executors.newScheduledThreadPool(20) ;
	@Autowired
	private Environment env ;
	 
	
	public void sendMessage(String to, String subject, String templateFile,
			Map<String, Object> templateModel) throws IOException, TemplateException, MessagingException {
	        
	    Template freemarkerTemplate = freemarkerConfigurer.createConfiguration().getTemplate(templateFile) ;
	    String htmlBody = FreeMarkerTemplateUtils.processTemplateIntoString(freemarkerTemplate, templateModel) ;
	 
	    MimeMessage message = sender.createMimeMessage();
	    MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
	    helper.setTo(to);
	    helper.setSubject(subject);
	    helper.setText(htmlBody, true);
	    quickService.submit(new Runnable() {
			@Override
			public void run() {
				try{
					sender.send(message);
				}catch(Exception e){
					logger.log(Level.SEVERE, "Exception occur while send a mail",e) ;
				}
			}
		});
	}
	
	public void sendMessage(String to, String subject, String templateFile, String pathToAttachment,
			 Map<String, Object> templateModel) throws IOException, TemplateException, MessagingException {
		
		Template freemarkerTemplate = freemarkerConfigurer.createConfiguration().getTemplate(templateFile) ;
	    String htmlBody = FreeMarkerTemplateUtils.processTemplateIntoString(freemarkerTemplate, templateModel) ;
	    
		MimeMessage message = sender.createMimeMessage() ;
		MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8") ;
		helper.setFrom(env.getProperty("spring.mail.username")) ;
		helper.setTo(to) ;
		helper.setSubject(subject) ;
		helper.setText(htmlBody, true) ;
		
		File attachment = new  File(pathToAttachment) ;
		FileSystemResource file = new FileSystemResource(attachment) ;
		helper.addAttachment(attachment.getName(), file) ;
		quickService.submit(new Runnable() {
			@Override
			public void run() {
				try{
					sender.send(message) ;
				}catch(Exception e){
					logger.log(Level.SEVERE, "Exception occur while send a mail",e) ;
				}
			}
		});
		
	}
	
}
