package org.glite.security.voms.admin.notification;

import org.apache.commons.lang.StringUtils;
import org.apache.velocity.VelocityContext;

public abstract class AbstractVelocityNotification extends
		VelocityEmailNotification {

	public static final String subjectPrefix = "[VOMS Admin]";
	VelocityContext context;
	String templatePrefix;

	public AbstractVelocityNotification() {
		setTemplatePrefix("/templates");
		context = new VelocityContext();
	}

	public void addToContext(String key, String value) {
		context.put(key, value);
	}

	@Override
	protected void buildMessage() {

		String templateFileName = String.format("/%s/%s.vm",
				getTemplatePrefix(), getClass().getSimpleName());
		setTemplateFile(templateFileName);

		buildMessageFromTemplate(context);

	}

	public String getTemplatePrefix() {
		return templatePrefix;
	}

	public void setTemplatePrefix(String templatePrefix) {
		this.templatePrefix = templatePrefix;
	}

	@Override
	public void setSubject(String subject) {

		super.setSubject(subjectPrefix + " " + subject);
	}

	@Override
	public String toString() {
		return "" + super.toString() + "[subject='" + getSubject()
				+ "',recipients='" + StringUtils.join(getRecipientList(), ",")
				+ "']";
	}

}
