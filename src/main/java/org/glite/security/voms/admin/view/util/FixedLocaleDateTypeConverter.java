package org.glite.security.voms.admin.view.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.apache.struts2.util.StrutsTypeConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensymphony.xwork2.conversion.TypeConversionException;

public class FixedLocaleDateTypeConverter extends StrutsTypeConverter {

	public static final Logger log = LoggerFactory.getLogger(FixedLocaleDateTypeConverter.class);
	
	@Override
	public Object convertFromString(Map context, String[] values, Class toClass) {
		
		String dateString = values[0];
		log.debug("Converting from String {} to date.",dateString);
		Date date = parseDate(dateString);
		return date;
	}

	@Override
	public String convertToString(Map context, Object o) {
		
		// FIXME: should parse BaseAction.properties for this!
		DateFormat formatter = new SimpleDateFormat("MM/dd/yy");
		Date d = (Date)o;
		
		log.debug("Converting from Date {} to string.",d.toString());
		
		return formatter.format(d);
	}
	
	Date parseDate(String dateString){
		
		// FIXME: should parse BaseAction.properties for this!
		DateFormat formatter = new SimpleDateFormat("MM/dd/yy");
		try {
			Date date = formatter.parse(dateString);
			return date;
		} catch (ParseException e) {
			log.warn("Error parsing date: {}", dateString);
			log.debug(e.getMessage(), e);
			throw new TypeConversionException("Error parsing date: "+dateString, e);
		}
		
	}

}
